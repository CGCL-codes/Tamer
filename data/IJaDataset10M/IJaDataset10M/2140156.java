package org.tripcom.integration.entry;

import java.net.URI;
import java.util.Set;

/**
 * Sent from the TSAdapter to the Distribution Manager. (The DM needs the results so that it can
 * merge it with the results from other clients.)
 *
 * @author Michael Lafite
 */
public class RdResultDMEntry extends RdBase {

    /**
     * serial number for serialization purposes
     */
    private static final long serialVersionUID = 312228L;

    public Set<TripleEntry> data;

    public Set<URI> tripleset;

    public ReadType kind;

    /**
     * Indicates whether the query has been completed by the local RDF store.
     */
    public Boolean queryCompleted;

    /**
     * Default constructor.
     */
    public RdResultDMEntry() {
    }

    /**
    * Creates a new {@code RdResultDMEntry} instance.
    *
    * @param data
    * @param space
    * @param triples
    * @param operationId
    * @param transactionId
    * @param queryCompleted
    */
    public RdResultDMEntry(Set<TripleEntry> data, SpaceURI space, Set<URI> triples, Long operationId, URI transactionId, Boolean queryCompleted) {
        super(space, transactionId, operationId, null, null);
        this.data = data;
    }

    public void setData(Set<TripleEntry> set) {
        this.data = set;
    }

    public void setTripleset(Set<URI> triples) {
        this.tripleset = triples;
    }

    public void setTimestamp(long timestamp) {
        if (this.timeout == null) {
            this.timeout = new Timeout(timestamp);
        } else {
            this.timeout.setTimestamp(timestamp);
        }
    }

    public Set<TripleEntry> getData() {
        return this.data;
    }

    public Set<URI> getTripleset() {
        return this.tripleset;
    }

    public long getTimestamp() {
        if (this.timeout != null) {
            return this.timeout.getTimestamp();
        } else {
            return 0L;
        }
    }
}
