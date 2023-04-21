package org.chon.web;

import java.util.Properties;
import org.chon.web.api.Application;

public class AppPropertyHelper {

    private static final Integer DEFAULT_APP_PRIORITY = 1000;

    public static int getAppPriority(Application app) {
        return getInt(app.getAppProperties(), Application.PRIORITY, DEFAULT_APP_PRIORITY);
    }

    public static String get(Properties properties, String key) {
        return get(properties, key, null);
    }

    public static String get(Properties properties, String key, String def) {
        if (properties != null) {
            return properties.getProperty(key, def);
        }
        return null;
    }

    public static Integer getInt(Properties properties, String key) {
        return getInt(properties, key, 0);
    }

    public static Integer getInt(Properties properties, String key, Integer def) {
        String s = get(properties, key);
        if (s == null) {
            return def;
        }
        try {
            return Integer.parseInt(s);
        } catch (NumberFormatException e) {
            return def;
        }
    }
}
