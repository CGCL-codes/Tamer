package org.t2framework.daisy.core.wrapper;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Logical connection.
 * 
 * @author shot
 * 
 */
public interface LogicalConnection extends Connection {

    /**
	 * Get underlying physical connection.
	 * 
	 * @return
	 * @throws SQLException
	 */
    Connection getPhysicalConnection() throws SQLException;
}
