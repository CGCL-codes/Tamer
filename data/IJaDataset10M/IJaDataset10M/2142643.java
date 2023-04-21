package com.rapplogic.xbee.xmpp.client;

import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;
import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;
import com.rapplogic.xbee.XBeeConnection;
import com.rapplogic.xbee.api.XBeeConfiguration;
import com.rapplogic.xbee.api.XBeeException;
import com.rapplogic.xbee.api.XBeePacket;
import com.rapplogic.xbee.util.ByteUtils;
import com.rapplogic.xbee.xmpp.XBeeXmppPacket;

/**
 * Provides communication with an XBee radio over XMPP.
 * A instance of XBeeXmppGateway must be running for this class to function.
 * <p/>
 * Connects to gateway and subscribes to the gateway, if not already in roster
 * Gateway does not need to be online at startup but of course send/receive will not be possible until gateway is online.
 * <p/>
 * Important: You must make sure the client's JID is in the the gateway's client list or it will not be able to communicate
 * with the gateway.  XMPP does not allow communication with another user (such as the gateway) until you have subscribed to/invited them 
 * AND they have subscribed to/accepted you.  If only the client has subscribed, it will receive presence events and can send messages
 * but these messages will not be received and no error will be generated.
 * 
 * @author andrew
 *
 */
public abstract class XBeeXmppClient extends XBeeXmppPacket implements ConnectionSink {

    private static final Logger log = Logger.getLogger(XBeeXmppClient.class);

    private String gateway;

    private XmppXBeeConnection connection;

    public XBeeXmppClient() {
        super(new XBeeConfiguration().withStartupChecks(false));
    }

    public XBeeXmppClient(XBeeConfiguration conf) {
        super(conf.withStartupChecks(false));
    }

    public void open(String user, String password, String gateway) throws XMPPException, XBeeException {
        this.open(null, null, user, password, gateway);
    }

    public void open(String server, Integer port, String user, String password, String gateway) throws XMPPException, XBeeException {
        this.setGateway(gateway);
        synchronized (this) {
            this.getXmppThing().getAvailableMap().put(this.getGateway(), Boolean.FALSE);
            this.getXmppThing().addRosterFriend(gateway);
            this.connectXmpp(server, port, user, password);
        }
        connection = new XmppXBeeConnection(this);
        this.initProviderConnection(connection);
    }

    public void open(String port, int speed) {
        throw new UnsupportedOperationException("Clients cannot connect via serial");
    }

    public Boolean isGatewayOnline() {
        return (Boolean) this.getXmppThing().getAvailableMap().get(this.getGateway());
    }

    public String getGateway() {
        return gateway;
    }

    public void setGateway(String gateway) {
        this.gateway = gateway;
    }

    public void processMessage(Chat chat, Message message) {
        try {
            if (log.isDebugEnabled()) {
                log.debug("Received packet from gateway: " + message.getBody());
            }
            int[] packet = this.decodeMessage(message);
            int[] packetWithStartByte = new int[packet.length + 1];
            packetWithStartByte[0] = XBeePacket.SpecialByte.START_BYTE.getValue();
            System.arraycopy(packet, 0, packetWithStartByte, 1, packet.length);
            connection.addPacket(packetWithStartByte);
        } catch (Exception e) {
            log.error("error processing message " + message.toXML(), e);
        }
    }

    /**
     * Called by XmppXBeeConnection
     */
    public void send(int[] packet) throws XBeeException, XMPPException {
        if (!XBeePacket.verify(packet)) {
            throw new XBeeException("Packet is malformed" + ByteUtils.toBase16(packet));
        }
        Message message = this.encodeMessage(packet);
        if (!this.isGatewayOnline() && !this.isOfflineMessages()) {
            throw new GatewayOfflineException();
        }
        if (log.isInfoEnabled()) {
            log.info("Sending request to gateway: " + ByteUtils.toBase16(packet));
        }
        this.getChat().sendMessage(message);
    }

    public void close() {
        try {
            if (this.isConnected()) {
                super.close();
            }
            if (this.getXmppThing().getConnection() != null) {
                log.info("Disconnecting XMPP Connection");
                this.getXmppThing().getConnection().disconnect();
            }
        } catch (Exception e) {
            log.error("failed to disconnect connection", e);
        }
        ((XBeeConnection) this.connection).close();
    }

    protected List<String> getRosterList() {
        List<String> gateway = new ArrayList<String>();
        gateway.add(this.getGateway());
        return gateway;
    }

    /**
	 * Returns the gateway chat object -- the only chat object for a client!
	 * 
	 * @return
	 */
    public Chat getChat() {
        return this.getXmppThing().getChatMap().get(this.getGateway());
    }

    public boolean waitForGatewayOnline(int wait) {
        log.info("waiting for gateway...");
        long start = System.currentTimeMillis();
        while (!this.isGatewayOnline()) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
            }
            if ((System.currentTimeMillis() - start) > wait) {
                break;
            }
        }
        return this.isGatewayOnline();
    }
}
