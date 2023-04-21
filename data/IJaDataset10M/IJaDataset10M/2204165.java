package net.sf.gratepic;

import java.awt.Desktop;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * @author leon
 */
public class BrowserOpener {

    private BrowserOpener() {
    }

    private static boolean openClean(URL url) {
        try {
            if (!Desktop.isDesktopSupported()) {
                return false;
            }
            Desktop desktop = Desktop.getDesktop();
            if (!desktop.isSupported(Desktop.Action.BROWSE)) {
                return false;
            }
            try {
                desktop.browse(url.toURI());
                return true;
            } catch (IOException ex) {
                return false;
            } catch (URISyntaxException ex) {
                return false;
            }
        } catch (NoClassDefFoundError ex) {
            return false;
        }
    }

    public static void openURL(URL u) {
        if (openClean(u)) {
            return;
        }
        String osName = System.getProperty("os.name");
        try {
            String url = u.toExternalForm();
            if (osName.startsWith("Mac OS")) {
                Class<?> fileMgr = Class.forName("com.apple.eio.FileManager");
                Method openURL = fileMgr.getDeclaredMethod("openURL", new Class[] { String.class });
                openURL.invoke(null, new Object[] { url });
            } else if (osName.startsWith("Windows")) {
                Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + url);
            } else {
                String[] browsers = { "firefox", "opera", "konqueror", "epiphany", "mozilla", "netscape" };
                String theBrowser = null;
                for (String browser : browsers) {
                    if (Runtime.getRuntime().exec(new String[] { "which", browser }).waitFor() == 0) {
                        theBrowser = browser;
                        break;
                    }
                }
                if (theBrowser == null) {
                    throw new Exception("Could not find web browser");
                } else {
                    Runtime.getRuntime().exec(new String[] { theBrowser, url });
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
