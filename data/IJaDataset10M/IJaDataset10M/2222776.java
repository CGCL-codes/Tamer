package eu.kolovos.minigen.ui;

import eu.kolovos.minigen.util.OperatingSystem;

public class BrowserLauncher {

    public static void openURL(String url) {
        try {
            if (OperatingSystem.isWindows()) Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + url); else {
                String[] browsers = { "firefox", "opera", "konqueror", "epiphany", "mozilla", "netscape" };
                String browser = null;
                for (int count = 0; count < browsers.length && browser == null; count++) if (Runtime.getRuntime().exec(new String[] { "which", browsers[count] }).waitFor() == 0) browser = browsers[count];
                if (browser == null) throw new Exception("Could not find web browser"); else Runtime.getRuntime().exec(new String[] { browser, url });
            }
        } catch (Exception e) {
        }
    }
}
