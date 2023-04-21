package nl.kbna.dioscuri.module.dma;

/**
 * Handler for the master DMA controller, providing implementations for 8-bit read and write functionality<BR>
 * This handler will be registered with the DMA class to provide device-specific methods for reading (memory -> device) 
 * and writing (device -> memory) a byte via DMA. 
 * 
 */
public abstract class DMA8Handler {

    protected String owner;

    /**
     * Device-specific implementation of the 8-bit DMA read functionality.<BR>
     * This provides a way for DMA to pass a byte read from memory (by way of DMA request)
     * to the device for further processing.
     * 
     * @param data  Byte from memory that is passed to the device for handling
     */
    public abstract void dma8ReadFromMem(byte data);

    /**
     * Device-specific implementation of the 8-bit DMA write functionality.<BR>
     * This provides a way for DMA to write a byte to memory (by way of DMA request)
     * passed from the device.
     * 
     * @return Byte from device that will be written to memory
     */
    public abstract byte dma8WriteToMem();
}
