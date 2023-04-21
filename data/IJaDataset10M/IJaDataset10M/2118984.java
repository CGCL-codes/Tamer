package com.msli.rcp.pref.standard;

import org.eclipse.osgi.util.NLS;

/**
 * Constants and utilities for application preference messages.
 * @author jonb
 */
public class PreferenceMessages extends NLS {

    private PreferenceMessages() {
    }

    public static String STARTUP_PAGE_NAME;

    public static String STARTUP_PAGE_DESC;

    public static String PROMPT_FOR_WORKSPACE_ON_STARTUP;

    public static String REFRESH_WORKSPACE_ON_STARTUP;

    public static String RESTORE_WORKBENCH_ON_STARTUP;

    public static String SHUTDOWN_PAGE_NAME;

    public static String SHUTDOWN_PAGE_DESC;

    public static String PROMPT_FOR_CONFIRM_ON_EXIT;

    public static String WORKSPACE_PAGE_NAME;

    public static String WORKSPACE_PAGE_DESC;

    public static String OPEN_REQUIRED_PROJECTS;

    public static String OPEN_REQUIRED_PROJECTS_ALWAYS;

    public static String OPEN_REQUIRED_PROJECTS_NEVER;

    public static String OPEN_REQUIRED_PROJECTS_PROMPT;

    private static final String BUNDLE_NAME = "com.msli.rcp.pref.standard.messages";

    static {
        NLS.initializeMessages(BUNDLE_NAME, PreferenceMessages.class);
    }
}
