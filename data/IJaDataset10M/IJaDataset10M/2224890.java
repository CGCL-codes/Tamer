package com.lettuce.client;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Savepoint;

/**
 * Action Listener of the XConnection's underlayer connection.
 * 
 * @author Zhigang Xie
 * @version 1.0, 03/26/2010
 * @since JDK 1.5
 */
public interface XConnectionActionListener {

    /**
	 * closing the connection
	 * 
	 * @param conn - the underlayer connection
	 * @throws SQLException
	 */
    void close(Connection conn) throws SQLException;

    /**
	 * committing the connection
	 * 
	 * @param conn - the underlayer connection
	 * 
	 * @throws SQLException
	 */
    void commit(Connection conn) throws SQLException;

    /**
	 * rolling back the connection
	 * @param conn - the underlayer connection
	 * @throws SQLException
	 */
    void rollback(Connection conn) throws SQLException;

    /**
	 * rolling back the connection
	 * @param conn - the underlayer connection
	 * @param savepoint
	 * @throws SQLException
	 */
    void rollback(Connection conn, Savepoint savepoint) throws SQLException;
}
