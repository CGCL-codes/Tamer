package net.jxta.myjxta.ui.action;

import java.awt.event.ActionEvent;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractAction;
import net.jxta.document.AdvertisementFactory;
import net.jxta.document.MimeMediaType;
import net.jxta.document.StructuredDocumentFactory;
import net.jxta.document.XMLDocument;
import net.jxta.endpoint.MessageElement;
import net.jxta.ext.config.Resource;
import net.jxta.ext.config.ResourceNotFoundException;
import net.jxta.logging.Logging;
import net.jxta.myjxta.View;
import net.jxta.myjxta.dialog.DialogMessage;
import net.jxta.myjxta.dialog.commands.StatusCommand;
import net.jxta.myjxta.dialog.util.RemoteCommandInvoker;
import net.jxta.myjxta.util.Group;
import net.jxta.myjxta.util.GroupNode;
import net.jxta.myjxta.util.Peer;
import net.jxta.myjxta.util.PeerNode;
import net.jxta.myjxta.util.Resources;
import net.jxta.protocol.PipeAdvertisement;

/**
 * @author james todd [gonzo at jxta dot org]
 * @version $Id: PeerStatusAction.java,v 1.20 2007/06/10 21:15:13 nano Exp $
 * @since 2007-11-2 替换jdialog
 */
public class PeerStatusAction extends AbstractAction {

    private static final int INTERVAL = 250;

    private static final int MAX = INTERVAL * 4 * 40;

    private static final ResourceBundle STRINGS = Resources.getStrings();

    private static final String ELEMENT_DELIMITER = "/";

    private static final String ATTRIBUTE_DELIMITER = "/@";

    private static final String RESPONSE = "/response";

    private static final String RESPONSE_STATUS = RESPONSE + ATTRIBUTE_DELIMITER + StatusCommand.STATUS;

    private static final String REQUEST = RESPONSE + ELEMENT_DELIMITER + "request";

    private static final String REQUEST_ID = REQUEST + ELEMENT_DELIMITER + StatusCommand.UID;

    private static final String STATUS = RESPONSE + ELEMENT_DELIMITER + StatusCommand.STATUS;

    private static final String PROFILE = STATUS + ELEMENT_DELIMITER + StatusCommand.PROFILE;

    private static final String PROFILE_VERSION = PROFILE + ATTRIBUTE_DELIMITER + StatusCommand.VERSION;

    private static final String PROFILE_TIME = PROFILE + ATTRIBUTE_DELIMITER + StatusCommand.TIME;

    private static final String PROFILE_JAVA_VERSION = PROFILE + ATTRIBUTE_DELIMITER + StatusCommand.JAVA_VERSION;

    private static final String PROFILE_JAVA_VENDOR = PROFILE + ATTRIBUTE_DELIMITER + StatusCommand.JAVA_VENDOR;

    private static final String PROFILE_OS_NAME = PROFILE + ATTRIBUTE_DELIMITER + StatusCommand.OS_NAME;

    private static final String PROFILE_OS_ARCHITECTURE = PROFILE + ATTRIBUTE_DELIMITER + StatusCommand.OS_ARCHITECTURE;

    private static final String PROFILE_PROCESSORS = PROFILE + ATTRIBUTE_DELIMITER + StatusCommand.PROCESSORS;

    private static final String PROFILE_JXTA_HOME = PROFILE + ATTRIBUTE_DELIMITER + StatusCommand.JXTA_HOME;

    private static final String PROFILE_FREE_MEMORY = PROFILE + ATTRIBUTE_DELIMITER + StatusCommand.FREE_MEMORY;

    private static final String PROFILE_MAXIMUM_MEMORY = PROFILE + ATTRIBUTE_DELIMITER + StatusCommand.MAXIMUM_MEMORY;

    private static final String PROFILE_TOTAL_MEMORY = PROFILE + ATTRIBUTE_DELIMITER + StatusCommand.TOTAL_MEMORY;

    private static final String PEER = STATUS + ELEMENT_DELIMITER + StatusCommand.PEER;

    private static final String PEER_ID = PEER + ATTRIBUTE_DELIMITER + StatusCommand.UID;

    private static final String PEER_NAME = PEER + ATTRIBUTE_DELIMITER + StatusCommand.NAME;

    private static final String PEER_IS_AUTHENTICATED = PEER + ATTRIBUTE_DELIMITER + StatusCommand.IS_AUTHENTICATED;

    private static final String RENDEZVOUS = STATUS + ELEMENT_DELIMITER + StatusCommand.RENDEZVOUS;

    private static final String RENDEZVOUS_ROLE = RENDEZVOUS + ATTRIBUTE_DELIMITER + StatusCommand.ROLE;

    private static final String RENDEZVOUS_PEER = RENDEZVOUS + ELEMENT_DELIMITER + StatusCommand.PEER;

    private static final String RENDEZVOUS_PEER_ID = RENDEZVOUS_PEER + ATTRIBUTE_DELIMITER + StatusCommand.UID;

    private static final String RENDEZVOUS_PEER_NAME = RENDEZVOUS_PEER + ATTRIBUTE_DELIMITER + StatusCommand.NAME;

    private static final String RENDEZVOUS_PEER_GROUP_ID = RENDEZVOUS_PEER + ATTRIBUTE_DELIMITER + StatusCommand.GROUP_ID;

    private static final String RENDEZVOUS_PEER_IS_ALIVE = RENDEZVOUS_PEER + ATTRIBUTE_DELIMITER + StatusCommand.IS_ALIVE;

    private static final String RENDEZVOUS_PEER_IS_IN_PEER_VIEW = RENDEZVOUS_PEER + ATTRIBUTE_DELIMITER + StatusCommand.IS_IN_PEER_VIEW;

    private static final String RENDEZVOUS_PEER_IS_THROTTLING = RENDEZVOUS_PEER + ATTRIBUTE_DELIMITER + StatusCommand.IS_THROTTLING;

    private static final String RENDEZVOUS_PEER_CREATE_TIME = RENDEZVOUS_PEER + ATTRIBUTE_DELIMITER + StatusCommand.CREATE_TIME;

    private static final String RENDEZVOUS_PEER_UPDATE_TIME = RENDEZVOUS_PEER + ATTRIBUTE_DELIMITER + StatusCommand.UPDATE_TIME;

    private static final String RENDEZVOUS_PEER_DIRECTION = RENDEZVOUS_PEER + ATTRIBUTE_DELIMITER + StatusCommand.DIRECTION;

    private static final String RENDEZVOUS_CLIENT = RENDEZVOUS + ELEMENT_DELIMITER + StatusCommand.CLIENT;

    private static final String RENDEZVOUS_CLIENT_ID = RENDEZVOUS_CLIENT + ATTRIBUTE_DELIMITER + StatusCommand.UID;

    private static final String RENDEZVOUS_CLIENT_NAME = RENDEZVOUS_CLIENT + ATTRIBUTE_DELIMITER + StatusCommand.NAME;

    private static final String RENDEZVOUS_CLIENT_TYPE = RENDEZVOUS_CLIENT + ATTRIBUTE_DELIMITER + StatusCommand.TYPE;

    private static final String RENDEZVOUS_CLIENT_IS_CONNECTED = RENDEZVOUS_CLIENT + ATTRIBUTE_DELIMITER + StatusCommand.CLIENT_IS_CONNECTED;

    private static final String RENDEZVOUS_CLIENT_LEASE_TIME = RENDEZVOUS_CLIENT + ATTRIBUTE_DELIMITER + StatusCommand.LEASE_TIME;

    private static final String RENDEZVOUS_PROVISIONER = RENDEZVOUS + ELEMENT_DELIMITER + StatusCommand.RENDEZVOUS;

    private static final String RENDEZVOUS_PROVISIONER_ID = RENDEZVOUS_PROVISIONER + ATTRIBUTE_DELIMITER + StatusCommand.UID;

    private static final String RENDEZVOUS_PROVISIONER_NAME = RENDEZVOUS_PROVISIONER + ATTRIBUTE_DELIMITER + StatusCommand.NAME;

    private static final String RENDEZVOUS_PROVISIONER_GROUP_ID = RENDEZVOUS_PROVISIONER + ATTRIBUTE_DELIMITER + StatusCommand.GROUP_ID;

    private static final String RENDEZVOUS_PROVISIONER_IS_ALIVE = RENDEZVOUS_PROVISIONER + ATTRIBUTE_DELIMITER + StatusCommand.IS_ALIVE;

    private static final String RENDEZVOUS_PROVISIONER_IS_IN_PEER_VIEW = RENDEZVOUS_PROVISIONER + ATTRIBUTE_DELIMITER + StatusCommand.IS_IN_PEER_VIEW;

    private static final String RENDEZVOUS_PROVISIONER_IS_THROTTLING = RENDEZVOUS_PROVISIONER + ATTRIBUTE_DELIMITER + StatusCommand.IS_THROTTLING;

    private static final String RENDEZVOUS_PROVISIONER_NUMBER_OF_CONNECTED_PEERS = RENDEZVOUS_PROVISIONER + ATTRIBUTE_DELIMITER + StatusCommand.NUMBER_OF_CONNECTED_PEERS;

    private static final String RENDEZVOUS_PROVISIONER_START_TIME = RENDEZVOUS_PROVISIONER + ATTRIBUTE_DELIMITER + StatusCommand.START_TIME;

    private static final String RENDEZVOUS_PROVISIONER_CREATE_TIME = RENDEZVOUS_PROVISIONER + ATTRIBUTE_DELIMITER + StatusCommand.CREATE_TIME;

    private static final String RENDEZVOUS_PROVISIONER_UPDATE_TIME = RENDEZVOUS_PROVISIONER + ATTRIBUTE_DELIMITER + StatusCommand.UPDATE_TIME;

    private static final String RENDEZVOUS_PROVISIONER_DIRECTION = RENDEZVOUS_PROVISIONER + ATTRIBUTE_DELIMITER + StatusCommand.DIRECTION;

    private static final String RELAYS = STATUS + ELEMENT_DELIMITER + StatusCommand.RELAYS;

    private static final String RELAYS_CLIENT = RELAYS + ELEMENT_DELIMITER + StatusCommand.CLIENT;

    private static final String RELAYS_CLIENT_ID = RELAYS_CLIENT + ATTRIBUTE_DELIMITER + StatusCommand.UID;

    private static final String RELAYS_CLIENT_NAME = RELAYS_CLIENT + ATTRIBUTE_DELIMITER + StatusCommand.NAME;

    private static final String RELAYS_SERVER = RELAYS + ELEMENT_DELIMITER + StatusCommand.SERVER;

    private static final String RELAYS_SERVER_ID = RELAYS_SERVER + ATTRIBUTE_DELIMITER + StatusCommand.UID;

    private static final String RELAYS_SERVER_NAME = RELAYS_SERVER + ATTRIBUTE_DELIMITER + StatusCommand.NAME;

    private static final String NEW_LINE = "\n";

    private static final String TAB = "  ";

    private static final String SPACE = " ";

    private static final String SLASH = "/";

    private static final Logger LOG = Logger.getLogger(PeerStatusAction.class.getName());

    private View view = null;

    public PeerStatusAction(String name, View view) {
        super(name);
        this.view = view;
        if (Logging.SHOW_FINE && LOG.isLoggable(Level.FINE)) {
            LOG.fine("PeerStatusAction instantiated");
        }
    }

    public boolean isEnabled() {
        PeerNode jxtaNode = (PeerNode) view.getJxtaNode(PeerNode.class);
        return jxtaNode != null;
    }

    public void actionPerformed(ActionEvent ae) {
        final PeerNode pn = (PeerNode) this.view.getJxtaNode(PeerNode.class);
        final Peer p = pn != null ? pn.getPeer() : null;
        final Group g = pn != null ? ((GroupNode) (pn.getParent())).getGroup() : null;
        String status = null;
        if (p != null && g != null) {
            status = STRINGS.getString("status.command.initiate") + ": " + p.getName();
            if (Logging.SHOW_INFO && LOG.isLoggable(Level.INFO)) {
                LOG.info(status);
            }
            new Thread(new Runnable() {

                public void run() {
                    process(g, pn);
                }
            }, getClass().getName() + ":getConnection").start();
        } else {
            String peerName = "null";
            if (p != null) {
                peerName = p.getName();
            }
            status = STRINGS.getString("error.peer.invalid") + " " + peerName;
            if (Logging.SHOW_SEVERE && LOG.isLoggable(Level.SEVERE)) {
                LOG.severe(status);
            }
        }
        if (status != null) {
            this.view.setStatus(status);
        }
    }

    private void process(Group g, PeerNode pn) {
        final Peer p = pn != null ? pn.getPeer() : null;
        String status = STRINGS.getString("status.peer.status.request") + ": " + p.getName();
        this.view.setStatus(status);
        if (Logging.SHOW_INFO && LOG.isLoggable(Level.INFO)) {
            LOG.info(status);
            LOG.info("instantiating StatusCommand");
        }
        RemoteCommandInvoker cf = new RemoteCommandInvoker(g, p.getPipeAdvertisement(), new StatusCommand(), this.view.getControl());
        if (Logging.SHOW_INFO && LOG.isLoggable(Level.INFO)) {
            LOG.info("dispatch command");
        }
        pn.setInfo(PeerNode.CONNECTION_ONGOING, "true", true);
        cf.invoke(MAX);
        DialogMessage r = cf.getResponse();
        if (Logging.SHOW_INFO && LOG.isLoggable(Level.INFO)) {
            LOG.info("StatusCommand response: " + r);
        }
        boolean isValid = true;
        String res = r != null ? r.getCommand() : null;
        if (res != null) {
            status = STRINGS.getString("status.peer.status.process") + ": " + p.getName();
            this.view.setStatus(status);
            if (Logging.SHOW_INFO && LOG.isLoggable(Level.INFO)) {
                LOG.info(status);
            }
            Resource rs = new Resource();
            try {
                rs.load(new ByteArrayInputStream(res.getBytes()));
            } catch (ResourceNotFoundException rnfe) {
                rnfe.printStackTrace();
            }
            StringBuffer sb = new StringBuffer();
            sb.append("profile:" + NEW_LINE);
            sb.append(TAB + "version: " + rs.get(PROFILE_VERSION) + NEW_LINE);
            sb.append(TAB + "jxta: " + rs.get(PROFILE_JXTA_HOME) + NEW_LINE);
            sb.append(TAB + "time: " + rs.get(PROFILE_TIME) + NEW_LINE);
            sb.append(TAB + "java: " + rs.get(PROFILE_JAVA_VERSION) + SLASH + rs.get(PROFILE_JAVA_VENDOR) + NEW_LINE);
            sb.append(TAB + "system: " + rs.get(PROFILE_OS_NAME) + SLASH + rs.get(PROFILE_OS_ARCHITECTURE) + SLASH + rs.get(PROFILE_PROCESSORS) + NEW_LINE);
            sb.append(TAB + "memory[f|m|t]: " + rs.get(PROFILE_FREE_MEMORY) + SLASH + rs.get(PROFILE_MAXIMUM_MEMORY) + SLASH + rs.get(PROFILE_TOTAL_MEMORY) + NEW_LINE);
            sb.append("peer:" + NEW_LINE);
            sb.append(TAB + "id: " + rs.get(PEER_ID) + NEW_LINE);
            sb.append(TAB + "name: " + rs.get(PEER_NAME) + NEW_LINE);
            sb.append(TAB + "is authenticated: " + rs.get(PEER_IS_AUTHENTICATED) + NEW_LINE);
            sb.append("rendezvous:" + NEW_LINE);
            sb.append(TAB + "role: " + rs.get(RENDEZVOUS_ROLE) + NEW_LINE);
            String id = null;
            for (Iterator pi = rs.getAll(RENDEZVOUS_PEER_ID).iterator(); pi.hasNext(); ) {
                id = (String) pi.next();
                String q = RENDEZVOUS_PEER + "[@id=\"" + id + "\"]";
                sb.append(TAB + "peer:" + NEW_LINE);
                sb.append(TAB + TAB + "id: " + id + NEW_LINE);
                sb.append(TAB + TAB + "name: " + rs.get(q + "/@name") + NEW_LINE);
                sb.append(TAB + TAB + "group id: " + rs.get(q + "/@groupId") + NEW_LINE);
                sb.append(TAB + TAB + "is alive: " + rs.get(q + "/@isAlive") + NEW_LINE);
                sb.append(TAB + TAB + "is in peer view: " + rs.get(q + "/@isInPeerView") + NEW_LINE);
                sb.append(TAB + TAB + "is throttling: " + rs.get(q + "/@isThrottling") + NEW_LINE);
                sb.append(TAB + TAB + "number of connected peers: " + rs.get(q + "/@numberOfConnectedPeers") + NEW_LINE);
                sb.append(TAB + TAB + "start time: " + rs.get(q + "/@startTime") + NEW_LINE);
                sb.append(TAB + TAB + "create time: " + rs.get(q + "/@createTime") + NEW_LINE);
                sb.append(TAB + TAB + "update time: " + rs.get(q + "/@updateTime") + NEW_LINE);
                sb.append(TAB + TAB + "direction: " + rs.get(q + "/@direction") + NEW_LINE);
            }
            for (Iterator pi = rs.getAll(RENDEZVOUS_PROVISIONER_ID).iterator(); pi.hasNext(); ) {
                id = (String) pi.next();
                String q = RENDEZVOUS_PROVISIONER + "[@id=\"" + id + "\"]";
                sb.append(TAB + "rendezvous:" + NEW_LINE);
                sb.append(TAB + TAB + "id: " + id + NEW_LINE);
                sb.append(TAB + TAB + "name: " + rs.get(q + "/@name") + NEW_LINE);
                sb.append(TAB + TAB + "type: " + rs.get(q + "/@type") + NEW_LINE);
                sb.append(TAB + TAB + "is connected: " + rs.get(q + "/@isConnected") + NEW_LINE);
                sb.append(TAB + TAB + "lease time: " + rs.get(q + "/@leaseTime") + NEW_LINE);
            }
            for (Iterator ci = rs.getAll(RENDEZVOUS_CLIENT_ID).iterator(); ci.hasNext(); ) {
                id = (String) ci.next();
                String q = RENDEZVOUS_CLIENT + "[@id=\"" + id + "\"]";
                sb.append(TAB + "client:" + NEW_LINE);
                sb.append(TAB + TAB + "id: " + id + NEW_LINE);
                sb.append(TAB + TAB + "name: " + rs.get(q + "/@name") + NEW_LINE);
                sb.append(TAB + TAB + "type: " + rs.get(q + "/@type") + NEW_LINE);
                sb.append(TAB + TAB + "is connected: " + rs.get(q + "/@isConnected") + NEW_LINE);
                sb.append(TAB + TAB + "lease time: " + rs.get(q + "/@leaseTime") + NEW_LINE);
            }
            sb.append("relays:" + NEW_LINE);
            for (Iterator ci = rs.getAll(RELAYS_CLIENT_ID).iterator(); ci.hasNext(); ) {
                id = (String) ci.next();
                String q = RELAYS_CLIENT + "[@id=\"" + id + "\"]";
                sb.append(TAB + "client:" + NEW_LINE);
                sb.append(TAB + TAB + "id: " + id + NEW_LINE);
                sb.append(TAB + TAB + "name: " + rs.get(q + "/@name") + NEW_LINE);
            }
            for (Iterator si = rs.getAll(RELAYS_SERVER_ID).iterator(); si.hasNext(); ) {
                id = (String) si.next();
                String q = RELAYS_SERVER + "[@id=\"" + id + "\"]";
                sb.append(TAB + "client:" + NEW_LINE);
                sb.append(TAB + TAB + "id: " + id + NEW_LINE);
                sb.append(TAB + TAB + "name: " + rs.get(q + "/@name") + NEW_LINE);
            }
            if (Logging.SHOW_FINE && LOG.isLoggable(Level.FINE)) {
                LOG.fine(sb.toString());
            }
            this.view.showMessageDialog(sb.toString());
        } else {
            isValid = false;
            status = STRINGS.getString("error.peer.status") + ": " + p.getName();
            this.view.setStatus(status);
            pn.setInfo(PeerNode.STATUS_REACHABLE, Boolean.FALSE, true);
            if (Logging.SHOW_INFO && LOG.isLoggable(Level.INFO)) {
                LOG.info("bad response: " + status);
            }
        }
        pn.setInfo(PeerNode.CONNECTION_ONGOING, null, true);
    }

    private PipeAdvertisement getPipeAdv(MessageElement me) {
        if (Logging.SHOW_INFO && LOG.isLoggable(Level.INFO)) {
            LOG.info("get pipeAdv");
        }
        XMLDocument sd = null;
        try {
            sd = (XMLDocument) StructuredDocumentFactory.newStructuredDocument(MimeMediaType.XMLUTF8, me.getStream());
        } catch (IOException ioe) {
            if (Logging.SHOW_SEVERE && LOG.isLoggable(Level.SEVERE)) {
                LOG.log(Level.SEVERE, "can\'t document", ioe);
            }
        }
        PipeAdvertisement pa = null;
        if (sd != null) {
            try {
                pa = (PipeAdvertisement) AdvertisementFactory.newAdvertisement(sd);
            } catch (RuntimeException ioe) {
                if (Logging.SHOW_SEVERE && LOG.isLoggable(Level.SEVERE)) {
                    LOG.log(Level.SEVERE, "can\'t get pipe advertisement", ioe);
                }
            }
        }
        return pa;
    }
}
