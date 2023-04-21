package org.ourgrid.acceptance.peer;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import java.io.File;
import java.util.List;
import org.easymock.classextension.EasyMock;
import org.junit.Before;
import org.junit.Test;
import org.ourgrid.acceptance.util.PeerAcceptanceUtil;
import org.ourgrid.acceptance.util.WorkerAcceptanceUtil;
import org.ourgrid.acceptance.util.WorkerAllocation;
import org.ourgrid.acceptance.util.peer.Req_010_Util;
import org.ourgrid.acceptance.util.peer.Req_011_Util;
import org.ourgrid.acceptance.util.peer.Req_015_Util;
import org.ourgrid.acceptance.util.peer.Req_018_Util;
import org.ourgrid.acceptance.util.peer.Req_019_Util;
import org.ourgrid.acceptance.util.peer.Req_020_Util;
import org.ourgrid.acceptance.util.peer.Req_025_Util;
import org.ourgrid.acceptance.util.peer.Req_036_Util;
import org.ourgrid.acceptance.util.peer.Req_101_Util;
import org.ourgrid.acceptance.util.peer.Req_106_Util;
import org.ourgrid.acceptance.util.peer.Req_108_Util;
import org.ourgrid.common.interfaces.LocalWorkerProvider;
import org.ourgrid.common.interfaces.LocalWorkerProviderClient;
import org.ourgrid.common.interfaces.RemoteWorkerProvider;
import org.ourgrid.common.interfaces.RemoteWorkerProviderClient;
import org.ourgrid.common.interfaces.Worker;
import org.ourgrid.common.interfaces.control.PeerControl;
import org.ourgrid.common.interfaces.control.PeerControlClient;
import org.ourgrid.common.interfaces.management.RemoteWorkerManagement;
import org.ourgrid.common.interfaces.to.LocalWorkerState;
import org.ourgrid.common.interfaces.to.RequestSpec;
import org.ourgrid.common.interfaces.to.UserInfo;
import org.ourgrid.common.interfaces.to.UserState;
import org.ourgrid.common.interfaces.to.WorkerInfo;
import org.ourgrid.common.spec.job.JobSpec;
import org.ourgrid.common.spec.worker.WorkerSpec;
import org.ourgrid.deployer.xmpp.XMPPAccount;
import org.ourgrid.discoveryservice.DiscoveryServiceConstants;
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

@ReqTest(reqs = "REQ015")
public class Req_015_Test extends PeerAcceptanceTestCase {

    private WorkerAcceptanceUtil workerAcceptanceUtil = new WorkerAcceptanceUtil(getComponentContext());

    private PeerAcceptanceUtil peerAcceptanceUtil = new PeerAcceptanceUtil(getComponentContext());

    private Req_010_Util req_010_Util = new Req_010_Util(getComponentContext());

    private Req_011_Util req_011_Util = new Req_011_Util(getComponentContext());

    private Req_018_Util req_018_Util = new Req_018_Util(getComponentContext());

    private Req_019_Util req_019_Util = new Req_019_Util(getComponentContext());

    private Req_020_Util req_020_Util = new Req_020_Util(getComponentContext());

    private Req_025_Util req_025_Util = new Req_025_Util(getComponentContext());

    private Req_101_Util req_101_Util = new Req_101_Util(getComponentContext());

    private Req_108_Util req_108_Util = new Req_108_Util(getComponentContext());

    private Req_036_Util req_036_Util = new Req_036_Util(getComponentContext());

    private Req_106_Util req_106_Util = new Req_106_Util(getComponentContext());

    private Req_015_Util req_015_Util = new Req_015_Util(getComponentContext());

    public static final String COMM_FILE_PATH = "req_015" + File.separator;

    private PeerComponent peerComponent;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        PeerAcceptanceUtil.setUp();
    }

    /**
	 * Verifies if the peer give the worker back to MyGrid, because the 
	 * request is not paused.
	 */
    @ReqTest(test = "AT-015.1", reqs = "REQ015")
    @Test
    public void test_AT_015_1_MyGridDisposesLocalWorker() throws Exception {
        XMPPAccount user = req_101_Util.createLocalUser("user011", "server011", "011011");
        peerComponent = req_010_Util.startPeer();
        PeerControl peerControl = peerAcceptanceUtil.getPeerControl();
        ObjectDeployment pcOD = peerAcceptanceUtil.getPeerControlDeployment();
        PeerControlClient peerControlClient1 = EasyMock.createMock(PeerControlClient.class);
        DeploymentID pccID1 = new DeploymentID(new ContainerID("pcc1", "broker", "broker"), "user1");
        AcceptanceTestUtil.publishTestObject(peerComponent, pccID1, peerControlClient1, PeerControlClient.class);
        AcceptanceTestUtil.setExecutionContext(peerComponent, pcOD, pccID1);
        try {
            peerControl.addUser(peerControlClient1, user.getUsername() + "@" + user.getServerAddress(), user.getUserpassword());
        } catch (CommuneRuntimeException e) {
        }
        String workerPublicKey = "workerPublicKey";
        WorkerSpec workerSpecA = workerAcceptanceUtil.createWorkerSpec("U1", "S1");
        req_010_Util.setWorkers(peerComponent, AcceptanceTestUtil.createList(workerSpecA));
        DeploymentID wmOID = req_019_Util.notifyWorkerRecovery(peerComponent, workerSpecA, workerPublicKey);
        req_025_Util.changeWorkerStatusToIdle(peerComponent, wmOID);
        String myGridPubKey = "myGridPubkey";
        DeploymentID lwpcOID = req_108_Util.login(peerComponent, user, myGridPubKey);
        LocalWorkerProviderClient lwpc = (LocalWorkerProviderClient) AcceptanceTestUtil.getBoundObject(lwpcOID);
        RequestSpec requestSpec1 = new RequestSpec(0, new JobSpec("label"), 1, "", 1, 0, 0);
        WorkerAllocation allocationA = new WorkerAllocation(wmOID);
        req_011_Util.requestForLocalConsumer(peerComponent, new TestStub(lwpcOID, lwpc), requestSpec1, allocationA);
        Worker worker = (Worker) req_025_Util.changeWorkerStatusToAllocatedForBroker(peerComponent, lwpcOID, wmOID, workerSpecA, requestSpec1).getObject();
        allocationA.addLoserRequestSpec(requestSpec1).addLoserConsumer(lwpcOID).addWinnerConsumer(lwpcOID);
        req_015_Util.localConsumerDisposesLocalWorker(peerComponent, worker, allocationA);
        assertTrue(peerAcceptanceUtil.isPeerInterestedOnBroker(lwpcOID.getServiceID()));
        assertTrue(peerAcceptanceUtil.isPeerInterestedOnLocalWorker(wmOID.getServiceID()));
        UserInfo userInfo1 = new UserInfo(user.getUsername(), user.getServerAddress(), myGridPubKey, UserState.CONSUMING);
        List<UserInfo> usersInfo = AcceptanceTestUtil.createList(userInfo1);
        req_106_Util.getUsersStatus(usersInfo);
        WorkerInfo workerInfo = new WorkerInfo(workerSpecA, LocalWorkerState.IN_USE, lwpcOID.getServiceID());
        List<WorkerInfo> workersInfo = AcceptanceTestUtil.createList(workerInfo);
        req_036_Util.getLocalWorkersStatus(workersInfo);
    }

    /**
	 * Verifies if the peer give the remote worker back to MyGrid, because the 
	 * request is not paused.
	 */
    @ReqTest(test = "AT-015.2", reqs = "REQ015")
    @Test
    public void test_AT_015_2_MyGridDisposesRemoteWorker() throws Exception {
        XMPPAccount user = req_101_Util.createLocalUser("user011", "server011", "011011");
        peerComponent = req_010_Util.startPeer();
        CommuneLogger loggerMock = getMock(NOT_NICE, CommuneLogger.class);
        peerComponent.setLogger(loggerMock);
        DeploymentID dsID = new DeploymentID(new ContainerID("magodosnos", "sweetleaf.lab", DiscoveryServiceConstants.MODULE_NAME), DiscoveryServiceConstants.DS_OBJECT_NAME);
        req_020_Util.notifyDiscoveryServiceRecovery(peerComponent, dsID);
        PeerControl peerControl = peerAcceptanceUtil.getPeerControl();
        ObjectDeployment pcOD = peerAcceptanceUtil.getPeerControlDeployment();
        PeerControlClient peerControlClient1 = EasyMock.createMock(PeerControlClient.class);
        DeploymentID pccID1 = new DeploymentID(new ContainerID("pcc1", "broker", "broker"), "user1");
        AcceptanceTestUtil.publishTestObject(peerComponent, pccID1, peerControlClient1, PeerControlClient.class);
        AcceptanceTestUtil.setExecutionContext(peerComponent, pcOD, pccID1);
        try {
            peerControl.addUser(peerControlClient1, user.getUsername() + "@" + user.getServerAddress(), user.getUserpassword());
        } catch (CommuneRuntimeException e) {
        }
        String mygridPubKey = "myGridPublicKey";
        DeploymentID lwpcOID = req_108_Util.login(peerComponent, user, mygridPubKey);
        LocalWorkerProviderClient lwpc = (LocalWorkerProviderClient) AcceptanceTestUtil.getBoundObject(lwpcOID);
        RequestSpec requestSpec1 = new RequestSpec(0, new JobSpec("label"), 1, "os = windows AND mem > 256", 1, 0, 0);
        req_011_Util.requestForLocalConsumer(peerComponent, new TestStub(lwpcOID, lwpc), requestSpec1);
        WorkerSpec workerSpec = workerAcceptanceUtil.createWorkerSpec("U1", "S1");
        workerSpec.putAttribute(WorkerSpec.ATT_OS, "windows");
        workerSpec.putAttribute(WorkerSpec.ATT_MEM, "512");
        TestStub rwpStub = req_020_Util.receiveRemoteWorkerProvider(peerComponent, requestSpec1, "rwpUser", "rwpServer", "rwpPublicKey", workerSpec);
        RemoteWorkerProvider rwp = (RemoteWorkerProvider) rwpStub.getObject();
        DeploymentID rwpID = rwpStub.getDeploymentID();
        TestStub rmwStub = req_018_Util.receiveRemoteWorker(peerComponent, rwp, rwpID, workerSpec, "workerPK", mygridPubKey);
        DeploymentID rmwOID = rmwStub.getDeploymentID();
        ObjectDeployment rmwOD = peerAcceptanceUtil.getRemoteWorkerMonitorDeployment();
        assertTrue(AcceptanceTestUtil.isInterested(peerComponent, rmwOID.getServiceID(), rmwOD.getDeploymentID()));
        ObjectDeployment lwpcOD = new ObjectDeployment(peerComponent.getContainer(), lwpcOID, AcceptanceTestUtil.getBoundObject(lwpcOID));
        TestStub workerStub = req_025_Util.changeWorkerStatusToAllocatedForBroker(peerComponent, lwpcOD, peerAcceptanceUtil.getRemoteWorkerManagementClientDeployment(), rmwStub.getDeploymentID(), workerSpec, requestSpec1);
        WorkerAllocation allocation = new WorkerAllocation(rmwOID);
        allocation.addLoserConsumer(lwpcOID).addWinnerConsumer(lwpcOID).addLoserRequestSpec(requestSpec1);
        req_015_Util.localDisposeRemoteWorker(peerComponent, workerStub, allocation, false);
        assertTrue(AcceptanceTestUtil.isInterested(peerComponent, rmwOID.getServiceID(), rmwOD.getDeploymentID()));
    }

    /**
	 * Verifies if the peer command the worker to stop working, after be 
	 * diposed by a remote peer.
	 */
    @ReqTest(test = "AT-015.3", reqs = "REQ015")
    @Test
    public void test_AT_015_3_RemotePeerDisposesLocalWorker() throws Exception {
        PeerAcceptanceUtil.copyTrustFile(COMM_FILE_PATH + "015_blank.xml");
        peerComponent = req_010_Util.startPeer();
        CommuneLogger loggerMock = getMock(NOT_NICE, CommuneLogger.class);
        peerComponent.setLogger(loggerMock);
        String workerPublicKey = "workerPublicKey";
        WorkerSpec workerSpecA = workerAcceptanceUtil.createWorkerSpec("U1", "S1");
        req_010_Util.setWorkers(peerComponent, AcceptanceTestUtil.createList(workerSpecA));
        DeploymentID wmOID = req_019_Util.notifyWorkerRecovery(peerComponent, workerSpecA, workerPublicKey);
        req_025_Util.changeWorkerStatusToIdle(peerComponent, wmOID);
        WorkerAllocation allocationA = new WorkerAllocation(wmOID);
        DeploymentID remoteClientOID = new DeploymentID(new ContainerID("remoteUser", "server", PeerConstants.MODULE_NAME, "peerPublicKey"), PeerConstants.REMOTE_WORKER_PROVIDER_CLIENT);
        RequestSpec requestSpec = new RequestSpec(0, new JobSpec("label"), 1, "", 1, 0, 0);
        RemoteWorkerProviderClient rwpc = req_011_Util.requestForRemoteClient(peerComponent, remoteClientOID, requestSpec, 0, allocationA);
        assertTrue(peerAcceptanceUtil.isPeerInterestedOnRemoteClient(remoteClientOID.getServiceID()));
        RemoteWorkerManagement rwm = req_025_Util.changeWorkerStatusToAllocatedForPeer(peerComponent, rwpc, wmOID, workerSpecA, remoteClientOID);
        req_015_Util.remoteDisposeLocalWorker(peerComponent, remoteClientOID, rwm, wmOID);
        assertFalse(AcceptanceTestUtil.isInterested(peerComponent, wmOID.getServiceID(), remoteClientOID));
        assertFalse(peerAcceptanceUtil.isPeerInterestedOnRemoteClient(remoteClientOID.getServiceID()));
        WorkerInfo workerInfo = new WorkerInfo(workerSpecA, LocalWorkerState.IDLE, null);
        List<WorkerInfo> workersInfo = AcceptanceTestUtil.createList(workerInfo);
        req_036_Util.getLocalWorkersStatus(workersInfo);
    }

    /**
	 * Verifies if the peer ignores a worker dispose, on these scenarios:
	 *
	 * The message sender is an unknown MyGrid;
	 * The message sender is a not logged MyGrid;
	 * For a logged MyGrid:
	 *	o The worker is null;
	 *	o The local worker is unknown;
	 *	o The local worker is not recovered;
	 *	o The local worker is owner;
	 *	o The local worker is idle;
	 *	o The local worker is allocated for other MyGrid;
	 *	o The local worker is allocated for a remote Peer;
	 *	o The remote worker is allocated for other MyGrid;
	 * The message sender is an unknown remote Peer;
	 * For a valid remote Peer:
	 *	o The local worker is null;
	 *	o The local worker is unknown;
	 *	o The local worker is not recovered;
	 *	o The local worker is idle;
	 *	o The local worker is owner;
	 *	o The local worker is allocated for a MyGrid;
	 *	o The local worker is allocated for other remote Peer;
	 *	o The worker is remote.
	 * @throws Exception
	 */
    @ReqTest(test = "AT-015.4", reqs = "REQ015")
    @Test
    public void test_AT_015_4_InputValidation() throws Exception {
        PeerAcceptanceUtil.copyTrustFile(COMM_FILE_PATH + "015_blank.xml");
        XMPPAccount user1 = req_101_Util.createLocalUser("user011_1", "server011", "011011");
        XMPPAccount user2 = req_101_Util.createLocalUser("user011_2", "server011", "011011");
        XMPPAccount user3 = req_101_Util.createLocalUser("user011_3", "server011", "011011");
        XMPPAccount user4 = req_101_Util.createLocalUser("user011_4", "server011", "011011");
        peerComponent = req_010_Util.startPeer();
        PeerControl peerControl = peerAcceptanceUtil.getPeerControl();
        PeerControlClient peerControlClient1 = EasyMock.createMock(PeerControlClient.class);
        DeploymentID pccID1 = new DeploymentID(new ContainerID("peerClient1", "peerClientServer1", "broker"), "broker");
        AcceptanceTestUtil.publishTestObject(peerComponent, pccID1, peerControlClient1, PeerControlClient.class);
        try {
            peerControl.addUser(peerControlClient1, user1.getUsername() + "@" + user1.getServerAddress(), user1.getUserpassword());
        } catch (CommuneRuntimeException e) {
        }
        PeerControlClient peerControlClient2 = EasyMock.createMock(PeerControlClient.class);
        DeploymentID pccID2 = new DeploymentID(new ContainerID("peerClient2", "peerClientServer2", "broker"), "broker");
        AcceptanceTestUtil.publishTestObject(peerComponent, pccID2, peerControlClient2, PeerControlClient.class);
        try {
            peerControl.addUser(peerControlClient2, user2.getUsername() + "@" + user2.getServerAddress(), user2.getUserpassword());
        } catch (CommuneRuntimeException e) {
        }
        PeerControlClient peerControlClient3 = EasyMock.createMock(PeerControlClient.class);
        DeploymentID pccID3 = new DeploymentID(new ContainerID("peerClient3", "peerClientServer3", "broker"), "broker");
        AcceptanceTestUtil.publishTestObject(peerComponent, pccID3, peerControlClient3, PeerControlClient.class);
        try {
            peerControl.addUser(peerControlClient3, user3.getUsername() + "@" + user3.getServerAddress(), user3.getUserpassword());
        } catch (CommuneRuntimeException e) {
        }
        PeerControlClient peerControlClient4 = EasyMock.createMock(PeerControlClient.class);
        DeploymentID pccID4 = new DeploymentID(new ContainerID("peerClient4", "peerClientServer4", "broker"), "broker");
        AcceptanceTestUtil.publishTestObject(peerComponent, pccID4, peerControlClient4, PeerControlClient.class);
        try {
            peerControl.addUser(peerControlClient4, user4.getUsername() + "@" + user4.getServerAddress(), user4.getUserpassword());
        } catch (CommuneRuntimeException e) {
        }
        CommuneLogger loggerMock = getMock(NOT_NICE, CommuneLogger.class);
        peerComponent.setLogger(loggerMock);
        DeploymentID workerID = createWorkerDeploymentID(workerAcceptanceUtil.createWorkerSpec("unknown", "unknown"), "workerPubKey");
        Worker worker = getMock(NOT_NICE, Worker.class);
        peerAcceptanceUtil.createStub(worker, Worker.class, workerID);
        LocalWorkerProvider lwp = peerAcceptanceUtil.getLocalWorkerProviderProxy();
        ObjectDeployment lwpOD = peerAcceptanceUtil.getLocalWorkerProviderDeployment();
        String unknownPublicKey = "unknownPublicKey";
        loggerMock.warn("Ignoring an unknown consumer which disposed a worker. Consumer public key: " + unknownPublicKey);
        replayActiveMocks();
        DeploymentID brokerID = new DeploymentID(new ContainerID("brokerUser", "brokerServer", "broker", unknownPublicKey), "broker");
        AcceptanceTestUtil.setExecutionContext(peerComponent, lwpOD, brokerID);
        lwp.disposeWorker(workerID.getServiceID());
        verifyActiveMocks();
        resetActiveMocks();
        String myGrid1PubKey = "myGrid1Pubkey";
        DeploymentID lwpc1OID = req_108_Util.login(peerComponent, user1, myGrid1PubKey);
        loggerMock.info("The local consumer [" + lwpc1OID + "] has failed. Canceling his requests.");
        replayActiveMocks();
        peerAcceptanceUtil.getClientMonitor().doNotifyFailure((LocalWorkerProviderClient) AcceptanceTestUtil.getBoundObject(lwpc1OID), lwpc1OID);
        verifyActiveMocks();
        resetActiveMocks();
        loggerMock.warn("Ignoring an unknown consumer which disposed a worker. Consumer public key: " + myGrid1PubKey);
        replayActiveMocks();
        AcceptanceTestUtil.setExecutionContext(peerComponent, lwpOD, lwpc1OID);
        lwp.disposeWorker(workerID.getServiceID());
        verifyActiveMocks();
        resetActiveMocks();
        lwpc1OID = req_108_Util.login(peerComponent, user1, myGrid1PubKey);
        loggerMock.warn("The consumer [" + lwpc1OID + "] disposed a null worker. This disposal was ignored.");
        replayActiveMocks();
        lwp.disposeWorker(null);
        verifyActiveMocks();
        resetActiveMocks();
        workerID = createWorkerDeploymentID(workerAcceptanceUtil.createWorkerSpec("unknown", "unknown"), "workerPubKey");
        peerAcceptanceUtil.createStub(worker, Worker.class, workerID);
        loggerMock.warn("The consumer [" + lwpc1OID + "] disposed an unknown worker. This disposal was ignored.");
        replayActiveMocks();
        AcceptanceTestUtil.setExecutionContext(peerComponent, lwpOD, lwpc1OID);
        lwp.disposeWorker(workerID.getServiceID());
        verifyActiveMocks();
        resetActiveMocks();
        WorkerSpec workerSpecA = workerAcceptanceUtil.createWorkerSpec("U1", "S1");
        workerSpecA.putAttribute(WorkerSpec.ATT_MEM, "512");
        WorkerSpec workerSpecB = workerAcceptanceUtil.createWorkerSpec("U2", "S1");
        workerSpecB.putAttribute(WorkerSpec.ATT_MEM, "32");
        WorkerSpec workerSpecC = workerAcceptanceUtil.createWorkerSpec("U3", "S1");
        WorkerSpec workerSpecD = workerAcceptanceUtil.createWorkerSpec("U4", "S1");
        WorkerSpec workerSpecF = workerAcceptanceUtil.createWorkerSpec("U6", "S1");
        req_010_Util.setWorkers(peerComponent, AcceptanceTestUtil.createList(workerSpecA, workerSpecB, workerSpecC, workerSpecD, workerSpecF));
        Worker workerA = getMock(NOT_NICE, Worker.class);
        String workerAPublicKey = "workerAPublicKey";
        workerID = createWorkerDeploymentID(workerSpecA, "workerAPublicKey");
        AcceptanceTestUtil.publishTestObject(peerComponent, workerID, workerA, Worker.class);
        loggerMock.warn("The consumer [" + lwpc1OID + "] disposed an unknown worker. This disposal was ignored.");
        replayActiveMocks();
        AcceptanceTestUtil.setExecutionContext(peerComponent, lwpOD, lwpc1OID);
        lwp.disposeWorker(workerID.getServiceID());
        verifyActiveMocks();
        resetActiveMocks();
        DeploymentID wmAOID = req_019_Util.notifyWorkerRecovery(peerComponent, workerSpecA, workerAPublicKey);
        AcceptanceTestUtil.notifyRecovery(peerComponent, wmAOID);
        loggerMock.warn("The consumer [" + lwpc1OID + "] disposed the worker [" + wmAOID.getServiceID() + "], that is not allocated for him. This disposal was ignored.");
        replayActiveMocks();
        AcceptanceTestUtil.setExecutionContext(peerComponent, lwpOD, lwpc1OID);
        lwp.disposeWorker(wmAOID.getServiceID());
        verifyActiveMocks();
        resetActiveMocks();
        req_025_Util.changeWorkerStatusToIdle(peerComponent, wmAOID);
        workerID = createWorkerDeploymentID(workerSpecA, "workerAPublicKey");
        AcceptanceTestUtil.publishTestObject(peerComponent, workerID, workerA, Worker.class);
        loggerMock.warn("The consumer [" + lwpc1OID + "] disposed the worker [" + wmAOID.getServiceID() + "], that is not allocated for him. This disposal was ignored.");
        replayActiveMocks();
        AcceptanceTestUtil.setExecutionContext(peerComponent, lwpOD, lwpc1OID);
        lwp.disposeWorker(wmAOID.getServiceID());
        verifyActiveMocks();
        resetActiveMocks();
        DeploymentID lwpc2OID = req_108_Util.login(peerComponent, user2, "myGrid2Pubkey");
        LocalWorkerProviderClient lwpc2 = (LocalWorkerProviderClient) AcceptanceTestUtil.getBoundObject(lwpc2OID);
        RequestSpec requestSpec1 = new RequestSpec(0, new JobSpec("label"), 1, "", 1, 0, 0);
        WorkerAllocation allocationA = new WorkerAllocation(wmAOID);
        req_011_Util.requestForLocalConsumer(peerComponent, new TestStub(lwpc2OID, lwpc2), requestSpec1, allocationA);
        req_025_Util.changeWorkerStatusToAllocatedForBroker(peerComponent, lwpc2OID, wmAOID, workerSpecA, requestSpec1);
        workerID = createWorkerDeploymentID(workerSpecA, "workerAPublicKey");
        AcceptanceTestUtil.publishTestObject(peerComponent, workerID, workerA, Worker.class);
        EasyMock.reset(loggerMock);
        loggerMock.warn("The consumer [" + lwpc1OID + "] disposed the worker [" + wmAOID.getServiceID() + "], that is not allocated for him. This disposal was ignored.");
        replayActiveMocks();
        AcceptanceTestUtil.setExecutionContext(peerComponent, lwpOD, lwpc1OID);
        lwp.disposeWorker(wmAOID.getServiceID());
        verifyActiveMocks();
        resetActiveMocks();
        String workerBPublicKey = "workerBPublicKey";
        DeploymentID wmBOID = req_019_Util.notifyWorkerRecovery(peerComponent, workerSpecB, workerBPublicKey);
        RequestSpec requestSpec2 = new RequestSpec(0, new JobSpec("label"), 2, "", 1, 0, 0);
        req_025_Util.changeWorkerStatusToIdle(peerComponent, wmBOID);
        WorkerAllocation allocationB = new WorkerAllocation(wmBOID);
        String remoteClientPublicKey = "remoteClientPublicKey";
        DeploymentID remoteClientID = new DeploymentID(new ContainerID("remoteClient", "peerServer", PeerConstants.MODULE_NAME, remoteClientPublicKey), "rwp");
        remoteClientID.setPublicKey(remoteClientPublicKey);
        RemoteWorkerProviderClient rwpc = req_011_Util.requestForRemoteClient(peerComponent, remoteClientID, requestSpec2, 0, allocationB);
        req_025_Util.changeWorkerStatusToAllocatedForPeer(peerComponent, rwpc, wmBOID, workerSpecB, remoteClientID);
        Worker workerB = EasyMock.createMock(Worker.class);
        workerID = createWorkerDeploymentID(workerSpecB, workerBPublicKey);
        AcceptanceTestUtil.publishTestObject(peerComponent, workerID, workerB, Worker.class);
        EasyMock.reset(loggerMock);
        peerComponent.setLogger(loggerMock);
        loggerMock.warn("The consumer [" + lwpc1OID + "] disposed the worker [" + wmBOID.getServiceID() + "], that is not allocated for him. This disposal was ignored.");
        replayActiveMocks();
        AcceptanceTestUtil.setExecutionContext(peerComponent, lwpOD, lwpc1OID);
        lwp.disposeWorker(wmBOID.getServiceID());
        verifyActiveMocks();
        resetActiveMocks();
        DeploymentID dsID = new DeploymentID(new ContainerID("magoDosNos", "sweetleaf.lab", DiscoveryServiceConstants.MODULE_NAME), DiscoveryServiceConstants.DS_OBJECT_NAME);
        req_020_Util.notifyDiscoveryServiceRecovery(peerComponent, dsID);
        String myGrid3PublicKey = "myGrid3PublicKey";
        DeploymentID lwpc3OID = req_108_Util.login(peerComponent, user3, myGrid3PublicKey);
        LocalWorkerProviderClient lwpc3 = (LocalWorkerProviderClient) AcceptanceTestUtil.getBoundObject(lwpc3OID);
        RequestSpec remoteRequestSpec = new RequestSpec(0, new JobSpec("label"), 3, "mem > 256", 1, 0, 0);
        req_011_Util.requestForLocalConsumer(peerComponent, new TestStub(lwpc3OID, lwpc3), remoteRequestSpec);
        WorkerSpec remoteWorkerSpec = workerAcceptanceUtil.createWorkerSpec("U1", "S2");
        remoteWorkerSpec.putAttribute(WorkerSpec.ATT_MEM, "512");
        RemoteWorkerProvider rwp = EasyMock.createMock(RemoteWorkerProvider.class);
        String rwpPublicKey = "rwpPublicKey";
        DeploymentID rwpOID = new DeploymentID(new ContainerID("rwpUser", "rwpServer", PeerConstants.MODULE_NAME, rwpPublicKey), PeerConstants.REMOTE_ACCESS_OBJECT_NAME);
        replayActiveMocks();
        AcceptanceTestUtil.publishTestObject(peerComponent, rwpOID, rwp, RemoteWorkerProvider.class);
        DeploymentID rwmOID = req_018_Util.receiveRemoteWorker(peerComponent, rwp, rwpOID, remoteWorkerSpec, "remoteWorkerPublicKey", myGrid3PublicKey).getDeploymentID();
        verifyActiveMocks();
        resetActiveMocks();
        ObjectDeployment lwpc3OD = new ObjectDeployment(peerComponent.getContainer(), lwpc3OID, AcceptanceTestUtil.getBoundObject(lwpc3OID));
        TestStub workerStub = req_025_Util.changeWorkerStatusToAllocatedForBroker(peerComponent, lwpc3OD, peerAcceptanceUtil.getRemoteWorkerManagementClientDeployment(), rwmOID, remoteWorkerSpec, remoteRequestSpec);
        AcceptanceTestUtil.publishTestObject(peerComponent, workerStub.getDeploymentID(), workerStub.getObject(), Worker.class, false);
        loggerMock.warn("The consumer [" + lwpc1OID + "] disposed the worker [" + workerStub.getDeploymentID().getServiceID() + "], that is not allocated for him. This disposal was ignored.");
        replayActiveMocks();
        AcceptanceTestUtil.setExecutionContext(peerComponent, lwpOD, myGrid1PubKey);
        lwp.disposeWorker(workerStub.getDeploymentID().getServiceID());
        verifyActiveMocks();
        resetActiveMocks();
        RemoteWorkerManagement remoteWorkerC = getMock(NOT_NICE, RemoteWorkerManagement.class);
        String workerCPublicKey = "workerCPublicKey";
        workerID = createWorkerDeploymentID(workerSpecC, workerCPublicKey);
        AcceptanceTestUtil.publishTestObject(peerComponent, workerID, remoteWorkerC, RemoteWorkerManagement.class);
        loggerMock.warn("Ignoring an unknown remote consumer which disposed a worker. Remote consumer public key: " + unknownPublicKey);
        replayActiveMocks();
        rwp = peerAcceptanceUtil.getRemoteWorkerProviderProxy();
        ObjectDeployment rwpOD = peerAcceptanceUtil.getRemoteWorkerProviderDeployment();
        AcceptanceTestUtil.setExecutionContext(peerComponent, lwpOD, unknownPublicKey);
        rwp.disposeWorker(workerID.getServiceID());
        verifyActiveMocks();
        resetActiveMocks();
        DeploymentID wmCOID = req_019_Util.notifyWorkerRecovery(peerComponent, workerSpecC, workerCPublicKey);
        req_025_Util.changeWorkerStatusToIdleWithAdvert(peerComponent, workerSpecC, wmCOID, dsID);
        RequestSpec requestSpec4 = new RequestSpec(0, new JobSpec("label"), 4, "", 1, 0, 0);
        String rwpcPubKey = "rwpcPubKey";
        DeploymentID rwpcOID = new DeploymentID(new ContainerID("rwpcUser", "rwpcServer", PeerConstants.MODULE_NAME, rwpcPubKey), "RWPC");
        WorkerAllocation allocationC = new WorkerAllocation(wmCOID);
        rwpc = req_011_Util.requestForRemoteClient(peerComponent, rwpcOID, requestSpec4, Req_011_Util.DO_NOT_LOAD_SUBCOMMUNITIES, allocationC);
        req_025_Util.changeWorkerStatusToAllocatedForPeer(peerComponent, rwpc, wmCOID, workerSpecC, rwpcOID);
        resetActiveMocks();
        peerComponent.setLogger(loggerMock);
        loggerMock.warn("The remote consumer [" + rwpcOID + "] disposed a null worker. This dispose was ignored.");
        replayActiveMocks();
        AcceptanceTestUtil.setExecutionContext(peerComponent, rwpOD, rwpcPubKey);
        rwp.disposeWorker(null);
        verifyActiveMocks();
        resetActiveMocks();
        RemoteWorkerManagement remoteWorkerE = getMock(NOT_NICE, RemoteWorkerManagement.class);
        String workerEPublicKey = "workerEPublicKey";
        workerID = createWorkerDeploymentID(workerAcceptanceUtil.createWorkerSpec("unknown", "unknown"), workerEPublicKey);
        AcceptanceTestUtil.publishTestObject(peerComponent, workerID, remoteWorkerE, RemoteWorkerManagement.class);
        loggerMock.warn("The remote consumer [" + rwpcOID + "] disposed a unknown worker. This disposal was ignored.");
        replayActiveMocks();
        rwp.disposeWorker(workerID.getServiceID());
        verifyActiveMocks();
        resetActiveMocks();
        RemoteWorkerManagement remoteWorkerD = getMock(NOT_NICE, RemoteWorkerManagement.class);
        String workerDPublicKey = "workerDPublicKey";
        workerID = createWorkerDeploymentID(workerSpecD, workerDPublicKey);
        AcceptanceTestUtil.publishTestObject(peerComponent, workerID, remoteWorkerD, RemoteWorkerManagement.class);
        loggerMock.warn("The remote consumer [" + rwpcOID + "] disposed a unknown worker. This disposal was ignored.");
        replayActiveMocks();
        rwp.disposeWorker(workerID.getServiceID());
        verifyActiveMocks();
        resetActiveMocks();
        DeploymentID wmDOID = req_019_Util.notifyWorkerRecovery(peerComponent, workerSpecD, workerDPublicKey);
        loggerMock.warn("The remote consumer [" + rwpcOID + "] disposed the worker [" + workerID.getServiceID() + "], " + "that is not allocated for him. This disposal was ignored.");
        replayActiveMocks();
        AcceptanceTestUtil.setExecutionContext(peerComponent, rwpOD, rwpcPubKey);
        rwp.disposeWorker(workerID.getServiceID());
        verifyActiveMocks();
        resetActiveMocks();
        req_025_Util.changeWorkerStatusToIdleWithAdvert(peerComponent, workerSpecD, wmDOID, dsID);
        AcceptanceTestUtil.publishTestObject(peerComponent, workerID, remoteWorkerD, RemoteWorkerManagement.class);
        loggerMock.warn("The remote consumer [" + rwpcOID + "] disposed the worker [" + workerID.getServiceID() + "], " + "that is not allocated for him. This disposal was ignored.");
        replayActiveMocks();
        AcceptanceTestUtil.setExecutionContext(peerComponent, rwpOD, rwpcPubKey);
        rwp.disposeWorker(workerID.getServiceID());
        verifyActiveMocks();
        resetActiveMocks();
        String workerFPublicKey = "workerFPublicKey";
        DeploymentID wmFOID = req_019_Util.notifyWorkerRecovery(peerComponent, workerSpecF, workerFPublicKey);
        req_025_Util.changeWorkerStatusToIdleWithAdvert(peerComponent, workerSpecF, wmFOID, dsID);
        String myGrid4PubKey = "myGrid4PubKey";
        DeploymentID lwpc4OID = req_108_Util.login(peerComponent, user4, myGrid4PubKey);
        LocalWorkerProviderClient lwpc4 = (LocalWorkerProviderClient) AcceptanceTestUtil.getBoundObject(lwpc4OID);
        WorkerAllocation allocationF = new WorkerAllocation(wmFOID);
        RequestSpec requestSpec5 = new RequestSpec(0, new JobSpec("label"), 5, "", 1, 0, 0);
        req_011_Util.requestForLocalConsumer(peerComponent, new TestStub(lwpc4OID, lwpc4), requestSpec5, allocationF);
        req_025_Util.changeWorkerStatusToAllocatedForBroker(peerComponent, lwpc4OID, wmFOID, workerSpecF, requestSpec5);
        RemoteWorkerManagement remoteWorkerF = getMock(NOT_NICE, RemoteWorkerManagement.class);
        workerID = createWorkerDeploymentID(workerSpecF, workerFPublicKey);
        AcceptanceTestUtil.publishTestObject(peerComponent, workerID, remoteWorkerF, RemoteWorkerManagement.class);
        loggerMock.warn("The remote consumer [" + rwpcOID + "] disposed the worker [" + workerID.getServiceID() + "], " + "that is not allocated for him. This disposal was ignored.");
        replayActiveMocks();
        AcceptanceTestUtil.setExecutionContext(peerComponent, rwpOD, rwpcPubKey);
        rwp.disposeWorker(workerID.getServiceID());
        verifyActiveMocks();
        resetActiveMocks();
        RemoteWorkerManagement remoteWorkerB = getMock(NOT_NICE, RemoteWorkerManagement.class);
        workerID = createWorkerDeploymentID(workerSpecB, workerBPublicKey);
        AcceptanceTestUtil.publishTestObject(peerComponent, workerID, remoteWorkerB, RemoteWorkerManagement.class);
        loggerMock.warn("The remote consumer [" + rwpcOID + "] disposed the worker [" + workerID.getServiceID() + "], " + "that is not allocated for him. This disposal was ignored.");
        replayActiveMocks();
        rwp.disposeWorker(workerID.getServiceID());
        verifyActiveMocks();
        resetActiveMocks();
        RemoteWorkerManagement remoteWorkerR = getMock(NOT_NICE, RemoteWorkerManagement.class);
        workerID = createWorkerDeploymentID(workerAcceptanceUtil.createWorkerSpec("unknown", "unknown"), "remoteWorkerPublicKey");
        AcceptanceTestUtil.publishTestObject(peerComponent, workerID, remoteWorkerR, RemoteWorkerManagement.class);
        loggerMock.warn("The remote consumer [" + rwpcOID + "] disposed the worker [" + workerID.getServiceID() + "], " + "that is not allocated for him. This disposal was ignored.");
        replayActiveMocks();
        rwp.disposeWorker(workerID.getServiceID());
        verifyActiveMocks();
        resetActiveMocks();
    }
}
