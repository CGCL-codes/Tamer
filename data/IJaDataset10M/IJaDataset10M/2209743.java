package org.zeroexchange.web.navigation.menu.populator;

import org.springframework.beans.factory.annotation.Autowired;
import org.zeroexchange.exception.BusinessLogicException;
import org.zeroexchange.web.navigation.menu.MenuBuilder;
import org.zeroexchange.web.navigation.menu.item.PageMenuItemFactory;
import org.zeroexchange.web.page.About;
import org.zeroexchange.web.page.auth.Login;
import org.zeroexchange.web.page.auth.Logout;

/**
 * Populator that populates menu with the default menu items.
 *  
 * @author black
 */
public class DefaultMenuPopulator implements MenuPopulator {

    @Autowired
    private PageMenuItemFactory pageMenuItemFactory;

    private int aboutOrder = -1;

    private int loginOrder = 100;

    private int logoutOrder = 1000;

    public void setAboutOrder(int aboutOrder) {
        this.aboutOrder = aboutOrder;
    }

    public void setLoginOrder(int loginOrder) {
        this.loginOrder = loginOrder;
    }

    public void setLogoutOrder(int logoutOrder) {
        this.logoutOrder = logoutOrder;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void populate(MenuBuilder menuBuilder) throws BusinessLogicException {
        menuBuilder.insert(pageMenuItemFactory.getMenuItem(About.class).setOrder(aboutOrder));
        menuBuilder.insert(pageMenuItemFactory.getMenuItem(Login.class).setOrder(loginOrder));
        menuBuilder.insert(pageMenuItemFactory.getMenuItem(Logout.class).setOrder(logoutOrder));
    }
}
