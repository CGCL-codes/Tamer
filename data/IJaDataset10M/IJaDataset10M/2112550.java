package de.fuh.xpairtise.tests.common.network;

import java.io.File;
import java.net.URI;
import java.net.URL;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;
import de.fuh.xpairtise.common.Constants;
import de.fuh.xpairtise.common.SessionRole;
import de.fuh.xpairtise.common.UserState;
import de.fuh.xpairtise.common.XPLog;
import de.fuh.xpairtise.common.network.INetworkFactory;
import de.fuh.xpairtise.common.network.IServerCommandInterface;
import de.fuh.xpairtise.common.network.data.ConnectionRequestReply;
import de.fuh.xpairtise.common.network.data.JoinXPSessionRequestReply;
import de.fuh.xpairtise.common.network.data.LeaveXPSessionRequestReply;
import de.fuh.xpairtise.common.network.data.RemoveXPSessionRequestReply;
import de.fuh.xpairtise.common.replication.IReplicatedListEventListener;
import de.fuh.xpairtise.common.replication.UnexpectedReplicationState;
import de.fuh.xpairtise.common.replication.elements.ReplicatedUser;
import de.fuh.xpairtise.plugin.network.ClientSideCommunicationFactory;
import de.fuh.xpairtise.server.ServerApplication;
import de.fuh.xpairtise.server.XPSessionManager;
import de.fuh.xpairtise.server.network.ServerSideCommunicationFactory;
import de.fuh.xpairtise.tests.util.PrivateAccessor;

public abstract class UserGalleryTestBase extends CommonNetworkTestBase {

    private String xpSessionId;

    private INetworkFactory networkFactory;

    private ClientSideCommunicationFactory clientFactory;

    private DummyUserGalleryController guiUserGalleryController;

    private IServerCommandInterface commandInterface;

    private ServerApplication serverApplication;

    private ReplicatedUser receivedUserObejct = null;

    private static final long TIMEOUT = 500;

    private class DummyUserGalleryController {

        private ArrayBlockingQueue<ReplicatedUser> blockingQueue = new ArrayBlockingQueue<ReplicatedUser>(5);

        private class ReplicationListener implements IReplicatedListEventListener<ReplicatedUser> {

            public void add(ReplicatedUser element) throws UnexpectedReplicationState {
                String userReceived = "Received this User Objekt: " + element.getUserId();
                blockingQueue.add(element);
                XPLog.printDebug(userReceived);
            }

            public void remove(ReplicatedUser element) throws UnexpectedReplicationState {
                throw new RuntimeException("Remove User is not implemented");
            }

            public void update(ReplicatedUser element) throws UnexpectedReplicationState {
                String updateReceived = "Received update for user: " + element.getUserId();
                blockingQueue.add(element);
                XPLog.printDebug(updateReceived);
            }

            public void removeAll() throws UnexpectedReplicationState {
            }
        }

        public DummyUserGalleryController(String id) throws Exception {
            clientFactory.attachToList(id, new ReplicationListener());
        }

        public ReplicatedUser getReceivedObject() throws InterruptedException {
            return blockingQueue.poll(TIMEOUT, TimeUnit.MILLISECONDS);
        }
    }

    protected void setUp() throws Exception {
        URL url = UserGalleryTestBase.class.getResource("activemq");
        URI uri = url.toURI();
        String projectPath = uri.getPath().replace("bin/de/fuh/xpairtise/tests/common/network/activemq", "");
        XPLog.printDebug("Project Path: " + projectPath);
        File userDBFileBack = new File(projectPath + "XPairtiseUserDB.bak");
        if (userDBFileBack.exists()) userDBFileBack.delete();
        File userDBFile = new File(projectPath + "XPairtiseUserDB.xml");
        if (userDBFile.exists()) {
            XPLog.printDebug("user db exists");
            if (userDBFile.renameTo(new File(projectPath + "XPairtiseUserDB.bak"))) XPLog.printDebug("user db backup created"); else XPLog.printDebug("user db backup could NOT be created");
        }
        netWorkInit();
    }

    protected void netWorkInit() throws Exception {
        networkFactory = getNetworkFactory();
        networkFactory.establishServerLink(null, Constants.SERVER_ADDRESS_DEFAULT);
        ServerSideCommunicationFactory.initialize(networkFactory);
        ClientSideCommunicationFactory.initialize(networkFactory);
        clientFactory = ClientSideCommunicationFactory.getInstance();
        commandInterface = clientFactory.getServerCommandInterface();
        serverApplication = new ServerApplication(true, false);
        serverApplication.startListen();
        String channelName = Constants.MASTER_LIST_NAME_USER_GALLERY;
        guiUserGalleryController = new DummyUserGalleryController(channelName);
        Thread.sleep(400);
    }

    protected void tearDown() throws Exception {
        serverApplication.shutdown();
        networkFactory.tearDownServerLink();
        URL url = UserGalleryTestBase.class.getResource("activemq");
        URI uri = url.toURI();
        String projectPath = uri.getPath().replace("bin/de/fuh/xpairtise/tests/common/network/activemq", "");
        File userDBFile = new File(projectPath + "XPairtiseUserDB.xml");
        if ((userDBFile.exists()) && (userDBFile.delete())) XPLog.printDebug("test user db deleted");
        File userDBFileBack = new File(projectPath + "XPairtiseUserDB.bak");
        if (userDBFileBack.exists()) {
            XPLog.printDebug("user db backup file found");
            if (userDBFileBack.renameTo(new File(projectPath + "XPairtiseUserDB.xml"))) XPLog.printDebug("user db restored"); else XPLog.printDebug("user db could NOT be restored");
        }
    }

    public void testUserGallery() throws Exception {
        assertEquals(receivedUserObejct, null);
        ConnectionRequestReply reply = commandInterface.sendConnectRequest("Tester1", "", "", null, false);
        assertEquals(Constants.REQUEST_REPLY_USER_UNKNOWN, reply.getResult());
        commandInterface.sendAddUserRequest("Tester2", "testpw", "testgr", "", "", "", "", "");
        Thread.sleep(300);
        receivedUserObejct = guiUserGalleryController.getReceivedObject();
        assertEquals(receivedUserObejct.getUserId(), "Tester2");
        assertEquals(receivedUserObejct.getState(), UserState.OFFLINE);
        XPLog.printDebug("user Tester2 registered");
        ConnectionRequestReply reply1 = commandInterface.sendConnectRequest("Tester2", "testpw1", "testgr", null, false);
        assertEquals(Constants.REQUEST_REPLY_WRONG_PASSWORD, reply1.getResult());
        assertEquals(guiUserGalleryController.getReceivedObject(), null);
        ConnectionRequestReply reply2 = commandInterface.sendConnectRequest("Tester2", "testpw", "testgr", null, false);
        assertEquals(Constants.REQUEST_REPLY_OK, reply2.getResult());
        receivedUserObejct = guiUserGalleryController.getReceivedObject();
        assertEquals(receivedUserObejct.getUserId(), "Tester2");
        assertEquals(receivedUserObejct.getState(), UserState.ONLINE);
        XPLog.printDebug("user Tester2 connected");
        serverApplication.onXPSessionCreateRequest(reply2.getSessionToken(), "TestSession", false);
        XPSessionManager xpSessionManager = (XPSessionManager) PrivateAccessor.getPrivateField(serverApplication, "xpSessionManager");
        xpSessionId = xpSessionManager.getXPSessionByTopic("TestSession", "testgr").getXpSessionId();
        XPLog.printDebug("Session created: id= " + xpSessionId);
        JoinXPSessionRequestReply joinReply = commandInterface.sendXPSessionJoinRequest(xpSessionId, "Tester2", SessionRole.NONE.ordinal());
        assertEquals(joinReply.getResult(), Constants.REQUEST_REPLY_OK);
        receivedUserObejct = guiUserGalleryController.getReceivedObject();
        assertEquals(receivedUserObejct.getUserId(), "Tester2");
        assertEquals(receivedUserObejct.getSessionTopic(), "TestSession");
        assertEquals(receivedUserObejct.getSessionId(), xpSessionId);
        assertEquals(receivedUserObejct.getSessionRole(), SessionRole.DRIVER);
        commandInterface.sendAddUserRequest("Tester3", "testpw", "testgr", "", "", "", "", "");
        assertEquals(guiUserGalleryController.getReceivedObject().getUserId(), "Tester3");
        ConnectionRequestReply reply3 = commandInterface.sendConnectRequest("Tester3", "testpw", "testgr", null, false);
        assertEquals(Constants.REQUEST_REPLY_OK, reply3.getResult());
        assertEquals(guiUserGalleryController.getReceivedObject().getUserId(), "Tester3");
        JoinXPSessionRequestReply joinReply1 = commandInterface.sendXPSessionJoinRequest(xpSessionId, "Tester3", SessionRole.NONE.ordinal());
        assertEquals(joinReply1.getResult(), Constants.REQUEST_REPLY_OK);
        receivedUserObejct = guiUserGalleryController.getReceivedObject();
        assertEquals(receivedUserObejct.getUserId(), "Tester3");
        assertEquals(receivedUserObejct.getSessionTopic(), "TestSession");
        assertEquals(receivedUserObejct.getSessionId(), xpSessionId);
        assertEquals(receivedUserObejct.getSessionRole(), SessionRole.NAVIGATOR);
        commandInterface.sendAddUserRequest("Tester4", "testpw", "testgr", "", "", "", "", "");
        assertEquals(guiUserGalleryController.getReceivedObject().getUserId(), "Tester4");
        ConnectionRequestReply reply4 = commandInterface.sendConnectRequest("Tester4", "testpw", "testgr", null, false);
        assertEquals(Constants.REQUEST_REPLY_OK, reply4.getResult());
        assertEquals(guiUserGalleryController.getReceivedObject().getUserId(), "Tester4");
        JoinXPSessionRequestReply joinReply2 = commandInterface.sendXPSessionJoinRequest(xpSessionId, "Tester4", SessionRole.NONE.ordinal());
        assertEquals(joinReply2.getResult(), Constants.REQUEST_REPLY_OK);
        receivedUserObejct = guiUserGalleryController.getReceivedObject();
        assertEquals(receivedUserObejct.getUserId(), "Tester4");
        assertEquals(receivedUserObejct.getSessionTopic(), "TestSession");
        assertEquals(receivedUserObejct.getSessionId(), xpSessionId);
        assertEquals(receivedUserObejct.getSessionRole(), SessionRole.SPECTATOR);
        LeaveXPSessionRequestReply leaveReply = commandInterface.sendXPSessionLeaveRequest(xpSessionId, "Tester4");
        assertEquals(Constants.REQUEST_REPLY_OK, leaveReply.getResult());
        receivedUserObejct = guiUserGalleryController.getReceivedObject();
        assertEquals(receivedUserObejct.getUserId(), "Tester4");
        assertEquals(receivedUserObejct.getState(), UserState.ONLINE);
        LeaveXPSessionRequestReply leaveReply1 = commandInterface.sendXPSessionLeaveRequest(xpSessionId, "Tester3");
        assertEquals(Constants.REQUEST_REPLY_OK, leaveReply1.getResult());
        receivedUserObejct = guiUserGalleryController.getReceivedObject();
        assertEquals(receivedUserObejct.getUserId(), "Tester3");
        assertEquals(receivedUserObejct.getState(), UserState.ONLINE);
        LeaveXPSessionRequestReply leaveReply2 = commandInterface.sendXPSessionLeaveRequest(xpSessionId, "Tester2");
        assertEquals(Constants.REQUEST_REPLY_OK, leaveReply2.getResult());
        receivedUserObejct = guiUserGalleryController.getReceivedObject();
        assertEquals(receivedUserObejct.getUserId(), "Tester2");
        assertEquals(receivedUserObejct.getState(), UserState.ONLINE);
        RemoveXPSessionRequestReply removeReply = commandInterface.sendXPSessionRemoveRequest(xpSessionId, "Tester2");
        assertEquals(Constants.REQUEST_REPLY_OK, removeReply.getResult());
        serverApplication.shutdown();
        networkFactory.tearDownServerLink();
        netWorkInit();
        receivedUserObejct = guiUserGalleryController.getReceivedObject();
        assertEquals(receivedUserObejct.getUserId(), "Tester2");
        assertEquals(receivedUserObejct.getState(), UserState.OFFLINE);
        receivedUserObejct = guiUserGalleryController.getReceivedObject();
        assertEquals(receivedUserObejct.getUserId(), "Tester3");
        assertEquals(receivedUserObejct.getState(), UserState.OFFLINE);
        receivedUserObejct = guiUserGalleryController.getReceivedObject();
        assertEquals(receivedUserObejct.getUserId(), "Tester4");
        assertEquals(receivedUserObejct.getState(), UserState.OFFLINE);
        receivedUserObejct = guiUserGalleryController.getReceivedObject();
        assertEquals(receivedUserObejct, null);
    }
}
