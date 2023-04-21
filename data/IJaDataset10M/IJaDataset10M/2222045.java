package org.bissa.configuration;

import org.bissa.exception.BissaConfigurationFault;
import rice.persistence.Cache;
import rice.persistence.EmptyCache;

/**
 *Bean Associated with PAST Empty Cache 
 */
public class EmptyCacheBean implements BissaConfigurationBean, CacheBean {

    public Object populateConfiguration(BissaConfiguration configuration) throws BissaConfigurationFault {
        Cache cache = new EmptyCache(configuration.getIdFactory());
        return cache;
    }
}
