package com.triplea.dao.repository;

import java.io.InputStream;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.MissingResourceException;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

public class Messages {

    private static final String BUNDLE_FILE_NAME = "/com/triplea/dao/sql/messages.properties";

    private static Messages instance = null;

    private static ResourceBundle bundle = null;

    private Messages() {
    }

    public static Messages getInstance() {
        if (instance == null) instance = new Messages();
        return instance;
    }

    public static String getString(String key) {
        try {
            if (bundle == null) {
                InputStream inputStream = getInstance().getClass().getResourceAsStream(BUNDLE_FILE_NAME);
                if (inputStream != null) bundle = new PropertyResourceBundle(inputStream);
            }
            if (bundle != null) return bundle.getString(key); else return '!' + key + '!';
        } catch (IOException e) {
            return '!' + key + '!';
        } catch (MissingResourceException e) {
            return '!' + key + '!';
        }
    }

    public static String getFormattedString(String key, Object[] arguments) {
        return MessageFormat.format(getString(key), arguments);
    }

    public static String getFormattedString(String key, Object arg1) {
        Object arguments[] = new Object[1];
        arguments[0] = arg1;
        return MessageFormat.format(getString(key), arguments);
    }

    public static String getFormattedString(String key, Object arg1, Object arg2) {
        Object arguments[] = new Object[2];
        arguments[0] = arg1;
        arguments[1] = arg2;
        return MessageFormat.format(getString(key), arguments);
    }

    public static String getFormattedString(String key, Object arg1, Object arg2, Object arg3) {
        Object arguments[] = new Object[3];
        arguments[0] = arg1;
        arguments[1] = arg2;
        arguments[2] = arg3;
        return MessageFormat.format(getString(key), arguments);
    }

    public static String getFormattedString(String key, Object arg1, Object arg2, Object arg3, Object arg4) {
        Object arguments[] = new Object[4];
        arguments[0] = arg1;
        arguments[1] = arg2;
        arguments[2] = arg3;
        arguments[3] = arg4;
        return MessageFormat.format(getString(key), arguments);
    }
}
