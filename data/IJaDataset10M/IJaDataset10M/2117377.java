package net.cattaka.util;

import java.io.InputStream;
import java.util.MissingResourceException;
import java.util.Properties;

public class MessageBundle {

    public static Properties properties;

    public static Properties versionProperties;

    static {
        properties = MessageBundleLoader.getMessageBundle();
        versionProperties = MessageBundleLoader.getVersionPorperties();
    }

    public static String getBuildNumber() {
        return versionProperties.getProperty("build.number");
    }

    public static String getReleaseNumber() {
        return versionProperties.getProperty("release.number");
    }

    public static String getMessage(String key) {
        try {
            String result = properties.getProperty(key);
            if (result != null) {
                return result;
            } else {
                return key;
            }
        } catch (MissingResourceException e) {
            return key;
        }
    }

    public static String[] convertMessages(String[] target) {
        for (int i = 0; i < target.length; i++) {
            if (target[i] != null) {
                target[i] = getMessage(target[i]);
            }
        }
        return target;
    }
}

class MessageBundleLoader {

    public static Properties getMessageBundle() {
        Properties properties = new Properties();
        try {
            InputStream in = MessageBundle.class.getClassLoader().getResourceAsStream("messages_ja_JP.properties");
            properties.loadFromXML(in);
        } catch (Exception e) {
            ExceptionHandler.error(e);
        }
        return properties;
    }

    public static Properties getVersionPorperties() {
        Properties properties = new Properties();
        try {
            InputStream in = MessageBundle.class.getClassLoader().getResourceAsStream("version.properties");
            properties.load(in);
        } catch (Exception e) {
            ExceptionHandler.error(e);
        }
        return properties;
    }
}
