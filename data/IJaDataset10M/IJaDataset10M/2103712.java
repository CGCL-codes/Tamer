package org.ourgrid.acceptance.peer;

import java.io.File;
import java.util.List;
import org.easymock.classextension.EasyMock;
import org.junit.After;
import org.junit.Test;
import org.ourgrid.acceptance.util.WorkerAcceptanceUtil;
import org.ourgrid.acceptance.util.WorkerAllocation;
import org.ourgrid.acceptance.util.peer.Req_010_Util;
import org.ourgrid.acceptance.util.peer.Req_011_Util;
import org.ourgrid.acceptance.util.peer.Req_019_Util;
import org.ourgrid.acceptance.util.peer.Req_025_Util;
import org.ourgrid.acceptance.util.peer.Req_036_Util;
import org.ourgrid.acceptance.util.peer.Req_101_Util;
import org.ourgrid.acceptance.util.peer.Req_108_Util;
import org.ourgrid.common.interfaces.LocalWorkerProviderClient;
import org.ourgrid.common.interfaces.control.PeerControl;
import org.ourgrid.common.interfaces.control.PeerControlClient;
import org.ourgrid.common.interfaces.to.LocalWorkerState;
import org.ourgrid.common.interfaces.to.RequestSpec;
import org.ourgrid.common.interfaces.to.WorkerInfo;
import org.ourgrid.common.spec.job.JobSpec;
import org.ourgrid.common.spec.worker.WorkerSpec;
import org.ourgrid.deployer.xmpp.XMPPAccount;
import org.ourgrid.peer.PeerComponent;
import org.ourgrid.peer.PeerConfiguration;
import org.ourgrid.peer.PeerConstants;
import org.ourgrid.reqtrace.ReqTest;
import br.edu.ufcg.lsd.commune.CommuneRuntimeException;
import br.edu.ufcg.lsd.commune.container.ObjectDeployment;
import br.edu.ufcg.lsd.commune.functionaltests.util.TestStub;
import br.edu.ufcg.lsd.commune.identification.ContainerID;
import br.edu.ufcg.lsd.commune.identification.DeploymentID;
import br.edu.ufcg.lsd.commune.identification.ServiceID;
import br.edu.ufcg.lsd.commune.test.AcceptanceTestUtil;

public class AT_0010 extends PeerAcceptanceTestCase {

    private PeerComponent component;

    private WorkerAcceptanceUtil workerAcceptanceUtil = new WorkerAcceptanceUtil(getComponentContext());

    private Req_010_Util req_010_Util = new Req_010_Util(getComponentContext());

    private Req_011_Util req_011_Util = new Req_011_Util(getComponentContext());

    private Req_019_Util req_019_Util = new Req_019_Util(getComponentContext());

    private Req_025_Util req_025_Util = new Req_025_Util(getComponentContext());

    private Req_036_Util req_036_Util = new Req_036_Util(getComponentContext());

    private Req_101_Util req_101_Util = new Req_101_Util(getComponentContext());

    private Req_108_Util req_108_Util = new Req_108_Util(getComponentContext());

    @After
    public void tearDown() throws Exception {
        super.tearDown();
        File trustFile = new File(PeerConfiguration.TRUSTY_COMMUNITIES_FILENAME);
        if (trustFile.exists()) {
            trustFile.delete();
        }
    }

    /**
	 * Verifies a peer with in use local Workers. The in use workers must become first in result.
	 */
    @ReqTest(test = "AT-0010", reqs = "")
    @Test
    public void test_AT_0010_LocalWorkers_in_Local_Allocation() throws Exception {
        copyTrustFile("011_blank.xml");
        XMPPAccount user1 = req_101_Util.createLocalUser("user011-1", "server011", "011011");
        component = req_010_Util.startPeer();
        WorkerSpec workerSpecA = workerAcceptanceUtil.createWorkerSpec("U1", "S1");
        WorkerSpec workerSpecB = workerAcceptanceUtil.createWorkerSpec("U2", "S1");
        WorkerSpec workerSpecC = workerAcceptanceUtil.createWorkerSpec("U3", "S1");
        WorkerSpec workerSpecD = workerAcceptanceUtil.createWorkerSpec("U4", "S1");
        WorkerSpec workerSpecE = workerAcceptanceUtil.createWorkerSpec("U5", "S1");
        WorkerSpec workerSpecF = workerAcceptanceUtil.createWorkerSpec("U6", "S1");
        List<WorkerSpec> workers = AcceptanceTestUtil.createList(workerSpecA, workerSpecB, workerSpecC, workerSpecD, workerSpecE, workerSpecF);
        req_010_Util.setWorkers(component, workers);
        String workerAPubKey = "publicKeyWA";
        DeploymentID workerAObjID = req_019_Util.notifyWorkerRecovery(component, workerSpecA, workerAPubKey);
        String workerBPubKey = "publicKeyWB";
        DeploymentID workerBObjID = req_019_Util.notifyWorkerRecovery(component, workerSpecB, workerBPubKey);
        String workerCPubKey = "publicKeyWC";
        DeploymentID workerCObjID = req_019_Util.notifyWorkerRecovery(component, workerSpecC, workerCPubKey);
        String workerDPubKey = "publicKeyWD";
        DeploymentID workerDObjID = req_019_Util.notifyWorkerRecovery(component, workerSpecD, workerDPubKey);
        String workerEPubKey = "publicKeyWE";
        req_019_Util.notifyWorkerRecovery(component, workerSpecE, workerEPubKey);
        req_025_Util.changeWorkerStatusToIdle(component, workerAObjID);
        req_025_Util.changeWorkerStatusToIdle(component, workerBObjID);
        req_025_Util.changeWorkerStatusToIdle(component, workerCObjID);
        DeploymentID remoteClientObjectID = new DeploymentID(new ServiceID("rclient", "server", PeerConstants.MODULE_NAME, PeerConstants.REMOTE_ACCESS_OBJECT_NAME));
        String remoteClientPubKey = "remoteClientPubKey";
        remoteClientObjectID.setPublicKey(remoteClientPubKey);
        RequestSpec requestSpec = new RequestSpec(0, new JobSpec("label"), 1, "", 1, 0, 0);
        WorkerAllocation allocationC = new WorkerAllocation(workerCObjID);
        req_011_Util.requestForRemoteClient(component, remoteClientObjectID, requestSpec, 1, allocationC);
        String localUserPubKey = "localUserPubKey";
        PeerControl peerControl = peerAcceptanceUtil.getPeerControl();
        ObjectDeployment pcOD = peerAcceptanceUtil.getPeerControlDeployment();
        PeerControlClient peerControlClient = EasyMock.createMock(PeerControlClient.class);
        DeploymentID pccID = new DeploymentID(new ContainerID("pcc", "broker", "broker"), localUserPubKey);
        AcceptanceTestUtil.publishTestObject(component, pccID, peerControlClient, PeerControlClient.class);
        AcceptanceTestUtil.setExecutionContext(component, pcOD, pccID);
        try {
            peerControl.addUser(peerControlClient, user1.getUsername() + "@" + user1.getServerAddress(), user1.getUserpassword());
        } catch (CommuneRuntimeException e) {
        }
        DeploymentID lwpcOID1 = req_108_Util.login(component, user1, localUserPubKey);
        LocalWorkerProviderClient lwpc = (LocalWorkerProviderClient) AcceptanceTestUtil.getBoundObject(lwpcOID1);
        WorkerAllocation allocationB = new WorkerAllocation(workerBObjID);
        WorkerAllocation allocationA = new WorkerAllocation(workerAObjID);
        RequestSpec requestSpec2 = new RequestSpec(0, new JobSpec("label"), 2, "", 2, 0, 0);
        req_011_Util.requestForLocalConsumer(component, new TestStub(lwpcOID1, lwpc), requestSpec2, allocationB, allocationA);
        req_025_Util.changeWorkerStatusToIdle(component, workerDObjID);
        WorkerInfo workerInfoA = new WorkerInfo(workerSpecA, LocalWorkerState.IN_USE, lwpcOID1.getServiceID());
        WorkerInfo workerInfoB = new WorkerInfo(workerSpecB, LocalWorkerState.IN_USE, lwpcOID1.getServiceID());
        WorkerInfo workerInfoC = new WorkerInfo(workerSpecC, LocalWorkerState.DONATED, remoteClientObjectID.getServiceID());
        WorkerInfo workerInfoD = new WorkerInfo(workerSpecD, LocalWorkerState.IDLE, null);
        WorkerInfo workerInfoE = new WorkerInfo(workerSpecE, LocalWorkerState.OWNER, null);
        WorkerInfo workerInfoF = new WorkerInfo(workerSpecF, LocalWorkerState.CONTACTING, null);
        List<WorkerInfo> localWorkersInfo = AcceptanceTestUtil.createList(workerInfoA, workerInfoB, workerInfoC, workerInfoD, workerInfoE, workerInfoF);
        req_036_Util.getLocalWorkersStatus(localWorkersInfo);
    }
}
