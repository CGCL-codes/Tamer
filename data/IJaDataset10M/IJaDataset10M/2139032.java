package org.apache.jetspeed.services.portletcache;

import org.apache.turbine.services.TurbineServices;

/**
 * This class is a static accessor for the PortletCache service
 * 
 * @author <a href="mailto:burton@apache.org">Kevin A. Burton</a>
 * @author <a href="mailto:raphael@apache.org">Rapha�l Luta</a>
 * @version $Id: PortletCache.java,v 1.4 2004/02/23 03:34:54 jford Exp $
 */
public class PortletCache {

    /**
     * Add an object to the cache
     *
     * @see PortletCacheService#addCacheable
     */
    public static void addCacheable(Cacheable item) {
        PortletCacheService gcs = (PortletCacheService) TurbineServices.getInstance().getService(PortletCacheService.SERVICE_NAME);
        gcs.addCacheable(item);
    }

    /**
     * Removes an object from the cache based on its handle
     *
     * @see PortletCacheService#removeCacheable
     */
    public static void removeCacheable(String handle) {
        PortletCacheService gcs = (PortletCacheService) TurbineServices.getInstance().getService(PortletCacheService.SERVICE_NAME);
        gcs.removeCacheable(handle);
    }

    /**
     * Retrieves an object from the cache
     *
     * @see PortletCacheService#getCacheable
     */
    public static Cacheable getCacheable(String handle) {
        PortletCacheService gcs = (PortletCacheService) TurbineServices.getInstance().getService(PortletCacheService.SERVICE_NAME);
        return gcs.getCacheable(handle);
    }
}
