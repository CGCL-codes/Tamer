package de.guhsoft.jinto.core.search;

import org.eclipse.search.ui.ISearchResult;

/**
 * @author mseele
 * 
 * toDo To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Generation - Code and Comments
 */
public interface IJIntoSearchRequestor extends ISearchResult {

    public void beginSearch();

    public void endSearch();

    public void accepSearchMatch(Object match);

    public void setQuery(JIntoSearchQuery jintoSearchQuery);
}
