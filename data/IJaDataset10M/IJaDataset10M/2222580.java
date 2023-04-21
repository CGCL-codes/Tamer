package de.fuh.xpairtise.server;

import java.util.Map;
import org.apache.log4j.Level;
import de.fuh.xpairtise.common.LogConstants;
import de.fuh.xpairtise.common.XPLog;
import de.fuh.xpairtise.common.network.IClientCommand;
import de.fuh.xpairtise.common.network.NetworkException;
import de.fuh.xpairtise.common.replication.UnexpectedReplicationState;
import de.fuh.xpairtise.common.replication.elements.ReplicatedUser;
import de.fuh.xpairtise.common.replication.elements.ReplicatedXPSession;
import de.fuh.xpairtise.common.util.SeDe;

/**
 * This class is notified about launches added, changed or removed by the driver
 * of a distributed pair programming session. It will send the corresponding
 * launch configurations to the <code>ClientLaunchHandler</code> of the other
 * participants.
 */
public class LaunchHandler {

    private UserSessionManager sessionManager;

    private XPSessionManager xpSessionManager;

    private ResourceManager resourceManager;

    /**
   * Creates a new <code>LaunchHandler</code> instance.
   * 
   * @param sessionManager
   *          the <code>UserSessionManager</code> instance
   * @param xpSessionManager
   *          the <code>XPSessionManager</code> instance
   * @param resourceManager
   *          the <code>ResourceManager</code> instance
   */
    public LaunchHandler(UserSessionManager sessionManager, XPSessionManager xpSessionManager, ResourceManager resourceManager) {
        if (XPLog.isDebugEnabled()) {
            XPLog.printDebug(LogConstants.LOG_PREFIX_LAUNCH_HANDLER + "started.");
        }
        this.sessionManager = sessionManager;
        this.xpSessionManager = xpSessionManager;
        this.resourceManager = resourceManager;
    }

    /**
   * Notifies the receiver that a launch has been added to the launch manager.
   * 
   * @param sender
   *          the user who has added the launch
   * @param name
   *          the name for the launch configuration
   * @param id
   *          the unique identifier for a launch configuration type extension
   * @param mode
   *          the mode of the launch
   * @param wc
   *          the configuration of the launch
   * 
   * @see de.fuh.xpairtise.common.network.IServerCommandListener#onLaunchAddedMessage(java.lang.String,
   *      java.lang.String, java.lang.String, java.lang.String, byte[])
   */
    public void onLaunchAddedMessage(String sender, String name, String id, String mode, byte[] wc) {
        if (XPLog.isDebugEnabled()) {
            try {
                XPLog.printDebug(LogConstants.LOG_PREFIX_LAUNCH_HANDLER + "launch added message received:   " + "sender=" + sender + ", name=" + name + ", id=" + id + ", mode=" + mode + ", launch configuration=" + SeDe.byteArrayToMap(wc));
            } catch (Exception e) {
                XPLog.logException(Level.WARN, 0, null, e);
            }
        }
        String recipient = null;
        UserSession senderSession = null;
        UserSession recipientSession = null;
        ReplicatedXPSession xpSession = null;
        if (sender != null) {
            senderSession = sessionManager.getSessionByUsername(sender);
            xpSession = xpSessionManager.getXPSessionByUsername(sender, senderSession.getUserObject().getUserGroup());
            if (xpSession != null) {
                if (xpSession.getDriver() != null) {
                    if (xpSession.getDriver().getUserId().equals(sender)) {
                        if (xpSession.getNumberOfUsers() > 1) {
                            try {
                                Map<String, Long> sids = resourceManager.getResourceSequenceIDs(xpSession.getXpSessionId());
                                byte[] sidBytes = SeDe.mapToByteArray(sids);
                                for (ReplicatedUser u : xpSession.getParticipants()) {
                                    recipient = u.getUserId();
                                    if ((recipient != null) && !(recipient.equals(sender))) {
                                        recipientSession = sessionManager.getSessionByUsername(recipient);
                                        if ((recipientSession != null) && (senderSession != null)) {
                                            sendLaunchAddedMessage(recipientSession, IClientCommand.LAUNCH_HANDLER_DESTINATION, name, id, mode, wc, sidBytes);
                                            if (XPLog.isDebugEnabled()) {
                                                XPLog.printDebug(LogConstants.LOG_PREFIX_LAUNCH_HANDLER + "launch added message sent:   " + "sender=" + sender + ", recipient=" + recipient + ", name=" + name + ", id=" + id + ", mode=" + mode + ", launch configuration=" + SeDe.byteArrayToMap(wc) + ", SIDs=" + sids);
                                            }
                                        } else {
                                            XPLog.getServerLog().error("Unable to send launch added message:  " + "sender=" + sender + ", recipient=" + recipient + ", name=" + name + ", id=" + id + ", mode=" + mode + ", launch configuration=" + SeDe.byteArrayToMap(wc) + ", SIDs=" + sids);
                                        }
                                    }
                                }
                            } catch (UnexpectedReplicationState e) {
                                XPLog.logException(Level.WARN, 0, null, e);
                            }
                        }
                    } else {
                        XPLog.getServerLog().warn("Only drivers are allowed to send \"launch added messages\"!");
                    }
                }
            }
        }
    }

    /**
   * Notifies the receiver that a launch has changed.
   * 
   * @param sender
   *          the user who has changed the launch
   * @param name
   *          the name for the launch configuration
   * @param id
   *          the unique identifier for a launch configuration type extension
   * @param mode
   *          the mode of the launch
   * @param wc
   *          the configuration of the launch
   * 
   * @see de.fuh.xpairtise.common.network.IServerCommandListener#onLaunchChangedMessage(java.lang.String,
   *      java.lang.String, java.lang.String, java.lang.String, byte[])
   */
    public void onLaunchChangedMessage(String sender, String name, String id, String mode, byte[] wc) {
        if (XPLog.isDebugEnabled()) {
            try {
                XPLog.printDebug(LogConstants.LOG_PREFIX_LAUNCH_HANDLER + "launch changed message received:  " + "sender=" + sender + ", name=" + name + ", id=" + id + ", mode=" + mode + ", launch configuration=" + SeDe.byteArrayToMap(wc));
            } catch (Exception e) {
                XPLog.logException(Level.WARN, 0, null, e);
            }
        }
        String recipient = null;
        UserSession senderSession = null;
        UserSession recipientSession = null;
        ReplicatedXPSession xpSession = null;
        senderSession = sessionManager.getSessionByUsername(sender);
        xpSession = xpSessionManager.getXPSessionByUsername(sender, senderSession.getUserObject().getUserGroup());
        if (sender != null) {
            if (xpSession != null) {
                if (xpSession.getDriver() != null) {
                    if (xpSession.getDriver().getUserId().equals(sender)) {
                        if (xpSession.getNumberOfUsers() > 1) {
                            for (ReplicatedUser u : xpSession.getParticipants()) {
                                recipient = u.getUserId();
                                if ((recipient != null) && !(recipient.equals(sender))) {
                                    recipientSession = sessionManager.getSessionByUsername(recipient);
                                    if ((recipientSession != null) && (senderSession != null)) {
                                        sendLaunchChangedMessage(recipientSession, IClientCommand.LAUNCH_HANDLER_DESTINATION, name, id, mode, wc);
                                        if (XPLog.isDebugEnabled()) {
                                            XPLog.printDebug(LogConstants.LOG_PREFIX_LAUNCH_HANDLER + "launch changed message sent:  " + "sender=" + sender + ", recipient=" + recipient + ", name=" + name + ", id=" + id + ", mode=" + mode + ", launch configuration=" + SeDe.byteArrayToMap(wc));
                                        }
                                    } else {
                                        XPLog.getServerLog().error("Unable to send launch changed message: " + "sender=" + sender + ", recipient=" + recipient + ", name=" + name + ", id=" + id + ", mode=" + mode + ", launch configuration=" + SeDe.byteArrayToMap(wc));
                                    }
                                }
                            }
                        }
                    } else {
                        XPLog.getServerLog().warn("Only drivers are allowed to send \"launch changed messages\"!");
                    }
                }
            }
        }
    }

    /**
   * Notifies the receiver that a launch has been removed.
   * 
   * @param sender
   *          the user who has removed the launch
   * @param name
   *          the name for the launch configuration
   * @param id
   *          the unique identifier for a launch configuration type extension
   * @param mode
   *          the mode of the launch
   * @param wc
   *          the configuration of the launch
   * 
   * @see de.fuh.xpairtise.common.network.IServerCommandListener#onLaunchRemovedMessage(java.lang.String,
   *      java.lang.String, java.lang.String, java.lang.String, byte[])
   */
    public void onLaunchRemovedMessage(String sender, String name, String id, String mode, byte[] wc) {
        if (XPLog.isDebugEnabled()) {
            try {
                XPLog.printDebug(LogConstants.LOG_PREFIX_LAUNCH_HANDLER + "launch removed message received: " + "sender=" + sender + ", name=" + name + ", id=" + id + ", mode=" + mode + ", launch configuration=" + SeDe.byteArrayToMap(wc));
            } catch (Exception e) {
                XPLog.logException(Level.WARN, 0, null, e);
            }
        }
        String recipient = null;
        UserSession senderSession = null;
        UserSession recipientSession = null;
        ReplicatedXPSession xpSession = null;
        if (sender != null) {
            senderSession = sessionManager.getSessionByUsername(sender);
            xpSession = xpSessionManager.getXPSessionByUsername(sender, senderSession.getUserObject().getUserGroup());
            if (xpSession != null) {
                if (xpSession.getDriver() != null) {
                    if (xpSession.getDriver().getUserId().equals(sender)) {
                        if (xpSession.getNumberOfUsers() > 1) {
                            for (ReplicatedUser u : xpSession.getParticipants()) {
                                recipient = u.getUserId();
                                if ((recipient != null) && !(recipient.equals(sender))) {
                                    recipientSession = sessionManager.getSessionByUsername(recipient);
                                    if ((recipientSession != null) && (senderSession != null)) {
                                        sendLaunchRemovedMessage(recipientSession, IClientCommand.LAUNCH_HANDLER_DESTINATION, name, id, mode, wc);
                                        if (XPLog.isDebugEnabled()) {
                                            XPLog.printDebug(LogConstants.LOG_PREFIX_LAUNCH_HANDLER + "launch removed message sent: " + "sender=" + sender + ", recipient=" + recipient + ", name=" + name + ", id=" + id + ", mode=" + mode + ", launch configuration=" + SeDe.byteArrayToMap(wc));
                                        }
                                    } else {
                                        XPLog.getServerLog().error("Unable to send launch removed message: " + "sender=" + sender + ", recipient=" + recipient + ", name=" + name + ", id=" + id + ", mode=" + mode + ", launch configuration=" + SeDe.byteArrayToMap(wc));
                                    }
                                }
                            }
                        }
                    } else {
                        XPLog.getServerLog().warn("Only drivers are allowed to send \"launch removed messages\"!");
                    }
                }
            }
        }
    }

    private void sendLaunchAddedMessage(UserSession session, int dest, String name, String id, String mode, byte[] wc, byte[] sids) {
        if ((session != null) && (session instanceof UserSession)) {
            try {
                session.getCommandInterface().sendShowLaunchAddedMessage(dest, name, id, mode, wc, sids);
            } catch (NetworkException e) {
                XPLog.logException(Level.WARN, 0, null, e);
            }
        }
    }

    private void sendLaunchChangedMessage(UserSession session, int dest, String name, String id, String mode, byte[] wc) {
        if ((session != null) && (session instanceof UserSession)) {
            try {
                session.getCommandInterface().sendShowLaunchChangedMessage(dest, name, id, mode, wc);
            } catch (NetworkException e) {
                XPLog.logException(Level.WARN, 0, null, e);
            }
        }
    }

    private void sendLaunchRemovedMessage(UserSession session, int dest, String name, String id, String mode, byte[] wc) {
        if ((session != null) && (session instanceof UserSession)) {
            try {
                session.getCommandInterface().sendShowLaunchRemovedMessage(dest, name, id, mode, wc);
            } catch (NetworkException e) {
                XPLog.logException(Level.WARN, 0, null, e);
            }
        }
    }
}
