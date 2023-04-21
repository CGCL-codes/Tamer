package org.openqa.selenium.server.browserlaunchers;

/**
 * The launcher interface for classes that will start/stop the browser process.
 *
 * @author Paul Hammant
 * @version $Revision: 732 $
 */
public interface BrowserLauncher {

    /**
     * Start the browser and navigate directly to the specified URL using the supplied browser configurations
     */
    void launchRemoteSession(String url);

    /**
     * Start the browser in Selenese mode, auto-running the specified HTML suite
     *
     * @param suiteUrl    the url of the HTML suite to launch
     * @param baseUrl    the url within which to initiate the session (if needed)
     */
    void launchHTMLSuite(String suiteUrl, String baseUrl);

    /**
     * Stop (kill) the browser process
     */
    void close();

    /**
     * Returns a process if there is an associated one running with this browser launcher (this is <b>not</b> required to be implementd).
     *
     * @return a handle to a process if one is available, or null if one is not available or if no browser is running
     */
    Process getProcess();
}
