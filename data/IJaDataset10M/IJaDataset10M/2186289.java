package org.mbari.sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.ResourceBundle;

/**
 * <p>Convience class used to establish a connection to a database. This class
 * is not called directly by a developer, rather it is used to support
 * <code>ConnectionFactory</code>.</p>
 *
 * @author <a href="http://www.mbari.org">MBARI</a>
 * @version $Id: DBConnection.java 332 2006-08-01 18:38:46Z hohonuuli $
 */
class DBConnection {

    /**
	 * @uml.property  name="password"
	 */
    private String password;

    /**
	 * @uml.property  name="url"
	 */
    private String url;

    /**
	 * @uml.property  name="user"
	 */
    private String user;

    /**
     * Constructs ...
     *
     *
     * @param ref
     *
     * @throws DBException
     */
    DBConnection(String ref) throws DBException {
        ResourceBundle rb = ResourceBundle.getBundle(ref);
        url = rb.getString("db.url");
        user = rb.getString("db.user");
        password = rb.getString("db.password");
        String driver = rb.getString("jdbc.driver");
        int timeout = Integer.parseInt(rb.getString("db.timeout"));
        try {
            Class.forName(driver);
        } catch (Exception e) {
            DBException s = new DBException();
            s.initCause(e);
            throw s;
        }
        DriverManager.setLoginTimeout(timeout);
    }

    /**
     * <p><!-- Method description --></p>
     *
     *
     * @return
     *
     * @throws DBException
     */
    Connection getConnection() throws DBException {
        Connection con = null;
        try {
            con = DriverManager.getConnection(url, user, password);
        } catch (Exception e) {
            DBException s = new DBException();
            s.initCause(e);
            throw s;
        }
        return con;
    }
}
