package verinec.netsim.util.net.sockets.fsm;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.SocketAddress;
import verinec.netsim.NetSimException;
import verinec.netsim.constants.Layers;
import verinec.netsim.constants.Protocolls;
import verinec.netsim.constants.TCP;
import verinec.netsim.entities.packets.Packet;
import verinec.netsim.entities.packets.TCPPacket;
import verinec.netsim.loggers.events.Event;
import verinec.netsim.loggers.events.TCPEvent;
import verinec.netsim.loggers.events.TCPEventStateChange;
import verinec.netsim.util.net.sockets.SocketImpl;

/** A SocketImpl in syn received state
 * @author Dominik Jungo
 * @version $Revision: 47 $
 */
public class SocketImplSynRecieved extends FSMSocketImpl {

    /** Creates a new SocketImpl in syn received state.
     * @param parent the SocketImpl used by the Socket
     */
    public SocketImplSynRecieved(SocketImpl parent) {
        super(parent);
        jlogger.fine("Socket" + parent + "in Syn Received State");
    }

    /**
     * @see verinec.netsim.util.net.sockets.fsm.FSMSocketImpl#proccessUp(verinec.netsim.entities.packets.Packet)
     */
    public void proccessUp(Packet packet) {
        try {
            parent.is.addPacket((TCPPacket) packet);
        } catch (NetSimException e) {
            jlogger.severe("droping packet, buffer is full");
        }
        jlogger.fine("procesing packet :" + packet);
        String time = verinec.netsim.util.SimTime.currentTimeIntString(parent.getTransportLayer().currentTime());
        Event event = new Event(time, parent.getTransportLayer().getNode().getNodeID(), Layers.TRANSPORT, Protocolls.TCP, packet.getSrcAddress().toString(), packet.getDstAddress().toString());
        TCPEvent eventdetail = new TCPEvent(TCP.RECEIVE, (TCPPacket) packet);
        event.setEventDetail(eventdetail);
        if (((TCPPacket) packet).isAck() && !((TCPPacket) packet).isSyn()) {
            jlogger.finest("changing to stablished state");
            Event event1 = new Event(time, parent.getTransportLayer().getNode().getNodeID(), Layers.TRANSPORT, Protocolls.TCP, packet.getSrcAddress().toString(), packet.getDstAddress().toString());
            TCPEventStateChange eventdetail1 = new TCPEventStateChange(TCP.ESTABLISHED);
            event1.setEventDetail(eventdetail1);
            event.addEvent(event1);
            parent.getLogger().addEvent(event);
            new SocketImplEstablished(parent);
            jlogger.finest("change to stablished state");
            parent.activate();
        } else {
            jlogger.info("droped packet " + packet);
        }
    }

    /**
     * @see java.net.SocketImpl#available()
     */
    protected int available() throws IOException {
        return 0;
    }

    /**
     * @see java.net.SocketImpl#close()
     */
    protected void close() throws IOException {
        try {
            TCPPacket tpacket = parent.createTCPPacket();
            tpacket.setFin(true);
            parent.sendData(tpacket, null);
        } catch (NetSimException e) {
            e.printStackTrace();
        }
        new SocketImplFinWait1(parent);
    }

    /**
     * @see java.net.SocketImpl#sendUrgentData(int)
     */
    protected void sendUrgentData(int data) throws IOException {
        throw new IOException("cannot send urgent data in Syn Received state");
    }

    /**
     * @see java.net.SocketImpl#getInputStream()
     */
    protected InputStream getInputStream() throws IOException {
        throw new IOException("cannot send get Input Stream in Syn Received state");
    }

    /**
     * @see java.net.SocketImpl#getOutputStream()
     */
    protected OutputStream getOutputStream() throws IOException {
        throw new IOException("cannot send get Output Stream in Syn Received state");
    }

    /**
     * @see java.net.SocketImpl#connect(java.lang.String, int)
     */
    protected void connect(String host, int port) throws IOException {
        throw new IOException("cannot connect in Syn Received state");
    }

    /**
     * @see java.net.SocketImpl#bind(java.net.InetAddress, int)
     */
    protected void bind(InetAddress host, int port) throws IOException {
        throw new IOException("cannot bind in Syn Received state");
    }

    /**
     * @see java.net.SocketImpl#connect(java.net.InetAddress, int)
     */
    protected void connect(InetAddress address, int port) throws IOException {
        throw new IOException("cannot connect in Syn Received state");
    }

    /**
     * @see java.net.SocketImpl#connect(java.net.SocketAddress, int)
     */
    protected void connect(SocketAddress address, int timeout) throws IOException {
        throw new IOException("cannot connect in Syn Received state");
    }

    /**
     * @see java.net.SocketImpl#accept(java.net.SocketImpl)
     */
    protected void accept(java.net.SocketImpl s) throws IOException {
        throw new IOException("cannot accept in Syn Received state");
    }
}
