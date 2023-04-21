package org.naftulin.configmgr;

/**
 * A factory that creates and maintains configuration management engine and configuration manager.
 * 
 * @author Henry Naftulin
 * @since 1.0
 */
public class ConfigurationManagerFactory {

    private static String configurationFile = "config.xml";

    private static ConfigurationManagement management;

    private static ConfigurationManager manager;

    /**
	 * Package private constructor, needed to access clear method. Only used
	 * for unit testing.
	 */
    ConfigurationManagerFactory() {
    }

    public static synchronized boolean isIniialized() {
        return management != null;
    }

    /**
	 * Sets a file from which master configuration would be read. 
	 * By default, the file is 'config.xml', but it could be owervritten by using this API.
	 * Only valid before configuration management is initialized, otherwise throws an exception.
	 * @param configFile the configuration file for master record.
	 * @throws ConfigurationManagerException if a configuration management engine is already initiazed.
	 */
    public static synchronized void setConfigurationFile(final String configFile) throws ConfigurationManagerException {
        if (management != null) {
            throw new ConfigurationManagerException("Configuration Manager already initialize");
        }
        configurationFile = configFile;
    }

    /**
	 * Returns configuration management engine object. If the object was not created yet,
	 * this call will create it.
	 * @return configuration management engine object.
	 * @throws ConfigurationManagerException if an error occured while creating a configuration
	 * managment object.
	 */
    public static synchronized ConfigurationManagement getConfigurationManagement() throws ConfigurationManagerException {
        if (management == null) {
            management = new ConfigurationManagementImpl(configurationFile);
            management.reload();
        }
        return management;
    }

    /**
	 * Returns configuration manager. If the manager was not created yet, this call
	 * will create it.
	 * @return configuration manager.
	 * @throws ConfigurationManagerException if an error occurs while creating a configuration
	 * manager.
	 */
    public static synchronized ConfigurationManager getConfigurationManager() throws ConfigurationManagerException {
        if (manager == null) {
            manager = getConfigurationManagement().getConfigurationManager();
        }
        return manager;
    }

    /**
	 * Clears the factory: resets configuration management, configuration manager and configuration file. 
	 * Needed for unit test settings only.
	 */
    static void clear() {
        synchronized (ConfigurationManagerFactory.class) {
            ConfigurationManagerFactory.management = null;
            ConfigurationManagerFactory.manager = null;
            ConfigurationManagerFactory.configurationFile = "config.xml";
        }
    }
}
