package org.squin.lookup.urisearch.impl;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.squin.common.Priority;
import org.squin.common.Task;
import org.squin.common.impl.TaskBase;
import org.squin.lookup.urisearch.URISearchResult;

/**
 * Represents a task to search for RDF documents that mention a given URI.
 *
 * @author Olaf Hartig (hartig@informatik.hu-berlin.de)
 */
public class URISearchTask extends TaskBase<URISearchResult> implements Task<URISearchResult> {

    private final Logger log = LoggerFactory.getLogger(URISearchTask.class);

    /**
	 * Identifier of the URI that has to be searched.
	 */
    public final int uriID;

    protected final QueryProcessor processor;

    /**
	 * Constructs a URI search task.
	 *
	 * @param uriID identifier of the URI that has to be searched
	 * @param priority priority of this task
	 *                 (mandatory, i.e. must not be null)
	 */
    public URISearchTask(int uriID, Priority priority, QueryProcessor processor) {
        super(priority);
        assert processor != null;
        this.uriID = uriID;
        this.processor = processor;
    }

    public URISearchResult createFailureResult(Exception e) {
        return new Failure(uriID, getTimestamp(), getExecutionStartTimestamp(), e);
    }

    public URISearchResult call() {
        log.debug("Started to search for the URI with ID {}.", uriID);
        List<Integer> discoveredDocumentURIs;
        try {
            discoveredDocumentURIs = processor.process(uriID);
        } catch (QueryProcessingException e) {
            log.debug("Searching for the URI with ID {} failed: {}", uriID, e.getMessage());
            return new Failure(uriID, getTimestamp(), getExecutionStartTimestamp(), e);
        }
        log.debug("Finished to search for the URI with ID {} - {} document URIs found.", uriID, discoveredDocumentURIs.size());
        return new URIsDiscovered(uriID, getTimestamp(), getExecutionStartTimestamp(), discoveredDocumentURIs);
    }

    abstract static class URISearchResultBase implements URISearchResult {

        public final int uriID;

        public final long queueTime;

        public final long execTime;

        public URISearchResultBase(int uriID, long taskInitTimestamp, long taskStartTimestamp) {
            this.uriID = uriID;
            queueTime = taskStartTimestamp - taskInitTimestamp;
            execTime = System.currentTimeMillis() - taskStartTimestamp;
        }

        public int getURIID() {
            return uriID;
        }

        public List<Integer> getDiscoveredDocumentURIs() {
            throw new UnsupportedOperationException();
        }

        public Exception getException() {
            throw new UnsupportedOperationException();
        }

        public long getQueueTime() {
            return queueTime;
        }

        public long getExecutionTime() {
            return execTime;
        }
    }

    protected static class URIsDiscovered extends URISearchResultBase {

        public final List<Integer> discoveredDocumentURIs;

        public URIsDiscovered(int uriID, long taskInitTimestamp, long taskStartTimestamp, List<Integer> discoveredDocumentURIs) {
            super(uriID, taskInitTimestamp, taskStartTimestamp);
            this.discoveredDocumentURIs = discoveredDocumentURIs;
        }

        public boolean isFailure() {
            return false;
        }

        @Override
        public List<Integer> getDiscoveredDocumentURIs() {
            return discoveredDocumentURIs;
        }
    }

    protected static class Failure extends URISearchResultBase {

        public final Exception exception;

        public Failure(int uriID, long taskInitTimestamp, long taskStartTimestamp, Exception exception) {
            super(uriID, taskInitTimestamp, taskStartTimestamp);
            this.exception = exception;
        }

        public boolean isFailure() {
            return true;
        }

        @Override
        public Exception getException() {
            return exception;
        }
    }
}
