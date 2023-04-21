package edu.arizona.mindseye;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class PropertiesManager {

    private static PropertiesManager _instance;

    private String _fileName;

    private Properties _properties;

    private PropertiesManager() {
    }

    public static PropertiesManager get() {
        if (_instance == null) _instance = new PropertiesManager();
        return _instance;
    }

    /**
	 * Load in the properties from a given file.
	 * @param propertiesFile
	 */
    public void load(String propertiesFile) throws Exception {
        _fileName = propertiesFile;
        _properties = new Properties();
        _properties.load(new FileInputStream(_fileName));
    }

    /**
	 * Write the properties to file.
	 */
    public void write() {
        write(_fileName);
    }

    /**
	 * Write the properties to the given file.
	 * @param propertiesFile
	 */
    public void write(String propertiesFile) {
        try {
            _properties.store(new FileOutputStream(propertiesFile), null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getProperty(String key) {
        if (_properties == null) throw new RuntimeException("Properties are null, must load first");
        return _properties.getProperty(key);
    }

    public void setProperty(String key, String value) {
        if (_properties == null) throw new RuntimeException("Properties are null, must load first");
        _properties.setProperty(key, value);
        write();
    }
}
