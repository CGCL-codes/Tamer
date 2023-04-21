package net.sourceforge.herald;

import java.lang.reflect.*;
import java.net.*;
import java.util.*;

/**
 * This class provides basic abstraction and storage for packets as
 * specified by the MSN Messenger V1.0 protocol.
 *
 * @author Chad Gibbons
 */
public class MessengerPacket {

    protected String rawPacket;

    protected String command;

    protected int transactionID;

    protected MessengerPacket() {
    }

    /**
     * Creates a basic MessengerPacket from a raw string of packet
     * data.
     *
     * @exception   ProtocolException   if the raw packet does not contain a
     *                                  command and transaction ID.
     */
    public MessengerPacket(String rawPacket) throws ProtocolException {
        init(rawPacket);
    }

    /**
     * Performs private object initialization. Derivitive classes
     * should use this to perform raw packet initialization when
     * required.
     *
     * @exception   ProtocolException   if the raw packet does not contain a
     *                                  command and transaction ID.
     */
    protected void init(String rawPacket) throws ProtocolException {
        this.rawPacket = rawPacket;
        StringTokenizer t = new StringTokenizer(rawPacket);
        command = t.nextToken();
        try {
            transactionID = Integer.parseInt(t.nextToken());
        } catch (NumberFormatException ex) {
            throw new ProtocolException("invalid transaction ID");
        }
    }

    /**
     * Returns the command sequence referred to in this packet.
     *
     * @returns String  the command sequence referred to in this packet.
     */
    public String getCommand() {
        return command;
    }

    /**
     * Returns the transaction ID referred to in this packet.
     *
     * @returns int the transaction ID referred to in this packet.
     */
    public int getTransactionID() {
        return transactionID;
    }

    /**
     * Returns the raw packet text of this packet.
     *
     * @returns String  the raw packet text of this packet.
     */
    public String toString() {
        return rawPacket;
    }

    /**
     * Returns the appropriate child class object for the provided
     * raw packet text.
     *
     * @returns MessengerPacket a object representing the raw packet 
     *                          data
     * @exception   ProtocolException   if the raw packet is malformed for the
     *                                  current raw packet text
     */
    public static MessengerPacket getInstance(String rawPacket) throws ProtocolException {
        String cmd = null;
        try {
            StringTokenizer st = new StringTokenizer(rawPacket);
            cmd = st.nextToken();
        } catch (NoSuchElementException ex) {
            throw new ProtocolException("packet missing command field");
        }
        Object o = commands.get(cmd);
        if (o == null) {
            throw new ProtocolException("unable to find command from packet");
        }
        Class c = (Class) o;
        Class[] argsClass = new Class[] { String.class };
        Object[] args = new Object[] { rawPacket };
        Constructor[] cons = c.getConstructors();
        MessengerPacket p = null;
        try {
            Constructor strArgConstructor = c.getConstructor(argsClass);
            p = (MessengerPacket) strArgConstructor.newInstance(args);
        } catch (Exception ex) {
            throw new ProtocolException("unable to create instance of packet");
        }
        return p;
    }

    /**
     * A Hashtable of command sequences and their concrete packet 
     * class types.
     */
    private static Hashtable commands = new Hashtable();

    static {
        commands.put(MessengerProtocol.CMD_ACK, ACKPacket.class);
        commands.put(MessengerProtocol.CMD_ANS, SwitchboardAcceptPacket.class);
        commands.put(MessengerProtocol.CMD_BLP, BLPPacket.class);
        commands.put(MessengerProtocol.CMD_BYE, ByePacket.class);
        commands.put(MessengerProtocol.CMD_CAL, CallPacket.class);
        commands.put(MessengerProtocol.CMD_CHG, ChangePacket.class);
        commands.put(MessengerProtocol.CMD_FLN, OfflinePacket.class);
        commands.put(MessengerProtocol.CMD_GTC, GTCPacket.class);
        commands.put(MessengerProtocol.CMD_INF, PolicyPacket.class);
        commands.put(MessengerProtocol.CMD_ILN, InitialStatePacket.class);
        commands.put(MessengerProtocol.CMD_IRO, InitialRosterPacket.class);
        commands.put(MessengerProtocol.CMD_JOI, JoinPacket.class);
        commands.put(MessengerProtocol.CMD_LST, ListPacket.class);
        commands.put(MessengerProtocol.CMD_MSG, MessagePacket.class);
        commands.put(MessengerProtocol.CMD_NAK, NACKPacket.class);
        commands.put(MessengerProtocol.CMD_NLN, OnlinePacket.class);
        commands.put(MessengerProtocol.CMD_OUT, LogoutPacket.class);
        commands.put(MessengerProtocol.CMD_RNG, RingPacket.class);
        commands.put(MessengerProtocol.CMD_SYN, SyncPacket.class);
        commands.put(MessengerProtocol.CMD_USR, AuthPacket.class);
        commands.put(MessengerProtocol.CMD_VER, VersionPacket.class);
        commands.put(MessengerProtocol.CMD_XFR, RedirectPacket.class);
        commands.put(MessengerProtocol.ERR_SYNTAX_ERROR, ErrorPacket.class);
        commands.put(MessengerProtocol.ERR_INVALID_PARAMETER, ErrorPacket.class);
        commands.put(MessengerProtocol.ERR_INVALID_USER, ErrorPacket.class);
        commands.put(MessengerProtocol.ERR_FQDN_MISSING, ErrorPacket.class);
        commands.put(MessengerProtocol.ERR_ALREADY_LOGIN, ErrorPacket.class);
        commands.put(MessengerProtocol.ERR_INVALID_USERNAME, ErrorPacket.class);
        commands.put(MessengerProtocol.ERR_INVALID_FRIENDLY_NAME, ErrorPacket.class);
        commands.put(MessengerProtocol.ERR_LIST_FULL, ErrorPacket.class);
        commands.put(MessengerProtocol.ERR_NOT_ON_LIST, ErrorPacket.class);
        commands.put(MessengerProtocol.ERR_ALREADY_IN_THE_MODE, ErrorPacket.class);
        commands.put(MessengerProtocol.ERR_ALREADY_IN_OPPOSITE_LIST, ErrorPacket.class);
        commands.put(MessengerProtocol.ERR_SWITCHBOARD_FAILED, ErrorPacket.class);
        commands.put(MessengerProtocol.ERR_NOTIFY_XFER_FAILED, ErrorPacket.class);
        commands.put(MessengerProtocol.ERR_REQUIRED_FIELDS_MISSING, ErrorPacket.class);
        commands.put(MessengerProtocol.ERR_NOT_LOGGED_IN, ErrorPacket.class);
        commands.put(MessengerProtocol.ERR_INTERNAL_SERVER, ErrorPacket.class);
        commands.put(MessengerProtocol.ERR_DB_SERVER, ErrorPacket.class);
        commands.put(MessengerProtocol.ERR_FILE_OPERATION, ErrorPacket.class);
        commands.put(MessengerProtocol.ERR_MEMORY_ALLOC, ErrorPacket.class);
        commands.put(MessengerProtocol.ERR_SERVER_BUSY, ErrorPacket.class);
        commands.put(MessengerProtocol.ERR_SERVER_UNAVAILABLE, ErrorPacket.class);
        commands.put(MessengerProtocol.ERR_PERR_NS_DOWN, ErrorPacket.class);
        commands.put(MessengerProtocol.ERR_DB_CONNECT, ErrorPacket.class);
        commands.put(MessengerProtocol.ERR_SERVER_GOING_DOWN, ErrorPacket.class);
        commands.put(MessengerProtocol.ERR_CREATE_CONNECTION, ErrorPacket.class);
        commands.put(MessengerProtocol.ERR_BLOCKING_WRITE, ErrorPacket.class);
        commands.put(MessengerProtocol.ERR_SESSION_OVERLOAD, ErrorPacket.class);
        commands.put(MessengerProtocol.ERR_USER_TOO_ACTIVE, ErrorPacket.class);
        commands.put(MessengerProtocol.ERR_TOO_MANY_SESSIONS, ErrorPacket.class);
        commands.put(MessengerProtocol.ERR_NOT_EXPECTED, ErrorPacket.class);
        commands.put(MessengerProtocol.ERR_BAD_FRIEND_FILE, ErrorPacket.class);
        commands.put(MessengerProtocol.ERR_AUTHENTICATION_FAILED, ErrorPacket.class);
        commands.put(MessengerProtocol.ERR_NOT_ALLOWED_WHEN_OFFLINE, ErrorPacket.class);
        commands.put(MessengerProtocol.ERR_NOT_ACCEPTING_NEW_USERS, ErrorPacket.class);
    }
}
