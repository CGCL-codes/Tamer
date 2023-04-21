package org.shell.utils.config;

public class ConfigManager {

    public static void set(String name, Object value) {
        ConfigImpl.getInstance().setProperty(name, value);
    }

    public static Object get(String name) {
        return ConfigImpl.getInstance().getProperty(name);
    }

    public static String[] list() {
        return ConfigImpl.getInstance().list();
    }
}
