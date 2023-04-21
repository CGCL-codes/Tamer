package com.esri.gpt.framework.request;

/**
 * Represents the criteria associated with a query.
 */
public class QueryCriteria extends Criteria {

    private SortOption _sortOption = new SortOption();

    /** Default constructor. */
    public QueryCriteria() {
    }

    /**
 * Gets the sort option.
 * @return the sort option
 */
    public SortOption getSortOption() {
        return _sortOption;
    }

    /**
 * Sets the sort option.
 * @param option the sort option
 */
    public void setSortOption(SortOption option) {
        _sortOption = option;
        if (_sortOption == null) {
            _sortOption = new SortOption();
        }
    }

    /**
 * Resets the criteria.
 */
    public void reset() {
    }
}
