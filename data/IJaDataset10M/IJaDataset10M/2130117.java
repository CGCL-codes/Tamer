package com.agimatec.sql.query;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public interface JdbcResultBuilder {

    /**
     * The init() method is called once after the SQL statement has been
     * executed and the result set is known.
     *
     * @param data       JDBC result set meta data.
     * @param resultList The List where the result objects are added to.
     */
    void afterExecute(java.sql.ResultSetMetaData data, Object queryObject, List resultList) throws SQLException;

    /**
     * This method is invoked each time the cursor in the resultset is moved
     * to the next row, as long as the resultList contains less objects than the client
     * expects.
     * <p/>
     * This method is expected to the following:
     * - use the values of the row to create some object
     * - might put the object to the resultList
     * <p/>
     * DO NOT: invoke next() or any method that moves the resultSet cursor or
     * closes the resultSet.
     *
     * @param row the ResultSet where the cursor is located on the current row
     */
    void fetch(ResultSet row) throws SQLException;

    /**
     * The close() method is invoked, just BEFORE all associated resources (e.g. resultSet)
     * are closed by the caller. The close() method is the last callback to the visitor
     * that allows to add pending results to the result list and to close own resources.
     */
    void close(boolean isComplete) throws SQLException;
}
