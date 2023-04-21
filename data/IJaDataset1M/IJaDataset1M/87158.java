package org.ofbiz.entity.jdbc;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import org.w3c.dom.Element;
import org.ofbiz.base.util.Debug;
import org.ofbiz.entity.GenericEntityException;
import org.ofbiz.entity.transaction.MinervaConnectionFactory;
import org.ofbiz.entity.transaction.TransactionFactory;

/**
 * ConnectionFactory - central source for JDBC connections
 *
 */
public class ConnectionFactory {

    public static final String module = ConnectionFactory.class.getName();

    public static Connection getConnection(String driverName, String connectionUrl, Properties props, String userName, String password) throws SQLException {
        if (driverName != null) {
            ConnectionFactory.loadDriver(driverName);
        }
        try {
            if (userName != null && userName.length() > 0) return DriverManager.getConnection(connectionUrl, userName, password); else if (props != null) return DriverManager.getConnection(connectionUrl, props); else return DriverManager.getConnection(connectionUrl);
        } catch (SQLException e) {
            Debug.logError(e, "SQL Error obtaining JDBC connection", module);
            throw e;
        }
    }

    public static Connection getConnection(String connectionUrl, String userName, String password) throws SQLException {
        return getConnection(null, connectionUrl, null, userName, password);
    }

    public static Connection getConnection(String connectionUrl, Properties props) throws SQLException {
        return getConnection(null, connectionUrl, props, null, null);
    }

    public static Connection getConnection(String helperName) throws SQLException, GenericEntityException {
        Connection con = TransactionFactory.getConnection(helperName);
        if (con == null) {
            Debug.logError("******* ERROR: No database connection found for helperName \"" + helperName + "\"", module);
        }
        return con;
    }

    public static Connection tryGenericConnectionSources(String helperName, Element inlineJdbcElement) throws SQLException, GenericEntityException {
        try {
            Connection con = MinervaConnectionFactory.getConnection(helperName, inlineJdbcElement);
            if (con != null) return con;
        } catch (Exception ex) {
            Debug.logError(ex, "There was an error getting a Minerva datasource.", module);
        }
        return null;
    }

    public static void loadDriver(String driverName) throws SQLException {
        if (DriverManager.getDriver(driverName) == null) {
            try {
                Driver driver = (Driver) Class.forName(driverName, true, Thread.currentThread().getContextClassLoader()).newInstance();
                DriverManager.registerDriver(driver);
            } catch (ClassNotFoundException e) {
                Debug.logWarning(e, "Unable to load driver [" + driverName + "]", module);
            } catch (InstantiationException e) {
                Debug.logWarning(e, "Unable to instantiate driver [" + driverName + "]", module);
            } catch (IllegalAccessException e) {
                Debug.logWarning(e, "Illegal access exception [" + driverName + "]", module);
            }
        }
    }

    public static void unloadDriver(String driverName) throws SQLException {
        Driver driver = DriverManager.getDriver(driverName);
        if (driver != null) {
            DriverManager.deregisterDriver(driver);
        }
    }
}
