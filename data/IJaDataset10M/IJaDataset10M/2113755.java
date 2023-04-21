package org.broadleafcommerce.vendor.service.cache;

import net.sf.ehcache.Cache;

/**
 * @author jfischer
 *
 */
public interface ServiceResponseCacheable {

    public void clearCache();

    public Cache getCache();
}
