package routing.bgr;

import java.io.IOException;
import takatuka.drivers.radio.Packet;
import takatuka.routing.*;
import takatuka.drivers.interfaces.*;

/**
 * Geographic Routing Protocol using BGR (Beaconless GeoRouting) mechanism.
 * Every node is assumed to have a x/y-coordinate in Euclidean space.
 * Data can be sent from one node to another node with known ID and Position.
 * Therefore each node receiving the packet calculates a timer dependend on
 * its distance to the destination. When the timer expires the node retransmits
 * the packet. Any other node waiting to send the same Packet cancel its timer
 * and does not retransmit the packet because its distance to the destination
 * has to be bigger (bigger timer value).
 * 
 * For more information about BGR see:
 * "BGR: Blind Geographic Routing for Sensor Networks"
 * by Matthias Witt and Volker Turau
 * Department of Telematics, Hamburg University of Technology
 * 
 * @author Alexander Schaetzle, Martin Przyjaciel-Zablocki
 * @version 1.6
 */
public class BGR_XY extends AbstractRoutingProtocol {

    private SendThread sender;

    private boolean debugMode;

    private static final byte HEADER_SIZE = 9;

    private static final byte PACKET_ID = 0;

    private static final byte SRC_ID = 1;

    private static final byte FORWARDER_ID = 2;

    private static final byte DEST_ID = 3;

    private static final byte TTL = 4;

    private static final byte FORWARDER_POS = 5;

    private static final byte DEST_POS = 7;

    private static byte MAX_TTL_VALUE = 5;

    private static int MAX_DELAY = 2000;

    /**
     * POSITION INFORMATION
     * - my position
     * - position of of my predecessor
     * - position of the destination
     */
    private byte myXPos;

    private byte myYPos;

    private byte predecessorXPos;

    private byte predecessorYPos;

    private byte destXPos;

    private byte destYPos;

    private float myDistToDest;

    private float predecessorDistToDest;

    private int delay;

    private byte packetID;

    private static Packet routingPacket = linkLayer.makePacket();

    private boolean packetLossMode;

    /**
     * History of forwarded Packets.
     * The Packet IDs of the last 10 forwarded Packets are stored
     * in order to avoid the retransmission of already forwarded Packets.
     * The combination of PacketID and SourceID is needed to distinguish
     * different senders which may have the same PacketID.
     */
    public static byte[] forwardedPacketIDs = new byte[10];

    public static byte[] forwardedPacketSources = new byte[10];

    public byte fwPointer;

    /**
     * History of received Packets.
     * The Packet IDs of the last 10 received Packets are stored
     * in order to avoid that a Packet is received several times.
     * The combination of PacketID and SourceID is needed to distinguish
     * different senders which may have the same PacketID.
     */
    public static byte[] receivedPacketIDs = new byte[10];

    public static byte[] receivedPacketSources = new byte[10];

    public byte rcPointer;

    /**
     * Generates a new Instance of the BGR Routing Protocol.
     * The class which is called when a packet is received for the node
     * must be given as parameter. When a packet is reveived for this node
     * the receiveData-function of this class is called.
     *
     * @see takatuka.routing.IDataReceiver#receiveData(byte[], short)
     *
     * @param _receiver     the Data Receiver for the node
     */
    public BGR_XY(IDataReceiver _receiver) {
        super();
        dataReceiver = _receiver;
        packetID = 0;
        fwPointer = 0;
        rcPointer = 0;
        routingPacket.source = factory.getMoteInfoDriver().getMoteID();
        routingPacket.destination = IRadio.AM_BROADCAST_ADDR;
        sender = new SendThread(linkLayer);
        debugMode = false;
        packetLossMode = false;
        myXPos = -1;
        myYPos = -1;
        destXPos = -1;
        destYPos = -1;
        sender.start();
    }

    /**
     * Send Data from the Node to another Node.
     * Before sending the Data the Position of the Node and the Position
     * of the Destination Node must be set up using the setMyPos- and
     * setDestPos-functions. The amount of Data which can be send with one
     * packet is limited to maxPayloadLength - 9.
     *
     * @see #setMyPos(byte, byte)
     * @see #setDestPos(byte, byte)
     *
     * @param data          the Data to be sent
     * @param destination   ID of the Destination Node
     * @throws java.io.IOException
     */
    public void sendData(byte[] data, short destination) throws IOException {
        if (data.length > (factory.getRadioDriver().maxPayloadLength() - HEADER_SIZE)) throw new IOException("Data is too long");
        if (myXPos == -1 || myYPos == -1) {
            throw new IOException("Own Position not specified");
        }
        if (destXPos == -1 || destYPos == -1) {
            throw new IOException("Position of Destination not specified");
        }
        inc_packetID();
        routingPacket.data[PACKET_ID] = packetID;
        routingPacket.data[SRC_ID] = (byte) factory.getMoteInfoDriver().getMoteID();
        routingPacket.data[FORWARDER_ID] = (byte) factory.getMoteInfoDriver().getMoteID();
        routingPacket.data[DEST_ID] = (byte) destination;
        routingPacket.data[TTL] = MAX_TTL_VALUE;
        routingPacket.data[FORWARDER_POS] = myXPos;
        routingPacket.data[FORWARDER_POS + 1] = myYPos;
        routingPacket.data[DEST_POS] = destXPos;
        routingPacket.data[DEST_POS + 1] = destYPos;
        routingPacket.usedLength = (byte) (data.length + HEADER_SIZE);
        copyDataToRoutingPacket(data);
        if (packetLossMode) {
            linkLayer.sendPacket(routingPacket);
            Thread.sleep(75);
            linkLayer.sendPacket(routingPacket);
            Thread.sleep(75);
            linkLayer.sendPacket(routingPacket);
            Thread.sleep(75);
        }
        linkLayer.sendPacket(routingPacket);
    }

    /**
     * Called when a packet is received.
     * This methods decides whether the node is the desired destination of the
     * packet or not. If not a timer is calculated dependend on the distance
     * to the destination. When the timer expires the packet is retransmitted.
     * If the packet is received again from another node before the timer
     * expires the timer will be cancelled and the packet is not retransmitted.
     *
     * @param p     the received Routing-Packet
     */
    public void receivePacket(Packet p) {
        int myID = factory.getMoteInfoDriver().getMoteID();
        if (p.data[SRC_ID] == myID) {
            return;
        }
        if (p.data[FORWARDER_ID] == myID) {
            return;
        }
        if (p.data[DEST_ID] != myID) {
            if (alreadyForwarded(p.data[PACKET_ID], p.data[SRC_ID])) {
                if (sender.isBusy() && sender.getPredecessor() != p.data[FORWARDER_ID]) {
                    sender.cancelSend();
                }
                return;
            }
            if (debugMode) {
                factory.getLEDDriver().setOn((byte) 1);
                Thread.sleep(100);
                factory.getLEDDriver().setOffAll();
            }
            if (sender.isBusy()) {
                if (debugMode) System.out.println("Node is busy");
                return;
            }
            if (myXPos == -1 || myYPos == -1) {
                if (debugMode) System.out.println("Own Position not specified");
                return;
            }
            destXPos = p.data[DEST_POS];
            destYPos = p.data[DEST_POS + 1];
            predecessorXPos = p.data[FORWARDER_POS];
            predecessorYPos = p.data[FORWARDER_POS + 1];
            myDistToDest = calculateDistance(myXPos, myYPos, destXPos, destYPos);
            predecessorDistToDest = calculateDistance(predecessorXPos, predecessorYPos, destXPos, destYPos);
            if (myDistToDest >= predecessorDistToDest) {
                return;
            }
            if (p.data[TTL] - 1 > 0) {
                sender.setPredecessor(p.data[FORWARDER_ID]);
                addForwardedPacket(p.data[PACKET_ID], p.data[SRC_ID]);
                updateRoutingPacket(p);
                delay = calculateDelay();
                if (debugMode) {
                    System.out.println(p.data[PACKET_ID] + " : " + delay);
                }
                sender.sendPacket(routingPacket, delay);
            }
        } else {
            if (alreadyReceived(p.data[PACKET_ID], p.data[SRC_ID])) {
                return;
            }
            addReceivedPacket(p.data[PACKET_ID], p.data[SRC_ID]);
            dataReceiver.receiveData(getDataFromRoutingPacket(p), (short) p.data[SRC_ID]);
        }
    }

    /**
     * Sets the Position of the Node
     *
     * @param x
     * @param y
     */
    public void setMyPos(byte x, byte y) {
        myXPos = x;
        myYPos = y;
    }

    /**
     * Sets the Position of the Destination Node
     *
     * @param x
     * @param y
     */
    public void setDestPos(byte x, byte y) {
        destXPos = x;
        destYPos = y;
    }

    /**
     * Calculates the euclidean distance between two nodes
     *
     * @param x1
     * @param y1
     * @param x2
     * @param y2
     * @return      distance of the two nodes
     */
    private float calculateDistance(byte x1, byte y1, byte x2, byte y2) {
        float xdiff = (float) x2 - (float) x1;
        float ydiff = (float) y2 - (float) y1;
        float xdiff2 = (float) (xdiff * xdiff);
        float ydiff2 = (float) (ydiff * ydiff);
        return (float) Math.sqrt(xdiff2 + ydiff2);
    }

    /**
     * Calculates the Delay for the forwarding of a packet
     *
     * @return      Delay for the timer
     */
    private int calculateDelay() {
        return (int) (myDistToDest / predecessorDistToDest * MAX_DELAY);
    }

    /**
     * Copies the Data to be sent into the Routing Packet.
     *
     * @param data    original Data to be sent
     */
    private void copyDataToRoutingPacket(byte[] data) {
        for (byte i = 0; i < data.length; i++) {
            routingPacket.data[HEADER_SIZE + i] = data[i];
        }
    }

    /**
     * Updates the HEADER-FIELDS of the Routing-Packet when the
     * packet is forwarded by the node.
     *
     * @param p     Routing Packet
     */
    private void updateRoutingPacket(Packet p) {
        routingPacket.source = p.data[SRC_ID];
        routingPacket.destination = IRadio.AM_BROADCAST_ADDR;
        routingPacket.data = p.data;
        routingPacket.usedLength = p.usedLength;
        routingPacket.data[TTL] = (byte) (p.data[TTL] - 1);
        routingPacket.data[FORWARDER_ID] = (byte) factory.getMoteInfoDriver().getMoteID();
        routingPacket.data[FORWARDER_POS] = myXPos;
        routingPacket.data[FORWARDER_POS + 1] = myYPos;
    }

    /**
     * Returns the Data from a received Routing Packet.
     *
     * @param p     The received Packet
     * @return      Byte-Array with the Data
     */
    private byte[] getDataFromRoutingPacket(Packet p) {
        byte dataLength = (byte) (p.usedLength - HEADER_SIZE);
        byte[] data = new byte[dataLength];
        for (byte i = 0; i < dataLength; i++) {
            data[i] = p.data[HEADER_SIZE + i];
        }
        return data;
    }

    /**
     * Checks whether a Packet has already been forwarded.
     * A Packet is uniquely defined by its PacketID and SourceID.
     *
     * @param packetID      ID of the Packet
     * @param sourceID      ID of the Sender
     * @return              true (already forwarded), false (not forwarded)
     */
    private boolean alreadyForwarded(byte packetID, byte sourceID) {
        for (byte i = 0; i < forwardedPacketIDs.length; i++) {
            if ((forwardedPacketIDs[i] == packetID) && (forwardedPacketSources[i] == sourceID)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Adds a Packet to the List of forwarded Packets.
     * The PacketID and SourceID are stored in a circulating List of 10 elements.
     *
     * @param packetID      ID of the Packet
     * @param sourceID      ID of the Sender
     */
    private void addForwardedPacket(byte packetID, byte sourceID) {
        forwardedPacketIDs[fwPointer] = packetID;
        forwardedPacketSources[fwPointer] = sourceID;
        if (fwPointer == forwardedPacketIDs.length - 1) {
            fwPointer = 0;
        } else fwPointer++;
    }

    /**
     * Checks whether a Packet has already been received.
     * A Packet is uniquely defined by its PacketID and SourceID.
     *
     * @param packetID      ID of the Packet
     * @param sourceID      ID of the Sender
     * @return              true (already received), false (not received)
     */
    private boolean alreadyReceived(byte packetID, byte sourceID) {
        for (byte i = 0; i < receivedPacketIDs.length; i++) {
            if ((receivedPacketIDs[i] == packetID) && (receivedPacketSources[i] == sourceID)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Adds a Packet to the List of received Packets.
     * The PacketID and SourceID are stored in a circulating List of 10 elements.
     *
     * @param packetID      ID of the Packet
     * @param sourceID      ID of the Sender
     */
    private void addReceivedPacket(byte packetID, byte sourceID) {
        receivedPacketIDs[rcPointer] = packetID;
        receivedPacketSources[rcPointer] = sourceID;
        if (rcPointer == receivedPacketIDs.length - 1) {
            rcPointer = 0;
        } else rcPointer++;
    }

    /**
     * Sets the TTL-value to the specified value.
     *
     * @param value     new TTL-value
     */
    public void setTTL(byte value) {
        MAX_TTL_VALUE = value;
    }

    /**
     * Sets the maximum Delay when forwarding a packet.
     *
     * @param value     new MAX_DELAY
     */
    public void setMaxDelay(int value) {
        MAX_DELAY = value;
    }

    /**
     * Increments the PacketID.
     * The PacketID is used to distinguish different Packets from one Sender.
     */
    private void inc_packetID() {
        packetID += 1;
    }

    /**
     * Turns the Debug Mode on.
     */
    public void setDebugModeOn() {
        debugMode = true;
        sender.setDebugModeOn();
    }

    /**
     * Turns the Debug Mode off.
     */
    public void setDebugModeOff() {
        debugMode = false;
        sender.setDebugModeOff();
    }

    /**
     * Turns the Packet Loss Mode on.
     * When Packet Loss Mode is activated every packet is sent four times
     * in order to handle high packet loss rates of the motes.
     */
    public void setPacketLossModeOn() {
        packetLossMode = true;
    }

    /**
     * Turn the Packet Loss Mode off.
     * When Packet Loss Mode is deactivated every packet is sent only once.
     * This is the default mode if nothing is specified.
     */
    public void setPacketLossModeOff() {
        packetLossMode = false;
    }
}
