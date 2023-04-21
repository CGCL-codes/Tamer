package com.acv.service.configuration;

import java.util.Hashtable;
import java.util.Map;
import java.util.Map.Entry;
import org.apache.log4j.Logger;
import com.acv.dao.configuration.model.Config;
import com.acv.dao.configuration.model.ConfigData;
import com.acv.service.common.exception.ObjectAlreadyExistsException;
import com.acv.service.common.exception.ObjectNotFoundException;
import com.acv.service.configuration.exception.ConfigurationException;

/**
 * Abstract class to extend to create a specific configuration class. Child
 * class construtor must initiate currentClassName with its own class name and
 * implement singleton pattern to prevent creating multiple instance of the same
 * configuration class.
 *
 * @author Mickael Guesnon
 */
public abstract class BaseConfiguration {

    protected Config config = null;

    protected static final Logger log = Logger.getLogger(BaseConfiguration.class);

    protected String currentClassName;

    protected ConfigurationManager configurationManager;

    public abstract void setConfigurationManager(ConfigurationManager configurationManager);

    /**
	 * Instantiates a new base configuration.
	 */
    protected BaseConfiguration() {
    }

    /**
	 * Private method that try to load a config from the cache then if missing,
	 * from the database. If no config objects are stored for the current class,
	 * create a new one with the createEmptyConfiguration method.
	 *
	 * @throws ConfigurationException
	 *             If the currentClassName is no initiated.
	 */
    protected void loadConfiguration() throws ConfigurationException {
        try {
            if (log.isDebugEnabled()) log.debug("loading config from database for " + currentClassName + " class");
            config = configurationManager.loadConfigurationByClass(currentClassName);
            log.debug("Config succesfully loaded from database");
            if (log.isDebugEnabled()) log.debug("Config : " + config);
        } catch (ObjectNotFoundException e) {
            log.warn("Configuration not found", e);
        }
    }

    /**
	 * Save the current config object.
	 */
    protected void saveConfiguration() {
        if (config != null) {
            try {
                configurationManager.saveConfiguration(config);
            } catch (ObjectAlreadyExistsException e) {
                log.warn("Configuration save failed", e);
            }
        }
        try {
            loadConfiguration();
        } catch (ConfigurationException e) {
            log.warn("Configuration load failed", e);
        }
    }

    /**
	 * Set a config parameter.
	 *
	 * @param parameterKey
	 *            The key of the paramater to set.
	 * @param parameterValue
	 *            The value parameter to set.
	 * @throws ConfigurationException
	 *             If one of parameters is missing.
	 */
    public void setConfigurationParameter(String parameterKey, String parameterValue) throws ConfigurationException {
        if (parameterKey == null) throw new ConfigurationException("Unspecified Parameter Key");
        if (parameterValue == null) throw new ConfigurationException("Unspecified Parameter Value");
        try {
            loadConfiguration();
        } catch (ConfigurationException e) {
            log.warn("Configuration load failed", e);
        }
        if (config.containsDataKey(parameterKey)) {
            config.getConfigData(parameterKey).setKeyValue(parameterValue);
        } else {
            ConfigData configData = new ConfigData();
            configData.setKeyName(parameterKey);
            configData.setKeyValue(parameterValue);
            config.getConfigDatas().add(configData);
        }
        saveConfiguration();
    }

    /**
	 * Delete the parameter linked to the specified key.
	 *
	 * @param parameterKey
	 *            The key of the value to delete.
	 * @throws ConfigurationException
	 *             If parameter key is missing or unspecified in current config.
	 */
    public void removeConfigurationParameter(String parameterKey) throws ConfigurationException {
        if (parameterKey == null) throw new ConfigurationException("Unspecified Parameter Key");
        try {
            loadConfiguration();
        } catch (ConfigurationException e) {
            if (log.isDebugEnabled()) log.debug(e.getMessage());
        }
        if (config.containsDataKey(parameterKey)) {
            config.getConfigDatas().remove(config.getConfigData(parameterKey));
        }
        saveConfiguration();
    }

    /**
	 * Retrieve a parameter from current config object.
	 *
	 * @param parameterKey
	 *            The key of the value to retrieve.
	 * @return ParameterValue Configuration value for the specified key.
	 * @throws ConfigurationException
	 *             If parameter key is missing.
	 */
    public String getConfigurationParameter(String parameterKey) throws ConfigurationException {
        if (parameterKey == null) throw new ConfigurationException("Unspecified Parameter Key");
        try {
            loadConfiguration();
        } catch (ConfigurationException e) {
            log.warn("Configuration load failed", e);
        }
        if (config.containsDataKey(parameterKey)) {
            return config.getConfigData(parameterKey).getKeyValue();
        }
        throw new ConfigurationException("Unknown parameter key : " + parameterKey);
    }

    /**
	 * Method used to retrieve the whole confguration map.
	 *
	 * @return ConfigDataMap The Map containing configuration data
	 */
    public Map<String, String> getConfigDataMap() {
        try {
            loadConfiguration();
        } catch (ConfigurationException e) {
            log.warn("Configuration load failed", e);
        }
        Map<String, String> data = new Hashtable<String, String>();
        for (ConfigData configData : config.getConfigDatas()) {
            data.put(configData.getKeyName(), configData.getKeyValue());
        }
        return data;
    }

    /**
	 * Method used to save the whole confguration map.
	 *
	 * @param data
	 *            The configuration map to save
	 */
    public void setConfigDataMap(Map<String, String> data) {
        try {
            loadConfiguration();
        } catch (ConfigurationException e) {
            log.warn("Configuration load failed", e);
        }
        for (Entry<String, String> entry : data.entrySet()) {
            if (config.containsDataKey(entry.getKey())) {
                config.getConfigData(entry.getKey()).setKeyValue(entry.getValue());
            } else {
                ConfigData configData = new ConfigData();
                configData.setKeyName(entry.getKey());
                configData.setKeyValue(entry.getValue());
                config.getConfigDatas().add(configData);
            }
        }
        saveConfiguration();
    }
}
