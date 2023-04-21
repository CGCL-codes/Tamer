package com.completex.objective.components.ocache;

/**
 * Odal cache factory is decorating factory for OdalCache objects.
 *
 * @author Gennady Krizhevsky
 */
public interface OdalCacheFactoryManager extends OdalCacheRegistry {

    void registerCacheFactory(OdalMultiIndexCacheFactory cacheFactory);

    void unregisterCacheFactory(OdalMultiIndexCacheFactory cacheFactory);
}
