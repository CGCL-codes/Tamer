package ch.epfl.lsr.adhoc.services.neighboring;

/**
 * This class implements a single table entry added to the neighbor table for
 * each activ neighboring node.
 *
 * @see NeighborTable
 */
public class NeighborTableEntry {

    /**
     * The identifier of the node
     */
    private long nodeID;

    /**
     * The time at which this entry will expire
     */
    private long expTime;

    /** 
     * The name of the node
     */
    private String nodeName;

    /**
     * Constructor of a single entry to be added to the table
     *
     * @param ip The IP address of the node
     * @param port The port on which the node is emitting
     * @param entryTime The time at which the entry is created
     */
    NeighborTableEntry(long nodeID, long entryTime, int entryExp) {
        this.nodeID = nodeID;
        expTime = entryTime + entryExp * 1000;
        nodeName = null;
    }

    /**
     * Constructor of a single entry to be added to the table
     *
     * @param ip The IP address of the node
     * @param nodeName The name of the node
     * @param port The port on which the node is emitting
     * @param entryTime The time at which the entry is created
     */
    NeighborTableEntry(long nodeID, String nodeName, long entryTime, int entryExp) {
        this.nodeID = nodeID;
        this.nodeName = nodeName;
        expTime = entryTime + entryExp * 1000;
    }

    public long getID() {
        return nodeID;
    }

    public long getLifetime() {
        return expTime;
    }

    public String getNodeName() {
        return nodeName;
    }

    public void setLifetime(long value) {
        expTime = value;
    }

    public String toString() {
        return (nodeName + " (" + nodeID + ") -- " + (expTime - System.currentTimeMillis()));
    }

    /**
     * This method determins if two entries are equal. The entries are thought
     * to be so if the address and the port are equal.
     *
     * @param entry The entry to compare this entry to
     * @return True if equal, false otherwise.
     */
    public boolean equals(NeighborTableEntry entry) {
        return (this.nodeID == entry.getID());
    }
}
