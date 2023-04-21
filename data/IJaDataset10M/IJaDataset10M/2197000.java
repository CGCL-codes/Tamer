package com.volantis.mcs.runtime.configuration;

import com.volantis.mcs.runtime.configuration.xml.digester.Enabled;

/**
 * Container for the page-cache configuration read from the mariner
 * configuration file.
 */
public class RenderedPageCacheConfiguration implements Enabled {

    /**
     * The caching strategy to use.
     */
    private String strategy;

    /**
     * The maximum number of entries in the cache.
     */
    private Integer maxEntries;

    /**
     * The default cache timeout.
     */
    private Integer timeout;

    /**
     * The default cache scope to use
     */
    private String defaultScope;

    /**
     * Get the maximum number of cache entries.
     *
     * @return The maximum number of cache entries
     */
    public Integer getMaxEntries() {
        return maxEntries;
    }

    /**
     * Set the maximum number of cache entries.
     *
     * @param maxEntries The new maximum number of cache entries
     */
    public void setMaxEntries(Integer maxEntries) {
        this.maxEntries = maxEntries;
    }

    /**
     * Get the caching strategy.
     *
     * @return The caching strategy
     */
    public String getStrategy() {
        return strategy;
    }

    /**
     * Set the caching strategy.
     *
     * @param strategy The new caching strategy
     */
    public void setStrategy(String strategy) {
        this.strategy = strategy;
    }

    /**
     * Get the cache timeout.
     * 
     * @return The cache timeout
     */
    public Integer getTimeout() {
        return timeout;
    }

    /**
     * Set the cache timeout.
     *
     * @param timeout The new cache timeout
     */
    public void setTimeout(Integer timeout) {
        this.timeout = timeout;
    }

    public String getDefaultScope() {
        return defaultScope;
    }

    public void setDefaultScope(String defaultScope) {
        this.defaultScope = defaultScope;
    }
}
