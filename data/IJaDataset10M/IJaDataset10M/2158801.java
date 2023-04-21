package karto.DataModels;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class Messages {

    public static String BUNDLE_NAME = "karto.DataModels.MessagesDE";

    public static ResourceBundle RESOURCE_BUNDLE;

    private Messages() {
    }

    public static String getString(String key) {
        try {
            return RESOURCE_BUNDLE.getString(key);
        } catch (MissingResourceException e) {
            return '!' + key + '!';
        }
    }
}
