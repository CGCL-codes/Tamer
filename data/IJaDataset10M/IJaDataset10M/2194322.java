package org.sigoa.refimpl.jobsystem.singleProcessor;

import org.sfc.refimpl.parallel.jobsystem.WorkerThread;
import org.sfc.spec.errors.IErrorHandler;
import org.sfc.spec.logging.ILogger;
import org.sfc.spec.security.ISecurityManager;
import org.sigoa.refimpl.jobsystem.OptimizationJob;
import org.sigoa.refimpl.jobsystem.OptimizationJobInfo;

/**
 * The single processor job system used for optimization
 * 
 * @author Thomas Weise
 */
public class SingleProcessorJobSystem extends org.sfc.refimpl.parallel.jobsystem.singleProcessor.SingleProcessorJobSystem<OptimizationJobInfo, OptimizationJob> {

    /**
   * Constructs a new thread group. The parent of this new group is the
   * thread group of the currently running thread.
   * <p>
   * The <code>checkAccess</code> method of the parent thread group is
   * called with no arguments; this may result in a security exception.
   * 
   * @param name
   *          the name of the new thread group.
   * @since JDK1.0
   */
    public SingleProcessorJobSystem(final String name) {
        this(name, null, null, null);
    }

    /**
   * Creates a new thread group. The parent of this new group is the
   * specified thread group.
   * <p>
   * The <code>checkAccess</code> method of the parent thread group is
   * called with no arguments; this may result in a security exception.
   * 
   * @param name
   *          the name of the new thread group.
   * @param security
   *          the security manager
   * @param errorHandler
   *          the error handler
   * @param logger
   *          the logger
   */
    public SingleProcessorJobSystem(final String name, final ISecurityManager security, final IErrorHandler errorHandler, final ILogger logger) {
        super(name, security, errorHandler, logger);
    }

    /**
   * Obtain the default job info record
   * 
   * @return the default job info record
   */
    @Override
    protected OptimizationJobInfo defaultJobInfo() {
        return OptimizationJobInfo.DEFAULT_OPTIMIZATION_JOB_INFO;
    }

    /**
   * Create a worker thread
   * 
   * @param index
   *          the index of the thread
   * @return a new worker thread
   */
    @Override
    protected WorkerThread<?, ?, ?> createWorker(final int index) {
        return new SingleProcessorWorkerThread(this, index);
    }
}
