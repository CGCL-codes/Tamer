package org.t2framework.commons.transaction;

import static org.t2framework.commons.transaction.Messages.*;
import static org.t2framework.commons.transaction.util.LoggerUtil.*;
import static org.t2framework.commons.transaction.util.TransactionUtil.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.RollbackException;
import javax.transaction.Status;
import javax.transaction.Synchronization;
import javax.transaction.SystemException;
import javax.transaction.Transaction;
import javax.transaction.xa.XAException;
import javax.transaction.xa.XAResource;
import javax.transaction.xa.Xid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.t2framework.commons.transaction.exception.CommonsRollbackException;
import org.t2framework.commons.transaction.exception.CommonsSystemException;
import org.t2framework.commons.transaction.exception.TransactionalIllegalStateException;
import org.t2framework.commons.transaction.xa.XidImpl;

/**
 * 
 * {@.en The implementation of {@link Transaction}, which is specified at JTA
 * Specification 3.3.}
 * 
 * <br />
 * 
 * {@.ja JTA仕様3.3に規定されているTransactionの実装クラスです.}
 * 
 * @author shot
 * @see javax.transaction.Transaction
 * @since 0.5.2
 * 
 *        TODO : Add javadoc add logging.
 */
public class TransactionImpl implements Transaction, Suspendable, Resumable, InterposedSynchronization {

    protected static Logger logger = LoggerFactory.getLogger(TransactionImpl.class);

    protected int status = Status.STATUS_NO_TRANSACTION;

    protected final Xid xid;

    protected AtomicInteger branchId = new AtomicInteger(0);

    protected List<XAResourceCoordinator> resourceCoordinators = new CopyOnWriteArrayList<XAResourceCoordinator>();

    protected List<Synchronization> synchronizations = new CopyOnWriteArrayList<Synchronization>();

    protected ReentrantLock lock = new ReentrantLock();

    protected SuspendResumeStatus suspendStatus = SuspendResumeStatus.NONE;

    protected List<Synchronization> interposedSynchronizations = new CopyOnWriteArrayList<Synchronization>();

    protected Map<Object, Object> resources = new HashMap<Object, Object>();

    public TransactionImpl() {
        this.xid = new XidImpl();
        setActiveStatus();
        debug(logger, TransactionMarkers.LIFECYCLE, DTransaction0001, this.xid);
    }

    @Override
    public boolean enlistResource(XAResource xaresource) throws RollbackException, IllegalStateException, SystemException {
        assertXAResourceNotNull(xaresource);
        assertNotSuspended();
        assertActiveOrPreparing();
        debug(logger, TransactionMarkers.LIFECYCLE, DTransaction0002, xaresource, xid);
        Xid branch = null;
        for (int i = resourceCoordinators.size() - 1; i >= 0; i--) {
            final XAResourceCoordinator coordinator = resourceCoordinators.get(i);
            final XAResource xar = coordinator.getXAResource();
            if (xaresource != xar) {
                try {
                    if (xaresource.isSameRM(xar)) {
                        branch = coordinator.getXid();
                        break;
                    }
                } catch (XAException e) {
                    logger.error(e.getMessage());
                    throw new IllegalStateException(e);
                }
            } else {
                return false;
            }
        }
        final boolean commitable = (branch == null);
        final int flags;
        if (isVendorDependent(xaresource)) {
            flags = XAResource.TMNOFLAGS;
        } else {
            flags = (commitable) ? XAResource.TMNOFLAGS : XAResource.TMJOIN;
        }
        if (branch == null) {
            branch = createBranch();
        }
        try {
            xaresource.start(branch, flags);
            addResourceCoordinator(xaresource, branch, commitable);
        } catch (XAException e) {
            throw new IllegalStateException(e);
        }
        debug(logger, TransactionMarkers.LIFECYCLE, DTransaction0003, xaresource, xid);
        return true;
    }

    protected boolean isVendorDependent(final XAResource xaresource) {
        final boolean oracled = xaresource.getClass().getName().startsWith("oracle");
        return oracled;
    }

    protected void addResourceCoordinator(XAResource xaResource, Xid xid, boolean commitable) {
        resourceCoordinators.add(new XAResourceCoordinator(xaResource, xid, commitable));
    }

    @Override
    public boolean delistResource(XAResource xaresource, int status) throws IllegalStateException, SystemException {
        assertXAResourceNotNull(xaresource);
        assertNotSuspended();
        if (status != XAResource.TMSUCCESS && status != XAResource.TMFAIL && status != XAResource.TMSUSPEND) {
            throw new TransactionalIllegalStateException("ETransaction0012");
        }
        debug(logger, TransactionMarkers.LIFECYCLE, DTransaction0004, xaresource, status, xid);
        for (int i = resourceCoordinators.size() - 1; i >= 0; i--) {
            final XAResourceCoordinator coordinator = resourceCoordinators.get(i);
            final XAResource xar = coordinator.getXAResource();
            if (xaresource.equals(xar)) {
                try {
                    coordinator.end(status);
                    debug(logger, TransactionMarkers.LIFECYCLE, DTransaction0005, xaresource, status, xid);
                    return true;
                } catch (XAException e) {
                    logger.error(e.getMessage());
                    setMarkedRollbackStatus();
                    return false;
                }
            }
        }
        throw new IllegalStateException();
    }

    public Transaction suspend() throws SystemException {
        assertNotSuspended();
        assertActiveOrPreparing();
        for (int i = resourceCoordinators.size() - 1; i >= 0; i--) {
            final XAResourceCoordinator coordinator = resourceCoordinators.get(i);
            try {
                coordinator.end(XAResource.TMSUSPEND);
            } catch (XAException e) {
                throw new SystemException(e.getMessage());
            }
        }
        suspendStatus = SuspendResumeStatus.SUSPENDED;
        return this;
    }

    @Override
    public boolean isSuspended() throws SystemException {
        return suspendStatus == SuspendResumeStatus.SUSPENDED;
    }

    @Override
    public boolean isResumed() {
        return suspendStatus == SuspendResumeStatus.RESUMED;
    }

    @Override
    public void resume(final Transaction transaction) throws SystemException {
        assertSuspended();
        for (int i = resourceCoordinators.size() - 1; i >= 0; i--) {
            final XAResourceCoordinator coordinator = resourceCoordinators.get(i);
            try {
                coordinator.start(XAResource.TMRESUME);
            } catch (XAException e) {
                throw new SystemException(e.getMessage());
            }
        }
        suspendStatus = SuspendResumeStatus.RESUMED;
    }

    protected void assertSuspended() throws SystemException {
        if (isSuspended() == false) {
            throw new SystemException();
        }
    }

    protected void assertNotSuspended() throws SystemException {
        if (isSuspended()) {
            throw new CommonsSystemException("ETransaction0019");
        }
    }

    protected Xid createBranch() {
        return new XidImpl(xid, branchId.incrementAndGet());
    }

    /**
	 * Complete the transaction.After completion, the transaction clears its
	 * status, synchronizations, and interposed synchronizations and attached
	 * resources.
	 * 
	 * @throws HeuristicMixedException
	 * @throws HeuristicRollbackException
	 * @throws RollbackException
	 * @throws SecurityException
	 * @throws SystemException
	 */
    @Override
    public void commit() throws HeuristicMixedException, HeuristicRollbackException, RollbackException, SecurityException, SystemException {
        try {
            assertNotSuspended();
            assertActive();
            debug(logger, TransactionMarkers.LIFECYCLE, DTransaction0006, this.xid);
            lock.lock();
            try {
                beforeCompletion();
                boolean twoPhase = false;
                if (status == Status.STATUS_ACTIVE) {
                    endXAResources(XAResource.TMSUCCESS);
                    if (isZeroPhaseCommit()) {
                        setCommittedStatus();
                    } else if (isOnePhaseCommit()) {
                        commitOnePhase();
                    } else {
                        twoPhase = true;
                        commitTwoPhase();
                    }
                }
                boolean rollbacked = false;
                if (status != Status.STATUS_COMMITTED) {
                    rollbackResources(twoPhase);
                    rollbacked = true;
                }
                afterCompletion();
                if (rollbacked) {
                    throw new CommonsRollbackException(this, getStatusNoException(this));
                }
            } finally {
                lock.unlock();
                debug(logger, TransactionMarkers.LIFECYCLE, DTransaction0007, this.xid);
            }
        } finally {
            forget();
        }
    }

    /**
	 * Roll back the transaction.After rolling back complets, the transaction
	 * clears its status, synchronizations, and interposed synchronizations and
	 * attached resources.
	 */
    @Override
    public void rollback() throws IllegalStateException, SystemException {
        try {
            assertNotSuspended();
            assertActiveOrMarkedRollback();
            debug(logger, TransactionMarkers.LIFECYCLE, DTransaction0008, this.xid);
            lock.lock();
            try {
                endXAResources(XAResource.TMFAIL);
                rollbackResources(false);
                afterCompletion();
            } finally {
                lock.unlock();
                debug(logger, TransactionMarkers.LIFECYCLE, DTransaction0009, this.xid);
            }
        } finally {
            forget();
        }
    }

    private void assertActiveOrMarkedRollback() throws IllegalStateException {
        switch(status) {
            case Status.STATUS_ACTIVE:
            case Status.STATUS_MARKED_ROLLBACK:
                break;
            default:
                throwIllegalStateException();
        }
    }

    protected void throwIllegalStateException() throws IllegalStateException {
        switch(status) {
            case Status.STATUS_PREPARING:
                throw new TransactionalIllegalStateException(ETransaction0002);
            case Status.STATUS_PREPARED:
                throw new TransactionalIllegalStateException(ETransaction0003);
            case Status.STATUS_COMMITTING:
                throw new TransactionalIllegalStateException(ETransaction0004);
            case Status.STATUS_COMMITTED:
                throw new TransactionalIllegalStateException(ETransaction0005);
            case Status.STATUS_MARKED_ROLLBACK:
                throw new TransactionalIllegalStateException(ETransaction0006);
            case Status.STATUS_ROLLING_BACK:
                throw new TransactionalIllegalStateException(ETransaction0007);
            case Status.STATUS_ROLLEDBACK:
                throw new TransactionalIllegalStateException(ETransaction0008);
            case Status.STATUS_NO_TRANSACTION:
                throw new TransactionalIllegalStateException(ETransaction0009);
            case Status.STATUS_UNKNOWN:
                throw new TransactionalIllegalStateException(ETransaction0010);
            default:
                throw new TransactionalIllegalStateException(ETransaction0011);
        }
    }

    protected void forget() {
        setNoTransactionStatus();
        resourceCoordinators.clear();
        synchronizations.clear();
        suspendStatus = SuspendResumeStatus.NONE;
        interposedSynchronizations.clear();
        resources.clear();
    }

    protected void afterCompletion() {
        for (Synchronization sync : interposedSynchronizations) {
            afterCompletion(sync);
        }
        for (Synchronization sync : synchronizations) {
            afterCompletion(sync);
        }
        setNoTransactionStatus();
    }

    protected void afterCompletion(Synchronization sync) {
        try {
            sync.afterCompletion(status);
        } catch (Throwable t) {
            logger.error(t.getMessage());
        }
    }

    /**
	 * Commit one phase.
	 */
    protected void commitOnePhase() {
        setCommittingStatus();
        final XAResourceCoordinator coordinator = resourceCoordinators.get(0);
        try {
            coordinator.commit(true);
        } catch (XAException e) {
            logger.error(e.getMessage());
            setUnknownStatus();
        }
        setCommittingToCommittedStatus();
    }

    /**
	 * Trying to commit two phase with resource managers.Resource managers votes
	 * if they can be committed or not.If all of resource managers votes as
	 * {@link Vote#COMMIT}, the transaction finally commits.If vote results
	 * shows {@link Vote#ROLLBACK} or {@link Vote#UNKNOWN}, it rolls back
	 * immediately.
	 */
    protected void commitTwoPhase() {
        final Vote voteResult = prepareForTwoPhase();
        if (voteResult == Vote.READONLY) {
            setCommittedStatus();
        } else if (voteResult == Vote.COMMIT) {
            commitTwoPhaseReally();
        } else if (voteResult == Vote.ROLLBACK) {
            setMarkedRollbackStatus();
        } else if (voteResult == Vote.UNKNOWN) {
            setMarkedRollbackStatus();
        }
    }

    /**
	 * Commit two phase, finally.
	 */
    protected void commitTwoPhaseReally() {
        setCommittingStatus();
        for (XAResourceCoordinator coordinator : resourceCoordinators) {
            if (coordinator.isCommitable() && coordinator.isVoteOk()) {
                try {
                    coordinator.commit(false);
                } catch (XAException e) {
                    logger.error(e.getMessage());
                    setUnknownStatus();
                }
            }
        }
        setCommittingToCommittedStatus();
    }

    protected Vote prepareForTwoPhase() {
        setPreparingStatus();
        Vote vote = Vote.READONLY;
        final List<XAResourceCoordinator> list = new ArrayList<XAResourceCoordinator>();
        for (XAResourceCoordinator coordinator : resourceCoordinators) {
            if (coordinator.isCommitable()) {
                list.add(coordinator);
            }
        }
        int size = list.size();
        for (int i = 0; i < size; i++) {
            final XAResourceCoordinator coodinator = list.get(i);
            try {
                if (executeLastResourceCommitOptimization(coodinator, i, list)) {
                    vote = Vote.COMMIT;
                    coodinator.markVoteNg();
                } else {
                    final int xastatus = coodinator.prepare();
                    if (xastatus == XAResource.XA_OK) {
                        vote = Vote.COMMIT;
                        coodinator.markVoteOk();
                    } else {
                        vote = Vote.UNKNOWN;
                        coodinator.markVoteNg();
                    }
                }
            } catch (XAException e) {
                logger.error(e.getMessage());
                coodinator.markVoteNg();
                setMarkedRollbackStatus();
                return Vote.ROLLBACK;
            }
        }
        setPreparingToPreparedStatus();
        return vote;
    }

    /**
	 * Trying to commit last resource as last resource commit optimization.
	 * 
	 * @param coordinator
	 * @param current
	 * @param list
	 * @return
	 * @throws XAException
	 */
    protected boolean executeLastResourceCommitOptimization(XAResourceCoordinator coordinator, int current, List<XAResourceCoordinator> list) throws XAException {
        if (current == list.size() - 1) {
            coordinator.commit(true);
            return true;
        } else {
            return false;
        }
    }

    protected boolean isZeroPhaseCommit() {
        return resourceCoordinators.size() == 0;
    }

    protected boolean isOnePhaseCommit() {
        return resourceCoordinators.size() == 1;
    }

    protected void rollbackResources(final boolean twoPhase) {
        setRollingbackStatus();
        for (XAResourceCoordinator coordinator : resourceCoordinators) {
            if (twoPhase && coordinator.isVoteOk() == false) {
                continue;
            }
            try {
                coordinator.rollback();
            } catch (XAException e) {
                logger.error(e.getMessage());
                setUnknownStatus();
            }
        }
        setRollingBackToRolledBackStatus();
    }

    protected void beforeCompletion() {
        for (Synchronization sync : synchronizations) {
            beforeCompletion(sync);
        }
        for (Synchronization sync : interposedSynchronizations) {
            beforeCompletion(sync);
        }
    }

    protected void beforeCompletion(Synchronization sync) {
        try {
            sync.beforeCompletion();
        } catch (Throwable t) {
            logger.error(t.getMessage());
            setMarkedRollbackStatus();
            endXAResources(XAResource.TMFAIL);
        }
    }

    protected void endXAResources(int status) {
        for (XAResourceCoordinator coodinator : resourceCoordinators) {
            try {
                coodinator.end(status);
            } catch (XAException e) {
                logger.error(e.getMessage());
                setMarkedRollbackStatus();
            }
        }
    }

    /**
	 * Get current status.The status is specified by {@link Status}.
	 * 
	 * @see javax.transaction.Status
	 */
    @Override
    public int getStatus() throws SystemException {
        return status;
    }

    /**
	 * Register {@link Synchronization}.The transaction must be active.
	 * 
	 * @throws RollbackException
	 * @throws IllegalStateException
	 *             throw if given synchronization is null.
	 * @throws SystemException
	 */
    @Override
    public void registerSynchronization(Synchronization sync) throws RollbackException, IllegalStateException, SystemException {
        assertActive();
        if (sync == null) {
            throw new TransactionalIllegalStateException(Messages.ETransaction0020);
        }
        synchronizations.add(sync);
    }

    /**
	 * Set the transaction always rolls back.
	 */
    @Override
    public void setRollbackOnly() throws IllegalStateException, SystemException {
        assertActiveOrPreparingOrPrepared();
        this.status = Status.STATUS_MARKED_ROLLBACK;
    }

    protected void setActiveStatus() {
        this.status = Status.STATUS_ACTIVE;
    }

    protected void setRollingbackStatus() {
        this.status = Status.STATUS_ROLLING_BACK;
    }

    protected void setPreparingStatus() {
        this.status = Status.STATUS_PREPARING;
    }

    protected void setMarkedRollbackStatus() {
        this.status = Status.STATUS_MARKED_ROLLBACK;
    }

    protected void setCommittingStatus() {
        this.status = Status.STATUS_COMMITTING;
    }

    protected void setCommittedStatus() {
        this.status = Status.STATUS_COMMITTED;
    }

    protected void setUnknownStatus() {
        this.status = Status.STATUS_UNKNOWN;
    }

    protected void setNoTransactionStatus() {
        this.status = Status.STATUS_NO_TRANSACTION;
    }

    protected void setCommittingToCommittedStatus() {
        if (this.status == Status.STATUS_COMMITTING) {
            this.status = Status.STATUS_COMMITTED;
        }
    }

    protected void setRollingBackToRolledBackStatus() {
        if (this.status == Status.STATUS_ROLLING_BACK) {
            this.status = Status.STATUS_ROLLEDBACK;
        }
    }

    protected void setPreparingToPreparedStatus() {
        if (this.status == Status.STATUS_PREPARING) {
            this.status = Status.STATUS_PREPARED;
        }
    }

    protected void assertXAResourceNotNull(XAResource xaresource) throws SystemException {
        if (xaresource == null) {
            throw new CommonsSystemException("ETransaction0018");
        }
    }

    protected void assertActive() {
        switch(status) {
            case Status.STATUS_ACTIVE:
                break;
            default:
                throwIllegalStateException();
        }
    }

    protected void assertActiveOrPreparing() {
        switch(status) {
            case Status.STATUS_ACTIVE:
            case Status.STATUS_PREPARING:
                break;
            default:
                throwIllegalStateException();
        }
    }

    protected void assertActiveOrPreparingOrPrepared() throws IllegalStateException {
        switch(status) {
            case Status.STATUS_ACTIVE:
            case Status.STATUS_PREPARING:
            case Status.STATUS_PREPARED:
                break;
            default:
                throwIllegalStateException();
        }
    }

    @Override
    public void addInterposedSynchronization(Synchronization sync) {
        interposedSynchronizations.add(sync);
    }

    /**
	 * Get interposed resource by key.
	 */
    @Override
    public Object getResource(Object key) {
        return resources.get(key);
    }

    /**
	 * Put resource for interposed synchronization.
	 */
    @Override
    public void putResource(Object key, Object value) {
        resources.put(key, value);
    }

    /**
	 * Get list of interposed {@link Synchronization}.
	 */
    @Override
    public List<Synchronization> getInterposedSynchronization() {
        return this.interposedSynchronizations;
    }
}
