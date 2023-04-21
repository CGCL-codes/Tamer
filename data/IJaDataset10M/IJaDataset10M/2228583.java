package org.sgodden.query.service;

import org.hibernate.Session;
import org.sgodden.query.Query;
import org.sgodden.query.ResultSet;
import com.google.inject.Provider;

/**
 * A service which runs queries and returns results.
 * @author goddens
 *
 */
public interface QueryService {

    /**
	 * Executes the passed query, and returns a result set.
	 * @param query the query to execute.
	 * @return the results of the query.
	 */
    public ResultSet executeQuery(Query query);
}
