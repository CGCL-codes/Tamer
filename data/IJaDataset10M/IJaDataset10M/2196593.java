package nl.kbna.dioscuri.module;

import nl.kbna.dioscuri.exception.ModuleException;

/**
 * Abstract class representing a generic hardware module.
 * This class defines a template for a BIOS module, which may contain a system BIOS, VIDEO BIOS and optional BIOSes.
 *  
 */
public abstract class ModuleBIOS extends Module {

    /**
     * Returns the system BIOS code from ROM
     * 
     * @return byte[] biosCode containing the binary code of System BIOS
     */
    public abstract byte[] getSystemBIOS();

    /**
     * Sets the system BIOS code in ROM
     * 
     * @param byte[] biosCode containing the binary code of System BIOS
     * 
     * @return true if BIOS code is of specified SYSTEMBIOS_ROM_SIZE and store is successful, false otherwise
     * @throws ModuleException
     */
    public abstract boolean setSystemBIOS(byte[] biosCode) throws ModuleException;

    /**
     * Returns the Video BIOS code from ROM
     * 
     * @return byte[] biosCode containing the binary code of Video BIOS
     */
    public abstract byte[] getVideoBIOS();

    /**
     * Sets the Video BIOS code in ROM
     * 
     * @param byte[] biosCode containing the binary code of Video BIOS
     * 
     * @return true if BIOS code is of specified VIDEOBIOS_ROM_SIZE and store is successful, false otherwise
     * @throws ModuleException
     */
    public abstract boolean setVideoBIOS(byte[] biosCode) throws ModuleException;
}
