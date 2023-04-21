package edu.ycp.egr.ydlx;

/**
 * Interface describing memory-mapped hardware.
 * Could be actual memory (RAM, ROM) or an IO device.
 * 
 * @author David Hovemeyer
 */
public interface MemoryMapped extends Cloneable {

    /**
	 * Get the base address where this device is mapped.
	 * 
	 * @return the device's base address
	 */
    public int getBaseAddress();

    /**
	 * Get the number of bytes mapped by this device.
	 * 
	 * @return number of bytes mapped by this device
	 */
    public int getNumBytesMapped();

    /**
	 * Read a word.
	 * 
	 * @param offset offset within the mapped device's address space
	 * @return the word value read
	 */
    public int readWord(int offset);

    /**
	 * Write a word.
	 * 
	 * @param offset offset within the mapped device's address space
	 * @param value the word value to write
	 */
    public void writeWord(int offset, int value);
}
