package jmotor.core.ioc.pool.impl;

import jmotor.core.ioc.config.EntryWrapperConfiguration;
import jmotor.core.ioc.pool.EntryWrapperConfigurationCachePool;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Component:
 * Description:
 * Date: 11-11-14
 *
 * @author Andy.Ai
 */
public class EntryWrapperConfigurationCachePoolImpl implements EntryWrapperConfigurationCachePool {

    private Map<String, EntryWrapperConfiguration> collectionConfigurationCache = new HashMap<String, EntryWrapperConfiguration>();

    public void put(EntryWrapperConfiguration entryWrapperConfiguration) {
        collectionConfigurationCache.put(entryWrapperConfiguration.getId(), entryWrapperConfiguration);
    }

    public void putAll(Collection<EntryWrapperConfiguration> entryWrapperConfigurations) {
        for (EntryWrapperConfiguration entryWrapperConfiguration : entryWrapperConfigurations) {
            put(entryWrapperConfiguration);
        }
    }

    public EntryWrapperConfiguration get(String key) {
        return collectionConfigurationCache.get(key);
    }
}
