package org.nexopenframework.business.resources;

import org.nexopenframework.business.BusinessService;
import org.nexopenframework.pool.PoolMetadata;

/**
 * <p>NexTReT Open Framework</p>
 * 
 * <p>Default implementation of the {@link PoolMetadataLocator}</p>
 * 
 * @author <a href="mailto:fme@nextret.net">Francesc Xavier Magdaleno</a>
 * @version 1.0
 * @since 1.0
 */
public class DefaultPoolMetadaLocator implements PoolMetadataLocator {

    /**
	 * @see org.nexopenframework.business.resources.PoolMetadataLocator#isPoolMetadata(org.nexopenframework.business.BusinessService)
	 */
    public boolean isPoolMetadata(BusinessService service) {
        return (service instanceof ObjectPoolingAware);
    }

    public PoolMetadata lookupPoolMetadata(BusinessService service) {
        ObjectPoolingAware objPooling = (ObjectPoolingAware) service;
        return objPooling.getPoolMetadata();
    }
}
