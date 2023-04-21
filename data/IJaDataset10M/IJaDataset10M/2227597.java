package org.apache.directory.server.dhcp.options.linklayer;

import org.apache.directory.server.dhcp.options.IntOption;

/**
 * This option specifies the timeout in seconds for ARP cache entries. The time
 * is specified as a 32-bit unsigned integer. The code for this option is 35,
 * and its length is 4.
 */
public class ArpCacheTimeout extends IntOption {

    public byte getTag() {
        return 35;
    }
}
