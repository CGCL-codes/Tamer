package com.cs.util.db.connection;

import com.cs.util.db.DatabaseEngineEnum;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

/**
 * Connection factory creates connections to databases.
 * @author dimitris@jmike.gr
 */
public class ConnectionFactory {

    /**
     * Retrieves a connection from the connection pool.
     * @param name The name of the connection in the connection pool.
     * @return A pooled Connection.
     * @throws javax.naming.NamingException
     * @throws java.sql.SQLException
     */
    public static Connection getConnection(String name) throws NamingException, SQLException {
        Context initCtx;
        initCtx = new InitialContext();
        Context envCtx = (Context) initCtx.lookup("java:comp/env");
        DataSource ds = (DataSource) envCtx.lookup("jdbc/" + name);
        return ds.getConnection();
    }

    /**
     * Creates a connection to the specified database.
     * @param type The type of the database (i.e. MYSQL or MSSQL).
     * @param host The host of the database.
     * @param port The port of the database.
     * @param schema The schema of the database.
     * @param username The username for connecting to the database.
     * @param password The password for connecting to the database.
     * @return A new Connection to the specified database.
     * @throws java.lang.ClassNotFoundException Thrown when mysql JDBC driver is not correctly installed.
     * @throws java.sql.SQLException
     */
    public static Connection getConnection(DatabaseEngineEnum type, String host, int port, String schema, String username, String password) throws ClassNotFoundException, SQLException {
        switch(type) {
            case MYSQL:
                {
                    Class.forName("com.mysql.jdbc.Driver");
                    return DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + schema + "?" + "user=" + username + "&password=" + password);
                }
            case MSSQL:
                {
                    return null;
                }
            default:
                {
                    return null;
                }
        }
    }
}
