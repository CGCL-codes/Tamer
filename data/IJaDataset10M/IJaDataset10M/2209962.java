package verinec.netsim.util.net.sockets.fsm;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.SocketAddress;
import verinec.netsim.NetSimException;
import verinec.netsim.entities.packets.Packet;
import verinec.netsim.entities.packets.TCPPacket;
import verinec.netsim.util.net.sockets.SocketImpl;

/** A SocketImpl in close wait state
 * @author Dominik Jungo
 * @version $Revision: 47 $
 */
public class SocketImplCloseWait extends FSMSocketImpl {

    /** Creates a new SocketImpl in close wait state.
     * @param parent the SocketImpl used by the Socket
     */
    public SocketImplCloseWait(SocketImpl parent) {
        super(parent);
        jlogger.fine("Socket" + parent + "in CloseWait State");
    }

    /**
     * @see verinec.netsim.util.net.sockets.fsm.FSMSocketImpl#proccessUp(verinec.netsim.entities.packets.Packet)
     */
    public void proccessUp(Packet packet) {
    }

    /**
     * @see java.net.SocketImpl#available()
     */
    protected int available() throws IOException {
        return parent.is.available();
    }

    /**
     * @see java.net.SocketImpl#close()
     */
    protected void close() throws IOException {
        jlogger.info("closing");
        try {
            TCPPacket tpacket = parent.createTCPPacket();
            tpacket.setAck(parent.getBuf());
            tpacket.setSeq(parent.getSeq());
            tpacket.setFin(true);
            parent.sendData(tpacket, parent.getLogger());
            parent.incrSeq();
            new SocketImplLastAck(parent);
        } catch (NetSimException e) {
            e.printStackTrace();
        }
    }

    /**
     * @see java.net.SocketImpl#sendUrgentData(int)
     */
    protected void sendUrgentData(int data) throws IOException {
    }

    /**
     * @see java.net.SocketImpl#getInputStream()
     */
    protected InputStream getInputStream() throws IOException {
        return parent.is;
    }

    /**
     * @see java.net.SocketImpl#getOutputStream()
     */
    protected OutputStream getOutputStream() throws IOException {
        return parent.os;
    }

    /**
     * @see java.net.SocketImpl#connect(java.lang.String, int)
     */
    protected void connect(String host, int port) throws IOException {
        throw new IOException("cannot connect in Close Wait State");
    }

    /**
     * @see java.net.SocketImpl#bind(java.net.InetAddress, int)
     */
    protected void bind(InetAddress host, int port) throws IOException {
        throw new IOException("cannot bind in Close Wait State");
    }

    /**
     * @see java.net.SocketImpl#connect(java.net.InetAddress, int)
     */
    protected void connect(InetAddress address, int port) throws IOException {
        throw new IOException("cannot connect in Close Wait State");
    }

    /**
     * @see java.net.SocketImpl#connect(java.net.SocketAddress, int)
     */
    protected void connect(SocketAddress address, int timeout) throws IOException {
        throw new IOException("cannot connect in Close Wait State");
    }

    /**
     * @see java.net.SocketImpl#accept(java.net.SocketImpl)
     */
    protected void accept(java.net.SocketImpl s) throws IOException {
        throw new IOException("cannot accept in Close Wait State");
    }
}
