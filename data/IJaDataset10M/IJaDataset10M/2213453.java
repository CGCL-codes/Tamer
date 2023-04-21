package org.zeroexchange.web.navigation.menu;

import java.util.ArrayList;
import java.util.List;
import java.util.MissingResourceException;
import javax.xml.bind.JAXBContext;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.Application;
import org.apache.wicket.Localizer;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.util.Assert;
import org.zeroexchange.exception.BusinessLogicException;
import org.zeroexchange.web.navigation.menu.decorator.MenuDecoratorsFactory;
import org.zeroexchange.web.navigation.menu.model.Leaf;
import org.zeroexchange.web.navigation.menu.model.Menu;
import org.zeroexchange.web.navigation.menu.model.MenuItem;
import org.zeroexchange.web.navigation.menu.model.Submenu;
import org.zeroexchange.web.navigation.target.filter.TargetFilter;

/**
 * @author black
 *
 */
public class DefaultMenuService implements MenuService, InitializingBean {

    private static final Log log = LogFactory.getLog(DefaultMenuService.class);

    @Autowired
    private TargetFilter targetFilter;

    @Autowired
    private MenuDecoratorsFactory menuDecoratorsFactory;

    private Resource menuDescriptionResource;

    private Menu menu;

    private String imageExtension = ".png";

    /**
     * @param imageExtension the imageExtension to set
     */
    public void setImageExtension(String imageExtension) {
        this.imageExtension = imageExtension;
    }

    /**
     * @param menuDescriptionResource the menuDescriptionResource to set.
     */
    public void setMenuDescriptionResource(Resource menuDescriptionResource) {
        this.menuDescriptionResource = menuDescriptionResource;
    }

    @Override
    public List<MenuItem> filterAccordingPermissions(List<MenuItem> items) {
        if (items == null) {
            return null;
        }
        List<MenuItem> resultList = new ArrayList<MenuItem>();
        for (MenuItem menuItem : items) {
            if (isMenuItemAllowed(menuItem)) {
                resultList.add(menuItem);
            }
        }
        return resultList;
    }

    @Override
    public List<MenuItem> getTopItems() {
        if (menu == null) {
            JAXBContext jaxbContext;
            try {
                jaxbContext = JAXBContext.newInstance(Menu.class);
                menu = (Menu) jaxbContext.createUnmarshaller().unmarshal(menuDescriptionResource.getInputStream());
            } catch (Exception e) {
                new BusinessLogicException("Cannot load menu description", e);
            }
        }
        return menu.getMenuItems();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(menuDescriptionResource);
    }

    @Override
    public List<MenuItem> getSubmenu(String key) {
        List<MenuItem> topMenu = getTopItems();
        for (MenuItem topMenuItem : topMenu) {
            if (topMenuItem instanceof Submenu) {
                Submenu submenu = (Submenu) topMenuItem;
                List<MenuItem> leftMenuCandidate = submenu.getMenuItems();
                if (getKey(topMenuItem).equals(key) || findItemByKey(leftMenuCandidate, key) != null) {
                    return leftMenuCandidate;
                }
            }
        }
        return null;
    }

    /**
     * Finds item by the key
     */
    protected MenuItem findItemByKey(List<MenuItem> items, String key) {
        if (items != null) {
            for (MenuItem item : items) {
                if (getKey(item).equals(key)) {
                    return item;
                }
                if (item instanceof Submenu) {
                    List<MenuItem> subItems = ((Submenu) item).getMenuItems();
                    MenuItem submenuItem = findItemByKey(subItems, key);
                    if (subItems != null) {
                        return submenuItem;
                    }
                }
            }
        }
        return null;
    }

    /**
     * Returns true if the menu item is allowed for the current user.
     */
    protected boolean isMenuItemAllowed(MenuItem menuItem) {
        if (menuItem instanceof Leaf) {
            return targetFilter.isAllowed(((Leaf) menuItem).getPageClass());
        } else if (menuItem instanceof Submenu) {
            Submenu submenu = (Submenu) menuItem;
            for (MenuItem item : submenu.getMenuItems()) {
                if (isMenuItemAllowed(item)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getKey(MenuItem menuItem) {
        String key = menuItem.getKey();
        if (key == null && menuItem instanceof Leaf) {
            Class pageClass = ((Leaf) menuItem).getPageClass();
            if (pageClass != null) {
                return StringUtils.uncapitalize(pageClass.getSimpleName());
            }
        }
        return key;
    }

    @Override
    public String getImage(MenuItem menuItem) {
        String image = menuItem.getImage();
        if (image == null && menuItem instanceof Leaf) {
            Class pageClass = ((Leaf) menuItem).getPageClass();
            if (pageClass != null) {
                return StringUtils.uncapitalize(pageClass.getSimpleName()) + imageExtension;
            }
        }
        return image;
    }

    @Override
    public String getTitle(MenuItem menuItem) {
        Localizer localizer = Application.get().getResourceSettings().getLocalizer();
        String title = null;
        try {
            title = localizer.getString("menutitle." + getKey(menuItem), null);
        } catch (MissingResourceException e) {
            title = localizer.getString("pagetitle." + getKey(menuItem), null);
        }
        String decoratorKey = menuItem.getDecorator();
        if (decoratorKey != null) {
            title = menuDecoratorsFactory.decorateTitle(title, decoratorKey);
        }
        return title;
    }
}
