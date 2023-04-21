package jaxlib.ee.sql;

import java.sql.Connection;
import java.sql.SQLException;
import javax.sql.XAConnection;
import javax.transaction.Synchronization;
import javax.transaction.Transaction;
import jaxlib.ee.transaction.TransactionContext;
import jaxlib.ee.transaction.TransactionStatus;
import jaxlib.logging.Log;

/**
 * @author  jw
 * @since   JaXLib 1.0
 * @version $Id: XAManagedConnectionHolder.java 2744 2009-09-22 11:42:08Z joerg_wassmer $
 */
final class XAManagedConnectionHolder extends Object implements Synchronization {

    private static final Log log = Log.logger();

    static XAManagedConnectionHolder getOrCreate(final TransactionContext tc, final XAConnection xaConnection, final Object key) throws Exception {
        XAManagedConnectionHolder holder = tc.get(key);
        if (holder == null) {
            final Transaction tx = tc.getTransaction();
            tx.enlistResource(xaConnection.getXAResource());
            holder = new XAManagedConnectionHolder(xaConnection.getConnection());
            tx.registerSynchronization(holder);
            tc.put(key, holder);
        }
        return holder;
    }

    private Connection connection;

    private XAManagedConnectionHolder(final Connection connection) {
        super();
        this.connection = connection;
    }

    final Connection getConnection() {
        return this.connection;
    }

    @Override
    public final void afterCompletion(final int status) {
        final Connection connection = this.connection;
        if (connection != null) {
            this.connection = null;
            try {
                connection.close();
            } catch (final SQLException ex) {
                log.warning(ex, "ignoring exception, tx status = %s", TransactionStatus.of(status));
            }
        }
    }

    @Override
    public final void beforeCompletion() {
    }
}
