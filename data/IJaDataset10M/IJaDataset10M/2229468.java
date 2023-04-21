package org.formaria.aria;

import java.util.Hashtable;
import java.util.Stack;
import java.util.Vector;
import org.formaria.debug.DebugLogger;
import org.formaria.aria.build.BuildProperties;
import org.formaria.aria.validation.Validator;

/**
 * A class for loading and displaying Pages. This class manages access to individual pages and coordinates update of the
 * dispaly area. Pages can also be displayed as part of a frameset. The page manager caches the pages to improve
 * performance and maintain state.
 * <p>
 * Copyright (c) Formaria Ltd., 2002-2003
 * </p>
 * <p>
 * License: see license.txt
 * </p>
 * $Revision: 2.16 $
 */
public class PageManager {

    private static final int CACHE_IGNORE = 0;

    private static final int CACHE_SAVE = 1;

    private static final int CACHE_CHECK_CLASS = 2;

    /**
     * Hashtable cache of used Page Objects
     */
    protected Hashtable pages;

    /**
     * The component package being used in this Project instance (AWT or Swing)
     */
    protected String packageName;

    /**
     * The pageHistory Stack used to go 'back'
     */
    protected Stack pageHistory;

    /**
     * PageDisplay interface for showing pages
     */
    protected PageDisplay pageDisplay;

    /**
     * Used to load pages from XML definition
     */
    protected Vector secondaryLoaders;

    /**
     * The owner project and the context in which this object operates.
     */
    protected Project currentProject;

    /**
     * A flag that controls whether or not validations are triggered on page transition
     */
    protected boolean triggerValidations;

    protected Hashtable pageNameMap;

    /**
     * Constructor which creates a hastable to store the pages
     *
     * @param project
     *            the owner project
     */
    public PageManager(Project project) {
        currentProject = project;
        secondaryLoaders = new Vector();
        triggerValidations = false;
        String tv = currentProject.getStartupParam("TriggerValidations");
        if ((tv != null) && tv.equals("true")) {
            triggerValidations = true;
        }
        pageNameMap = new Hashtable();
        reset();
    }

    /**
     * Set the package name.
     *
     * @param pkgName
     *            the name of the package we're running from.
     */
    public void setPackageName(String pkgName) {
        if (pkgName != null && pkgName.length() > 0) {
            if (pkgName.endsWith(".")) {
                pkgName = pkgName.substring(0, pkgName.length() - 1);
            }
        }
        packageName = pkgName;
    }

    /**
     * Set the interface to invoke when a page has been displayed. Reads the startup parameters Frames and UsesFrames to
     * control loading of the frame set. If Frames is not specified it defaults to "frames", and if UsesFrames is not
     * specified it defaults to "true".
     *
     * @param pgDisplay
     *            The PageDisplay interface
     */
    public void setPageDisplay(PageDisplay pgDisplay) {
        pageDisplay = pgDisplay;
    }

    /**
     * Load a page. Check first to see if we have it in the Hashtable
     *
     * @param className
     *            the name of the class we want to load. If the new page is a dialog or a dialog derivative it is not
     *            cached
     * @return The created or cached page
     */
    public PageSupport loadPage(String className) {
        return doLoadPage(null, className, CACHE_CHECK_CLASS);
    }

    /**
     * Load a page. Check first to see if we have it in the Hashtable when the cache flag is set to true.
     *
     * @param className
     *            the name of the class we want to load.
     * @param cache
     *            whether or not to cache the page, or check the class being cached
     * @return the cached page or the newly created page.
     */
    public PageSupport loadPage(String className, boolean cache) {
        return doLoadPage(null, className, cache ? CACHE_SAVE : CACHE_IGNORE);
    }

    /**
     * Load a page and cache it under a name other than its page name. Check first to see if we have it in the Hashtable
     * when the cache flag is set to true.
     *
     * @param cacheName
     *            the name by which the page will be known in the cache
     * @param className
     *            the name of the class we want to load.
     * @param cache
     *            whether or not to cache the page, or check the class being cached
     * @return the cached page or the newly created page.
     */
    public PageSupport loadPageAs(String cacheName, String className, boolean cache) {
        return doLoadPage(cacheName, className, cache ? CACHE_SAVE : CACHE_IGNORE);
    }

    /**
     * Load a page. Check first to see if we have it in the Hashtable when the cache flag is set to true.
     *
     * @param cacheName
     *            the name by which the page will be known in the cache
     * @param className
     *            the name of the class we want to load.
     * @param cache
     *            whether or not to cache the page or check the cache
     * @return the cached page or the newly created page.
     */
    private PageSupport doLoadPage(String cacheName, String className, int cache) {
        className = lookupPageName(className);
        ProjectManager.setCurrentProject(currentProject);
        PageSupport newPage = null;
        String pagePackage = packageName;
        int pos = className.lastIndexOf('.');
        if (pos > 0) {
            pagePackage = className.substring(0, pos);
        }
        String pageName = className.substring(pos + 1);
        if ((pagePackage != null) && (pagePackage.length() > 1)) {
            pageName = pagePackage + (pagePackage.endsWith(".") ? "" : ".") + pageName;
        }
        if (cacheName == null) {
            cacheName = pageName;
        }
        if (cache != CACHE_IGNORE) {
            newPage = (PageSupport) pages.get(cacheName);
        }
        if (newPage == null) {
            int numLoaders = secondaryLoaders.size();
            for (int i = 0; i < numLoaders; i++) {
                if (secondaryLoaders != null) {
                    PageLoader secondaryLoader = (PageLoader) secondaryLoaders.elementAt(i);
                    try {
                        newPage = secondaryLoader.loadPage(pagePackage, className, false);
                    } catch (Exception e) {
                    }
                    if (newPage != null) {
                        if (cache == CACHE_CHECK_CLASS) {
                            cache = CACHE_SAVE;
                        }
                        if (cache == CACHE_SAVE) {
                            pages.put(cacheName, newPage);
                        }
                        break;
                    }
                }
            }
            if (newPage == null) {
                try {
                    newPage = (PageSupport) Class.forName(pageName.trim()).newInstance();
                } catch (Exception e) {
                    logPageCreationError(className, "Unable to create the page: " + pageName, e);
                    String defClass = currentProject.getStartupParam("DefaultClass");
                    if (defClass == null) {
                        newPage = new Page();
                    } else {
                        try {
                            Class clazz = Class.forName(defClass.trim());
                            newPage = (PageSupport) clazz.newInstance();
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                }
                if (newPage != null) {
                    if (cache == CACHE_CHECK_CLASS) {
                        cache = CACHE_SAVE;
                    }
                    if (cache == CACHE_SAVE) {
                        pages.put(cacheName, newPage);
                    }
                }
            }
            newPage.setName(className);
            newPage.setStatus(Page.CREATED);
            try {
                newPage.pageCreated();
            } catch (Exception ex) {
                logPageCreationError(className, "The pageCreated method could not be invoked due to an exception", ex);
            }
        }
        return newPage;
    }

    /**
     * Remove a page from page cache.
     *
     * @param className
     *            the name of the class we want to remove.
     */
    public void unloadPage(String className) {
        int pos = className.lastIndexOf('.');
        String pagePackage = packageName;
        String pageName = className.substring(pos + 1);
        if ((pagePackage != null) && (pagePackage.length() > 1)) {
            pageName = pagePackage + (pagePackage.endsWith(".") ? "" : ".") + pageName;
        }
        pages.remove(pageName);
    }

    /**
     * Log the page creation error message
     *
     * @param message
     *            the error message
     * @param e
     *            the exception
     */
    protected void logPageCreationError(String className, String message, Exception e) {
        DebugLogger.logError(message);
        if (BuildProperties.DEBUG) {
            e.printStackTrace();
        }
    }

    /**
     * Get a page
     *
     * @param className
     *            the page name
     * @return the page or else null if the page hasn't been loaded yet.
     */
    public PageSupport getPage(String className) {
        String pagePackage = packageName;
        int pos = className.lastIndexOf('.');
        if (pos > 0) {
            pagePackage = className.substring(0, pos);
        }
        String pageName = className.substring(pos + 1);
        if ((pagePackage != null) && (pagePackage.length() > 1)) {
            pageName = pagePackage + (pagePackage.endsWith(".") ? "" : ".") + pageName;
        }
        return (PageSupport) pages.get(pageName);
    }

    /**
     * Get the page currently displayed by a particular target container
     *
     * @param target
     *            the target container name
     * @return the page or else null if the page hasn't been loaded yet.
     */
    public PageSupport getCurrentPage(String target) {
        PageSupport page = null;
        if (!pageHistory.empty()) {
            String className = (String) pageHistory.peek();
            if (className != null) {
                page = loadPage(className);
            }
        }
        return page;
    }

    /**
     * Get the page previously displayed by the 'content' target container
     *
     * @return the page or else null if there wasn't a previous page
     */
    public PageSupport getPreviousPage() {
        int historyLen = pageHistory.size();
        if (historyLen > 1) {
            return getPage((String) pageHistory.elementAt(historyLen - 2));
        }
        return null;
    }

    /**
     * Load and show a page.
     *
     * @param className
     *            The name, without package info, of the page to be displayed
     * @return the page being displayed
     */
    public PageSupport showPage(String className) {
        return showPage(className, null, null);
    }

    /**
     * Load and show a page.
     *
     * @param className
     *            The name, without package info, of the page to be displayed
     * @param target
     *            the area to update
     * @return the page being displayed
     */
    public PageSupport showPage(String className, String target) {
        return showPage(className, target, null);
    }

    /**
     * Load and show a page.
     *
     * @param className
     *            The name, without package info, of the page to be displayed
     * @param target
     *            the area to update
     * @param attribs
     *            attributes for use by the new page / taregt
     * @return the page being displayed
     */
    public PageSupport showPage(String className, String target, Hashtable attribs) {
        if (!checkValidations(target)) {
            return null;
        }
        PageSupport page = loadPage(className);
        if (page == null) {
            page = loadPage("ErrorPage");
        } else {
            pageHistory.push(className);
        }
        return (PageSupport) pageDisplay.displayPage(page, target, attribs);
    }

    /**
     * Show the previously displayed page.
     *
     * @return The previous Page
     */
    public PageSupport showPrevious() {
        if (!checkValidations(null)) {
            return null;
        }
        if (pageHistory.size() > 1) {
            pageHistory.pop();
        }
        if (!pageHistory.empty()) {
            try {
                String className = (String) pageHistory.peek();
                if (className != null) {
                    PageSupport page = loadPage(className);
                    pageDisplay.displayPage(page);
                    return page;
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return null;
    }

    /**
     * Load and show a dialog.
     *
     * @param parent
     *            the parent of this dialog or null.
     * @param className
     *            The name, without package info, of the page to be displayed
     * @param title
     *            the the dialog title
     * @param x
     *            the x coordinate, or -1 for ignore/default location. Some widget set implementations may also support
     *            values of -2 for centered on screen
     * @param y
     *            the location of the dialog on screen location is to be used. To centre the dialog on the mouse
     *            location, if the dialog is displayed in response to say a mouse event, the following code can be
     *            added:
     *
     *            <pre>
     * &lt;code&gt;
     *      Point pt = (( MouseEvent )getCurrentEvent() ).getPoint();
     *      Point topLeft = myObject.getLocationOnScreen();
     *      pt.translate( topLeft.x, topLeft.y );
     *      pageMgr.showDialog( &quot;MyDialog&quot;, translate( &quot;MYDIALOG_TITLE&quot; ), pt.x, pt.y );
     * &lt;/code&gt;
     * </pre>
     * @return the page being displayed
     */
    public PageSupport showDialog(Object parent, String className, String title, int x, int y) {
        DialogSupport popupDialog = (DialogSupport) loadPage(className, false);
        popupDialog.setCaption(title);
        popupDialog.pack();
        popupDialog.showDialog(parent);
        return popupDialog;
    }

    /**
     * Check that the validations pass, provided that the validateOnPageTransition flag is set to true
     *
     * @param target
     *            the target container name
     * @return false if the validations were checked and found to fail, otherwise true
     */
    protected boolean checkValidations(String target) {
        if (triggerValidations) {
            PageSupport ps = getCurrentPage(target);
            if (ps != null) {
                int ret = ps.checkValidations();
                if (ret != Validator.LEVEL_IGNORE) {
                    return false;
                }
            }
        }
        return true;
    }

    public void setTriggerValidations(boolean state) {
        triggerValidations = state;
    }

    /**
     * Set the page loader class. AriaBuilder is an example of a secondary page loader that loads pages from an XML
     * description.
     *
     * @param pl
     *            the page loader instance
     */
    public void addSecondaryLoader(PageLoader pl) {
        secondaryLoaders.addElement(pl);
    }

    /**
     * Add the page to the pageHistory stack
     *
     * @param className
     *            The name, without package info, of the page to be displayed
     */
    public void addHistory(String className) {
        pageHistory.push(className);
    }

    /**
     * Reset the page history and dump any loaded pages.
     */
    public void reset() {
        pages = new Hashtable(3);
        pageHistory = new Stack();
    }

    /**
     * Check the page name lookup for a page name substitution. For example a frameset might specify the startup page as
     * HOME, only to have it remapped on startup to 'Welcome' or whatever is appropriate at the time. The
     * XLifeCycleListener.initialize method may be used to set such parameters at startup
     *
     * @param name
     *            the key
     * @return the mapped name
     */
    private String lookupPageName(String name) {
        String pageName = (String) pageNameMap.get(name);
        if (pageName == null) {
            return name;
        }
        return pageName;
    }

    /**
     * Set the page name used for a particular key. For example a frameset might specify the startup page as HOME, only
     * to have it remapped on startup to 'Welcome' or whatever is appropriate at the time. The
     * XLifeCycleListener.initialize method may be used to set such parameters at startup
     *
     * @param key
     *            the lookup key
     * @param pageName
     *            the mapped name
     */
    public void mapPageName(String key, String pageName) {
        pageNameMap.put(key, pageName);
    }
}
