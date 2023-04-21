package org.zeroexchange.web.navigation.menu;

import java.util.List;
import org.zeroexchange.web.navigation.menu.model.MenuItem;

/**
 * @author black
 *
 */
public interface MenuService {

    /**
     * Filters specified list according current user's permissions.
     */
    List<MenuItem> filterAccordingPermissions(List<MenuItem> items);

    /**
     * Returns top menu items.
     */
    List<MenuItem> getTopItems();

    /**
     * Returns submenu of the current menu.
     */
    List<MenuItem> getSubmenu(String key);

    /**
     * Safely returns the menu key.
     */
    String getKey(MenuItem menuItem);

    /**
     * Safely returns the menu image.
     */
    String getImage(MenuItem menuItem);

    /**
     * Returns the title of the menu item.
     */
    String getTitle(MenuItem menuItem);
}
