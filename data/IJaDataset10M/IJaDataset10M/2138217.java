package protocol;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;
import org.w3c.dom.Element;
import protocol.handlers.PacketDispatcher;
import datautils.DataDeserializer;
import datautils.DataSerializer;

/**
 * A connection of our protocol.  The connection class is used to send and 
 * receive packets from a connection. It can also hold information about the
 * peer of this connection.
 */
public final class ProtocolConnection {

    private PacketDispatcher packetDispatcher;

    /**
     * Constructor
     * @param peerConnection A connected socket to the peer
     * @param pl The packer listener that will know about new packets.
     * @throws IOException
     */
    public ProtocolConnection(InputStream is, OutputStream os, PacketDispatcher pl) throws IOException {
        assert pl != null : "No packet listener!!";
        this.inputStream = is;
        this.outputStream = os;
        this.packetDispatcher = pl;
        startThreads();
    }

    /**
     * Adds a queue to the connection. If several queues are added, the packets
     * in them will be send in paralel, hence the advantage.
     * @param p the queue
     */
    public void addPacketQueueToQueue(PacketQueue p) {
        synchronized (sendMonitor) {
            this.queuesIterator.add(p);
            sendMonitor.notify();
        }
    }

    /**
     * Place a packet in the send queue. This method returns immediatly. The 
     * packet will be send when possible.
     * @param p
     */
    public void addPacketToQueue(Element p) {
        synchronized (sendMonitor) {
            packetQueue.add(p);
            sendMonitor.notify();
        }
    }

    /**
     * Handle the send queue. Basically loop forever and send packets if they 
     * exist.
     * @throws IOException
     */
    private void handleQueue() throws IOException {
        for (; ; ) {
            Element p;
            synchronized (sendMonitor) {
                while ((packetQueue.size() == 0) && (packetQueues.size() == 0)) {
                    try {
                        sendMonitor.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                if (packetQueue.size() != 0) {
                    p = packetQueue.remove(0);
                } else {
                    p = getPacketFromQueues();
                    if (p == null) {
                        continue;
                    }
                }
            }
            DataSerializer ds = new DataSerializer(this.outputStream);
            try {
                ds.writeBuffer(new Packet(p).toBuffer());
            } catch (InvalidPacketException e) {
                System.err.println("AAA! error happened!!");
            }
            this.outputStream.flush();
        }
    }

    private Element getPacketFromQueues() {
        Element p = null;
        while (p == null && queuesIterator.hasNext()) {
            p = queuesIterator.next().getPacket();
            if (p != null) {
                return p;
            }
            queuesIterator.remove();
        }
        if (queuesIterator.hasNext() == false) {
            queuesIterator = packetQueues.listIterator();
        }
        while (p == null && queuesIterator.hasNext()) {
            p = queuesIterator.next().getPacket();
            if (p != null) {
                return p;
            }
            queuesIterator.remove();
        }
        return null;
    }

    /**
     * Handle the receiving of packets.
     * Read them from a socket and notify about them to the packetListener. 
     * @throws Exception
     */
    private void receivePackets() throws Exception {
        DataDeserializer dd = new DataDeserializer(this.inputStream);
        for (; ; ) {
            try {
                this.packetDispatcher.handle(new Packet(dd.readBuffer()).getXmlElement(), this);
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Start the threads responsibe of sending and receiving messgaes.
     * Currently there are two threads for each client. This is no efficient
     * but we dont care.
     * This can be made more efficient by using nio (selectors), adding another 
     * class\thread to manage all connections. and with out (maybe almost with 
     * out) breaking code that uses the class.
     */
    private void startThreads() {
        new Thread(new Runnable() {

            public void run() {
                try {
                    handleQueue();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }, "Packet Sending").start();
        new Thread(new Runnable() {

            public void run() {
                try {
                    receivePackets();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, "Packet Receiver").start();
    }

    /** The input stream of the socket  */
    private InputStream inputStream;

    /** The outputstream of the socket  */
    private OutputStream outputStream;

    /** The send queue of packets waiting to be sent */
    private List<Element> packetQueue = Collections.synchronizedList(new ArrayList<Element>());

    /** The list of queues to send */
    private List<PacketQueue> packetQueues = Collections.synchronizedList(new ArrayList<PacketQueue>());

    private ListIterator<PacketQueue> queuesIterator = packetQueues.listIterator();

    private Object sendMonitor = new Object();

    public void close() {
        try {
            this.inputStream.close();
        } catch (IOException e) {
        }
        try {
            this.outputStream.close();
        } catch (IOException e) {
        }
    }

    public PacketDispatcher getPacketDispatcher() {
        return packetDispatcher;
    }
}
