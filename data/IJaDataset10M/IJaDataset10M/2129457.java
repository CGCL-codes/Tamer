package net.eiroca.j2me.app;

import java.util.Hashtable;

/**
 * The Class Device.
 */
public class Device {

    /** The self. */
    private static Device self = null;

    /** The Constant LOCALE. */
    public static final String LOCALE = "lo";

    /** The Constant PLATFORM. */
    public static final String PLATFORM = "pl";

    /** The prop. */
    public static Hashtable prop = new Hashtable();

    /**
   * Instantiates a new device.
   */
    private Device() {
        String locale = BaseApp.readProperty("microedition.locale", null);
        if (locale != null) {
            final int ps = locale.indexOf("-");
            if (ps > 0) {
                locale = locale.substring(0, ps);
            }
            if (locale.length() == 0) {
                locale = null;
            }
            if (locale != null) {
                Device.prop.put(Device.LOCALE, locale);
            }
        }
        final String platform = BaseApp.readProperty("microedition.platform", null);
        Device.prop.put(Device.PLATFORM, platform);
    }

    /**
   * Inits the.
   */
    public static void init() {
        if (Device.self == null) {
            Device.self = new Device();
        }
    }

    /**
   * Gets the locale.
   * 
   * @return the locale
   */
    public static final String getLocale() {
        return (String) Device.prop.get(Device.LOCALE);
    }

    /**
   * Gets the platform.
   * 
   * @return the platform
   */
    public static final String getPlatform() {
        return (String) Device.prop.get(Device.PLATFORM);
    }
}
