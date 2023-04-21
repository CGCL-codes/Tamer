package org.openrtk.idl.epp0705;

/**
 * Class defining constant instances of authorization information types
 * generic for all objects.</p>
 * Used in conjunction with the epp_AuthInfo class to indicate the object
 * authorization information.</p>
 * Currently, only one type is permitted: PW (password).</p>
 * $Header: /cvsroot/epp-rtk/epp-rtk/java/src/org/openrtk/idl/epp0705/epp_AuthInfoType.java,v 1.1 2003/03/20 22:42:17 tubadanm Exp $<br>
 * $Revision: 1.1 $<br>
 * $Date: 2003/03/20 22:42:17 $<br>
 * @see org.openrtk.idl.epp0705.epp_AuthInfo
 */
public class epp_AuthInfoType implements org.omg.CORBA.portable.IDLEntity {

    private int __value;

    private static int __size = 2;

    private static org.openrtk.idl.epp0705.epp_AuthInfoType[] __array = new org.openrtk.idl.epp0705.epp_AuthInfoType[__size];

    private static String[] __strings = { "pw", "ext" };

    /**
   * Integer value representing the PW authorization information type.
   * @see #PW
   */
    public static final int _PW = 0;

    /**
   * Instance of epp_AuthInfoType representing the PW authorization information type.
   * Used directly with epp_AuthInfo.
   * @see org.openrtk.idl.epp0705.epp_AuthInfo
   */
    public static final org.openrtk.idl.epp0705.epp_AuthInfoType PW = new org.openrtk.idl.epp0705.epp_AuthInfoType(_PW);

    /**
   * Integer value representing the EXT authorization information type.
   * @see #EXT
   */
    public static final int _EXT = 1;

    /**
   * Instance of epp_AuthInfoType representing the EXT authorization information type.
   * Used directly with epp_AuthInfo.
   * @see org.openrtk.idl.epp0705.epp_AuthInfo
   */
    public static final org.openrtk.idl.epp0705.epp_AuthInfoType EXT = new org.openrtk.idl.epp0705.epp_AuthInfoType(_EXT);

    /**
   * Accessor method for the internal integer representing the type of authorization information.
   * @return The integer value of this authorization information type
   */
    public int value() {
        return __value;
    }

    /**
   * Transform an integer into a epp_AuthInfoType constant.
   * Given the integer representation of the authorization information type,
   * returns one of the authorization information type constants.
   * @param value The integer value for the desired authorization information type
   */
    public static org.openrtk.idl.epp0705.epp_AuthInfoType from_int(int value) {
        if (value >= 0 && value < __size) return __array[value]; else throw new org.omg.CORBA.BAD_PARAM();
    }

    /**
   * For internal use only.
   * Initializes the internal authorization information type array.
   * @param value The integer value for the desired authorization information type
   */
    protected epp_AuthInfoType(int value) {
        __value = value;
        __array[__value] = this;
    }

    public String toString() {
        return __strings[this.value()];
    }
}
