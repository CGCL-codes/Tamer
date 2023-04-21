package uk.org.primrose.pool.datasource;

import uk.org.primrose.Constants;
import uk.org.primrose.pool.PoolException;
import uk.org.primrose.pool.core.Pool;
import uk.org.primrose.pool.core.PoolLoader;
import java.io.PrintWriter;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.SQLException;
import javax.sql.DataSource;

public class PrimroseDataSource implements DataSource, Serializable {

    /** 
     * 
     */
    private static final long serialVersionUID = -3545935342768603717L;

    private String poolName = "";

    public PrimroseDataSource() {
    }

    public void setPoolName(String poolName) {
        this.poolName = poolName;
    }

    public String getPoolName() {
        return poolName;
    }

    /** 
    *   Attempts to establish a connection with the data source that this DataSource object represents. 
    */
    public Connection getConnection() throws SQLException {
        Pool pool = PoolLoader.findExistingPool(poolName);
        if (pool == null) {
            throw new SQLException("Cannot find primrose pool under name '" + poolName + "'");
        }
        try {
            return pool.getConnection();
        } catch (PoolException pe) {
            pool.getLogger().printStackTrace(pe);
            throw new SQLException(pe.toString());
        }
    }

    /** 
    *   Attempts to establish a connection with the data source that this DataSource object represents. 
    */
    public Connection getConnection(String username, String password) throws SQLException {
        return getConnection();
    }

    /** 
    *   Gets the maximum time in seconds that this data source can wait while attempting to connect to a database. 
    */
    public int getLoginTimeout() {
        return -1;
    }

    /** 
    *    Retrieves the log writer for this DataSource object. 
    */
    public PrintWriter getLogWriter() {
        return null;
    }

    /** 
    *   Sets the maximum time in seconds that this data source will wait while attempting to connect to a database. 
    */
    public void setLoginTimeout(int seconds) {
    }

    /** 
    *   Sets the log writer for this DataSource object to the given java.io.PrintWriter object. 
    */
    public void setLogWriter(PrintWriter out) {
    }

    @Override
    public boolean isWrapperFor(Class<?> arg0) throws SQLException {
        throw new SQLException("Unimplemented in Primrose " + Constants.VERSION);
    }

    @Override
    public <T> T unwrap(Class<T> arg0) throws SQLException {
        throw new SQLException("Unimplemented in Primrose " + Constants.VERSION);
    }
}
