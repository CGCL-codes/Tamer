package org.commonfarm.security.config;

/**
 * Factory for {@link SecurityConfig} instances.
 */
public class SecurityConfigFactory {

    private static SecurityConfig instance;

    /**
	 * Get a SecurityConfig instance.
	 * 
	 * @return A default implementation of SecurityConfig.
	 * @throws RuntimeException
	 *             If loading the configuration failed.
	 */
    public static SecurityConfig getInstance() {
        if (instance == null) loadInstance(SecurityConfigImpl.DEFAULT_CONFIG_LOCATION);
        return instance;
    }

    /**
	 * Get a SecurityConfig instance.
	 * 
	 * @param configFileLocation
	 *            Path to config file resource (usu. 'seraph-config.xml')
	 * @return
	 * @throws RuntimeException
	 *             If loading the configuration failed.
	 */
    public static SecurityConfig getInstance(final String configFileLocation) {
        if (instance == null) loadInstance(configFileLocation);
        return instance;
    }

    /** Set the SecurityConfig instance to return. Useful for unit tests. */
    public static void setSecurityConfig(final SecurityConfig securityConfig) {
        instance = securityConfig;
    }

    private static void loadInstance(final String configFileLocation) {
        try {
            instance = new SecurityConfigImpl(configFileLocation);
        } catch (ConfigurationException e) {
            e.printStackTrace();
            throw new RuntimeException("Could not load security config '" + configFileLocation + "': " + e.getMessage());
        }
    }

    private SecurityConfigFactory() {
    }
}
