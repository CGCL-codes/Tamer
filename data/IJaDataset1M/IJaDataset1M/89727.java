package org.ops4j.peaberry.cache;

import org.ops4j.peaberry.ServiceRegistry;

/**
 * {@link ServiceRegistry} that caches service instances for faster lookup.
 * 
 * @author mcculls@gmail.com (Stuart McCulloch)
 */
public interface CachingServiceRegistry extends ServiceRegistry {

    /**
   * Release any unused service instances from the cache.
   * 
   * @param targetGeneration the generation to flush
   */
    void flush(int targetGeneration);
}
