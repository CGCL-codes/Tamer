package org.go.database;

import java.sql.Connection;
import org.go.Trigger;
import org.go.Work;
import org.go.core.SchedulerSignaler;
import org.go.database.delegate.JobStoreSupport;
import org.go.domain.CompletedExecutionInstruction;
import org.go.domain.TriggerType;
import org.go.expcetion.JobPersistenceException;
import org.go.expcetion.SchedulerConfigException;
import org.go.spi.ClassLoadHelper;

/**
 * <p>
 * <code>JobStoreTX</code> is meant to be used in a standalone environment.
 * Both commit and rollback will be handled by this class.
 * </p>
 * 
 * <p>
 * If you need a <code>{@link org.quartz.spi.JobStore}</code> class to use
 * within an application-server environment, use <code>{@link
 * org.quartz.impl.jdbcjobstore.JobStoreCMT}</code>
 * instead.
 * </p>
 * 
 * @author <a href="mailto:jeff@binaryfeed.org">Jeffrey Wescott</a>
 * @author James House
 */
public class JobStoreTX extends JobStoreSupport {

    /**
	 * Execute the given callback having optionally aquired the given lock.
	 * For <code>JobStoreTX</code>, because it manages its own transactions
	 * and only has the one datasource, this is the same behavior as 
	 * executeInNonManagedTXLock().
	 * 
	 * @param lockName The name of the lock to aquire, for example 
	 * "TRIGGER_ACCESS".  If null, then no lock is aquired, but the
	 * lockCallback is still executed in a transaction.
	 * 
	 * @see JobStoreSupport#executeInNonManagedTXLock(String, TransactionCallback)
	 * @see JobStoreCMT#executeInLock(String, TransactionCallback)
	 * @see JobStoreSupport#getNonManagedTXConnection()
	 * @see JobStoreSupport#getConnection()
	 */
    @Override
    protected Object executeInLock(String lockName, TransactionCallback txCallback) throws JobPersistenceException {
        return executeInNonManagedTXLock(lockName, txCallback);
    }

    /**
	 * For <code>JobStoreTX</code>, the non-managed TX connection is just 
	 * the normal connection because it is not CMT.
	 * 
	 * @see JobStoreSupport#getConnection()
	 */
    @Override
    protected Connection getNonManagedTXConnection() throws JobPersistenceException {
        return getConnection();
    }

    @Override
    public void addWorkDesc(Work workerDesc) {
    }

    @Override
    public void initialize(ClassLoadHelper loadHelper, SchedulerSignaler signaler) throws SchedulerConfigException {
        super.initialize(loadHelper, signaler);
        getLog().info("JobStoreTX initialized.");
    }

    @Override
    public Work queryWorkDesc(String id) {
        return null;
    }

    @Override
    public Work[] queryWorkDesc(TriggerType type) {
        return null;
    }

    @Override
    public void triggeredJobComplete(Trigger trigger, Object jobDetail, CompletedExecutionInstruction setAllJobTriggersError) throws JobPersistenceException {
    }
}
