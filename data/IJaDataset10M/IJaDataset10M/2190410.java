package com.myJava.system.viewer;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLDecoder;
import com.myJava.configuration.FrameworkConfiguration;
import com.myJava.system.NoBrowserFoundException;
import com.myJava.system.OSTool;
import com.myJava.util.log.Logger;

public class DefaultViewerHandler implements ViewerHandler {

    private static final String[] BROWSERS = FrameworkConfiguration.getInstance().getOSBrowsers();

    private static String APPLE_FILE_MGR = "com.apple.eio.FileManager";

    public boolean test() {
        return true;
    }

    public void browse(URL urlObj) throws IOException, NoBrowserFoundException {
        String url = urlObj.toExternalForm();
        if (OSTool.isSystemMACOS()) {
            try {
                Class fileMgr = Class.forName(APPLE_FILE_MGR);
                Method openURL = fileMgr.getDeclaredMethod("openURL", new Class[] { String.class });
                openURL.invoke(null, new Object[] { urlObj });
            } catch (Exception e) {
                Logger.defaultLogger().error(e);
                throw new NoBrowserFoundException(e);
            }
        } else if (OSTool.isSystemWindows()) {
            if (url.startsWith("file:/") && url.charAt(6) != '/') {
                url = "file:///" + url.substring(6);
                url = URLDecoder.decode(url);
            }
            OSTool.execute(new String[] { "rundll32", "url.dll,FileProtocolHandler", url }, true);
        } else {
            String browser = null;
            for (int count = 0; count < BROWSERS.length && browser == null; count++) {
                if (OSTool.execute(new String[] { "which", BROWSERS[count] }) == 0) {
                    browser = BROWSERS[count];
                }
            }
            if (browser != null) {
                OSTool.execute(new String[] { browser, url }, true);
            } else {
                throw new NoBrowserFoundException("No browser cound be found.");
            }
        }
    }

    public boolean isBrowseSupported() {
        return true;
    }

    public boolean isOpenSupported() {
        return false;
    }

    public void open(File file) throws IOException {
        throw new UnsupportedOperationException("The 'open' method is not supported by this implementation.");
    }
}
