package com.hp.hpl.jena.rdql;

public interface QueryResultsRewindable extends QueryResults {

    /** Move back to the start of the iterator for this instance of results of a query.
     *
     */
    public void rewind();
}
