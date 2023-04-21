package org.isandlatech.plugins.rest;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.browser.IWebBrowser;
import org.eclipse.ui.browser.IWorkbenchBrowserSupport;

/**
 * Handles browsers windows
 * 
 * @author Thomas Calmant
 */
public final class BrowserController {

    /** Controller unique instance */
    private static BrowserController sSingleton;

    /**
	 * Free browsers, if needed
	 */
    public static void close() {
        if (sSingleton != null) {
            sSingleton.dispose();
        }
    }

    /**
	 * Grab the browser controller
	 * 
	 * @return The browser controller
	 */
    public static BrowserController getController() {
        if (sSingleton == null) {
            sSingleton = new BrowserController();
        }
        return sSingleton;
    }

    /** ID -> Browser hash map */
    private final Map<String, IWebBrowser> pWebBrowsers;

    /**
	 * Single instance
	 */
    private BrowserController() {
        pWebBrowsers = new HashMap<String, IWebBrowser>();
    }

    /**
	 * Free browsers
	 */
    protected void dispose() {
        for (IWebBrowser browser : pWebBrowsers.values()) {
            if (browser != null) {
                browser.close();
            }
        }
        pWebBrowsers.clear();
    }

    /**
	 * Creates or retrieves a browser for this ID. Returns null on error
	 * 
	 * @param aId
	 *            Browser ID (can be null)
	 * 
	 * @return A browser associated to this ID, null on error
	 */
    protected IWebBrowser getBrowser(final String aId) {
        IWebBrowser browser;
        int internalOrExternal = IWorkbenchBrowserSupport.AS_EDITOR;
        if (isLinux()) {
            internalOrExternal = IWorkbenchBrowserSupport.AS_EXTERNAL;
        }
        try {
            browser = PlatformUI.getWorkbench().getBrowserSupport().createBrowser(internalOrExternal | IWorkbenchBrowserSupport.LOCATION_BAR | IWorkbenchBrowserSupport.NAVIGATION_BAR | IWorkbenchBrowserSupport.STATUS, aId, null, null);
            pWebBrowsers.put(aId, browser);
        } catch (PartInitException e) {
            RestPlugin.logError("Error preparing a browser", e);
            browser = pWebBrowsers.get(aId);
        }
        return browser;
    }

    /**
	 * Tests if the current operating system is a Linux flavor
	 * 
	 * @return True if the current operating system is Linux.
	 */
    public boolean isLinux() {
        String osName = System.getProperty("os.name");
        if (osName == null) {
            return false;
        }
        osName = osName.toLowerCase();
        return osName.contains("linux");
    }

    /**
	 * Opens the given location into a browser (internal if possible, else
	 * external). Does nothing on error
	 * 
	 * @param aId
	 *            Browser ID (for internal browser only)
	 * @param aLocation
	 *            A valid URL
	 */
    public void openUrl(final String aId, final String aLocation) {
        try {
            URL url = new URL(aLocation);
            openUrl(aId, url);
        } catch (MalformedURLException e) {
            RestPlugin.logError("Can't parse the given URL : '" + aLocation + "'", e);
        }
    }

    /**
	 * Opens the given location into a browser (internal if possible, else
	 * external). Does nothing on error
	 * 
	 * @param aId
	 *            Browser ID (for internal browser only)
	 * @param aUrl
	 *            The target URL
	 */
    public void openUrl(final String aId, final URL aUrl) {
        IWebBrowser browser = getBrowser(aId);
        try {
            browser.openURL(aUrl);
        } catch (PartInitException e) {
            RestPlugin.logError("Can't open the URL '" + aUrl + "'", e);
        }
    }
}
