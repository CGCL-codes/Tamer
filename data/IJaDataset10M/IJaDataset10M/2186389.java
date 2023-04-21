package org.gromurph.util;

import java.util.List;
import edu.stanford.ejalbert.exception.BrowserLaunchingInitializingException;
import edu.stanford.ejalbert.exception.UnsupportedOperatingSystemException;

/**
 * Revamped in Jan, 2008 to use new BrowserLauncher2 code, this class is now a singleton wrapper
 * around the new code from stanford (http://browserlaunch2.sourceforge.net/)
 */
public class BrowserLauncher {

    public static org.gromurph.util.BrowserLauncher INSTANCE = new org.gromurph.util.BrowserLauncher();

    private edu.stanford.ejalbert.BrowserLauncher launcher;

    public BrowserLauncher() {
        try {
            launcher = new edu.stanford.ejalbert.BrowserLauncher();
        } catch (BrowserLaunchingInitializingException e) {
            e.printStackTrace();
        } catch (UnsupportedOperatingSystemException e) {
            e.printStackTrace();
        }
    }

    public static void openUrl(String url) {
        INSTANCE._openUrl(url);
    }

    private void _openUrl(String url) {
        if (launcher != null) {
            launcher.openURLinBrowser("file://" + url);
        } else {
        }
    }

    public static boolean initializedOK() {
        return (INSTANCE.launcher != null);
    }

    public static void setPreferredBrowser(String browserAppName) {
        INSTANCE.preferredBrowser = browserAppName;
    }

    public static String getPreferredBrowser() {
        return INSTANCE.preferredBrowser;
    }

    private String preferredBrowser = null;

    public static List<String> getBrowserList() {
        return INSTANCE._getBrowserList();
    }

    private List<String> _getBrowserList() {
        if (launcher != null) {
            return launcher.getBrowserList();
        }
        return null;
    }
}
