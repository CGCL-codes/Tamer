package avahi4j;

import avahi4j.Avahi4JConstants.Protocol;
import avahi4j.ServiceResolver.ServiceResolverEvent;

/**
 * This class encapsulates the details about a resolved service. It contains:
 * <ul>
 * <li>The interface number on the host where the service is offered</li>
 * <li>The IP protocol version the service uses</li>
 * <li>The name of the service</li>
 * <li>The type of the service</li>
 * <li>The domain where the service can be found</li>
 * <li>The name of the host offering the service</li>
 * <li>An {@link Address} object containing the IP address of the host</li>
 * <li>The port number where the service  is reachable</li>
 * <li>A list of TXT records associated with the service</li>
 * <li>Some lookup result flags (See LOOKUP_RESULT_* in {@link Avahi4JConstants})</li>
 * <li>A {@link ServiceResolverEvent} object, indicating whether the service 
 * could be resolved or not.</li>
 * </ul>
 * <b>Make sure to check the value of {@link #resolverEvent} first. If it is set
 * to {@link ServiceResolverEvent#RESOLVER_FAILURE}, other fields are 
 * meaningless.</b>
 * @author gilles
 *
 */
public class ResolvedService {

    /**
	 * The interface number on the host where the service is offered
	 */
    public int interfaceNum;

    /**
	 * The IP protocol version the service uses
	 */
    public Protocol proto;

    /**
	 * A {@link ServiceResolverEvent} object, indicating whether the service 
	 * could be resolved or not
	 */
    public ServiceResolverEvent resolverEvent;

    /**
	 * The name of the service. <b>When resolving a service, a <code>null</code>
	 * name value indicates that the service is no longer offered.</b> 
	 */
    public String name;

    /**
	 * the type of the service. <b>When resolving a service, a <code>null</code>
	 * type value indicates that the service is no longer offered.</b>
	 */
    public String type;

    /**
	 * The domain where the service can be found. <b>When resolving a service, 
	 * a <code>null</code> domain value indicates that the service is no longer
	 * offered.</b>
	 */
    public String domain;

    /**
	 * The name of the host offering the service
	 */
    public String hostname;

    /**
	 * The IP address of the host offering the service
	 */
    public Address address;

    /**
	 * The port number where the service is reachable
	 */
    public int port;

    /**
	 * TXT records associated with the service
	 */
    public String txtRecords[];

    /**
	 * Some lookup result flags (See LOOKUP_RESULT_* in {@link Avahi4JConstants})
	 */
    public int lookupResultFlag;

    public String toString() {
        if (resolverEvent == ServiceResolverEvent.RESOLVER_FOUND) {
            if (name == null && type == null && hostname == null) {
                return "Service removed";
            } else {
                String string = "Service name: " + name + "\nInterface: " + interfaceNum + "Protocol: " + proto + "\nType: " + type + "\nHostname :" + hostname + "\nDomain: " + domain + "\nAddress: " + address + "\nPort: " + port + "\nLookup result flags: " + Avahi4JConstants.lookupResultToString(lookupResultFlag) + "\nTXT records:";
                for (String s : txtRecords) string += s;
                return string;
            }
        } else {
            return "Unable to resolve name";
        }
    }
}
