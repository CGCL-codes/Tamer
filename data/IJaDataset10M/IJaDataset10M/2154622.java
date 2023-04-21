package com.ibm.celldt.environment.remotesimulator.core;

import org.eclipse.osgi.util.NLS;

/**
 * @author Richard Maciel
 *
 * @since 1.2.1
 */
public class DefaultValues extends NLS {

    private static final String BUNDLE_ID = "com.ibm.celldt.environment.remotesimulator.core.PluginResources";

    public static String DEFAULT_REMOTE_KEY_PATH;

    public static String DEFAULT_REMOTE_KEY_PASSPHRASE;

    public static String DEFAULT_REMOTE_IS_PASSWORD_AUTH;

    public static String DEFAULT_REMOTE_TIMEOUT;

    public static String DEFAULT_REMOTE_LOGIN_USERNAME;

    public static String DEFAULT_REMOTE_LOGIN_PASSWORD;

    public static String DEFAULT_REMOTE_CONNECTION_ADDRESS;

    public static String DEFAULT_REMOTE_CONNECTION_PORT;

    public static String DEFAULT_SIMULATOR_IS_AUTOMATIC_CONFIG;

    public static String DEFAULT_SIMULATOR_IS_PASSWORD_AUTH;

    public static String DEFAULT_SIMULATOR_LOGIN_USERNAME;

    public static String DEFAULT_SIMULATOR_LOGIN_PASSWORD;

    public static String DEFAULT_SIMULATOR_KEY_PATH;

    public static String DEFAULT_SIMULATOR_KEY_PASSPHRASE;

    public static String DEFAULT_SIMULATOR_CONNECTION_ADDRESS;

    public static String DEFAULT_SIMULATOR_CONNECTION_PORT;

    public static String DEFAULT_SIMULATOR_CONNECTION_TIMEOUT;

    public static String DEFAULT_SYSTEM_WORKSPACE;

    static {
        NLS.initializeMessages(BUNDLE_ID, DefaultValues.class);
    }

    private DefaultValues() {
    }
}
