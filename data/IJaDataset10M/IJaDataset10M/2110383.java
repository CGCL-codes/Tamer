package com.prolix.editor;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {

    private static final String BUNDLE_NAME = "com.prolix.editor.messages";

    public static String BasicChecks_ACTIVITYINOUT;

    public static String BasicChecks_ACTIVITYLOOP;

    public static String BasicChecks_CONNECTIONOK;

    public static String BasicChecks_NOACTIVITY;

    public static String BasicChecks_OK;

    public static String BasicChecks_prolix_head_name;

    public static String BasicChecks_ROLEMISSING;

    public static String BasicChecks_ROLESOK;

    public static String BasicChecks_SELECTIONPOINT;

    public static String BasicChecks_SEQUENCEOK;

    public static String BasicChecks_STARTPOINTREFMISSING;

    public static String BasicChecks_ACTIVITYDESCRIPTION1;

    public static String BasicChecks_ACTIVITYDESCRIPTION2;

    static {
        NLS.initializeMessages(BUNDLE_NAME, Messages.class);
    }

    private Messages() {
    }
}
