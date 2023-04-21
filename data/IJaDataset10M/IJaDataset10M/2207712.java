package org.squin.lookup.urisearch;

import java.util.List;

/**
 * This interface represents the result of a finished search for a URI.
 *
 * @author Olaf Hartig (hartig@informatik.hu-berlin.de)
 */
public interface URISearchResult {

    /**
	 * Returns the identifier of the searched URI.
	 */
    int getURIID();

    /**
	 * Returns a list of URIs for documents that were constituting the search
	 * result. The order of URIs in the list should reflect the ranking of the
	 * documents in the search result.
	 */
    List<Integer> getDiscoveredDocumentURIs();

    /**
	 * Return true if the URI search failed.
	 */
    boolean isFailure();

    /**
	 * Returns the cause of the failure (if the search failed).
	 *
	 * @throws UnsupportedOperationException when {@link #isFailure} is false
	 */
    Exception getException() throws UnsupportedOperationException;

    /**
	 * Returns the time (in ms) the corresponding search task has spent in
	 * the queue. That is the interval between issuing the task and starting
	 * to actual execute it.
	 */
    long getQueueTime();

    /**
	 * Returns the time (in ms) required to execute the corresponding
	 * search task.
	 */
    long getExecutionTime();
}
