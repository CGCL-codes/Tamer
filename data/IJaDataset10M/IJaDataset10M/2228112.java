package ru.caffeineim.protocols.icq.core;

import java.io.IOException;
import java.util.List;
import java.util.Vector;
import javax.net.SocketFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import ru.caffeineim.protocols.icq.Flap;
import ru.caffeineim.protocols.icq.Tlv;
import ru.caffeineim.protocols.icq.contacts.ContactList;
import ru.caffeineim.protocols.icq.contacts.Group;
import ru.caffeineim.protocols.icq.contacts.IContactList;
import ru.caffeineim.protocols.icq.integration.OscarInterface;
import ru.caffeineim.protocols.icq.integration.listeners.ContactListListener;
import ru.caffeineim.protocols.icq.integration.listeners.MessagingListener;
import ru.caffeineim.protocols.icq.integration.listeners.MetaAckListener;
import ru.caffeineim.protocols.icq.integration.listeners.MetaInfoListener;
import ru.caffeineim.protocols.icq.integration.listeners.OurStatusListener;
import ru.caffeineim.protocols.icq.integration.listeners.UserStatusListener;
import ru.caffeineim.protocols.icq.integration.listeners.XStatusListener;
import ru.caffeineim.protocols.icq.request.Request;
import ru.caffeineim.protocols.icq.request.RequestKeeper;
import ru.caffeineim.protocols.icq.request.event.RequestListener;
import ru.caffeineim.protocols.icq.setting.Tweaker;

/**
 * <p>Created by
 *   @author Fabrice Michellonet
 *   @author Samolisov Pavel
 */
public class OscarConnection {

    private static Log log = LogFactory.getLog(OscarConnection.class);

    private Tlv cookie;

    private String userId;

    private String password;

    private boolean authorized = false;

    private boolean connected = false;

    private IContactList contactList;

    private OscarPingHandler pingHandler;

    private Tweaker tweaker;

    private OscarClient client;

    private OscarPacketAnalyser analyser;

    private RequestKeeper requestKeeper;

    private List messagingListeners;

    private List userStatusListeners;

    private List ourStatusListeners;

    private List xStatusListeners;

    private List contactListListeners;

    private List metaInfoListeners;

    private List metaAckListeners;

    private Object contactListLock;

    private int flapSeqNrs;

    static {
        OscarConfiguration.load();
        log.debug("OscarConfiguration has been loaded");
    }

    public OscarConnection(String host, int port, String userId, String password) {
        this(host, port, userId, password, new Tweaker());
    }

    public OscarConnection(String host, int port, String userId, String password, Tweaker tweaker) {
        this(host, port, userId, password, new Tweaker(), null);
    }

    public OscarConnection(String host, int port, String userId, String password, Tweaker tweaker, SocketFactory factory) {
        this.userId = userId;
        this.password = password;
        this.tweaker = tweaker;
        this.flapSeqNrs = 0;
        analyser = new OscarPacketAnalyser(this);
        client = new OscarClient(host, port, analyser, factory);
        requestKeeper = new RequestKeeper();
        messagingListeners = new Vector();
        ourStatusListeners = new Vector();
        userStatusListeners = new Vector();
        xStatusListeners = new Vector();
        contactListListeners = new Vector();
        metaInfoListeners = new Vector();
        metaAckListeners = new Vector();
        contactListLock = new Object();
    }

    public void addMetaAckListener(MetaAckListener listener) {
        log.debug("MetaAckListener " + listener.getClass().getName() + " has been added");
        metaAckListeners.add(listener);
    }

    public boolean removeMetaAckListener(MetaAckListener listener) {
        log.debug("MetaAckListener " + listener.getClass().getName() + " has been removed");
        return metaAckListeners.remove(listener);
    }

    public void addMetaInfoListener(MetaInfoListener listener) {
        log.debug("MetaInfoListener " + listener.getClass().getName() + " has been added");
        metaInfoListeners.add(listener);
    }

    public boolean removeMetaInfoListener(MetaInfoListener listener) {
        log.debug("MetaInfoListener " + listener.getClass().getName() + " has been removed");
        return metaInfoListeners.remove(listener);
    }

    public void addContactListListener(ContactListListener listener) {
        log.debug("ContactListListener " + listener.getClass().getName() + " has been added");
        contactListListeners.add(listener);
    }

    public boolean removeContactListListener(ContactListListener listener) {
        log.debug("ContactListListener " + listener.getClass().getName() + " has been removed");
        return contactListListeners.remove(listener);
    }

    public void addMessagingListener(MessagingListener listener) {
        log.debug("MessagingListener " + listener.getClass().getName() + " has been added");
        messagingListeners.add(listener);
    }

    public boolean removeMessagingListener(MessagingListener listener) {
        log.debug("MessagingListener " + listener.getClass().getName() + " has been removed");
        return messagingListeners.remove(listener);
    }

    public void addUserStatusListener(UserStatusListener listener) {
        log.debug("UserStatusListener " + listener.getClass().getName() + " has been added");
        userStatusListeners.add(listener);
    }

    public boolean removeUserStatusListener(UserStatusListener listener) {
        log.debug("UserStatusListener " + listener.getClass().getName() + " has been removed");
        return userStatusListeners.remove(listener);
    }

    public void addXStatusListener(XStatusListener listener) {
        log.debug("XStatusListener " + listener.getClass().getName() + " has been added");
        xStatusListeners.add(listener);
    }

    public boolean removeXStatusListener(XStatusListener listener) {
        log.debug("XStatusListener " + listener.getClass().getName() + " has been removed");
        return xStatusListeners.remove(listener);
    }

    public void addOurStatusListener(OurStatusListener listener) {
        log.debug("OurStatusListener " + listener.getClass().getName() + " has been added");
        ourStatusListeners.add(listener);
    }

    public boolean removeOurStatusListener(OurStatusListener listener) {
        log.debug("OurStatusListener " + listener.getClass().getName() + " has been removed");
        return ourStatusListeners.remove(listener);
    }

    /**
     * Send a packet to the server.
     * @param flapPacket The paquet to be sent.
     */
    public synchronized void sendFlap(Flap flapPacket) {
        if (flapPacket.getSequenceNumber() == Integer.MAX_VALUE) {
            flapSeqNrs++;
            if (flapSeqNrs > 0xffff) flapSeqNrs = 0;
            flapPacket.setSequenceNumber(flapSeqNrs);
        }
        try {
            client.sendPacket(flapPacket.getByteArray());
        } catch (IOException e) {
            if (connected) {
                try {
                    client.disconnect();
                } catch (IOException e1) {
                    log.debug(e1.getMessage(), e1);
                } finally {
                    if (authorized) notifyOnLogout(e);
                }
            }
        }
    }

    protected synchronized void notifyOnLogout(Exception exception) {
        for (int i = 0; i < getOurStatusListeners().size(); i++) {
            OurStatusListener l = (OurStatusListener) getOurStatusListeners().get(i);
            log.debug("notify listener " + l.getClass().getName() + " onLogout(" + exception + ")");
            l.onLogout(exception);
        }
    }

    /**
     * Send a packet to the server and start the monitoring system.<br/>
     * The <b>listener</b> will be warned of the server reply by a <b>RequestAnswerEvent</b> event.
     *
     * @param flapPacket The paquet to be sent.
     * @param listener The class that monitor the packet.
     *
     * @return The request object that has been created, null if the flap do not contains
     * a Snac section.
     */
    public synchronized Request sendMonitoredFlap(Flap flapPacket, RequestListener listener) {
        int requestId;
        Request request = null;
        if (flapPacket.hasSnac()) {
            requestId = requestKeeper.nextAvailableRequestId();
            flapPacket.getSnac().setRequestId(requestId);
            request = new Request(flapPacket, listener);
            requestKeeper.addRequest(request);
        }
        sendFlap(flapPacket);
        return request;
    }

    /**
     * Connect to the server
     */
    public synchronized void connect() {
        try {
            client.connect();
            connected = true;
            log.debug("OscarConnection has been connected");
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            notifyOnLogout(e);
        }
    }

    /**
     * This will cause the connection to be closed.
     *
     * @throws IOException
     */
    public synchronized void close() {
        try {
            if (connected) {
                client.disconnect();
            }
        } catch (IOException e) {
            log.debug(e.getMessage(), e);
        } finally {
            log.debug("OscarConnection has been disconnected");
            connected = false;
            setAuthorized(false);
        }
    }

    protected synchronized void setAuthorized(boolean status) {
        authorized = status;
        if (authorized) {
            for (int i = 0; i < getOurStatusListeners().size(); i++) {
                OurStatusListener l = (OurStatusListener) getOurStatusListeners().get(i);
                log.debug("notify listener " + l.getClass().getName() + " onLogin()");
                l.onLogin();
            }
        }
    }

    public boolean isAuthorized() {
        return authorized;
    }

    public String getUserId() {
        return userId;
    }

    public String getPassword() {
        return password;
    }

    public Tlv getCookie() {
        return cookie;
    }

    public void setCookie(Tlv cookie) {
        this.cookie = cookie;
    }

    public OscarClient getClient() {
        return client;
    }

    public void setClient(OscarClient client) {
        this.client = client;
    }

    public Tweaker getTweaker() {
        return tweaker;
    }

    public OscarPacketAnalyser getPacketAnalyser() {
        return analyser;
    }

    public List getMessagingListeners() {
        return messagingListeners;
    }

    public List getOurStatusListeners() {
        return ourStatusListeners;
    }

    public List getUserStatusListeners() {
        return userStatusListeners;
    }

    public List getXStatusListeners() {
        return xStatusListeners;
    }

    public List getContactListListeners() {
        return contactListListeners;
    }

    public List getMetaInfoListeners() {
        return metaInfoListeners;
    }

    public List getMetaAckListeners() {
        return metaAckListeners;
    }

    public RequestKeeper getRequestKeeper() {
        return requestKeeper;
    }

    public IContactList getContactList() {
        if (contactList == null) {
            OscarInterface.sendContatListRequest(this);
            log.debug("ContactListRequest has been sent");
            log.warn("ContactList is not fetched");
        }
        return contactList;
    }

    public void buildContactList(Group rootGroup, int count) {
        synchronized (contactListLock) {
            contactList = new ContactList(this, rootGroup, count);
            contactListLock.notifyAll();
        }
    }
}
