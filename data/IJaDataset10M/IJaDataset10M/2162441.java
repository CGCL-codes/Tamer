package org.jage.query;

/**
 * Interface to all queryable classes, i.e. classes on which a query can be
 * performed.
 * 
 * @author KrzS
 */
public interface IQueryable {

    /**
	 * Queries the object.
	 * 
	 * @param query
	 *            the query
	 * @return a result of the query
	 */
    public IQueryResult query(Query query);
}
