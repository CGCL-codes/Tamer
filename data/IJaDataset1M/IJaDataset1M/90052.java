package com.xenoage.util.settings;

import java.io.IOException;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Properties;
import java.util.Set;
import com.xenoage.util.FileUtils;
import com.xenoage.util.Parser;
import com.xenoage.util.error.ErrorLevel;
import com.xenoage.util.error.BasicErrorProcessing;
import com.xenoage.util.io.IO;
import com.xenoage.util.logging.Log;
import com.xenoage.util.logging.LogLevel;

/**
 * This class manages simple configuration data, which is String values
 * identified by String keys.
 * 
 * The values are stored in .settings files, which are XML files in the format
 * described at http://java.sun.com/dtd/properties.dtd
 * 
 * @author Andreas Wenger
 * @author Uli Teschemacher
 */
public class Settings {

    private static Settings instance = null;

    private static BasicErrorProcessing err = null;

    private String directory;

    private Hashtable<String, Properties> files;

    /**
	 * Gets the only instance of the Settings class.
	 */
    public static Settings getInstance() {
        if (instance == null) {
            instance = new Settings("data/config");
        }
        return instance;
    }

    /**
   * Sets the error processing class. If unset,
   * a {@link RuntimeException} will be thrown in any method where an error occurs.
   */
    public static void setErrorProcessing(BasicErrorProcessing err) {
        Settings.err = err;
    }

    /**
	 * Creates a new Settings instance using the given directory to locate the
	 * .settings files.
	 */
    public Settings(String directory) {
        this.directory = directory;
        while (this.directory.endsWith("/")) {
            this.directory = this.directory.substring(0, this.directory.length() - 1);
        }
        this.files = new Hashtable<String, Properties>();
        Set<String> fileList = null;
        try {
            fileList = IO.listDataFiles(directory);
        } catch (IOException ex) {
            if (err != null) err.report(ErrorLevel.Error, "Could not list setting files!", ex); else throw new RuntimeException("Could not list setting files!", ex);
        }
        for (String file : fileList) {
            String name = file;
            if (name.endsWith(".settings")) {
                try {
                    reload(FileUtils.getNameWithoutExt(file));
                } catch (Exception ex) {
                    if (err != null) err.report(ErrorLevel.Error, "Could not load settings from " + file, ex); else throw new RuntimeException("Could not load settings from " + file, ex);
                }
            }
        }
    }

    /**
	 * Gets the value of the setting with the given key. If not found, null is
	 * returned.
	 * @param key the key of the setting
	 * @param file the filename where the setting can be found, without
	 * extension
	 */
    public String getSetting(String key, String file) {
        Properties p = files.get(file);
        if (p != null) {
            return p.getProperty(key);
        }
        return null;
    }

    /**
	 * Sets the value of the setting with the given key. If the key or the file
	 * doesn't exist yet, it is created. The setting is not stored in the file
	 * immediately, but may be saved automatically later. To save it
	 * immediately, <code>save</code> has to be called, or use
	 * <code>saveSetting</code>.
	 * @param key the key of the setting
	 * @param file the filename where the setting will be stored, without extension
	 * @param value the new value of the setting
	 */
    public void setSetting(String key, String file, Object value) {
        Properties p = files.get(file);
        if (p == null) {
            p = new Properties();
            files.put(file, p);
        }
        p.put(key, value);
    }

    /**
	 * Sets the value of th setting with the given key. If the key or the file
	 * doesn't exist yet, it is created. The setting is immediately stored in
	 * the given file.
	 */
    public void saveSetting(String key, String file, String value) {
        setSetting(key, file, value);
        save(file);
    }

    /**
	 * Saves the settings of the given file permanently.
	 */
    public void save(String file) {
        Properties p = files.get(file);
        if (p != null) {
            try {
                p.storeToXML(IO.openOutputStream(this.directory + "/" + file + ".settings"), "Changed " + new Date());
            } catch (IOException ex) {
                Log.log(LogLevel.Warning, "Could not store settings to file \"" + file + "\"", ex);
            }
        }
    }

    /**
	 * Reloads the given settings file.
	 */
    public void reload(String file) {
        try {
            Properties p = new Properties();
            p.loadFromXML(IO.openInputStream(directory + "/" + file + ".settings"));
            files.put(file, p);
        } catch (Exception ex) {
            Log.log(LogLevel.Warning, "Could not load settings from file \"" + file + "\"", ex);
        }
    }

    /**
	 * Convenience method: Gets the given setting as a float value, or use the
	 * given default value, if the is setting not available or readable.
	 */
    public float getSetting(String key, String file, float defaultValue) {
        String s = getSetting(key, file);
        if (s != null) {
            Float ret = Parser.parseFloatNull(s);
            if (ret != null) {
                return ret;
            }
        }
        return defaultValue;
    }

    /**
	 * Gets all settings of the given file as a {@link HashMap}. If the file
	 * does not exist, null is returned. If there are no entries, an empty
	 * {@link HashMap} is returned.
	 * @param key the key of the setting
	 * @param file the filename where the setting can be found, without
	 * extension
	 */
    public HashMap<String, String> getAllSettings(String file) {
        HashMap<String, String> ret = new HashMap<String, String>();
        Properties p = files.get(file);
        if (p != null) {
            Enumeration<Object> keys = p.keys();
            while (keys.hasMoreElements()) {
                String key = (String) (keys.nextElement());
                String value = p.getProperty(key);
                ret.put(key, value);
            }
        }
        return ret;
    }

    /**
	 * Convenience method: Gets the given setting as a int value, or use the
	 * given default value, if the is setting not available or readable.
	 */
    public int getSetting(String key, String file, int defaultValue) {
        String s = getSetting(key, file);
        if (s != null) {
            Integer ret = Parser.parseIntegerNull(s);
            if (ret != null) {
                return ret;
            }
        }
        return defaultValue;
    }

    /**
	 * Convenience method: Gets the given setting as a String value, or use the
	 * given default value, if the is setting not available or readable.
	 */
    public String getSetting(String key, String file, String defaultValue) {
        String s = getSetting(key, file);
        if (s != null) {
            return s;
        }
        return defaultValue;
    }
}
