package se.vgregion.sitemap.impl;

import se.vgregion.sitemap.CacheLoader;
import se.vgregion.sitemap.SitemapEntryLoader;
import se.vgregion.sitemap.model.SitemapCache;

/**
 * Implementation of the CacheLoader interface which populates a SitemapCache by using the appropriate loader.
 */
public abstract class DefaultSitemapCacheLoader implements CacheLoader<SitemapCache> {

    private final SitemapEntryLoader sitemapEntryLoader;

    private final String applicationBaseURL;

    /**
     * Constructs a new {@link DefaultSitemapCacheLoader}.
     * 
     * @param webbisCacheService
     *            The {@link WebbisCacheServiceImpl} implementation to use to fetch units.
     * @param applicationBaseURL
     *            The external URL to the application.
     */
    public DefaultSitemapCacheLoader(final SitemapEntryLoader sitemapEntryLoader, String applicationBaseURL) {
        this.sitemapEntryLoader = sitemapEntryLoader;
        this.applicationBaseURL = applicationBaseURL;
    }

    /**
     * {@inheritDoc}
     */
    public SitemapCache createEmptyCache() {
        return new SitemapCache();
    }

    public SitemapCache loadCache() {
        SitemapCache cache = new SitemapCache();
        populateSitemapEntryCache(cache);
        return cache;
    }

    public SitemapEntryLoader getSitemapEntryLoader() {
        return sitemapEntryLoader;
    }

    public String getApplicationBaseURL() {
        return applicationBaseURL;
    }

    public abstract void populateSitemapEntryCache(SitemapCache cache);
}
