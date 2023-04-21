package edu.nupt.jxta.impl.dht.rpv;

import java.io.IOException;
import net.jxta.document.MimeMediaType;
import net.jxta.endpoint.EndpointService;
import net.jxta.endpoint.Message;
import net.jxta.endpoint.Messenger;
import net.jxta.endpoint.MessengerEvent;
import net.jxta.endpoint.MessengerEventListener;
import net.jxta.endpoint.OutgoingMessageEvent;
import net.jxta.endpoint.OutgoingMessageEventListener;
import net.jxta.impl.util.TimeUtils;
import net.jxta.logging.Logging;
import net.jxta.protocol.RdvAdvertisement;
import java.util.logging.Level;
import java.util.logging.Logger;
import edu.nupt.jxta.impl.dht.table.HashFunction;
import edu.nupt.jxta.impl.dht.table.ID;

/**
 * An element of the PeerView.
 *
 * <p/>The destination address (peerID) is part of PeerViewDestination, which implements the
 * comparable interface. That makes it possible to sort and create ordered lists of
 * PeerViewElements, and to search these lists while knowing only a destination address.
 */
public final class PeerViewElement extends PeerViewDestination implements OutgoingMessageEventListener {

    /**
     *  Logger
     */
    private static final transient Logger LOG = Logger.getLogger(PeerViewElement.class.getName());

    public PeerView getPeerview() {
        return peerview;
    }

    public void setPeerview(PeerView peerview) {
        this.peerview = peerview;
    }

    /**
     * EndpointService that this PeerViewElement must use.
     */
    private final EndpointService endpoint;

    /**
     * Absolute time in milliseconds at which this element was created.
     */
    private final long created;

    /**
     * Absolute time in milliseconds at which this element was created.
     */
    private long lastUpdate = 0;

    /**
     * The encapsulated RdvAdvertisement for the Peer this instance
     * represents.
     */
    private RdvAdvertisement radv = null;

    /**
     * True is the remote peer is known to be alive, false otherwise.
     * It is always alive at birth. It may die soon after and we want to
     * generate an event in that case.
     */
    private boolean alive = true;

    /**
     * If true then we are not accepting new messages until something unclogs.
     */
    private volatile boolean throttling = false;

    /**
     * PeerView that owns this PeerViewElement.
     */
    private PeerView peerview = null;

    /**
     *  A cached Messenger for sending to the destination peer.
     */
    private Messenger cachedMessenger = null;

    private ID hashID;

    /**
     * Initialize from a RdvAdvertisement.
     *
     * @param endpoint The endpoint service.
     * @param radv the RdvAdvertisement from which to initialize
     */
    PeerViewElement(PeerView parent, EndpointService endpoint, RdvAdvertisement radv) {
        super(radv.getPeerID());
        this.endpoint = endpoint;
        this.radv = radv;
        peerview = parent;
        hashID = HashFunction.getHashFunction().getHashKey(radv.getPeerID());
        created = TimeUtils.timeNow();
        lastUpdate = created;
    }

    /**
     *  {@inheritDoc}
     *  <p/>
     *  A simple implementation for debugging. Do not attempt to parse this value!
     */
    @Override
    public String toString() {
        StringBuilder asString = new StringBuilder();
        asString.append('\"');
        asString.append(radv.getName());
        asString.append('\"');
        asString.append(alive ? " A " : " a ");
        asString.append(isInPeerView() ? " P " : " p ");
        asString.append(throttling ? " T " : " t ");
        asString.append(" [");
        asString.append(TimeUtils.toRelativeTimeMillis(TimeUtils.timeNow(), created) / TimeUtils.ASECOND);
        asString.append("/");
        asString.append(TimeUtils.toRelativeTimeMillis(TimeUtils.timeNow(), lastUpdate) / TimeUtils.ASECOND);
        asString.append("]");
        return asString.toString();
    }

    /**
     * {@inheritDoc}
     */
    public void messageSendSucceeded(OutgoingMessageEvent e) {
        setAlive(true, true);
        throttling = false;
    }

    /**
     * {@inheritDoc}
     */
    public void messageSendFailed(OutgoingMessageEvent e) {
        if (null != e.getFailure()) {
            setAlive(false, true);
            LOG.warning(e.getFailure().toString());
            LOG.info(this.getRdvAdvertisement().getDocument(MimeMediaType.XMLUTF8).toString());
            peerview.notifyFailure(this, false);
        }
        throttling = (e.getFailure() == null);
    }

    /**
     * Return <code>true</code> if the remote peer is known to be alive,
     * <code>false</code> otherwise.
     *
     * @return Return <code>true</code> if the remote peer is known to be
     * alive, <code>false</code> otherwise.
     */
    public boolean isAlive() {
        return alive;
    }

    /**
     * Update the connection status based upon the result of the last message
     * send.
     *
     * <p/>We track the current dead-alive state and If we're in a peerview
     * notify it of the transitions from alive to dead.
     *
     * @param live The known liveness of our connection to this peer.
     * @param doNotify {@code true} will cause failure notifications to be sent.
     * {@code false} makes notifications the caller's responsibility.
     * @return {@code true} if a failure notification needs to be sent otherwise
     * {@code false}.
     */
    boolean setAlive(boolean live, boolean doNotify) {
        boolean mustNotify;
        LOG.info("SET ALIVE" + live);
        synchronized (this) {
            mustNotify = alive && !live;
            alive = live;
        }
        if (mustNotify && doNotify) {
            PeerView temp = peerview;
            if (null != temp) {
                LOG.warning("notify failure" + this.getPeerID());
                temp.notifyFailure(this, true);
            }
        }
        return mustNotify;
    }

    boolean isInPeerView() {
        return (null != peerview);
    }

    /**
     *  Sets the peerview
     */
    synchronized void setPeerView(PeerView pv) {
        if ((null != peerview) && (null != pv)) {
            throw new IllegalStateException("Element already in " + peerview);
        }
        peerview = pv;
    }

    /**
     *  Return the time in absolute milliseconds at which we last updated this peer.
     */
    long getLastUpdateTime() {
        return lastUpdate;
    }

    /**
     *  Sets the time in absolute milliseconds at which we last updated this peer.
     */
    void setLastUpdateTime(long last) {
        lastUpdate = last;
    }

    /**
     * Send a message to the peer which is represented by the current
     * PeerViewElement.
     *
     * @param msg the message to send
     *
     * @param serviceName the service name on the destination peer to
     * which the message will be demultiplexed
     *
     * @param serviceParam the service param on the destination peer
     * to which the message will be demultiplexed
     *
     * @return true if the message was successfully handed off to the
     * endpoint for delivery, false otherwise
     */
    public boolean sendMessage(Message msg, String serviceName, String serviceParam) {
        if (throttling) {
            if (Logging.SHOW_WARNING && LOG.isLoggable(Level.WARNING)) {
                LOG.warning("Declining to send   throttling on " + this);
            }
            return false;
        }
        Messenger sendVia = getCachedMessenger();
        if (null == sendVia) {
            if (Logging.SHOW_WARNING && LOG.isLoggable(Level.WARNING)) {
                LOG.warning("Could not get  messenger for " + getPeerID());
            }
            OutgoingMessageEvent event = new OutgoingMessageEvent(msg, new IOException("Couldn't get messenger for " + getPeerID()));
            messageSendFailed(event);
            return false;
        }
        sendVia.sendMessage(msg.clone(), serviceName, serviceParam, this);
        return true;
    }

    /**
     * Get the encapsulated Peer Advertisement.
     *
     * @return the Advertisement of the Peer represented by this
     * object
     */
    public RdvAdvertisement getRdvAdvertisement() {
        return radv;
    }

    /**
     * Set the encapsulated Peer Advertisement.
     *
     * @param adv is the RdvAdvertisement to be set.
     * @return RdvAdvertisement the old Advertisement of the Peer represented by this
     * object
     */
    RdvAdvertisement setRdvAdvertisement(RdvAdvertisement adv) {
        if (!radv.getPeerID().equals(adv.getPeerID())) {
            if (Logging.SHOW_SEVERE && LOG.isLoggable(Level.SEVERE)) {
                LOG.severe("adv refers to a different peer");
            }
            throw new IllegalArgumentException("adv refers to a different peer");
        }
        RdvAdvertisement old = radv;
        this.radv = adv;
        setLastUpdateTime(TimeUtils.timeNow());
        return old;
    }

    /**
     *  Return a messenger suitable for sending to this peer.
     *
     *  @return a messenger to this PVE peer or if {@code null} if peer is
     *  unreachable.
     */
    private Messenger getCachedMessenger() {
        boolean mustNotify = false;
        synchronized (this) {
            if ((null == cachedMessenger) || ((cachedMessenger.getState() & Messenger.USABLE) == 0)) {
                cachedMessenger = null;
                if (Logging.SHOW_FINE && LOG.isLoggable(Level.FINE)) {
                    LOG.fine("Getting cached Messenger for " + radv.getName());
                }
                cachedMessenger = endpoint.getMessengerImmediate(getDestAddress(), radv.getRouteAdv());
                if (null == cachedMessenger) {
                    System.out.println("cachedmessage is null.");
                    mustNotify = setAlive(false, false);
                } else if ((cachedMessenger.getState() & Messenger.RESOLVED) != 0) {
                    mustNotify = setAlive(true, false);
                }
            }
        }
        if (mustNotify) {
            PeerView temp = peerview;
            if (null != temp) {
                LOG.info(this.getRdvAdvertisement().getDocument(MimeMediaType.XMLUTF8).toString());
                temp.notifyFailure(this, true);
            }
        }
        return cachedMessenger;
    }

    public ID getHashID() {
        if (hashID == null) hashID = HashFunction.getHashFunction().getHashKey(getPeerID());
        return hashID;
    }

    public boolean sendLeaveMessage(Message msg, String serviceName, String serviceParam) {
        if (throttling) {
            if (Logging.SHOW_WARNING && LOG.isLoggable(Level.WARNING)) {
                LOG.warning("Declining to send  throttling on " + this);
            }
            return false;
        }
        Messenger sendVia = getCachedMessenger();
        if (null == sendVia) {
            if (Logging.SHOW_WARNING && LOG.isLoggable(Level.WARNING)) {
                LOG.warning("Could not get messenger for " + getPeerID());
            }
            return false;
        }
        try {
            sendVia.sendMessage(msg, serviceName, serviceParam);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
