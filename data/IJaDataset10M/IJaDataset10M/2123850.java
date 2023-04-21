package co.edu.unal.ungrid.services.server.db.base;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.SQLException;
import java.util.Properties;
import co.edu.unal.ungrid.services.server.db.pool.ConnectionPool;

/**
 * @author  Administrator
 */
public abstract class AbstractConnector {

    public AbstractConnector(String sAlias, String sSource, String sDriver, String sUser, String sPwd, String sMinConn, String sMaxConn) {
        m_pProps = new Properties();
        m_pProps.put(ALIAS, sAlias);
        m_pProps.put(SOURCE, sSource);
        m_pProps.put(DRIVER, sDriver);
        m_pProps.put(USER, sUser);
        m_pProps.put(PWD, sPwd);
        int minCon = MINCON;
        int maxCon = MAXCON;
        try {
            minCon = Integer.parseInt(sMinConn);
            maxCon = Integer.parseInt(sMaxConn);
        } catch (Exception exc) {
            System.out.println("AbstractConnector::AbstractConnector(): exc=" + exc);
        }
        try {
            m_connPool = new ConnectionPool(this, minCon, maxCon, DEF_WAIT_TIMEOUT, DEF_MAX_LEASE_TIME);
        } catch (SQLException se) {
            System.out.println("AbstractConnector::AbstractConnector(): exc=" + se);
        }
    }

    public boolean initPool() throws SQLException {
        return m_connPool.initializePoolToMinConnections();
    }

    public Connection getConnection() {
        Connection conn = null;
        try {
            conn = m_connPool.getConnection();
        } catch (Exception exc) {
            System.out.println("AbstractConnector::getConnection(): exc=" + exc);
        }
        return conn;
    }

    public void freeConnection(Connection conn) {
        if (conn != null) {
            m_connPool.freeConnection(conn);
        }
    }

    public String getAlias() {
        return m_pProps.getProperty(ALIAS);
    }

    public abstract boolean loadDriver();

    public abstract Connection newConnection();

    public abstract Connection newTransConnection();

    public abstract boolean canAutoCreateModel();

    public abstract int maxLengthTransactionName();

    protected Properties m_pProps;

    protected ConnectionPool m_connPool;

    protected Driver m_dDriver;

    public static final String NAME = "name";

    public static final String ALIAS = "alias";

    public static final String SOURCE = "source";

    public static final String DRIVER = "driver";

    public static final String USER = "user";

    public static final String PWD = "password";

    public static final int MINCON = 1;

    public static final int MAXCON = 5;

    public static final int DEF_WAIT_TIMEOUT = 3 * 60 * 1000;

    public static final int DEF_MAX_LEASE_TIME = 5 * 60 * 1000;
}
