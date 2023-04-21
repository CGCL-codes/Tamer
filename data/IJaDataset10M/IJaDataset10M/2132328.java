package de.alexanderwilden.jatobo;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class JatoboProperties extends Properties {

    private static final long serialVersionUID = 1L;

    public JatoboProperties() {
        super();
    }

    public void load(String configFile) throws FileNotFoundException, IOException {
        this.load(new FileInputStream(configFile));
    }

    public String getString(String key) {
        return getString(key, "");
    }

    public String getString(String key, String defaultValue) {
        if (getProperty(key) == null) return defaultValue;
        return getProperty(key).trim();
    }

    public boolean getBoolean(String key) {
        return getBoolean(key, false);
    }

    public boolean getBoolean(String key, boolean defaultValue) {
        String val = getProperty(key).trim();
        if (val == null) return defaultValue; else if (val.equalsIgnoreCase("true") || val.equalsIgnoreCase("yes")) {
            return true;
        } else return false;
    }

    public int getInt(String key) {
        return getInt(key, 0);
    }

    public int getInt(String key, int defaultValue) {
        if (getProperty(key) == null) return defaultValue;
        try {
            return Integer.parseInt(getProperty(key).trim());
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    public long getLong(String key) {
        return getLong(key, 0);
    }

    public long getLong(String key, long defaultValue) {
        if (getProperty(key) == null) return defaultValue;
        try {
            return Long.parseLong(getProperty(key).trim());
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    public float getFloat(String key) {
        return getFloat(key, 0F);
    }

    public float getFloat(String key, float defaultValue) {
        if (getProperty(key) == null) return defaultValue;
        try {
            return Float.parseFloat(getProperty(key).trim());
        } catch (NumberFormatException e) {
            return 0F;
        }
    }

    public double getDouble(String key) {
        return getDouble(key, 0D);
    }

    public double getDouble(String key, double defaultValue) {
        if (getProperty(key) == null) return defaultValue;
        try {
            return Double.parseDouble(getProperty(key).trim());
        } catch (NumberFormatException e) {
            return 0D;
        }
    }

    public String[] getStringArray(String key, String separator) {
        return getStringArray(key, separator, new String[0]);
    }

    public String[] getStringArray(String key, String separator, String[] defaultValue) {
        if (getProperty(key) == null) return defaultValue;
        return getProperty(key).split(separator);
    }

    public List<Map<String, String>> getMultiField(String key, String[] subkeys) {
        List<Map<String, String>> res = new LinkedList<Map<String, String>>();
        int count = 0;
        String value = getProperty(key + "." + count + "." + subkeys[0]);
        while (value != null) {
            Map<String, String> currMap = new HashMap<String, String>();
            for (String subkey : subkeys) {
                currMap.put(subkey, getProperty(key + "." + count + "." + subkey));
            }
            res.add(currMap);
            count++;
            value = getProperty(key + "." + count + "." + subkeys[0]);
        }
        return res;
    }
}
