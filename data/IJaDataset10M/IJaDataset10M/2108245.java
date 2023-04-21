package org.pojosoft.core.configuration;

import org.apache.log4j.Logger;
import java.util.HashMap;
import java.util.Iterator;

/**
 * The Configuration Service Singleton.
 * @author POJO Software
 * @version 1.0
 * @since 1.0
 */
public class ConfigurationService {

    static Logger logger = Logger.getLogger(ConfigurationService.class);

    /**
   * The cache factory
   */
    private HashMap<String, ConfigurationLoader> configurationLoaders = null;

    private static ConfigurationService instance = null;

    private Cache configCache = null;

    /**
   * Get the  Configuration Loaders
   *
   * @return HashMap
   */
    public HashMap<String, ConfigurationLoader> getConfigurationLoaders() {
        return configurationLoaders;
    }

    /**
   * Set the Configuration Loaders
   *
   * @param configurationLoaders HashMap
   */
    public void setConfigurationLoaders(HashMap<String, ConfigurationLoader> configurationLoaders) {
        this.configurationLoaders = configurationLoaders;
    }

    /**
   * Get the singleton instance.
   *
   * @return ConfigurationService
   */
    public static ConfigurationService getInstance() {
        if (instance == null) {
            instance = new ConfigurationService();
            instance.init();
        }
        return instance;
    }

    protected Configuration loadConfig(String configClassName) {
        ConfigurationLoader loader = this.configurationLoaders.get(configClassName);
        return loader.load();
    }

    /**
   * Initialization method.
   */
    public void init() {
        instance = this;
        configCache = CacheHelper.instance.getCache("configCache");
        Iterator keys = configurationLoaders.keySet().iterator();
        while (keys.hasNext()) {
            String key = (String) keys.next();
            ConfigurationLoader loader = configurationLoaders.get(key);
            logger.info("Load the config " + key + " using ConfigLoader " + loader.getClass().getName());
            Configuration config = loader.load();
            if (configCache != null) {
                configCache.put(config.getClass().getName(), config);
            }
        }
    }

    /**
   * Get the SystemConfiguration. It might be cached if caching is enabled.
   *
   * @return SystemConfiguration.
   */
    public DefaultSystemPropertiesConfig getSystemConfig() {
        DefaultSystemPropertiesConfig config = null;
        if (configCache != null) {
            try {
                config = (DefaultSystemPropertiesConfig) configCache.get(DefaultSystemPropertiesConfig.class.getName());
            } catch (NotFoundInCacheException e) {
                config = (DefaultSystemPropertiesConfig) loadConfig(DefaultSystemPropertiesConfig.class.getName());
                configCache.put(config.getClass().getName(), config);
            }
        } else {
            config = (DefaultSystemPropertiesConfig) loadConfig(DefaultSystemPropertiesConfig.class.getName());
        }
        return config;
    }
}
