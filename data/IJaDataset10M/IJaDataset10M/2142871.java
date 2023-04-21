package xbrowser;

import xbrowser.bookmark.XBookmarkManager;
import xbrowser.cache.XCacheManager;
import xbrowser.cookie.XCookieManager;
import xbrowser.history.XHistoryManager;
import xbrowser.logger.XLogManager;
import xbrowser.plugin.XPluginManager;
import xbrowser.plugin.XPluginUIManager;
import xbrowser.util.XResourceManager;
import xbrowser.util.XScreenManager;
import xbrowser.widgets.XComponentBuilder;

/**
 * Application-wide store for singleton objects.
 */
public final class XRepository {

    /**
	 * Use to create application's component.
	 */
    public static XComponentBuilder getComponentBuilder() {
        return compBuilder;
    }

    /**
	 * Use to retrieve application's resource strings.
	 */
    public static XResourceManager getResourceManager() {
        return resourceManager;
    }

    /**
	 * Use to log application's message.
	 */
    public static XLogManager getLogger() {
        return logManager;
    }

    /**
	 * Use to manage plugins.
	 */
    public static XPluginManager getPluginManager() {
        return pluginManager;
    }

    /**
	 * Use to manage plugin's UI.
	 */
    public static XPluginUIManager getPluginUIManager() {
        return pluginUIManager;
    }

    /**
	 * Use to work with history.
	 */
    public static XHistoryManager getHistoryManager() {
        return historyManager;
    }

    /**
	 * Use to work with bookmarks.
	 */
    public static XBookmarkManager getBookmarkManager() {
        return bookmarkManager;
    }

    /**
	 * Use to work with cookies.
	 */
    public static XCookieManager getCookieManager() {
        return cookieManager;
    }

    /**
	 * Use to work with screens.
	 */
    public static XScreenManager getScreenManager() {
        return screenManager;
    }

    /**
	 * Use to retrieve application configuration.
	 */
    public static XProjectConfig getConfiguration() {
        return projectConfig;
    }

    /**
	 * Use to work with cache indexes.
	 */
    public static XCacheManager getCacheManager() {
        return cacheManager;
    }

    private static XResourceManager resourceManager = null;

    private static XComponentBuilder compBuilder = null;

    private static XLogManager logManager = null;

    private static XPluginManager pluginManager = null;

    private static XPluginUIManager pluginUIManager = null;

    private static XHistoryManager historyManager = null;

    private static XBookmarkManager bookmarkManager = null;

    private static XCookieManager cookieManager = null;

    private static XScreenManager screenManager = null;

    private static XProjectConfig projectConfig = null;

    private static XCacheManager cacheManager = null;

    static {
        resourceManager = new XResourceManager(XRepository.class.getClassLoader());
        resourceManager.initResourceBundle(XProjectConstants.PRODUCT_NAME);
        compBuilder = new XComponentBuilder(resourceManager);
        logManager = new XLogManager();
        pluginManager = new XPluginManager();
        pluginUIManager = new XPluginUIManager();
        historyManager = new XHistoryManager();
        bookmarkManager = new XBookmarkManager();
        cookieManager = new XCookieManager();
        screenManager = new XScreenManager();
        projectConfig = new XProjectConfig();
        cacheManager = new XCacheManager();
    }
}
