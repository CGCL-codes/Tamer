package verinec.netsim.util.net.sockets.fsm;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.SocketImpl;
import java.util.Iterator;
import java.util.Vector;
import verinec.netsim.NetSimException;
import verinec.netsim.addresses.IAddress;
import verinec.netsim.addresses.IPAddress;
import verinec.netsim.constants.Layers;
import verinec.netsim.constants.Protocolls;
import verinec.netsim.constants.TCP;
import verinec.netsim.entities.packets.Packet;
import verinec.netsim.entities.packets.TCPPacket;
import verinec.netsim.loggers.events.Event;
import verinec.netsim.loggers.events.TCPEvent;
import verinec.netsim.loggers.events.TCPEventStateChange;
import verinec.netsim.util.Random;
import verinec.netsim.util.SimTime;

/** A SocketImpl in closed state
 * @author Dominik Jungo
 * @version $Revision: 662 $
 */
public class SocketImplClosed extends FSMSocketImpl {

    private Vector SocketImpls;

    private int backlog;

    /** Creates a new SocketImpl in closed state.
     * @param parent the SocketImpl used by the Socket
     */
    public SocketImplClosed(verinec.netsim.util.net.sockets.SocketImpl parent) {
        super(parent);
        backlog = 10;
        SocketImpls = new Vector();
        jlogger.fine("Socket" + parent + "in Closed State");
        String time = verinec.netsim.util.SimTime.currentTimeIntString(parent.getTransportLayer().currentTime());
        Event event = new Event(time, parent.getTransportLayer().getNode().getNodeID(), Layers.TRANSPORT, Protocolls.TCP, "", "");
        TCPEventStateChange eventdetail = new TCPEventStateChange(TCP.CLOSED);
        event.setEventDetail(eventdetail);
        parent.getLogger().addEvent(event);
        parent.setLogger(event);
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
        throw new IOException("Cannot close a closed Socket");
    }

    /**
     * @see java.net.SocketImpl#sendUrgentData(int)
     */
    protected void sendUrgentData(int data) throws IOException {
        throw new IOException("Cannot send data in closed state");
    }

    /**
     * @see java.net.SocketImpl#getInputStream()
     */
    protected InputStream getInputStream() throws IOException {
        throw new IOException("in closed state there is no InputStream");
    }

    /**
     * @see java.net.SocketImpl#getOutputStream()
     */
    protected OutputStream getOutputStream() throws IOException {
        throw new IOException("in closed state there is no OutputStream");
    }

    /**
     * @see java.net.SocketImpl#connect(java.lang.String, int)
     */
    protected void connect(String host, int port) throws IOException {
        connect(InetAddress.getByName(host), port);
    }

    /**
     * @see java.net.SocketImpl#bind(java.net.InetAddress, int)
     */
    protected void bind(InetAddress host, int port) throws IOException {
        if (port == 0) {
            do {
                port = 1024 + Random.getInstance().nextInt(65535 - 1024);
            } while (!parent.getTransportLayer().isPortFree(port));
        } else {
            if (!parent.getTransportLayer().isPortFree(port)) {
                throw new IOException("cannot bind to port " + port + ". port number already used with another socket.");
            }
        }
        byte[] haddress = host.getAddress();
        if (haddress[0] == 0 && haddress[1] == 0 && haddress[2] == 0 && haddress[3] == 0) {
            parent.getTransportLayer().getLocalAddress();
            host = ((IPAddress) parent.getTransportLayer().getInterface(new IPAddress((byte) 127, (byte) 0, (byte) 0, (byte) 1))).toInetAddress();
        }
        parent.setAddress(host);
        parent.setLocalPort(port);
        jlogger.info("binding host:" + host + " port:" + port);
        parent.getTransportLayer().bind(parent);
        String time = verinec.netsim.util.SimTime.currentTimeIntString(parent.getTransportLayer().currentTime());
        Event event = new Event(time, parent.getTransportLayer().getNode().getNodeID(), Layers.TRANSPORT, Protocolls.TCP, host.toString().substring(host.toString().indexOf("/") + 1), "");
        TCPEvent eventdetail = new TCPEvent(TCP.BIND);
        event.setEventDetail(eventdetail);
        parent.getLogger().addEvent(event);
        parent.setLogger(event);
    }

    /**
     * @see java.net.SocketImpl#connect(java.net.InetAddress, int)
     */
    protected void connect(InetAddress address, int port) throws IOException {
        connect(new InetSocketAddress(address, port), SO_TIMEOUT);
    }

    /**
     * @see java.net.SocketImpl#connect(java.net.SocketAddress, int)
     */
    protected void connect(SocketAddress address, int timeout) throws IOException {
        jlogger.entering(getClass().getName(), "connect", new Object[] { SocketAddress.class, Integer.class });
        jlogger.info("connecting to: " + address + " with timeout: " + timeout);
        String host = address.toString().substring(address.toString().indexOf("/") + 1, address.toString().indexOf(":"));
        String port = address.toString().substring(address.toString().indexOf(":") + 1);
        parent.setAddress(new IPAddress(host).toInetAddress());
        parent.setPort(Integer.parseInt(port));
        try {
            TCPPacket packet = parent.createTCPPacket();
            String time = SimTime.currentTimeIntString(parent.getTransportLayer().currentTime());
            Event event = new Event(time, parent.getTransportLayer().getNode().getNodeID(), Layers.TRANSPORT, Protocolls.TCP, packet.getSrcAddress().toString(), packet.getDstAddress().toString());
            TCPEvent eventdetail = new TCPEvent(TCP.CONNECT);
            event.setEventDetail(eventdetail);
            parent.getLogger().addEvent(event);
            Event event1 = new Event(time, parent.getTransportLayer().getNode().getNodeID(), Layers.TRANSPORT, Protocolls.TCP, packet.getSrcAddress().toString(), packet.getDstAddress().toString());
            TCPEventStateChange eventdetail1 = new TCPEventStateChange(TCP.SYNSEND);
            event1.setEventDetail(eventdetail1);
            event.addEvent(event1);
            parent.setLogger(event1);
            packet.setSyn(true);
            packet.setSeq(parent.getSeq());
            parent.sendData(packet, event1);
        } catch (NetSimException e) {
            e.printStackTrace();
        }
        parent.incrSeq();
        new SocketImplSynSend(parent);
        parent.passivate();
        jlogger.info("connected");
    }

    /**
     * @see java.net.SocketImpl#accept(java.net.SocketImpl)
     */
    protected synchronized void accept(SocketImpl s) throws IOException {
        jlogger.entering(getClass().getName(), "accept", SocketImpl.class);
        if (SocketImpls.size() <= backlog) {
            SocketImpls.add(s);
            parent.getTransportLayer().bind(s);
        } else {
            throw new IOException("can't accept more than " + backlog + " connections for this server socket");
        }
        ((verinec.netsim.util.net.sockets.SocketImpl) s).setLocalPort(getLocalPort());
        new SocketImplListen(s);
        String time = SimTime.currentTimeIntString(parent.getTransportLayer().currentTime());
        Event event = new Event(time, parent.getTransportLayer().getNode().getNodeID(), Layers.TRANSPORT, Protocolls.TCP, "", "");
        TCPEvent eventdetail = new TCPEvent(TCP.ACCEPT);
        event.setEventDetail(eventdetail);
        Event event1 = new Event(time, parent.getTransportLayer().getNode().getNodeID(), Layers.TRANSPORT, Protocolls.TCP, "", "");
        TCPEventStateChange eventdetail1 = new TCPEventStateChange(TCP.LISTEN);
        event1.setEventDetail(eventdetail1);
        event.addEvent(event1);
        parent.getLogger().addEvent(event);
        parent.setLogger(event);
        parent.passivate();
        jlogger.info("accepting Socket " + s);
        jlogger.exiting(getClass().getName(), "accept");
    }

    /**
     * @see verinec.netsim.util.net.sockets.fsm.FSMSocketImpl#proccessUp(verinec.netsim.entities.packets.Packet)
     */
    public void proccessUp(Packet packet) {
        int port = ((TCPPacket) packet).getDstPort();
        Iterator iter = SocketImpls.iterator();
        verinec.netsim.util.net.sockets.SocketImpl unbound = null;
        while (iter.hasNext()) {
            verinec.netsim.util.net.sockets.SocketImpl s = (verinec.netsim.util.net.sockets.SocketImpl) iter.next();
            if (port == s.getLocalPort()) {
                s.setLogger(parent.getLogger());
                s.proccessUp(packet);
                break;
            } else if (s.getPort() == 0) {
                unbound = s;
            }
        }
        if (unbound == null) {
            new TCPPacket(packet.getModel(), (IPAddress) packet.getDstAddress(), (IPAddress) packet.getSrcAddress(), port, ((TCPPacket) packet).getSrcPort());
        } else {
            unbound.setLogger(parent.getLogger());
            unbound.proccessUp(packet);
        }
    }

    /** Sets the number of accpeting connections
     * @param backlog the number of accpeting connections
     */
    public void setBacklog(int backlog) {
        this.backlog = backlog;
    }

    /** gets a bound socket from this host, null if there isn't a bound socket with specified parameters
	 * @param localport localport of the bound socket
	 * @param remoteaddress remote adddress of the bound socket
	 * @param remoteport remote port of the bound socket
	 * @return the bound socket
	 */
    public SocketImpl getSocketImpl(int localport, IAddress remoteaddress, int remoteport) {
        verinec.netsim.util.net.sockets.SocketImpl returnvalue = null;
        Iterator iter = SocketImpls.iterator();
        while (iter.hasNext()) {
            verinec.netsim.util.net.sockets.SocketImpl si = (verinec.netsim.util.net.sockets.SocketImpl) iter.next();
            if (new IPAddress((Inet4Address) si.getAddress()).equals(remoteaddress) && si.getLocalPort() == localport && si.getPort() == remoteport) {
                returnvalue = si;
            }
        }
        return returnvalue;
    }
}
