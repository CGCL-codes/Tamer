package com.controltier.ctl.tasks.session;

import java.util.Map;
import java.util.Properties;

/**
 * A session represents a set of keyed data.
 */
public interface Session {

    /**
     * Gets session identifier
     * @return
     */
    String getId();

    /**
     * Gets value specified by key
     * @param key
     * @return value identified by key
     */
    String get(String key);

    /**
     * Stores value accessible by key
     * @param key
     * @param value
     */
    void put(String key, String value);

    /**
     * Remove entry from session
     * @param key
     */
    void remove(String key);

    /**
     * Checks if a value is stored under key
     * @param key
     * @return
     */
    boolean contains(String key);

    /**
     * Return the keys as a list
     * @return String of key list
     */
    String getKeyList(String delimiter);

    /**
     * Return the values as a list
     * @return String list of values
     */
    String getValueList(String delimiter);

    /**
     * Returns contents of session as a map
     * @return map containing items
     */
    Map toMap();

    /**
     * Add all items from map to session
     * @param map key value pairs to add
     */
    void putAll(Map map);
}
