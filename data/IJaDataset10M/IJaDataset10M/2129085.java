package org.ourgrid.acceptance.peer;

import org.easymock.classextension.EasyMock;
import org.junit.Test;
import org.ourgrid.acceptance.util.WorkerAcceptanceUtil;
import org.ourgrid.acceptance.util.peer.Req_010_Util;
import org.ourgrid.acceptance.util.peer.Req_011_Util;
import org.ourgrid.acceptance.util.peer.Req_018_Util;
import org.ourgrid.acceptance.util.peer.Req_020_Util;
import org.ourgrid.acceptance.util.peer.Req_037_Util;
import org.ourgrid.acceptance.util.peer.Req_101_Util;
import org.ourgrid.acceptance.util.peer.Req_108_Util;
import org.ourgrid.common.interfaces.LocalWorkerProviderClient;
import org.ourgrid.common.interfaces.RemoteWorkerProvider;
import org.ourgrid.common.interfaces.control.PeerControl;
import org.ourgrid.common.interfaces.control.PeerControlClient;
import org.ourgrid.common.interfaces.status.RemoteWorkerInfo;
import org.ourgrid.common.interfaces.to.RequestSpec;
import org.ourgrid.common.spec.job.JobSpec;
import org.ourgrid.common.spec.worker.WorkerSpec;
import org.ourgrid.deployer.xmpp.XMPPAccount;
import org.ourgrid.discoveryservice.DiscoveryServiceConstants;
import org.ourgrid.peer.PeerComponent;
import org.ourgrid.reqtrace.ReqTest;
import br.edu.ufcg.lsd.commune.CommuneRuntimeException;
import br.edu.ufcg.lsd.commune.container.ObjectDeployment;
import br.edu.ufcg.lsd.commune.functionaltests.util.TestStub;
import br.edu.ufcg.lsd.commune.identification.ContainerID;
import br.edu.ufcg.lsd.commune.identification.DeploymentID;
import br.edu.ufcg.lsd.commune.test.AcceptanceTestUtil;

@ReqTest(reqs = "REQ037")
public class Req_037_Test extends PeerAcceptanceTestCase {

    private PeerComponent component;

    private WorkerAcceptanceUtil workerAcceptanceUtil = new WorkerAcceptanceUtil(getComponentContext());

    private Req_010_Util req_010_Util = new Req_010_Util(getComponentContext());

    private Req_011_Util req_011_Util = new Req_011_Util(getComponentContext());

    private Req_018_Util req_018_Util = new Req_018_Util(getComponentContext());

    private Req_020_Util req_020_Util = new Req_020_Util(getComponentContext());

    private Req_037_Util req_037_Util = new Req_037_Util(getComponentContext());

    private Req_101_Util req_101_Util = new Req_101_Util(getComponentContext());

    private Req_108_Util req_108_Util = new Req_108_Util(getComponentContext());

    /**
	 * Verifies a peer without remote Workers. It must return an empty list of remote Workers.
	 */
    @ReqTest(test = "AT-037.1", reqs = "REQ037")
    @Test
    public void test_AT_037_1_PeerWithoutRemoteWorkers() throws Exception {
        component = req_010_Util.startPeer();
        req_037_Util.getRemoteWorkersStatus();
    }

    /**
	 * Verifies a peer with two remote Workers from distinct providers.
	 * @throws Exception
	 */
    @ReqTest(test = "AT-037.2", reqs = "REQ037")
    @Test
    public void test_AT_037_2_PeerWithRemoteWorkers() throws Exception {
        XMPPAccount user = req_101_Util.createLocalUser("user011", "server011", "011011");
        component = req_010_Util.startPeer();
        DeploymentID dsID = new DeploymentID(new ContainerID("magoDosNos", "sweetleaf.lab", DiscoveryServiceConstants.MODULE_NAME), DiscoveryServiceConstants.DS_OBJECT_NAME);
        req_020_Util.notifyDiscoveryServiceRecovery(component, dsID);
        String mygridPubKey = "myGridPublicKey";
        PeerControl peerControl = peerAcceptanceUtil.getPeerControl();
        ObjectDeployment pcOD = peerAcceptanceUtil.getPeerControlDeployment();
        PeerControlClient peerControlClient = EasyMock.createMock(PeerControlClient.class);
        DeploymentID pccID = new DeploymentID(new ContainerID("pcc", "broker", "broker"), mygridPubKey);
        AcceptanceTestUtil.publishTestObject(component, pccID, peerControlClient, PeerControlClient.class);
        AcceptanceTestUtil.setExecutionContext(component, pcOD, pccID);
        try {
            peerControl.addUser(peerControlClient, user.getUsername() + "@" + user.getServerAddress(), user.getUserpassword());
        } catch (CommuneRuntimeException e) {
        }
        DeploymentID lwpcOID = req_108_Util.login(component, user, mygridPubKey);
        LocalWorkerProviderClient lwpc = (LocalWorkerProviderClient) AcceptanceTestUtil.getBoundObject(lwpcOID);
        int requestID = 1;
        RequestSpec requestSpec = new RequestSpec(0, new JobSpec("label"), requestID, "os = windows AND mem > 256", 2, 0, 0);
        req_011_Util.requestForLocalConsumer(component, new TestStub(lwpcOID, lwpc), requestSpec);
        WorkerSpec workerSpec1 = workerAcceptanceUtil.createWorkerSpec("U1", "S1");
        workerSpec1.putAttribute(WorkerSpec.ATT_OS, "windows");
        workerSpec1.putAttribute(WorkerSpec.ATT_MEM, "512");
        TestStub rwp1Stub = req_020_Util.receiveRemoteWorkerProvider(component, requestSpec, "rwp1User", "rwp1Server", "rwp1PublicKey", workerSpec1);
        RemoteWorkerProvider rwp1 = (RemoteWorkerProvider) rwp1Stub.getObject();
        DeploymentID rwp1ID = rwp1Stub.getDeploymentID();
        req_018_Util.receiveRemoteWorker(component, rwp1, rwp1ID, workerSpec1, "worker1PK", mygridPubKey);
        WorkerSpec workerSpec2 = workerAcceptanceUtil.createWorkerSpec("U2", "S2");
        workerSpec2.putAttribute(WorkerSpec.ATT_OS, "windows");
        workerSpec2.putAttribute(WorkerSpec.ATT_MEM, "512");
        TestStub rwp2Stub = req_020_Util.receiveRemoteWorkerProvider(component, requestSpec, "rwp2User", "rwp2Server", "rwp2PublicKey", workerSpec2);
        RemoteWorkerProvider rwp2 = (RemoteWorkerProvider) rwp2Stub.getObject();
        DeploymentID rwp2ID = rwp2Stub.getDeploymentID();
        req_018_Util.receiveRemoteWorker(component, rwp2, rwp2ID, workerSpec2, "worker2PK", mygridPubKey);
        RemoteWorkerInfo remoteWorkerInfo1 = new RemoteWorkerInfo(workerSpec1, rwp1ID.getServiceID(), lwpcOID.getServiceID());
        RemoteWorkerInfo remoteWorkerInfo2 = new RemoteWorkerInfo(workerSpec2, rwp2ID.getServiceID(), lwpcOID.getServiceID());
        req_037_Util.getRemoteWorkersStatus(remoteWorkerInfo1, remoteWorkerInfo2);
    }
}
