package net.tourbook.device.daum.ergobike;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {

    private static final String BUNDLE_NAME = "net.tourbook.device.daum.ergobike.messages";

    public static String pref_regional_decimalSeparator;

    public static String pref_regional_groupSeparator;

    public static String pref_regional_title;

    public static String pref_regional_useCustomDecimalFormat;

    public static String pref_regional_value_example;

    static {
        NLS.initializeMessages(BUNDLE_NAME, Messages.class);
    }

    private Messages() {
    }
}
