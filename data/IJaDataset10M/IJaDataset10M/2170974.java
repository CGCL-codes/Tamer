package com.common.transaction;

import javax.ejb.EJBContext;
import com.common.persistence.domainstore.IPersistenceManager;

public class J2EECMTPersistenceManagerTransactionContext implements TransactionContext {

    private EJBContext sessionContext;

    private IPersistenceManager persistenceManager;

    protected J2EECMTPersistenceManagerTransactionContext() {
    }

    public J2EECMTPersistenceManagerTransactionContext(EJBContext sessionContext, IPersistenceManager persistenceManager) {
        if (sessionContext == null || persistenceManager == null) throw new IllegalArgumentException("Null context no permitido");
        this.persistenceManager = persistenceManager;
        this.sessionContext = sessionContext;
    }

    public void begin() throws TransactionException {
    }

    public void closeAll() throws TransactionException {
    }

    public void commitAll() throws TransactionException {
        persistenceManager.flush();
    }

    public void rollbackAll() throws TransactionException {
        try {
            sessionContext.setRollbackOnly();
        } catch (Throwable t) {
            throw new TransactionException("No se puedo hacer rollback", t);
        }
    }
}
