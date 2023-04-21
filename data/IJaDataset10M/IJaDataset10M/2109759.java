package com.thyante.thelibrarian.util;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import com.thyante.thelibrarian.info.IItemDatabaseProxyConfiguration;
import com.thyante.thelibrarian.model.ICollection;
import com.thyante.thelibrarian.model.IConfiguration;

/**
 * @author Matthias-M. Christen
 *
 */
public class SearchResultCache {

    private static SearchResultCache THIS = new SearchResultCache();

    /**
	 * Returns an instance of the search result cache.
	 * @return The search result cache
	 */
    public static SearchResultCache getInstance() {
        return THIS;
    }

    public static class CachedResult {

        protected ICollection m_collection;

        protected List<Object> m_listResults;

        protected String m_strSearchString;

        protected IItemDatabaseProxyConfiguration m_configuration;

        /**
		 * Constructs the cache object.
		 * @param listResults The search result list
		 * @param strSearchString The search string that was used to produce the search result
		 * @param configuration The search engine configuration
		 */
        protected CachedResult(ICollection collection, List<Object> listResults, String strSearchString, IItemDatabaseProxyConfiguration configuration) {
            m_collection = collection;
            m_listResults = listResults;
            m_strSearchString = strSearchString;
            m_configuration = getInstance().getCachedConfiguration(configuration);
        }

        /**
		 * Returns the search string that was used to produce the results cached in this object.
		 * @return The search string
		 */
        public String getSearchString() {
            return m_strSearchString;
        }

        /**
		 * Returns the database proxy configuration with which the search has been performed.
		 * @return The database proxy configuration
		 */
        public IItemDatabaseProxyConfiguration getConfiguration() {
            return m_configuration;
        }

        /**
		 * Returns the collection on which the search has been performed.
		 * @return The collection on which the search has been performed
		 */
        public ICollection getCollection() {
            return m_collection;
        }

        /**
		 * Returns the cached search results.
		 * @return
		 */
        public List<Object> getResults() {
            return m_listResults;
        }

        /**
		 * Returns the number of items in the search result
		 * @return The number of search result items
		 */
        public int getResultsCount() {
            return m_listResults.size();
        }

        public boolean equals(String strSearchString, IConfiguration configuration) {
            return strSearchString.equals(m_strSearchString) && configuration.equals(m_configuration);
        }
    }

    /**
	 * Configurations cache
	 */
    protected Map<IItemDatabaseProxyConfiguration, IItemDatabaseProxyConfiguration> m_mapConfigurationCache;

    /**
	 * The list of cached search results
	 */
    protected List<CachedResult> m_listResults;

    /**
	 * Constructs the search result cache singleton.<br/>
	 * (Not instantiable from foreign objects)
	 */
    private SearchResultCache() {
        m_mapConfigurationCache = new HashMap<IItemDatabaseProxyConfiguration, IItemDatabaseProxyConfiguration>();
        m_listResults = new LinkedList<CachedResult>();
    }

    /**
	 * Returns an immutable configuration object from the cache (or adds a copy of
	 * <code>configuration</code> to the cache if it doesn't exist yet) that equals
	 * the configuration <code>configuration</code>.
	 * @param configuration The configuration to look for
	 * @return An immutable object equaling the <code>configuration</code>
	 */
    private IItemDatabaseProxyConfiguration getCachedConfiguration(IItemDatabaseProxyConfiguration configuration) {
        IItemDatabaseProxyConfiguration configImmutable = m_mapConfigurationCache.get(configuration);
        if (configImmutable == null) {
            configImmutable = (IItemDatabaseProxyConfiguration) configuration.clone();
            m_mapConfigurationCache.put(configImmutable, configImmutable);
        }
        return configImmutable;
    }

    /**
	 * Caches the search result list <code>listResults</code>.
	 * @param <T> The result object class parameter
	 * @param collection The collection on which the search has been performed
	 * @param listResults The list of search results to cache
	 * @param strSearchString The search string that was used to produce the search results
	 * @param configuration The search engine configuration
	 */
    @SuppressWarnings("unchecked")
    public <T> void cache(ICollection collection, List<T> listResults, String strSearchString, IItemDatabaseProxyConfiguration configuration) {
        m_listResults.add(new CachedResult(collection, (List<Object>) listResults, strSearchString, configuration));
    }

    public List<Object> getCachedResults(String strSearchString, IConfiguration configuration) {
        for (CachedResult cr : m_listResults) if (cr.equals(strSearchString, configuration)) return cr.getResults();
        return null;
    }

    public Iterable<CachedResult> getCachedResults() {
        return m_listResults;
    }
}
