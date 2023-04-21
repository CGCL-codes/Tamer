package com.goodcodeisbeautiful.archtea.search;

import java.util.List;

public class FilteredSearchResult implements SearchResult {

    /** base result */
    protected SearchResult m_result;

    /**
	 * Constructor.
	 * @param result is a base result.
	 */
    public FilteredSearchResult(SearchResult result) {
        m_result = result;
    }

    /**
	 * Get searchTimeSeconds.
	 * @return searchTimeSeconds.
	 */
    public double getSearchTimeSeconds() {
        return m_result.getSearchTimeSeconds();
    }

    /**
	 * Get searchTimeMills.
	 * @return searchTImeMills.
	 */
    public long getSearchTimeMills() {
        return m_result.getSearchTimeMills();
    }

    /**
	 * Get hit count.
	 * @return hit count.
	 */
    public long getHitCount() {
        return m_result.getHitCount();
    }

    /**
	 * Get a name.
	 * @return a name.
	 */
    public String getName() {
        return m_result.getName();
    }

    /**
	 * Get FoundItem list.
	 * @return FoundItem list.
	 */
    public List getFoundItems() {
        return m_result.getFoundItems();
    }

    /**
	 * Get items per page.
	 * @return items per page.
	 */
    public long getItemsPerPage() {
        return m_result.getItemsPerPage();
    }
}
