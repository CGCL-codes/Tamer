package org.blaps.erazer;

/**
 * 
 * @author François JACOBS 26/04/2009
 */
public interface ErasePattern {

    /**
     * Generates random bytes and places them into a user-supplied byte array.
     * The number of random bytes produced is equal to the length of the byte array.
     * 
     * @param bytes
     */
    public abstract void getBytes(byte[] bytes);

    public abstract void getBytes(byte[] bytes, int off, int len);

    public abstract void reset();
}
