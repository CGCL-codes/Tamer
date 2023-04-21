package org.opennfc.cardlistener;

import org.opennfc.NfcException;

/**
 * The Topaz API provides an access to the tags compliant with the
 * Topaz 96/512 tags and the Jewel tags.
 *
 * @since Open NFC 4.0
 */
public interface TopazConnection extends Connection {

    /**
     * Checks if the tag is writable
     *
     * @return true if the tag is writable.
     *
     * @throws IllegalStateException the connection is closed.
     */
    boolean isChipWritable();

    /**
     * Reads a specified area of tag.
     *
     * @param offset the position within the tag of area to be read.
     * @param length the length of the area of the tag to be read.
     *
     * @return the data read.
     *
     * @throws IllegalArgumentException if <code>offset</code> or <code>length</code> are not valid.
     * @throws IllegalStateException the connection is closed.
     * @throws NfcException a NFC error occurred.
     */
    byte[] read(int offset, int length) throws NfcException;

    /**
     * Writes data into a specified area of a tag.
     *
     * @param buffer the data to be written in the tag.
     * @param offset the position of the tag of the area to be written.
     * @param lock Locks the sector containing the area after write.
     *
     * @throws IllegalArgumentException if <code>buffer</code> is null or empty.
     * @throws IllegalArgumentException if <code>offset</code> is not valid.
     * @throws IllegalStateException the connection is closed.
     * @throws NfcException a NFC error occurred.
     */
    void write(byte[] buffer, int offset, boolean lock) throws NfcException;

    /**
    * Returns he UID value.
     *
     * @throws IllegalStateException the connection is closed.
    */
    byte[] getUid();

    /**
     * Returns the block size in bytes.
     *
     * @throws IllegalStateException the connection is closed.
     */
    int getBlockSize();

    /**
     * Returns the sector number.
     *
     * @throws IllegalStateException the connection is closed.
     */
    int getBlockNumber();

    /**
     * Returns the header ROM HRO, HR1.
     *
     * @throws IllegalStateException the connection is closed.
     */
    byte[] getHeaderRom();
}
