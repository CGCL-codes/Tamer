package org.light.portal.search.impl;

import static org.light.portal.util.Constants._SEARCH_HOST;
import org.apache.lucene.document.Document;
import org.apache.lucene.search.Query;
import org.light.portal.logger.Logger;
import org.light.portal.logger.LoggerFactory;
import org.light.portal.search.Searcher;
import org.light.portal.search.model.SearchCriteria;
import org.light.portal.search.model.SearchResult;
import org.light.portal.search.model.SearchResultItem;

/**
 * 
 * @author Jianmin Liu
 **/
public class SearcherServiceImpl implements Searcher {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private Searcher localSearcher;

    private Searcher remoteSearcher;

    private Searcher searcher;

    private Searcher getSearcher() {
        if (searcher == null) {
            if (_SEARCH_HOST) {
                searcher = localSearcher;
            } else {
                searcher = remoteSearcher;
            }
        }
        return searcher;
    }

    public SearchResult search(SearchCriteria criteria) throws Exception {
        return getSearcher().search(criteria);
    }

    public SearchResult search(Class klass, SearchCriteria criteria) throws Exception {
        return getSearcher().search(klass, criteria);
    }

    public Query getQurey(SearchCriteria criteria) throws Exception {
        return getSearcher().getQurey(criteria);
    }

    public SearchResultItem getResultItem(Query query, Document doc) throws Exception {
        return getSearcher().getResultItem(query, doc);
    }

    public Searcher getLocalSearcher() {
        return localSearcher;
    }

    public void setLocalSearcher(Searcher localSearcher) {
        this.localSearcher = localSearcher;
    }

    public Searcher getRemoteSearcher() {
        return remoteSearcher;
    }

    public void setRemoteSearcher(Searcher remoteSearcher) {
        this.remoteSearcher = remoteSearcher;
    }
}
