package hypercast;

/**
 * This interface defines functions of a physical address. 
 * It is implemented by any physical address class.
 * Physical address if defined for an overlay socket, it 
 * contains information for sending message through any of 
 * adapters in the overlay socket, i.e., Node Adapter
 * and Socket Adapter. It is different from I_UnderlayAddress
 * which contains information for sending message through 
 * a single socket in the undrlay network. For example, 
 * the addresses of DT servers, buddies are instances of 
 * I_UnderlayAddress.
 * 
 * @author HyperCast Team
 * @author Dongwen Wang
 * @version	2.0, Feb. 28, 2001
 * @author Jianping Wang
 * @version	3.0, Aug. 05, 2004
 * 
 * @see I_UnderlayAddress
 */
public interface I_PhysicalAddress extends I_NetworkAddress {

    /** Checks if an object matches this I_Address.
		* if <code>o</code> is null, false is returned.
		* @throws IllegalArgumentException if Object is not of same type.
		*/
    public boolean addressCheck(Object o);
}
