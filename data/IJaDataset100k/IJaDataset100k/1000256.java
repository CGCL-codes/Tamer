package org.ozoneDB.core.storage;

import java.util.Collection;
import java.util.Map;

/**
 * A cache works a bit like a map. The difference is that for all key-object
 * pairs put into the cache there is no assurance whatsoever that those
 * key-object pairs will stay in the cache.
 * More specifically: given the fact that the last time
 * the put-method was called for a key K was with value O, then any subsequent
 * calls to the get-method with key K will either result in object O being
 * returned, or null. Once a null has been returned for key K, a null value will
 * always be returned for key K, until K is used again in <code>put()</code>.
 * 
 * @author <a href="mailto:leoATmekenkampD0Tcom">Leo Mekenkamp (mind the anti sp@m)</a>
 * @version $Id: Cache.java,v 1.3 2004/10/21 21:15:23 leomekenkamp Exp $
 */
public interface Cache {

    /**
     * Puts an object into the cache, along with its identifying key.
     */
    public void put(Object key, Object value);

    /**
     * Returns the object in this cache for the given key.
     */
    public Object get(Object key);

    /**
     * Returns the object in this cache for the given key and removes it from
     * the cache.
     */
    public Object remove(Object key);

    public Map copyToMap();

    public int size();

    /**
     * Makes this cache empty.
     */
    void clear();
}
