package org.motiv.store;

import org.motiv.core.Element;
import org.motiv.core.CacheException;

/**
 * This is the interface for all stores. 
 * @author Pavlov Dm
 */
public interface Store {

    /**
     * Puts an item into the store.
     * @return true if this is a new put for the key or element is null. Returns false if it was an update.
     */
    boolean put(Element element) throws CacheException;

    /**
     * Gets an item from the store.
     */
    Element get(Object key);

    /**
     * Removes an item from the store.
     */
    Element remove(Object key);

    /**
     * Remove all of the elements from the store.
     */
    void removeAll() throws CacheException;

    /**
     * Prepares for shutdown.
     */
    public void dispose();

    /**
     * A check to see if a key is in the Store.
     * @param key The Element key
     * @return true if found. No check is made to see if the Element is expired.
     */
    boolean containsKey(Object key);
}
