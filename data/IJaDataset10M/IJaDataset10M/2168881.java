package edu.nupt.jxta.impl.rendezvous.dht;

import java.io.IOException;
import java.util.Enumeration;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.jxta.document.Advertisement;
import net.jxta.document.AdvertisementFactory;
import net.jxta.document.XMLDocument;
import net.jxta.endpoint.EndpointAddress;
import net.jxta.endpoint.Message;
import net.jxta.endpoint.Messenger;
import net.jxta.id.ID;
import net.jxta.impl.protocol.RdvConfigAdv;
import net.jxta.impl.rendezvous.RendezVousPropagateMessage;
import net.jxta.logging.Logging;
import net.jxta.peergroup.PeerGroup;
import net.jxta.platform.Module;
import net.jxta.protocol.ConfigParams;
import net.jxta.rendezvous.RendezvousEvent;

public class AdhocRendezvousService extends RendezVousServiceProvider {

    /**
     * Log4J Logger
     */
    private static final transient Logger LOG = Logger.getLogger(AdhocRendezvousService.class.getName());

    /**
     * Default Maximum TTL. This is minimum needed to bridge networks.
     */
    private static final int DEFAULT_MAX_TTL = 2;

    /**
     * Constructor
     *
     * @param g          the peergroup
     * @param rdvService the rendezvous service
     */
    public AdhocRendezvousService(PeerGroup g, ChordRendezvousServiceImpl rdvService) {
        super(g, rdvService);
        ConfigParams confAdv = g.getConfigAdvertisement();
        if (confAdv != null) {
            Advertisement adv = null;
            try {
                XMLDocument configDoc = (XMLDocument) confAdv.getServiceParam(rdvService.getAssignedID());
                if (null != configDoc) {
                    configDoc.addAttribute("type", RdvConfigAdv.getAdvertisementType());
                    adv = AdvertisementFactory.newAdvertisement(configDoc);
                }
            } catch (java.util.NoSuchElementException ignored) {
            }
            if (adv instanceof RdvConfigAdv) {
                RdvConfigAdv rdvConfigAdv = (RdvConfigAdv) adv;
                MAX_TTL = (-1 != rdvConfigAdv.getMaxTTL()) ? rdvConfigAdv.getMaxTTL() : DEFAULT_MAX_TTL;
            } else {
                MAX_TTL = DEFAULT_MAX_TTL;
            }
        } else {
            MAX_TTL = DEFAULT_MAX_TTL;
        }
        if (Logging.SHOW_INFO && LOG.isLoggable(Level.INFO)) {
            LOG.info("RendezVous Service is initialized for " + g.getPeerGroupID() + " as an ad hoc peer. ");
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected int startApp(String[] arg) {
        super.startApp(arg);
        rdvService.generateEvent(RendezvousEvent.BECAMEEDGE, group.getPeerID());
        return Module.START_OK;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized void stopApp() {
        if (closed) {
            return;
        }
        closed = true;
        super.stopApp();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Vector<ID> getConnectedPeerIDs() {
        return new Vector<ID>(0);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isConnectedToRendezVous() {
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void connectToRendezVous(EndpointAddress addr, Object hint) throws IOException {
        throw new UnsupportedOperationException("Not supported by ad hoc");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void challengeRendezVous(ID peer, long delay) {
        throw new UnsupportedOperationException("Not supported by ad hoc");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void disconnectFromRendezVous(ID peerId) {
        throw new UnsupportedOperationException("Not supported by ad hoc");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void propagate(Message msg, String serviceName, String serviceParam, int ttl) throws IOException {
        ttl = Math.min(ttl, MAX_TTL);
        RendezVousPropagateMessage propHdr = updatePropHeader(msg, getPropHeader(msg), serviceName, serviceParam, ttl);
        if (null != propHdr) {
            sendToNetwork(msg, propHdr);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void propagateInGroup(Message msg, String serviceName, String serviceParam, int ttl) throws IOException {
        ttl = Math.min(ttl, MAX_TTL);
        RendezVousPropagateMessage propHdr = updatePropHeader(msg, getPropHeader(msg), serviceName, serviceParam, ttl);
        if (null != propHdr) {
            sendToNetwork(msg, propHdr);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void propagate(Enumeration<? extends ID> destPeerIDs, Message msg, String serviceName, String serviceParam, int ttl) {
        ttl = Math.min(ttl, MAX_TTL);
        RendezVousPropagateMessage propHdr = updatePropHeader(msg, getPropHeader(msg), serviceName, serviceParam, ttl);
        if (null != propHdr) {
            int numPeers = 0;
            try {
                while (destPeerIDs.hasMoreElements()) {
                    ID dest = destPeerIDs.nextElement();
                    if (Logging.SHOW_FINE && LOG.isLoggable(Level.FINE)) {
                        LOG.fine("Sending " + msg + " to client " + dest);
                    }
                    EndpointAddress addr = mkAddress(dest, PropSName, PropPName);
                    Messenger messenger = rdvService.endpoint.getMessenger(addr);
                    if (null != messenger) {
                        try {
                            messenger.sendMessage(msg);
                            numPeers++;
                        } catch (IOException failed) {
                        }
                    }
                }
            } finally {
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void propagateToNeighbors(Message msg, String serviceName, String serviceParam, int ttl) throws IOException {
        ttl = Math.min(ttl, MAX_TTL);
        RendezVousPropagateMessage propHdr = updatePropHeader(msg, getPropHeader(msg), serviceName, serviceParam, ttl);
        if (null != propHdr) {
            try {
                sendToNetwork(msg, propHdr);
            } catch (IOException failed) {
                throw failed;
            }
        }
    }

    /**
     * {@inheritDoc}
     * <p/>
     * The definition of walk says that we should forward the message to the
     * most appropriate peer. Since we don't make any effort keep track of other
     * peers we don't have anywhere to send the message.
     */
    @Override
    public void walk(Message msg, String serviceName, String serviceParam, int ttl) throws IOException {
    }

    /**
     * {@inheritDoc}
     * <p/>
     * Unlike the undirected walk we are told where to send the message so we
     * deliver it as requested.
     */
    @Override
    public void walk(Vector<? extends ID> destPeerIDs, Message msg, String serviceName, String serviceParam, int ttl) throws IOException {
        propagate(destPeerIDs.elements(), msg, serviceName, serviceParam, ttl);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void repropagate(Message msg, RendezVousPropagateMessage propHdr, String serviceName, String serviceParam) {
        if (Logging.SHOW_FINE && LOG.isLoggable(Level.FINE)) {
            LOG.fine("Repropagating " + msg + " (" + propHdr.getMsgId() + ")");
        }
        try {
            propHdr = updatePropHeader(msg, propHdr, serviceName, serviceParam, MAX_TTL);
            if (null != propHdr) {
                sendToNetwork(msg, propHdr);
            } else {
                if (Logging.SHOW_FINE && LOG.isLoggable(Level.FINE)) {
                    LOG.fine("No propagate header, declining to repropagate " + msg + ")");
                }
            }
        } catch (Exception ez1) {
            if (Logging.SHOW_WARNING && LOG.isLoggable(Level.WARNING)) {
                if (propHdr != null) {
                    LOG.log(Level.WARNING, "Failed to repropagate " + msg + " (" + propHdr.getMsgId() + ")", ez1);
                } else {
                    LOG.log(Level.WARNING, "Could to repropagate " + msg, ez1);
                }
            }
        }
    }

    @Override
    public boolean isConnectedToNetwork() {
        return false;
    }
}
