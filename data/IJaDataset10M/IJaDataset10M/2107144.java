package de.mordred;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class Props {

    public static String RANDOM_FRAGEN = "random";

    public static String LIZENZ = "license";

    private static Props singleton;

    private static Object LOCK = new Object();

    private static String FILE = "jp.properties";

    private Properties _properties;

    /**
	 * Returns the Instance
	 * @return
	 */
    public static Props getInstance() {
        if (singleton == null) {
            synchronized (LOCK) {
                singleton = new Props();
            }
        }
        return singleton;
    }

    private Props() {
        _properties = loadProperties();
    }

    public String getString(String s) {
        if (_properties.get(s) != null) return (String) _properties.get(s); else return "";
    }

    public boolean getBoolean(String s) {
        if (_properties.get(s) != null) {
            return Boolean.parseBoolean(_properties.getProperty(s));
        }
        return false;
    }

    public void setProperty(String prop, String value) {
        _properties.setProperty(prop, value);
        save();
    }

    public void setProperty(String prop, boolean bool) {
        _properties.setProperty(prop, Boolean.toString(bool));
        save();
    }

    public void save() {
        try {
            _properties.store(new FileOutputStream(new File(FILE)), "Stored Properties");
        } catch (FileNotFoundException e) {
        } catch (IOException e) {
        }
    }

    private Properties loadProperties() {
        Properties props = new Properties();
        try {
            props.load(new FileInputStream(FILE));
        } catch (FileNotFoundException e) {
            File f = new File(FILE);
            try {
                f.createNewFile();
                props.load(new FileInputStream(f));
            } catch (IOException e1) {
            }
        } catch (IOException e) {
        }
        return props;
    }
}
