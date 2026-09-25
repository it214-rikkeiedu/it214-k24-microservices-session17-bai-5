package com.grabfood.fallback.service;

import com.grabfood.fallback.entity.MenuItem;
import com.grabfood.fallback.repository.MenuItemRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class MenuItemService {

    private static final Logger log = LoggerFactory.getLogger(MenuItemService.class);

    @Autowired
    private MenuItemRepository menuItemRepository;

    @Cacheable(value = "menuItems", key = "#id")
    public MenuItem getMenuItemById(Long id) {
        log.info("Executing database query to fetch MenuItem with ID: {}", id);
        return menuItemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("MenuItem not found with ID: " + id));
    }

    @CachePut(value = "menuItems", key = "#menuItem.id")
    public MenuItem updateMenuItem(MenuItem menuItem) {
        log.info("Executing database update for MenuItem with ID: {}", menuItem.getId());
        return menuItemRepository.save(menuItem);
    }

    @CacheEvict(value = "menuItems", key = "#id")
    public void deleteMenuItem(Long id) {
        log.info("Executing database delete for MenuItem with ID: {}", id);
        menuItemRepository.deleteById(id);
    }

    public MenuItem createMenuItem(MenuItem menuItem) {
        log.info("Executing database save for a new MenuItem");
        return menuItemRepository.save(menuItem);
    }
}