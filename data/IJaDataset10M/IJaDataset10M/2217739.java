package com.bitmovers.maui.engine.cachemanager;

/**
* I_HasCacheStrategy <p>
* This interface is for any object within the Cache Manager which can have a caching
* strategy.
*
*/
public interface I_HasCacheStrategy {

    /**
	* Get the caching strategy for this object
	*
	* @return The I_CacheStrategy
	*/
    public I_CacheStrategy getCacheStrategy();

    /**
	* Set the cache strategy
	*
	* @param aCacheStrategy The I_CacheStrategy to set
	*/
    public void setCacheStrategy(I_CacheStrategy aCacheStrategy);
}
