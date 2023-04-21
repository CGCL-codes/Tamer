package com.googlecode.ehcache.annotations;

import com.googlecode.ehcache.annotations.resolver.CacheableCacheResolver;

/**
 * Represents the objects needed to intercept calls to methods annotated
 * with {@link com.googlecode.ehcache.annotations.Cacheable}
 * 
 * @author Eric Dalquist
 * @version $Revision: 656 $
 */
public interface CacheableAttribute extends MethodAttribute {

    /**
     * @return The {@link CacheableCacheResolver} used to determine the Cache and ExceptionCache to use. Cannot return null.
     */
    public CacheableCacheResolver getCacheResolver();

    /**
     * @return true If the null return values should be cached.
     */
    public boolean isCacheNull();

    /**
     * @return The {@link CacheableInterceptor} to use when handling intercepted method invocations. Cannot return null.
     */
    public CacheableInterceptor getCacheInterceptor();
}
