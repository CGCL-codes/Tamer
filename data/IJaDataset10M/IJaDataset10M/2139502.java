package net.confex.java3d.translations;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import net.confex.translations.IMessages;

public class Messages implements IMessages {

    private static final String BUNDLE_NAME = "net.confex.java3d.translations.messages";

    private ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME, new Locale("en"));

    public Messages() {
    }

    public void switchLocale(String lang) {
        Locale locale = new Locale(lang);
        RESOURCE_BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME, locale);
    }

    public String getString(String key) {
        try {
            return RESOURCE_BUNDLE.getString(key);
        } catch (MissingResourceException e) {
            return null;
        }
    }
}
