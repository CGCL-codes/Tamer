package net.sf.anima.xmpp.packet;

import java.util.ArrayList;
import java.util.concurrent.LinkedBlockingQueue;
import org.apache.log4j.Logger;
import com.metamech.jabber.xml.XmlStanza;

public class PacketDispatcher extends Thread {

    static Logger logger = Logger.getLogger(PacketDispatcher.class);

    LinkedBlockingQueue<XmlStanza> packetQueue;

    ArrayList<PacketListener> packetListeners;

    boolean aktiv;

    public PacketDispatcher(LinkedBlockingQueue<XmlStanza> packetQueue) {
        this.packetQueue = packetQueue;
        packetListeners = new ArrayList<PacketListener>();
        aktiv = true;
    }

    public boolean addListener(PacketListener listener) {
        if (listener == null) {
            return false;
        }
        packetListeners.add(listener);
        return true;
    }

    public boolean removeListener(PacketListener listener) {
        packetListeners.remove(listener);
        return true;
    }

    public void run() {
        while (aktiv) {
            try {
                XmlStanza packet = packetQueue.take();
                logger.debug("Packet arrived: " + packet.toString());
                XmlStanza child;
                String matchString;
                if (packet.getElement().equals("iq")) {
                    child = packet.getFirstChild("query");
                    if (child == null) {
                        matchString = "iq";
                    } else {
                        matchString = child.getNamespace();
                    }
                } else {
                    matchString = packet.getElement();
                }
                synchronized (packetListeners) {
                    for (PacketListener listener : packetListeners) {
                        if (listener.getPacketType().equals(matchString)) {
                            listener.notify(packet);
                        }
                    }
                }
            } catch (InterruptedException e) {
                logger.error(e);
            } catch (Exception ex) {
                logger.error("QueueThread: ", ex);
            }
        }
    }
}
