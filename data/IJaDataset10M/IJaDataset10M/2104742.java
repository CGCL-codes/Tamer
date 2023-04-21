package org.davic.net.dvb;

import org.davic.net.InvalidLocatorException;

/**
 * DVB Locator that is bound to a network. An object of this type identifies
 * uniquely a given entity and the delivery system in which it is carried.
 * <p>
 * For example, a service may be carried in both satellite and terrestrial
 * networks and the DvbLocator identifying that service may be common, but both
 * of them will have a different DvbNetworkBoundLocator.
 */
public class DvbNetworkBoundLocator extends org.davic.net.dvb.DvbLocator implements org.davic.net.TransportDependentLocator {

    /**
	 * Constructor for a network bound locator
	 * @param unboundLocator an unbound DVB locator
	 * @param networkId network identifier of the network
	 * @exception InvalidLocatorException when the parameters to construct the
	 *                locator wouldn't specify a valid locator (e.g. a numeric
	 *                identifier out of range)
	 */
    public DvbNetworkBoundLocator(DvbLocator unboundLocator, int networkId) throws InvalidLocatorException {
        super(1, 1);
    }

    /**
	 * Returns the network_id
	 * @return network_id
	 */
    public int getNetworkId() {
        return 0;
    }
}
