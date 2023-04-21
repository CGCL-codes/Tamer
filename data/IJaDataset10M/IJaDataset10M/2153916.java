package com.jujunie.service.web;

/**
 * Represents the values pushed into the context for
 * display purposes.
 * 
 * @author julien
 * 
 * @version $Revision $
 *
 */
public interface DisplayContext {

    /**
     * Set the given object in the context using
     * the given key.
     * @param key key used to store the object
     * @param obj the object to store
     */
    void set(String key, Object obj);

    /**
     * Get the object corresponding to the given key.
     * It returns null if the object is not found
     * @param key key of the object to retreive
     * @return the object corresponding to the given key.
     */
    Object get(String key);
}
