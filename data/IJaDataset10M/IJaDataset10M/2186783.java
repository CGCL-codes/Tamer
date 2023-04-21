package com.amazon.carbonado.repo.dirmi;

import java.rmi.NoSuchObjectException;
import java.rmi.RemoteException;
import java.util.concurrent.TimeUnit;
import com.amazon.carbonado.IsolationLevel;
import com.amazon.carbonado.PersistException;
import com.amazon.carbonado.Repository;
import com.amazon.carbonado.Transaction;
import com.amazon.carbonado.txn.TransactionManager;
import com.amazon.carbonado.txn.TransactionMonitor;

/**
 * 
 *
 * @author Brian S O'Neill
 */
class ClientTransactionManager extends TransactionManager<RemoteTransaction> {

    private final ClientRepository mRepository;

    ClientTransactionManager(ClientRepository repo, TransactionMonitor monitor) {
        super(monitor);
        if (repo == null) {
            throw new IllegalArgumentException();
        }
        mRepository = repo;
    }

    @Override
    protected IsolationLevel selectIsolationLevel(Transaction parent, IsolationLevel level) {
        if (level == null) {
            if (parent == null) {
                level = IsolationLevel.READ_COMMITTED;
            } else {
                level = parent.getIsolationLevel();
            }
        }
        return level;
    }

    @Override
    protected boolean supportsForUpdate() {
        return true;
    }

    @Override
    protected RemoteTransaction createTxn(RemoteTransaction parent, IsolationLevel level) {
        if (parent == null) {
            return mRepository.getRemoteRepository().enterTopTransaction(level);
        } else {
            return mRepository.getRemoteRepository().enterTransaction(parent, level);
        }
    }

    @Override
    protected RemoteTransaction createTxn(RemoteTransaction parent, IsolationLevel level, int timeout, TimeUnit unit) {
        if (parent == null) {
            return mRepository.getRemoteRepository().enterTopTransaction(level, timeout, unit);
        } else {
            return mRepository.getRemoteRepository().enterTransaction(parent, level, timeout, unit);
        }
    }

    @Override
    protected void setForUpdate(RemoteTransaction txn, boolean forUpdate) {
        try {
            txn.setForUpdate(forUpdate);
        } catch (NoSuchObjectException e) {
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected boolean commitTxn(RemoteTransaction txn) throws PersistException {
        try {
            txn.commit();
            return true;
        } catch (PersistException e) {
            if (e.getCause() instanceof NoSuchObjectException) {
                return false;
            } else {
                throw e;
            }
        }
    }

    @Override
    protected void abortTxn(RemoteTransaction txn) throws PersistException {
        try {
            txn.exit();
        } catch (PersistException e) {
            if (e.getCause() instanceof NoSuchObjectException) {
            } else {
                throw e;
            }
        }
    }
}
