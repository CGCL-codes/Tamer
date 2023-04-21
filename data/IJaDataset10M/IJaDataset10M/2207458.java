package org.ourgrid.acceptance.util.worker;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import org.easymock.classextension.EasyMock;
import org.ourgrid.acceptance.util.WorkerAcceptanceUtil;
import org.ourgrid.common.interfaces.AccountingAggregator;
import org.ourgrid.common.interfaces.WorkerSpecListener;
import org.ourgrid.common.interfaces.management.WorkerManagement;
import org.ourgrid.common.interfaces.management.WorkerManagementClient;
import org.ourgrid.common.interfaces.to.WorkerStatus;
import org.ourgrid.discoveryservice.DiscoveryServiceConstants;
import org.ourgrid.matchers.RequestRepetitionRunnableMatcher;
import org.ourgrid.peer.PeerConstants;
import org.ourgrid.worker.WorkerComponent;
import org.ourgrid.worker.WorkerConstants;
import br.edu.ufcg.lsd.commune.container.ContainerContext;
import br.edu.ufcg.lsd.commune.container.ObjectDeployment;
import br.edu.ufcg.lsd.commune.container.logging.CommuneLogger;
import br.edu.ufcg.lsd.commune.identification.ContainerID;
import br.edu.ufcg.lsd.commune.identification.DeploymentID;
import br.edu.ufcg.lsd.commune.test.AcceptanceTestUtil;

public class Req_090_Util extends WorkerAcceptanceUtil {

    public Req_090_Util(ContainerContext context) {
        super(context);
    }

    public WorkerManagementClient setUnknownPeer(WorkerComponent component, DeploymentID peerID) {
        return setPeer(component, peerID, true, null, false, null);
    }

    public WorkerManagementClient setKnownPeer(WorkerComponent component, DeploymentID peerID, WorkerStatus workerStatus) {
        return setPeer(component, peerID, false, workerStatus, false, null);
    }

    public WorkerManagementClient setPeerByMasterPeerFromSameLocation(WorkerComponent component, DeploymentID peerID, WorkerStatus status, WorkerManagementClient wmc) {
        return setPeer(component, peerID, false, status, true, wmc);
    }

    public WorkerManagementClient setPeerByMasterPeerFromDiffLocation(WorkerComponent component, DeploymentID peerID, WorkerStatus status) {
        return setPeer(component, peerID, false, status, false, null);
    }

    @SuppressWarnings("unchecked")
    private WorkerManagementClient setPeer(WorkerComponent component, DeploymentID peerID, boolean isPeerUnknown, WorkerStatus workerStatus, boolean isMasterPeerHasSameDeployId, WorkerManagementClient wmc) {
        CommuneLogger oldLogger = component.getLogger();
        CommuneLogger newLogger = EasyMock.createMock(CommuneLogger.class);
        component.setLogger(newLogger);
        if (wmc == null) {
            wmc = EasyMock.createMock(WorkerManagementClient.class);
            EasyMock.reset(wmc);
            createStub(wmc, WorkerManagementClient.class, peerID);
        }
        EasyMock.reset(wmc);
        ScheduledExecutorService newTimer = EasyMock.createMock(ScheduledExecutorService.class);
        ;
        ScheduledExecutorService oldTimer = component.getTimer();
        component.setTimer(newTimer);
        if (isPeerUnknown) {
            newLogger.warn("The unknown peer [" + peerID + "] tried to set itself as manager of this Worker." + " This message was ignored. Unknown peer public key: [" + peerID.getPublicKey() + "].");
        } else if (isMasterPeerHasSameDeployId) {
            newLogger.warn("The peer [" + peerID + "] set itself as manager of this Worker. This message was ignored." + " Because the master peer did not notify fail.");
            wmc.statusChanged(workerStatus);
        } else {
            newLogger.debug("Cleaning Worker.");
            newLogger.info("The peer [" + peerID + "] set itself as manager of this Worker.");
            ScheduledFuture future = EasyMock.createMock(ScheduledFuture.class);
            EasyMock.expect(newTimer.scheduleWithFixedDelay(RequestRepetitionRunnableMatcher.eqMatcher(createReportWorkAccountingRunnable(component)), EasyMock.eq(WorkerConstants.REPORT_WORK_ACCOUNTING_TIME), EasyMock.eq(WorkerConstants.REPORT_WORK_ACCOUNTING_TIME), EasyMock.eq(TimeUnit.SECONDS))).andReturn(future);
            wmc.statusChanged(workerStatus);
        }
        EasyMock.replay(wmc);
        EasyMock.replay(newLogger);
        EasyMock.replay(newTimer);
        AccountingAggregator accounting = EasyMock.createMock(AccountingAggregator.class);
        EasyMock.replay(accounting);
        WorkerSpecListener workerSpecListener = EasyMock.createMock(WorkerSpecListener.class);
        EasyMock.replay(workerSpecListener);
        WorkerManagement workerManag = getWorkerManagement();
        ObjectDeployment wmOD = getWorkerManagementDeployment();
        AcceptanceTestUtil.setExecutionContext(component, wmOD, peerID);
        DeploymentID accID = new DeploymentID(new ContainerID("acc", "accServer", DiscoveryServiceConstants.MODULE_NAME, "dsPK"), DiscoveryServiceConstants.DS_OBJECT_NAME);
        DeploymentID workerSpecListenerID = new DeploymentID(new ContainerID("peerUser", "peerServer", PeerConstants.WORKER_SPEC_LISTENER_OBJECT_NAME, "listenerPk"), DiscoveryServiceConstants.DS_OBJECT_NAME);
        AcceptanceTestUtil.publishTestObject(component, accID, accounting, AccountingAggregator.class);
        AcceptanceTestUtil.publishTestObject(component, workerSpecListenerID, workerSpecListener, WorkerSpecListener.class);
        workerManag.setPeer(wmc, accounting, workerSpecListener);
        EasyMock.verify(accounting);
        EasyMock.verify(workerSpecListener);
        EasyMock.verify(wmc);
        EasyMock.verify(newLogger);
        EasyMock.reset(wmc);
        createStub(wmc, WorkerManagementClient.class, peerID);
        EasyMock.replay(wmc);
        EasyMock.verify(newTimer);
        component.setTimer(oldTimer);
        component.setLogger(oldLogger);
        return wmc;
    }
}
