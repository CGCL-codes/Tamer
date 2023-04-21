package org.t2framework.daisy.core.pool.impl;

import java.sql.SQLException;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;
import java.util.TimerTask;
import java.util.Map.Entry;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import javax.sql.PooledConnection;
import javax.transaction.Transaction;
import org.t2framework.commons.util.Foreach;
import org.t2framework.commons.util.Logger;
import org.t2framework.daisy.core.DaisyCoreMessages;
import org.t2framework.daisy.core.datasource.XADataSourceBridge;
import org.t2framework.daisy.core.pool.ConnectionPool;
import org.t2framework.daisy.core.util.JDBCCloseableUtil;
import org.t2framework.daisy.core.xa.impl.XAConnectionImpl;

/**
 * 
 * {@.en An implementation of {@link ConnectionPool} and
 * {@link XAConnectionPool}.}
 * 
 * <br />
 * 
 * {@.ja }
 * 
 * @author shot
 * 
 */
public class ConnectionPoolImpl implements XAConnectionPool {

    /**
	 * Logger.
	 */
    protected static Logger logger = Logger.getLogger(ConnectionPoolImpl.class);

    /**
	 * Max active pool size.
	 */
    protected int maxActiveSize = 10;

    /**
	 * Free pool time out(msec).
	 */
    protected long freePoolTimeout = 60000;

    /**
	 * Active pooled connection map which is keyed by transaction.
	 */
    protected Map<Transaction, XAConnectionImpl> activeTxConnectionMap = new HashMap<Transaction, XAConnectionImpl>();

    /**
	 * Active pooled connection set which is not associated with any
	 * transaction.
	 */
    protected Set<XAConnectionImpl> activeConnectionSet = Collections.synchronizedSet(new HashSet<XAConnectionImpl>());

    /**
	 * The scheduler.
	 */
    protected ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

    /**
	 * Free pool with {@link PoolTask}.
	 */
    protected LinkedList<PoolTask> freePool = new LinkedList<PoolTask>();

    /**
	 * {@link XADataSourceBridge} instance.This must be set before using
	 * {@link ConnectionPoolImpl}.
	 */
    protected XADataSourceBridge<XAConnectionImpl> bridge;

    public ConnectionPoolImpl() {
    }

    public ConnectionPoolImpl(XADataSourceBridge<XAConnectionImpl> bridge) {
        this.bridge = bridge;
    }

    /**
	 * Get {@link XAConnectionImpl} as {@link PooledConnection}.If there is
	 * already associated with the transaction, then return that one.Or if free
	 * pool has one or more, return it.After all, if there is no pooled
	 * connection exist, create a new one for pooling.This method is
	 * thread-safe.
	 */
    @Override
    public synchronized XAConnectionImpl get() throws SQLException {
        logger.debug(DaisyCoreMessages.DDaisyCore0102);
        Transaction transaction = getTransaction();
        XAConnectionImpl pooledConnection = null;
        if (transaction != null) {
            pooledConnection = activeTxConnectionMap.get(transaction);
            logger.debug(DaisyCoreMessages.DDaisyCore0104, new Object[] { transaction });
        }
        if (pooledConnection == null) {
            waitUntilFree();
            if (freePool.isEmpty()) {
                pooledConnection = bridge.createPooledConnection();
                logger.debug(DaisyCoreMessages.DDaisyCore0105);
            } else {
                PoolTask poolTask = freePool.removeLast();
                poolTask.cancel();
                pooledConnection = poolTask.pooledConnection;
                if (bridge.validateConnection(pooledConnection) == false) {
                    pooledConnection.close();
                    pooledConnection = bridge.createPooledConnection();
                }
            }
            if (transaction != null) {
                mapTransactionAndPooledConnection(transaction, pooledConnection);
            } else {
                addActivePooledConnection(pooledConnection);
            }
        }
        logger.debug(DaisyCoreMessages.DDaisyCore0103);
        return pooledConnection;
    }

    /**
	 * 
	 * {@.en Make an association with the transaction and pooled connection.}
	 * 
	 * <br />
	 * 
	 * {@.ja }
	 * 
	 * @param transaction
	 * @param pooledConnection
	 * @throws IllegalStateException
	 * @throws SQLException
	 */
    protected void mapTransactionAndPooledConnection(final Transaction transaction, final XAConnectionImpl pooledConnection) throws IllegalStateException, SQLException {
        if (transaction == null) {
            throw new IllegalStateException("transaction must not be null.");
        }
        synchronized (this) {
            activeTxConnectionMap.put(transaction, pooledConnection);
            bridge.setupTransaction(transaction, pooledConnection);
        }
    }

    /**
	 * 
	 * {@.en Make pooled connection status active.}
	 * 
	 * <br />
	 * 
	 * {@.ja }
	 * 
	 * @param pooledConnection
	 */
    protected void addActivePooledConnection(XAConnectionImpl pooledConnection) {
        activeConnectionSet.add(pooledConnection);
    }

    /**
	 * Recycle pooled connection.
	 */
    @Override
    public void recycle(XAConnectionImpl pooledConnection) {
        final Transaction transaction = getTransaction();
        if (transaction != null) {
            recycleByTransaction(transaction);
            logger.debug(DaisyCoreMessages.DDaisyCore0106, new Object[] { transaction, pooledConnection });
        } else {
            recyclePooledConnection(pooledConnection);
            logger.debug(DaisyCoreMessages.DDaisyCore0107, new Object[] { pooledConnection });
        }
    }

    /**
	 * Dispose the given pooled connection.
	 */
    @Override
    public void dispose(XAConnectionImpl pooledConnection) {
        final Transaction transaction = getTransaction();
        if (transaction != null) {
            disposeByTransaction(transaction);
            logger.debug(DaisyCoreMessages.DDaisyCore0108, new Object[] { transaction, pooledConnection });
        } else {
            disposePooledConnection(pooledConnection);
            logger.debug(DaisyCoreMessages.DDaisyCore0109, new Object[] { pooledConnection });
        }
    }

    /**
	 * 
	 * {@.en Recycle pooled connection by associated transaction.}
	 * 
	 * <br />
	 * 
	 * {@.ja }
	 * 
	 * @param transaction
	 */
    protected void recycleByTransaction(final Transaction transaction) {
        if (transaction == null) {
            return;
        }
        synchronized (this) {
            final XAConnectionImpl pooledConnection = passivatePooledConnectionByTransaction(transaction);
            if (pooledConnection != null) {
                addFreePool(pooledConnection);
            }
        }
    }

    /**
	 * 
	 * {@.en Recycle pooled connection.}
	 * 
	 * <br />
	 * 
	 * {@.ja }
	 * 
	 * @param pooledConnection
	 */
    protected void recyclePooledConnection(XAConnectionImpl pooledConnection) {
        if (pooledConnection == null) {
            return;
        }
        synchronized (this) {
            activeConnectionSet.remove(pooledConnection);
            addFreePool(pooledConnection);
        }
    }

    /**
	 * 
	 * {@.en Passivate pooled connection associated by the transaction.}
	 * 
	 * <br />
	 * 
	 * {@.ja }
	 * 
	 * @param transaction
	 * @return
	 */
    protected XAConnectionImpl passivatePooledConnectionByTransaction(Transaction transaction) {
        final XAConnectionImpl pooledConnection = activeTxConnectionMap.remove(transaction);
        return pooledConnection;
    }

    /**
	 * 
	 * {@.en Dispose pooled connection which is associated by the given
	 * transaction.}
	 * 
	 * <br />
	 * 
	 * {@.ja }
	 * 
	 * @param transaction
	 */
    protected void disposeByTransaction(final Transaction transaction) {
        if (transaction == null) {
            return;
        }
        synchronized (this) {
            final XAConnectionImpl pooledConnection = passivatePooledConnectionByTransaction(transaction);
            if (pooledConnection != null) {
                JDBCCloseableUtil.closeQuietly(pooledConnection);
            }
        }
    }

    /**
	 * 
	 * {@.en Dispose pooled connection.}
	 * 
	 * <br />
	 * 
	 * {@.ja }
	 * 
	 * @param pooledConnection
	 */
    protected void disposePooledConnection(XAConnectionImpl pooledConnection) {
        if (pooledConnection == null) {
            return;
        }
        synchronized (this) {
            activeConnectionSet.remove(pooledConnection);
            JDBCCloseableUtil.close(pooledConnection);
        }
    }

    /**
	 * 
	 * {@.en Add given pooled connection to free pool.}
	 * 
	 * <br />
	 * 
	 * {@.ja }
	 * 
	 * @param pooledConnection
	 */
    protected void addFreePool(XAConnectionImpl pooledConnection) {
        try {
            PoolTask poolTask = new PoolTask(pooledConnection);
            final long freePoolTimeout = getFreePoolTimeout();
            scheduler.scheduleAtFixedRate(poolTask, freePoolTimeout, freePoolTimeout, TimeUnit.MILLISECONDS);
            freePool.addLast(poolTask);
        } catch (RejectedExecutionException e) {
            logger.debug("rejected by executor : {}", new Object[] { e.getMessage() });
            dispose(pooledConnection);
        }
    }

    /**
	 * 
	 * {@.en Get transaction.}
	 * 
	 * <br />
	 * 
	 * {@.ja }
	 * 
	 * @return
	 */
    protected Transaction getTransaction() {
        return this.bridge.getTransaction();
    }

    /**
	 * 
	 * {@.en Wait and see until active pool makes available.}
	 * 
	 * <br />
	 * 
	 * {@.ja }
	 * 
	 */
    protected void waitUntilFree() {
        for (; getMaxActiveSize() < (activeTxConnectionMap.size() + activeConnectionSet.size() + 1); ) {
            try {
                wait();
            } catch (InterruptedException ignore) {
            }
        }
        return;
    }

    /**
	 * 
	 * {@.en Internal pooling timer task.}
	 * 
	 * <br />
	 * 
	 * {@.ja }
	 * 
	 * @author shot
	 * 
	 */
    protected class PoolTask extends TimerTask {

        XAConnectionImpl pooledConnection;

        public PoolTask(XAConnectionImpl pooledConnection) {
            this.pooledConnection = pooledConnection;
        }

        @Override
        public void run() {
            try {
                logger.debug("PoolTask running.");
                synchronized (ConnectionPoolImpl.this) {
                    freePool.remove(this);
                }
                if (pooledConnection != null) {
                    JDBCCloseableUtil.closeQuietly(pooledConnection);
                }
            } catch (Throwable t) {
                logger.debug("Error at PoolTask.run() : {}", t.getMessage());
            } finally {
                this.pooledConnection = null;
            }
        }
    }

    /**
	 * Dispose all connection pool and associated resources.
	 */
    @Override
    public void disposeAll() {
        logger.debug(DaisyCoreMessages.DDaisyCore0110);
        scheduler.shutdown();
        try {
            if (scheduler.awaitTermination(getFreePoolTimeout(), TimeUnit.SECONDS) == false) {
                scheduler.shutdownNow();
            }
        } catch (InterruptedException e) {
            logger.debug("interrupted by other thread : {}", e.getMessage());
            scheduler.shutdownNow();
        }
        synchronized (this) {
            clearActiveTransactionPool();
            clearActivePool();
            clearFreePool();
        }
        this.bridge = null;
        logger.debug(DaisyCoreMessages.DDaisyCore0111);
    }

    /**
	 * 
	 * {@.en Clear active transaction pool.}
	 * 
	 * <br />
	 * 
	 * {@.ja }
	 * 
	 */
    protected synchronized void clearActiveTransactionPool() {
        if (activeTxConnectionMap.isEmpty() == false) {
            Foreach.each(activeTxConnectionMap, new Foreach.EasyMapItem<Transaction, XAConnectionImpl>() {

                @Override
                public void each(Entry<Transaction, XAConnectionImpl> target, int index, Iterable<Entry<Transaction, XAConnectionImpl>> all) throws RuntimeException {
                    XAConnectionImpl pooledConnection = target.getValue();
                    JDBCCloseableUtil.closeQuietly(pooledConnection);
                }
            });
            activeTxConnectionMap.clear();
        }
    }

    /**
	 * 
	 * {@.en Clear active pool.}
	 * 
	 * <br />
	 * 
	 * {@.ja }
	 * 
	 */
    protected synchronized void clearActivePool() {
        if (activeConnectionSet.isEmpty() == false) {
            Foreach.each(activeConnectionSet, new Foreach.EasyIterableItem<XAConnectionImpl>() {

                @Override
                public void each(XAConnectionImpl target, int index, Iterable<XAConnectionImpl> all) throws RuntimeException {
                    JDBCCloseableUtil.closeQuietly(target);
                }
            });
            activeConnectionSet.clear();
        }
    }

    /**
	 * Clear free pool.
	 */
    @Override
    public synchronized void clearFreePool() {
        if (freePool.isEmpty() == false) {
            Foreach.each(freePool, new Foreach.EasyIterableItem<PoolTask>() {

                @Override
                public void each(PoolTask target, int index, Iterable<PoolTask> all) throws RuntimeException {
                    target.cancel();
                    JDBCCloseableUtil.closeQuietly(target.pooledConnection);
                }
            });
            freePool.clear();
        }
    }

    /**
	 * 
	 * {@.en Set max active size. Max active size must be more than zero.}
	 * 
	 * <br />
	 * 
	 * {@.ja }
	 * 
	 * @param maxActiveSize
	 */
    public void setMaxActiveSize(int maxActiveSize) {
        if (maxActiveSize < 1) {
            throw new IllegalArgumentException();
        }
        this.maxActiveSize = maxActiveSize;
    }

    /**
	 * 
	 * {@.en Get max active size.}
	 * 
	 * <br />
	 * 
	 * {@.ja }
	 * 
	 * @return
	 */
    public int getMaxActiveSize() {
        return maxActiveSize;
    }

    /**
	 * 
	 * {@.en Get free pool timeout(msec).}
	 * 
	 * <br />
	 * 
	 * {@.ja }
	 * 
	 * @return
	 */
    public long getFreePoolTimeout() {
        return freePoolTimeout;
    }

    /**
	 * 
	 * {@.en Set free pool timeout(msec).}
	 * 
	 * <br />
	 * 
	 * {@.ja }
	 * 
	 * @param freePoolTimeout
	 */
    public void setFreePoolTimeout(long freePoolTimeout) {
        this.freePoolTimeout = freePoolTimeout;
    }

    /**
	 * 
	 * {@.en Get {@link XADataSourceBridge}.}
	 * 
	 * <br />
	 * 
	 * {@.ja }
	 * 
	 * @return
	 */
    public XADataSourceBridge<XAConnectionImpl> getBridge() {
        return bridge;
    }

    /**
	 * Set {@link XADataSourceBridge}.
	 */
    @Override
    public void setBridge(XADataSourceBridge<XAConnectionImpl> bridge) {
        this.bridge = bridge;
    }
}
