package org.aladdinframework.util;

import java.io.FileInputStream;
import java.net.URL;
import java.util.Properties;
import android.util.Log;

/**
 * Represents general configuration settings for the Aladdin Framework. This file works in conjunction with a text-based
 * properties file that is deployed with the Aladdin Framework and must conform to the Aladdin configuration
 * specification (see developer documentation). The configuration file MUST be named "aladdin.conf", MUST exist in the
 * 'conf' subdirectory of Aladdin's data directory, and MUST be readable by the Aladdin runtime process only.
 * <p>
 * To create an AladdinConfiguration, use the static factory method 'createConfiguration'. This method intelligently
 * parses the specified properties file and throws detailed exceptions if errors are found.
 * 
 * @author Darren Carlson
 */
public class AladdinConfiguration {

    public static final String TAG = AladdinConfiguration.class.getSimpleName();

    public static String ALADDIN_DATABASE_PATH = "aladdin.database.path";

    public static String ALLOW_ALADDIN_UPDATES = "allow.aladdin.updates";

    public static String ALLOW_CONTEXT_PLUGIN_UPDATES = "allow.context.plugin.updates";

    public static String CONTEXT_CACHE_MAX_EVENTS = "context.cache.max.events";

    public static String CONTEXT_CACHE_MAX_DURATION_MILLS = "context.cache.max.duration.mills";

    public static String CONTEXT_CACHE_CULL_INTERVAL_MILLS = "context.cache.cull.interval.mills";

    public static String ALLOW_REMOTE_CONFIGURATION = "allow.remote.configuration";

    public static String ALLOW_USER_SERVER_MANAGEMENT = "allow.user.server.management";

    public static String APP_LIVELINESS_CHECK_MILLS = "app.liveliness.check.mills";

    public static String APP_INACTIVITY_TIMEOUT_MILLS = "app.inactivity.timeout.mills";

    public static String PRIMARY_BOOTSTRAP_SERVER_ALIAS = "primary.bootstrap.server.alias";

    public static String PRIMARY_BOOTSTRAP_SERVERURL = "primary.bootstrap.server.url";

    public static String PRIMARY_BOOTSTRAP_SERVER_PORT = "primary.bootstrap.server.port";

    public static String PRIMARY_BOOTSTRAP_SERVER_USERNAME = "primary.bootstrap.server.username";

    public static String PRIMARY_BOOTSTRAP_SERVER_PASSWORD = "primary.bootstrap.server.password";

    public static String BACKUP_BOOTSTRAP_SERVER_ALIAS = "backup.bootstrap.server.alias";

    public static String BACKUP_BOOTSTRAP_SERVER_URL = "backup.bootstrap.server.url";

    public static String BACKUP_BOOTSTRAP_SERVER_PORT = "backup.bootstrap.server.port";

    public static String BACKUP_BOOTSTRAP_SERVER_USERNAME = "backup.bootstrap.server.username";

    public static String BACKUP_BOOTSTRAP_SERVER_PASSWORD = "backup.bootstrap.server.password";

    public static String PRIMARY_REMOTE_CONFIG_SERVER_ALIAS = "primary.remote.config.server.alias";

    public static String PRIMARY_REMOTE_CONFIG_SERVER_URL = "primary.remote.config.server.url";

    public static String PRIMARY_REMOTE_CONFIG_SERVER_PORT = "primary.remote.config.server.port";

    public static String PRIMARY_REMOTE_CONFIG_SERVER_USERNAME = "primary.remote.config.server.username";

    public static String PRIMARY_REMOTE_CONFIG_SERVER_PASSWORD = "primary.remote.config.server.password";

    public static String BACKUP_REMOTE_CONFIG_SERVER_ALIAS = "backup.remote.config.server.alias";

    public static String BACKUP_REMOTE_CONFIG_SERVER_URL = "backup.remote.config.server.url";

    public static String BACKUP_REMOTE_CONFIG_SERVER_PORT = "backup.remote.config.server.port";

    public static String BACKUP_REMOTE_CONFIG_SERVER_USERNAME = "backup.remote.config.server.username";

    public static String BACKUP_REMOTE_CONFIG_SERVER_PASSWORD = "backup.remote.config.server.password";

    private String aladdinDatabaseFilePath;

    private boolean aladdinUpdatesAllowed;

    private boolean contextPluginsUpdatesAllowed;

    private boolean remoteConfigAllowed;

    private boolean userServerManagementAllowed;

    private int appLivelinessCheckIntervalMills;

    private int contextCacheMaxEvents;

    private int contextCacheMaxDurationMills;

    private int contextCacheCullIntervalMills;

    private int appInactivityTimeoutMills;

    private ServerInfo primaryBootstrapServer;

    private ServerInfo backupBootstrapServer;

    private ServerInfo primaryRemoteConfigServer;

    private ServerInfo backupRemoteConfigServer;

    private AladdinConfiguration() {
    }

    /**
     * Factory method that creates an AladdinConfiguration using the specified properties file path. This method
     * intelligently parses the specified properties file and throws detailed exceptions if errors are found.
     * 
     * @param propsFilePath
     *            The path to the properties file
     * Returns a configured AladdinConfiguration based on the specified properties file
     * @throws Exception
     *             If the properties file cannot be parsed (includes a detailed error message)
     */
    public static AladdinConfiguration createConfiguration(String propsFilePath) throws Exception {
        Log.i(TAG, "Creating AladdinConfiguration using path: " + propsFilePath);
        AladdinConfiguration config = new AladdinConfiguration();
        Properties props = new Properties();
        props.load(new FileInputStream(propsFilePath));
        config.setAladdinDatabaseFilePath(validate(props, ALADDIN_DATABASE_PATH));
        config.setAladdinUpdatesAllowed(Boolean.parseBoolean(validate(props, ALLOW_ALADDIN_UPDATES)));
        config.setContextPluginsUpdatesAllowed(Boolean.parseBoolean(validate(props, ALLOW_CONTEXT_PLUGIN_UPDATES)));
        config.setContextCacheMaxEvents(Integer.parseInt(validate(props, CONTEXT_CACHE_MAX_EVENTS)));
        config.setContextCacheMaxDurationMills(Integer.parseInt(validate(props, CONTEXT_CACHE_MAX_DURATION_MILLS)));
        config.setContextCacheCullIntervalMills(Integer.parseInt(validate(props, CONTEXT_CACHE_CULL_INTERVAL_MILLS)));
        config.setRemoteConfigAllowed(Boolean.parseBoolean(validate(props, ALLOW_REMOTE_CONFIGURATION)));
        config.setUserServerManagementAllowed(Boolean.parseBoolean(validate(props, ALLOW_USER_SERVER_MANAGEMENT)));
        config.setAppLivelinessCheckIntervalMills(Integer.parseInt(validate(props, APP_LIVELINESS_CHECK_MILLS)));
        config.setAppInactivityTimeoutMills(Integer.parseInt(validate(props, APP_INACTIVITY_TIMEOUT_MILLS)));
        if (config.isAladdinUpdatable()) {
            config.setPrimaryBootstrapServer(makeServer("primaryBootstrapServer", true, props.getProperty(PRIMARY_BOOTSTRAP_SERVER_ALIAS), props.getProperty(PRIMARY_BOOTSTRAP_SERVERURL), props.getProperty(PRIMARY_BOOTSTRAP_SERVER_PORT), props.getProperty(PRIMARY_BOOTSTRAP_SERVER_USERNAME), props.getProperty(PRIMARY_BOOTSTRAP_SERVER_PASSWORD)));
            config.setBackupBootstrapServer(makeServer("backupBootstrapServer", false, props.getProperty(BACKUP_BOOTSTRAP_SERVER_ALIAS), props.getProperty(BACKUP_BOOTSTRAP_SERVER_URL), props.getProperty(BACKUP_BOOTSTRAP_SERVER_PORT), props.getProperty(BACKUP_BOOTSTRAP_SERVER_USERNAME), props.getProperty(BACKUP_BOOTSTRAP_SERVER_PASSWORD)));
        }
        if (config.isRemoteConfigAllowed()) {
            config.setPrimaryRemoteConfigServer(makeServer("primaryRemoteConfigServer", true, props.getProperty(PRIMARY_REMOTE_CONFIG_SERVER_ALIAS), props.getProperty(PRIMARY_REMOTE_CONFIG_SERVER_URL), props.getProperty(PRIMARY_REMOTE_CONFIG_SERVER_PORT), props.getProperty(PRIMARY_REMOTE_CONFIG_SERVER_USERNAME), props.getProperty(PRIMARY_REMOTE_CONFIG_SERVER_PASSWORD)));
            config.setBackupRemoteConfigServer(makeServer("backupRemoteConfigServer", false, props.getProperty(BACKUP_REMOTE_CONFIG_SERVER_ALIAS), props.getProperty(BACKUP_REMOTE_CONFIG_SERVER_URL), props.getProperty(BACKUP_REMOTE_CONFIG_SERVER_PORT), props.getProperty(BACKUP_REMOTE_CONFIG_SERVER_USERNAME), props.getProperty(BACKUP_REMOTE_CONFIG_SERVER_PASSWORD)));
        }
        return config;
    }

    /**
     * Utility method that extracts the requested propString from the incoming Properties, throwing a detailed exception
     * if the string cannot be found.
     * 
     * @param props
     *            The Properties file
     * @param propString
     *            The Property string to extract
     * Returns the extracted property string
     * @throws Exception
     *             If the property string cannot be found
     */
    private static String validate(Properties props, String propString) throws Exception {
        String s = props.getProperty(propString);
        if (s == null) throw new Exception("Could not find: " + propString);
        return s;
    }

    /**
     * Utility method that creates a ServerInfo from the incoming data. This method automatically validates the created
     * ServerInfo and throws a detailed exception (if requested) using the incoming description string as a descriptor
     * for the exception.
     */
    private static ServerInfo makeServer(String description, boolean throwException, String alias, String url, String port, String user, String pass) throws Exception {
        ServerInfo info = new ServerInfo();
        info.setAlias(alias);
        info.setUrl(new URL(url));
        info.setPort(Integer.parseInt(port));
        info.setUserName(user);
        info.setPassword(pass);
        if (!info.validateServerInfo()) {
            if (throwException) throw new Exception("Could not validate: " + description); else return null;
        }
        return info;
    }

    /**
     * Returns the path of the aladdin database from the root of the Aladdin installation directory, including the
     *         database's filename and extension.
     */
    public String getAladdinDatabaseFilePath() {
        return aladdinDatabaseFilePath;
    }

    /**
     * Sets path of the aladdin database from the root of the Aladdin installation directory, including the database's
     * filename and extension.
     */
    public void setAladdinDatabaseFilePath(String aladdinDatabaseFilePath) {
        this.aladdinDatabaseFilePath = aladdinDatabaseFilePath;
    }

    /**
     * Specifies if Aladdin is allowed to discover and dynamically install Aladdin Framework updates at runtime.
     * 
     * Returns true if Aladdin updates are allowed; false otherwise
     */
    public boolean isAladdinUpdatable() {
        return aladdinUpdatesAllowed;
    }

    /**
     * Sets if Aladdin is allowed to discover and dynamically install Aladdin Framework updates at runtime.
     * 
     * @param aladdinUpdatesAllowed
     *            true if Aladdin updates are allowed; false otherwise
     */
    public void setAladdinUpdatesAllowed(boolean aladdinUpdatesAllowed) {
        this.aladdinUpdatesAllowed = aladdinUpdatesAllowed;
    }

    /**
     * Specifies if Aladdin is allowed to dynamically discover and dynamically install context plugins at runtime.
     * 
     * Returns true if context plugin updates are allowed; false otherwise
     */
    public boolean areContextPluginUpdatesAllowed() {
        return contextPluginsUpdatesAllowed;
    }

    /**
     * Sets if Aladdin is allowed to dynamically discover and dynamically install context plugins at runtime.
     * 
     * @param contextPluginsUpdatesAllowed
     *            true if context plugin updates are allowed; false otherwise
     */
    public void setContextPluginsUpdatesAllowed(boolean contextPluginsUpdatesAllowed) {
        this.contextPluginsUpdatesAllowed = contextPluginsUpdatesAllowed;
    }

    /**
     * @return how often Aladdin should check if bound applications are alive (in milliseconds)
     */
    public int getAppLivelinessCheckIntervalMills() {
        return appLivelinessCheckIntervalMills;
    }

    /**
     * Sets how often Aladdin should check if bound applications are alive (in milliseconds)
     */
    public void setAppLivelinessCheckIntervalMills(int appLivelinessCheckIntervalMills) {
        this.appLivelinessCheckIntervalMills = appLivelinessCheckIntervalMills;
    }

    /**
     * Returns the time period of inactivity necessary for a bound application to listed as inactive (in milliseconds)
     */
    public int getAppInactivityTimeoutMills() {
        return appInactivityTimeoutMills;
    }

    /**
     * Sets the time period of inactivity necessary for a bound application to listed as inactive (in milliseconds)
     */
    public void setAppInactivityTimeoutMills(int appInactivityTimeoutMills) {
        this.appInactivityTimeoutMills = appInactivityTimeoutMills;
    }

    /**
     * Returns the max number of context events that may be cached (Note that 0 implies disabled)
     */
    public int getContextCacheMaxEvents() {
        return contextCacheMaxEvents;
    }

    /**
     * Sets the max number of context events that may be cached (Note that 0 implies disabled)
     */
    public void setContextCacheMaxEvents(int contextCacheMaxEvents) {
        this.contextCacheMaxEvents = contextCacheMaxEvents;
    }

    /**
     * Returns the max duration that a context event may be cached (in milliseconds)
     */
    public int getContextCacheMaxDurationMills() {
        return contextCacheMaxDurationMills;
    }

    /**
     * Sets the max duration that a context event may be cached (in milliseconds)
     */
    public void setContextCacheMaxDurationMills(int contextCacheMaxDurationMills) {
        this.contextCacheMaxDurationMills = contextCacheMaxDurationMills;
    }

    /**
     * @return How often the context event cache should scan for and remove expired events (in milliseconds)
     */
    public int getContextCacheCullIntervalMills() {
        return contextCacheCullIntervalMills;
    }

    /**
     * Sets how often the context event cache should scan for and remove expired events (in milliseconds)
     */
    public void setContextCacheCullIntervalMills(int contextCacheCullIntervalMills) {
        this.contextCacheCullIntervalMills = contextCacheCullIntervalMills;
    }

    /**
     * Specifies if Aladdin can be remotely configured using the remote configuration server settings
     * 
     * Returns true if Aladdin can be remotely configured; false otherwise
     */
    public boolean isRemoteConfigAllowed() {
        return remoteConfigAllowed;
    }

    /**
     * Sets if Aladdin can be remotely configured using the remote configuration server settings
     * 
     * @param remoteConfigAllowed
     *            true if Aladdin can be remotely configured; false otherwise
     */
    public void setRemoteConfigAllowed(boolean remoteConfigAllowed) {
        this.remoteConfigAllowed = remoteConfigAllowed;
    }

    /**
     * Specifies whether or not users are allowed to change and manage Aladdin's server settings Note: if false, servers
     * are configured exclusively from the settings below, or through remote config (if allowed)
     * 
     * Returns true if users are allowed to change and manage Aladdin's server settings; false otherwise
     */
    public boolean isUserServerManagementAllowed() {
        return userServerManagementAllowed;
    }

    /**
     * Sets whether or not users are allowed to change and manage Aladdin's server settings
     * 
     * @param userServerManagementAllowed
     *            true if users are allowed to change and manage Aladdin's server settings; false otherwise
     */
    public void setUserServerManagementAllowed(boolean userServerManagementAllowed) {
        this.userServerManagementAllowed = userServerManagementAllowed;
    }

    /**
     * Returns the primary bootstrap server for the Aladdin Framework
     */
    public ServerInfo getPrimaryBootstrapServer() {
        return primaryBootstrapServer;
    }

    /**
     * Sets the primary Bootstrap server for the Aladdin Framework 
     */
    public void setPrimaryBootstrapServer(ServerInfo bootstrapServer) {
        this.primaryBootstrapServer = bootstrapServer;
    }

    /**
     * Returns the backup server for Aladdin Framework updates (optional)
     */
    public ServerInfo getBackupBootstrapServer() {
        return backupBootstrapServer;
    }

    /**
     * Sets the backup bootstrap server for the Aladdin Framework
     */
    public void setBackupBootstrapServer(ServerInfo backupBootstrapServer) {
        this.backupBootstrapServer = backupBootstrapServer;
    }

    /**
     * Returns the primary server for Aladdin Framework remote configuration
     */
    public ServerInfo getPrimaryRemoteConfigServer() {
        return primaryRemoteConfigServer;
    }

    /**
     * Sets the primary server for Aladdin Framework remote configuration
     */
    public void setPrimaryRemoteConfigServer(ServerInfo primaryRemoteConfigServer) {
        this.primaryRemoteConfigServer = primaryRemoteConfigServer;
    }

    /**
     * Returns the backup server for Aladdin Framework remote configuration
     */
    public ServerInfo getBackupRemoteConfigServer() {
        return backupRemoteConfigServer;
    }

    /**
     * Sets the backup server for Aladdin Framework remote configuration
     */
    public void setBackupRemoteConfigServer(ServerInfo backupRemoteConfigServer) {
        this.backupRemoteConfigServer = backupRemoteConfigServer;
    }
}
