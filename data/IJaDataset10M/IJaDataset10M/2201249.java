package com.sun.jini.outrigger.logstore;

import com.odi.util.OSHashtable;
import com.sun.jini.outrigger.Recover;
import com.sun.jini.outrigger.StoredObject;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.Enumeration;
import net.jini.core.transaction.server.TransactionConstants;
import net.jini.space.InternalSpaceException;

/**
 * This object represents a pending transaction in a <code>BackEnd</code>. As
 * operations are performed under the transaction, they are logged into this
 * object. When the transaction is committed, each operation is committed into
 * the DB.
 * 
 * @author Sun Microsystems, Inc.
 * 
 */
class PendingTxn {

    /** A superclass for the objects that represent pending operations. */
    abstract static class PendingOp {

        /**
		 * Commit the operation by invoking the relevant method on the
		 * <code>processor</code> object.
		 */
        abstract void commit(BackEnd processor);
    }

    /** An object that represents a pending write. */
    static class WriteOp extends PendingOp {

        Resource entry;

        WriteOp(Resource entry) {
            this.entry = entry;
        }

        void commit(BackEnd processor) {
            processor.writeOp(entry, null);
        }
    }

    /** An object that represents a pending take. */
    static class TakeOp extends PendingOp {

        byte cookie[];

        TakeOp(byte cookie[]) {
            this.cookie = cookie;
        }

        void commit(BackEnd processor) {
            processor.takeOp(cookie, null);
        }
    }

    private long id;

    private int state;

    private OSHashtable ops;

    private StoredObject transaction;

    /**
	 * Create a new <code>PendingTxn</code> for the given <code>id</code>.
	 */
    PendingTxn(Long id) {
        this.id = id.longValue();
        state = TransactionConstants.ACTIVE;
        ops = new OSHashtable();
    }

    /**
	 * Add a new pending <code>write</code> operation.
	 */
    void addWrite(Resource entry) {
        ops.put(new ByteArrayWrapper(entry.getCookie()), new WriteOp(entry));
    }

    /**
	 * Add a new pending <code>take</code> operation.
	 */
    void addTake(byte cookie[]) {
        ByteArrayWrapper baw = new ByteArrayWrapper(cookie);
        if (ops.remove(baw) == null) ops.put(baw, new TakeOp(cookie));
    }

    /**
	 * Get a pending write resource.
	 */
    Resource get(ByteArrayWrapper cookie) {
        PendingOp po = (PendingOp) ops.get(cookie);
        if ((po != null) & (po instanceof WriteOp)) return ((WriteOp) po).entry;
        return null;
    }

    /**
	 * Remove a pending write.
	 */
    Resource remove(ByteArrayWrapper cookie) {
        Resource entry = get(cookie);
        if (entry != null) ops.remove(cookie);
        return entry;
    }

    /**
	 * Recover prepared transactions. This method returns true if this pending
	 * transaction was recovered.
	 */
    boolean recover(Recover space) throws Exception {
        if (state != TransactionConstants.PREPARED) return false;
        space.recoverTransaction(new Long(id), transaction);
        Enumeration e = ops.elements();
        while (e.hasMoreElements()) {
            PendingTxn.PendingOp op = (PendingTxn.PendingOp) e.nextElement();
            if (op instanceof PendingTxn.WriteOp) {
                space.recoverWrite(((PendingTxn.WriteOp) op).entry, new Long(id));
            } else if (op instanceof PendingTxn.TakeOp) {
                space.recoverTake(ByteArrayWrapper.toUuid(((PendingTxn.TakeOp) op).cookie), new Long(id));
            } else {
                throw new InternalSpaceException("unknown operation type: " + op.getClass().getName());
            }
        }
        return true;
    }

    /**
	 * Set the <code>Transaction</code> object.
	 */
    void prepare(StoredObject tr) {
        transaction = tr;
        state = TransactionConstants.PREPARED;
    }

    /**
	 * Commit all the operations by invoking the relevant method on the
	 * <code>processor</code> object.
	 */
    void commit(BackEnd processor) {
        Enumeration e = ops.elements();
        while (e.hasMoreElements()) ((PendingOp) e.nextElement()).commit(processor);
        state = TransactionConstants.COMMITTED;
    }

    public int hashCode() {
        return (int) id;
    }

    public boolean equals(Object o) {
        try {
            return ((PendingTxn) o).id == id;
        } catch (Exception e) {
        }
        return false;
    }
}
