package org.peertrust.demo.resourcemanagement;

/**
 * Implementation of the CacheElementCreator provide
 * a cache with a mechanism to create objects. This is
 * used if a cache miss accors
 * 
 * @author Patrice Congo (token77)
 *
 */
public interface CacheElementCreator {

    /**
	 * Creates a cache element
	 * @param key -- the key of the element to create
	 * @return the created cache element
	 */
    public Object createCacheElement(Object key);
}
