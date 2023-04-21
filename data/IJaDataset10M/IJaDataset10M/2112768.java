package org.broadleafcommerce.vendor.service.cache;

import java.util.List;

/**
 * @author jfischer
 *
 */
public interface CacheRequest {

    public List<CacheItemRequest> getCacheItemRequests();
}
