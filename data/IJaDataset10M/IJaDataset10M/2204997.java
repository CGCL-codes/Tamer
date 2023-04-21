package com.nepxion.cots.twaver.locale;

import java.util.Locale;
import com.nepxion.util.locale.LocaleManager;

public class TLocale {

    public static final Class BUNDLE_CLASS = TLocale.class;

    public static String getString(String key) {
        return LocaleManager.getString(BUNDLE_CLASS, key);
    }

    public static String getString(String key, Locale locale) {
        return LocaleManager.getString(BUNDLE_CLASS, key, locale);
    }
}
