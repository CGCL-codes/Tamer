package net.assimilator.entry;

import net.jini.entry.AbstractEntry;
import net.jini.lookup.entry.ServiceControlled;
import java.util.HashMap;
import java.util.Map;

/**
 * The ComputeResourceUtilization provides a mechanism to represent the
 * utilization of a {@link net.assimilator.qos.ComputeResource}. ComputeResource
 * quantitative behavior is represented by
 * {@link net.assimilator.core.MeasuredResource} objects. Each MeasuredResource
 * contains a relative value which represents the percentage of the resource being
 * measured. This value is the relative utilization of the resource's usage. The
 * summation of a compute resource's utilization is represented by the
 * utilization property.
 *
 * @author unknown
 * @version $Id: ComputeResourceUtilization.java 181 2007-07-30 05:22:34Z khartig $
 */
public class ComputeResourceUtilization extends AbstractEntry implements Comparable, ServiceControlled {

    private static final long serialVersionUID = 6659110753600125480L;

    /**
     * Description of the ComputeResource
     */
    public String description;

    /**
     * The host name of the compute resource
     */
    public String hostName;

    /**
     * The IP Address of the compute resource
     */
    public String address;

    /**
     * The utilization of the compute resource, which is a summation of
     * {@link net.assimilator.core.MeasuredResource} components, representing a
     * snapshot of the depletion-oriented resources of the compute resource
     */
    public Double utilization;

    /**
     * Map of measurable capability utilization values
     */
    public Map utilizationMap;

    /**
     * Construct a ComputeResourceUtilization
     */
    public ComputeResourceUtilization() {
    }

    /**
     * Construct a ComputeResourceUtilization
     *
     * @param description A description for the resource
     * @param hostName    The host name of the ComputeResource
     * @param address     IP address of the ComputeResource
     * @param utilization Composite utilization of the ComputeResource
     * @param uMap        Map of name, value pairs corresponding to whats being
     *                    measured on the compute resource
     */
    public ComputeResourceUtilization(String description, String hostName, String address, Double utilization, Map uMap) {
        if (description == null) throw new NullPointerException("description is null");
        if (hostName == null) throw new NullPointerException("hostName is null");
        if (address == null) throw new NullPointerException("address is null");
        if (utilization == null) throw new NullPointerException("utilization is null");
        if (uMap == null) throw new NullPointerException("utilizationMap is null");
        this.description = description;
        this.hostName = hostName;
        this.address = address;
        this.utilization = utilization;
        this.utilizationMap = new HashMap(uMap);
    }

    /**
     * Compares this ResourceCapability object with another
     * ComputeResourceUtilization object for order using the computed
     * utilization of the ComputeResourceUtilization
     *
     * @param o Object to compare to
     */
    public int compareTo(Object o) {
        ComputeResourceUtilization that = (ComputeResourceUtilization) o;
        return (this.utilization.compareTo(that.utilization));
    }
}
