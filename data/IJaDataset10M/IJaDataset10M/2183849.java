package protopeer.network.flowbased;

import protopeer.network.*;

/**
 * Extremely simple implementation of the interface
 * <code>IDelayModel</code>. Returns the same delay
 * specified in the constructor for every path
 * in the network.
 *
 */
public class ConstantDelayModel implements IDelayModel {

    private double delay;

    /**
	 * Constructor. 
	 * 
	 * @param delay in milliseconds.
	 */
    public ConstantDelayModel(double delay) {
        this.delay = delay;
    }

    public double getDelay(NetworkAddress source, NetworkAddress destination, Message message) {
        return delay;
    }
}
