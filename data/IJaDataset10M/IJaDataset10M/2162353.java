package org.openrtk.idl.epp0705;

/**
 * The IDL interface implemented by the EPPClient class.</p>
 * The interface defines the complete list of EPP result codes and some RTK specific
 * result codes in static class variables.</p>
 * And the interface brings together epp_SessionOperations and standard IDL classes.
 * $Header: /cvsroot/epp-rtk/epp-rtk/java/src/org/openrtk/idl/epp0705/epp_Session.java,v 1.3 2003/09/10 21:29:58 tubadanm Exp $<br>
 * $Revision: 1.3 $<br>
 * $Date: 2003/09/10 21:29:58 $<br>
 * @see com.tucows.oxrs.epp0705.rtk.EPPClient
 */
public interface epp_Session extends epp_SessionOperations, org.omg.CORBA.Object, org.omg.CORBA.portable.IDLEntity {

    public static final short EPP_COMMAND_COMPLETED_SUCCESSFULLY = (short) (1000);

    public static final short EPP_COMMAND_COMPLETED_SUCCESSFULLY_ACTION_PENDING = (short) (1001);

    public static final short EPP_COMMAND_COMPLETED_SUCCESSFULLY_NO_MESSAGES = (short) (1300);

    /**
   * Same short value as EPP_COMMAND_COMPLETED_SUCCESSFULLY_ACK_TO_DEQUEUE, but
   * provided for backward compatibility.
   * @deprecated
   */
    public static final short EPP_COMMAND_COMPLETED_SUCCESSFULLY_MESSAGE_PRESENT = (short) (1301);

    public static final short EPP_COMMAND_COMPLETED_SUCCESSFULLY_ACK_TO_DEQUEUE = (short) (1301);

    public static final short EPP_COMMAND_COMPLETED_SUCCESSFULLY_ENDING_SESSION = (short) (1500);

    public static final short EPP_UNKNOWN_COMMAND = (short) (2000);

    public static final short EPP_COMMAND_SYNTAX_ERROR = (short) (2001);

    public static final short EPP_COMMAND_USE_ERROR = (short) (2002);

    public static final short EPP_REQUIRED_PARAMETER_MISSING = (short) (2003);

    public static final short EPP_PARAMETER_VALUE_RANGE_ERROR = (short) (2004);

    public static final short EPP_PARAMETER_VALUE_SYNTAX_ERROR = (short) (2005);

    public static final short EPP_UNIMPLEMENTED_PROTOCOL_VERSION = (short) (2100);

    public static final short EPP_UNIMPLEMENTED_COMMAND = (short) (2101);

    public static final short EPP_UNIMPLEMENTED_OPTION = (short) (2102);

    public static final short EPP_UNIMPLEMENTED_EXTENSION = (short) (2103);

    public static final short EPP_BILLING_FAILURE = (short) (2104);

    public static final short EPP_OBJECT_IS_NOT_ELIGIBLE_FOR_RENEWAL = (short) (2105);

    public static final short EPP_OBJECT_IS_NOT_ELIGIBLE_FOR_TRANSFER = (short) (2106);

    public static final short EPP_AUTHENTICATION_FAILURE = (short) (2200);

    public static final short EPP_AUTHORIZATION_FAILURE = (short) (2201);

    public static final short EPP_INVALID_AUTHORIZATION_IDENTIFIER = (short) (2202);

    public static final short EPP_OBJECT_PENDING_TRANSFER = (short) (2300);

    public static final short EPP_OBJECT_NOT_PENDING_TRANSFER = (short) (2301);

    public static final short EPP_OBJECT_EXISTS = (short) (2302);

    public static final short EPP_OBJECT_DOES_NOT_EXIST = (short) (2303);

    public static final short EPP_OBJECT_STATUS_PROHIBITS_OPERATION = (short) (2304);

    public static final short EPP_OBJECT_ASSOCIATION_PROHIBITS_OPERATION = (short) (2305);

    public static final short EPP_PARAMETER_VALUE_POLICY_ERROR = (short) (2306);

    public static final short EPP_UNIMPLEMENTED_OBJECT_SERVICE = (short) (2307);

    public static final short EPP_DATA_MANAGEMENT_POLICY_VIOLATION = (short) (2308);

    public static final short EPP_COMMAND_FAILED = (short) (2400);

    /**
   * Same short value as EPP_COMMAND_FAILED_SERVER_CLOSING_CONNECTION, but
   * provided for backward compatibility.
   * @deprecated
   */
    public static final short EPP_COMMAND_FAILED_SERVER_ENDING_SESSION = (short) (2500);

    public static final short EPP_COMMAND_FAILED_SERVER_CLOSING_CONNECTION = (short) (2500);

    /**
   * Same short value as EPP_AUTHENTICATION_ERROR_SERVER_CLOSING_CONNECTION, but
   * provided for backward compatibility.
   * @deprecated
   */
    public static final short EPP_TIMEOUT_SERVER_ENDING_SESSION = (short) (2501);

    public static final short EPP_AUTHENTICATION_ERROR_SERVER_CLOSING_CONNECTION = (short) (2501);

    public static final short EPP_SESSION_LIMIT_EXCEEDED_SERVER_CLOSING_CONNECTION = (short) (2502);

    public static final short RTK_COMMUNICATIONS_FAILURE = (short) (2600);

    public static final short RTK_UNEXPECTED_SERVER_DISCONNECT = (short) (2601);
}
