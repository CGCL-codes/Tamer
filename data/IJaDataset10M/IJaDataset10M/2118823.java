package org.hibernate.cache.jbc2;

import java.util.Properties;
import org.hibernate.cache.jbc2.builder.MultiplexingCacheInstanceManager;

/**
 * {@link JBossCacheRegionFactory} that uses
 * {@link MultiplexingCacheInstanceManager} as its
 * {@link #getCacheInstanceManager() CacheInstanceManager}.
 * <p>
 * Supports separate JBoss Cache instances for entity, collection, query
 * and timestamp caching, with the expectation that a single JGroups resource 
 * (i.e. a multiplexed channel or a shared transport channel) will be shared 
 * between the caches. JBoss Cache instances are created from a factory.
 * </p>
 * <p>
 * This version instantiates the factory itself. See 
 * {@link MultiplexingCacheInstanceManager} for configuration details. 
 * </p>
 * 
 * @author Brian Stansberry
 * @version $Revision$
 */
public class MultiplexedJBossCacheRegionFactory extends JBossCacheRegionFactory {

    /**
     * FIXME Per the RegionFactory class Javadoc, this constructor version
     * should not be necessary.
     * 
     * @param props The configuration properties
     */
    public MultiplexedJBossCacheRegionFactory(Properties props) {
        this();
    }

    /**
     * Create a new MultiplexedJBossCacheRegionFactory.
     * 
     */
    public MultiplexedJBossCacheRegionFactory() {
        super(new MultiplexingCacheInstanceManager());
    }
}
