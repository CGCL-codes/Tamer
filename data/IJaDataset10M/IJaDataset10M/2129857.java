package com.ibm.celldt.managedbuilder.xl.ui.internal;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class XLPackages {

    private static final String BUNDLE_NAME = "com.ibm.celldt.managedbuilder.xl.ui.internal.xlpackages";

    private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME);

    private XLPackages() {
    }

    public static String getString(String key) {
        try {
            return RESOURCE_BUNDLE.getString(key);
        } catch (MissingResourceException e) {
            return '!' + key + '!';
        }
    }
}
