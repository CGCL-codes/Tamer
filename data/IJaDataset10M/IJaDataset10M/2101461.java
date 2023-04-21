package net.pmonks.DAL.generator.schema;

import java.util.*;
import java.security.*;
import java.sql.*;
import javax.sql.*;
import org.apache.log4j.*;
import org.apache.log4j.xml.*;

public class DBConnectionManager {

    /**
     * Log4J category used for logging by the class.
     */
    static Category cat = Category.getInstance(DBConnectionManager.class.getName());

    protected Map connectionPool = null;

    protected String userName = null;

    protected String password = null;

    protected String JDBCDriver = null;

    protected String JDBCURL = null;

    public DBConnectionManager(String userName, String password, String JDBCDriver, String JDBCURL) throws SQLException, ClassNotFoundException, InvalidParameterException {
        if (JDBCDriver == null || JDBCURL == null || JDBCDriver.length() <= 0 || JDBCURL.length() <= 0) {
            String msg = "DBConnectionManager constructor was passed a null or empty parameter.";
            cat.error(msg);
            throw new InvalidParameterException(msg);
        }
        connectionPool = new HashMap();
        cat.debug("Loading JDBC driver '" + JDBCDriver + "'...");
        Class.forName(JDBCDriver);
        this.userName = userName;
        this.password = password;
        this.JDBCDriver = JDBCDriver;
        this.JDBCURL = JDBCURL;
    }

    public Connection getConnection(String name) throws SQLException {
        Connection result = null;
        if (connectionPool.containsKey(name)) {
            result = (Connection) connectionPool.get(name);
        } else {
            if (userName != null && password != null) {
                cat.debug("Connecting to database (using connection name '" + name + "'), URL='" + JDBCURL + "', username='" + userName + "', password='" + password + "'...");
                result = DriverManager.getConnection(JDBCURL, userName, password);
            } else {
                cat.debug("Connecting to database  (using connection name '" + name + "'), URL='" + JDBCURL + "'...");
                result = DriverManager.getConnection(JDBCURL);
            }
            connectionPool.put(name, result);
        }
        return (result);
    }

    public void cleanUp() throws SQLException {
        Iterator it = connectionPool.keySet().iterator();
        while (it.hasNext()) {
            String connectionName = (String) it.next();
            Connection con = (Connection) connectionPool.get(connectionName);
            if (con != null) {
                if (!con.isClosed()) {
                    cat.debug("  Closing DB connection '" + connectionName + "'...");
                    con.close();
                }
                con = null;
            }
        }
    }

    public void finalize() throws SQLException {
        cleanUp();
    }
}
