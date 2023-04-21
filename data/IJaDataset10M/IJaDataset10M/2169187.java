package com.jeronimo.eko.core.config.impl;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;
import com.google.inject.Singleton;
import com.jeronimo.eko.core.config.ISConfigurationStore;

/**
 * A property-file based configuration store. Very simple.
 * @author J�r�me Bonnet
 */
@Singleton
public class PropertiesConfigurationStore implements ISConfigurationStore {

    Properties properties;

    File f;

    public PropertiesConfigurationStore(File f) throws FileNotFoundException, IOException {
        this.f = f;
        properties = new Properties();
        FileReader fr = new FileReader(f);
        try {
            properties.load(fr);
        } finally {
            fr.close();
        }
    }

    public File getFile() {
        return f;
    }

    public Properties getProperties() {
        return properties;
    }

    @Override
    public void deleteValue(String key) {
        properties.remove(key);
    }

    @Override
    public String getValue(String key) {
        return properties.getProperty(key);
    }

    @Override
    public Set<String> keySet() {
        Set<String> result = new HashSet<String>();
        for (Object k : properties.keySet()) {
            result.add((String) k);
        }
        return result;
    }

    @Override
    public void setValue(String key, String value) {
        properties.setProperty(key, value);
    }
}
