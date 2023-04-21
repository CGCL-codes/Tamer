package org.travelfusion.xmlclient.cache;

/**
 * Defines configurable properties of the caching policy for a given request type. Any given {@link CacheProvider}
 * implementation may or may not honor these policy settings. Some implementations may also use extended versions of
 * this interface, in order to define additional policy settings. Refer to the documentation for your chosen provider.
 * 
 * @author Jesse McLaughlin (nzjess@gmail.com)
 */
public interface CachePolicy {

    /**
   * The amount of time a cache entry will remain valid in the cache.
   */
    long getTimeoutMillis();

    void setTimeoutMillis(long timeoutMillis);

    /**
   * The amount of time to block a request thread while waiting for a prior thread to refresh a stale cache entry.
   */
    long getStaleWaitTimeoutMillis();

    void setStaleWaitTimeoutMillis(long staleWaitTimeoutMillis);

    /**
   * True to cache response seperately for different XML login IDs. False to cache the same response regardless of XML
   * login ID.
   */
    boolean isCachePerXmlLoginId();

    void setCachePerXmlLoginId(boolean cachePerXmlLoginId);
}
