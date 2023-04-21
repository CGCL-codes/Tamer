package com.ixora.jobs.remote;

import java.rmi.RemoteException;
import com.ixora.jobs.JobDefinition;
import com.ixora.jobs.JobId;
import com.ixora.remote.RemoteManaged;
import com.ixora.rms.exception.RMSException;

/**
 * @author Daniel Moraru
 */
public interface JobManager extends RemoteManaged {

    /**
     * @param job
     * @throws RemoteException
     * @throws RMSException
     */
    JobId runJob(JobDefinition job) throws RemoteException, RMSException;

    /**
     * Cancels the given job.
     * @param jid
     * @throws RemoteException
     * @throws RMSException
     */
    void cancelJob(JobId jid) throws RemoteException, RMSException;
}
