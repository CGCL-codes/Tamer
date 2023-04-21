package org.hibernate.test.cache.jbc2.functional.util;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.LinkedList;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.RollbackException;
import javax.transaction.Status;
import javax.transaction.Synchronization;
import javax.transaction.SystemException;
import javax.transaction.Transaction;
import javax.transaction.xa.XAResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * SimpleJtaTransactionImpl variant that works with DualNodeTransactionManagerImpl.
 *
 * @author Brian Stansberry
 */
public class DualNodeJtaTransactionImpl implements Transaction {

    private static final Logger log = LoggerFactory.getLogger(DualNodeJtaTransactionImpl.class);

    private int status;

    private LinkedList synchronizations;

    private Connection connection;

    private final DualNodeJtaTransactionManagerImpl jtaTransactionManager;

    public DualNodeJtaTransactionImpl(DualNodeJtaTransactionManagerImpl jtaTransactionManager) {
        this.jtaTransactionManager = jtaTransactionManager;
        this.status = Status.STATUS_ACTIVE;
    }

    public int getStatus() {
        return status;
    }

    public void commit() throws RollbackException, HeuristicMixedException, HeuristicRollbackException, IllegalStateException, SystemException {
        if (status == Status.STATUS_MARKED_ROLLBACK) {
            log.trace("on commit, status was marked for rollback-only");
            rollback();
        } else {
            status = Status.STATUS_PREPARING;
            for (int i = 0; i < synchronizations.size(); i++) {
                Synchronization s = (Synchronization) synchronizations.get(i);
                s.beforeCompletion();
            }
            status = Status.STATUS_COMMITTING;
            if (connection != null) {
                try {
                    connection.commit();
                    connection.close();
                } catch (SQLException sqle) {
                    status = Status.STATUS_UNKNOWN;
                    throw new SystemException();
                }
            }
            status = Status.STATUS_COMMITTED;
            for (int i = 0; i < synchronizations.size(); i++) {
                Synchronization s = (Synchronization) synchronizations.get(i);
                s.afterCompletion(status);
            }
            jtaTransactionManager.endCurrent(this);
        }
    }

    public void rollback() throws IllegalStateException, SystemException {
        status = Status.STATUS_ROLLEDBACK;
        if (connection != null) {
            try {
                connection.rollback();
                connection.close();
            } catch (SQLException sqle) {
                status = Status.STATUS_UNKNOWN;
                throw new SystemException();
            }
        }
        if (synchronizations != null) {
            for (int i = 0; i < synchronizations.size(); i++) {
                Synchronization s = (Synchronization) synchronizations.get(i);
                s.afterCompletion(status);
            }
        }
        jtaTransactionManager.endCurrent(this);
    }

    public void setRollbackOnly() throws IllegalStateException, SystemException {
        status = Status.STATUS_MARKED_ROLLBACK;
    }

    public void registerSynchronization(Synchronization synchronization) throws RollbackException, IllegalStateException, SystemException {
        if (synchronizations == null) {
            synchronizations = new LinkedList();
        }
        synchronizations.add(synchronization);
    }

    public void enlistConnection(Connection connection) {
        if (this.connection != null) {
            throw new IllegalStateException("Connection already registered");
        }
        this.connection = connection;
    }

    public Connection getEnlistedConnection() {
        return connection;
    }

    public boolean enlistResource(XAResource xaResource) throws RollbackException, IllegalStateException, SystemException {
        return false;
    }

    public boolean delistResource(XAResource xaResource, int i) throws IllegalStateException, SystemException {
        return false;
    }
}
