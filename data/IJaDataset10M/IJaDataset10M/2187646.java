package org.apache.commons.dbcp;

import java.sql.Connection;
import java.sql.SQLException;
import org.apache.commons.pool.ObjectPool;

/**
 * A delegating connection that, rather than closing the underlying
 * connection, returns itself to an {@link ObjectPool} when
 * closed.
 *
 * @author Rodney Waldhoff
 * @author Glenn L. Nielsen
 * @author James House
 * @version $Revision: 479137 $ $Date: 2006-11-25 08:51:48 -0700 (Sat, 25 Nov 2006) $
 */
public class PoolableConnection extends DelegatingConnection {

    protected ObjectPool _pool = null;

    /**
     *
     * @param conn my underlying connection
     * @param pool the pool to which I should return when closed
     */
    public PoolableConnection(Connection conn, ObjectPool pool) {
        super(conn);
        _pool = pool;
    }

    /**
     *
     * @param conn my underlying connection
     * @param pool the pool to which I should return when closed
     * @param config the abandoned configuration settings
     * @deprecated AbandonedConfig is now deprecated.
     */
    public PoolableConnection(Connection conn, ObjectPool pool, AbandonedConfig config) {
        super(conn, config);
        _pool = pool;
    }

    /**
     * Returns me to my pool.
     */
    public synchronized void close() throws SQLException {
        boolean isClosed = false;
        try {
            isClosed = isClosed();
        } catch (SQLException e) {
            try {
                _pool.invalidateObject(this);
            } catch (Exception ie) {
            }
            throw new SQLNestedException("Cannot close connection (isClosed check failed)", e);
        }
        if (isClosed) {
            try {
                _pool.invalidateObject(this);
            } catch (Exception ie) {
            }
            throw new SQLException("Already closed.");
        } else {
            try {
                _pool.returnObject(this);
            } catch (SQLException e) {
                throw e;
            } catch (RuntimeException e) {
                throw e;
            } catch (Exception e) {
                throw new SQLNestedException("Cannot close connection (return to pool failed)", e);
            }
        }
    }

    /**
     * Actually close my underlying {@link Connection}.
     */
    public void reallyClose() throws SQLException {
        super.close();
    }
}
