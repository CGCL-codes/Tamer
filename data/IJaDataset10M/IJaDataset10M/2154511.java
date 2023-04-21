package org.zeroexchange.web.navigation.menu.item;

import org.zeroexchange.exception.BusinessLogicException;

/**
 * The factory provides MenuITem instances.
 * 
 * @author black
 */
public interface PageMenuItemFactory {

    /**
     * Returns the menu item by the page class.
     */
    MenuItem getMenuItem(Class pageClass) throws BusinessLogicException;
}
