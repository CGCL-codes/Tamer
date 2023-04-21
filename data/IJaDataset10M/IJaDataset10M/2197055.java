package com.goodcodeisbeautiful.archtea.search.os;

import com.goodcodeisbeautiful.archtea.config.ArchteaPropertyFactory;
import com.goodcodeisbeautiful.archtea.config.DefaultOpenSearchConfig;
import com.goodcodeisbeautiful.archtea.config.OpenSearchConfig;
import com.goodcodeisbeautiful.archtea.config.SearchConfig;
import com.goodcodeisbeautiful.archtea.search.ArchteaQuery;
import com.goodcodeisbeautiful.archtea.search.ArchteaSearchException;
import com.goodcodeisbeautiful.archtea.search.ArchteaSearcher;
import com.goodcodeisbeautiful.archtea.search.SearchResult;

/**
 * @author hata
 *
 */
public abstract class AbstractOpenSearchArchteaSearcher implements ArchteaSearcher {

    /** a default encoding for searchTerms for urlencoding */
    public static final String DEFAULT_SEARCHTERMS_ENCODING = "UTF-8";

    /** a key to replace searchTerms */
    public static final String SEARCH_TERMS_KEY = "{searchTerms}";

    /** a key to replace startIndex */
    public static final String START_INDEX_KEY = "{startIndex}";

    /** a key to replace count */
    public static final String COUNT_KEY = "{count}";

    /** a key to replace startPage */
    public static final String START_PAGE_KEY = "{startPage}";

    /** search config. */
    private OpenSearchConfig m_searchConfig;

    /**
	 * 
	 */
    public AbstractOpenSearchArchteaSearcher() {
    }

    public abstract SearchResult search(ArchteaQuery query) throws ArchteaSearchException;

    /**
     * Get SearchConfig.
     * @return search config.
     * @see com.goodcodeisbeautiful.archtea.search.ArchteaSearcher#getConfig()
     */
    public SearchConfig getConfig() {
        return m_searchConfig;
    }

    /**
     * Set a SearchCofig.
     * @param config is a new config.
     * @see com.goodcodeisbeautiful.archtea.search.ArchteaSearcher#setConfig(com.goodcodeisbeautiful.archtea.config.SearchConfig)
     */
    public void setConfig(SearchConfig config) {
        if (config instanceof OpenSearchConfig) m_searchConfig = (OpenSearchConfig) config;
    }

    /**
     * Get SearchConfig as OpenSearchConfig.
     * @return OpenSearchConfig or null.
     */
    public OpenSearchConfig getOpenSearchConfig() {
        return m_searchConfig;
    }

    /**
     * Get default timeout.
     * @return a default timeout.
     */
    protected int getHttpClientTimeout() {
        return (m_searchConfig instanceof DefaultOpenSearchConfig) ? ((DefaultOpenSearchConfig) m_searchConfig).getHttpClientTimeout() : ArchteaPropertyFactory.DEFAULT_OPEN_SEARCH_TIMEOUT;
    }
}
