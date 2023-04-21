package com.antilia.demo.picviewer.viewer.nav;

import com.antilia.web.button.IMenuItemHolder;
import com.antilia.web.button.IMenuItemsFactory;
import com.antilia.web.button.SeparatorButton;

/**
 * 
 * @author Ernesto Reinaldo Barreiro (reiern70@gmail.com)
 */
public class NavigationItemsFactory implements IMenuItemsFactory {

    private static final long serialVersionUID = 1L;

    private static NavigationItemsFactory instance;

    private NavigationItemsFactory() {
    }

    @SuppressWarnings("unchecked")
    public void populateMenuItems(String menuId, IMenuItemHolder itemHolder) {
        itemHolder.addMenuItem(new SeparatorButton());
        itemHolder.addMenuItem(new FirstElementButton());
        itemHolder.addMenuItem(new PreviousElementButton());
        itemHolder.addMenuItem(new ElementNumberItem());
        itemHolder.addMenuItem(new NextElementButton());
        itemHolder.addMenuItem(new LastElementButton());
    }

    /**
	 * @return the instance
	 */
    public static NavigationItemsFactory getInstance() {
        if (instance == null) instance = new NavigationItemsFactory();
        return instance;
    }
}
