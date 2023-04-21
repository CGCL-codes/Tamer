package org.openrtk.idl.epp0503.host;

/**
 * Class defining constant instances of IP address types for host.</p>
 * Used in conjunction with the epp_HostAddress class to indicate the
 * type of the host IP address: IPV4, IPV6.</p>
 * $Header: /cvsroot/epp-rtk/epp-rtk/java/src/org/openrtk/idl/epp0503/host/epp_HostAddressType.java,v 1.1 2003/03/21 16:18:30 tubadanm Exp $<br>
 * $Revision: 1.1 $<br>
 * $Date: 2003/03/21 16:18:30 $<br>
 * @see org.openrtk.idl.epp0503.host.epp_HostAddress
 */
public class epp_HostAddressType implements org.omg.CORBA.portable.IDLEntity {

    private int __value;

    private static int __size = 2;

    private static org.openrtk.idl.epp0503.host.epp_HostAddressType[] __array = new org.openrtk.idl.epp0503.host.epp_HostAddressType[__size];

    private static String[] __strings = { "v4", "v6" };

    /**
   * Integer value representing the IPV4 address type.
   * @see #IPV4
   */
    public static final int _IPV4 = 0;

    /**
   * Instance of epp_HostAddressType representing the IPV4 address type.
   * Used directly with epp_HostAddress.
   * @see org.openrtk.idl.epp0503.host.epp_HostAddress
   */
    public static final org.openrtk.idl.epp0503.host.epp_HostAddressType IPV4 = new org.openrtk.idl.epp0503.host.epp_HostAddressType(_IPV4);

    /**
   * Integer value representing the IPV6 address type.
   * @see #IPV6
   */
    public static final int _IPV6 = 1;

    /**
   * Instance of epp_HostAddressType representing the IPV6 address type.
   * Used directly with epp_HostAddress.
   * @see org.openrtk.idl.epp0503.host.epp_HostAddress
   */
    public static final org.openrtk.idl.epp0503.host.epp_HostAddressType IPV6 = new org.openrtk.idl.epp0503.host.epp_HostAddressType(_IPV6);

    /**
   * Accessor method for the internal integer representing the type of address.
   * @return The integer value of this host address type
   */
    public int value() {
        return __value;
    }

    /**
   * Transform an integer into a epp_HostAddressType constant.
   * Given the integer representation of the address type, returns
   * one of the address type constants.
   * @param value The integer value for the desired address type
   */
    public static org.openrtk.idl.epp0503.host.epp_HostAddressType from_int(int value) {
        if (value >= 0 && value < __size) return __array[value]; else throw new org.omg.CORBA.BAD_PARAM();
    }

    /**
   * For internal use only.
   * Initializes the internal address type array.
   * @param value The integer value for the desired address type
   */
    protected epp_HostAddressType(int value) {
        __value = value;
        __array[__value] = this;
    }

    public String toString() {
        return __strings[this.value()];
    }
}
