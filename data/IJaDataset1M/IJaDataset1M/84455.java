package org.amityregion5.projectx.client.communication;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.amityregion5.projectx.common.communication.Constants;
import org.amityregion5.projectx.common.communication.DatagramListener;

/**
 * A class for listening for multicast packets.
 * 
 * NOTE: Make sure you start() it!
 * 
 * @author Joe Stein
 * @author Daniel Centore
 */
public class MulticastCommunicationHandler extends Thread {

    private boolean keepListening = true;

    private ArrayList<DatagramListener> dgListeners = new ArrayList<DatagramListener>();

    private MulticastSocket sock;

    @Override
    public void run() {
        try {
            sock = new MulticastSocket(Constants.UDPORT);
            InetAddress group = InetAddress.getByName(Constants.UDPGROUP);
            sock.joinGroup(group);
            DatagramPacket packet;
            while (keepListening) {
                byte[] buf = new byte[256];
                packet = new DatagramPacket(buf, buf.length);
                sock.receive(packet);
                firePacketReceived(packet);
            }
        } catch (IOException ex) {
            Logger.getLogger(MulticastCommunicationHandler.class.getName()).log(Level.SEVERE, null, ex);
            keepListening = false;
        }
    }

    private void firePacketReceived(DatagramPacket pack) {
        for (DatagramListener dgl : dgListeners) {
            dgl.handle(pack);
        }
    }

    /**
     * Registers a listener for multicast stuff
     * 
     * @param listener The DatagramListener
     */
    public void registerListener(DatagramListener listener) {
        dgListeners.add(listener);
    }

    /**
     * Un-registers a listener
     * 
     * @param listener The DatagramListener
     */
    public void removeListener(DatagramListener listener) {
        dgListeners.remove(listener);
    }
}
