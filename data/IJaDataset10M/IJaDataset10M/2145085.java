package com.sun.jini.outrigger;

import java.util.Set;
import java.util.WeakHashMap;
import net.jini.core.transaction.TransactionException;
import net.jini.space.InternalSpaceException;

/**
 * Subclass of <code>QueryWatcher</code> for <code>takeIfExists</code> queries.
 * Resolves with the first matching transition where the entry is visible to the
 * associated transaction and the entry is still available, or of the locked
 * entry set goes empty.
 */
class TakeIfExistsWatcher extends SingletonQueryWatcher implements IfExistsWatcher, Transactable {

    /**
	 * The set of entries that would match but are currently unavailable (e.g.
	 * they are locked). We only keep the ids, not the entries themselves.
	 */
    private final Set lockedEntries;

    /**
	 * Set <code>true</code> once the query thread is done processing the
	 * backlog. Once this is <code>true</code> it is ok to resolve if
	 * <code>lockedEntries</code> is empty.
	 */
    private boolean backlogFinished = false;

    /**
	 * If non-null the transaction this query is being performed under. If
	 * <code>null</code> this query is not associated with a transaction.
	 */
    private final Txn txn;

    /**
	 * Set of entries (represented by <code>EntryHolder</code>s) that we would
	 * have liked to return, but have been provisionally removed.
	 */
    private final WeakHashMap provisionallyRemovedEntrySet;

    /**
	 * Create a new <code>TakeIfExistsWatcher</code>.
	 * 
	 * @param expiration
	 *            the initial expiration time for this
	 *            <code>TransitionWatcher</code> in milliseconds since the
	 *            beginning of the epoch.
	 * @param timestamp
	 *            the value that is used to sort <code>TransitionWatcher</code>
	 *            s.
	 * @param startOrdinal
	 *            the highest ordinal associated with operations that are
	 *            considered to have occurred before the operation associated
	 *            with this watcher.
	 * @param lockedEntries
	 *            Set of entries (by their IDs) that match but are unavailable.
	 *            Must be non-empty. Keeps a reference to this object.
	 * @param provisionallyRemovedEntrySet
	 *            If the watcher encounters an entry that can not be read/taken
	 *            because it has been provisionally removed then its handle will
	 *            be placed in this <code>WeakHashMap</code> as a key (with null
	 *            as the value). May be <code>null</code> in which case
	 *            provisionally removed entries will not be recorded. Ensures
	 *            that object is only accessed by one thread at a time
	 * @param txn
	 *            If the query is being performed under a transaction the
	 *            <code>Txn</code> object associated with that transaction.
	 * @throws NullPointerException
	 *             if <code>lockedEntries</code> is <code>null</code>.
	 */
    TakeIfExistsWatcher(long expiration, long timestamp, long startOrdinal, Set lockedEntries, WeakHashMap provisionallyRemovedEntrySet, Txn txn) {
        super(expiration, timestamp, startOrdinal);
        if (lockedEntries == null) throw new NullPointerException("lockedEntries must be non-null");
        this.lockedEntries = lockedEntries;
        this.txn = txn;
        this.provisionallyRemovedEntrySet = provisionallyRemovedEntrySet;
    }

    boolean isInterested(EntryTransition transition, long ordinal) {
        return (ordinal > startOrdinal) && !isResolved();
    }

    synchronized void process(EntryTransition transition, long now) {
        if (isResolved()) return;
        final EntryHandle handle = transition.getHandle();
        final EntryRep rep = handle.rep();
        final boolean isAvailable = transition.isAvailable();
        final TransactableMgr transitionTxn = transition.getTxn();
        if (isAvailable && ((null == transitionTxn) || txn == transitionTxn)) {
            if (getServer().attemptCapture(handle, txn, true, null, provisionallyRemovedEntrySet, now, this)) {
                resolve(handle, null);
            } else {
                lockedEntries.add(rep.id());
            }
        } else if (isAvailable) {
            lockedEntries.add(rep.id());
        } else {
            lockedEntries.remove(rep.id());
            if (backlogFinished && lockedEntries.isEmpty()) resolve(null, null);
        }
    }

    synchronized boolean catchUp(EntryTransition transition, long now) {
        if (isResolved()) return true;
        final EntryHandle handle = transition.getHandle();
        final EntryRep rep = handle.rep();
        final boolean isAvailable = transition.isAvailable();
        final TransactableMgr transitionTxn = transition.getTxn();
        if (isAvailable && ((null == transitionTxn) || txn == transitionTxn)) {
            if (getServer().attemptCapture(handle, txn, true, lockedEntries, provisionallyRemovedEntrySet, now, this)) {
                resolve(handle, null);
                return true;
            }
            return false;
        }
        if (isAvailable) {
            synchronized (handle) {
                if (!handle.removed()) {
                    lockedEntries.add(rep.id());
                }
            }
            return false;
        }
        lockedEntries.remove(rep.id());
        return false;
    }

    /**
	 * Once the backlog is complete we can resolve if lockedEntries is/becomes
	 * empty.
	 */
    public synchronized void caughtUp() {
        backlogFinished = true;
        if (isResolved()) return;
        if (lockedEntries.isEmpty()) resolve(null, null);
    }

    public synchronized boolean isLockedEntrySetEmpty() {
        if (!isResolved()) throw new IllegalStateException("Query not yet resolved");
        return lockedEntries.isEmpty();
    }

    /**
	 * If a transaction ends in the middle of a query we want to throw an
	 * exception to the client making the query not the <code>Txn</code> calling
	 * us here.)
	 */
    public synchronized int prepare(TransactableMgr mgr, OutriggerServerImpl space) {
        assert txn != null : "Transactable method called on a " + "non-transactional TakeIfExistsWatcher";
        if (!isResolved()) {
            resolve(null, new TransactionException("completed while " + "operation in progress"));
        }
        return NOTCHANGED;
    }

    /**
	 * This should never happen since we always return <code>NOTCHANGED</code>
	 * from <code>prepare</code>.
	 */
    public void commit(TransactableMgr mgr, OutriggerServerImpl space) {
        throw new InternalSpaceException("committing a blocking query");
    }

    /**
	 * If a transaction ends in the middle of a query we want to throw an
	 * exception to the client making the query (not the <code>Txn</code>
	 * calling us here.)
	 */
    public void abort(TransactableMgr mgr, OutriggerServerImpl space) {
        prepare(mgr, space);
    }
}
