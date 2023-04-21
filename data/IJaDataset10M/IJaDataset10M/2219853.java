package org.apache.directory.server.dhcp.options.dhcp;

import org.apache.directory.server.dhcp.options.ByteOption;

/**
 * This option is used to indicate that the DHCP 'sname' or 'file' fields are
 * being overloaded by using them to carry DHCP options. A DHCP server inserts
 * this option if the returned parameters will exceed the usual space allotted
 * for options. If this option is present, the client interprets the specified
 * additional fields after it concludes interpretation of the standard option
 * fields. The code for this option is 52, and its length is 1. Legal values for
 * this option are: Value Meaning ----- -------- 1 the 'file' field is used to
 * hold options 2 the 'sname' field is used to hold options 3 both fields are
 * used to hold options
 */
public class OptionOverload extends ByteOption {

    public byte getTag() {
        return 52;
    }
}
