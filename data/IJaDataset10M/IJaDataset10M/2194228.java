package tyrex.tm.impl;

import javax.transaction.xa.XAResource;
import javax.naming.Context;
import javax.naming.NamingException;
import javax.security.auth.Subject;
import javax.transaction.Transaction;
import tyrex.tm.RuntimeContext;
import tyrex.naming.MemoryContext;
import tyrex.naming.MemoryContextFactory;
import tyrex.naming.MemoryBinding;
import tyrex.tm.XAResourceCallback;
import tyrex.util.FastThreadLocal;

/**
 * Implementation of {@link RuntimeContext}.
 *
 * @author <a href="arkin@intalio.com">Assaf Arkin</a>
 * @version $Revision: 1.10 $ $Date: 2001/10/05 22:15:34 $
 */
public class ThreadContext extends RuntimeContext {

    /**
     * The transaction associated with this thread, if the thread
     * is in a transaction, or null if the thread is not in a
     * transaction.
     */
    protected TransactionImpl _tx;

    /**
     * The XA resources and callbacks that have been opened before or during the
     * transaction and must be enlisted with the transaction when
     * the transaction starts. Allows null entries, but no duplicates.
     * May be null.
     */
    protected InternalXAResourceHolder _xaResourceHolder;

    private final Subject _subject;

    private final MemoryBinding _bindings;

    private static ThreadEntry[] _table;

    /**
     * Determines the size of the hash table. This must be a prime
     * value and within range of the average number of threads we
     * want to deal with. Potential values are:
     * <pre>
     * Threads  Prime
     * -------  -----
     *   256     239
     *   512     521
     *   1024    1103
     *   2048    2333
     *   4096    4049
     * </pre>
     */
    private static final int TABLE_SIZE = 1103;

    public ThreadContext(Subject subject) {
        _bindings = new MemoryBinding();
        _subject = subject;
    }

    public ThreadContext(Context context, Subject subject) throws NamingException {
        if (context == null) _bindings = new MemoryBinding(); else {
            if (!(context instanceof MemoryContext)) throw new NamingException("The context was not created from " + MemoryContextFactory.class.getName());
            _bindings = ((MemoryContext) context).getBindings();
            if (!_bindings.isRoot()) throw new NamingException("The context is not a root context");
        }
        _subject = subject;
    }

    static {
        _table = new ThreadEntry[TABLE_SIZE];
    }

    public static ThreadContext getThreadContext() {
        ThreadEntry entry;
        ThreadContext context;
        Thread thread;
        int index;
        thread = Thread.currentThread();
        index = (thread.hashCode() & 0x7FFFFFFF) % _table.length;
        entry = _table[index];
        while (entry != null) {
            if (entry._thread == thread) return entry._context;
            entry = entry._nextEntry;
        }
        synchronized (_table) {
            context = new ThreadContext(null);
            entry = new ThreadEntry(context, thread, null);
            entry._nextEntry = _table[index];
            _table[index] = entry;
        }
        return context;
    }

    public static ThreadContext getThreadContext(Thread thread) {
        ThreadEntry entry;
        ThreadContext context;
        int index;
        index = (thread.hashCode() & 0x7FFFFFFF) % _table.length;
        entry = _table[index];
        while (entry != null) {
            if (entry._thread == thread) return entry._context;
            entry = entry._nextEntry;
        }
        synchronized (_table) {
            context = new ThreadContext(null);
            entry = new ThreadEntry(context, thread, null);
            entry._nextEntry = _table[index];
            _table[index] = entry;
        }
        return context;
    }

    public static void setThreadContext(ThreadContext context) {
        Thread thread;
        ThreadEntry entry;
        ThreadEntry next;
        int index;
        if (context == null) throw new IllegalArgumentException("Argument context is null");
        synchronized (_table) {
            thread = Thread.currentThread();
            index = (thread.hashCode() & 0x7FFFFFFF) % _table.length;
            entry = _table[index];
            if (entry != null && entry._thread == thread) {
                entry = new ThreadEntry(context, thread, entry);
                _table[index] = entry;
                return;
            }
            if (entry != null) next = entry._nextEntry; else next = null;
            while (next != null) {
                if (next._thread == thread) {
                    next = new ThreadEntry(context, thread, next);
                    entry._nextEntry = next;
                    return;
                }
                entry = next;
                next = next._nextEntry;
            }
            entry = new ThreadEntry(context, thread, null);
            entry._nextEntry = _table[index];
            _table[index] = entry;
        }
    }

    public static ThreadContext unsetThreadContext() {
        Thread thread;
        ThreadEntry entry;
        ThreadEntry next;
        ThreadEntry previous;
        int index;
        synchronized (_table) {
            thread = Thread.currentThread();
            index = (thread.hashCode() & 0x7FFFFFFF) % _table.length;
            entry = _table[index];
            if (entry == null) return null; else if (entry._thread == thread) {
                previous = entry._previous;
                if (previous == null) _table[index] = entry._nextEntry; else {
                    previous._nextEntry = entry._nextEntry;
                    _table[index] = previous;
                }
                return entry._context;
            }
            next = entry._nextEntry;
            while (next != null) {
                if (next._thread == thread) {
                    previous = next._previous;
                    if (previous == null) entry._nextEntry = next._nextEntry; else {
                        previous._nextEntry = next._nextEntry;
                        entry._nextEntry = previous;
                    }
                    return next._context;
                }
                entry = next;
                next = next._nextEntry;
            }
        }
        return null;
    }

    public static void cleanup(Thread thread) {
        ThreadEntry entry;
        ThreadEntry next;
        int index;
        if (thread == null) throw new IllegalArgumentException("Argument thread is null");
        synchronized (_table) {
            index = (thread.hashCode() & 0x7FFFFFFF) % _table.length;
            entry = _table[index];
            if (entry == null) return;
            if (entry._thread == thread) {
                _table[index] = entry._nextEntry;
                return;
            }
            next = entry._nextEntry;
            while (next != null) {
                if (next._thread == thread) {
                    entry._nextEntry = next._nextEntry;
                    return;
                }
                entry = next;
                next = next._nextEntry;
            }
        }
    }

    public Context getEnvContext() {
        return _bindings.getContext();
    }

    public Transaction getTransaction() {
        return _tx;
    }

    public Subject getSubject() {
        return _subject;
    }

    public void cleanup() {
        _tx = null;
        _xaResourceHolder = null;
    }

    public MemoryBinding getMemoryBinding() {
        return _bindings;
    }

    /**
     * Adds an XA resource to the association list.
     */
    protected void add(XAResource xaResource, XAResourceCallback callback) {
        InternalXAResourceHolder xaResourceHolder;
        if (xaResource == null) throw new IllegalArgumentException("Argument xaResource is null");
        if (_xaResourceHolder == null) _xaResourceHolder = new InternalXAResourceHolder(xaResource, callback); else {
            xaResourceHolder = _xaResourceHolder;
            do {
                if (xaResource == xaResourceHolder._xaResource) {
                    return;
                }
                xaResourceHolder = xaResourceHolder._next;
            } while (null != xaResourceHolder);
            xaResourceHolder = new InternalXAResourceHolder(xaResource, callback);
            xaResourceHolder._next = _xaResourceHolder;
            _xaResourceHolder = xaResourceHolder;
        }
    }

    /**
     * Removes an XA resource from the associated list.
     *
     * @param xaResource the XA resource
     * @return True if removed
     */
    protected boolean remove(XAResource xaResource) {
        InternalXAResourceHolder xaResourceHolder;
        InternalXAResourceHolder previousXAResourceHolder;
        if (_xaResourceHolder != null) {
            xaResourceHolder = _xaResourceHolder;
            previousXAResourceHolder = null;
            do {
                if (xaResource == xaResourceHolder._xaResource) {
                    if (null == previousXAResourceHolder) {
                        _xaResourceHolder = xaResourceHolder._next;
                    } else {
                        previousXAResourceHolder._next = xaResourceHolder._next;
                    }
                    return true;
                }
                previousXAResourceHolder = xaResourceHolder;
                xaResourceHolder = xaResourceHolder._next;
            } while (null != xaResourceHolder);
        }
        return false;
    }

    /**
     * Returns all the XA resources, or null if no resources
     * are enlisted.
     *
     * @return All XA resources, or null
     */
    protected XAResourceHolder[] getXAResourceHolders() {
        int count = 0;
        XAResourceHolder[] xaResourceHolders;
        InternalXAResourceHolder xaResourceHolder;
        if (_xaResourceHolder == null) return null;
        xaResourceHolder = _xaResourceHolder;
        do {
            ++count;
            xaResourceHolder = xaResourceHolder._next;
        } while (null != xaResourceHolder);
        xaResourceHolders = new XAResourceHolder[count];
        count = 0;
        xaResourceHolder = _xaResourceHolder;
        do {
            xaResourceHolders[count++] = xaResourceHolder;
            xaResourceHolder = xaResourceHolder._next;
        } while (null != xaResourceHolder);
        return xaResourceHolders;
    }

    /**
     * Each entry in the table has a key (thread), a value or null
     * (we don't remove on null) and a reference to the next entry in
     * the same table position.
     */
    private static final class ThreadEntry {

        /**
         * The thread with which this entry is associated.
         */
        final Thread _thread;

        /**
         * The current thread context.
         */
        ThreadContext _context;

        /**
         * The previous thread entry (single-linked stack)
         * associated with this thead.
         */
        ThreadEntry _previous;

        /**
         * The next thread entry in this bucket.
         */
        ThreadEntry _nextEntry;

        ThreadEntry(ThreadContext context, Thread thread, ThreadEntry previous) {
            _context = context;
            _thread = thread;
            if (previous != null) {
                _previous = previous;
                _nextEntry = previous._nextEntry;
            }
        }
    }

    /**
     * Describes an {@link XAResource} enlisted with this transaction
     * manager.
     */
    private static class InternalXAResourceHolder extends XAResourceHolder {

        /**
         * The next XA resource holder
         */
        private InternalXAResourceHolder _next;

        InternalXAResourceHolder(XAResource xaResource, XAResourceCallback callback) {
            super(xaResource, callback);
        }
    }
}
