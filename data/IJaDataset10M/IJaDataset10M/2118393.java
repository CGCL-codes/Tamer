package mw.client.utils.gui;

import java.io.IOException;

/**
 * Opens page in a local browser
 * 
 * {@link http://forum.java.sun.com/thread.jspa?threadID=527199&messageID=2530618}
 *
 */
public class JLinkJumper {

    public static boolean displayURL(String url) {
        boolean result = true;
        boolean windows = isWindowsPlatform();
        String cmd = null;
        try {
            if (windows) {
                cmd = WIN_PATH + " " + WIN_FLAG + " " + url;
                Runtime.getRuntime().exec(cmd);
            } else {
                cmd = UNIX_PATH + " " + UNIX_FLAG + "(" + url + ")";
                Process p = Runtime.getRuntime().exec(cmd);
                try {
                    int exitCode = p.waitFor();
                    if (exitCode != 0) {
                        p = Runtime.getRuntime().exec(cmd);
                    }
                } catch (InterruptedException x) {
                    result = false;
                    System.err.println("Error bringing up browser, cmd='" + cmd + "'");
                    System.err.println("Caught: " + x);
                }
            }
        } catch (IOException ex) {
            result = false;
            System.err.println("Could not invoke browser, command=" + cmd);
            System.err.println("Caught: " + ex);
        }
        return result;
    }

    /**
	 * Try to determine whether this application is running under Windows or
	 * some other platform by examing the "os.name" property.
	 * 
	 * @return true if this application is running under a Windows OS
	 */
    public static boolean isWindowsPlatform() {
        String os = System.getProperty("os.name");
        if (os != null && os.startsWith(WIN_ID)) return true; else return false;
    }

    public static void main(String[] args) {
        displayURL("http://www.javaworld.com");
    }

    private static final String WIN_ID = "Windows";

    private static final String WIN_PATH = "rundll32";

    private static final String WIN_FLAG = "url.dll,FileProtocolHandler";

    private static final String UNIX_PATH = "netscape";

    private static final String UNIX_FLAG = "-remote openURL";
}
