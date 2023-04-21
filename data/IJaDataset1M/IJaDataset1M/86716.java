package org.teaframework.services.cache;

import java.util.HashMap;
import org.teaframework.exception.CacheServiceException;

/**
 * <p>
 * Support for simple hashmap cache.
 * </p>
 * 
 * @author <a href="mailto:founder_chen@yahoo.com.cn">Peter Cheng </a>
 * @version $Revision: 1.1 $ $Date: 2006/02/15 08:45:45 $
 * @version Revision: 1.0
 */
public class HashMapCacheServiceImpl implements CacheService {

    private HashMap cache;

    /**
     * Default Constructor
     */
    public HashMapCacheServiceImpl() {
        cache = new HashMap();
    }

    /**
     * @see org.teaframework.services.cache.CacheService#get(java.lang.Object)
     */
    public Object get(Object key) throws CacheServiceException {
        return cache.get(key);
    }

    /**
     * @see org.teaframework.services.cache.CacheService#put(java.lang.Object,
     *      java.lang.Object)
     */
    public void put(Object key, Object value) throws CacheServiceException {
        cache.put(key, value);
    }

    /**
     * @see org.teaframework.services.cache.CacheService#remove(java.lang.Object)
     */
    public void remove(Object key) throws CacheServiceException {
        cache.remove(key);
    }

    /**
     * @see org.teaframework.services.cache.CacheService#clear()
     */
    public void clear() throws CacheServiceException {
        cache.clear();
    }
}
