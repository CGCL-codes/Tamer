package org.modss.facilitator.shared.browser;

import java.net.URL;

/**
 * Defines the browser manager interface.  All use of the browser should
 * be performed through this interface.
 */
public interface BrowserManager {

    /**
     * Show a URL.
     *
     * @param url the url to show.
     */
    public void showURL(URL url);
}
