package org.vosao.search;

import java.util.List;

/**
 * Search index of all site pages for one language.
 * 
 * @author Alexander Oleynik
 *
 */
public interface SearchIndex {

    void updateIndex(Long pageId);

    void removeFromIndex(Long pageId);

    List<Hit> search(SearchResultFilter filter, String query, int textSize);

    void saveIndex();

    String getLanguage();

    void clear();
}
