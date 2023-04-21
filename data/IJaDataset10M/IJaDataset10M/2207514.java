package gridsim.net.fnb;

import gridsim.net.*;

/**
 * This class is used by a router to inform users of a dropped packet.
 * @author Agustin Caminero, Universidad de Castilla La Mancha (Spain).
 * @since GridSim Toolkit 4.2
 */
public class FnbDroppedUserObject implements Packet {

    private int userID;

    private int objectID;

    private boolean isFile;

    /**
     * Create an object of this class.
     * @param userID the user id
     * @param objectID the packet id
     */
    public FnbDroppedUserObject(int userID, int objectID, boolean isfile) {
        this.userID = userID;
        this.objectID = objectID;
        this.isFile = isfile;
    }

    /**
     * Gets the isFile
     * @return true if this is a file, false otherwise
     */
    public boolean getIsFile() {
        return isFile;
    }

    /**
     * Gets the user id
     * @return user id
     */
    public int getUserID() {
        return userID;
    }

    /**
     * Gets this packet tag.
     * @return -1 since no packet tag has been stored in this class.
     * @pre $none
     * @post $none
     */
    public int getTag() {
        return -1;
    }

    /**
     * Sets an entity ID from the last hop that this packet has traversed.<br>
     * Note that this method is not used.
     * @param last  an entity ID from the last hop
     * @pre last > 0
     * @post $none
     */
    public void setLast(int last) {
    }

    /**
     * Gets an entity ID from the last hop that this packet has traversed.
     * @return -1 since no entity ID has been stored in this class.
     * @pre $none
     * @post $none
     */
    public int getLast() {
        return -1;
    }

    /**
     * Sets the network service type of this packet.<br>
     * Note that this method is not used.
     * @param serviceType   this packet's service type
     * @pre serviceType >= 0
     * @post $none
     */
    public void setNetServiceType(int serviceType) {
    }

    /**
     * Gets the network service type of this packet
     * @return -1 since no network service type has been stored in this class.
     * @pre $none
     * @post $none
     */
    public int getNetServiceType() {
        return -1;
    }

    /**
     * Returns the ID of the source of this packet.
     * @return -1 since no source ID has been stored in this class.
     * @pre $none
     * @post $none
     */
    public int getSrcID() {
        return -1;
    }

    /**
     * Returns the ID of this object (e.g. gridlet ID)
     * @return packet ID
     * @pre $none
     * @post $none
     */
    public int getID() {
        return objectID;
    }

    /**
     * Returns the destination id of this packet.
     * @return -1 since no destination ID has been stored in this class.
     * @pre $none
     * @post $none
     */
    public int getDestID() {
        return -1;
    }

    /**
     * Sets the size of this packet. <br>
     * Note that this method is not used.
     * @param size  size of the packet
     * @return <tt>false</tt> since this method is not used.
     * @pre size >= 0
     * @post $none
     */
    public boolean setSize(long size) {
        return false;
    }

    /**
     * Returns the size of this packet
     * @return size of the packet
     * @pre $none
     * @post $none
     */
    public long getSize() {
        return -1;
    }
}
