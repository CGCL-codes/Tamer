package org.jaffa.cache;

/** This will return an instance of ICache for the given named cache.
 * For the time being this will merely return an instance of the WeakCache.
 * Later this will be enhanced to read a configuration file to load the appropriate ICache implementation based on the name, with all the appropriate tuning parameters.
 *
 * @author  GautamJ
 */
public class CacheManager {

    /** This will return an instance of the ICache for the input cache-name.
     * @param cacheName The name of the cache
     * @return an ICache implementation for the input cache-name.
     */
    public static ICache getCache(String cacheName) {
        return new WeakCache();
    }
}
