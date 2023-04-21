package vavi.net.im.protocol.oscar;

import java.io.IOException;
import java.util.Properties;
import java.util.logging.Logger;
import vavi.net.im.Buddy;
import vavi.net.im.Group;
import vavi.net.im.Message;
import vavi.net.im.Session;
import vavi.net.im.SmileyComponent;
import vavi.net.im.protocol.Protocol;

/**
 * OscarProtocol.
 * 
 * @author Raghu
 * @author <a href="mailto:vavivavi@yahoo.co.jp">Naohide Sano</a> (nsano)
 */
public class OscarProtocol extends Protocol {

    /** */
    static Logger log = Logger.getLogger(OscarProtocol.class.getName());

    /** */
    private static final String PROTOCOL_NAME = "AOL Instant Messenger";

    /** */
    private static final String DEFAULT_HOST = "login.oscar.aol.com";

    /** 22; - for work when firewall blocks me! */
    private static final int DEFAULT_PORT = 5190;

    /**
     * host.
     */
    private String host;

    /**
     * port.
     */
    private int port;

    /** AIM Command Handler */
    private OscarConnection connection;

    /**
     * Default Constructor
     */
    public OscarProtocol() {
        try {
            Properties props = new Properties();
            props.load(OscarProtocol.class.getResourceAsStream("aim.properties"));
            host = props.getProperty("aim.host");
            port = Integer.parseInt(props.getProperty("aim.port"));
        } catch (IOException e) {
            log.warning(String.valueOf(e));
            host = DEFAULT_HOST;
            port = DEFAULT_PORT;
        }
        statusMap.put("online", 0x0000);
        statusMap.put("away", 0x0001);
        statusMap.put("dnd", 0x0002);
        statusMap.put("na", 0x0004);
        statusMap.put("busy", 0x0010);
        statusMap.put("invisible", 0x0100);
        features.put(Feature.BuddyAddRequestSupported, true);
        features.put(Feature.BuddyGroupSupported, true);
        features.put(Feature.IgnoreSupported, true);
        features.put(Feature.OfflineMessageSupported, false);
        features.put(Feature.TypingNotifySupported, true);
        features.put(Feature.ConferenceSupported, false);
        features.put(Feature.MailNotifySupported, false);
        features.put(Feature.InvisibleSupported, true);
        features.put(Feature.BuddyNameAliasSupported, false);
        connection = new OscarConnection(listeners);
    }

    /**
     * @param status 
     */
    protected void changeStatusInternal(String status) throws IOException {
        Integer statusFlag = null;
        if (status == null) {
            statusFlag = (Integer) statusMap.get("invisible");
        } else {
            statusFlag = (Integer) statusMap.get(status);
            if (statusFlag == null) {
                throw new IllegalArgumentException("status " + status + " not supported");
            }
        }
        connection.changeStatus(statusFlag);
    }

    /** */
    protected Object localizeStatusInternal(String status) {
        return 0;
    }

    /**
     * Connect to the AIM server
     * 
     * @param props
     */
    protected void connectInternal(Properties props) throws IOException {
        connection.setListeners(this.listeners);
        connection.connect(host, port, props);
    }

    /**
     * Disconnect from AIM
     */
    protected void disconnectInternal() throws IOException {
        if (connection.isRunning()) {
            connection.disconnect();
            connection.finalize();
        }
    }

    /**
     * Gets the protocol name
     * 
     * @return String containing the protocol name
     */
    public String getProtocolName() {
        return PROTOCOL_NAME;
    }

    /**
     * Starts a conference
     */
    protected void startSessionInternal(Session session) throws IOException {
    }

    /**
     * @param message invitation message
     */
    protected void startSessionInternal(Session session, String message) throws IOException {
    }

    /**
     * Quits a conference.
     * 
     * @param session conference to quit
     */
    protected void quitSessionInternal(Session session) throws IOException {
    }

    /**
     * Sends an instant message to the conference.
     * 
     * @param session Session
     * @param message Message to send to the conference
     */
    protected void sendSessionMessageInternal(Session session, Message message) throws IOException {
    }

    /**
     * Adds a buddy to the Buddy List.
     * 
     * @param buddy Buddy to be added to the Buddy List
     */
    protected void addToBuddyListInternal(Buddy buddy) throws IOException {
        connection.addToBuddyList(buddy);
    }

    /**
     * Deletes a buddy from the Buddy List.
     * 
     * @param buddy Buddy to be deleted from the Buddy List
     */
    protected void deleteFromBuddyListInternal(Buddy buddy) throws IOException {
        connection.deleteFromBuddyList(buddy);
    }

    /**
     * Ignores a buddy.
     * 
     * @param buddy Buddy to ignore
     */
    protected void ignoreBuddyInternal(Buddy buddy) throws IOException {
        connection.ignoreBuddy(buddy);
    }

    /**
     * Unignores a buddy.
     * 
     * @param buddy Buddy to unignore
     */
    protected void unignoreBuddyInternal(Buddy buddy) throws IOException {
        connection.unIgnoreBuddy(buddy);
    }

    /**
     * Sends an instant message to a buddy.
     * 
     * @param session Buddy to send message to
     * @param message Message to send
     */
    protected void sendInstantMessageInternal(Session session, Message message) throws IOException {
        connection.sendInstantMessage(session.getParticipants()[0], message);
    }

    /** */
    protected void startTypingInternal(Buddy buddy) throws IOException {
        connection.typingStarted(buddy);
    }

    /** */
    protected void stopTypingInternal(Buddy buddy) throws IOException {
        connection.typingStopped(buddy);
    }

    /** */
    public SmileyComponent[] getSupportedSmileys() {
        return null;
    }

    /** */
    protected void changeBuddyAliasInternal(Buddy buddy, String alias) throws IOException {
    }

    /** */
    protected void addGroupInternal(Group group) throws IOException {
    }

    /** */
    protected void removeGroupInternal(Group group) throws IOException {
    }

    /** */
    protected void changeGroupNameInternal(Group group, String newName) throws IOException {
    }

    /** */
    protected void changeGroupOfBuddyInternal(Buddy buddy, Group oldGroup, Group newGroup) throws IOException {
    }
}
