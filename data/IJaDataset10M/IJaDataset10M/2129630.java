package org.jpos.iso;

import java.io.Serializable;

/**
 * @author Eoin.Flood@orbiscom.com
 */
public interface ISOHeader extends Cloneable, Serializable {

    /**
     * Return this header as byte array.
     */
    public byte[] pack();

    /**
     * Create a new ISOHeader from a byte array.
     *
     * @return The Number of bytes consumed.
     */
    public int unpack(byte[] b);

    /**
     * Set the Destination address in this ISOHeader.
     */
    public void setDestination(String dst);

    /**
     * Return the destination address in this ISOHeader.
     * returns null if there is no destination address
     */
    public String getDestination();

    /**
     * Set the Source address in this ISOHeader.
     */
    public void setSource(String src);

    /**
     * Return the source address in this ISOHeader.
     * returns null if there is no source address
     */
    public String getSource();

    /**
     * return the number of bytes in this ISOHeader
     */
    public int getLength();

    /**
     * Swap the source and destination addresses in this ISOHeader
     * (if they exist).
     */
    public void swapDirection();

    /**
     * Allow object to be cloned.
     */
    public Object clone();
}
