package com.ibm.celldt.utils.searcher;

/**
 * @author laggarcia
 * @since 3.0.0
 */
public class NoEngineSearcher implements Searcher {

    /**
	 * 
	 */
    public NoEngineSearcher() {
    }

    public void search() throws SearchFailedException {
        throw new SearchFailedException(SearcherMessages.noSearchEngine);
    }
}
