package org.dev2live.catalogs;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author bertram A Catalog as Singleton impletation
 */
public class Catalog {

    private static Catalog catalog;

    private ConcurrentMap<String, String> commands = new ConcurrentHashMap<String, String>();

    protected Catalog() {
    }

    /**
	 * @param key
	 * @param name
	 */
    public void add(String key, String name) {
        commands.put(key, name);
    }

    /**
	 * @param key
	 * @return
	 */
    public String get(String key) {
        return commands.get(key);
    }

    /**
	 * Get a Map with all entrys where the key starts with
	 * @param key
	 * @return the a new Map from the compare
	 */
    public Map<String, String> getStartsWith(String key) {
        Map<String, String> result = new HashMap<String, String>();
        String[] keys = commands.keySet().toArray(new String[0]);
        for (String k : keys) {
            if (k.startsWith(key)) result.put(k, commands.get(k));
        }
        return result;
    }

    /**
	 * Get a Map with all entrys where the key ends with
	 * @param key
	 * @return the a new Map from the compare
	 */
    public Map<String, String> getEndsWith(String key) {
        Map<String, String> result = new HashMap<String, String>();
        String[] keys = commands.keySet().toArray(new String[0]);
        for (String k : keys) {
            if (k.endsWith(key)) result.put(k, commands.get(k));
        }
        return result;
    }

    /**
	 * @return
	 */
    public Set<String> getKeys() {
        return (commands.keySet());
    }

    /**
	 * @param key
	 * @return
	 */
    public boolean containsKey(String key) {
        return commands.containsKey(key);
    }

    /**
	 * @param value
	 * @return
	 */
    public boolean containsValue(String value) {
        return commands.containsValue(value);
    }

    /**
	 * @return a instance of this
	 */
    public static Catalog instance() {
        if (null == catalog) {
            catalog = new Catalog();
        }
        return catalog;
    }

    /**
	 * Converts this Catalog to a String. Useful for debugging purposes.
	 * 
	 * @return a representation of this catalog as a String
	 */
    public String toString() {
        String[] keys = getKeys().toArray(new String[0]);
        StringBuffer str = new StringBuffer(this.getClass().getName() + "[");
        int kl = keys.length - 1;
        int kc = 0;
        for (String key : keys) {
            str.append(key).append(":").append(this.get(key));
            if ((kc++) < kl) str.append(", ");
        }
        str.append("]");
        return str.toString();
    }
}
