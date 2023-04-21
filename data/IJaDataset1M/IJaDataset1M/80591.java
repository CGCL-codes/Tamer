package net.sf.tacos.components;

import java.util.List;
import net.sf.tacos.services.CategoryInfo;
import net.sf.tacos.services.SiteMap;
import org.apache.tapestry.BaseComponent;

/**
 * Useful methods for generating a site map.
 * @author andyhot
 */
public abstract class SiteMapComponent extends BaseComponent {

    public abstract SiteMap getSiteMap();

    public abstract String getName();

    public abstract String getCategory();

    public abstract boolean getShowAll();

    public abstract boolean getIgnoreCurrentPage();

    /**
     * If the sitemap should be shown.
     */
    public boolean getShowSiteMap() {
        return getIgnoreCurrentPage() || (getCurrentPageCategory() != null);
    }

    /**
     * The category where the given page belongs.
     */
    public CategoryInfo getCategoryOf(String page) {
        return getSiteMap().getCategoryFromPage(page);
    }

    /**
     * The category of the currently displaying page.
     */
    public CategoryInfo getCurrentPageCategory() {
        return getCategoryOf(getPage().getPageName());
    }

    /**
     * If the given page is part of the currently rendering category.
     */
    public boolean isPageInCategory(String page) {
        return getSiteMap().inCategory(page, getCategory());
    }

    /**
     * If the currently displaying page is part of the currently rendering category.
     */
    public boolean isCurrentPageInCategory() {
        return isPageInCategory(getPage().getPageName());
    }

    /**
     * The default page for the currently rendering category.
     */
    public String getDefaultPage() {
        return getSiteMap().getDefaultPage(getCategory()).getName();
    }

    /**
     * The css class to be applied for the currently rendering category.
     * It is 'current' if the category in question contains the displaying page.
     */
    public String getCategoryClass() {
        return isCurrentPageInCategory() ? "current" : null;
    }

    /**
     * The display name for the currently rendering category.
     */
    public String getCategoryDisplayName() {
        if (getShowAll()) return getSiteMap().getCategoryInfo(getCategory()).getName(); else return getSiteMap().getDefaultPageDesc(getCategory());
    }

    /**
     * The display name for the currently rendering page.
     */
    public String getPageDisplayName() {
        return getSiteMap().getPageInfo(getName()).getDesc();
    }

    /**
     * Gets the pages that belong to the same category as the given one.
     */
    public List getPagesOfCategoryOfPage(String page) {
        return getSiteMap().getCategoryPages(page);
    }

    /**
     * Gets the pages that belong to the same category as the currently displaying one.
     */
    public List getPagesOfCategoryOfCurrentPage() {
        return getPagesOfCategoryOfPage(getPage().getPageName());
    }

    /**
     * Gets the pages of the given category.
     */
    public List getPagesOfCategory(String category) {
        return getSiteMap().getCategoryInfo(category).getPageNames();
    }

    /**
     * Gets the pages of the currently rendering category.
     */
    public List getPagesOfCurrentCategory() {
        return getPagesOfCategory(getCategory());
    }

    public boolean getShowPage() {
        if (getShowAll()) return true; else return !getName().equals(getCategoryOf(getPage().getPageName()).getDefaultPage());
    }

    /**
     * The css class to be applied for the currently rendering page.
     * It is 'here' if the page in question is in fact the one currently displayed.
     */
    public String getPageClass() {
        String name = getName();
        String pageName = getPage().getPageName();
        boolean selected = name.equals(pageName) || getSiteMap().contains(name, pageName);
        return selected ? "here" : null;
    }
}
