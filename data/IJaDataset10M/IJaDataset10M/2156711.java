package org.opennms.netmgt.linkd.snmp;

import java.net.InetAddress;
import org.opennms.netmgt.capsd.snmp.NamedSnmpVar;
import org.opennms.netmgt.capsd.snmp.SnmpTableEntry;

/**
 *<P>The IpNetToMediaTableEntry class is designed to hold all the MIB-II
 * information for one entry in the ipNetToMediaTable. The table effectively
 * contains a list of these entries, each entry having information
 * about one physical address. The entry contains the ifindex binding, the mac address,
 * ip address and entry type.</P>
 *
 * <P>This object is used by the IpNetToMediaTable to hold infomation
 * single entries in the table. See the IpNetToMediaTable documentation
 * form more information.</P>
 *
 * @author <A HREF="mailto:rssntn67@yahoo.it">Antonio</A>
 * @author <A HREF="mailto:sowmya@opennms.org">Sowmya</A>
 * @author <A HREF="mailto:weave@oculan.com">Weave</A>
 * @author <A>Jon Whetzel</A>
 * @author <A HREF="http://www.opennms.org/">OpenNMS</A>
 *
 *
 * @see IpNetToMediaTable
 * @see <A HREF="http://www.ietf.org/rfc/rfc1213.txt">RFC1213</A>
 */
public final class IpNetToMediaTableEntry extends SnmpTableEntry {

    public static final String INTM_INDEX = "ipNetToMediaIfIndex";

    public static final String INTM_PHYSADDR = "ipNetToMediaPhysAddress";

    public static final String INTM_NETADDR = "ipNetToMediaNetAddress";

    public static final String INTM_TYPE = "ipNetToMediatype";

    /**
	 * <P>The keys that will be supported by default from the 
	 * TreeMap base class. Each of the elements in the list
	 * are an instance of the IpNetToMediatable. Objects
	 * in this list should be used by multiple instances of
	 * this class.</P>
	 */
    public static NamedSnmpVar[] ms_elemList = null;

    /**
	 * <P>Initialize the element list for the class. This
	 * is class wide data, but will be used by each instance.</P>
	 */
    static {
        ms_elemList = new NamedSnmpVar[4];
        int ndx = 0;
        ms_elemList[ndx++] = new NamedSnmpVar(NamedSnmpVar.SNMPINT32, INTM_INDEX, ".1.3.6.1.2.1.4.22.1.1", 1);
        ms_elemList[ndx++] = new NamedSnmpVar(NamedSnmpVar.SNMPOCTETSTRING, INTM_PHYSADDR, ".1.3.6.1.2.1.4.22.1.2", 2);
        ms_elemList[ndx++] = new NamedSnmpVar(NamedSnmpVar.SNMPIPADDRESS, INTM_NETADDR, ".1.3.6.1.2.1.4.22.1.3", 3);
        ms_elemList[ndx++] = new NamedSnmpVar(NamedSnmpVar.SNMPINT32, INTM_TYPE, ".1.3.6.1.2.1.4.22.1.4", 4);
    }

    /**
	 * <P>The TABLE_OID is the object identifier that represents
	 * the root of the IP Address table in the MIB forest.</P>
	 */
    public static final String TABLE_OID = ".1.3.6.1.2.1.4.22.1";

    /**
	 * <P>Creates a default instance of the ipNetToMedia
	 * table entry map. The map represents a singular
	 * instance of the mac address table. Each column in
	 * the table for the loaded instance may be retreived
	 * either through its name or object identifier.</P>
	 *
	 * <P>The initial table is constructied with zero
	 * elements in the map.</P>
	 */
    public IpNetToMediaTableEntry() {
        super(ms_elemList);
    }

    public int getIpNetToMediaIfIndex() {
        Integer val = getInt32(IpNetToMediaTableEntry.INTM_INDEX);
        if (val == null) return -1;
        return val;
    }

    public String getIpNetToMediaPhysAddress() {
        return getHexString(IpNetToMediaTableEntry.INTM_PHYSADDR);
    }

    public InetAddress getIpNetToMediaNetAddress() {
        return getIPAddress(IpNetToMediaTableEntry.INTM_NETADDR);
    }

    public int getIpNetToMediatype() {
        Integer val = getInt32(IpNetToMediaTableEntry.INTM_TYPE);
        if (val == null) return -1;
        return val;
    }
}
