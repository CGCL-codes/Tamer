package org.pittjug.sql.pool;

import java.io.PrintStream;
import java.sql.Connection;
import java.sql.SQLException;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.Properties;
import javax.sql.DataSource;

/**
 *    The Pool class that provides access to a JNDI DataSource pool . Additional Required Parameter:<br>
 * org.pittjug.sql.pool.JNDIPool.JNDIDataName the name of the DataSource in JNDI<br> <br>
 * Other parameters if required by the JNDI DataSource:<br> org.pittjug.sql.pool.JNDIPool.dbLogin Database user name<br>
 * org.pittjug.sql.pool.JNDIPool.dbPassword the users password<br>
 * <p>Copyright: Copyright (c) 2002</p> <p>Company: PittJUG</p>
 * @author Carl Trusiak
 * @version 1.0
 */
public class JNDIPool implements Pool {

    private static String dbDriver;

    private static String user;

    private static String password;

    public void freeConnection(Connection conn) throws SQLException {
        conn.close();
    }

    public Connection getConnection() throws SQLException {
        try {
            InitialContext ctx = new InitialContext();
            DataSource ds = (DataSource) ctx.lookup(dbDriver);
            if ((user == null) || (password == null)) {
                return ds.getConnection();
            } else {
                return ds.getConnection(user, password);
            }
        } catch (NamingException ne) {
            throw new SQLException("Unable to locate DataSource : " + dbDriver + "\n" + ne.getMessage());
        }
    }

    public void init(Properties props) throws PoolException {
        try {
            dbDriver = props.getProperty("org.pittjug.sql.pool.JNDIPool.JNDIDataName");
            user = props.getProperty("org.pittjug.sql.pool.JNDIPool.dbLogin");
            password = props.getProperty("org.pittjug.sql.pool.JNDIPool.dbPassword");
        } catch (Exception e) {
            System.out.println(e);
            throw new PoolException("Exception occured creating JNDIPool", e);
        }
    }

    public void destroy() throws PoolException {
    }
}
