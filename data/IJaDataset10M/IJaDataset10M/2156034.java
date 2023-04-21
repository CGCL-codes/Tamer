package org.apache.directory.server.dhcp.options.perinterface;

import org.apache.directory.server.dhcp.options.DhcpOption;

/**
 * This option specifies whether or not the client should solicit routers using
 * the Router Discovery mechanism defined in RFC 1256. A value of 0 indicates
 * that the client should not perform router discovery. A value of 1 means that
 * the client should perform router discovery. The code for this option is 31,
 * and its length is 1.
 */
public class PerformRouterDiscovery extends DhcpOption {

    public byte getTag() {
        return 31;
    }
}
