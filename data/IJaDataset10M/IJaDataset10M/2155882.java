package net.sf.compositor.util;

import java.io.IOException;
import java.net.URL;
import java.util.regex.Pattern;

/**
 * Spawns a web browser. Based on code from
 * http://jgamedev.blogspot.com/2010/03/there-is-no-way-for-java-to-cleanly.html
 */
public abstract class NativeBrowser {

    private static Pattern SCHEME_AND_PORT = Pattern.compile(":[^/]+:");

    private static Pattern HOST_AND_PORT = Pattern.compile(".+:[0-9]+");

    private static Pattern SCHEME_AND_DETAIL = Pattern.compile(".+:[^0-9]+");

    private NativeBrowser() {
        throw new UnsupportedOperationException();
    }

    /**
	 * Opens a URL in a native browser. Anything that doesn't look like a URL
	 * already gets "http://" as a prefix.
	 */
    public static void open(final String url) throws IOException {
        open(new URL(needPrefix(url) ? "http://" + url : url));
    }

    private static boolean needPrefix(final String url) {
        if (-1 != url.indexOf("://")) return false;
        if (SCHEME_AND_PORT.matcher(url).matches()) return false;
        if (HOST_AND_PORT.matcher(url).matches()) return true;
        if (SCHEME_AND_DETAIL.matcher(url).matches()) return false;
        return true;
    }

    /**
	 * Opens a URL in a native browser.
	 */
    public static void open(final URL url) throws IOException {
        try {
            if (Env.IS_WINDOWS) openBroswerWindows(url); else if (Env.IS_MAC) openBroswerOSX(url); else openBroswerLinux(url);
        } catch (final InterruptedException x) {
            final IOException iox = new IOException("Interrupted while spawning native browser for URL \"" + url + "\" - " + x);
            iox.initCause(x);
            throw iox;
        }
    }

    /**
	 * Spawns a browser on Windows
	 */
    private static void openBroswerWindows(final URL url) throws IOException {
        Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + url);
    }

    /**
	 * Spawns a browser on a Mac
	 */
    private static void openBroswerOSX(final URL url) throws IOException {
        Runtime.getRuntime().exec("open " + url);
    }

    /**
	 * Spawns a browser on Linux
	 */
    private static void openBroswerLinux(final URL url) throws IOException, InterruptedException {
        final Runtime runtime = Runtime.getRuntime();
        if (runtime.exec("which mozilla").waitFor() == 0) {
            runtime.exec("mozilla " + url);
        } else if (runtime.exec("which netscape").waitFor() == 0) {
            runtime.exec("netscape " + url);
        } else if (runtime.exec("which opera").waitFor() == 0) {
            runtime.exec("opera " + url);
        } else {
            throw new IOException("Browser not found.");
        }
    }

    public static void main(final String[] args) throws Exception {
        NativeBrowser.open("www.example.com");
        NativeBrowser.open("http://www.example.net/whatever");
        NativeBrowser.open("https://www.example.net/whatever");
        NativeBrowser.open("ftp://www.example.com/");
        NativeBrowser.open("file://c:/");
        NativeBrowser.open("gopher://gopher.freeshell.org");
        NativeBrowser.open("mailto:nobody@example.com?subject=Jam&body=I%20like%20it%20on%20toast.");
    }
}
