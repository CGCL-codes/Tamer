package org.ourgrid.acceptance.util.worker;

import java.util.List;
import java.util.concurrent.Future;
import org.easymock.classextension.EasyMock;
import org.ourgrid.acceptance.util.WorkerAcceptanceUtil;
import org.ourgrid.common.interfaces.management.WorkerManagementClient;
import org.ourgrid.worker.WorkerComponent;
import br.edu.ufcg.lsd.commune.container.ContainerContext;
import br.edu.ufcg.lsd.commune.container.logging.CommuneLogger;
import br.edu.ufcg.lsd.commune.container.servicemanager.FileTransferManager;
import br.edu.ufcg.lsd.commune.identification.DeploymentID;
import br.edu.ufcg.lsd.commune.processor.filetransfer.IncomingTransferHandle;
import br.edu.ufcg.lsd.commune.processor.filetransfer.OutgoingTransferHandle;
import br.edu.ufcg.lsd.commune.processor.filetransfer.TransferHandle;
import br.edu.ufcg.lsd.commune.test.AcceptanceTestUtil;

public class Req_122_Util extends WorkerAcceptanceUtil {

    public Req_122_Util(ContainerContext context) {
        super(context);
    }

    public void unknownPeerFails(WorkerComponent component, DeploymentID unknownPeerID) {
        masterPeerFails(component, null, unknownPeerID, true, false, false, false, null, null);
    }

    public void peerFailsBeforeSetPeer(WorkerComponent component, DeploymentID peerID) {
        masterPeerFails(component, null, peerID, false, true, false, false, null, null);
    }

    public void peerFailsAfterSetPeer(WorkerComponent component, WorkerManagementClient wmc, DeploymentID wmcID) {
        masterPeerFails(component, wmc, wmcID, false, false, false, false, null, null);
    }

    public void peerFailsWithCleaningWorker(WorkerComponent component, WorkerManagementClient wmc, DeploymentID wmcID) {
        masterPeerFails(component, wmc, wmcID, false, false, true, false, null, null);
    }

    public void peerFailsAndWorkerHasIncomingTransfer(WorkerComponent component, WorkerManagementClient wmc, DeploymentID wmcID, List<TransferHandle> handles) {
        masterPeerFails(component, wmc, wmcID, false, false, true, true, null, handles);
    }

    public void peerFailsAndWorkerHasOutgoingTransfer(WorkerComponent component, WorkerManagementClient wmc, DeploymentID wmcID, List<TransferHandle> handles) {
        masterPeerFails(component, wmc, wmcID, false, false, true, false, null, handles);
    }

    public void peerFailsAndWorkerIsExecutingACommand(WorkerComponent component, WorkerManagementClient wmc, DeploymentID wmcID, Future<?> executionFuture) {
        masterPeerFails(component, wmc, wmcID, false, false, true, false, executionFuture, null);
    }

    private void masterPeerFails(WorkerComponent component, WorkerManagementClient wmc, DeploymentID peerID, boolean unknownPeer, boolean isPeerUndefined, boolean cleans, boolean hasIncomingTransfer, Future<?> executionFuture, List<TransferHandle> handles) {
        CommuneLogger oldLogger = component.getLogger();
        CommuneLogger newLogger = EasyMock.createMock(CommuneLogger.class);
        component.setLogger(newLogger);
        if (wmc == null) {
            wmc = EasyMock.createMock(WorkerManagementClient.class);
            EasyMock.replay(wmc);
        }
        FileTransferManager ftm = EasyMock.createMock(FileTransferManager.class);
        component.setFileTransferManager(ftm);
        if (unknownPeer) {
            newLogger.warn("The unknown peer [" + peerID + "] has failed. This message was ignored.");
        } else {
            if (isPeerUndefined) {
                newLogger.warn("The peer [" + peerID + "] that didn't set itself as manager of this Worker has failed. This message was ignored.");
            } else {
                if (cleans) {
                    newLogger.debug("Cleaning Worker.");
                }
                if (executionFuture != null) {
                    EasyMock.expect(executionFuture.cancel(true)).andReturn(true);
                    EasyMock.replay(executionFuture);
                }
                if (handles != null) {
                    if (hasIncomingTransfer) {
                        for (TransferHandle handle : handles) {
                            ftm.cancelIncomingTransfer((IncomingTransferHandle) handle);
                        }
                    } else {
                        for (TransferHandle handle : handles) {
                            ftm.cancelOutgoingTransfer((OutgoingTransferHandle) handle);
                        }
                    }
                }
                newLogger.warn("The master peer [" + peerID + "] has failed. Worker will interrupt the working," + " it means cancel any transfer or execution.");
            }
        }
        EasyMock.replay(newLogger);
        getMasterPeerMonitor(component).doNotifyFailure(wmc, peerID);
        EasyMock.verify(newLogger);
        if (executionFuture != null) {
            EasyMock.verify(executionFuture);
        }
        component.setLogger(oldLogger);
    }

    public boolean isWorkerInterestedOnMasterPeerFailure(WorkerComponent component, DeploymentID peerID) {
        return AcceptanceTestUtil.isInterested(component, peerID.getServiceID(), getMasterPeerMonitorDeployment(component).getDeploymentID());
    }
}
