package net.sf.tacos.services;

import java.util.List;

/**
 * This class reads the sitemap configuration, and provides access to
 * basic relationship information, such as page categories, bread crumbs, and
 * other information, that the application may need to ease navigation.
 *
 * @author Gabriel Handford
 */
public interface SiteMap {

    /**
     * Get list of named categories.
     * @return The category listing
     */
    public List getCategories();

    /**
     * Get category info for named category.
     * @param name The category name
     * @return The category info
     */
    public CategoryInfo getCategoryInfo(String name);

    /**
     * Get page information.
     * @param name The page name
     * @return The page descriptor
     */
    public PageInfo getPageInfo(String name);

    /**
     * Get the category info for the page name.
     * @param pageName The page name.
     * @return The first category associated with this page.
     */
    public CategoryInfo getCategoryFromPage(String pageName);

    /**
     * Check if page name is in the specified category.
     * @param pageName Page name
     * @param category Category
     * @return True if in the specified category
     */
    public boolean inCategory(String pageName, String category);

    /**
     * Get the default page description.
     * @param category Category
     * @return Category default page description
     */
    public String getDefaultPageDesc(String category);

    /**
     * Get pages for a specific page names category.
     * @param pageName Page name
     * @return Pages
     */
    public List getCategoryPages(String pageName);

    /**
     * Get the default page.
     * @param category Category
     * @return Default page
     */
    public PageInfo getDefaultPage(String category);

    /**
     * Check if page name is contained in the page element tree.
     * @param parent The page to start at.
     * @param pageName The page to find.
     * @return True if the parent contains the page in its tree.
     */
    public boolean contains(String parent, String pageName);

    /**
     * Get bread crumbs.
     * @param pageName The page name.
     * @return The bread crumbs (page name list).
     */
    public List getBreadCrumbs(String pageName);
}
