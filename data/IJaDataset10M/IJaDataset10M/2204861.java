package com.gorillalogic.dal.common;

import com.gorillalogic.dal.*;
import com.gorillalogic.dal.common.table.store.TxnLayer;
import com.gorillalogic.accounts.GXESession;

/**
 * Package-only access to Txn.
 *
 * @author bpm
 * @version 1.0
 */
public interface CommonTxn extends Txn {

    TxnLayer topmostLayer();

    boolean isCurrent();

    void suspend();

    void resume();

    boolean isSuspended();

    CommonTable pendingUpdateCommonTable();

    CommonTable nestingCommonTable();

    public static final CommonTxn nullTxn = new NullTxn();

    class NullTxn implements CommonTxn {

        private void fault() {
            throw new InternalException("Null Transaction");
        }

        public long txnId() {
            return -1;
        }

        public GXESession getSession() {
            return null;
        }

        public void suspend() {
            fault();
        }

        public void resume() {
            fault();
        }

        public boolean isSuspended() {
            return false;
        }

        public String getPrompt() {
            return "";
        }

        public void markPrompt(char c) {
            fault();
        }

        public void unmarkPrompt() {
            fault();
        }

        public void begin() {
            fault();
        }

        public void ensureTxnBegun() {
            fault();
        }

        public boolean inProgress() {
            return false;
        }

        public boolean pendingUpdates() {
            return false;
        }

        public int nesting() {
            return 0;
        }

        public void commit() {
            fault();
        }

        public void commit(Scope scope) {
            fault();
        }

        public void commit(boolean force) {
            fault();
        }

        public void commit(Scope scope, boolean force) {
            fault();
        }

        public void abort() {
            fault();
        }

        public void abort(boolean force) {
            fault();
        }

        public boolean abortIfEmpty() {
            return false;
        }

        public synchronized void abortAll(boolean includingCurrentSession) throws AccessException {
            CommonSession.abortAll(includingCurrentSession);
        }

        public void readyToContinue() {
        }

        public void synch() {
        }

        public void synch(Scope scope) {
        }

        public void trigger() {
        }

        public void trigger(Scope scope) {
        }

        public void check() {
        }

        public void check(Scope scope) {
        }

        public void spawn() {
        }

        public void spawn(Scope scope) {
        }

        public Table getPendingSynch(boolean deep) {
            return null;
        }

        public Table getPendingTrigger(boolean deep) {
            return null;
        }

        public Table getPendingCheck(boolean deep) {
            return null;
        }

        public Table getDefinedSynch(Scope scope, boolean deep) {
            return null;
        }

        public Table getDefinedTrigger(Scope scope, boolean deep) {
            return null;
        }

        public Table getDefinedCheck(Scope scope, boolean deep) {
            return null;
        }

        public void setTrace(boolean which) {
        }

        public PkgTable executionState() {
            return null;
        }

        public TxnLayer topmostLayer() {
            return TxnLayer.factory.getNullTxnLayer();
        }

        public boolean isCurrent() {
            return false;
        }

        public Table pendingUpdateTable() {
            return null;
        }

        public Table nestingTable() {
            return null;
        }

        public CommonTable pendingUpdateCommonTable() {
            return null;
        }

        public CommonTable nestingCommonTable() {
            return null;
        }

        public void exec(FreeOperation fop) throws AccessException {
        }
    }

    public interface FreeOperation {

        void exec() throws AccessException;
    }

    public void exec(FreeOperation fop) throws AccessException;
}
