package org.ourgrid.acceptance.peer;

import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import org.easymock.classextension.EasyMock;
import org.junit.Before;
import org.junit.Test;
import org.ourgrid.acceptance.util.PeerAcceptanceUtil;
import org.ourgrid.acceptance.util.WorkerAcceptanceUtil;
import org.ourgrid.acceptance.util.WorkerAllocation;
import org.ourgrid.acceptance.util.peer.Req_010_Util;
import org.ourgrid.acceptance.util.peer.Req_011_Util;
import org.ourgrid.acceptance.util.peer.Req_014_Util;
import org.ourgrid.acceptance.util.peer.Req_019_Util;
import org.ourgrid.acceptance.util.peer.Req_025_Util;
import org.ourgrid.acceptance.util.peer.Req_101_Util;
import org.ourgrid.acceptance.util.peer.Req_108_Util;
import org.ourgrid.acceptance.util.peer.Req_117_Util;
import org.ourgrid.acceptance.util.peer.Req_118_Util;
import org.ourgrid.common.interfaces.LocalWorkerProvider;
import org.ourgrid.common.interfaces.LocalWorkerProviderClient;
import org.ourgrid.common.interfaces.RemoteWorkerProviderClient;
import org.ourgrid.common.interfaces.control.PeerControl;
import org.ourgrid.common.interfaces.control.PeerControlClient;
import org.ourgrid.common.interfaces.to.RequestSpec;
import org.ourgrid.common.spec.worker.WorkerSpec;
import org.ourgrid.deployer.xmpp.XMPPAccount;
import org.ourgrid.peer.PeerComponent;
import org.ourgrid.peer.PeerConstants;
import org.ourgrid.reqtrace.ReqTest;
import br.edu.ufcg.lsd.commune.CommuneRuntimeException;
import br.edu.ufcg.lsd.commune.container.ObjectDeployment;
import br.edu.ufcg.lsd.commune.container.logging.CommuneLogger;
import br.edu.ufcg.lsd.commune.functionaltests.util.TestStub;
import br.edu.ufcg.lsd.commune.identification.ContainerID;
import br.edu.ufcg.lsd.commune.identification.DeploymentID;
import br.edu.ufcg.lsd.commune.test.AcceptanceTestUtil;

public class AT_0039 extends PeerAcceptanceTestCase {

    private PeerComponent component;

    private WorkerAcceptanceUtil workerAcceptanceUtil = new WorkerAcceptanceUtil(getComponentContext());

    private Req_010_Util req_010_Util = new Req_010_Util(getComponentContext());

    private Req_011_Util req_011_Util = new Req_011_Util(getComponentContext());

    private Req_014_Util req_014_Util = new Req_014_Util(getComponentContext());

    private Req_019_Util req_019_Util = new Req_019_Util(getComponentContext());

    private Req_025_Util req_025_Util = new Req_025_Util(getComponentContext());

    private Req_101_Util req_101_Util = new Req_101_Util(getComponentContext());

    private Req_108_Util req_108_Util = new Req_108_Util(getComponentContext());

    private Req_117_Util req_117_Util = new Req_117_Util(getComponentContext());

    private Req_118_Util req_118_Util = new Req_118_Util(getComponentContext());

    @Before
    public void setUp() throws Exception {
        super.setUp();
        component = req_010_Util.startPeer();
    }

    /**
	 * This test contains the following steps:
  	 *  1. Start a Peer with 5 recovered workers
	 *   2. A remote consumer requests 1 worker with the RequestID "1", and obtains it
	 *   3. The same remote consumer requests 1 worker with the RequestID "1", and obtains it
	 *   4. Another remote consumer requests 1 worker with the RequestID "1", and obtains it
	 *   5. A local consumer requests 1 worker with the RequestID "1", and obtains it
	 *   6. The same local consumer requests 1 worker with the RequestID "1", expect the peer to:
	 *         1. Ignore the request
	 *         2. Log the warn message
	 *   7. Another local consumer requests 1 worker with the RequestID "1", expect the peer to:
	 *         1. Ignore the request
	 *         2. Log the warn message
	 *   8. The first local consumer pauses the request
	 *   9. The other local consumer requests 1 worker with the RequestID "1", expect the peer to:
	 *         1. Ignore the request
	 *         2. Log the warn message
	 *  10. The first local consumer resumes the request
	 *  11. The other local consumer requests 1 worker with the RequestID "1", expect the peer to:
	 *         1. Ignore the request
	 *         2. Log the warn message
	 *  12. A remote consumer requests 1 worker with the RequestID "1", and obtains it
	 *  13. The local consumer finishes the request
	 *  14. The other local consumer requests 1 worker with the RequestID "1", and obtains it
     */
    @ReqTest(test = "AT-0039", reqs = "REQ011")
    @Test
    public void test_AT_0039_RequestWorkersWithRequestIDRepetead() throws Exception {
        XMPPAccount user1 = req_101_Util.createLocalUser("user01", "server011", "011011");
        XMPPAccount user2 = req_101_Util.createLocalUser("user02", "server011", "011011");
        CommuneLogger loggerMock = EasyMock.createMock(CommuneLogger.class);
        component.setLogger(loggerMock);
        ScheduledExecutorService timer = EasyMock.createMock(ScheduledExecutorService.class);
        component.setTimer(timer);
        WorkerSpec workerSpecA = workerAcceptanceUtil.createWorkerSpec("U1", "S1");
        WorkerSpec workerSpecB = workerAcceptanceUtil.createWorkerSpec("U2", "S1");
        WorkerSpec workerSpecC = workerAcceptanceUtil.createWorkerSpec("U3", "S1");
        WorkerSpec workerSpecD = workerAcceptanceUtil.createWorkerSpec("U4", "S1");
        WorkerSpec workerSpecE = workerAcceptanceUtil.createWorkerSpec("U5", "S1");
        List<WorkerSpec> workers = AcceptanceTestUtil.createList(workerSpecA, workerSpecB, workerSpecC, workerSpecD, workerSpecE);
        req_010_Util.setWorkers(component, workers);
        String workerAPublicKey = "workerApublicKey";
        String workerBPublicKey = "workerBPublicKey";
        String workerCPublicKey = "workerCPublicKey";
        String workerDPublicKey = "workerDPublicKey";
        String workerEPublicKey = "workerEPublicKey";
        DeploymentID workerAObjID = req_019_Util.notifyWorkerRecovery(component, workerSpecA, workerAPublicKey);
        DeploymentID workerBObjID = req_019_Util.notifyWorkerRecovery(component, workerSpecB, workerBPublicKey);
        DeploymentID workerCObjID = req_019_Util.notifyWorkerRecovery(component, workerSpecC, workerCPublicKey);
        DeploymentID workerDObjID = req_019_Util.notifyWorkerRecovery(component, workerSpecD, workerDPublicKey);
        DeploymentID workerEObjID = req_019_Util.notifyWorkerRecovery(component, workerSpecE, workerEPublicKey);
        req_025_Util.changeWorkerStatusToIdle(component, workerEObjID);
        req_025_Util.changeWorkerStatusToIdle(component, workerDObjID);
        req_025_Util.changeWorkerStatusToIdle(component, workerCObjID);
        req_025_Util.changeWorkerStatusToIdle(component, workerBObjID);
        req_025_Util.changeWorkerStatusToIdle(component, workerAObjID);
        DeploymentID remoteClient1OID = PeerAcceptanceUtil.createRemoteConsumerID("r1UserName", "server", "consumerPublicKey1");
        int request1ID = 1;
        RequestSpec requestSpec1 = new RequestSpec(0, createJobSpec("label"), request1ID, "", 1, 0, 0);
        WorkerAllocation workerAllocationA = new WorkerAllocation(workerAObjID);
        RemoteWorkerProviderClient remoteClient1 = req_011_Util.requestForRemoteClient(component, remoteClient1OID, requestSpec1, 0, workerAllocationA);
        req_025_Util.changeWorkerStatusToAllocatedForPeer(component, remoteClient1, workerAObjID, workerSpecA, remoteClient1OID);
        WorkerAllocation workerAllocationB = new WorkerAllocation(workerBObjID);
        req_011_Util.requestForRemoteClient(component, remoteClient1OID, requestSpec1, Req_011_Util.DO_NOT_LOAD_SUBCOMMUNITIES, workerAllocationB);
        req_025_Util.changeWorkerStatusToAllocatedForPeer(component, remoteClient1, workerBObjID, workerSpecB, remoteClient1OID);
        String remoteClient2PublicKey = "consumer2PublicKey";
        DeploymentID remoteClient2OID = new DeploymentID(new ContainerID("r2UserName", "server", PeerConstants.MODULE_NAME, remoteClient2PublicKey), PeerConstants.REMOTE_WORKER_PROVIDER_CLIENT);
        WorkerAllocation workerAllocationC = new WorkerAllocation(workerCObjID);
        RemoteWorkerProviderClient remoteClient2 = req_011_Util.requestForRemoteClient(component, remoteClient2OID, requestSpec1, Req_011_Util.DO_NOT_LOAD_SUBCOMMUNITIES, workerAllocationC);
        req_025_Util.changeWorkerStatusToAllocatedForPeer(component, remoteClient2, workerCObjID, workerSpecC, remoteClient2OID);
        String mygrid1PubKey = "publicKeyL1";
        String mygrid2PubKey = "publicKeyL2";
        PeerControl peerControl = peerAcceptanceUtil.getPeerControl();
        ObjectDeployment pcOD = peerAcceptanceUtil.getPeerControlDeployment();
        PeerControlClient peerControlClient = EasyMock.createMock(PeerControlClient.class);
        DeploymentID pccID = new DeploymentID(new ContainerID("pcc", "broker", "broker"), mygrid1PubKey);
        AcceptanceTestUtil.publishTestObject(component, pccID, peerControlClient, PeerControlClient.class);
        AcceptanceTestUtil.setExecutionContext(component, pcOD, pccID);
        try {
            peerControl.addUser(peerControlClient, user1.getUsername() + "@" + user1.getServerAddress(), user1.getUserpassword());
        } catch (CommuneRuntimeException e) {
        }
        DeploymentID lwpc1OID = req_108_Util.login(component, user1, mygrid1PubKey);
        LocalWorkerProviderClient lwpc = (LocalWorkerProviderClient) AcceptanceTestUtil.getBoundObject(lwpc1OID);
        PeerControlClient peerControlClient2 = EasyMock.createMock(PeerControlClient.class);
        DeploymentID pccID2 = new DeploymentID(new ContainerID("pcc2", "broker2", "broker"), mygrid2PubKey);
        AcceptanceTestUtil.publishTestObject(component, pccID2, peerControlClient2, PeerControlClient.class);
        AcceptanceTestUtil.setExecutionContext(component, pcOD, pccID2);
        try {
            peerControl.addUser(peerControlClient2, user2.getUsername() + "@" + user2.getServerAddress(), user2.getUserpassword());
        } catch (CommuneRuntimeException e) {
        }
        DeploymentID lwpc2OID = req_108_Util.login(component, user2, mygrid2PubKey);
        WorkerAllocation workerAllocationD = new WorkerAllocation(workerDObjID);
        req_011_Util.requestForLocalConsumer(component, new TestStub(lwpc1OID, lwpc), requestSpec1, workerAllocationD);
        req_025_Util.changeWorkerStatusToAllocatedForBroker(component, lwpc1OID, workerDObjID, workerSpecD, requestSpec1);
        component.setLogger(loggerMock);
        EasyMock.reset(loggerMock);
        loggerMock.warn("Request " + request1ID + ": New request ignored because this request ID " + "number is already being used. Local consumer ID: [" + lwpc1OID + "]");
        EasyMock.replay(loggerMock);
        LocalWorkerProvider lwp = peerAcceptanceUtil.getLocalWorkerProviderProxy();
        ObjectDeployment lwpOD = peerAcceptanceUtil.getLocalWorkerProviderDeployment();
        AcceptanceTestUtil.setExecutionContext(component, lwpOD, lwpc1OID);
        lwp.requestWorkers(requestSpec1);
        EasyMock.verify(loggerMock);
        EasyMock.reset(loggerMock);
        loggerMock.warn("Request " + request1ID + ": New request ignored because this request ID " + "number is already being used. Local consumer ID: [" + lwpc2OID + "]");
        EasyMock.replay(loggerMock);
        AcceptanceTestUtil.setExecutionContext(component, lwpOD, lwpc2OID);
        lwp.requestWorkers(requestSpec1);
        EasyMock.verify(loggerMock);
        EasyMock.reset(loggerMock);
        req_117_Util.pauseRequest(component, lwpc1OID, request1ID, null);
        loggerMock.warn("Request " + request1ID + ": New request ignored because this request ID " + "number is already being used. Local consumer ID: [" + lwpc1OID + "]");
        EasyMock.replay(loggerMock);
        AcceptanceTestUtil.setExecutionContext(component, lwpOD, lwpc1OID);
        lwp.requestWorkers(requestSpec1);
        EasyMock.verify(loggerMock);
        EasyMock.reset(loggerMock);
        loggerMock.warn("Request " + request1ID + ": New request ignored because this request ID " + "number is already being used. Local consumer ID: [" + lwpc2OID + "]");
        EasyMock.replay(loggerMock);
        AcceptanceTestUtil.setExecutionContext(component, lwpOD, lwpc2OID);
        lwp.requestWorkers(requestSpec1);
        EasyMock.verify(loggerMock);
        EasyMock.reset(loggerMock);
        req_118_Util.resumeRequestWithNoReschedule(requestSpec1, lwpc1OID, component);
        loggerMock.warn("Request " + request1ID + ": New request ignored because this request ID " + "number is already being used. Local consumer ID: [" + lwpc1OID + "]");
        EasyMock.replay(loggerMock);
        AcceptanceTestUtil.setExecutionContext(component, lwpOD, lwpc1OID);
        lwp.requestWorkers(requestSpec1);
        EasyMock.verify(loggerMock);
        EasyMock.reset(loggerMock);
        loggerMock.warn("Request " + request1ID + ": New request ignored because this request ID " + "number is already being used. Local consumer ID: [" + lwpc2OID + "]");
        EasyMock.replay(loggerMock);
        AcceptanceTestUtil.setExecutionContext(component, lwpOD, lwpc2OID);
        lwp.requestWorkers(requestSpec1);
        EasyMock.verify(loggerMock);
        EasyMock.reset(loggerMock);
        WorkerAllocation workerAllocationE = new WorkerAllocation(workerEObjID);
        req_011_Util.requestForRemoteClient(component, remoteClient1OID, requestSpec1, Req_011_Util.DO_NOT_LOAD_SUBCOMMUNITIES, workerAllocationE);
        req_025_Util.changeWorkerStatusToAllocatedForPeer(component, remoteClient1, workerEObjID, workerSpecE, remoteClient1OID);
        req_014_Util.finishRequestWithLocalWorkers(component, lwp, lwpOD, mygrid1PubKey, requestSpec1, AcceptanceTestUtil.createList(workerAllocationD));
        req_011_Util.requestForRemoteClient(component, remoteClient2OID, requestSpec1, Req_011_Util.DO_NOT_LOAD_SUBCOMMUNITIES, workerAllocationD);
    }
}
