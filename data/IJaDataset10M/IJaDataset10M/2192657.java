package svc.core.io;

import svc.core.memory.Address;

/**
 * Output devices.
 *
 */
public interface OutputDevice extends IODevice {

    /**
	 * Transfers the output data.
	 *
	 */
    public void output();

    /**
	 * Sets the target of this input operation. For stream-output
	 * devices this target is where the data is to be transferred;
	 * for block-input devices this target is the beginning
	 * address of the block of data. 
	 * @param address the target address
	 */
    public void setTarget(Address address);
}
