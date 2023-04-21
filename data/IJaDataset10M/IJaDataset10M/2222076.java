package com.completex.objective.components.persistency.core;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author Gennady Krizhevsky
 */
public interface ResultSetIterator {

    /**
     * Moves cursor to the next ResultSet
     * 
     * @return true if there is one
     * @throws SQLException
     */
    boolean next() throws SQLException;

    /**
     * Get current ResultSet
     *  
     * @return ResultSet
     * @throws SQLException
     */
    ResultSet getResultSet() throws SQLException;

    /**
     * 
     * @return true if there is current ResultSet
     */
    boolean hasResultSet();

    /**
     * 
     * @return true if the cursor is after the last ResultSet
     */
    boolean isAfterLast();

    /**
     * Get current ResultSet index. If cursor is before 1st, the index is -1. 
     * There is no upper limit on current ResultSet index. Use isAfterLast() method 
     * to see if the index makes sense.
     * 
     * @see ResultSetIterator#isAfterLast()
     * @return current ResultSet index. If cursor is before 1st, the index is -1. 
     */
    int getResultSetIndex();
}
