package takatuka.routing.java.dymo;

/**
 * Represents a piece of routing information about one node in the network.
 * This information pieces are extracted from received packets to then decide 
 * about storing it as a rule in the routing table.
 * @author Thomas Stocker
 */
public class NodeInfo {

    /**
     * Number of bytes this information needs within a packet.
     */
    public static final int REQUIRED_BYTES = 5;

    /**
     * Address of the node.
     */
    public short address = -1;

    /**
     * Sequence number of the node.
     */
    public short seqNum = -1;

    /**
     * The distance to the node calculated from the actual node (the own node).
     */
    public byte distance = -1;

    /**
     * The corresponding DymoMessage.
     * If routing information is extracted from a DymoMessage,
     * a reference to it is kept to enable an extended calculation of its 
     * usefulness, when deciding to keep or discard it.
     */
    public DymoMessage cMessage = null;

    /**
     * Creates a NodeInfo object carrying only the node address and its 
     * sequence number. In some cases the distance to a node is not known.
     * @param address Address of the Node
     * @param seqNum Sequence number of the node
     */
    public NodeInfo(short address, short seqNum) {
        this.address = address;
        this.seqNum = seqNum;
    }

    /**
     * Creates a NodeInfo object carrying all necessary fields.
     * @param address Address of the Node
     * @param seqNum Sequence number of the node
     * @param distance Distance to the node
     */
    public NodeInfo(short address, short seqNum, byte distance) {
        this(address, seqNum);
        this.distance = distance;
    }

    /**
     * Creates a NodeInfo object carrying all necessary fields,
     * with an additional reference to the routing message where the information
     * is from.
     * @param address Address of the Node
     * @param seqNum Sequence number of the node
     * @param distance Distance to the node
     * @param cMessage Reference to routing message
     */
    public NodeInfo(short address, short seqNum, byte distance, DymoMessage cMessage) {
        this(address, seqNum, distance);
        this.cMessage = cMessage;
    }

    /**
     * Prints the routing information.
     */
    public void print() {
        System.out.print('[');
        System.out.print(address);
        System.out.print(' ');
        System.out.print(seqNum);
        System.out.print(' ');
        System.out.print(distance);
        System.out.print(' ');
        if (cMessage == null) {
            System.out.println('0');
        } else {
            System.out.println('1');
        }
        System.out.print(']');
    }
}
