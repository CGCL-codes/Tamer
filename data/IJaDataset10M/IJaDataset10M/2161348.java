package tyrex.resource.jdbc;

import javax.sql.PooledConnection;
import javax.transaction.xa.XAResource;
import javax.transaction.xa.Xid;
import tyrex.services.Clock;
import tyrex.tm.XAResourceCallback;

/**
 * Represents an entry in the connection pool.
 *
 * @author <a href="arkin@intalio.com">Assaf Arkin</a>
 * @version $Revision: 1.4 $
 */
final class PoolEntry implements XAResourceCallback {

    /**
     * The pooled connection associated with this entry.
     */
    protected final PooledConnection _pooled;

    /**
     * The hash code for this entry.
     */
    protected final int _hashCode;

    /**
     * Reference to the next connection entry in hash table.
     */
    protected PoolEntry _nextEntry;

    /**
     * The state of the pooled connection. One of {@link #AVAILABLE},
     * {@link #IN_USE} or {@link #CLOSED}.
     */
    protected int _state;

    /**
     * The XA resource associated with this connection. May be null.
     */
    protected final XAResource _xaResource;

    /**
     * The timestamp for a used connection returns the clock time at which
     * the connection was made available to the application. The timestamp
     * for an unused connection returns the clock time at which the
     * connection was placed in the pool.
     */
    protected long _timeStamp;

    /**
     * The user name.
     */
    protected final String _user;

    /**
     * The password.
     */
    protected final String _password;

    /**
     * The connection pool
     */
    private final ConnectionPool _connectionPool;

    /**
     * The reference count to track the number of times the
     * XA resource is enlisted in a transaction.
     */
    protected int _enlistCount;

    /**
     * True if the XA resource has been enlisted in a transaction
     */
    protected boolean _enlistedInTransaction;

    /**
     * Constructs a new pool entry. A new pool entry is not available by
     * default. The <tt>available</tt> variable must be set to false to
     * make it available.
     *
     * @param connectionPool the connection pool to which this pool 
     * entry belongs
     * @param pooled The pooled connection
     * @param hashCode The managed connection hash code
     * @param xaResource The XA resource interface, or null
     * @param txManager The transaction manager in which this resource
     * is enlisted
     * @param user The user name or null
     * @param password The password or null
      */
    protected PoolEntry(ConnectionPool connectionPool, PooledConnection pooled, int hashCode, XAResource xaResource, String user, String password) {
        if (connectionPool == null) throw new IllegalArgumentException("Argument connectionPool is null");
        if (pooled == null) throw new IllegalArgumentException("Argument pooled is null");
        _connectionPool = connectionPool;
        _pooled = pooled;
        _hashCode = hashCode;
        _xaResource = xaResource;
        _user = user;
        _password = password;
        _state = ConnectionPool.IN_USE;
        _timeStamp = Clock.clock();
        _enlistCount = 0;
        _enlistedInTransaction = false;
    }

    /**
     * Called when the XA resource associated with this callback 
     * has been enlisted in a transaction,i.e. 
     * javax.transaction.XA.XAResource.start(javax.transaction.XA.XAResource.TMSTART)
     * has been called.
     *
     * @param xid the xid that was used to enlist the XA resource
     *      (required)
     */
    public void enlist(Xid xid) {
        synchronized (_connectionPool) {
            _enlistedInTransaction = true;
            ++_enlistCount;
            if (_connectionPool._category.isDebugEnabled()) {
                _connectionPool._category.debug(_pooled + " enlisted in transaction " + _enlistCount);
            }
        }
    }

    /**
     * Called when the XA resource associated with this callback 
     * has been delisted from a transaction,i.e. 
     * javax.transaction.XA.XAResource.end(javax.transaction.XA.XAResource.TMFAIL)
     * has been called.
     *
     * @param xid the xid that was used to enlist the XA resource
     *      (required)
     */
    public void fail(Xid xid) {
        synchronized (_connectionPool) {
            --_enlistCount;
            if (_connectionPool._category.isDebugEnabled()) {
                _connectionPool._category.debug(_pooled + " xa resource failed " + _enlistCount);
            }
            if ((ConnectionPool.CLOSED == _state) && (0 == _enlistCount)) {
                _connectionPool.release(_pooled, true);
            }
        }
    }

    /**
     * Called when the XA resource associated with this callback 
     * has been committed/rolledback in a transaction,i.e. 
     * javax.transaction.XA.XAResource.commit() or
     * javax.transaction.XA.XAResource.rollback()
     * has been called.
     *
     * @param xid the xid that was used to enlist the XA resource
     *      (required)
     * @param commit True if the XA resource has been committed.
     *      False if the XA resource has been rolled back.
     */
    public void boundary(Xid xid, boolean commit) {
        synchronized (_connectionPool) {
            --_enlistCount;
            if (_connectionPool._category.isDebugEnabled()) {
                _connectionPool._category.debug(_pooled + " transaction ended " + _enlistCount);
            }
            if ((ConnectionPool.CLOSED == _state) && (0 == _enlistCount)) {
                _connectionPool.release(_pooled, true);
            }
        }
    }
}
