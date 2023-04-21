package edu.iu.iv.toolkits.vwtk.messages;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class UtilMessages {

    private static final String BUNDLE_NAME = "edu.iu.iv.toolkits.vwtk.messages.utilmessages";

    private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME);

    private UtilMessages() {
    }

    public static String str(String key) {
        try {
            return RESOURCE_BUNDLE.getString(key);
        } catch (MissingResourceException e) {
            return '!' + key + '!';
        }
    }
}
