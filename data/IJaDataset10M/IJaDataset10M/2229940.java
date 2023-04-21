package org.ourgrid.acceptance.discoveryservice;

import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import org.ourgrid.acceptance.util.discoveryservice.Req_502_Util;
import org.ourgrid.acceptance.util.discoveryservice.Req_505_Util;
import org.ourgrid.acceptance.util.discoveryservice.Req_506_Util;
import org.ourgrid.acceptance.util.discoveryservice.Req_508_Util;
import org.ourgrid.acceptance.util.discoveryservice.Req_510_Util;
import org.ourgrid.common.statistics.beans.ds.DS_PeerStatusChange;
import org.ourgrid.common.statistics.beans.status.PeerStatus;
import org.ourgrid.discoveryservice.DiscoveryServiceComponent;
import org.ourgrid.peer.PeerConstants;
import org.ourgrid.reqtrace.ReqTest;
import br.edu.ufcg.lsd.commune.functionaltests.util.TestStub;
import br.edu.ufcg.lsd.commune.identification.ServiceID;

public class Req_510_Test extends DiscoveryServiceAcceptanceTestCase {

    private Req_502_Util req_502_Util = new Req_502_Util(super.getComponentContext());

    private Req_505_Util req_505_Util = new Req_505_Util(super.getComponentContext());

    private Req_506_Util req_506_Util = new Req_506_Util(super.getComponentContext());

    private Req_508_Util req_508_Util = new Req_508_Util(super.getComponentContext());

    private Req_510_Util req_510_Util = new Req_510_Util(super.getComponentContext());

    /**
	 * Create a DS;
	 * Start a DS with the correct public key;
	 * Call the joinCommunity message with the following parameters:
     *     o WorkerProvider: username = test2, servername = servertest2 and service = REMOTE_WORKERPROVIDER;
     *     o PeerStatusProvider: username = test2, servername = servertest2 and service = PEER;
	 * Call the joinCommunity message with the following parameters:
     *     o WorkerProvider: username = test, servername = servertest and service = REMOTE_WORKERPROVIDER;
     *     o PeerStatusProvider: username = test, servername = servertest and service = PEER;
	 * Call the joinCommunity message with the following parameters:
     *     o WorkerProvider: username = test3, servername = servertest3 and service = REMOTE_WORKERPROVIDER;
     *     o PeerStatusProvider: username = test3, servername = servertest3 and service = PEER;
	 * Call the leaveCommunity message with a client with the following attributes:
     *     o username = test, servername = servertest;
	 * Call the doNotifyFailure message with the deploymentID:
     *     o username = test2, servername = servertest2 and service = DS_CLIENT and publickey = dsClientPK;
	 * Call the joinCommunity message with the following parameters:
     *     o WorkerProvider: username = test, servername = servertest and service = REMOTE_WORKERPROVIDER;
     *     o PeerStatusProvider: username = test, servername = servertest and service = PEER;
     * Call the getPeerStatusChangeHistory message;
     * Verify if the list contains the following ordered data:
     *     o  Address: test@servertest Status: UP
     *     o  Address: test@servertest Status: DOWN
     *     o  Address: test@servertest Status: UP
     *     o  Address: test2@servertest2 Status: UP
     *     o  Address: test2@servertest2 Status: DOWN
     *     o  Address: test3@servertest3 Status: UP
	 *
     */
    @ReqTest(test = "AT-510.1", reqs = "")
    @Test
    public void test_AT_510_queryNormalPeerStatusHistoryChange() throws Exception {
        DiscoveryServiceComponent component = req_502_Util.startDiscoveryService();
        TestStub dscTestStub1 = req_505_Util.createDiscoveryServiceClient(new ServiceID("test1", "servertest1", "PEER", PeerConstants.DS_CLIENT));
        req_505_Util.getRemoteWorkerProviders(component, dscTestStub1);
        Thread.sleep(10);
        TestStub dscTestStub2 = req_505_Util.createDiscoveryServiceClient(new ServiceID("test2", "servertest2", "PEER", PeerConstants.DS_CLIENT));
        req_505_Util.getRemoteWorkerProviders(component, dscTestStub2);
        Thread.sleep(10);
        TestStub dscTestStub3 = req_505_Util.createDiscoveryServiceClient(new ServiceID("test3", "servertest3", "PEER", PeerConstants.DS_CLIENT));
        req_505_Util.getRemoteWorkerProviders(component, dscTestStub3);
        Thread.sleep(10);
        req_506_Util.leaveCommunity(component, dscTestStub2);
        Thread.sleep(10);
        req_508_Util.doNotifyFailure(component, dscTestStub1);
        Thread.sleep(10);
        req_505_Util.getRemoteWorkerProviders(component, dscTestStub1, true);
        Thread.sleep(10);
        List<DS_PeerStatusChange> historyList = new ArrayList<DS_PeerStatusChange>();
        DS_PeerStatusChange change = new DS_PeerStatusChange();
        change.setPeerAddress("test1@servertest1");
        change.setCurrentStatus(PeerStatus.UP);
        historyList.add(change);
        change = new DS_PeerStatusChange();
        change.setPeerAddress("test2@servertest2");
        change.setCurrentStatus(PeerStatus.UP);
        historyList.add(change);
        change = new DS_PeerStatusChange();
        change.setPeerAddress("test3@servertest3");
        change.setCurrentStatus(PeerStatus.UP);
        historyList.add(change);
        change = new DS_PeerStatusChange();
        change.setPeerAddress("test2@servertest2");
        change.setCurrentStatus(PeerStatus.DOWN);
        historyList.add(change);
        change = new DS_PeerStatusChange();
        change.setPeerAddress("test1@servertest1");
        change.setCurrentStatus(PeerStatus.DOWN);
        historyList.add(change);
        change = new DS_PeerStatusChange();
        change.setPeerAddress("test1@servertest1");
        change.setCurrentStatus(PeerStatus.UP);
        historyList.add(change);
        req_510_Util.getPeerStatusChangeHistory(component, historyList);
    }

    /**
	 * Create a DS;
	 * Start a DS with the correct public key;
	 * Call the joinCommunity message with the following parameters:
     *     o WorkerProvider: username = test2, servername = servertest2 and service = REMOTE_WORKERPROVIDER;
     *     o PeerStatusProvider: username = test2, servername = servertest2 and service = PEER;
	 * Call the joinCommunity message with the following parameters:
     *     o WorkerProvider: username = test, servername = servertest and service = REMOTE_WORKERPROVIDER;
     *     o PeerStatusProvider: username = test, servername = servertest and service = PEER;
	 * Call the joinCommunity message with the following parameters:
     *     o WorkerProvider: username = test3, servername = servertest3 and service = REMOTE_WORKERPROVIDER;
     *     o PeerStatusProvider: username = test3, servername = servertest3 and service = PEER;
	 * Call the joinCommunity message with the following parameters:
     *     o WorkerProvider: username = test4, servername = servertest4 and service = REMOTE_WORKERPROVIDER;
     *     o PeerStatusProvider: username = test4, servername = servertest4 and service = PEER;
	 * Call the leaveCommunity message with a client with the following attributes:
     *     o username = test, servername = servertest;
	 * Call the leaveCommunity message with a client with the following attributes:
     *     o username = test, servername = servertest;
	 * Call the doNotifyFailure message with the deploymentID:
     *     o username = test2, servername = servertest2 and service = DS_CLIENT and publickey = dsClientPK;
	 * Call the joinCommunity message with the following parameters:
     *     o WorkerProvider: username = test2, servername = servertest2 and service = REMOTE_WORKERPROVIDER;
     *     o PeerStatusProvider: username = test2, servername = servertest2 and service = PEER;
	 * Call the joinCommunity message with the following parameters:
     *     o WorkerProvider: username = test2, servername = servertest2 and service = REMOTE_WORKERPROVIDER;
     *     o PeerStatusProvider: username = test2, servername = servertest2 and service = PEER;
	 * Call the joinCommunity message with the following parameters:
     *     o WorkerProvider: username = test5, servername = servertest5 and service = REMOTE_WORKERPROVIDER;
     *     o PeerStatusProvider: username = test5, servername = servertest5 and service = PEER;
	 * Call the doNotifyFailure message with the deploymentID:
     *     o username = test5, servername = servertest5 and service = DS_CLIENT and publickey = dsClientPK;
	 * Verify if the list contains the following ordered data:
     *     o Address: test@servertest Status: UP
     *     o Address: test@servertest Status: DOWN
     *     o Address: test2@servertest2 Status: UP
     *     o Address: test2@servertest2 Status: DOWN
     *     o Address: test2@servertest2 Status: UP
     *     o Address: test3@servertest3 Status: UP
     *     o Address: test4@servertest4 Status: UP
     *     o Address: test5@servertest5 Status: UP
     *     o Address: test5@servertest5 Status: DOWN
	 *
	 */
    @ReqTest(test = "AT-510.2", reqs = "")
    @Test
    public void test_AT_510_queryIlegalPeerStatusHistoryChange() throws Exception {
        DiscoveryServiceComponent component = req_502_Util.startDiscoveryService();
        TestStub dscTestStub2 = req_505_Util.createDiscoveryServiceClient(new ServiceID("test2", "servertest2", "PEER", PeerConstants.DS_CLIENT));
        req_505_Util.getRemoteWorkerProviders(component, dscTestStub2);
        Thread.sleep(10);
        TestStub dscTestStub1 = req_505_Util.createDiscoveryServiceClient(new ServiceID("test1", "servertest1", "PEER", PeerConstants.DS_CLIENT));
        req_505_Util.getRemoteWorkerProviders(component, dscTestStub1);
        Thread.sleep(10);
        TestStub dscTestStub3 = req_505_Util.createDiscoveryServiceClient(new ServiceID("test3", "servertest3", "PEER", PeerConstants.DS_CLIENT));
        req_505_Util.getRemoteWorkerProviders(component, dscTestStub3);
        Thread.sleep(10);
        TestStub dscTestStub4 = req_505_Util.createDiscoveryServiceClient(new ServiceID("test4", "servertest4", "PEER", PeerConstants.DS_CLIENT));
        req_505_Util.getRemoteWorkerProviders(component, dscTestStub4);
        Thread.sleep(10);
        req_506_Util.leaveCommunity(component, dscTestStub1);
        Thread.sleep(10);
        req_506_Util.leaveCommunity(component, dscTestStub1, false);
        Thread.sleep(10);
        req_508_Util.doNotifyFailure(component, dscTestStub2);
        Thread.sleep(10);
        req_505_Util.getRemoteWorkerProviders(component, dscTestStub2);
        Thread.sleep(10);
        req_505_Util.getRemoteWorkerProviders(component, dscTestStub2, true);
        Thread.sleep(10);
        TestStub dscTestStub5 = req_505_Util.createDiscoveryServiceClient(new ServiceID("test5", "servertest5", "PEER", PeerConstants.DS_CLIENT));
        req_505_Util.getRemoteWorkerProviders(component, dscTestStub5);
        Thread.sleep(10);
        req_508_Util.doNotifyFailure(component, dscTestStub5);
        Thread.sleep(10);
        List<DS_PeerStatusChange> historyList = new ArrayList<DS_PeerStatusChange>();
        DS_PeerStatusChange change = new DS_PeerStatusChange();
        change.setPeerAddress("test2@servertest2");
        change.setCurrentStatus(PeerStatus.UP);
        historyList.add(change);
        change = new DS_PeerStatusChange();
        change.setPeerAddress("test1@servertest1");
        change.setCurrentStatus(PeerStatus.UP);
        historyList.add(change);
        change = new DS_PeerStatusChange();
        change.setPeerAddress("test3@servertest3");
        change.setCurrentStatus(PeerStatus.UP);
        historyList.add(change);
        change = new DS_PeerStatusChange();
        change.setPeerAddress("test4@servertest4");
        change.setCurrentStatus(PeerStatus.UP);
        historyList.add(change);
        change = new DS_PeerStatusChange();
        change.setPeerAddress("test1@servertest1");
        change.setCurrentStatus(PeerStatus.DOWN);
        historyList.add(change);
        change = new DS_PeerStatusChange();
        change.setPeerAddress("test2@servertest2");
        change.setCurrentStatus(PeerStatus.DOWN);
        historyList.add(change);
        change = new DS_PeerStatusChange();
        change.setPeerAddress("test2@servertest2");
        change.setCurrentStatus(PeerStatus.UP);
        historyList.add(change);
        change = new DS_PeerStatusChange();
        change.setPeerAddress("test5@servertest5");
        change.setCurrentStatus(PeerStatus.UP);
        historyList.add(change);
        change = new DS_PeerStatusChange();
        change.setPeerAddress("test5@servertest5");
        change.setCurrentStatus(PeerStatus.DOWN);
        historyList.add(change);
        req_510_Util.getPeerStatusChangeHistory(component, historyList);
    }
}
