package eg.nileu.cis.nilestore.availablepeers.port;

import se.sics.kompics.Event;
import se.sics.kompics.address.Address;
import eg.nileu.cis.nilestore.common.NilestoreAddress;

/**
 * The Class RemovePeer.
 * 
 * @author Mahmoud Ismail <mahmoudahmedismail@gmail.com>
 */
public class RemovePeer extends Event {

    /** The nilestore address. */
    private final NilestoreAddress nilestoreAddress;

    /** The address. */
    private final Address address;

    /**
	 * Instantiates a new removes the peer.
	 * 
	 * @param address
	 *            the address
	 */
    public RemovePeer(Address address) {
        this.address = address;
        this.nilestoreAddress = null;
    }

    /**
	 * Instantiates a new removes the peer.
	 * 
	 * @param nilestoreAddress
	 *            the nilestore address
	 */
    public RemovePeer(NilestoreAddress nilestoreAddress) {
        this.nilestoreAddress = nilestoreAddress;
        this.address = null;
    }

    /**
	 * Gets the nilestore address.
	 * 
	 * @return the nilestore address
	 */
    public NilestoreAddress getNilestoreAddress() {
        return nilestoreAddress;
    }

    /**
	 * Gets the address.
	 * 
	 * @return the address
	 */
    public Address getAddress() {
        return address;
    }
}
