package org.jecars;

import javax.jcr.RepositoryException;
import javax.jcr.query.Query;
import javax.jcr.query.QueryResult;

/**
 * CARS_QueryManager
 *
 * @version $Id: CARS_QueryManager.java,v 1.1 2007/09/26 14:20:16 weertj Exp $
 */
public class CARS_QueryManager {

    private static Object mRunningQuery = new Object();

    /** Execute query one at the time (performance issue)
   */
    static QueryResult executeQuery(Query pQuery) throws RepositoryException {
        QueryResult qr = null;
        System.out.println("Want to execute query: " + pQuery.getStatement());
        synchronized (mRunningQuery) {
            long time = System.currentTimeMillis();
            System.out.println("Executing query: " + pQuery.getStatement());
            qr = pQuery.execute();
            System.out.println("Query ready in " + ((System.currentTimeMillis() - time) / 1000) + " seconds");
        }
        return qr;
    }
}
