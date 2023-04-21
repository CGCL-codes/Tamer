package sw_emulator.hardware.bus;

/**
 * The interface <code>readableBus</code> represents an bus where we can read
 * bytes.
 * To do this the methods <code>read</code> is provided.
 * For convenience the 16 bits of the address are stored in 32 bits (int type)
 *
 * @author Ice
 * @version 1.00 16/08/1999
 */
public interface readableBus {

    /**
   * Read a byte from the bus at specific address location.
   *
   * @param addr the address location
   * @return the readed byte
   */
    public byte read(int addr);
}
