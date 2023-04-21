package org.mandarax.sql;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import org.mandarax.util.logging.LogCategories;
import org.mandarax.kernel.Clause;
import org.mandarax.kernel.ClauseSetException;
import org.mandarax.kernel.Fact;
import org.mandarax.kernel.LogicFactory;
import org.mandarax.kernel.Term;

/**
 * Clause set iterator for SQL clause sets.
 * @author <A href="http://www-ist.massey.ac.nz/JBDietrich" target="_top">Jens Dietrich</A>
 * @version 3.4 <7 March 05>
 * @since 1.5
 */
class SQLClauseIterator extends org.mandarax.util.AbstractClauseIterator implements LogCategories {

    private SQLPredicate predicate;

    private java.sql.ResultSet rs = null;

    private Fact nextFact = null;

    private LogicFactory logFact = LogicFactory.getDefaultFactory();

    private Class[] struct = null;

    private SQLTypeMapping typeMapping = null;

    private Map map = null;

    private List cache = null;

    private boolean closeConnection = false;

    private Connection connection = null;

    /**
	 * Constructor.
	 * @param p the predicate
	 * @param queryResult the result set
	 * @param c the cache list to collect facts - if null, nothing will be cached
	 * @param closeConnection whether to close the connection at the end
	 * @param con the connection used
	 */
    SQLClauseIterator(SQLPredicate p, java.sql.ResultSet queryResult, List c, boolean closeConnection, Connection con) {
        super();
        predicate = p;
        rs = queryResult;
        struct = predicate.getStructure();
        typeMapping = predicate.getTypeMapping();
        map = (typeMapping == null) ? null : typeMapping.getTypeMapping();
        cache = c;
        this.closeConnection = closeConnection;
        this.connection = con;
    }

    /**
	 * Indicates whether there are more clauses.
	 * @return true if there are more clauses, false otherwise
	 * @throws ClauseSetException
	 */
    public boolean hasMoreClauses() throws ClauseSetException {
        try {
            boolean hasNext = rs.next();
            if (hasNext) {
                Term[] terms = new Term[struct.length];
                for (int i = 0; i < struct.length; i++) {
                    if (typeMapping == null) {
                        terms[i] = logFact.createConstantTerm(rs.getObject(i + 1));
                    } else {
                        terms[i] = logFact.createConstantTerm(typeMapping.postMap((map == null) ? rs.getObject(i + 1) : rs.getObject(i + 1, map)));
                    }
                }
                nextFact = logFact.createFact(predicate, terms);
                if (cache != null) {
                    cache.add(nextFact);
                }
                return true;
            } else {
                nextFact = null;
                return false;
            }
        } catch (SQLException x) {
            nextFact = null;
            throw new ClauseSetException("Exception building facts from records", x);
        }
    }

    /**
	 * Get the next clause.
	 * @return the next clause
	 * @throws ClauseSetException
	 */
    public Clause nextClause() throws ClauseSetException {
        return nextFact;
    }

    /**
     * Close the iterator and release all resources.
     */
    public void close() {
        try {
            if (rs != null && closeConnection) {
                rs.close();
                if (LOG_SQL.isDebugEnabled()) LOG_SQL.debug("JDBC connection released by SQLClauseIterator " + this);
            }
        } catch (SQLException x) {
            LOG_SQL.error("Exception closing result set in SQL clause iterator", x);
        }
        try {
            if (connection != null && closeConnection) connection.close();
        } catch (SQLException x) {
            LOG_SQL.error("Exception closing connection in SQL clause iterator", x);
        }
    }
}
