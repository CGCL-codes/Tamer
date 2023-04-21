package org.jsecurity.cache;

/**
 * Interface implemented by components that utilize a CacheManager and wish that CacheManager to be supplied if
 * one is available.
 *
 * <p>This is used so internal security components that use a CacheManager can be injected with it instead of having
 * to create one on their own.
 *
 * @author Les Hazlewood
 * @since 0.9
 */
public interface CacheManagerAware {

    /**
     * Sets the available CacheManager instance on this component.
     *
     * @param cacheManager the CacheManager instance to set on this component.
     */
    void setCacheManager(CacheManager cacheManager);
}
