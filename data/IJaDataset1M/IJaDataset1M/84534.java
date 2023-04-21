package de.tud.kom.nat.im.model;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.DatagramChannel;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import de.tud.kom.nat.comm.ICommFacade;
import de.tud.kom.nat.comm.IMessageProcessor;
import de.tud.kom.nat.comm.msg.IEnvelope;
import de.tud.kom.nat.comm.msg.IMessage;
import de.tud.kom.nat.comm.msg.IPeerID;
import de.tud.kom.nat.comm.msg.StayAliveMessage;
import de.tud.kom.nat.comm.msg.UDPPing;
import de.tud.kom.nat.comm.msg.UDPPong;
import de.tud.kom.nat.comm.util.BlockingHook;
import de.tud.kom.nat.comm.util.SocketFormatter;
import de.tud.kom.nat.nattrav.broker.ConnectionTargetRequestor;
import de.tud.kom.nat.nattrav.broker.IApplicationCallback;
import de.tud.kom.nat.util.Logger;

public class ChatApplicationCallback implements IApplicationCallback {

    private HashMap<IPeerID, InetSocketAddress> mappings = new HashMap<IPeerID, InetSocketAddress>();

    private static final ArrayList<InetSocketAddress> emptyResult = new ArrayList<InetSocketAddress>(0);

    private ICommFacade commFacade;

    private IPeerID myID;

    public ChatApplicationCallback(IPeerID myID) {
        this.myID = myID;
    }

    public Collection<InetSocketAddress> getRelayHostsFor(IPeerID targetID) {
        InetSocketAddress relay = mappings.get(targetID);
        if (relay == null) return emptyResult;
        ArrayList<InetSocketAddress> addr = new ArrayList<InetSocketAddress>(1);
        addr.add(relay);
        return addr;
    }

    public void setRelayHost(IPeerID hostID, InetSocketAddress relay) {
        Logger.log("Relayhost for " + hostID + " is " + relay);
        mappings.put(hostID, relay);
    }

    public InetSocketAddress getRelayHost(InetSocketAddress host) {
        return mappings.get(host);
    }

    public void sendUDPStayAlive(DatagramChannel connectedChannel) {
        try {
            commFacade.sendUDPMessage(connectedChannel, new StayAliveMessage(getOwnPeerID(), null, true));
            Logger.log("Sent UDP Stayalive for " + connectedChannel.socket().getRemoteSocketAddress());
        } catch (IOException e) {
            Logger.logError(e, "Error sending stay alive!");
        }
    }

    public void setCommFacade(ICommFacade commFacade) {
        this.commFacade = commFacade;
    }

    public boolean testUDPConnectivity(DatagramChannel datagramChannel) {
        return testUDPConnectivity(datagramChannel, (InetSocketAddress) datagramChannel.socket().getRemoteSocketAddress());
    }

    public boolean testUDPConnectivity(DatagramChannel datagramChannel, InetSocketAddress addr) {
        if (datagramChannel.isConnected() && addr != null && !datagramChannel.socket().getRemoteSocketAddress().equals(addr)) throw new IllegalArgumentException("When socket is connected, address as to be null or the remote address!");
        if (!datagramChannel.isConnected() && addr == null) throw new IllegalArgumentException("Address must not be null for unconnected channels!");
        if (datagramChannel.isConnected()) addr = ConnectionTargetRequestor.getInstance().getRemoteAddress(datagramChannel);
        boolean result = false;
        boolean hasBeenRegistered = commFacade.getChannelManager().getSelectionKey(datagramChannel) != null;
        if (!hasBeenRegistered && commFacade.getChannelManager().registerChannel(datagramChannel) == null) {
            Logger.logError(null, "Could not register channel to test connectivity!");
            return false;
        }
        try {
            IMessageProcessor msgProc = commFacade.getMessageProcessor();
            BlockingHook bh = BlockingHook.createAwaitMessageHook(datagramChannel, UDPPong.class);
            msgProc.installHook(bh, bh.getPredicate());
            try {
                Logger.log("Testing this socket: " + SocketFormatter.formatChannel(datagramChannel));
                IMessage msg = new UDPPing(getOwnPeerID());
                commFacade.sendUDPMessage(datagramChannel, msg, addr);
                IEnvelope env = bh.waitForMessage();
                result = (env != null);
            } finally {
                msgProc.removeHook(bh);
            }
        } catch (Exception e) {
            result = false;
        }
        if (!hasBeenRegistered) commFacade.getChannelManager().unregisterChannel(datagramChannel);
        return result;
    }

    public IPeerID getOwnPeerID() {
        return myID;
    }
}
