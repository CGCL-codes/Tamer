package jaxlib.ee.sql;

import java.io.PrintWriter;
import java.io.Serializable;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.logging.Logger;
import javax.sql.ConnectionPoolDataSource;
import javax.sql.PooledConnection;
import javax.sql.XADataSource;
import jaxlib.util.CheckArg;

/**
 * A datasource providing connections managed via the {@link jaxlib.ee.transaction.Transactions#getManager() global
 * default transaction manager}.
 *
 * @author  jw
 * @since   JaXLib 1.0
 * @version $Id: XAManagedConnectionPoolDataSource.java 2964 2011-07-31 15:50:55Z joerg_wassmer $
 */
final class XAManagedConnectionPoolDataSource extends Object implements ConnectionPoolDataSource, Serializable {

    /**
   * @since JaXLib 1.0
   */
    private static final long serialVersionUID = 1L;

    private final XADataSource dataSource;

    private final Object key;

    XAManagedConnectionPoolDataSource(final XADataSource dataSource, final Object key) {
        super();
        CheckArg.notNull(dataSource, "dataSource");
        this.dataSource = dataSource;
        this.key = key;
    }

    @Override
    public final int getLoginTimeout() throws SQLException {
        return this.dataSource.getLoginTimeout();
    }

    @Override
    public final PrintWriter getLogWriter() throws SQLException {
        return this.dataSource.getLogWriter();
    }

    @Override
    public final Logger getParentLogger() throws SQLFeatureNotSupportedException {
        return this.dataSource.getParentLogger();
    }

    @Override
    public final PooledConnection getPooledConnection() throws SQLException {
        return new XAManagedPooledConnection(this.key, this.dataSource.getXAConnection());
    }

    @Override
    public final PooledConnection getPooledConnection(final String user, final String password) throws SQLException {
        return new XAManagedPooledConnection(this.key, this.dataSource.getXAConnection(user, password));
    }

    @Override
    public final void setLoginTimeout(final int seconds) throws SQLException {
        this.dataSource.setLoginTimeout(seconds);
    }

    @Override
    public final void setLogWriter(final PrintWriter out) throws SQLException {
        this.dataSource.setLogWriter(out);
    }

    @Override
    public final String toString() {
        return super.toString() + "(" + this.dataSource + ")";
    }
}
