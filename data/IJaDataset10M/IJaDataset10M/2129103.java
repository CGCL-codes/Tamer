package com.googlecode.traytools.utils;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import com.googlecode.traytools.config.Configuration;

/**
 * Operating System specific utilities.
 * 
 * Used for places where different OS acts differently.
 * 
 * @author Tomas Varaneckas
 * @version $Id: OSUtils.java 514 2009-04-01 13:32:50Z tomas.varaneckas $
 */
public abstract class OSUtils {

    /**
	 * Supported Operating Systems
	 * 
	 * @author Tomas Varaneckas
	 * @version $Id: OSUtils.java 514 2009-04-01 13:32:50Z tomas.varaneckas $
	 */
    public enum OS {

        MAC, WIN, UNIX, UNKNOWN
    }

    /**
	 * Current Operating System
	 */
    public static final OS CURRENT_OS = getCurrentOS();

    /**
	 * Gets the current operating system
	 * 
	 * @return
	 */
    private static OS getCurrentOS() {
        final String os = System.getProperty("os.name", "unknown").toLowerCase();
        if (os.startsWith("win")) {
            return OS.WIN;
        }
        if (os.startsWith("mac")) {
            return OS.MAC;
        }
        if (os.startsWith("linux") || os.startsWith("unix") || os.startsWith("bsd")) {
            return OS.UNIX;
        }
        return OS.UNKNOWN;
    }

    /**
	 * Gets the tray icon size
	 * 
	 * @return
	 */
    public static int getTrayIconSize() {
        if (System.getProperty("java.version").compareTo("1.6") >= 0 && Configuration.SETTINGS.getString("guess.tray.icon.size").equals("true")) {
            try {
                Logger.debug("Java > 1.6, trying java.awt.SystemTray");
                final Class<?> systemTrayClass = Class.forName("java.awt.SystemTray");
                Method m = systemTrayClass.getMethod("getSystemTray", new Class[] {});
                final Object systemTray = m.invoke(systemTrayClass, new Object[] {});
                m = systemTray.getClass().getMethod("getTrayIconSize", new Class[] {});
                final Object size = m.invoke(systemTray, new Object[] {});
                m = null;
                return ((java.awt.Dimension) size).height;
            } catch (final Exception e) {
                Logger.warn("Failed calling java.awt.SystemTray object", e);
            }
        }
        if (CURRENT_OS.equals(OS.WIN)) {
            return 16;
        }
        if (CURRENT_OS.equals(OS.UNIX)) {
            return 24;
        }
        if (CURRENT_OS.equals(OS.MAC)) {
            return 16;
        }
        return 16;
    }

    /**
	 * Gets the system tray (menubar, panel, whatever) size
	 * 
	 * @return
	 */
    public static int getTraySize() {
        if (CURRENT_OS.equals(OS.MAC)) {
            return 26;
        }
        return getTrayIconSize();
    }

    /**
	 * Executes app with parameters
	 * 
	 * @param app command
	 * @param params parameters
	 * @return needs further execution? (when failed, returns true, otherwise false)
	 */
    public static boolean exec(String app, String params) {
        try {
            final List<String> cmd = new ArrayList<String>();
            switch(CURRENT_OS) {
                case MAC:
                    if (app.toLowerCase().endsWith(".app")) {
                        cmd.add("open");
                        cmd.add("-a");
                        cmd.add(app);
                        cmd.add(params);
                        break;
                    }
                default:
                    cmd.add(app);
                    cmd.add(params);
            }
            Logger.debug("Executing: " + cmd);
            String[] cmdArray = new String[] {};
            cmdArray = cmd.toArray(cmdArray);
            Runtime.getRuntime().exec(cmdArray);
            cmdArray = null;
            return false;
        } catch (final Exception e) {
            Logger.warn("Failed executing app " + app + " with params: " + params, e);
        }
        return true;
    }

    /**
	 * Sleep for some milliseconds. No exceptions.
	 * 
	 * @param millis 
	 */
    public static void sleep(final long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Logger.warn("Thread sleep interrupted", e);
        }
    }

    /**
     * Gets platform friendly fixed font name
     * 
     * @return
     */
    public static String getFixedFontName() {
        switch(CURRENT_OS) {
            case MAC:
                return "Monaco";
            case UNIX:
                return "Monospace";
            default:
                return "Courier New";
        }
    }
}
