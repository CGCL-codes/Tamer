package org.jdeluxe.i18n;

import java.util.Enumeration;

/**
 * The Class XDBResourceBundle.
 */
public class XDBResourceBundle extends CommonResourceBundle {

    @Override
    public Enumeration<String> getKeys() {
        return null;
    }

    @Override
    protected Object handleGetObject(String key) {
        String country = getLocale().getCountry();
        System.out.println("Search for : " + key + " with locale = " + country);
        return "QWERTZUIOIUZTREWQ";
    }
}
