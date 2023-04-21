package net.sf.ehcache_zen;

import java.util.Collection;
import net.sf.ehcache.constructs.blocking.CacheEntryFactory;

/**
 * An implementation of CacheEntryFactory that can route requests for keys
 * amongst a collection of managed CacheEntryFactory's.
 * 
 * This class should be instantiated with a collection of one or more
 * MultiCacheEntryFactory's and/or a default CacheEntryFactory. When
 * createEntry(Object) is called, each registered MultiCacheEntryFactory will be
 * invoked with isCanCreateEntry(Object), and when one returns true, then that
 * factory's createEntry(Object) will be called and its result returned. If no
 * MultiCacheEntryFactory returns true, then the default CacheEntryFactory, if
 * present, will be invoked with createEntry(Object). Finally, if no object
 * could be obtained from the above steps, then null will be returned.
 * 
 * @author bhafer
 */
public class MultiCacheEntryFactoryRouter implements CacheEntryFactory {

    Collection<MultiCacheEntryFactory> multiFactories;

    CacheEntryFactory defaultFactory;

    /**
     * Returns the collection of MultiCacheEntryFactory's registered with this
     * instance.
     * 
     * @return The collection of MultiCacheEntryFactory's registered with this
     *         instance, if present; else, null.
     */
    public Collection<MultiCacheEntryFactory> getMultiFactories() {
        return multiFactories;
    }

    /**
     * Assigns a collection of MultiCacheEntryFactory's for this instance.
     * 
     * @param factories
     *            Collection of MultiCacheEntryFactory's.
     */
    public void setMultiFactories(Collection<MultiCacheEntryFactory> multiFactories) {
        this.multiFactories = multiFactories;
    }

    /**
     * Returns the default CacheEntryFactory registered with this instance.
     * 
     * @return The default CacheEntryFactory registered with this instance, if
     *         present; else, null.
     */
    public CacheEntryFactory getDefaultFactory() {
        return defaultFactory;
    }

    /**
     * Assigns a default CacheEntryFactory for this instance.
     * 
     * @param defaultFactory
     */
    public void setDefaultFactory(CacheEntryFactory defaultFactory) {
        this.defaultFactory = defaultFactory;
    }

    /**
     * Attempts to route the request for the specified key, first amongst the
     * registered MultiCacheEntryFactory's, then to the default
     * CacheEntryFactory.
     * 
     * @param key
     *            Cache key.
     * @return The entry, or null if it does not exist.
     * @throws Exception
     *             On failure creating the object.
     */
    @Override
    public Object createEntry(Object key) throws Exception {
        if (multiFactories != null) {
            for (MultiCacheEntryFactory multiFactory : multiFactories) {
                if (multiFactory.isCanCreateEntry(key)) {
                    return multiFactory.createEntry(key);
                }
            }
        }
        if (defaultFactory != null) {
            return defaultFactory.createEntry(key);
        }
        return null;
    }
}
