package edu.ucla.cs.typecast.util;

import java.io.*;
import java.util.*;

/**
 * Reads UNIX-style config files.
 */
public class ConfigurationReader {

    /**
	 * Config entries.
	 */
    protected Map<String, String> configs = new HashMap<String, String>();

    /**
	 * Value that get(key) returns when the corresponding value is not found.
	 */
    protected String defaultValue = null;

    /**
	 * Constructor. Reads the configuration from the given file.
	 * 
	 * @param str
	 * @throws IOException
	 */
    public ConfigurationReader(File file) throws IOException {
        this(new FileReader(file));
    }

    /**
	 * Constructor. Reads the configuration from the given reader.
	 * 
	 * @param str
	 * @throws IOException
	 */
    public ConfigurationReader(Reader rd) throws IOException {
        BufferedReader br = (rd instanceof BufferedReader) ? (BufferedReader) rd : new BufferedReader(rd);
        while (true) {
            String line = br.readLine();
            if (line == null) break;
            if (line.matches("^[\\s\\t]*#")) {
                continue;
            }
            String[] parts = line.split("[\\s\\t]*=[\\s\\t]*", 2);
            if (parts.length >= 2) {
                configs.put(parts[0], parts[1]);
            }
        }
    }

    /**
	 * Constructor. Reads the configuration from the given string.
	 * 
	 * @param str
	 * @throws IOException
	 */
    public ConfigurationReader(String str) throws IOException {
        this(new StringReader(str));
    }

    /**
	 * Get the value of a config entry.
	 * 
	 * @param key
	 * @return
	 */
    public String get(String key) {
        if (key != null && configs.containsKey(key)) return configs.get(key); else return defaultValue;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public String toString() {
        String result = this.getClass().getName();
        result += "[";
        boolean first = true;
        for (String key : configs.keySet()) {
            result += key + "=" + configs.get(key);
            if (first) {
                result += ";";
                first = false;
            }
        }
        result += "]";
        return result;
    }
}
