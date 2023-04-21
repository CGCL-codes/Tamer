package de.innosystec.unrar.unpack.vm;

/**
 * DOCUMENT ME
 *
 * @author $LastChangedBy$
 * @version $LastChangedRevision$
 */
public class BitInput {

    /**
	 * the max size of the input
	 */
    public static final int MAX_SIZE = 0x8000;

    protected int inAddr;

    protected int inBit;

    protected byte[] inBuf;

    /**
	 * 
	 */
    public void InitBitInput() {
        inAddr = 0;
        inBit = 0;
    }

    /**
     * @param Bits 
     */
    public void addbits(int Bits) {
        Bits += inBit;
        inAddr += Bits >> 3;
        inBit = Bits & 7;
    }

    /**
     * @return the bits (unsigned short)
     */
    public int getbits() {
        return (((((inBuf[inAddr] & 0xff) << 16) + ((inBuf[inAddr + 1] & 0xff) << 8) + ((inBuf[inAddr + 2] & 0xff))) >>> (8 - inBit)) & 0xffff);
    }

    /**
     *  
     */
    public BitInput() {
        inBuf = new byte[MAX_SIZE];
    }

    /**
     * @param Bits add the bits
     */
    public void faddbits(int Bits) {
        addbits(Bits);
    }

    /**
     * @return get the bits
     */
    public int fgetbits() {
        return (getbits());
    }

    /**
     * Indicates an Overfow
     * @param IncPtr how many bytes to inc
     * @return true if an Oververflow would occur
     */
    public boolean Overflow(int IncPtr) {
        return (inAddr + IncPtr >= MAX_SIZE);
    }

    public byte[] getInBuf() {
        return inBuf;
    }
}
