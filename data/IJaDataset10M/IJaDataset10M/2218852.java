package com.ivis.xprocess.ui.properties;

import org.eclipse.osgi.util.NLS;

public class DefaultNagMessages extends NLS {

    private static final String BUNDLE_NAME = "com.ivis.xprocess.ui.properties.defaultnag";

    public static String dialog_title;

    public static String default_line1;

    public static String default_line2;

    public static String default_line3;

    public static String default_license_generator_url;

    static {
        NLS.initializeMessages(BUNDLE_NAME, DefaultNagMessages.class);
    }
}
