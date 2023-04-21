package org.dhcp4java.server.struct;

import java.io.Serializable;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.logging.Logger;
import org.dhcp4java.DHCPOption;
import org.dhcp4java.HardwareAddress;
import org.dhcp4java.InetCidr;
import org.dhcp4java.server.config.ConfigException;
import org.dhcp4java.server.filter.AlwaysTrueFilter;
import org.dhcp4java.server.filter.RequestFilter;

/**
 * 
 * @author Stephan Hadinger
 * @version 0.71
 */
public class Subnet implements Serializable {

    private static final long serialVersionUID = 1L;

    private static final Logger logger = Logger.getLogger(Subnet.class.getName().toLowerCase());

    /** freely usable comment */
    private String comment = null;

    /** network range of the subnet = CIDR */
    private final InetCidr cidr;

    /** giaddr pointing to this Subnet */
    private final Collection<InetAddress> giaddrs = new LinkedList<InetAddress>();

    /** list of address ranges, sorted */
    private final SortedSet<AddressRange> addrRanges = new TreeSet<AddressRange>();

    /** list of static addresses already assigned */
    private final Map<HardwareAddress, InetAddress> staticAddressesByMac = new HashMap<HardwareAddress, InetAddress>();

    private final Map<InetAddress, HardwareAddress> staticAddressesByIp = new HashMap<InetAddress, HardwareAddress>();

    /** filter applicable to Subnet */
    private RequestFilter requestFilter = new AlwaysTrueFilter();

    /** array of dhcp options */
    private DHCPOption[] dhcpOptions = DHCPOPTION_0;

    public Subnet(InetCidr cidr) {
        if (cidr == null) {
            throw new NullPointerException();
        }
        this.cidr = cidr;
    }

    /**
     * 
     */
    @Override
    public String toString() {
        StringBuffer buf = new StringBuffer();
        buf.append("comment=").append(comment);
        return buf.toString();
    }

    /**
	 * @return Returns the comment.
	 */
    public String getComment() {
        return comment;
    }

    /**
	 * @param comment The comment to set.
	 */
    public void setComment(String comment) {
        this.comment = comment;
    }

    /**
	 * @return Returns the giaddrs.
	 */
    public Collection<InetAddress> getGiaddrs() {
        return giaddrs;
    }

    /**
	 * @return Returns the cidr.
	 */
    public InetCidr getCidr() {
        return cidr;
    }

    /**
	 * @return Returns the addrRanges.
	 */
    public SortedSet<AddressRange> getAddrRanges() {
        return addrRanges;
    }

    private static final DHCPOption[] DHCPOPTION_0 = new DHCPOption[0];

    /**
	 * @return Returns the dhcpOptions.
	 */
    public DHCPOption[] getDhcpOptions() {
        return dhcpOptions;
    }

    /**
	 * @param dhcpOptions The dhcpOptions to set.
	 */
    public void setDhcpOptions(DHCPOption[] dhcpOptions) {
        this.dhcpOptions = dhcpOptions;
    }

    public void addStaticAddress(HardwareAddress macAddr, InetAddress ipAddr) throws ConfigException {
        if (macAddr == null) {
            throw new NullPointerException("hardwareAddr is null");
        }
        if (ipAddr == null) {
            throw new NullPointerException("ipAddr is null");
        }
        if (!(ipAddr instanceof Inet4Address)) {
            throw new IllegalArgumentException("ipAddr is not IPv4 address");
        }
        if (staticAddressesByIp.containsKey(ipAddr)) {
            throw new ConfigException("static ip [" + ipAddr.getHostAddress() + "] is already used");
        }
        if (staticAddressesByMac.containsKey(macAddr)) {
            logger.warning("Hardware address [" + macAddr + "]already has an IP address statically assigned");
        }
    }

    /**
	 * @return Returns the requestFilter.
	 */
    public RequestFilter getRequestFilter() {
        return requestFilter;
    }

    /**
	 * @param requestFilter The requestFilter to set.
	 */
    public void setRequestFilter(RequestFilter requestFilter) {
        this.requestFilter = requestFilter;
    }
}
