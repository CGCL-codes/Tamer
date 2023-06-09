package net.sourceforge.poi.util;

import net.sourceforge.poi.util.LittleEndian.BufferUnderrunException;
import java.io.*;

/**
 * behavior of a field at a fixed location within a byte array
 *
 * @author Marc Johnson (marc_johnson27591@hotmail.com
 */
public interface FixedField {

    /**
     * set the value from its offset into an array of bytes
     *
     * @param data the byte array from which the value is to be read
     *
     * @exception ArrayIndexOutOfBoundsException if the offset is out
     *            of the array's valid index range
     */
    public void readFromBytes(byte[] data) throws ArrayIndexOutOfBoundsException;

    /**
     * set the value from an InputStream
     *
     * @param stream the InputStream from which the value is to be
     *               read
     *
     * @exception BufferUnderrunException if there is not enough data
     *            available from the InputStream
     * @exception IOException if an IOException is thrown from reading
     *            the InputStream
     */
    public void readFromStream(InputStream stream) throws IOException, BufferUnderrunException;

    /**
     * write the value out to an array of bytes at the appropriate
     * offset
     *
     * @param data the array of bytes to which the value is to be
     *             written
     *
     * @exception ArrayIndexOutOfBoundsException if the offset is out
     *            of the array's valid index range
     */
    public void writeToBytes(byte[] data) throws ArrayIndexOutOfBoundsException;

    /**
     * return the value as a String
     *
     * @return the value as a String
     */
    public String toString();
}
