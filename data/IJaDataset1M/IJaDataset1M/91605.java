package org.eclipse.help.ui.internal.search;

import java.util.Dictionary;
import org.eclipse.help.internal.search.WebSearch;
import org.eclipse.help.search.*;
import org.eclipse.help.ui.ISearchScopeFactory;
import org.eclipse.jface.preference.IPreferenceStore;

/**
 * Factory for creating scope objects for the generic web search engine
 */
public class WebSearchScopeFactory implements ISearchScopeFactory {

    public static final String P_URL = "url";

    public ISearchScope createSearchScope(IPreferenceStore store, String engineId, Dictionary parameters) {
        String urlTemplate = getProperty(store, engineId, parameters);
        return new WebSearch.Scope(urlTemplate);
    }

    private String getProperty(IPreferenceStore store, String engineId, Dictionary parameters) {
        String value = store.getString(engineId + "." + P_URL);
        if (value != null && value.length() > 0) return value;
        return (String) parameters.get(P_URL);
    }
}
