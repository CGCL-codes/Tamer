package jreader.gui;

import java.io.IOException;

/**
 * Klasa służąca do otwierania zewnętrznej przeglądarki internetowej.
 * @author Karol
 *
 */
public class BrowserControl {

    private static final String WIN_ID = "Windows";

    private static final String WIN_PATH = "rundll32";

    private static final String WIN_FLAG = "url.dll,FileProtocolHandler";

    private static final String UNIX_PATH = "firefox";

    private static final String UNIX_FLAG = "";

    /**
	* Otwiera link w domyślnej przeglądarce systemu operacyjnego. 
	*
	* @param url link do strony (musi zaczynać się od "http://" lub "file://")
	*/
    public static void displayURL(String url) {
        boolean windows = isWindowsPlatform();
        String cmd = null;
        try {
            if (windows) {
                cmd = WIN_PATH + " " + WIN_FLAG + " " + url;
                Runtime.getRuntime().exec(cmd);
            } else {
                cmd = UNIX_PATH + " " + UNIX_FLAG + url;
                Process p = Runtime.getRuntime().exec(cmd);
                try {
                    int exitCode = p.waitFor();
                    if (exitCode != 0) {
                        cmd = "opera" + " " + url;
                        p = Runtime.getRuntime().exec(cmd);
                    }
                } catch (InterruptedException x) {
                    System.err.println("Error bringing up browser, cmd='" + cmd + "'");
                    System.err.println("Caught: " + x);
                }
            }
        } catch (IOException x) {
            System.err.println("Could not invoke browser, command=" + cmd);
            System.err.println("Caught: " + x);
        }
    }

    /**
	* Rozpoznaje system na jakim uruchomiony jest program.
	*
	* @return <code>true</code> jeśli rozpoznany system to windows, w innym przyadku </code>false</code>
	*/
    public static boolean isWindowsPlatform() {
        String os = System.getProperty("os.name");
        if (os != null && os.startsWith(WIN_ID)) return true; else return false;
    }
}
