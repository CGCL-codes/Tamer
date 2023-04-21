package org.ourgrid.acceptance.util.worker;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import org.easymock.classextension.EasyMock;
import org.ourgrid.acceptance.util.WorkerAcceptanceUtil;
import org.ourgrid.common.interfaces.Worker;
import org.ourgrid.common.interfaces.management.RemoteWorkerManagement;
import org.ourgrid.common.interfaces.management.RemoteWorkerManagementClient;
import org.ourgrid.matchers.BeginAllocationRunnableMatcher;
import org.ourgrid.matchers.ValidWorkerMatcher;
import org.ourgrid.worker.WorkerComponent;
import org.ourgrid.worker.WorkerConstants;
import br.edu.ufcg.lsd.commune.container.ContainerContext;
import br.edu.ufcg.lsd.commune.container.ObjectDeployment;
import br.edu.ufcg.lsd.commune.container.logging.CommuneLogger;
import br.edu.ufcg.lsd.commune.container.servicemanager.FileTransferManager;
import br.edu.ufcg.lsd.commune.identification.ContainerID;
import br.edu.ufcg.lsd.commune.identification.DeploymentID;
import br.edu.ufcg.lsd.commune.identification.ServiceID;
import br.edu.ufcg.lsd.commune.network.xmpp.XMPPProperties;
import br.edu.ufcg.lsd.commune.processor.filetransfer.IncomingTransferHandle;
import br.edu.ufcg.lsd.commune.processor.filetransfer.OutgoingTransferHandle;
import br.edu.ufcg.lsd.commune.processor.filetransfer.TransferHandle;
import br.edu.ufcg.lsd.commune.test.AcceptanceTestUtil;

public class Req_121_Util extends WorkerAcceptanceUtil {

    public Req_121_Util(ContainerContext context) {
        super(context);
    }

    public void workForMyGridByUnknownRemotePeer(WorkerComponent component, String senderPublicKey) {
        workForMyGridByRemotePeer(component, senderPublicKey, null, false, false, false, false, null, null);
    }

    public RemoteWorkerManagementClient workForMyGridOnAllocatedForPeerWorker(WorkerComponent component, String remotePeerPubKey, String myGridPubKey) {
        return workForMyGridByRemotePeer(component, remotePeerPubKey, myGridPubKey, true, true, true, false, null, null);
    }

    public RemoteWorkerManagementClient workForMyGridOnAllocatedForMyGridWorker(WorkerComponent component, String remotePeerPubKey, String myGridPubKey) {
        return workForMyGridByRemotePeer(component, remotePeerPubKey, myGridPubKey, true, false, true, false, null, null);
    }

    public RemoteWorkerManagementClient workForMyGridOnAllocatedForMyGridWorkerWithCleaningError(WorkerComponent component, String remotePeerPubKey, String myGridPubKey, String playpenDirWithError) {
        return workForMyGridByRemotePeer(component, remotePeerPubKey, myGridPubKey, true, false, true, false, playpenDirWithError, null);
    }

    public RemoteWorkerManagementClient workForMyGridOnAllocatedForMyGridWorkerWithFileOnTransfer(WorkerComponent component, String remotePeerPubKey, String myGridPubKey, boolean isIncomingFile, List<TransferHandle> handles) {
        return workForMyGridByRemotePeer(component, remotePeerPubKey, myGridPubKey, true, false, true, isIncomingFile, null, handles);
    }

    private RemoteWorkerManagementClient workForMyGridByRemotePeer(WorkerComponent component, String remotePeerPubKey, String myGridPubKey, boolean isPeerKnown, boolean isWorkerAllocatedForPeer, boolean cleans, boolean isIncomingFile, String playpenDirWithError, List<TransferHandle> handles) {
        CommuneLogger oldLogger = component.getLogger();
        CommuneLogger newLogger = EasyMock.createMock(CommuneLogger.class);
        component.setLogger(newLogger);
        FileTransferManager ftm = EasyMock.createMock(FileTransferManager.class);
        component.setFileTransferManager(ftm);
        RemoteWorkerManagement rWorkerManag = getRemoteWorkerManagement();
        ObjectDeployment rwmOD = getRemoteWorkerManagementDeployment();
        RemoteWorkerManagementClient rwmc = EasyMock.createMock(RemoteWorkerManagementClient.class);
        DeploymentID remotePeerID = new DeploymentID(new ContainerID("rusername", "rserver", "rmodule", remotePeerPubKey), "remotePeer");
        createStub(rwmc, RemoteWorkerManagementClient.class, remotePeerID);
        Future future = null;
        ExecutorService newThreadPool = EasyMock.createMock(ExecutorService.class);
        component.setExecutorThreadPool(newThreadPool);
        if (!isPeerKnown) {
            newLogger.warn("An unknown remote peer tried to command this Worker to work for a remote consumer." + " This message was ignored. Unknown remote peer public key: [" + remotePeerPubKey + "].");
        } else {
            newLogger.info("Remote peer commanded this Worker to work for a remote consumer." + " Remote consumer public key: [" + myGridPubKey + "].");
            if (isWorkerAllocatedForPeer) {
                newLogger.debug("Status changed from ALLOCATED_FOR_PEER to ALLOCATED_FOR_BROKER.");
            }
            newLogger.debug("Worker begin allocation action, preparing to start the working.");
            future = EasyMock.createMock(Future.class);
            EasyMock.expect(newThreadPool.submit(BeginAllocationRunnableMatcher.eqMatcher(createBeginAllocationRunnable()))).andReturn(future).once();
            if (cleans) {
                newLogger.debug("Cleaning Worker.");
                if (playpenDirWithError != null) {
                    newLogger.error("Error while trying to clean the playpen directory [" + playpenDirWithError + "].");
                }
                if (handles != null) {
                    if (isIncomingFile) {
                        for (TransferHandle handle : handles) {
                            ftm.cancelIncomingTransfer((IncomingTransferHandle) handle);
                        }
                    } else {
                        for (TransferHandle handle : handles) {
                            ftm.cancelOutgoingTransfer((OutgoingTransferHandle) handle);
                        }
                    }
                    EasyMock.replay(ftm);
                }
            }
        }
        EasyMock.replay(newLogger);
        AcceptanceTestUtil.setExecutionContext(component, rwmOD, remotePeerPubKey);
        rWorkerManag.workForBroker(rwmc, myGridPubKey);
        EasyMock.verify(newLogger);
        if (handles != null) {
            EasyMock.verify(ftm);
        }
        component.setLogger(oldLogger);
        return rwmc;
    }

    public Worker beginAllocationCompleted(WorkerComponent component, RemoteWorkerManagementClient rwmc) {
        CommuneLogger oldLogger = component.getLogger();
        CommuneLogger newLogger = EasyMock.createMock(CommuneLogger.class);
        component.setLogger(newLogger);
        ObjectDeployment wmOD = getWorkerManagementDeployment();
        ServiceID workerEntityID = new ServiceID(new ContainerID(context.getProperty(XMPPProperties.PROP_USERNAME), context.getProperty(XMPPProperties.PROP_XMPP_SERVERNAME), WorkerConstants.WORKER, wmOD.getDeploymentID().getPublicKey()), WorkerConstants.WORKER);
        newLogger.debug("Allocation action was completed.");
        EasyMock.reset(rwmc);
        rwmc.statusChangedAllocatedForBroker(ValidWorkerMatcher.eqMatcher(getServiceManager(), workerEntityID));
        EasyMock.replay(rwmc);
        EasyMock.replay(newLogger);
        getWorkerExecutionClient().readyForAllocation();
        EasyMock.verify(rwmc);
        EasyMock.reset(rwmc);
        EasyMock.verify(newLogger);
        component.setLogger(oldLogger);
        return getWorker();
    }
}
