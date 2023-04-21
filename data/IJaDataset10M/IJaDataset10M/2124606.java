package org.nexopenframework.management.jdbc3.monitor.jdbcx;

import java.sql.SQLException;
import javax.sql.XAConnection;
import javax.transaction.xa.XAResource;

/**
 * <p>NexOpen Framework</p>
 * 
 * <p>Implementation of {@link XAConnection} for monitoring a custom implementation</p>
 * 
 * @see org.nexopenframework.management.jdbc3.monitor.jdbc.DelegateConnection
 * @see javax.sql.XAConnection
 * @author  Francesc Xavier Magdaleno
 * @version 1.0
 * @since 1.0.0.m2
 */
public class MonitorableXAConnection extends MonitorablePooledConnection implements XAConnection {

    /**
	 * <p></p>
	 * 
	 * @param pooledConnection
	 */
    MonitorableXAConnection(final XAConnection pooledConnection, final String user) {
        super(pooledConnection, user);
    }

    public XAResource getXAResource() throws SQLException {
        if (this.pooledConnection instanceof XAConnection) {
            return ((XAConnection) this.pooledConnection).getXAResource();
        }
        throw new SQLException("Not a XAConnection " + this.pooledConnection);
    }
}
