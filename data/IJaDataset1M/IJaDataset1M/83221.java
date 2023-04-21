package com.jiexplorer;

import java.text.MessageFormat;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class Messages {

    private static ResourceBundle messagesBundle = ResourceBundle.getBundle("com/jiexplorer/messages");

    public static String getMessage(final String key) {
        try {
            return messagesBundle.getString(key);
        } catch (MissingResourceException e) {
            return key;
        } catch (NullPointerException e) {
            return "!" + key + "!";
        }
    }

    public static String getMessage(final String key, final Object[] objs) {
        try {
            return MessageFormat.format(getMessage(key), objs);
        } catch (MissingResourceException e) {
            return key;
        } catch (NullPointerException e) {
            return "!" + key + "!";
        }
    }

    public static String getVersionString() {
        return getMessage("Version");
    }
}
