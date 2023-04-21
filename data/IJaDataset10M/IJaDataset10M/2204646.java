package com.rapidweb.generator.core;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @author yxli
 * 
 */
public class ConfigurationManager {

    private Properties properties = null;

    private static ConfigurationManager configurationManager = null;

    private ConfigurationManager(InputStream fis) {
        try {
            properties = new Properties();
            properties.load(fis);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static synchronized ConfigurationManager getInstance(InputStream fis) {
        if (configurationManager == null) {
            configurationManager = new ConfigurationManager(fis);
        }
        return configurationManager;
    }

    public String getPropertyValue(String key) {
        return properties == null ? key : properties.getProperty(key);
    }

    public void setPropertyValue(String key, String value) {
        synchronized (properties) {
            properties.put(key, value);
        }
    }
}
