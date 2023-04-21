package verinec.netsim.util.tables;

import java.util.Comparator;
import java.util.Iterator;
import verinec.netsim.addresses.IAddress;
import verinec.netsim.addresses.IPAddress;
import verinec.netsim.addresses.IPAddressNetmask;

/**
 * @author Dominik Jungo
 * @version $Revision: 47 $
 */
public class RoutingTable extends AddressTable {

    private class NetmaskAddressComparator implements Comparator {

        /**
		 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
		 */
        public int compare(Object arg0, Object arg1) {
            IPAddressNetmask ipN = (IPAddressNetmask) arg1;
            IPAddress ip = ipN.convert((IPAddress) arg0);
            IPAddress ip2 = new IPAddress(ipN.toString());
            return ip.compareTo(ip2);
        }
    }

    private class Entry extends AddressTable.Entry {

        private IAddress gateway;

        /**
		 * Creates a new AddressTable Entry (@see AddressTable.Entry)
		 * 
		 * @param destination
		 *            the destination address
		 * @param iface
		 *            the interface that is used to send data to the destination
		 *            address
		 * @param gateway
		 *            the gateway to which the data is send (which sends data
		 *            itself then to the destination or a further gateway)
		 * @param creation_time
		 *            time when the entry was created
		 */
        public Entry(IPAddressNetmask destination, IAddress iface, IAddress gateway, double creation_time) {
            super(destination, iface, creation_time);
            this.gateway = gateway;
        }

        /**
		 * gets the Gateway for this entry
		 * 
		 * @return the Gateway for this entry
		 */
        public IAddress getGateway() {
            return gateway;
        }
    }

    /**
	 * Creates a new RoutingTable
	 */
    public RoutingTable() {
        super();
    }

    /**
	 * adds an entry to the routing table
	 * 
	 * @param arg0
	 *            a key (destination address)
	 * @param arg1
	 *            the address of the interface
	 * @param arg2
	 *            the address of the used gateway
	 * @param time
	 *            creation time of this entry
	 * @return the old value with the key arg0.
	 *         <code>null<null> if there is no value with this key.
	 */
    public Object put(Object arg0, Object arg1, Object arg2, double time) {
        Object returnvalue = this.get(arg0);
        if (returnvalue != null) {
            remove(arg0);
        }
        entrySet().add(new Entry((IPAddressNetmask) arg0, (IAddress) arg1, (IAddress) arg2, time));
        return returnvalue;
    }

    /**
	 * gets the Address of the Interface over which data will be send to the
	 * destination address (<code>key</code>). A matching algrothym using
	 * netmasks is used. If no interface address matches the address of the
	 * interface connected to default gateway is returned.
	 * 
	 * @param key
	 *            the destination address
	 * @return address of the interface over which data will be send to
	 *         destination.
	 */
    public IAddress getInterface(IAddress key) {
        IAddress result = null;
        Iterator i = entrySet().iterator();
        Comparator comparator = new NetmaskAddressComparator();
        while (i.hasNext()) {
            Entry e = (Entry) i.next();
            if ((comparator.compare(key, e.getKey()) == 0 || (!i.hasNext())) && result == null) {
                result = (IAddress) e.getValue();
            }
        }
        return result;
    }
}
