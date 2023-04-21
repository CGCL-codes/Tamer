package org.dhcpcluster.config;

import java.io.Serializable;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.Logger;
import org.dhcp4java.DHCPConstants;
import org.dhcp4java.InetCidr;
import org.dhcpcluster.filter.AlwaysTrueFilter;
import org.dhcpcluster.filter.RequestFilter;
import org.dhcpcluster.struct.Node;
import org.dhcpcluster.struct.Subnet;

/**
 * 
 * @author Stephan Hadinger
 * @version 0.72
 */
public class TopologyConfig implements Serializable {

    private static final long serialVersionUID = 1L;

    @SuppressWarnings("unused")
    private static final Logger logger = Logger.getLogger(GlobalConfig.class);

    /** list of subnets hashed by their cidr */
    private final Map<InetCidr, Subnet> subnetsByCidr = new HashMap<InetCidr, Subnet>();

    /** lowest mask value in all subnets declared */
    private int lowestMask = 32;

    /** highest mask value in all subnets declared */
    private int highestMask = -1;

    private Node rootNode = new Node();

    /** provide a fast search for subnets via an associated giaddr */
    private final Map<InetAddress, Subnet> subnetsByGiaddr = new HashMap<InetAddress, Subnet>();

    /** Filter chain applied to every request, tells whether to process the request or not */
    private final RequestFilter globalFilter = new AlwaysTrueFilter();

    /**
     * Constructor
     *
     */
    public TopologyConfig() {
    }

    public Collection<Subnet> getSubnetCollection() {
        return subnetsByCidr.values();
    }

    public Subnet findSubnetByCidr(InetCidr cidr) {
        return subnetsByCidr.get(cidr);
    }

    public Subnet findSubnetByGiaddr(InetAddress giaddr) {
        return subnetsByGiaddr.get(giaddr);
    }

    public void registerSubnet(Subnet subnet) throws ConfigException {
        if (subnet == null) {
            throw new NullPointerException("subnet is null");
        }
        subnetsByCidr.put(subnet.getCidr(), subnet);
        int mask = subnet.getCidr().getMask();
        if (mask < lowestMask) {
            lowestMask = mask;
        }
        if (mask > highestMask) {
            highestMask = mask;
        }
        for (InetAddress giaddr : subnet.getGiaddrs()) {
            if (subnetsByGiaddr.containsKey(giaddr)) {
                throw new ConfigException("giaddr: " + giaddr.getHostName() + " already present in subnet " + subnetsByGiaddr.get(giaddr).getCidr());
            }
            subnetsByGiaddr.put(giaddr, subnet);
        }
    }

    public Subnet findSubnetFromRequestGiaddr(InetAddress giaddr) {
        Subnet foundSubnet = null;
        if (giaddr == null) {
            return null;
        }
        if (!(giaddr instanceof Inet4Address)) {
            throw new IllegalArgumentException("giaddr must be IPv4");
        }
        if (DHCPConstants.INADDR_ANY.equals(giaddr)) {
        } else {
            foundSubnet = findSubnetByGiaddr(giaddr);
            if (foundSubnet == null) {
                for (int mask = getHighestMask(); mask >= getLowestMask(); mask--) {
                    InetCidr iterCidr = new InetCidr(giaddr, mask);
                    foundSubnet = findSubnetByCidr(iterCidr);
                    if (foundSubnet != null) {
                        break;
                    }
                }
            }
        }
        return foundSubnet;
    }

    /**
	 * @return Returns the highestMask.
	 */
    public int getHighestMask() {
        return highestMask;
    }

    /**
	 * @return Returns the lowestMask.
	 */
    public int getLowestMask() {
        return lowestMask;
    }

    public RequestFilter getGlobalFilter() {
        return globalFilter;
    }

    /**
	 * @return Returns the rootNode.
	 */
    public Node getRootNode() {
        return rootNode;
    }

    /**
	 * @param rootNode The rootNode to set.
	 */
    public void setRootNode(Node rootNode) {
        this.rootNode = rootNode;
    }
}
