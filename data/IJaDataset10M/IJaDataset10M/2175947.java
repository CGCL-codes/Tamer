package net.sf.jimo.modules.msn.impl;

import java.io.IOException;
import java.net.UnknownHostException;
import java.security.NoSuchAlgorithmException;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.StringTokenizer;
import java.util.Vector;
import net.sf.jimo.api.JIMOConstants;
import net.sf.jimo.modules.im.api.Buddy;
import net.sf.jimo.modules.im.api.Conference;
import net.sf.jimo.modules.im.api.Message;
import net.sf.jimo.modules.im.api.Response;
import net.sf.jimo.modules.im.api.TextComponent;
import net.sf.jimo.modules.im.api.exception.IMException;
import net.sf.jimo.modules.im.api.net.ProxyInfo;
import net.sf.jimo.modules.im.api.exception.IMIllegalStateException;
import net.sf.jimo.modules.im.api.exception.IMIllegalArgumentException;
import net.sf.jimo.modules.im.api.exception.IMUnsupportedOperationException;

/**
 * Notification server is where the entire session is maintained. Almost everything happens
 * here, including state changes (user goes offline), list retrieval and changes, chat
 * requests, and email notification.
 */
class NotificationServer extends MsnServer {

    /**
	 * Event processor for this server.
	 */
    private EventProcessor processor;

    /**
	 * MsnProtocol instance that owns this server.
	 */
    private MsnProtocol protocol;

    /**
	 * Maps a group ID to the group name.
	 */
    private Hashtable allGroups;

    /**
	 * List of all buddies.
	 */
    private Vector allBuddies;

    /**
	 * Buddies in allow list.
	 */
    private Vector allowList;

    /**
	 * Buddies in block list.
	 */
    private Vector blockList;

    /**
	 * This is a mapping of all conference objects to their
	 * corresponding switchboard sessions.
	 */
    private Hashtable confSBMap;

    /**
	 * Constructs a notification server with a specified event processor
	 * and proxy information.
	 *
	 * @param protocol the MsnProtocol instance that owns this server.
	 * @param processor the event processor for this server.
	 * @param host the host name to connect to.
	 * @param port the TCP/IP port to connect to.
	 * @param info proxy information to be used for connections.
	 * @throws UnknownHostException if host is not known.
	 * @throws IOException if an I/O error occurs while connecting to the host.
	 * @throws IMIllegalStateException if info is not initialized properly.
	 */
    NotificationServer(MsnProtocol protocol, EventProcessor processor, String host, int port, ProxyInfo info) throws java.io.IOException, IMIllegalStateException {
        super(host, port, info);
        this.protocol = protocol;
        this.processor = processor;
        this.allGroups = new Hashtable();
        this.allBuddies = new Vector();
        this.allowList = new Vector();
        this.blockList = new Vector();
        this.confSBMap = new Hashtable();
        Command cmd = new Command("VER");
        cmd.addParam("MSNP8");
        sendToServer(cmd, "processVersion");
    }

    void changeAlias(String alias) {
        Command rea = new Command("REA");
        rea.addParam(protocol.getUsername());
        rea.addParam(alias.replaceAll(" ", "%20"));
        sendToServer(rea, "doNothing");
    }

    /**
	 * Process the reply for VER command.
	 *
	 * @param cmd the reply from the server.
	 */
    void processVersion(AbstractCommand cmd) {
        if (cmd instanceof Command) {
            if ("VER".equals(cmd.getType()) && "MSNP8".equals(cmd.getParam(0))) {
                Command cvr = new Command("CVR");
                cvr.addParam("0x0409");
                String os = System.getProperty("os.name").toLowerCase();
                if (os.length() > 3) os = os.substring(0, 3);
                cvr.addParam(os);
                cvr.addParam(Util.urlDecode(System.getProperty("os.version")));
                cvr.addParam(Util.urlEncode(System.getProperty("os.arch")));
                cvr.addParam("JIMO");
                cvr.addParam(JIMOConstants.JIMO_VERSION);
                cvr.addParam("JIMO");
                cvr.addParam(Util.urlEncode(this.protocol.getUsername()));
                sendToServer(cvr, "processClientVersion");
                return;
            }
        }
        shutdown();
        processor.connectFailed("Version transaction failed - " + cmd);
    }

    /**
	 * Process the reply for CVR command.
	 *
	 * @param cmd the reply from the server.
	 */
    void processClientVersion(AbstractCommand cmd) {
        if (cmd instanceof Command) {
            if ("CVR".equals(cmd.getType())) {
                Command usr = new Command("USR");
                usr.addParam("TWN");
                usr.addParam("I");
                usr.addParam(protocol.getUsername());
                sendToServer(usr, "processAuthInitial");
                return;
            }
        }
        shutdown();
        processor.connectFailed("Client versioning was rejected - " + cmd);
    }

    /**
	 * Process the reply for initial USR command.
	 *
	 * @param cmd the reply from the server.
	 */
    void processAuthInitial(AbstractCommand cmd) {
        if (cmd instanceof Command) {
            if ("USR".equals(cmd.getType()) && "TWN".equals(cmd.getParam(0)) && "S".equals(cmd.getParam(1))) {
                try {
                    String challenge = cmd.getParam(2);
                    String str = Util.passportAuthenticate(protocol.getUsername(), protocol.getPassword(), challenge, protocol.getProxyInfo());
                    if (str == null) {
                        processor.connectFailed("Passport authentication failed");
                        return;
                    }
                    Command usr = new Command("USR");
                    usr.addParam("TWN");
                    usr.addParam("S");
                    usr.addParam(str);
                    sendToServer(usr, "processAuthSubsequent");
                    return;
                } catch (IOException e) {
                    shutdown();
                    processor.connectFailed("I/O error while authenticating");
                    e.printStackTrace();
                    return;
                }
            } else if ("XFR".equals(cmd.getType()) && "NS".equals(cmd.getParam(0))) {
                shutdown();
                String server = cmd.getParam(1);
                protocol.switchServer(server);
                return;
            }
        }
        shutdown();
        processor.connectFailed("Unknown authentication mechanism - " + cmd);
    }

    /**
	 * Process the reply for subsequent USR command.
	 *
	 * @param cmd the reply from the server.
	 */
    void processAuthSubsequent(AbstractCommand cmd) {
        if (cmd instanceof Command) {
            if ("USR".equals(cmd.getType()) && "OK".equals(cmd.getParam(0))) {
                processor.connected();
                Command syn = new Command("SYN");
                syn.addParam("0");
                sendToServer(syn, "processSyncLists");
                return;
            }
        }
        shutdown();
        processor.connectFailed("Authentication failed - " + cmd);
    }

    private int lstReplyCount;

    private int lstReceivedCount;

    /**
	 * Process the reply for SYN command.
	 *
	 * @param cmd the reply from the server.
	 */
    void processSyncLists(AbstractCommand cmd) {
        if (cmd instanceof Command) {
            if ("SYN".equals(cmd.getType())) {
                lstReplyCount = Integer.parseInt(cmd.getParam(1));
            }
        }
    }

    /**
	 * Process the reply for BLP command.
	 *
	 * @param cmd the reply from the server.
	 */
    void processPrivacySetting(AbstractCommand cmd) {
        if (cmd instanceof Command) {
            if ("BLP".equals(cmd.getType()) && "AL".equals(cmd.getParam(1))) {
                Command chg = new Command("CHG");
                chg.addParam("NLN");
                sendToServer(chg, "processInitialStatus");
                return;
            }
        }
        shutdown();
        processor.connectFailed("Unable to update privacy setting - " + cmd);
    }

    /**
	 * Process the reply for CHG command.
	 *
	 * @param cmd the reply from the server.
	 */
    void processInitialStatus(AbstractCommand cmd) {
        if (cmd instanceof Command) {
            if ("CHG".equals(cmd.getType()) && "NLN".equals(cmd.getParam(0))) {
                return;
            } else if ("ILN".equals(cmd.getType())) {
                String statusCode = cmd.getParam(0);
                String passport = cmd.getParam(1);
                String alias = cmd.getParam(2);
                if (statusCode != null && passport != null) {
                    Buddy buddy = new Buddy(protocol, passport);
                    buddy.setAlias(alias);
                    buddy.setStatus(Util.getVerboseStatus(statusCode));
                    processor.buddyStatusChanged(buddy);
                }
                return;
            }
        }
        shutdown();
        processor.connectFailed("Unable to update initial status - " + cmd);
    }

    /**
	 * Change the status message.
	 *
	 * @param code the new status code.
	 */
    void changeStatus(String code) {
        Command chg = new Command("CHG");
        chg.addParam(code);
        sendToServer(chg, "doNothing");
    }

    /**
	 * Starts a switchboard session for conferences.
	 *
	 * @param conf the conference that uses this switchboard session.
	 */
    void startSBSession(Conference conf) {
        Command xfr = new Command("XFR");
        xfr.addParam("SB");
        sendToServer(xfr, "processSwitchSB");
        confSBMap.put(conf, new Object());
    }

    /**
	 * Process the response for a switchboard transfer request.
	 *
	 * @param cmd the reply from the server.
	 */
    void processSwitchSB(AbstractCommand cmd) {
        if (!(cmd instanceof Command) || !"XFR".equals(cmd.getType()) || !"SB".equals(cmd.getParam(0))) return;
        String server = cmd.getParam(1);
        String hash = cmd.getParam(3);
        if (server == null || hash == null) return;
        int index = server.indexOf(':');
        if (index == -1) return;
        Enumeration e = confSBMap.keys();
        Conference conf = null;
        while (e.hasMoreElements()) {
            conf = (Conference) e.nextElement();
            Object sb = confSBMap.get(conf);
            if (!(sb instanceof SwitchboardServer)) break; else conf = null;
        }
        if (conf == null) return;
        try {
            String host = server.substring(0, index);
            int port = Integer.parseInt(server.substring(index + 1));
            SwitchboardServer sb = new SwitchboardServer(protocol, processor, hash, host, port, conf, protocol.getProxyInfo());
            synchronized (confSBMap) {
                confSBMap.put(conf, sb);
                confSBMap.notify();
            }
        } catch (NumberFormatException nfe) {
            nfe.printStackTrace();
            System.exit(1);
        } catch (IOException ioe) {
            Message msg = new Message();
            msg.addComponent(new TextComponent("Unable to initiate an IM session: " + ioe.getMessage()));
            processor.protocolMessageReceived(msg);
        } catch (IMIllegalStateException ise) {
            Message msg = new Message();
            msg.addComponent(new TextComponent("Unable to initiate an IM session: " + ise.getMessage()));
            processor.protocolMessageReceived(msg);
        }
    }

    /**
	 * Quit from a conference.
	 *
	 * @param conf the conference to quit from
	 */
    void quitSBSession(Conference conf) {
        SwitchboardServer sb = (SwitchboardServer) confSBMap.get(conf);
        if (sb != null) {
            Command out = new Command("OUT");
            sb.sendToServer(out, "doNothing");
            sb.shutdown();
            confSBMap.remove(conf);
        }
    }

    /**
	 * Send a conference message.
	 *
	 * @param conf the conference to send message to.
	 * @param message the message to be sent.
	 */
    void sendConferenceMessage(Conference conf, Message message) {
        SwitchboardServer sb = (SwitchboardServer) confSBMap.get(conf);
        if (sb != null) sb.sendMessage(message); else {
        }
    }

    /**
	 * Adds a buddy to the forward list.
	 *
	 * @param buddy the buddy to be added.
	 * @throws IMIllegalArgumentException if the group of the buddy is not specified.
	 */
    void addBuddyToForwardList(Buddy buddy) throws IMIllegalArgumentException {
        String group = buddy.getGroup();
        if (group == null) {
            if (allGroups.keys().hasMoreElements()) group = ((Integer) allGroups.keys().nextElement()).toString();
        }
        Command add = new Command("ADD");
        add.addParam("FL");
        String username = buddy.getUsername();
        add.addParam(username);
        String alias = buddy.getAlias();
        alias = alias == null ? username : alias;
        add.addParam(alias);
        Enumeration e = allGroups.keys();
        if (e.hasMoreElements()) {
            add.addParam(((Integer) e.nextElement()).toString());
            sendToServer(add, "doNothing");
            return;
        }
        int groupID = addNewBuddyGroup(group);
        if (groupID != -1) {
            add.addParam(String.valueOf(groupID));
            sendToServer(add, "doNothing");
            return;
        }
    }

    /**
	 * Removes a buddy from the forward list.
	 *
	 * @param buddy the buddy to be added.
	 * @throws IMIllegalArgumentException if the group of the buddy is not specified.
	 */
    void removeBuddyFromForwardList(Buddy buddy) throws IMIllegalArgumentException {
        String group = buddy.getGroup();
        if (group == null) throw new IMIllegalArgumentException("Group name of the buddy is not specified");
        Command add = new Command("REM");
        add.addParam("FL");
        String username = buddy.getUsername();
        add.addParam(username);
        Enumeration e = allGroups.keys();
        while (e.hasMoreElements()) {
            Integer groupID = (Integer) e.nextElement();
            String groupName = (String) allGroups.get(groupID);
            if (group.equals(groupName)) {
                add.addParam(groupID.toString());
                sendToServer(add, "doNothing");
                return;
            }
        }
        processor.buddyDeleteFailed(buddy, "Buddy group does not exist - " + group);
    }

    /**
	 * Adds a buddy to the blocked list.
	 *
	 * @param buddy the buddy to be added.
	 */
    void addBuddyToBlockedList(Buddy buddy) {
        Command add = new Command("ADD");
        add.addParam("BL");
        String username = buddy.getUsername();
        add.addParam(username);
        String alias = buddy.getAlias();
        alias = alias == null ? username : alias;
        add.addParam(alias);
        sendToServer(add, "processBuddyBlock");
    }

    /**
	 * Removes a buddy from the blocked list.
	 *
	 * @param buddy the buddy to be added.
	 */
    void removeBuddyFromBlockedList(Buddy buddy) {
        Command add = new Command("REM");
        add.addParam("BL");
        String username = buddy.getUsername();
        add.addParam(username);
        sendToServer(add, "processBuddyUnblock");
    }

    /**
	 * Create a new buddy group. This method blocks until it gets
	 * a reply from the MSN server.
	 *
	 * @param group the name of the group to be added
	 * @return the group id of the newly created group
	 */
    private int addNewBuddyGroup(String group) {
        Command adg = new Command("ADG");
        adg.addParam(Util.urlEncode(group));
        adg.addParam("0");
        AbstractCommand cmd = sendToServer(adg);
        if (cmd instanceof Command) {
            if ("ADG".equals(cmd.getType())) {
                return Integer.parseInt(cmd.getParam(2));
            }
        }
        return -1;
    }

    /**
	 * This method does nothing. It used for callback where we don't have
	 * anything to do.
	 */
    void doNothing(AbstractCommand cmd) {
    }

    /**
	 * Processes messages received from MSN server.
	 *
	 * @param msg the message received from server.
	 */
    protected synchronized void processMessage(MsnMessage msg) {
        String type = msg.getHeaderField("Content-Type");
        if ("text/x-msmsgsinitialemailnotification".equals(type)) {
            String body = msg.getBody();
            int linePos = body.indexOf("Inbox-Unread:");
            if (linePos != -1) {
                int lineEnd = body.indexOf(body, linePos + 13);
                if (lineEnd != -1) {
                    int count = Integer.parseInt(body.substring(linePos + 13, lineEnd).trim());
                    processor.mailNotificationReceived(count, null, null);
                }
            }
        } else if ("application/x-msmsgsemailnotification".equals(type)) {
        }
    }

    /**
	 * Processes asynchronous commands received from MSN server.
	 *
	 * @param cmd the command received from server.
	 */
    protected void processAsyncCommand(AbstractCommand cmd) {
        String type = cmd.getType();
        if ("LSG".equals(type)) {
            Integer groupID = new Integer(cmd.getParam(0));
            String groupName = Util.urlDecode(cmd.getParam(1));
            allGroups.put(groupID, groupName);
        } else if ("LST".equals(type)) {
            String passport = cmd.getParam(0);
            String alias = Util.urlDecode(cmd.getParam(1));
            int memberLists = Integer.parseInt(cmd.getParam(2));
            Integer[] memberGroups = null;
            if (cmd.getParamCount() == 4) {
                StringTokenizer tok = new StringTokenizer(cmd.getParam(3), ",");
                memberGroups = new Integer[tok.countTokens()];
                for (int i = 0; tok.hasMoreTokens(); i++) memberGroups[i] = new Integer(tok.nextToken());
            }
            if ((memberLists & 1) != 0) {
                for (int i = 0; i < memberGroups.length; i++) {
                    String group = (String) allGroups.get(memberGroups[i]);
                    Buddy buddy = new Buddy(protocol, passport, group);
                    buddy.setAlias(alias);
                    allBuddies.add(buddy);
                }
            }
            if ((memberLists & 2) != 0) {
                Buddy buddy = new Buddy(protocol, passport);
                buddy.setAlias(alias);
                allowList.add(buddy);
            }
            if ((memberLists & 4) != 0) {
                Buddy buddy = new Buddy(protocol, passport);
                buddy.setAlias(alias);
                blockList.add(buddy);
            }
            if (++lstReceivedCount == lstReplyCount) {
                Buddy[] buddies = (Buddy[]) allBuddies.toArray(new Buddy[0]);
                processor.buddyListReceived(buddies);
                Command blp = new Command("BLP");
                blp.addParam("AL");
                sendToServer(blp, "processPrivacySetting");
            }
        } else if ("CHL".equals(type)) {
            String challenge = cmd.getParam(1);
            if (challenge != null) handleChallenge(challenge);
            return;
        } else if ("RNG".equals(type)) handleSBInvitation(cmd); else if ("NLN".equals(type)) {
            String passport = cmd.getParam(1);
            String alias = Util.urlDecode(cmd.getParam(2));
            String status = Util.getVerboseStatus(cmd.getParam(0));
            Buddy buddy = new Buddy(this.protocol, passport);
            buddy.setAlias(alias);
            buddy.setStatus(status);
            processor.buddyStatusChanged(buddy);
        } else if ("FLN".equals(type)) {
            String passport = cmd.getParam(0);
            Buddy buddy = new Buddy(this.protocol, passport);
            buddy.setStatus(null);
            processor.buddyStatusChanged(buddy);
        }
    }

    /**
	 * Respond to a CHL from the MSN server.
	 *
	 * @param challenge the challege string sent by the server
	 */
    private void handleChallenge(String challenge) {
        try {
            String str = challenge + "Q1P7W2E4J9R8U3S5";
            MsnQuery query = new MsnQuery(Util.getMD5Hash(str));
            sendToServer(query, "processQueryResult");
        } catch (NoSuchAlgorithmException e) {
            shutdown();
            processor.disconnected();
        }
    }

    /**
	 * Process a swicthboard session invitation.
	 *
	 * @param cmd the RNG command sent by the server for invitation.
	 */
    private void handleSBInvitation(AbstractCommand cmd) {
        String sessionID = cmd.getParam(0);
        String server = cmd.getParam(1);
        String authString = cmd.getParam(3);
        String passport = cmd.getParam(4);
        String alias = Util.urlDecode(cmd.getParam(5));
        try {
            Buddy host = new Buddy(protocol, passport);
            host.setAlias(alias);
            Buddy[] buddies = new Buddy[] { host };
            Conference conf = new Conference(protocol, host, buddies);
            Response res = processor.conferenceInvitationReceived(conf, "");
            if (res != null && res.isAccepted()) joinSBSession(sessionID, server, authString, conf);
        } catch (IMException e) {
        }
    }

    /**
	 * Process the reply for QRY command.
	 *
	 * @param cmd the reply from the server.
	 */
    void processQueryResult(AbstractCommand cmd) {
        if (cmd instanceof Command && "QRY".equals(cmd.getType())) return;
        shutdown();
        processor.disconnected();
    }

    /**
	 * Returns an existing SB session with a single buddy.
	 *
	 * @param buddy the only buddy in this SB session.
	 * @return the SB server having this session.
	 */
    SwitchboardServer getExistingSBSession(Buddy buddy) {
        Enumeration e = confSBMap.elements();
        while (e.hasMoreElements()) {
            SwitchboardServer server = (SwitchboardServer) e.nextElement();
            if (server.isIMSession(buddy)) return server;
        }
        return null;
    }

    /**
	 * Join a SB session when we are invited.
	 *
	 * @param sessionID session ID of this SB session.
	 * @param server the server to connect to in the form hostname:port
	 * @param authString the authentication string to be used for joining
	 * @param conf the conference object representing this SB session.
	 */
    void joinSBSession(String sessionID, String server, String authString, Conference conf) {
        int index = server.indexOf(':');
        if (index == -1) return;
        try {
            String host = server.substring(0, index);
            int port = Integer.parseInt(server.substring(index + 1));
            ProxyInfo info = protocol.getProxyInfo();
            SwitchboardServer sb = new SwitchboardServer(protocol, processor, host, port, info, conf);
            confSBMap.put(conf, sb);
            sb.join(authString, sessionID);
        } catch (IOException e) {
            Message msg = new Message();
            msg.addComponent(new TextComponent("Unable to initiate an IM session: " + e.getMessage()));
            processor.protocolMessageReceived(msg);
        } catch (IMIllegalStateException e) {
            Message msg = new Message();
            msg.addComponent(new TextComponent("Unable to initiate an IM session: " + e.getMessage()));
            processor.protocolMessageReceived(msg);
        }
    }

    /**
	 * Returns an SB server corresponding to this conference. If nothing is
	 * created yet, wait till one becomes available.
	 *
	 * @param conf conference for which SB server is to be returned
	 * @return SB server for this conference object
	 */
    SwitchboardServer waitForSBSession(Conference conf) {
        if (!confSBMap.containsKey(conf)) return null; else {
            while (true) {
                try {
                    synchronized (confSBMap) {
                        Object obj = confSBMap.get(conf);
                        if (obj != null && obj instanceof SwitchboardServer) return (SwitchboardServer) obj; else confSBMap.wait();
                    }
                } catch (InterruptedException e) {
                }
            }
        }
    }

    /**
	 * Invoked when an SB session terminates abnormally.
	 *
	 * @param server the SB server that terminated.
	 */
    void switchBoardTerminated(SwitchboardServer server) {
        Enumeration e = confSBMap.keys();
        while (e.hasMoreElements()) {
            Conference conf = (Conference) e.nextElement();
            if (server.equals(confSBMap.get(conf))) {
                confSBMap.remove(conf);
                return;
            }
        }
    }

    /**
	 * Invoked when the associated reader thread exits abnormally. This
	 * method shuts down the IM session.
	 */
    protected void readerExited() {
        shutdown();
        processor.disconnected();
    }

    /**
	 * Invoked when the associated writer thread exits abnormally. This
	 * method shuts down the IM session.
	 */
    protected void writerExited() {
        shutdown();
        processor.disconnected();
    }
}
