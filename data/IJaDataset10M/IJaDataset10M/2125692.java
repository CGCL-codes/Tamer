package org.avaje.ebean.server.core;

public interface RelationalQueryEngine {

    /**
	 * Find a list of beans using relational query.
	 */
    public abstract Object findMany(RelationalQueryRequest request);
}
