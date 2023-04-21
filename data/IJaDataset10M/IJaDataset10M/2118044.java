package net.sf.refactorit.ui;

import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Arrays;

/**
 * A simple, static class to display a URL in the system browser.  <br><br>
 *
 * Under Unix, the system browser is hard-coded to be 'netscape'.
 * Netscape must be in your PATH for this to work.  This has been
 * tested with the following platforms: AIX, HP-UX and Solaris.  <br><br>
 *
 * Under Windows, this will bring up the default browser under windows,
 * usually either Netscape or Microsoft IE.  The default browser is
 * determined by the OS.  This has been tested under Windows 95/98/NT/2000.  <br><br>
 *
 * The code is adapted from a
 * <A HREF="http://www.javaworld.com/javaworld/javatips/jw-javatip66.html">
 * JavaWorld article</A>.
 */
public class ExternalWebBrowser {

    private static final String WIN_PATH = "rundll32";

    private static final String WIN_FLAG = "url.dll,FileProtocolHandler";

    /**
   * Display a file in the system browser.  If you want to display a
   * file, you must include the absolute path name.
   *
   * @param url the file's url
   */
    public static void newWindow(URL url) throws IOException {
        String urlString = URLDecoder.decode(url.toExternalForm());
        if (RuntimePlatform.isWindows()) {
            if (urlString.toLowerCase().endsWith(".htm") || urlString.toLowerCase().endsWith(".html")) {
                urlString += "?";
            }
            final String cmd = WIN_PATH + " " + WIN_FLAG + " " + urlString;
            Runtime.getRuntime().exec(cmd);
            return;
        }
        if (RuntimePlatform.isMacOsX()) {
            try {
                Class mrjFileUtils = Class.forName("com.apple.mrj.MRJFileUtils");
                Method method = mrjFileUtils.getMethod("openURL", new Class[] { String.class });
                method.invoke(null, new Object[] { urlString });
            } catch (Exception e) {
                System.err.println("EXCEPTION -- PLEASE REPORT -- cannot start web browser on Mac:");
                e.printStackTrace();
            }
            return;
        }
        try {
            newWindowUnix("netscape", urlString);
        } catch (IOException t) {
            try {
                newWindowUnix("mozilla", urlString);
            } catch (IOException e) {
                newWindowKonqueror(urlString);
            }
        }
    }

    /**
   * Display a file in the system browser on Unix.  If you want to display a
   * file, you must include the absolute path name.
   *
   * @param browserExecutable browser to start (e.g., <code>netscape</code>).
   * @param urlString the file's url.
   *
   * @throws IOException if failed to start browser. For instance, browser
   *         missing.
   */
    private static void newWindowUnix(String browserExecutable, String urlString) throws IOException {
        String[] cmd = new String[] { browserExecutable, "-remote", "openURL(" + urlString + ")" };
        Process p = Runtime.getRuntime().exec(cmd);
        try {
            int exitCode = p.waitFor();
            if (exitCode != 0) {
                cmd = new String[] { browserExecutable, urlString };
                p = Runtime.getRuntime().exec(cmd);
            }
        } catch (InterruptedException x) {
            System.err.println("EXCEPTION -- PLEASE REPORT:");
            System.err.println("Error bringing up browser, cmd='" + Arrays.asList(cmd) + "'");
            System.err.println("Caught: " + x);
        }
    }

    /**
   * Display a file in the Konqueror browser on Unix.  If you want to display a
   * file, you must include the absolute path name.
   *
   * @param urlString the file's url.
   *
   *
   * @throws IOException if failed to start browser. For instance, browser
   *         missing.
   */
    private static void newWindowKonqueror(String urlString) throws IOException {
        Runtime.getRuntime().exec(new String[] { "konqueror", urlString });
    }

    /**
   * Simple example.
   */
    public static void main(String[] args) throws Exception {
        newWindow(new URL("http://www.refactorit.com/index.html"));
    }
}
