package sim.toolkit;

import javacard.framework.*;

/**
 * The interface of SAT Accessor.
 */
public interface AccessSAT extends Shareable {

    /**
     * Gets the length of the APDUBuffer.
     * @return requested length
     */
    public short getAPDUBufferLength();

    /**
     * Gets the maximum length of the APDUBuffer.
     * @return requested length
     */
    public short getAPDUBufferMax();

    /**
     * Gets one byte from the APDUBuffer.
     * @param index Index of requested byte in the buffer
     * @return requested byte
     */
    public byte getAPDUBufferByte(short index);

    /**
     * Sets one byte from the APDUBuffer.
     * @param index Index of byte in the buffer
     * @param value The value to be set
     */
    public void setAPDUBufferByte(short index, byte value);

    /**
     * Sets the data in the out buffer.
     * @param length length of data
     */
    public void setOutBufferData(short length);

    /**
     * Returns the length of Data that has been set in the out buffer.
     * @return length of data
     */
    public short getOutDataLength();

    /**
     * Sets the event listener applet.
     * @param aid applet AID
     */
    public void setEventListener(AID aid);

    /**
     * Removes the event listener from the list of listeners.
     * @param aid applet AID
     */
    public void clearEventListener(AID aid);

    /**
     * Returns true if the applet corresponding to the AID passed to this 
     * method is found in the list of listeners.
     * @param aid applet AID
     * @return true if the applet is a listener and false otherwise
     */
    public boolean isEventListenerSet(AID aid);
}
