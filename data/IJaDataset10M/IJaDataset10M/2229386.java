package org.opcda2out.output.database;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * An updater used to set a value in a prepared statements
 *
 * @param <T> The class of the data value
 * @author Joao Leal
 */
public interface PstmtElementUpdater<T> {

    /**
     * Updates the prepared statement with the provided value
     * 
     * @param pstmt The prepared statement
     * @param pos The data position
     * @param value The new value
     * @throws SQLException
     */
    public void updatePsmtVal(PreparedStatement pstmt, int pos, T value) throws SQLException;

    /**
     * Provides the sql data type (as defined in {@link java.sql.Types Types})
     * 
     * @return The sql data type
     */
    public int getSQLType();
}
