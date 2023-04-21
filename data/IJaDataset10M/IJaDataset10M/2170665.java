package net.sourceforge.processdash.ui.systray;

import net.sourceforge.processdash.util.FallbackObjectFactory;

/**
 * Utility class to access the application system tray icon. Currently,
 *  only one icon is supported. The class attempts to initialize
 *  the icon using JDK 6 systray support. If the underlying JDK doesn't
 *  support systray API, then initialization will fall back to the default
 *  implementation, which simply does nothing.
 * 
 * @author Max Agapov <magapov@gmail.com>
 *
 */
public class SystemTrayManagement {

    /** A user setting which is used to enable/disable the system tray icon */
    public static final String DISABLED_SETTING = "systemTray.disabled";

    /**
     * the icon object
     */
    private static final SystemTrayIcon icon = initialize();

    /**
     * Private constructor to enforce non-instantiability
     */
    private SystemTrayManagement() {
    }

    /**
     * Initialize system tray icon using available implementation.
     * 
     * @return system tray icon
     */
    private static SystemTrayIcon initialize() {
        return new FallbackObjectFactory<SystemTrayIcon>(SystemTrayIcon.class).add("SystemTrayIconJDK6Impl").get();
    }

    public static SystemTrayIcon getIcon() {
        return icon;
    }
}
