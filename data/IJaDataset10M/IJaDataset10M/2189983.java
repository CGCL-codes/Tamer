package org.nakedobjects.persistence.sql;

import org.apache.log4j.Logger;

public abstract class AbstractDatabaseConnector implements DatabaseConnector {

    private static final Logger LOG = Logger.getLogger(AbstractDatabaseConnector.class);

    private boolean isUsed;

    public final void setUsed(boolean isUsed) {
        this.isUsed = isUsed;
    }

    public final boolean isUsed() {
        return isUsed;
    }

    private int transactionLevel = 0;

    public final void startTransaction() {
        transactionLevel++;
    }

    public final void endTransaction() {
        transactionLevel--;
    }

    public final boolean isTransactionComplete() {
        return transactionLevel == 0;
    }

    private DatabaseConnectorPool pool;

    public final void setConnectionPool(DatabaseConnectorPool pool) {
        this.pool = pool;
    }

    public final DatabaseConnectorPool getConnectionPool() {
        return pool;
    }
}
