package fr.soleil.hdbtdbArchivingApi.ArchivingApi.DataBaseManagement.DbConnection;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import oracle.jdbc.pool.OracleConnectionCacheManager;
import oracle.jdbc.pool.OracleDataSource;
import fr.esrf.Tango.ErrSeverity;
import fr.soleil.commonarchivingapi.ArchivingTools.Tools.GlobalConst;
import fr.soleil.hdbtdbArchivingApi.ArchivingApi.ConfigConst;
import fr.soleil.hdbtdbArchivingApi.ArchivingApi.DataBaseManagement.DbCommands.ConnectionCommands;
import fr.soleil.hdbtdbArchivingApi.ArchivingApi.DataBaseManagement.PoolConnection.IPoolConnection;
import fr.soleil.hdbtdbArchivingApi.ArchivingApi.DataBaseUtils.IDbUtils;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Tools.ArchivingException;

/**
 * @author AYADI
 *
 */
public class OracleConnection extends DBConnection {

    public OracleConnection(int type, IDbUtils ut, IPoolConnection pc) {
        super(pc);
        dbUtils = ut;
        setDriver(ConfigConst.DRIVER_ORACLE);
        setDbType(type);
    }

    public OracleConnection(int type, IDbUtils ut, String host, String name, String schema, String user, String pass, IPoolConnection pc) {
        super(host, name, schema, user, pass, pc);
        dbUtils = ut;
        setDriver(ConfigConst.DRIVER_ORACLE);
        setDbType(type);
    }

    /**
	  * <b>Description : </b>  Gets the current database's autocommit <br>
	  *
	  * @return True if database is in autocommit mode
	  */
    public boolean getAutoCommit() {
        return ConfigConst.AUTO_COMMIT_ORACLE;
    }

    /**
	 * <b>Description : </b> Allows to connect to the <I>HDB</I> database when of <I>Oracle</I> type.
	 *
	 * @throws ArchivingException
	 */
    public void connect() throws ArchivingException {
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            String url = ConfigConst.DRIVER_ORACLE + ":@" + getHost() + ":" + ConfigConst.ORACLE_PORT + ":" + getName();
            setDriver(ConfigConst.DRIVER_ORACLE);
            setM_dataSource(poolConnection.getPoolInstance(url, getUser(), getPasswd()));
        } catch (ClassNotFoundException e) {
            String message = GlobalConst.ARCHIVING_ERROR_PREFIX + " : " + GlobalConst.DRIVER_MISSING;
            String reason = "Failed while executing OracleConnection.connect_oracle() method...";
            String desc = "No Oracle driver available..., please check !";
            throw new ArchivingException(message, reason, ErrSeverity.PANIC, desc, "", e);
        }
    }

    public Connection getConnection() {
        try {
            Connection conn = getM_dataSource().getConnection();
            alterSession(conn);
            return conn;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        } catch (ArchivingException e) {
            return null;
        }
    }

    public void closeConnection(Connection conn) {
        try {
            if (conn != null) {
                conn.close();
                conn = null;
            }
            if (getM_dataSource() != null) {
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
	 * This method is used when connecting an Oracle database. It tunes the connection to the database.
	 *
	 * @throws ArchivingException
	 */
    private void alterSession(Connection conn) throws ArchivingException {
        if (conn == null) return;
        Statement stmt = null;
        String sqlStr1, sqlStr2, sqlStr3;
        sqlStr1 = "alter session set NLS_NUMERIC_CHARACTERS = \". \"";
        sqlStr2 = "alter session set NLS_TIMESTAMP_FORMAT = 'DD-MM-YYYY HH24:MI:SS.FF'";
        sqlStr3 = "alter session set NLS_DATE_FORMAT = 'DD-MM-YYYY HH24:MI:SS'";
        try {
            stmt = conn.createStatement();
            lastStatement = stmt;
            stmt.executeQuery(sqlStr1);
            stmt.executeQuery(sqlStr2);
            stmt.executeQuery(sqlStr3);
        } catch (SQLException e) {
            String message = dbUtils.getCommunicationFailureMessage(e);
            String reason = GlobalConst.STATEMENT_FAILURE;
            String desc = "Failed while executing OracleConnection.alterSession() method...";
            throw new ArchivingException(message, reason, ErrSeverity.WARN, desc, this.getClass().getName(), e);
        } finally {
            try {
                ConnectionCommands.close(stmt);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void loadDriver() {
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void setDataBaseUrl(String url) {
        url = ConfigConst.DRIVER_ORACLE + ":@" + getHost() + ":" + ConfigConst.ORACLE_PORT + ":" + getName();
        System.out.println("DBConnection/buildUrlAndConnect/BD_ORACLE/url|" + url);
    }
}
