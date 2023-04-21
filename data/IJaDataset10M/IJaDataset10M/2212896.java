package de.ofahrt.mnemoj.impl;

import com.sun.sgs.app.DataManager;
import com.sun.sgs.app.TaskManager;

final class ManagerContext {

    private final Transaction transaction;

    public ManagerContext(Transaction transaction) {
        this.transaction = transaction;
    }

    public Transaction getTaskTransaction() {
        return transaction;
    }

    public DataManager getDataManager() {
        return transaction;
    }

    public TaskManager getTaskManager() {
        return transaction;
    }
}
