package be.vanvlerken.bert.packetdistributor.common;

import java.io.Serializable;

/**
 * Internal representation of a chunk of data (the payload of a DatagramPacket for example)
 */
public class DataChunk implements Serializable {

    /**
     * Comment for <code>serialVersionUID</code>
     */
    private static final long serialVersionUID = 1L;

    private byte[] buffer;

    private int dataLength;

    public DataChunk(int size) {
        buffer = new byte[size];
        dataLength = 0;
    }

    public DataChunk(byte[] bytes) {
        buffer = bytes;
        dataLength = buffer.length;
    }

    /**
     * Returns the bytes in this DataChunk
     * @return byte[]
     */
    public byte[] getBytes() {
        return buffer;
    }

    /**
     * Returns the size of the DataChunk
     * @return int
     */
    public int getSize() {
        return buffer.length;
    }

    /**
     * Method setDataLength.
     * @param dataLength
     */
    public void setDataLength(int dataLength) {
        this.dataLength = dataLength;
    }

    /**
     * Returns the dataLength.
     * @return int
     */
    public int getDataLength() {
        return dataLength;
    }
}
