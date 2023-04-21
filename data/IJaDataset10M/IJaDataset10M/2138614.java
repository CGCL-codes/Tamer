package de.joergjahnke.gameboy;

/**
 * Interface for a ROMManager which loads and discard ROM banks on demand
 *
 * @author Joerg Jahnke (joergjahnke@users.sourceforge.net)
 */
public interface ROMManager {

    /**
     * Store data of a ROM bank
     *
     * @param   romBank bank no. to save
     * @param   data    16k of ROM bank data
     */
    public void storeROMBank(final int romBank, final byte[] data);

    /**
     * Load ROM bank data
     *
     * @param   romBank bank no. to load
     * @return  16k of ROM bank data
     */
    public byte[] loadROMBank(final int romBank);

    /**
     * Clear contents and prepare for a new ROM file
     */
    public void reset();

    /**
     * Free resources of the ROM manager
     */
    public void destroy();
}
