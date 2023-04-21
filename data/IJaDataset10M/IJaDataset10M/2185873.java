package protopeer.network.flowbased;

import java.util.*;
import org.apache.log4j.*;
import protopeer.measurement.*;
import protopeer.network.*;

/**
 * Creates network interfaces of type <code>FlowBasedNetworkInterface</code>
 * and maintains a mapping between the address and the interface.
 * 
 */
public class FlowBasedNetworkInterfaceFactory implements NetworkInterfaceFactory {

    private static final Logger logger = Logger.getLogger(FlowBasedNetworkInterfaceFactory.class);

    private int allocatedNodeAddresses;

    private Map<NetworkAddress, FlowBasedNetworkInterface> address2InterfaceMap;

    private FlowBasedNetworkModel networkModel;

    public FlowBasedNetworkInterfaceFactory(FlowBasedNetworkModel networkModel) {
        this.networkModel = networkModel;
        address2InterfaceMap = new LinkedHashMap<NetworkAddress, FlowBasedNetworkInterface>();
        allocatedNodeAddresses = 0;
    }

    /**
	 * Creates a new <code>IntegerNetworkAddress</code> and ensures
	 * that no address is used twice
	 * @return
	 */
    public NetworkAddress allocateAddress() {
        return new IntegerNetworkAddress(allocatedNodeAddresses++);
    }

    /**
	 * @return the number of still available node adresses
	 */
    public int getNumAvailableAddresses() {
        return Integer.MAX_VALUE - allocatedNodeAddresses;
    }

    /**
	 * Creates a new network interface of type <code>FlowBasedNetworkInterface</code>.
	 * If not address is specified, it asks the network model to allocate one.
	 * 
	 */
    public FlowBasedNetworkInterface createNewNetworkInterface(MeasurementLogger measurementLogger, NetworkAddress addressToBindTo) {
        if (addressToBindTo == null) {
            addressToBindTo = allocateAddress();
        }
        FlowBasedNetworkInterface networkInterface = new FlowBasedNetworkInterface(measurementLogger, addressToBindTo, networkModel, this);
        if (address2InterfaceMap.containsKey(addressToBindTo)) {
            logger.error("Address " + addressToBindTo + " allocated multiple times");
        }
        address2InterfaceMap.put(addressToBindTo, networkInterface);
        return networkInterface;
    }

    /**
	 * Returns the network interface with the specified <code>address</code>
	 * 
	 * @param address address of the searched interface
	 * @return the network interface with the specified <code>address</code>
	 */
    public FlowBasedNetworkInterface getNetworkInterface(NetworkAddress address) {
        return address2InterfaceMap.get(address);
    }
}
