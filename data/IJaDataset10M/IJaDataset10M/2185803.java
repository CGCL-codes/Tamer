package org.jmove.core.search;

import java.util.Set;

/**
 * todo: removeEntry(), clear() etc.
 *
 * @author Axel Terfloth
 */
public interface SearchIndex {

    /** Adds an entry to the search index. */
    void add(SearchIndexEntry entry);

    /**
	 * This method searches the index entries by an expression passed as an argument.<br>
	 * The concrete type of search and the interpretation of the search expression is specific
	 * to the concrete implementation. The result set may be sorted or not depending on the
	 * implementation.
	 *
	 * @param searchExpression
	 * @return a set of search index entries
	 */
    Set searchEntries(String searchExpression);
}
