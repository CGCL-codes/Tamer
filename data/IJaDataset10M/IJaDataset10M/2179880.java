package com.sun.jini.outrigger;

import com.sun.jini.constants.TxnConstants;
import com.sun.jini.constants.ThrowableConstants;
import com.sun.jini.logging.Levels;
import com.sun.jini.thread.RetryTask;
import com.sun.jini.thread.TaskManager;
import com.sun.jini.thread.WakeupManager;
import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.UnmarshalException;
import java.rmi.NoSuchObjectException;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.Iterator;
import java.util.Collection;
import java.util.Collections;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.jini.core.transaction.TransactionException;
import net.jini.core.transaction.UnknownTransactionException;
import net.jini.core.transaction.server.ServerTransaction;
import net.jini.core.transaction.server.TransactionConstants;

/**
 * A task that will try to validate the state of a transaction. This uses weak
 * references a good deal to let the other parts of the system be GC'ed as
 * necessary.
 * <p>
 * The retry mechanism is subtle, so bear with me. The purpose is to ensure that
 * if any activity is being blocked by a given transaction, that transaction
 * will be tested at some point in the future (if necessary, i.e., if it still
 * is thought to be active). We assume it to be rare that a transactions that
 * the space thinks is active is, in fact, aborted, so the algorithm is designed
 * to guarantee the detection without a lot of overhead, specifically without a
 * lot of RMI calls.
 * <p>
 * Each task has three values: a <code>nextQuery</code> time, a
 * <code>mustQuery</code> boolean that force the next query to be made, and
 * <code>deltaT</code>, the time at which the following query will be scheduled.
 * When the task is awakened at its <code>nextQuery</code> time, it checks to
 * see if it must make an actual query to the transaction manager, which it will
 * do if either <code>mustQuery</code> is <code>true</code>, or if we know about
 * any in progress queries on the space that are blocked on the transaction.
 * Whether or not an actual query is made, <code>deltaT</code> is added to
 * <code>nextQuery</code> to get the <code>nextQuery</code> time,
 * <code>deltaT</code> is doubled, and <code>mustQuery</code> boolean is set to
 * <code>false</code>.
 * <p>
 * There are two kinds of requests that a with which transaction can cause a
 * conflict -- those with long timeouts (such as blocking reads and takes) and
 * those that are under short timeouts (such as reads and takes with zero-length
 * timeouts). We will treat them separately at several points of the algorithm.
 * A short timeout is any query whose expiration time is sooner than the
 * <code>nextQuery</code> time. Any other timeout is long If a short query
 * arrives, <code>mustQuery</code> is set to <code>true</code>.
 * <p>
 * The result is that any time a transaction causes a conflict, if the query on
 * the space has not ended by the time of the <code>nextQuery</code> we will
 * attempt to poll the transaction manager. There will also poll the transaction
 * manager if any conflict occurred on a query on the space with a short
 * timeout.
 * <p>
 * The first time a transaction causes a conflict, we schedule a time in the
 * future at which we will poll its status. We do not poll right away because
 * often a transaction will complete on its own before we get to that time,
 * making the check unnecessary. An instant poll is, therefore, unnecessarily
 * aggressive, since giving an initial grace time will usually mean no poll is
 * made at all. So if the first conflict occurs at <i>T</i><sub>0</sub>, the
 * <code>nextQuery</code> value will be <i>T</i><sub>0</sub>
 * <code>+INITIAL_GRACE</code>, the boolean will be <code>true</code> to force
 * that poll to happen, and <code>deltaT</code> will be set to
 * <code>INITIAL_GRACE</code>.
 * 
 * @author Sun Microsystems, Inc.
 * 
 * @see TxnMonitor
 */
class TxnMonitorTask extends RetryTask implements TransactionConstants, com.sun.jini.constants.TimeConstants {

    /** transaction being monitored */
    private final Txn txn;

    /** the monitor we were made by */
    private final TxnMonitor monitor;

    /**
	 * All the queries on the space (not queries to the transaction manager)
	 * waiting for <code>txn</code> to be resolved. <code>null</code> until we
	 * have at least one. Represented by <code>QueryWatcher</code> objects.
	 */
    private Map queries;

    /** count of RemoteExceptions */
    private int failCnt;

    /**
	 * The next time we need to poll the transaction manager to get
	 * <code>txn</code>'s actual state.
	 */
    private long nextQuery;

    /**
	 * When we're given an opportunity to poll the transaction manager for the
	 * <code>txn</code>'s state, do so.
	 */
    private boolean mustQuery;

    /** next value added to <code>nextQuery</code> */
    private long deltaT;

    /**
	 * The initial grace period before the first query.
	 */
    private static final long INITIAL_GRACE = 15 * SECONDS;

    /**
	 * The retry time when we have an encountered an exception
	 */
    private static final long BETWEEN_EXCEPTIONS = 15 * SECONDS;

    /**
	 * The largest value that <code>deltaT</code> will reach.
	 */
    private static final long MAX_DELTA_T = 1 * HOURS;

    /**
	 * The maximum number of failures allowed in a row before we simply give up
	 * on the transaction and consider it aborted.
	 */
    private static final int MAX_FAILURES = 3;

    /** Logger for logging transaction related information */
    private static final Logger logger = Logger.getLogger(OutriggerServerImpl.txnLoggerName);

    /**
	 * Create a new TxnMonitorTask.
	 */
    TxnMonitorTask(Txn txn, TxnMonitor monitor, TaskManager manager, WakeupManager wakeupMgr) {
        super(manager, wakeupMgr);
        this.txn = txn;
        this.monitor = monitor;
        nextQuery = startTime();
        deltaT = INITIAL_GRACE;
        mustQuery = true;
    }

    /**
	 * Return the time of the next query, bumping <code>deltaT</code> as
	 * necessary for the next iteration. If the transaction has voted
	 * <code>PREPARED</code> or the manager has been giving us a
	 * <code>RemoteException</code>, we should retry on short times; otherwise
	 * we back off quickly.
	 */
    public long retryTime() {
        if (failCnt == 0 && txn.getState() != PREPARED) {
            if (logger.isLoggable(Level.FINEST)) {
                logger.log(Level.FINEST, "{0} retryTime adds {1}", new Object[] { this, new Long(deltaT) });
            }
            nextQuery += deltaT;
            if (deltaT < MAX_DELTA_T) deltaT = Math.min(deltaT * 2, MAX_DELTA_T);
        } else {
            if (logger.isLoggable(Level.FINEST)) {
                logger.log(Level.FINEST, "{0} retryTime adds {1} (for {2})", new Object[] { this, new Long(BETWEEN_EXCEPTIONS), (failCnt != 0 ? "failure" : "PREPARED") });
            }
            nextQuery += BETWEEN_EXCEPTIONS;
        }
        return nextQuery;
    }

    /**
	 * We can run in parallel with any task, so just return <CODE>false</CODE>.
	 */
    public boolean runAfter(java.util.List tasks, int size) {
        return false;
    }

    synchronized void addSibling(Txn txn) {
        if (queries == null || queries.size() == 0) return;
        Collection sibling = Collections.nCopies(1, txn);
        Iterator it = queries.keySet().iterator();
        while (it.hasNext()) {
            QueryWatcher query = (QueryWatcher) it.next();
            if (query != null) monitor.add(query, sibling);
        }
    }

    /**
	 * Try to see if this transaction should be aborted. This returns
	 * <code>true</code> (don't repeat the task) if it knows that the
	 * transaction is no longer interesting to anyone.
	 */
    public boolean tryOnce() {
        if (logger.isLoggable(Level.FINEST)) {
            logger.log(Level.FINEST, "{0} attempt {1} mustQuery:{2}", new Object[] { this, new Integer(attempt()), new Boolean(mustQuery) });
        }
        if (attempt() == 0) return false;
        if (logger.isLoggable(Level.FINEST)) {
            logger.log(Level.FINEST, "{0} txn.getState() = {1}", new Object[] { this, new Integer(txn.getState()) });
        }
        int txnState = txn.getState();
        if (txnState != ACTIVE && txnState != PREPARED) return true;
        mustQuery |= (txnState == PREPARED);
        synchronized (this) {
            if (!mustQuery) {
                if (queries == null) return false;
                Iterator it = queries.keySet().iterator();
                boolean foundNeed = false;
                if (logger.isLoggable(Level.FINEST)) {
                    logger.log(Level.FINEST, "{0} nextQuery {1}", new Object[] { this, new Long(nextQuery) });
                }
                while (it.hasNext()) {
                    QueryWatcher query = (QueryWatcher) it.next();
                    if (query == null) continue;
                    if (logger.isLoggable(Level.FINEST)) {
                        logger.log(Level.FINEST, "{0} query.getExpiration() {1}", new Object[] { this, new Long(query.getExpiration()) });
                    }
                    if (query.getExpiration() < nextQuery || query.isResolved()) it.remove(); else {
                        foundNeed = true;
                        break;
                    }
                }
                if (logger.isLoggable(Level.FINEST)) {
                    logger.log(Level.FINEST, "{0} foundNeed = {1}", new Object[] { this, new Boolean(foundNeed) });
                }
                if (!foundNeed) return false;
            }
            mustQuery = false;
        }
        ServerTransaction tr;
        try {
            tr = txn.getTransaction(monitor.space().getRecoveredTransactionManagerPreparer());
        } catch (RemoteException e) {
            final int cat = ThrowableConstants.retryable(e);
            if (cat == ThrowableConstants.BAD_INVOCATION || cat == ThrowableConstants.BAD_OBJECT) {
                logUnpackingFailure("definite exception", Level.INFO, true, e);
                return true;
            } else if (cat == ThrowableConstants.INDEFINITE) {
                logUnpackingFailure("indefinite exception", Levels.FAILED, false, e);
                mustQuery = true;
                return false;
            } else if (cat == ThrowableConstants.UNCATEGORIZED) {
                mustQuery = true;
                logUnpackingFailure("uncategorized exception", Level.INFO, false, e);
                return false;
            } else {
                logger.log(Level.WARNING, "ThrowableConstants.retryable " + "returned out of range value, " + cat, new AssertionError(e));
                return false;
            }
        } catch (IOException e) {
            logUnpackingFailure("IOException", Level.INFO, true, e);
            return true;
        } catch (RuntimeException e) {
            logUnpackingFailure("RuntimeException", Level.INFO, true, e);
            return true;
        } catch (ClassNotFoundException e) {
            logUnpackingFailure("ClassNotFoundException", Levels.FAILED, false, e);
            mustQuery = true;
            return false;
        }
        if (logger.isLoggable(Level.FINEST)) logger.log(Level.FINEST, "{0} tr = {1}", new Object[] { this, tr });
        int trState;
        try {
            trState = tr.getState();
        } catch (TransactionException e) {
            if (logger.isLoggable(Level.INFO)) logger.log(Level.INFO, "Got TransactionException when " + "calling getState on " + tr + ", dropping transaction " + tr.id, e);
            trState = ABORTED;
        } catch (NoSuchObjectException e) {
            if (++failCnt >= MAX_FAILURES) {
                if (logger.isLoggable(Level.INFO)) {
                    logger.log(Level.INFO, "Got NoSuchObjectException when " + "calling getState on " + tr + ", this was the " + failCnt + " RemoteException, dropping transaction" + tr.id, e);
                }
                trState = ABORTED;
            } else {
                if (logger.isLoggable(Levels.FAILED)) {
                    logger.log(Levels.FAILED, "Got NoSuchObjectException " + "when calling getState on " + tr + ", failCount = " + failCnt + ", will retry", e);
                }
                mustQuery = true;
                return false;
            }
        } catch (RemoteException e) {
            if (++failCnt >= MAX_FAILURES) {
                synchronized (txn) {
                    switch(txn.getState()) {
                        case ACTIVE:
                            if (logger.isLoggable(Level.INFO)) {
                                logger.log(Level.INFO, "Got RemoteException " + "when calling getState on " + tr + ", this " + "was " + failCnt + " RemoteException, " + "dropping active transaction " + tr.id, e);
                            }
                            try {
                                monitor.space().abort(tr.mgr, tr.id);
                                return true;
                            } catch (UnknownTransactionException ute) {
                                throw new AssertionError(ute);
                            } catch (UnmarshalException ume) {
                                throw new AssertionError(ume);
                            }
                        case PREPARED:
                            final Level l = (failCnt % MAX_FAILURES == 0) ? Level.INFO : Levels.FAILED;
                            if (logger.isLoggable(l)) {
                                logger.log(l, "Got RemoteException when calling " + "getState on " + tr + ", this was " + failCnt + " RemoteException, will keep " + "prepared transaction " + tr.id, e);
                            }
                            mustQuery = true;
                            return false;
                        case ABORTED:
                        case COMMITTED:
                            return true;
                        default:
                            throw new AssertionError("Txn in unreachable state");
                    }
                }
            } else {
                if (logger.isLoggable(Levels.FAILED)) {
                    logger.log(Levels.FAILED, "Got RemoteException when " + "calling getState on " + tr + ", failCount = " + failCnt + ", will retry", e);
                }
                mustQuery = true;
                return false;
            }
        }
        if (logger.isLoggable(Level.FINER)) {
            logger.log(Level.FINER, "{0} trState = {1}", new Object[] { this, new Integer(trState) });
        }
        failCnt = 0;
        if (trState != txnState) {
            if (logger.isLoggable(Level.FINER)) {
                logger.log(Level.FINER, "{0} mgr state[{1}] != local state [{2}]", new Object[] { this, TxnConstants.getName(trState), TxnConstants.getName(txnState) });
            }
            try {
                switch(trState) {
                    case ABORTED:
                        logger.log(Level.FINER, "{0} moving to abort", this);
                        monitor.space().abort(tr.mgr, tr.id);
                        return true;
                    case COMMITTED:
                        logger.log(Level.FINER, "{0} moving to commit", this);
                        monitor.space().commit(tr.mgr, tr.id);
                        return true;
                }
            } catch (UnknownTransactionException e) {
                return true;
            } catch (UnmarshalException ume) {
                throw new AssertionError(ume);
            }
        }
        logger.log(Level.FINEST, "{0} return false", this);
        return false;
    }

    /**
	 * Add in a resource. The lease may already be in, in which case it is
	 * ignored, or it may be null, in which case it was a non-leased probe that
	 * was blocked and we simply set <code>mustQuery</code> to <code>true</code>
	 * .
	 */
    synchronized void add(QueryWatcher query) {
        if (query == null || query.getExpiration() <= nextQuery) {
            if (logger.isLoggable(Level.FINEST)) logger.log(Level.FINEST, "adding resource to task -- SHORT");
            mustQuery = true;
        } else {
            if (logger.isLoggable(Level.FINEST)) logger.log(Level.FINEST, "adding resource to task -- LONG");
            if (queries == null) queries = new WeakHashMap();
            queries.put(query, null);
        }
    }

    /** Log failed unpacking attempt attempt */
    private void logUnpackingFailure(String exceptionDescription, Level level, boolean terminal, Throwable t) {
        if (logger.isLoggable(level)) {
            logger.log(level, "Encountered " + exceptionDescription + "while unpacking exception to check state, " + (terminal ? "dropping" : "keeping") + " monitoring task", t);
        }
    }
}
