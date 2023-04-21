package com.ajaf.context;

import java.util.HashMap;

/**
 * This is a general purpose class that facilitates a Context with attributes.
 * Currently PageContext is implemented using this
 *  - if required this class should be extended to provide further features.
 * @author Pankaj Bhargava
 */
public class Context {

    private HashMap attributes = new HashMap();

    /**
     * Sets an attribute by name - value pair
     */
    public void setAttribute(String name, Object value) {
        attributes.put(name, value);
    }

    /**
     * Gets an attribute by name
     */
    public Object getAttribute(String name) {
        return attributes.get(name);
    }

    /**
     * Removes an attribute by name
     */
    public void removeAttribute(String name) {
        attributes.remove(name);
    }
}
