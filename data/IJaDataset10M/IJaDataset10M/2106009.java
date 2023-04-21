package ch.netcetera.wikisearch.swing;

import java.util.logging.Level;
import java.util.logging.Logger;
import ch.netcetera.wikisearch.search.ISearch;
import ch.netcetera.wikisearch.search.ISearchResultListener;
import ch.netcetera.wikisearch.search.SearchException;
import ch.netcetera.wikisearch.search.SearchResultModel;
import ch.netcetera.wikisearch.util.SwingWorker;

/**
 * SearchWorker implements a background-search thread.
 * This is a bit of multi-threading fun... 
 * Searching is typically slow and to keep the GUI responsive, 
 * we put the search operation to a background thread.
 * see ch.netcetera.niki.util.SwingWorker. 
 * Works really fine :-))
 */
public class SearchWorker extends SwingWorker {

    private SearchResultModel results = null;

    private Logger logger = Logger.getLogger("ch.netcetera.wikisearch.swing");

    private String searchTerm = null;

    private ISearch searcher = null;

    private ISearchResultListener resultListener = null;

    public SearchWorker(ISearch aSearcher, String aSearchTerm, ISearchResultListener aResultListener) {
        super();
        this.searcher = aSearcher;
        this.searchTerm = aSearchTerm;
        this.resultListener = aResultListener;
    }

    /**
	 * The expensive operation is to be implemented in here.
	 */
    public Object construct() {
        try {
            results = searcher.search(searchTerm);
        } catch (SearchException sex) {
            logger.log(Level.WARNING, "Searching caused an Error.", sex);
            sex.printStackTrace(System.err);
        }
        return results;
    }

    /**
	 * This runs on the event-dispatching thread, therefore we 
	 * put the GUI operations in here.
	 */
    public void finished() {
        if (results != null) {
            if (results.getSearchString().equals(searchTerm)) {
                resultListener.updateResults(results);
                logger.fine("SearchComponent.SwingWorker.finished: last search finished searchString=" + results.getSearchString());
            } else {
                logger.fine("SearchComponent.SwingWorker.finished: earlier search finished searchString=" + results.getSearchString());
            }
        }
    }
}
