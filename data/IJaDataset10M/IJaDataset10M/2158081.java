package org.motiv.core;

import org.motiv.config.CacheConfiguration;
import org.motiv.store.*;
import java.io.Serializable;

/**
 * Cache class
 * @author Pavlov Dm
 */
public class Cache {

    private volatile CacheConfiguration configuration;

    private volatile Store compoundStore;

    /**
    * Constructor
    */
    public Cache(CacheConfiguration configuration) {
        this.configuration = configuration;
        initialise();
    }

    /**
	* Get cache name method
	*/
    public String getName() {
        return configuration.getName();
    }

    /**
	* Get cache configuration method
	*/
    public CacheConfiguration getCacheConfiguration() {
        return configuration;
    }

    /**
     * Whether this cache uses a disk store
     * @return true if the cache either overflows to disk
     */
    protected boolean isDiskStore() {
        return configuration.isOverflowToDisk();
    }

    /**
     * Newly created caches.
     */
    public void initialise() {
        synchronized (this) {
            if (!isDiskStore()) {
                compoundStore = new MemoryStore(this, null);
            } else {
                compoundStore = new MemoryStore(this, new DiskStore(this));
            }
        }
    }

    /**
     * Put an element in the cache.
     *
     * @param element A cache Element.
     */
    public final void put(Element element) throws IllegalArgumentException, CacheException {
        if (element == null) {
            return;
        }
        if (element.getObjectKey() == null) {
            return;
        }
        element.resetAccessStatistics();
        boolean elementExists = !compoundStore.put(element);
        if (elementExists) {
            element.updateUpdateStatistics();
        }
    }

    /**
     * Gets an element from the cache. Updates Element Statistics
  
     * @param key a serializable value. Null keys are not stored so get(null) always returns null
     * @return the element, or null, if it does not exist.
     */
    public final Element get(Serializable key) throws CacheException {
        return get((Object) key);
    }

    /**
     * Gets an element from the cache. Updates Element Statistics
     * @param key an Object value
     * @return the element, or null, if it does not exist.
     */
    public final Element get(Object key) throws CacheException {
        Element element = compoundStore.get(key);
        if (element != null) {
            if (element.isExpired()) {
                remove(key);
                element = null;
            } else {
                element.updateAccessStatistics();
            }
        }
        return element;
    }

    /**
     * Removes an {@link Element} from the Cache. This also removes it from any
     * stores it may be in.
     * @param key the element key to operate on
     * @return true if the element was removed, false if it was not found in the cache
     */
    public final boolean remove(Object key) throws IllegalStateException {
        return remove(key);
    }

    /**
     * Removes an {@link Element} from the Cache. This also removes it from any
     * stores it may be in.
     * @param key                         the element key to operate on
     * @return true if the element was removed, false if it was not found in the cache
     */
    public final boolean remove(Serializable key) {
        return (compoundStore.remove(key) != null);
    }

    /**
     * Removes all cached items.
     */
    public void removeAll() throws CacheException {
        compoundStore.removeAll();
    }

    /**
    * Starts an orderly shutdown of the Cache. Steps are:
    */
    public synchronized void dispose() {
        compoundStore.dispose();
    }
}
