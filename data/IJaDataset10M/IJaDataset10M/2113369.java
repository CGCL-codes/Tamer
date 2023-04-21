package de.fuh.xpairtise.common.network.imp.activemq;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.StreamMessage;
import de.fuh.xpairtise.common.Constants;
import de.fuh.xpairtise.common.LogConstants;
import de.fuh.xpairtise.common.XPLog;
import de.fuh.xpairtise.common.network.IRemoteCommands;
import de.fuh.xpairtise.common.network.IServerCommandInterface;
import de.fuh.xpairtise.common.network.NetworkException;
import de.fuh.xpairtise.common.network.data.AddUserRequestReply;
import de.fuh.xpairtise.common.network.data.ConnectionRequestReply;
import de.fuh.xpairtise.common.network.data.CreateXPSessionRequestReply;
import de.fuh.xpairtise.common.network.data.JoinXPSessionRequestReply;
import de.fuh.xpairtise.common.network.data.LeaveXPSessionRequestReply;
import de.fuh.xpairtise.common.network.data.RemoveXPSessionRequestReply;
import de.fuh.xpairtise.common.network.data.ResourcePullReply;
import de.fuh.xpairtise.common.network.data.SendHeartbeatReply;
import de.fuh.xpairtise.common.network.imp.activemq.internal.ActiveMQBrokerConnection;
import de.fuh.xpairtise.common.network.imp.activemq.internal.IActiveMQBrokerConnectionStatusListener;
import de.fuh.xpairtise.common.util.GenerateId;
import de.fuh.xpairtise.common.util.IProgressRelay;
import de.fuh.xpairtise.common.util.SeDe;

/**
 * This class realizes the IServerCommandInterface by using ActiveMQ with a
 * queue which is used for forwarding commands to the server.
 */
public class ActiveMQServerCommandInterface implements IServerCommandInterface {

    private Session commandSession;

    private Queue commandQueue;

    private Queue heartbeatQueue;

    private MessageProducer commandProducer;

    private Session heartbeatSession;

    private MessageProducer heartbeatProducer;

    private ActiveMQBrokerConnection network;

    /**
   * The random token returned by the server in its response to a connect
   * request.
   */
    private String userSessionToken = null;

    private InputStream resourceInputStream;

    private Session uploadSession;

    private ActiveMQBrokerConnection fileTransferNetwork;

    private long maxHeartbeatTimeout;

    /**
   * Creates the command interface.
   * 
   * @param network
   *          network which generates the sessions
   * @param fileTransferNetwork
   *          fileTransferNetwork used for file transfer
   * @throws NetworkException
   */
    public ActiveMQServerCommandInterface(ActiveMQBrokerConnection network, ActiveMQBrokerConnection fileTransferNetwork) throws NetworkException {
        this.network = network;
        this.fileTransferNetwork = fileTransferNetwork;
        this.network.addStatusListener(new IActiveMQBrokerConnectionStatusListener() {

            public void onBrokerConnectionEstablished() throws NetworkException {
                if (commandSession == null || heartbeatSession == null) {
                    try {
                        if (commandSession == null) {
                            commandSession = ActiveMQServerCommandInterface.this.network.createSession();
                            commandQueue = commandSession.createQueue("ServerCommandInterface");
                            commandProducer = commandSession.createProducer(commandQueue);
                        }
                        if (heartbeatSession == null) {
                            heartbeatSession = ActiveMQServerCommandInterface.this.network.createSession();
                            heartbeatQueue = heartbeatSession.createQueue("ServerCommandInterface.Heartbeat");
                            heartbeatProducer = heartbeatSession.createProducer(heartbeatQueue);
                        }
                    } catch (Exception e) {
                        throw new NetworkException(e);
                    }
                }
            }

            public void onBrokerConnectionClosed() throws NetworkException {
                try {
                    if (commandSession != null) {
                        commandProducer.close();
                        commandSession.close();
                    }
                    if (heartbeatSession != null) {
                        heartbeatProducer.close();
                        heartbeatSession.close();
                    }
                } catch (Exception e) {
                }
                commandSession = null;
                heartbeatSession = null;
            }
        });
        this.fileTransferNetwork.addStatusListener(new IActiveMQBrokerConnectionStatusListener() {

            public void onBrokerConnectionEstablished() throws NetworkException {
                try {
                    if (uploadSession == null) {
                        uploadSession = ActiveMQServerCommandInterface.this.fileTransferNetwork.createSession();
                    }
                } catch (Exception e) {
                    throw new NetworkException(e);
                }
            }

            public void onBrokerConnectionClosed() throws NetworkException {
                try {
                    if (uploadSession != null) {
                        uploadSession.close();
                    }
                } catch (Exception e) {
                }
                uploadSession = null;
                try {
                    if (resourceInputStream != null) {
                        resourceInputStream.close();
                    }
                } catch (IOException e) {
                }
                resourceInputStream = null;
            }
        });
    }

    public void setSessionToken(String newToken) {
        userSessionToken = newToken;
    }

    private void checkForClosedSession() throws NetworkException {
        if (commandSession == null || heartbeatSession == null || uploadSession == null) {
            throw new NetworkException("session closed");
        }
    }

    public void sendChatMessage(String channelId, String text) throws NetworkException {
        if (XPLog.isDebugEnabled()) {
            XPLog.printDebug(LogConstants.LOG_PREFIX_ACTIVEMQ_SERVERCOMMANDINTERFACE + "Sending AMQ chat message");
        }
        checkForClosedSession();
        try {
            StreamMessage m = commandSession.createStreamMessage();
            m.writeString(channelId);
            m.writeString(text);
            sendMessage(m, IRemoteCommands.SEND_CHAT_MESSAGE_ID);
        } catch (JMSException e) {
            throw new NetworkException(e);
        }
    }

    public void sendEditorOpenMessage(String editorId, String xpSessionId, String project, String inputPath, String inputChecksum, String editorType) throws NetworkException {
        if (XPLog.isDebugEnabled()) {
            XPLog.printDebug(LogConstants.LOG_PREFIX_ACTIVEMQ_SERVERCOMMANDINTERFACE + "Sending AMQ editor open message");
        }
        checkForClosedSession();
        try {
            StreamMessage m = commandSession.createStreamMessage();
            m.writeString(editorId);
            m.writeString(xpSessionId);
            m.writeString(project);
            m.writeString(inputPath);
            m.writeString(inputChecksum);
            m.writeString(editorType);
            sendMessage(m, IRemoteCommands.SEND_EDITOR_OPEN_MESSAGE_ID);
        } catch (JMSException e) {
            throw new NetworkException(e);
        }
    }

    public void sendEditorChangeInputMessage(String editorId, String xpSessionId, String project, String inputPath, String inputChecksum) throws NetworkException {
        if (XPLog.isDebugEnabled()) {
            XPLog.printDebug(LogConstants.LOG_PREFIX_ACTIVEMQ_SERVERCOMMANDINTERFACE + "Sending AMQ editor change input message");
        }
        checkForClosedSession();
        try {
            StreamMessage m = commandSession.createStreamMessage();
            m.writeString(editorId);
            m.writeString(xpSessionId);
            m.writeString(project);
            m.writeString(inputPath);
            m.writeString(inputChecksum);
            sendMessage(m, IRemoteCommands.SEND_EDITOR_INPUT_MESSAGE_ID);
        } catch (JMSException e) {
            throw new NetworkException(e);
        }
    }

    public void sendEditorCloseMessage(String editorId, String xpSessionId) throws NetworkException {
        if (XPLog.isDebugEnabled()) {
            XPLog.printDebug(LogConstants.LOG_PREFIX_ACTIVEMQ_SERVERCOMMANDINTERFACE + "Sending AMQ editor close message");
        }
        checkForClosedSession();
        try {
            StreamMessage m = commandSession.createStreamMessage();
            m.writeString(editorId);
            m.writeString(xpSessionId);
            sendMessage(m, IRemoteCommands.SEND_EDITOR_CLOSE_MESSAGE_ID);
        } catch (JMSException e) {
            throw new NetworkException(e);
        }
    }

    public void sendEditorActivateMessage(String editorId, String xpSessionId) throws NetworkException {
        if (XPLog.isDebugEnabled()) {
            XPLog.printDebug(LogConstants.LOG_PREFIX_ACTIVEMQ_SERVERCOMMANDINTERFACE + "Sending AMQ editor activate message");
        }
        checkForClosedSession();
        try {
            StreamMessage m = commandSession.createStreamMessage();
            m.writeString(editorId);
            m.writeString(xpSessionId);
            sendMessage(m, IRemoteCommands.SEND_EDITOR_ACTIVATE_MESSAGE_ID);
        } catch (JMSException e) {
            throw new NetworkException(e);
        }
    }

    public void sendEditorTextChangeMessage(String editorId, int offset, String add, int replace) throws NetworkException {
        if (XPLog.isDebugEnabled()) {
            XPLog.printDebug(LogConstants.LOG_PREFIX_ACTIVEMQ_SERVERCOMMANDINTERFACE + "Sending AMQ editor text change message");
        }
        checkForClosedSession();
        try {
            StreamMessage m = commandSession.createStreamMessage();
            m.writeString(editorId);
            m.writeInt(offset);
            m.writeString(add);
            m.writeInt(replace);
            sendMessage(m, IRemoteCommands.SEND_EDITOR_TEXT_CHANGE_MESSAGE_ID);
        } catch (JMSException e) {
            throw new NetworkException(e);
        }
    }

    public void sendEditorCaretChangeMessage(String editorId, int offset) throws NetworkException {
        if (XPLog.isDebugEnabled()) {
            XPLog.printDebug(LogConstants.LOG_PREFIX_ACTIVEMQ_SERVERCOMMANDINTERFACE + "Sending AMQ editor caret change message");
        }
        checkForClosedSession();
        try {
            StreamMessage m = commandSession.createStreamMessage();
            m.writeString(editorId);
            m.writeInt(offset);
            sendMessage(m, IRemoteCommands.SEND_EDITOR_CARET_CHANGE_MESSAGE_ID);
        } catch (JMSException e) {
            throw new NetworkException(e);
        }
    }

    public void sendEditorSelectionChangeMessage(String editorId, int offset, int length) throws NetworkException {
        if (XPLog.isDebugEnabled()) {
            XPLog.printDebug(LogConstants.LOG_PREFIX_ACTIVEMQ_SERVERCOMMANDINTERFACE + "Sending AMQ editor selection change message");
        }
        checkForClosedSession();
        try {
            StreamMessage m = commandSession.createStreamMessage();
            m.writeString(editorId);
            m.writeInt(offset);
            m.writeInt(length);
            sendMessage(m, IRemoteCommands.SEND_EDITOR_SELECTION_CHANGE_MESSAGE_ID);
        } catch (JMSException e) {
            throw new NetworkException(e);
        }
    }

    public void sendEditorViewportChangeMessage(String editorId, int vOffset) throws NetworkException {
        if (XPLog.isDebugEnabled()) {
            XPLog.printDebug(LogConstants.LOG_PREFIX_ACTIVEMQ_SERVERCOMMANDINTERFACE + "Sending AMQ editor viewport change message");
        }
        checkForClosedSession();
        try {
            StreamMessage m = commandSession.createStreamMessage();
            m.writeString(editorId);
            m.writeInt(vOffset);
            sendMessage(m, IRemoteCommands.SEND_EDITOR_VIEWPORT_CHANGE_MESSAGE_ID);
        } catch (JMSException e) {
            throw new NetworkException(e);
        }
    }

    public CreateXPSessionRequestReply sendXPSessionCreateRequest(String xpSessionTopic, boolean uploadsPending) throws NetworkException {
        if (XPLog.isDebugEnabled()) {
            XPLog.printDebug(LogConstants.LOG_PREFIX_ACTIVEMQ_SERVERCOMMANDINTERFACE + "Sending AMQ XPSession create message");
        }
        checkForClosedSession();
        CreateXPSessionRequestReply reply = null;
        try {
            StreamMessage m = commandSession.createStreamMessage();
            m.writeString(xpSessionTopic);
            m.writeBoolean(uploadsPending);
            Object o = sendAndWaitForReply(m, IRemoteCommands.SEND_XPSESSION_CREATE_MESSAGE_ID, Constants.CLIENT_ACTIVEMQ_WAIT_FOR_REPLY_TIMEOUT_FAST);
            if (o != null) {
                if (!(o instanceof CreateXPSessionRequestReply)) {
                    throw new NetworkException("CREATE xpSession reply contained unexpected object: " + o.getClass());
                }
                reply = (CreateXPSessionRequestReply) o;
            }
        } catch (Exception e) {
            throw new NetworkException(e);
        }
        return reply;
    }

    public JoinXPSessionRequestReply sendXPSessionJoinRequest(String xpSessionId, String user, int preferredRole) throws NetworkException {
        if (XPLog.isDebugEnabled()) {
            XPLog.printDebug(LogConstants.LOG_PREFIX_ACTIVEMQ_SERVERCOMMANDINTERFACE + "Sending AMQ XPSession join request message");
        }
        checkForClosedSession();
        JoinXPSessionRequestReply reply = null;
        try {
            StreamMessage joinMsg = commandSession.createStreamMessage();
            joinMsg.writeString(xpSessionId);
            joinMsg.writeString(user);
            joinMsg.writeInt(preferredRole);
            Object o = sendAndWaitForReply(joinMsg, IRemoteCommands.SEND_XPSESSION_JOIN_MESSAGE_ID, Constants.CLIENT_ACTIVEMQ_WAIT_FOR_REPLY_TIMEOUT_FAST);
            if (o != null) {
                if (!(o instanceof JoinXPSessionRequestReply)) {
                    throw new NetworkException("JOIN xpSession reply contained unexpected object: " + o.getClass());
                }
                reply = (JoinXPSessionRequestReply) o;
            }
        } catch (JMSException e) {
            throw new NetworkException(e);
        }
        return reply;
    }

    public LeaveXPSessionRequestReply sendXPSessionLeaveRequest(String xpSessionId, String user) throws NetworkException {
        if (XPLog.isDebugEnabled()) {
            XPLog.printDebug(LogConstants.LOG_PREFIX_ACTIVEMQ_SERVERCOMMANDINTERFACE + "Sending AMQ XPSession leave request message");
        }
        checkForClosedSession();
        LeaveXPSessionRequestReply reply = null;
        try {
            StreamMessage leaveMsg = commandSession.createStreamMessage();
            leaveMsg.writeString(xpSessionId);
            leaveMsg.writeString(user);
            Object o = sendAndWaitForReply(leaveMsg, IRemoteCommands.SEND_XPSESSION_LEAVE_MESSAGE_ID, Constants.CLIENT_ACTIVEMQ_WAIT_FOR_REPLY_TIMEOUT_FAST);
            if (o != null) {
                if (!(o instanceof LeaveXPSessionRequestReply)) {
                    throw new NetworkException("LEAVE xpSession reply contained unexpected object: " + o.getClass());
                }
                reply = (LeaveXPSessionRequestReply) o;
            } else {
                throw new NetworkException("No reply received on LEAVE xpSession.");
            }
        } catch (JMSException e) {
            throw new NetworkException(e);
        }
        return reply;
    }

    public RemoveXPSessionRequestReply sendXPSessionRemoveRequest(String xpSessionId, String user) throws NetworkException {
        if (XPLog.isDebugEnabled()) {
            XPLog.printDebug(LogConstants.LOG_PREFIX_ACTIVEMQ_SERVERCOMMANDINTERFACE + "Sending AMQ XPSession remove request message");
        }
        checkForClosedSession();
        RemoveXPSessionRequestReply reply = null;
        try {
            StreamMessage removeMsg = commandSession.createStreamMessage();
            removeMsg.writeString(xpSessionId);
            removeMsg.writeString(user);
            Object o = sendAndWaitForReply(removeMsg, IRemoteCommands.SEND_XPSESSION_REMOVE_MESSAGE_ID, Constants.CLIENT_ACTIVEMQ_WAIT_FOR_REPLY_TIMEOUT_SLOW);
            if (o != null) {
                if (!(o instanceof RemoveXPSessionRequestReply)) {
                    throw new NetworkException("REMOVE xpSession reply contained unexpected object: " + o.getClass());
                }
                reply = (RemoveXPSessionRequestReply) o;
            } else {
                throw new NetworkException("No reply received on REMOVE xpSession.");
            }
        } catch (JMSException e) {
            throw new NetworkException(e);
        }
        return reply;
    }

    public void sendAddProjectMessage(String xpSessionId, String name, boolean uploadsPending, InputStream data, IProgressRelay progress) throws NetworkException {
        if (XPLog.isDebugEnabled()) {
            XPLog.printDebug(LogConstants.LOG_PREFIX_ACTIVEMQ_SERVERCOMMANDINTERFACE + "Sending AMQ project upload message");
        }
        checkForClosedSession();
        try {
            StreamMessage m = commandSession.createStreamMessage();
            m.writeString(xpSessionId);
            m.writeString(name);
            m.writeBoolean(uploadsPending);
            String queueId = data != null ? "ProjectDataUpload" + GenerateId.getInstance().generateId() : "";
            m.writeString(queueId);
            sendMessage(m, IRemoteCommands.SEND_PROJECT_ADDED_MESSAGE);
            if (data != null) {
                uploadData(queueId, data, progress);
            }
        } catch (Exception e) {
            throw new NetworkException(e);
        }
    }

    public void sendAddVCProjectMessage(String xpSessionId, String name, String type, String location, String revision, Map<String, String> members, List<String> folders, List<String> remoteOnly, boolean uploadsPending, InputStream modifiedData, IProgressRelay progress) throws NetworkException {
        if (XPLog.isDebugEnabled()) {
            XPLog.printDebug(LogConstants.LOG_PREFIX_ACTIVEMQ_SERVERCOMMANDINTERFACE + "Sending AMQ vc project upload message");
        }
        checkForClosedSession();
        try {
            StreamMessage m = commandSession.createStreamMessage();
            m.writeString(xpSessionId);
            m.writeString(name);
            m.writeString(type);
            m.writeString(location);
            m.writeString(revision);
            byte[] memberArray = SeDe.mapToByteArray(members);
            m.writeInt(memberArray.length);
            m.writeBytes(memberArray);
            if (folders == null) {
                m.writeInt(-1);
            } else {
                m.writeInt(folders.size());
                for (String folder : folders) {
                    m.writeString(folder);
                }
            }
            if (remoteOnly == null) {
                m.writeInt(-1);
            } else {
                m.writeInt(remoteOnly.size());
                for (String member : remoteOnly) {
                    m.writeString(member);
                }
            }
            m.writeBoolean(uploadsPending);
            String queueId = modifiedData != null ? "ProjectUpload." + GenerateId.getInstance().generateId() : "";
            m.writeString(queueId);
            sendMessage(m, IRemoteCommands.SEND_VCPROJECT_ADDED_MESSAGE);
            if (modifiedData != null) {
                uploadData(queueId, modifiedData, progress);
            }
        } catch (Exception e) {
            throw new NetworkException(e);
        }
    }

    public void sendRemoveProjectsMessage(String xpSessionId, List<String> projects) throws NetworkException {
        if (XPLog.isDebugEnabled()) {
            XPLog.printDebug(LogConstants.LOG_PREFIX_ACTIVEMQ_SERVERCOMMANDINTERFACE + "Sending AMQ project remove message");
        }
        checkForClosedSession();
        try {
            StreamMessage m = commandSession.createStreamMessage();
            m.writeString(xpSessionId);
            m.writeInt(projects.size());
            for (String project : projects) {
                m.writeString(project);
            }
            sendMessage(m, IRemoteCommands.SEND_PROJECT_REMOVED_MESSAGE);
        } catch (Exception e) {
            throw new NetworkException(e);
        }
    }

    public ConnectionRequestReply sendConnectRequest(String username, String password, String group, byte[] icon, boolean createAccount) throws NetworkException {
        if (XPLog.isDebugEnabled()) {
            XPLog.printDebug(LogConstants.LOG_PREFIX_ACTIVEMQ_SERVERCOMMANDINTERFACE + "Sending AMQ connect request message");
        }
        checkForClosedSession();
        ConnectionRequestReply reply = null;
        try {
            StreamMessage connectMsg = commandSession.createStreamMessage();
            connectMsg.writeString(username);
            connectMsg.writeString(password);
            connectMsg.writeString(group);
            if (icon != null) {
                connectMsg.writeInt(icon.length);
                connectMsg.writeBytes(icon);
            } else {
                connectMsg.writeInt(0);
            }
            connectMsg.writeBoolean(createAccount);
            Object o = sendAndWaitForReply(connectMsg, IRemoteCommands.SEND_CONNECT_MESSAGE_ID, Constants.CLIENT_ACTIVEMQ_WAIT_FOR_REPLY_TIMEOUT_CONNECT);
            if (o != null) {
                if (!(o instanceof ConnectionRequestReply)) {
                    throw new NetworkException("CONNECT reply contained unexpected object: " + o.getClass());
                }
                reply = (ConnectionRequestReply) o;
            } else {
                throw new NetworkException("No reply received on CONNECT");
            }
        } catch (JMSException e) {
            throw new NetworkException(e);
        }
        return reply;
    }

    public void sendDisconnectMessage() throws NetworkException {
        if (XPLog.isDebugEnabled()) {
            XPLog.printDebug(LogConstants.LOG_PREFIX_ACTIVEMQ_SERVERCOMMANDINTERFACE + "Sending AMQ disconnect message");
        }
        checkForClosedSession();
        try {
            Message m = commandSession.createMessage();
            sendMessage(m, IRemoteCommands.SEND_DISCONNECT_MESSAGE_ID);
            userSessionToken = null;
        } catch (JMSException e) {
            throw new NetworkException(e);
        }
    }

    public void sendWhiteboardPencilUpdate(String xpSessionId, int[] points, byte[] color, int lineThickness) throws NetworkException {
        if (XPLog.isDebugEnabled()) {
            XPLog.printDebug(LogConstants.LOG_PREFIX_ACTIVEMQ_SERVERCOMMANDINTERFACE + "Sending AMQ whiteboard pencil update message");
        }
        checkForClosedSession();
        try {
            StreamMessage m = commandSession.createStreamMessage();
            m.writeString(xpSessionId);
            m.writeInt(points.length);
            for (int i = 0; i < points.length; ++i) {
                m.writeInt(points[i]);
            }
            m.writeBytes(color);
            m.writeInt(lineThickness);
            sendMessage(m, IRemoteCommands.SEND_WHITEBOARD_PENCIL_UPDATE);
        } catch (JMSException e) {
            throw new NetworkException(e);
        }
    }

    public void sendWhiteboardClear(String xpSessionId) throws NetworkException {
        if (XPLog.isDebugEnabled()) {
            XPLog.printDebug(LogConstants.LOG_PREFIX_ACTIVEMQ_SERVERCOMMANDINTERFACE + "Sending AMQ whiteboard clear message");
        }
        checkForClosedSession();
        try {
            StreamMessage m = commandSession.createStreamMessage();
            m.writeString(xpSessionId);
            sendMessage(m, IRemoteCommands.SEND_WHITEBOARD_CLEAR);
        } catch (JMSException e) {
            throw new NetworkException(e);
        }
    }

    public SendHeartbeatReply sendHeartbeat(int timeout) throws NetworkException {
        long time = System.currentTimeMillis();
        if (XPLog.isDebugEnabled()) {
            XPLog.printDebug(LogConstants.LOG_PREFIX_ACTIVEMQ_SERVERCOMMANDINTERFACE + "Sending AMQ heartbeat message");
        }
        if (userSessionToken == null) return null;
        SendHeartbeatReply reply = null;
        checkForClosedSession();
        try {
            Message m = heartbeatSession.createMessage();
            Object o = sendAndWaitForReply(m, IRemoteCommands.HEARTBEAT_MESSAGE_ID, timeout, heartbeatSession, heartbeatProducer);
            if (o != null) {
                if (!(o instanceof SendHeartbeatReply)) {
                    throw new NetworkException("Send heartbeat reply contained unexpected object: " + o.getClass());
                }
            } else {
                throw new NetworkException("No reply received on send heartbeat request");
            }
            reply = (SendHeartbeatReply) o;
        } catch (JMSException e) {
            throw new NetworkException(e);
        }
        if (XPLog.isDebugEnabled()) {
            if (reply != null) {
                long t_diff = System.currentTimeMillis() - time;
                if (t_diff > maxHeartbeatTimeout) {
                    maxHeartbeatTimeout = t_diff;
                }
                XPLog.printDebug(LogConstants.LOG_PREFIX_ACTIVEMQ_SERVERCOMMANDINTERFACE + "Heartbeat message reply received time=" + t_diff + " max_time=" + maxHeartbeatTimeout);
            } else {
                XPLog.printDebug(LogConstants.LOG_PREFIX_ACTIVEMQ_SERVERCOMMANDINTERFACE + "Heartbeat message no reply received time=" + (System.currentTimeMillis() - time));
            }
        }
        return reply;
    }

    public AddUserRequestReply sendAddUserRequest(String userId, String userPassword, String userGroup, String lastName, String firstName, String email, String instant, String phone) throws NetworkException {
        if (XPLog.isDebugEnabled()) {
            XPLog.printDebug(LogConstants.LOG_PREFIX_ACTIVEMQ_SERVERCOMMANDINTERFACE + "Sending AMQ add user request message");
        }
        checkForClosedSession();
        AddUserRequestReply reply = null;
        try {
            StreamMessage addMsg = commandSession.createStreamMessage();
            addMsg.writeString(userId);
            addMsg.writeString(userPassword);
            addMsg.writeString(userGroup);
            addMsg.writeString(lastName);
            addMsg.writeString(firstName);
            addMsg.writeString(email);
            addMsg.writeString(instant);
            addMsg.writeString(phone);
            Object o = sendAndWaitForReply(addMsg, IRemoteCommands.SEND_ADD_USER_REQUEST, Constants.CLIENT_ACTIVEMQ_WAIT_FOR_REPLY_TIMEOUT_FAST);
            if (o != null) {
                if (!(o instanceof AddUserRequestReply)) {
                    throw new NetworkException("Add user reply contained unexpected object: " + o.getClass());
                }
                reply = (AddUserRequestReply) o;
            } else {
                throw new NetworkException("No reply received on user add request");
            }
        } catch (JMSException e) {
            throw new NetworkException(e);
        }
        return reply;
    }

    public void sendRemoveUserRequest(String userId, String userPassword, String group) throws NetworkException {
        if (XPLog.isDebugEnabled()) {
            XPLog.printDebug(LogConstants.LOG_PREFIX_ACTIVEMQ_SERVERCOMMANDINTERFACE + "Sending AMQ remove user request message");
        }
        checkForClosedSession();
        try {
            StreamMessage m = commandSession.createStreamMessage();
            m.writeString(userId);
            m.writeString(userPassword);
            m.writeString(group);
            sendMessage(m, IRemoteCommands.SEND_REMOVE_USER_REQUEST);
        } catch (JMSException e) {
            throw new NetworkException(e);
        }
    }

    public void sendModifyUserRequest(String oldUserId, String oldUserPassword, String oldGroup, String newUserId, String newUserPassword, String newGroup, String newLastName, String newFirstName, String newEmail, String newInstant, String newPhone) throws NetworkException {
        if (XPLog.isDebugEnabled()) {
            XPLog.printDebug(LogConstants.LOG_PREFIX_ACTIVEMQ_SERVERCOMMANDINTERFACE + "Sending AMQ modify user request message");
        }
        checkForClosedSession();
        try {
            StreamMessage m = commandSession.createStreamMessage();
            m.writeString(oldUserId);
            m.writeString(oldUserPassword);
            m.writeString(oldGroup);
            m.writeString(newUserId);
            m.writeString(newUserPassword);
            m.writeString(newGroup);
            m.writeString(newLastName);
            m.writeString(newFirstName);
            m.writeString(newEmail);
            m.writeString(newInstant);
            m.writeString(newPhone);
            sendMessage(m, IRemoteCommands.SEND_MODIFY_USER_REQUEST);
        } catch (JMSException e) {
            throw new NetworkException(e);
        }
    }

    public void sendInvitation(String sender, String recipient, String xpSessionTopic) throws NetworkException {
        if (XPLog.isDebugEnabled()) {
            XPLog.printDebug(LogConstants.LOG_PREFIX_ACTIVEMQ_SERVERCOMMANDINTERFACE + "Sending AMQ invitation message");
        }
        checkForClosedSession();
        try {
            StreamMessage m = commandSession.createStreamMessage();
            m.writeString(sender);
            m.writeString(recipient);
            m.writeString(xpSessionTopic);
            sendMessage(m, IRemoteCommands.SEND_INVITATION);
        } catch (JMSException e) {
            throw new NetworkException(e);
        }
    }

    public void sendFileAddedMessage(String xpSessionId, String project, String path, byte[] content, String checksum) throws NetworkException {
        if (XPLog.isDebugEnabled()) {
            XPLog.printDebug(LogConstants.LOG_PREFIX_ACTIVEMQ_SERVERCOMMANDINTERFACE + "Sending AMQ file added message");
        }
        checkForClosedSession();
        try {
            StreamMessage m = commandSession.createStreamMessage();
            m.writeString(xpSessionId);
            m.writeString(project);
            m.writeString(path);
            m.writeInt(content.length);
            m.writeBytes(content);
            m.writeString(checksum);
            sendMessage(m, IRemoteCommands.SEND_RESOURCE_FILE_ADDED_MESSAGE);
        } catch (JMSException e) {
            throw new NetworkException(e);
        }
    }

    public void sendVCFileAddedMessage(String xpSessionId, String project, String path, String revision, byte[] content, String checksum) throws NetworkException {
        if (XPLog.isDebugEnabled()) {
            XPLog.printDebug(LogConstants.LOG_PREFIX_ACTIVEMQ_SERVERCOMMANDINTERFACE + "Sending AMQ vc file added message");
        }
        checkForClosedSession();
        try {
            StreamMessage m = commandSession.createStreamMessage();
            m.writeString(xpSessionId);
            m.writeString(project);
            m.writeString(path);
            m.writeString(revision);
            int length = content != null ? content.length : -1;
            m.writeInt(length);
            if (content != null) {
                m.writeBytes(content);
            }
            m.writeString(checksum);
            sendMessage(m, IRemoteCommands.SEND_RESOURCE_VCFILE_ADDED_MESSAGE);
        } catch (JMSException e) {
            throw new NetworkException(e);
        }
    }

    public void sendFileChangedMessage(String xpSessionId, String project, String path, byte[] content, String checksum) throws NetworkException {
        if (XPLog.isDebugEnabled()) {
            XPLog.printDebug(LogConstants.LOG_PREFIX_ACTIVEMQ_SERVERCOMMANDINTERFACE + "Sending AMQ file changed message");
        }
        checkForClosedSession();
        try {
            StreamMessage m = commandSession.createStreamMessage();
            m.writeString(xpSessionId);
            m.writeString(project);
            m.writeString(path);
            m.writeInt(content.length);
            m.writeBytes(content);
            m.writeString(checksum);
            sendMessage(m, IRemoteCommands.SEND_RESOURCE_FILE_CHANGED_MESSAGE);
        } catch (JMSException e) {
            throw new NetworkException(e);
        }
    }

    public void sendVCFileChangedMessage(String xpSessionId, String project, String path, String revision, byte[] content, String checksum) throws NetworkException {
        if (XPLog.isDebugEnabled()) {
            XPLog.printDebug(LogConstants.LOG_PREFIX_ACTIVEMQ_SERVERCOMMANDINTERFACE + "Sending AMQ vc file changed message");
        }
        checkForClosedSession();
        try {
            StreamMessage m = commandSession.createStreamMessage();
            m.writeString(xpSessionId);
            m.writeString(project);
            m.writeString(path);
            m.writeString(revision);
            int length = content != null ? content.length : -1;
            m.writeInt(length);
            if (content != null) {
                m.writeBytes(content);
            }
            m.writeString(checksum);
            sendMessage(m, IRemoteCommands.SEND_RESOURCE_VCFILE_CHANGED_MESSAGE);
        } catch (JMSException e) {
            throw new NetworkException(e);
        }
    }

    public void sendFolderAddedMessage(String xpSessionId, String project, String path) throws NetworkException {
        if (XPLog.isDebugEnabled()) {
            XPLog.printDebug(LogConstants.LOG_PREFIX_ACTIVEMQ_SERVERCOMMANDINTERFACE + "Sending AMQ folder added message");
        }
        checkForClosedSession();
        try {
            StreamMessage m = commandSession.createStreamMessage();
            m.writeString(xpSessionId);
            m.writeString(project);
            m.writeString(path);
            sendMessage(m, IRemoteCommands.SEND_RESOURCE_FOLDER_ADDED_MESSAGE);
        } catch (JMSException e) {
            throw new NetworkException(e);
        }
    }

    public void sendVCFolderAddedMessage(String xpSessionId, String project, String path, String revision) throws NetworkException {
        if (XPLog.isDebugEnabled()) {
            XPLog.printDebug(LogConstants.LOG_PREFIX_ACTIVEMQ_SERVERCOMMANDINTERFACE + "Sending AMQ vc folder added message");
        }
        checkForClosedSession();
        try {
            StreamMessage m = commandSession.createStreamMessage();
            m.writeString(xpSessionId);
            m.writeString(project);
            m.writeString(path);
            m.writeString(revision);
            sendMessage(m, IRemoteCommands.SEND_RESOURCE_VCFOLDER_ADDED_MESSAGE);
        } catch (JMSException e) {
            throw new NetworkException(e);
        }
    }

    public void sendVCFolderChangedMessage(String xpSessionId, String project, String path, String revision) throws NetworkException {
        if (XPLog.isDebugEnabled()) {
            XPLog.printDebug(LogConstants.LOG_PREFIX_ACTIVEMQ_SERVERCOMMANDINTERFACE + "Sending AMQ vc folder changed message");
        }
        checkForClosedSession();
        try {
            StreamMessage m = commandSession.createStreamMessage();
            m.writeString(xpSessionId);
            m.writeString(project);
            m.writeString(path);
            m.writeString(revision);
            sendMessage(m, IRemoteCommands.SEND_RESOURCE_VCFOLDER_CHANGED_MESSAGE);
        } catch (JMSException e) {
            throw new NetworkException(e);
        }
    }

    public void sendResourceRemovedMessage(String xpSessionId, String project, String path) throws NetworkException {
        if (XPLog.isDebugEnabled()) {
            XPLog.printDebug(LogConstants.LOG_PREFIX_ACTIVEMQ_SERVERCOMMANDINTERFACE + "Sending AMQ resource removed message");
        }
        checkForClosedSession();
        try {
            StreamMessage m = commandSession.createStreamMessage();
            m.writeString(xpSessionId);
            m.writeString(project);
            m.writeString(path);
            sendMessage(m, IRemoteCommands.SEND_RESOURCE_REMOVED_MESSAGE);
        } catch (JMSException e) {
            throw new NetworkException(e);
        }
    }

    public void sendVCResourceRemovedMessage(String xpSessionId, String project, String path, boolean unregister) throws NetworkException {
        if (XPLog.isDebugEnabled()) {
            XPLog.printDebug(LogConstants.LOG_PREFIX_ACTIVEMQ_SERVERCOMMANDINTERFACE + "Sending AMQ vc resource removed message");
        }
        checkForClosedSession();
        try {
            StreamMessage m = commandSession.createStreamMessage();
            m.writeString(xpSessionId);
            m.writeString(project);
            m.writeString(path);
            m.writeBoolean(unregister);
            sendMessage(m, IRemoteCommands.SEND_VCRESOURCE_REMOVED_MESSAGE);
        } catch (JMSException e) {
            throw new NetworkException(e);
        }
    }

    public void sendVCProjectChangedMessage(String xpSessionId, String project, String location, String revision) throws NetworkException {
        if (XPLog.isDebugEnabled()) {
            XPLog.printDebug(LogConstants.LOG_PREFIX_ACTIVEMQ_SERVERCOMMANDINTERFACE + "Sending AMQ vc project changed message");
        }
        checkForClosedSession();
        try {
            StreamMessage m = commandSession.createStreamMessage();
            m.writeString(xpSessionId);
            m.writeString(project);
            m.writeString(location);
            m.writeString(revision);
            sendMessage(m, IRemoteCommands.SEND_VCPROJECT_CHANGED_MESSAGE);
        } catch (JMSException e) {
            throw new NetworkException(e);
        }
    }

    public void sendLaunchAdded(String sender, String name, String id, String mode, byte[] wc) throws NetworkException {
        if (XPLog.isDebugEnabled()) {
            XPLog.printDebug(LogConstants.LOG_PREFIX_ACTIVEMQ_SERVERCOMMANDINTERFACE + "Sending AMQ launch added message");
        }
        checkForClosedSession();
        try {
            StreamMessage m = commandSession.createStreamMessage();
            m.writeString(sender);
            m.writeString(name);
            m.writeString(id);
            m.writeString(mode);
            m.writeInt(wc.length);
            m.writeBytes(wc);
            sendMessage(m, IRemoteCommands.SEND_LAUNCH_ADDED_MESSAGE);
        } catch (JMSException e) {
            throw new NetworkException(e);
        }
    }

    public void sendLaunchChanged(String sender, String name, String id, String mode, byte[] wc) throws NetworkException {
        if (XPLog.isDebugEnabled()) {
            XPLog.printDebug(LogConstants.LOG_PREFIX_ACTIVEMQ_SERVERCOMMANDINTERFACE + "Sending AMQ launch changed message");
        }
        checkForClosedSession();
        try {
            StreamMessage m = commandSession.createStreamMessage();
            m.writeString(sender);
            m.writeString(name);
            m.writeString(id);
            m.writeString(mode);
            m.writeInt(wc.length);
            m.writeBytes(wc);
            sendMessage(m, IRemoteCommands.SEND_LAUNCH_CHANGED_MESSAGE);
        } catch (JMSException e) {
            throw new NetworkException(e);
        }
    }

    public void sendLaunchRemoved(String sender, String name, String id, String mode, byte[] wc) throws NetworkException {
        if (XPLog.isDebugEnabled()) {
            XPLog.printDebug(LogConstants.LOG_PREFIX_ACTIVEMQ_SERVERCOMMANDINTERFACE + "Sending AMQ lauch removed message");
        }
        checkForClosedSession();
        try {
            StreamMessage m = commandSession.createStreamMessage();
            m.writeString(sender);
            m.writeString(name);
            m.writeString(id);
            m.writeString(mode);
            m.writeInt(wc.length);
            m.writeBytes(wc);
            sendMessage(m, IRemoteCommands.SEND_LAUNCH_REMOVED_MESSAGE);
        } catch (JMSException e) {
            throw new NetworkException(e);
        }
    }

    public ResourcePullReply sendResourcePullRequest(String xpSessionId, String project, String path) throws NetworkException {
        if (XPLog.isDebugEnabled()) {
            XPLog.printDebug(LogConstants.LOG_PREFIX_ACTIVEMQ_SERVERCOMMANDINTERFACE + "Sending AMQ resource pull request message xpSessionId=" + xpSessionId + " path=" + path);
        }
        ResourcePullReply reply = null;
        checkForClosedSession();
        try {
            StreamMessage m = commandSession.createStreamMessage();
            m.writeString(xpSessionId);
            m.writeString(project);
            m.writeString(path);
            Object o = sendAndWaitForReply(m, IRemoteCommands.SEND_RESOURCE_PULL_MESSAGE, 0);
            if (o != null) {
                if (!(o instanceof ResourcePullReply)) {
                    throw new NetworkException("ResourcePullReply contained unexpected object: " + o.getClass());
                }
                reply = (ResourcePullReply) o;
                if (reply.getQueueId().length() > 0 && reply.getResourceContent() == null) {
                    Queue q = uploadSession.createQueue(reply.getQueueId());
                    resourceInputStream = fileTransferNetwork.createInputStream(q);
                    byte[] buffer = new byte[(int) reply.getResourceSize()];
                    int totalCount = 0;
                    int count;
                    while ((count = resourceInputStream.read(buffer, totalCount, buffer.length)) != -1) {
                        totalCount += count;
                    }
                    reply.setResourceContent(buffer);
                    resourceInputStream.close();
                    resourceInputStream = null;
                }
            } else {
                throw new NetworkException("No pull reply received - timeout or connection closed");
            }
        } catch (JMSException e) {
            throw new NetworkException(e);
        } catch (IOException e) {
            throw new NetworkException(e);
        }
        return reply;
    }

    public void sendRoleChangeRequest(String xpSessionID, String sender, String recipient) throws NetworkException {
        if (XPLog.isDebugEnabled()) {
            XPLog.printDebug(LogConstants.LOG_PREFIX_ACTIVEMQ_SERVERCOMMANDINTERFACE + "Sending AMQ role changed request message");
        }
        checkForClosedSession();
        try {
            StreamMessage m = commandSession.createStreamMessage();
            m.writeString(xpSessionID);
            m.writeString(sender);
            m.writeString(recipient);
            sendMessage(m, IRemoteCommands.SEND_ROLE_CHANGE_REQUEST);
        } catch (JMSException e) {
            throw new NetworkException(e);
        }
    }

    public void sendNewRoleChangeRequestState(boolean state) throws NetworkException {
        if (XPLog.isDebugEnabled()) {
            XPLog.printDebug(LogConstants.LOG_PREFIX_ACTIVEMQ_SERVERCOMMANDINTERFACE + "Sending AMQ new role change request state message");
        }
        checkForClosedSession();
        try {
            StreamMessage m = commandSession.createStreamMessage();
            m.writeString(userSessionToken);
            m.writeBoolean(state);
            sendMessage(m, IRemoteCommands.SEND_NEW_ROLE_CHANGE_REQUEST_STATE);
        } catch (JMSException e) {
            throw new NetworkException(e);
        }
    }

    public void sendMessage(String sender, String recipient, String message) throws NetworkException {
        if (XPLog.isDebugEnabled()) {
            XPLog.printDebug(LogConstants.LOG_PREFIX_ACTIVEMQ_SERVERCOMMANDINTERFACE + "Sending AMQ message");
        }
        checkForClosedSession();
        try {
            StreamMessage m = commandSession.createStreamMessage();
            m.writeString(sender);
            m.writeString(recipient);
            m.writeString(message);
            sendMessage(m, IRemoteCommands.SEND_MESSAGE);
        } catch (JMSException e) {
            throw new NetworkException(e);
        }
    }

    public void sendWhiteboardEraseCommand(String xpSessionId, int[] points) throws NetworkException {
        if (XPLog.isDebugEnabled()) {
            XPLog.printDebug(LogConstants.LOG_PREFIX_ACTIVEMQ_SERVERCOMMANDINTERFACE + "Sending AMQ whiteboard erase command message");
        }
        checkForClosedSession();
        try {
            StreamMessage m = commandSession.createStreamMessage();
            m.writeString(xpSessionId);
            m.writeInt(points.length);
            for (int i = 0; i < points.length; ++i) {
                m.writeInt(points[i]);
            }
            sendMessage(m, IRemoteCommands.SEND_WHITEBOARD_ERASE_COMMAND);
        } catch (JMSException e) {
            throw new NetworkException(e);
        }
    }

    private Object sendAndWaitForReply(Message m, int messageId, int timeout) throws JMSException, NetworkException {
        return sendAndWaitForReply(m, messageId, timeout, commandSession, commandProducer);
    }

    private Object sendAndWaitForReply(Message m, int messageId, int timeout, Session session, MessageProducer producer) throws JMSException, NetworkException {
        Object reply = null;
        Queue queue = session.createTemporaryQueue();
        MessageConsumer consumer = session.createConsumer(queue);
        m.setJMSReplyTo(queue);
        sendMessage(m, messageId, producer);
        while (true) {
            checkForClosedSession();
            ObjectMessage replyMsg = (ObjectMessage) consumer.receive(timeout == 0 ? 5000 : timeout);
            if (replyMsg != null) {
                Object containedObject = replyMsg.getObject();
                if (containedObject == null) {
                    throw new NetworkException("reply contained no object.");
                }
                reply = containedObject;
                break;
            } else if (timeout != 0) {
                XPLog.getLog().info("ActiveMQ reply timeout exceeded (request messageid: " + messageId + ", timeout: " + timeout + " msec)");
                break;
            }
        }
        return reply;
    }

    private void sendMessage(Message m, int messageId) throws JMSException {
        sendMessage(m, messageId, commandProducer);
    }

    private void sendMessage(Message m, int messageId, MessageProducer producer) throws JMSException {
        m.setIntProperty(IRemoteCommands.MESSAGE_ID_PROPERTY, messageId);
        m.setStringProperty(Constants.ACTIVEMQ_TOKEN_PROPERTY, userSessionToken);
        producer.send(m);
    }

    private void uploadData(String queueId, InputStream data, IProgressRelay progress) throws JMSException, IOException {
        long time = System.currentTimeMillis();
        Queue q = uploadSession.createQueue(queueId);
        OutputStream os = fileTransferNetwork.createQutputStream(q);
        byte[] buffer = new byte[Constants.UPLOAD_BUFFER];
        int count;
        long totalCount = 0;
        long time2 = System.currentTimeMillis();
        while ((count = data.read(buffer, 0, buffer.length)) != -1) {
            os.write(buffer, 0, count);
            totalCount += count;
            if (totalCount >= (1024 * 1024) && (totalCount % (1024 * 1024)) == 0) {
                long t = System.currentTimeMillis();
                int uploaded = (int) (totalCount / 1024 / 1024);
                if (XPLog.isDebugEnabled()) {
                    XPLog.printDebug(LogConstants.LOG_PREFIX_ACTIVEMQ_SERVERCOMMANDINTERFACE + String.format("%dMB uploaded, time elapsed=%dms, total time=%dms", uploaded, t - time2, t - time));
                }
                if (progress != null) {
                    progress.subTask(uploaded + " MB uploaded.");
                }
                time2 = t;
            }
            if (progress != null) {
                progress.worked(1);
            }
        }
        os.close();
        data.close();
        if (progress != null) {
            progress.done();
        }
        if (XPLog.isDebugEnabled()) {
            XPLog.printDebug(LogConstants.LOG_PREFIX_ACTIVEMQ_SERVERCOMMANDINTERFACE + String.format("Finished uploading data, time elpased=%d ...", System.currentTimeMillis() - time));
        }
    }
}
