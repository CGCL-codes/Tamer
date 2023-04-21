package org.xfc.util.platform;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.net.URL;
import org.xfc.XApp;
import org.xfc.util.XUtils;

/**
 * A utility class containing platform detection and abstraction.
 * 
 * @author Devon Carew
 */
public class XPlatform {

    private static XPlatformInterface platformInterface;

    private XPlatform() {
    }

    /**
     * @return one of "win", "mac", or "unix"
     */
    public static String getShortPlatformId() {
        if (isWindows()) return "win";
        if (isMacintosh()) return "mac";
        return "unix";
    }

    /**
     * Return true if the application is running on a Macintosh.
     * 
     * @return true if the application is running on a Macintosh
     */
    public static boolean isMacintosh() {
        return System.getProperty("os.name").toLowerCase().startsWith("mac");
    }

    /**
     * Return true if the application is running on Windows.
     * 
     * @return true if the application is running on Windows
     */
    public static boolean isWindows() {
        return System.getProperty("os.name").toLowerCase().indexOf("windows") != -1;
    }

    /**
	 * Developers should not call this method directly. The visibility has been reduced from public because it
	 * really isn't part of the public API. This method is called from XApp.java via reflection.
	 * 
	 * @param app
	 * @return Returns the newly created platform interface.
	 */
    static XPlatformInterface platformInit(XApp app) {
        if (platformInterface != null) throw new IllegalStateException("Platform interface has already been inited");
        String className = XUtils.getPackageName(XPlatformInterface.class) + ".X" + XUtils.capitalize(getShortPlatformId()) + "Platform";
        Class platClass = XUtils.classForName(className);
        if (platClass != null) {
            try {
                Constructor ctor = platClass.getDeclaredConstructor(new Class[] { XApp.class });
                Object platObject = ctor.newInstance(new Object[] { app });
                platformInterface = (XPlatformInterface) platObject;
            } catch (NoSuchMethodException e) {
                throw new RuntimeException("Platform class " + className + " is missing a required constructor", e);
            } catch (RuntimeException e) {
                throw e;
            } catch (Throwable t) {
                throw new RuntimeException(t);
            }
        }
        if (platformInterface == null) platformInterface = new XDefaultPlatform(app);
        return platformInterface;
    }

    /**
	 * Returns the platform interface.
	 * 
	 * @return Returns the platform interface.
	 */
    public static XPlatformInterface getPlatform() {
        if (platformInterface == null) throw new IllegalStateException("Platform interface not yet inited");
        return platformInterface;
    }

    /**
	 * Display the given file in the platform default browser.
	 * 
	 * @param file
	 * @throws IOException
	 */
    public static void showInBrowser(File file) throws IOException {
        getPlatform().showInBrowser(file);
    }

    /**
	 * Display the given URL in the platform default browser.
	 * 
	 * @param url the URL to display
	 * @throws IOException
	 */
    public static void showInBrowser(String url) throws IOException {
        getPlatform().showInBrowser(url);
    }

    /**
	 * Display the given URL in the platform default browser.
	 * 
	 * @param url
	 * @throws IOException
	 */
    public static void showInBrowser(URL url) throws IOException {
        getPlatform().showInBrowser(url);
    }
}
