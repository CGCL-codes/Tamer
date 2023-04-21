package org.ourgrid.broker.scheduler;

import java.util.List;
import org.ourgrid.broker.commands.SchedulerData;
import org.ourgrid.broker.scheduler.extensions.GenericTransferProgress;
import org.ourgrid.broker.scheduler.extensions.IncomingHandle;
import org.ourgrid.broker.scheduler.extensions.OutgoingHandle;
import org.ourgrid.broker.status.JobWorkerStatus;
import org.ourgrid.common.executor.ExecutorResult;
import org.ourgrid.common.filemanager.FileInfo;
import org.ourgrid.common.interfaces.to.MessageHandle;
import org.ourgrid.common.interfaces.to.RequestSpec;
import org.ourgrid.common.spec.job.JobSpec;
import org.ourgrid.common.spec.worker.WorkerSpec;

public interface SchedulerIF {

    public List<SchedulerData> start();

    public List<SchedulerData> stop();

    public List<SchedulerData> schedule();

    public List<SchedulerData> addJob(JobSpec jobSpec, int jobID);

    public List<SchedulerData> cancelJob(int jobID);

    public List<SchedulerData> cleanAllFinishedJobs();

    public List<SchedulerData> cleanFinishedJob(int jobID);

    public List<SchedulerData> finishRequests();

    public List<SchedulerData> finishPeerRequests(String peerContainerID);

    public List<SchedulerData> notifyWhenJobIsFinished(int jobID, String deploymentID);

    public List<SchedulerData> setPeers(String[] peersID);

    public List<SchedulerData> loginSucceed(String peerID);

    public List<SchedulerData> hereIsWorker(String workerID, String workerPK, String senderPublicKey, String peerID, RequestSpec requestSpec, WorkerSpec workerSpec);

    public List<SchedulerData> localWorkerProviderFailure(String peerID);

    public List<SchedulerData> workerFailure(String workerContainerID, String workerPublicKey);

    public List<SchedulerData> sendMessage(MessageHandle messageHandle);

    public List<SchedulerData> jobEndedInterestedIsDown(String interestedID);

    public List<SchedulerData> outgoingTransferCancelled(OutgoingHandle handle, long amountWritten);

    public List<SchedulerData> outgoingTransferCompleted(OutgoingHandle handle, long amountWritten);

    public List<SchedulerData> outgoingTransferFailed(OutgoingHandle handle, String failCause, long amountWritten);

    public List<SchedulerData> incomingTransferCompleted(IncomingHandle handle, long amountWritten);

    public List<SchedulerData> incomingTransferFailed(IncomingHandle handle, String failCause, long amountWritten);

    public List<SchedulerData> transferRejected(OutgoingHandle handle);

    public List<SchedulerData> updateTransferProgress(GenericTransferProgress transferProgress);

    public List<SchedulerData> transferRequestReceived(IncomingHandle handle);

    public List<SchedulerData> errorOcurred(String workerContainerID, String errorCause, String gridProcessErrorType);

    public List<SchedulerData> hereIsFileInfo(String workerContainerID, long transferHandle, FileInfo fileInfo);

    public List<SchedulerData> hereIsGridProcessResult(String workerContainerID, ExecutorResult result);

    public List<SchedulerData> workerIsReady(String workerContainerID);

    public JobWorkerStatus getCompleteStatus();
}
