package org.objectstyle.cayenne.remote;

import org.objectstyle.cayenne.query.Query;

/**
 * A message passed to a DataChannel to request a query execution with result returned as
 * QueryResponse.
 * 
 * @since 1.2
 * @author Andrus Adamchik
 */
public class QueryMessage implements ClientMessage {

    protected Query query;

    private QueryMessage() {
    }

    public QueryMessage(Query query) {
        this.query = query;
    }

    public Query getQuery() {
        return query;
    }

    public String toString() {
        return "Query";
    }
}
