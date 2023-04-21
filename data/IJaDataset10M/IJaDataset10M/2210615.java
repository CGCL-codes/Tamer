package org.dasein.cloud.services.firewall;

import java.util.Collection;
import java.util.Locale;
import org.dasein.cloud.CloudException;
import org.dasein.cloud.InternalException;

/**
 * <p>
 * Operations on whatever concept the underlying cloud uses to regulate network traffic into a
 * server or group of servers.
 * </p>
 * @author George Reese @ enStratus (http://www.enstratus.com)
 */
public interface FirewallServices {

    /**
     * Provides positive authorization for the specified firewall rule. Any call to this method should
     * result in an override of any previous revocations.
     * @param firewallId the unique, cloud-specific ID for the firewall being targeted by the new rule
     * @param cidr the source CIDR (http://en.wikipedia.org/wiki/CIDR) for the allowed traffic
     * @param protocol the protocol (tcp/udp/icmp) supported by this rule
     * @param beginPort the beginning of the port range to be allowed, inclusive
     * @param endPort the end of the port range to be allowed, inclusive
     * @throws CloudException an error occurred with the cloud provider establishing the rule
     * @throws InternalException an error occurred locally trying to establish the rule
     */
    public void authorize(String firewallId, String cidr, Protocol protocol, int beginPort, int endPort) throws CloudException, InternalException;

    /**
     * Creates a new firewall with the specified name.
     * @param name the user-friendly name for the new firewall
     * @param description a description of the purpose of the firewall
     * @return the unique ID for the newly created firewall
     * @throws CloudException an error occurred with the cloud provider while performing the operation
     * @throws InternalException an error occurred locally independent of any events in the cloud
     */
    public String create(String name, String description) throws InternalException, CloudException;

    /**
     * Deletes the specified firewall from the system.
     * @param firewallId the unique ID of the firewall to be deleted
     * @throws InternalException an error occurred locally independent of any events in the cloud
     * @throws CloudException an error occurred with the cloud provider while performing the operation
     */
    public void delete(String firewallId) throws InternalException, CloudException;

    /**
     * Provides the full firewall data for the specified firewall.
     * @param firewallId the unique ID of the desired firewall
     * @return the firewall state for the specified firewall instance
     * @throws InternalException an error occurred locally independent of any events in the cloud
     * @throws CloudException an error occurred with the cloud provider while performing the operation
     */
    public Firewall getFirewall(String firewallId) throws InternalException, CloudException;

    /**
     * Provides the firewall terminology for the concept of a firewall. For example, AWS calls a 
     * firewall a "security group".
     * @param locale the locale for which you should translate the firewall term
     * @return the translated term for firewall with the target cloud provider
     */
    public String getProviderTermForFirewall(Locale locale);

    /**
     * Provides the affirmative rules supported by the named firewall.
     * @param firewallId the unique ID of the firewall being queried
     * @return all rules supported by the target firewall
     * @throws InternalException an error occurred locally independent of any events in the cloud
     * @throws CloudException an error occurred with the cloud provider while performing the operation
     */
    public Collection<FirewallRule> getRules(String firewallId) throws InternalException, CloudException;

    /**
     * Lists all firewalls in the current provider context.
     * @return a list of all firewalls in the current provider context
     * @throws InternalException an error occurred locally independent of any events in the cloud
     * @throws CloudException an error occurred with the cloud provider while performing the operation
     */
    public Collection<Firewall> list() throws InternalException, CloudException;

    /**
     * Revokes the specified access from the named firewall.
     * @param firewallId the firewall from which the rule is being revoked
     * @param cidr the source CIDR (http://en.wikipedia.org/wiki/CIDR) for the rule being removed
     * @param protocol the protocol (tcp/icmp/udp) of the rule being removed
     * @param beginPort the initial port of the rule being removed
     * @param endPort the end port of the rule being removed
     * @throws InternalException an error occurred locally independent of any events in the cloud
     * @throws CloudException an error occurred with the cloud provider while performing the operation
     */
    public void revoke(String firewallId, String cidr, Protocol protocol, int beginPort, int endPort) throws CloudException, InternalException;
}
