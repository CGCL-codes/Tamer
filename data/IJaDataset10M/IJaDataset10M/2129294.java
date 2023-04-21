package org.apache.directory.server.dhcp.options.dhcp;

import org.apache.directory.server.dhcp.options.DhcpOption;

/**
 * This option is used by DHCP clients to specify their unique identifier. DHCP
 * servers use this value to index their database of address bindings. This
 * value is expected to be unique for all clients in an administrative domain.
 * Identifiers SHOULD be treated as opaque objects by DHCP servers. The client
 * identifier MAY consist of type-value pairs similar to the 'htype'/'chaddr'
 * fields. For instance, it MAY consist of a hardware type and hardware address.
 * In this case the type field SHOULD be one of the ARP hardware types defined
 * in STD2. A hardware type of 0 (zero) should be used when the value field
 * contains an identifier other than a hardware address (e.g. a fully qualified
 * domain name). For correct identification of clients, each client's client-
 * identifier MUST be unique among the client-identifiers used on the subnet to
 * which the client is attached. Vendors and system administrators are
 * responsible for choosing client-identifiers that meet this requirement for
 * uniqueness. The code for this option is 61, and its minimum length is 2.
 */
public class ClientIdentifier extends DhcpOption {

    public byte getTag() {
        return 61;
    }
}
