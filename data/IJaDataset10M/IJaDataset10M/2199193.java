package com.ibm.celldt.environment.control;

import com.ibm.celldt.remotetools.core.IRemoteExecutionManager;

/**
 * Describes a job that will run on a remote host.
 * <p>
 * Job is a collection of sequential operations executed using the IRemoteExecutionManager
 * on the target environment.
 * <p> 
 * The ITargetJob be run as a thread inside the Job Controller.
 *
 * @author Daniel Felix Ferber
 * @since 1.1
 */
public interface ITargetJob {

    /**
	 * Implementation of the job.
	 * <p>
	 * The IRemoteExecutionManager that is capable of doing the operations
	 * will be provided automatically.
	 */
    void run(IRemoteExecutionManager manager);
}
