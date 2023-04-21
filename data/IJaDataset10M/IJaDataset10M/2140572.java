package javaclient3.structures.rfid;

import javaclient3.structures.*;

/**
 * Structure describing a single RFID tag.
 * @author Radu Bogdan Rusu
 * @version
 * <ul>
 *      <li>v3.0 - Player 3.0 supported
 * </ul>
 */
public class PlayerRfidTag implements PlayerConstants {

    private int type;

    private byte[] guid;

    /**
     * @return Tag type.
     */
    public synchronized int getType() {
        return this.type;
    }

    /**
     * @param newType Tag type.
     */
    public synchronized void setType(int newType) {
        this.type = newType;
    }

    /**
     * @return GUID count.
     */
    public synchronized int getGuid_count() {
        return (this.guid == null) ? 0 : guid.length;
    }

    /**
     * @return The Globally Unique IDentifier (GUID) of the tag.
     */
    public synchronized byte[] getGuid() {
        return this.guid;
    }

    /**
     * @param newGuid The Globally Unique IDentifier (GUID) of the tag.
     */
    public synchronized void setGuid(byte[] newGuid) {
        this.guid = newGuid;
    }
}
