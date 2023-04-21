package net.sf.jnclib.tp.ssh2.crai;

/**
 * Interface for generating crypto-quality pseudo-random numbers.
 */
public interface CraiRandom {

    /**
     * Fill a byte buffer with pseudo-random bytes.
     * 
     * @param b the buffer to fill
     */
    public void getBytes(byte[] b);

    public byte[] getBytes(int len);

    public byte getByte();

    public short getShort();

    public int getInt();
}
