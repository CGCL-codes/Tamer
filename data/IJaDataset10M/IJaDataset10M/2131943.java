package com.ivis.xprocess.ui.properties;

import org.eclipse.osgi.util.NLS;

public class TotalAvailabilityMessages extends NLS {

    private static final String BUNDLE_NAME = "com.ivis.xprocess.ui.properties.totalavailability";

    public static String totalavailability_problem_title;

    public static String totalavailability_noninteger_entered_message;

    static {
        NLS.initializeMessages(BUNDLE_NAME, TotalAvailabilityMessages.class);
    }
}
