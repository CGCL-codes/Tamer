package com.sleepycat.db;

import com.sleepycat.db.internal.DbTxn;

public class PreparedTransaction {

    private byte[] gid;

    private Transaction txn;

    PreparedTransaction(final DbTxn txn, final byte[] gid) {
        this.txn = new Transaction(txn);
        this.gid = gid;
    }

    public byte[] getGID() {
        return gid;
    }

    public Transaction getTransaction() {
        return txn;
    }
}
