package org.openrtk.idl.epp0402.contact;

/**
 * Class defining constant instances of status types for contact.</p>
 * Used in conjunction with the epp_ContactStatus class to indicate the contact status.</p>
 * $Header: /cvsroot/epp-rtk/epp-rtk/java/src/org/openrtk/idl/epp0402/contact/epp_ContactStatusType.java,v 1.1 2003/03/21 16:35:40 tubadanm Exp $<br>
 * $Revision: 1.1 $<br>
 * $Date: 2003/03/21 16:35:40 $<br>
 * @see org.openrtk.idl.epp0402.contact.epp_ContactStatus
 */
public class epp_ContactStatusType implements org.omg.CORBA.portable.IDLEntity {

    private int __value;

    private static int __size = 10;

    private static org.openrtk.idl.epp0402.contact.epp_ContactStatusType[] __array = new org.openrtk.idl.epp0402.contact.epp_ContactStatusType[__size];

    private static String[] __strings = { "clientDeleteProhibited", "serverDeleteProhibited", "clientTransferProhibited", "serverTransferProhibited", "clientUpdateProhibited", "serverUpdateProhibited", "linked", "ok", "pendingDelete", "pendingTransfer" };

    /**
   * Integer value representing the CLIENT_DELETE_PROHIBITED status type.
   * @see #CLIENT_DELETE_PROHIBITED
   */
    public static final int _CLIENT_DELETE_PROHIBITED = 0;

    /**
   * Instance of epp_ContactStatusType representing the CLIENT_DELETE_PROHIBITED status type.
   * Used directly with epp_ContactStatus.
   * @see org.openrtk.idl.epp0402.contact.epp_ContactStatus
   */
    public static final org.openrtk.idl.epp0402.contact.epp_ContactStatusType CLIENT_DELETE_PROHIBITED = new org.openrtk.idl.epp0402.contact.epp_ContactStatusType(_CLIENT_DELETE_PROHIBITED);

    /**
   * Integer value representing the SERVER_DELETE_PROHIBITED status type.
   * @see #SERVER_DELETE_PROHIBITED
   */
    public static final int _SERVER_DELETE_PROHIBITED = 1;

    /**
   * Instance of epp_ContactStatusType representing the SERVER_DELETE_PROHIBITED status type.
   * Used directly with epp_ContactStatus.
   * @see org.openrtk.idl.epp0402.contact.epp_ContactStatus
   */
    public static final org.openrtk.idl.epp0402.contact.epp_ContactStatusType SERVER_DELETE_PROHIBITED = new org.openrtk.idl.epp0402.contact.epp_ContactStatusType(_SERVER_DELETE_PROHIBITED);

    /**
   * Integer value representing the CLIENT_TRANSFER_PROHIBITED status type.
   * @see #CLIENT_TRANSFER_PROHIBITED
   */
    public static final int _CLIENT_TRANSFER_PROHIBITED = 2;

    /**
   * Instance of epp_ContactStatusType representing the CLIENT_TRANSFER_PROHIBITED status type.
   * Used directly with epp_ContactStatus.
   * @see org.openrtk.idl.epp0402.contact.epp_ContactStatus
   */
    public static final org.openrtk.idl.epp0402.contact.epp_ContactStatusType CLIENT_TRANSFER_PROHIBITED = new org.openrtk.idl.epp0402.contact.epp_ContactStatusType(_CLIENT_TRANSFER_PROHIBITED);

    /**
   * Integer value representing the SERVER_TRANSFER_PROHIBITED status type.
   * @see #SERVER_TRANSFER_PROHIBITED
   */
    public static final int _SERVER_TRANSFER_PROHIBITED = 3;

    /**
   * Instance of epp_ContactStatusType representing the SERVER_TRANSFER_PROHIBITED status type.
   * Used directly with epp_ContactStatus.
   * @see org.openrtk.idl.epp0402.contact.epp_ContactStatus
   */
    public static final org.openrtk.idl.epp0402.contact.epp_ContactStatusType SERVER_TRANSFER_PROHIBITED = new org.openrtk.idl.epp0402.contact.epp_ContactStatusType(_SERVER_TRANSFER_PROHIBITED);

    /**
   * Integer value representing the CLIENT_UPDATE_PROHIBITED status type.
   * @see #CLIENT_UPDATE_PROHIBITED
   */
    public static final int _CLIENT_UPDATE_PROHIBITED = 4;

    /**
   * Instance of epp_ContactStatusType representing the CLIENT_UPDATE_PROHIBITED status type.
   * Used directly with epp_ContactStatus.
   * @see org.openrtk.idl.epp0402.contact.epp_ContactStatus
   */
    public static final org.openrtk.idl.epp0402.contact.epp_ContactStatusType CLIENT_UPDATE_PROHIBITED = new org.openrtk.idl.epp0402.contact.epp_ContactStatusType(_CLIENT_UPDATE_PROHIBITED);

    /**
   * Integer value representing the SERVER_UPDATE_PROHIBITED status type.
   * @see #SERVER_UPDATE_PROHIBITED
   */
    public static final int _SERVER_UPDATE_PROHIBITED = 5;

    /**
   * Instance of epp_ContactStatusType representing the SERVER_UPDATE_PROHIBITED status type.
   * Used directly with epp_ContactStatus.
   * @see org.openrtk.idl.epp0402.contact.epp_ContactStatus
   */
    public static final org.openrtk.idl.epp0402.contact.epp_ContactStatusType SERVER_UPDATE_PROHIBITED = new org.openrtk.idl.epp0402.contact.epp_ContactStatusType(_SERVER_UPDATE_PROHIBITED);

    /**
   * Integer value representing the LINKED status type.
   * @see #LINKED
   */
    public static final int _LINKED = 6;

    /**
   * Instance of epp_ContactStatusType representing the LINKED status type.
   * Used directly with epp_ContactStatus.
   * @see org.openrtk.idl.epp0402.contact.epp_ContactStatus
   */
    public static final org.openrtk.idl.epp0402.contact.epp_ContactStatusType LINKED = new org.openrtk.idl.epp0402.contact.epp_ContactStatusType(_LINKED);

    /**
   * Integer value representing the OK status type.
   * @see #OK
   */
    public static final int _OK = 7;

    /**
   * Instance of epp_ContactStatusType representing the OK status type.
   * Used directly with epp_ContactStatus.
   * @see org.openrtk.idl.epp0402.contact.epp_ContactStatus
   */
    public static final org.openrtk.idl.epp0402.contact.epp_ContactStatusType OK = new org.openrtk.idl.epp0402.contact.epp_ContactStatusType(_OK);

    /**
   * Integer value representing the PENDING_DELETE status type.
   * @see #PENDING_DELETE
   */
    public static final int _PENDING_DELETE = 8;

    /**
   * Instance of epp_ContactStatusType representing the PENDING_DELETE status type.
   * Used directly with epp_ContactStatus.
   * @see org.openrtk.idl.epp0402.contact.epp_ContactStatus
   */
    public static final org.openrtk.idl.epp0402.contact.epp_ContactStatusType PENDING_DELETE = new org.openrtk.idl.epp0402.contact.epp_ContactStatusType(_PENDING_DELETE);

    /**
   * Integer value representing the PENDING_TRANSFER status type.
   * @see #PENDING_TRANSFER
   */
    public static final int _PENDING_TRANSFER = 9;

    /**
   * Instance of epp_ContactStatusType representing the PENDING_TRANSFER status type.
   * Used directly with epp_ContactStatus.
   * @see org.openrtk.idl.epp0402.contact.epp_ContactStatus
   */
    public static final org.openrtk.idl.epp0402.contact.epp_ContactStatusType PENDING_TRANSFER = new org.openrtk.idl.epp0402.contact.epp_ContactStatusType(_PENDING_TRANSFER);

    /**
   * Accessor method for the internal integer representing the type of status.
   * @return The integer value of this contact status type
   */
    public int value() {
        return __value;
    }

    /**
   * Transform an integer into a epp_ContactStatusType constant.
   * Given the integer representation of the status type, returns
   * one of the status type constants.
   * @param value The integer value for the desired status type
   */
    public static org.openrtk.idl.epp0402.contact.epp_ContactStatusType from_int(int value) {
        if (value >= 0 && value < __size) return __array[value]; else throw new org.omg.CORBA.BAD_PARAM();
    }

    /**
   * For internal use only.
   * Initializes the internal status type array.
   * @param value The integer value for the desired status type
   */
    protected epp_ContactStatusType(int value) {
        __value = value;
        __array[__value] = this;
    }

    public String toString() {
        return __strings[this.value()];
    }
}
