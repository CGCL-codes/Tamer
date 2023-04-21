package org.quartz.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.core.JobRunShellFactory;
import org.quartz.core.QuartzScheduler;
import org.quartz.core.QuartzSchedulerResources;
import org.quartz.core.SchedulingContext;
import org.quartz.simpl.CascadingClassLoadHelper;
import org.quartz.simpl.RAMJobStore;
import org.quartz.simpl.SimpleThreadPool;
import org.quartz.spi.ClassLoadHelper;
import org.quartz.spi.JobStore;
import org.quartz.spi.SchedulerPlugin;
import org.quartz.spi.ThreadPool;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

/**
 * <p>
 * A singleton implementation of <code>{@link org.quartz.SchedulerFactory}</code>.
 * </p>
 * 
 * <p>
 * Here are some examples of using this class:
 * </p>
 * <p>
 * To create a scheduler that does not write anything to the database (is not
 * persistent), you can call <code>createVolatileScheduler</code>:
 * 
 * <pre>
 *  DirectSchedulerFactory.getInstance().createVolatileScheduler(10); // 10 threads * // don't forget to start the scheduler: DirectSchedulerFactory.getInstance().getScheduler().start();
 * </pre>
 * 
 * 
 * <p>
 * Several create methods are provided for convenience. All create methods
 * eventually end up calling the create method with all the parameters:
 * </p>
 * 
 * <pre>
 *  public void createScheduler(String schedulerName, String schedulerInstanceId, ThreadPool threadPool, JobStore jobStore, String rmiRegistryHost, int rmiRegistryPort)
 * </pre>
 * 
 * 
 * <p>
 * Here is an example of using this method:
 * </p>
 *  *
 *  * <pre>// create the thread pool SimpleThreadPool threadPool = new SimpleThreadPool(maxThreads, Thread.NORM_PRIORITY); threadPool.initialize(); * // create the job store JobStore jobStore = new RAMJobStore(); jobStore.initialize();
 * 
 *  DirectSchedulerFactory.getInstance().createScheduler("My Quartz Scheduler", "My Instance", threadPool, jobStore, "localhost", 1099); * // don't forget to start the scheduler: DirectSchedulerFactory.getInstance().getScheduler("My Quartz Scheduler", "My Instance").start();
 * </pre>
 * 
 * 
 * <p>
 * You can also use a JDBCJobStore instead of the RAMJobStore:
 * </p>
 * 
 * <pre>
 *  DBConnectionManager.getInstance().addConnectionProvider("someDatasource", new JNDIConnectionProvider("someDatasourceJNDIName"));
 * 
 *  JDBCJobStore jdbcJobStore = new JDBCJobStore(); jdbcJobStore.setDataSource("someDatasource"); jdbcJobStore.setPostgresStyleBlobs(true); jdbcJobStore.setTablePrefix("QRTZ_"); jdbcJobStore.setInstanceId("My Instance"); jdbcJobStore.initialize();
 * </pre>
 * 
 * @author Mohammad Rezaei
 * @author James House
 * 
 * @see JobStore
 * @see ThreadPool
 */
public class DirectSchedulerFactory implements SchedulerFactory {

    public static final String DEFAULT_INSTANCE_ID = "SIMPLE_NON_CLUSTERED";

    public static final String DEFAULT_SCHEDULER_NAME = "SimpleQuartzScheduler";

    private boolean initialized = false;

    private static DirectSchedulerFactory instance = new DirectSchedulerFactory();

    private final Log log = LogFactory.getLog(getClass());

    protected Log getLog() {
        return log;
    }

    /**
     * Constructor
     */
    protected DirectSchedulerFactory() {
    }

    public static DirectSchedulerFactory getInstance() {
        return instance;
    }

    /**
     * Creates an in memory job store (<code>{@link RAMJobStore}</code>)
     * The thread priority is set to Thread.NORM_PRIORITY
     * 
     * @param maxThreads
     *          The number of threads in the thread pool
     * @throws SchedulerException
     *           if initialization failed.
     */
    public void createVolatileScheduler(int maxThreads) throws SchedulerException {
        SimpleThreadPool threadPool = new SimpleThreadPool(maxThreads, Thread.NORM_PRIORITY);
        threadPool.initialize();
        JobStore jobStore = new RAMJobStore();
        this.createScheduler(threadPool, jobStore);
    }

    /**
     * @deprecated see correctly spelled method.
     * @see #createVolatileScheduler(int)
     */
    public void createVolatileSchduler(int maxThreads) throws SchedulerException {
        createVolatileScheduler(maxThreads);
    }

    /**
     * Creates a proxy to a remote scheduler. This scheduler can be retrieved
     * via {@link DirectSchedulerFactory#getScheduler()}
     * 
     * @param rmiHost
     *          The hostname for remote scheduler
     * @param rmiPort
     *          Port for the remote scheduler. The default RMI port is 1099.
     * @throws SchedulerException
     *           if the remote scheduler could not be reached.
     */
    public void createRemoteScheduler(String rmiHost, int rmiPort) throws SchedulerException {
        createRemoteScheduler(DEFAULT_SCHEDULER_NAME, DEFAULT_INSTANCE_ID, rmiHost, rmiPort);
        initialized = true;
    }

    /**
     * Same as
     * {@link DirectSchedulerFactory#createRemoteScheduler(String rmiHost, int rmiPort)},
     * with the addition of specifying the scheduler name and instance ID. This
     * scheduler can only be retrieved via
     * {@link DirectSchedulerFactory#getScheduler(String)}
     * 
     * @param schedulerName
     *          The name for the scheduler.
     * @param schedulerInstanceId
     *          The instance ID for the scheduler.
     * @param rmiHost
     *          The hostname for remote scheduler
     * @param rmiPort
     *          Port for the remote scheduler. The default RMI port is 1099.
     * @throws SchedulerException
     *           if the remote scheduler could not be reached.
     */
    public void createRemoteScheduler(String schedulerName, String schedulerInstanceId, String rmiHost, int rmiPort) throws SchedulerException {
        createRemoteScheduler(schedulerName, schedulerInstanceId, null, rmiHost, rmiPort);
    }

    /**
     * Same as
     * {@link DirectSchedulerFactory#createRemoteScheduler(String rmiHost, int rmiPort)},
     * with the addition of specifying the scheduler name, instance ID, and rmi 
     * bind name. This scheduler can only be retrieved via
     * {@link DirectSchedulerFactory#getScheduler(String)}
     * 
     * @param schedulerName
     *          The name for the scheduler.
     * @param schedulerInstanceId
     *          The instance ID for the scheduler.
     * @param rmiBindName 
     *          The name of the remote scheduler in the RMI repository.  If null
     *          defaults to the generated unique identifier. 
     * @param rmiHost
     *          The hostname for remote scheduler
     * @param rmiPort
     *          Port for the remote scheduler. The default RMI port is 1099.
     * @throws SchedulerException
     *           if the remote scheduler could not be reached.
     */
    public void createRemoteScheduler(String schedulerName, String schedulerInstanceId, String rmiBindName, String rmiHost, int rmiPort) throws SchedulerException {
        SchedulingContext schedCtxt = new SchedulingContext();
        schedCtxt.setInstanceId(schedulerInstanceId);
        String uid = (rmiBindName != null) ? rmiBindName : QuartzSchedulerResources.getUniqueIdentifier(schedulerName, schedulerInstanceId);
        RemoteScheduler remoteScheduler = new RemoteScheduler(schedCtxt, uid, rmiHost, rmiPort);
        SchedulerRepository schedRep = SchedulerRepository.getInstance();
        schedRep.bind(remoteScheduler);
    }

    /**
     * Creates a scheduler using the specified thread pool and job store. This
     * scheduler can be retrieved via
     * {@link DirectSchedulerFactory#getScheduler()}
     * 
     * @param threadPool
     *          The thread pool for executing jobs
     * @param jobStore
     *          The type of job store
     * @throws SchedulerException
     *           if initialization failed
     */
    public void createScheduler(ThreadPool threadPool, JobStore jobStore) throws SchedulerException {
        createScheduler(DEFAULT_SCHEDULER_NAME, DEFAULT_INSTANCE_ID, threadPool, jobStore);
        initialized = true;
    }

    /**
     * Same as
     * {@link DirectSchedulerFactory#createScheduler(ThreadPool threadPool, JobStore jobStore)},
     * with the addition of specifying the scheduler name and instance ID. This
     * scheduler can only be retrieved via
     * {@link DirectSchedulerFactory#getScheduler(String)}
     * 
     * @param schedulerName
     *          The name for the scheduler.
     * @param schedulerInstanceId
     *          The instance ID for the scheduler.
     * @param threadPool
     *          The thread pool for executing jobs
     * @param jobStore
     *          The type of job store
     * @throws SchedulerException
     *           if initialization failed
     */
    public void createScheduler(String schedulerName, String schedulerInstanceId, ThreadPool threadPool, JobStore jobStore) throws SchedulerException {
        createScheduler(schedulerName, schedulerInstanceId, threadPool, jobStore, null, 0, -1, -1);
    }

    /**
     * Creates a scheduler using the specified thread pool and job store and
     * binds it to RMI.
     * 
     * @param schedulerName
     *          The name for the scheduler.
     * @param schedulerInstanceId
     *          The instance ID for the scheduler.
     * @param threadPool
     *          The thread pool for executing jobs
     * @param jobStore
     *          The type of job store
     * @param rmiRegistryHost
     *          The hostname to register this scheduler with for RMI. Can use
     *          "null" if no RMI is required.
     * @param rmiRegistryPort
     *          The port for RMI. Typically 1099.
     * @param idleWaitTime
     *          The idle wait time in milliseconds. You can specify "-1" for
     *          the default value, which is currently 30000 ms.
     * @throws SchedulerException
     *           if initialization failed
     */
    public void createScheduler(String schedulerName, String schedulerInstanceId, ThreadPool threadPool, JobStore jobStore, String rmiRegistryHost, int rmiRegistryPort, long idleWaitTime, long dbFailureRetryInterval) throws SchedulerException {
        createScheduler(schedulerName, schedulerInstanceId, threadPool, jobStore, null, rmiRegistryHost, rmiRegistryPort, idleWaitTime, dbFailureRetryInterval);
    }

    /**
     * Creates a scheduler using the specified thread pool, job store, and
     * plugins, and binds it to RMI.
     * 
     * @param schedulerName
     *          The name for the scheduler.
     * @param schedulerInstanceId
     *          The instance ID for the scheduler.
     * @param threadPool
     *          The thread pool for executing jobs
     * @param jobStore
     *          The type of job store
     * @param schedulerPluginMap
     *          Map from a <code>String</code> plugin names to  
     *          <code>{@link org.quartz.spi.SchedulerPlugin}</code>s.  Can use
     *          "null" if no plugins are required. 
     * @param rmiRegistryHost
     *          The hostname to register this scheduler with for RMI. Can use
     *          "null" if no RMI is required.
     * @param rmiRegistryPort
     *          The port for RMI. Typically 1099.
     * @param idleWaitTime
     *          The idle wait time in milliseconds. You can specify "-1" for
     *          the default value, which is currently 30000 ms.
     * @throws SchedulerException
     *           if initialization failed
     */
    public void createScheduler(String schedulerName, String schedulerInstanceId, ThreadPool threadPool, JobStore jobStore, Map schedulerPluginMap, String rmiRegistryHost, int rmiRegistryPort, long idleWaitTime, long dbFailureRetryInterval) throws SchedulerException {
        JobRunShellFactory jrsf = new StdJobRunShellFactory();
        SchedulingContext schedCtxt = new SchedulingContext();
        schedCtxt.setInstanceId(schedulerInstanceId);
        QuartzSchedulerResources qrs = new QuartzSchedulerResources();
        qrs.setName(schedulerName);
        qrs.setInstanceId(schedulerInstanceId);
        qrs.setJobRunShellFactory(jrsf);
        qrs.setThreadPool(threadPool);
        qrs.setJobStore(jobStore);
        qrs.setRMIRegistryHost(rmiRegistryHost);
        qrs.setRMIRegistryPort(rmiRegistryPort);
        if (schedulerPluginMap != null) {
            for (Iterator pluginIter = schedulerPluginMap.values().iterator(); pluginIter.hasNext(); ) {
                qrs.addSchedulerPlugin((SchedulerPlugin) pluginIter.next());
            }
        }
        QuartzScheduler qs = new QuartzScheduler(qrs, schedCtxt, idleWaitTime, dbFailureRetryInterval);
        ClassLoadHelper cch = new CascadingClassLoadHelper();
        cch.initialize();
        jobStore.initialize(cch, qs.getSchedulerSignaler());
        Scheduler scheduler = new StdScheduler(qs, schedCtxt);
        if (schedulerPluginMap != null) {
            for (Iterator pluginEntryIter = schedulerPluginMap.entrySet().iterator(); pluginEntryIter.hasNext(); ) {
                Map.Entry pluginEntry = (Map.Entry) pluginEntryIter.next();
                ((SchedulerPlugin) pluginEntry.getValue()).initialize((String) pluginEntry.getKey(), scheduler);
            }
        }
        jrsf.initialize(scheduler, schedCtxt);
        getLog().info("Quartz scheduler '" + scheduler.getSchedulerName());
        getLog().info("Quartz scheduler version: " + qs.getVersion());
        SchedulerRepository schedRep = SchedulerRepository.getInstance();
        qs.addNoGCObject(schedRep);
        schedRep.bind(scheduler);
    }

    /**
     * <p>
     * Returns a handle to the Scheduler produced by this factory.
     * </p>
     * 
     * <p>
     * you must call createRemoteScheduler or createScheduler methods before
     * calling getScheduler()
     * </p>
     */
    public Scheduler getScheduler() throws SchedulerException {
        if (!initialized) {
            throw new SchedulerException("you must call createRemoteScheduler or createScheduler methods before calling getScheduler()");
        }
        return getScheduler(DEFAULT_SCHEDULER_NAME);
    }

    /**
     * <p>
     * Returns a handle to the Scheduler with the given name, if it exists.
     * </p>
     */
    public Scheduler getScheduler(String schedName) throws SchedulerException {
        SchedulerRepository schedRep = SchedulerRepository.getInstance();
        return schedRep.lookup(schedName);
    }

    /**
     * <p>
     * Returns a handle to all known Schedulers (made by any
     * StdSchedulerFactory instance.).
     * </p>
     */
    public Collection getAllSchedulers() throws SchedulerException {
        return SchedulerRepository.getInstance().lookupAll();
    }
}
