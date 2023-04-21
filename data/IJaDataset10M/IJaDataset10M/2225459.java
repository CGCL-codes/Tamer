package org.gs.game.gostop;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import javax.swing.UIManager;

public class Resource {

    private static Locale _locale = Locale.getDefault();

    private static final String PROP_BASE = "resources.gostop";

    public static final String STD_YES_BUTTON = "OptionPane.yesButtonText";

    public static final String STD_NO_BUTTON = "OptionPane.noButtonText";

    public static final String STD_OK_BUTTON = "OptionPane.okButtonText";

    public static final String STD_CANCEL_BUTTON = "OptionPane.cancelButtonText";

    public static void setLocale(String locale) {
        if (locale == null) _locale = Locale.getDefault(); else {
            String lang;
            String country;
            int index = locale.indexOf('_');
            if (index > 0) {
                lang = locale.substring(0, index);
                country = locale.substring(index + 1);
            } else {
                lang = locale;
                country = "";
            }
            _locale = new Locale(lang, country, "");
        }
    }

    public static Locale getLocale() {
        return _locale;
    }

    public static String getProperty(String name) {
        return getProperty(name, _locale);
    }

    public static String getProperty(String name, Locale locale) {
        String property;
        try {
            ResourceBundle rb = ResourceBundle.getBundle(PROP_BASE, locale);
            property = rb.getString(name);
        } catch (MissingResourceException e) {
            property = null;
        }
        return property;
    }

    public static String getStandardProperty(String name) {
        return getStandardProperty(name, _locale);
    }

    public static String getStandardProperty(String name, Locale locale) {
        return UIManager.getString(name, locale);
    }

    public static Locale getResourceLocale(Locale locale) {
        Locale resourceLocale;
        try {
            ResourceBundle rb = ResourceBundle.getBundle(PROP_BASE, locale);
            resourceLocale = rb.getLocale();
        } catch (MissingResourceException e) {
            resourceLocale = null;
        }
        return resourceLocale;
    }

    public static String format(String patternId, Object... params) {
        String msg;
        String pattern = getProperty(patternId);
        if (pattern == null) msg = patternId; else msg = MessageFormat.format(pattern, params);
        return msg;
    }
}
