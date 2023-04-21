package org.sgmiller.quickstem;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Stack;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Provides the basic methods begin, commit, and rollback.  
 * 
 * @author scott
 *
 */
public abstract class Transaction {

    private static final Queue<Transaction> openTransactions = new LinkedList<Transaction>();

    static final Transaction NONE = new ReadCommittedTransaction(Long.MAX_VALUE, TransactionState.COMMITTED, false);

    static final Transaction MIN_COMMITTED = new ReadCommittedTransaction(Long.MIN_VALUE, TransactionState.COMMITTED, false);

    private static final AtomicLong nexttid = new AtomicLong(Long.MIN_VALUE + 0x100);

    private static final ThreadLocal<Transaction> currentTransaction = new ThreadLocal<Transaction>();

    private static final ThreadLocal<Stack<Transaction>> currentTransactionStack = new ThreadLocal<Stack<Transaction>>();

    static final long nextTid() {
        return nexttid.getAndAdd(0x100);
    }

    static final int txnThreadCacheSize = 31;

    static final Transaction[] txnThreadCache = new Transaction[txnThreadCacheSize];

    /**
	 * Retrieve the active thread's current transaction.
	 * @return
	 */
    public static final Transaction current() {
        Thread currentThread = Thread.currentThread();
        int idx = (int) currentThread.getId() % txnThreadCacheSize;
        Transaction txn = txnThreadCache[idx];
        if (txn == null || (txn != NONE && txn.thread != currentThread)) {
            txn = currentTransaction.get();
            if (txn == null) {
                txn = NONE;
            }
            txnThreadCache[idx] = txn;
        }
        return txn;
    }

    /**
	 * Start a new STM transaction
	 */
    public static Transaction begin() {
        return begin(IsolationLevel.READ_COMMITTED);
    }

    /** 
	 * Start a new STM transaction with a specific isolation level
	 */
    public static Transaction begin(IsolationLevel isolationLevel) {
        Transaction current = currentTransaction.get();
        if (current == null) {
            switch(isolationLevel) {
                case READ_UNCOMMITTED:
                    current = new ReadUncommittedTransaction();
                    break;
                case READ_COMMITTED:
                    current = new ReadCommittedTransaction(true);
                    break;
                case REPEATABLE_READ:
                    current = new RepeatableReadTransaction();
                    break;
                case SERIALIZABLE:
                    current = new SerializableTransaction();
                    break;
            }
            current.setCurrent();
        }
        return current;
    }

    /**
	 * Commit a transaction.  All values modified will be visible to future transactions.
	 */
    public static void commit() {
        Transaction current = current();
        try {
            current.icommit(false);
        } catch (NullPointerException npe) {
            if (current == null) {
                throw new StemRuntimeException("Cannot commit, no transaction started.");
            } else {
                throw npe;
            }
        } catch (SerializationException se) {
            current.thread = null;
            currentTransaction.set(null);
            throw se;
        }
        current.thread = null;
        popSubtransaction();
    }

    private static void popSubtransaction() {
        Stack<Transaction> txnStack = currentTransactionStack.get();
        if (txnStack != null) {
            if (!txnStack.isEmpty()) {
                Transaction last = txnStack.pop();
                last.setCurrent();
                return;
            }
        }
        currentTransaction.set(null);
    }

    private static synchronized void pushSubtransaction() {
        Stack<Transaction> txnStack = currentTransactionStack.get();
        if (txnStack == null) {
            txnStack = new Stack<Transaction>();
            currentTransactionStack.set(txnStack);
        }
        txnStack.push(currentTransaction.get());
    }

    /**
	 * Rollback a transaction.  No modified values will be visible to any transaction.
	 */
    public static void rollback() {
        Transaction current = current();
        try {
            current.irollback();
        } catch (NullPointerException npe) {
            if (current == null) {
                throw new StemRuntimeException("Cannot commit, no transaction started.");
            } else {
                throw npe;
            }
        }
        current.thread = null;
        popSubtransaction();
    }

    protected static final long nestedMask(long tid) {
        return tid & 0xffffffffffffff00l;
    }

    AtomicInteger nextNestedTid = new AtomicInteger(0);

    long tid;

    final long parentTid;

    private final boolean tracked;

    protected TransactionState state = TransactionState.OPEN;

    protected final IsolationLevel isolationLevel;

    private Thread thread;

    List<Transaction> subtransactions = new ArrayList<Transaction>();

    Transaction(IsolationLevel level, boolean track) {
        this(level, nextTid(), TransactionState.OPEN, track);
    }

    protected long nextNestedTid() {
        int nestedTid = nextNestedTid.incrementAndGet();
        if (nestedTid > 0xff) throw new StemRuntimeException("Out of nested transaction ids");
        return nestedTid;
    }

    public static Transaction beginSubTransaction() {
        Transaction current = currentTransaction.get();
        if (current == null) {
            throw new StemRuntimeException("Cannot begin a subtransaction outside of a transaction");
        } else {
            long nestedTid = current.tid + current.nextNestedTid();
            current.thread = null;
            Transaction subTxn = null;
            switch(current.isolationLevel) {
                case READ_UNCOMMITTED:
                    subTxn = new ReadUncommittedTransaction(nestedTid);
                    break;
                case READ_COMMITTED:
                    subTxn = new ReadCommittedTransaction(nestedTid, true);
                    break;
                case REPEATABLE_READ:
                    subTxn = new RepeatableReadTransaction(nestedTid);
                    break;
                case SERIALIZABLE:
                    subTxn = new SerializableTransaction(nestedTid);
                    break;
            }
            current.subtransactions.add(subTxn);
            pushSubtransaction();
            subTxn.setCurrent();
            return subTxn;
        }
    }

    protected synchronized void track() {
        openTransactions.add(this);
    }

    Transaction(IsolationLevel level, long tid, TransactionState state, boolean track) {
        this.isolationLevel = level;
        this.tid = tid;
        this.parentTid = nestedMask(tid);
        this.tracked = track;
        this.state = state;
        assert (!tracked || state == TransactionState.OPEN);
        if (track) track();
    }

    public Transaction(IsolationLevel level, long tid, boolean track) {
        this(level, tid, TransactionState.OPEN, track);
    }

    static final Object VAL_CONTINUE = new Object();

    /**
	 * Retrieves the value of a particular stem variable as seen
	 * by this transaction.  Mostly used internally by the collections
	 * classes as an optimization.
	 * 
	 * @param <V>
	 * @param stem
	 * @return
	 */
    public <V> V get(Stem<V> stem) {
        Version<V> lc = stem.versions;
        Version<V> vc = lc;
        do {
            assert (vc != null);
            if (vc.txn == this) {
                break;
            } else if (vc.txn.state == TransactionState.ROLLED_BACK && lc != vc) {
                lc.last = vc.last;
                vc = vc.last;
                continue;
            } else {
                Object value = consider(stem, vc);
                if (value != VAL_CONTINUE) break;
            }
            lc = vc;
            vc = vc.last;
        } while (true);
        return (V) vc.value;
    }

    /**
	 * Specific implementations use this method to consider whether a particular version
	 * is visible to this transaction.  If so, it is returned in get()
	 */
    protected abstract Object consider(Stem<?> stem, Version<?> version);

    private void setState(final TransactionState state) {
        this.state = state;
        if (tracked && state != TransactionState.OPEN) {
            openTransactions.remove(this);
        }
        synchronized (this) {
            notifyAll();
        }
    }

    final synchronized void irollback() {
        if (state == TransactionState.OPEN) {
            setState(TransactionState.ROLLED_BACK);
            for (Transaction subtxn : subtransactions) {
                if (subtxn.state == TransactionState.COMMITTED) {
                    subtxn.setState(TransactionState.ROLLED_BACK);
                }
            }
        } else {
            throw new IllegalStateException("Cannot rollback a transaction in the " + state + " state.");
        }
    }

    void doCommit() {
    }

    final synchronized void icommit(boolean force) {
        if (state == TransactionState.OPEN) {
            if (force || !isSubtransaction()) {
                doCommit();
                for (Transaction subtxn : subtransactions) {
                    if (subtxn.state == TransactionState.OPEN) {
                        subtxn.icommit(true);
                    }
                }
                setState(TransactionState.COMMITTED);
            }
        } else {
            throw new IllegalStateException("Cannot commit a transaction in the " + state + " state.");
        }
    }

    public String toString() {
        return Long.toHexString(tid >>> 8) + "/" + Long.toHexString(tid & 0xff) + "[" + state + "]";
    }

    public int hashCode() {
        return (int) ((tid & 0xffffffff) ^ tid >>> 32);
    }

    public boolean equals(Object o) {
        return ((Transaction) o).tid == tid;
    }

    /**
	 * Set the current thread's active transaction
	 */
    public Transaction setCurrent() {
        thread = Thread.currentThread();
        currentTransaction.set(this);
        return this;
    }

    static Transaction getFirstOutstanding() {
        Transaction first = openTransactions.peek();
        if (first == null) return NONE; else return first;
    }

    static Transaction beginCommited() {
        return oneShot().setCurrent();
    }

    /**
	 * Creates an untracked transaction.  Used only by Stem.set when a transaction is not open.
	 * This creates proper versioning for autocommited sets and serialization conflict detection.
	 * @return
	 */
    static Transaction oneShot() {
        return new OneShotTransaction();
    }

    /**
	 * Return the current state of this transaction.
	 * @return
	 */
    public final TransactionState getState() {
        return state;
    }

    public static void printOpenTransactions() {
        System.out.println("Open transactions:");
        for (Transaction tr : openTransactions) {
            System.out.println(tr);
        }
    }

    public IsolationLevel getIsolationLevel() {
        return isolationLevel;
    }

    final boolean isTopLevel() {
        return parentTid == tid;
    }

    final boolean isSubtransaction(Transaction txn) {
        return txn.parentTid == tid;
    }

    final boolean isSubtransaction() {
        return parentTid != tid;
    }

    final boolean isRelated(Transaction txn) {
        return parentTid == txn.parentTid;
    }
}
