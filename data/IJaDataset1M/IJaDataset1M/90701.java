package cn.ac.ntarl.umt.database.pool;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;
import java.util.Properties;
import org.apache.log4j.Logger;
import cn.ac.ntarl.umt.database.DatabaseError;
import cn.ac.ntarl.umt.database.GeneralDataBaseException;
import cn.ac.ntarl.umt.database.NotInitialized;
import org.apache.commons.dbcp.BasicDataSource;
import org.apache.commons.dbcp.BasicDataSourceFactory;

public final class ConnectionPool {

    private BasicDataSource ds = null;

    private ConnectionPool(String username, String password) throws GeneralDataBaseException {
        if (jdbcDriver == null) {
            log.fatal("Could not find jdbc.driver property. Check configuration");
            throw new GeneralDataBaseException("Could not find jdbc.driver property. Check configuration");
        }
        try {
            Class.forName(jdbcDriver);
            log.info("Loaded JDBC driver " + jdbcDriver);
        } catch (ClassNotFoundException e) {
            log.fatal("Could not find JDBC driver jar: " + e);
            throw new GeneralDataBaseException("Could not find JDBC driver jar: " + e, e);
        }
        if (username == null) throw new GeneralDataBaseException("username is null, please set a database username");
        if (password == null) throw new GeneralDataBaseException("password is null, please set a database password");
        this.username = username;
        this.password = password;
        Properties p = new Properties();
        p.setProperty("driverClassName", jdbcDriver);
        p.setProperty("url", jdbcURL);
        p.setProperty("username", username);
        p.setProperty("password", password);
        if (MAX_CONN > 0) {
            p.setProperty("maxActive", String.valueOf(MAX_CONN));
        }
        if (MAX_FREE > 0) {
            p.setProperty("maxIdle", String.valueOf(MAX_FREE));
        }
        p.setProperty("minIdle", "2");
        p.setProperty("testOnBorrow", "true");
        p.setProperty("testOnReturn", "true");
        p.setProperty("testWhileIdle", "true");
        p.setProperty("logAbandoned", "true");
        p.setProperty("validationQuery", "select 1 ");
        try {
            ds = (BasicDataSource) BasicDataSourceFactory.createDataSource(p);
        } catch (Exception e) {
            throw new GeneralDataBaseException(e);
        }
    }

    public static void init(String username, String password, String ajdbcDriver, String ajdbcURL, int maxConn, int maxFree) throws GeneralDataBaseException {
        jdbcDriver = ajdbcDriver;
        jdbcURL = ajdbcURL;
        MAX_CONN = maxConn;
        MAX_FREE = maxFree;
        pool = new ConnectionPool(username, password);
    }

    public static ConnectionPool getInstance() throws NotInitialized {
        if (pool == null) throw new NotInitialized();
        return pool;
    }

    private Connection connect() throws DatabaseError {
        Connection q;
        try {
            q = ds.getConnection();
            return q;
        } catch (SQLException e) {
            throw new DatabaseError(e, null);
        }
    }

    private int numConns() {
        System.out.print("Connection pool:");
        System.out.print("Active:" + ds.getNumActive() + ";");
        System.out.print("Idel:" + ds.getNumIdle() + ";");
        System.out.println("TestsPerEvictionRun:" + ds.getNumTestsPerEvictionRun() + ";");
        return ds.getNumActive() + ds.getNumIdle();
    }

    public synchronized Connection get(boolean dontwait) throws DatabaseError {
        Connection conn = null;
        conn = connect();
        return conn;
    }

    public void release(Connection c) {
        try {
            if (c != null && !c.isClosed()) {
                c.close();
            }
        } catch (SQLException e) {
        }
    }

    public void forget(Connection c) {
        try {
            if (c != null) {
                if (!c.isClosed()) {
                    try {
                        c.rollback();
                    } catch (SQLException se) {
                    }
                    c.close();
                }
            }
        } catch (SQLException e) {
        }
    }

    @Override
    public String toString() {
        return "connection pool";
    }

    public synchronized boolean closeAll() {
        try {
            ds.close();
            return true;
        } catch (Exception e) {
            log.error("Exception while closing all connections: " + e);
            return false;
        }
    }

    private static int MAX_CONN;

    private static int MAX_FREE;

    private static final transient Logger log = Logger.getLogger(ConnectionPool.class);

    private static ConnectionPool pool = null;

    private final LinkedList<Connection> freeConns = new LinkedList<Connection>();

    private final Set<Connection> usedConns = new HashSet<Connection>();

    private static String jdbcDriver;

    private static String jdbcURL;

    private String username;

    private String password;
}
