package org.opensourcephysics.ejs;

import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.Locale;

public class EjsRes {

    private static final String BUNDLE_NAME = "org.opensourcephysics.resources.ejs.ejs_res";

    static ResourceBundle res = ResourceBundle.getBundle(BUNDLE_NAME, Locale.getDefault());

    private EjsRes() {
    }

    public static void setLocale(Locale locale) {
        res = ResourceBundle.getBundle(BUNDLE_NAME, locale);
    }

    public static String getString(String key) {
        try {
            return res.getString(key);
        } catch (MissingResourceException e) {
            return '!' + key + '!';
        }
    }
}
