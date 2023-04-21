package edu.iu.iv.toolkits.vwtk.resources;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class VWTkResources {

    private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle("edu.iu.iv.toolkits.vwtk.resources.vwtkresources");

    public static final String getString(String key) {
        String string = null;
        try {
            string = RESOURCE_BUNDLE.getString(key);
        } catch (MissingResourceException mre) {
            string = '!' + key + '!';
        }
        return string;
    }
}
