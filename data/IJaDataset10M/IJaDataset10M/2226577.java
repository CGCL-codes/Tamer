package com.extentech.toolkit;

/** Launches the user's default browser to display a web page.
 * @author Dem Pilafian
 * @author Sam Hanes
 */
public class BrowserLauncher {

    /** List of potential browsers on systems without a default mechanism. */
    public static final String[] browsers = { "google-chrome", "firefox", "opera", "konqueror", "epiphany", "seamonkey", "galeon", "kazehakase", "mozilla" };

    /** The browser that was last successfully run. */
    private static String browser = null;

    /** Opens the specified web page in the user's default browser.
	 * @param url the URL of the page to be opened
	 * @throws Exception if an error occurred attempting to launch the browser.
	 *         If the browser is successfully started but later fails for
	 *         some reason, no exception will be thrown.
	 */
    public static void open(String url) throws Exception {
        try {
            Class desktop = Class.forName("java.awt.Desktop");
            desktop.getDeclaredMethod("browse", new Class[] { java.net.URI.class }).invoke(desktop.getDeclaredMethod("getDesktop", (Class[]) null).invoke(null, (Object[]) null), new Object[] { java.net.URI.create(url) });
            return;
        } catch (ClassNotFoundException e) {
        } catch (NoSuchMethodException e) {
        } catch (Exception e) {
            throw new Exception("failed to launch browser", e);
        }
        String osName = System.getProperty("os.name");
        try {
            if (osName.startsWith("Mac OS")) Class.forName("com.apple.eio.FileManager").getDeclaredMethod("openURL", new Class[] { String.class }).invoke(null, new Object[] { url }); else if (osName.startsWith("Windows")) Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + url); else {
                if (browser == null) {
                    for (int idx = 0; idx < browsers.length; idx++) {
                        if (Runtime.getRuntime().exec(new String[] { "which", browsers[idx] }).waitFor() == 0) {
                            browser = browsers[idx];
                        }
                    }
                    if (browser == null) throw new Exception("no browser found");
                }
                Runtime.getRuntime().exec(new String[] { browser, url });
            }
        } catch (Exception e) {
            throw new Exception("failed to launch browser", e);
        }
    }
}
