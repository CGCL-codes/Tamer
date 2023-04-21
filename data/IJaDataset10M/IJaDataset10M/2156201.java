package org.neurox.esearch;

import org.eclipse.search.ui.ISearchQuery;

/**
 * This interface is intended to be implemented by clients!
 * @author dk
 */
public interface IESearchQuery extends ISearchQuery {

    /**
	 * Sets the text from the searchbox
	 * @param searchText
	 */
    public void setSearchText(String searchText);
}
