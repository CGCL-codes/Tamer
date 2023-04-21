package org.gjt.mm.mysql;

import java.sql.SQLException;

/**
 * Here for backwards compatibility with MM.MySQL
 * 
 * @author Mark Matthews
 */
public class Driver extends com.mysql.jdbc.Driver {

    /**
	 * Creates a new instance of Driver
	 * 
	 * @throws SQLException
	 *             if a database error occurs.
	 */
    public Driver() throws SQLException {
        super();
    }
}
