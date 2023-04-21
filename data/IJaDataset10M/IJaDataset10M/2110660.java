package net.java.slee.resources.smpp;

import net.java.slee.resources.smpp.SmppSession;

/**
 * This interface represents a generic transaction interface defining the methods common between client and server
 * transactions.
 * 
 * @author amit bhayani
 */
public interface SmppTransaction {

    /**
	 * No Error. Specified in a response PDU to indicate the success of the corresponding request PDU.
	 */
    public static final int ESME_ROK = 0;

    /**
	 * Message Length is invalid. short_message field or message_payload TLV has an invalid length (usually too long for
	 * the given MC or underlying network technology).
	 */
    public static final int ESME_RINVMSGLEN = 0x00000001;

    /**
	 * Command Length is invalid. PDU length is considered invalid, either because the value is too short or too large
	 * for the given PDU.
	 */
    public static final int ESME_RINVCMDLEN = 0x00000002;

    /**
	 * Invalid Command ID. Command ID is not recognised, either because the operation is not supported or unknown.
	 */
    public static final int ESME_RINVCMDID = 0x00000003;

    /**
	 * Incorrect BIND Status for given command. PDU has been sent in the wrong session state. E.g. sending a submit_sm
	 * without first establishing a Bound_TX session state.
	 */
    public static final int ESME_RINVBNDSTS = 0x00000004;

    /**
	 * ESME Already in Bound State. A bind request has been issued within a session that is already bound.
	 */
    public static final int ESME_RALYBND = 0x00000005;

    /**
	 * Invalid Priority Flag. Priority flag contains an illegal or unsupported value.
	 */
    public static final int ESME_RINVPRTFLG = 0x00000006;

    /**
	 * Invalid Registered Delivery Flag. Registered field contains an invalid setting.
	 */
    public static final int ESME_RINVREGDLVFLG = 00000007;

    /**
	 * MC system error indicating that all or part of the MC is currently unavailable. This can be returned in any
	 * response PDU.
	 */
    public static final int ESME_RSYSERR = 0x00000008;

    /**
	 * Invalid Source Address. Source address of message is considered invalid. Usually this is because the field is
	 * either too long or contains invalid characters.
	 */
    public static final int ESME_RINVSRCADR = 0x0000000A;

    /**
	 * Invalid Destination Address. Destination address of message is considered invalid. Usually this is because the
	 * field is either zero length, too long or contains invalid characters.
	 */
    public static final int ESME_RINVDSTADR = 0x0000000B;

    /**
	 * Message ID is invalid. Message ID specified in cancel_sm, query_sm or other operations is invalid.
	 */
    public static final int ESME_RINVMSGID = 0x0000000C;

    /**
	 * Bind Filed. A generic failure scenario for a bind attempt. This may be due to a provisioning error, incorrect
	 * password or other reason. A MC will typically return this error for an invalid system_id, system_type, password
	 * or other attribute that may cause a bind failure.
	 */
    public static final int ESME_RBINDFAIL = 0x0000000D;

    /**
	 * Invalid Password. Password field in bind PDU is invalid. This is usually returned when the length is too short or
	 * too long. It is not supposed to be returned when the ESME has specified the incorrect password.
	 */
    public static final int ESME_RINVPASWD = 0x0000000E;

    /**
	 * Invalid System ID. The System ID field in bind PDU is invalid. This is usually returned when the length is too
	 * short or too long. It is not supposed to be returned when the ESME has specified the incorrect system id.
	 */
    public static final int ESME_RINVSYSID = 0x0000000F;

    /**
	 * Cancel SM Failed. Generic failure error for cancel_sm operation.
	 */
    public static final int ESME_RCANCELFAIL = 0x00000011;

    /**
	 * Replace SM Failed. Generic failure for replace_sm operation.
	 */
    public static final int ESME_RREPLACEFAIL = 0x00000013;

    /**
	 * Message Queue Full. Used to indicate a resource error within the MC. This may be interpreted as the maximum
	 * number of messages addressed to a single destination or a global maximum of undelivered messages within the MC.
	 */
    public static final int ESME_RMSGQFUL = 0x00000014;

    /**
	 * Invalid Service Type. Service type is rejected either because it is not recognised by the MC or because its
	 * length is not within the defined range.
	 */
    public static final int ESME_RINVSERTYP = 0x00000015;

    /**
	 * Invalid number of destinations. The number_of_dests field in the submit_multi PDU is invalid.
	 */
    public static final int ESME_RINVNUMDESTS = 0x00000033;

    /**
	 * Invalid Distribution List name. The dl_name field specified in the submit_multi PDU is either invalid, or
	 * non-existent.
	 */
    public static final int ESME_RINVDLNAME = 0x00000034;

    /**
	 * Destination flag is invalid (submit_multi). The dest_flag field in the submit_multi PDU has been encoded with an
	 * invalid setting.
	 */
    public static final int ESME_RINVDESTFLAG = 0x00000040;

    /**
	 * Submit w/replace functionality has been requested where it is either unsupported or inappropriate for the
	 * particular MC. This can typically occur with submit_multi where the context of “replace if present” is often a
	 * best effort operation and MCs may not support the feature in submit_multi. Another reason for returning this
	 * error would be where the feature has been denied to an ESME.
	 */
    public static final int ESME_RINVSUBREP = 0x00000042;

    /**
	 * Invalid esm_class field data. The esm_class field has an unsupported setting.
	 */
    public static final int ESME_RINVESMCLASS = 0x00000043;

    /**
	 * Cannot Submit to Distribution List. Distribution lists are not supported, are denied or unavailable.
	 */
    public static final int ESME_RCNTSUBDL = 0x00000044;

    /**
	 * submit_sm, data_sm or submit_multi failed. Generic failure error for submission operations.
	 */
    public static final int ESME_RSUBMITFAIL = 0x00000045;

    /**
	 * Invalid Source address TON. The source TON of the message is either invalid or unsupported.
	 */
    public static final int ESME_RINVSRCTON = 0x00000048;

    /**
	 * Invalid Source address NPI. The source NPI of the message is either invalid or unsupported.
	 */
    public static final int ESME_RINVSRCNPI = 0x00000049;

    /**
	 * Invalid Destination address TON. The destination TON of the message is either invalid or unsupported.
	 */
    public static final int ESME_RINVDSTTON = 0x00000050;

    /**
	 * Invalid Destination address NPI. The destination NPI of the message is either invalid or unsupported.
	 */
    public static final int ESME_RINVDSTNPI = 0x00000051;

    /**
	 * Invalid system_type field. The System type of bind PDU has an incorrect length or contains illegal characters.
	 */
    public static final int ESME_RINVSYSTYP = 0x00000053;

    /**
	 * Invalid replace_if_present flag. The replace_if_present flag has been encoded with an invalid or unsupported
	 * setting.
	 */
    public static final int ESME_RINVREPFLAG = 0x00000054;

    /**
	 * Invalid number of messages.
	 */
    public static final int ESME_RINVNUMMSGS = 0x00000055;

    /**
	 * Throttling error (ESME has exceeded allowed message limits). This type of error is usually returned where an ESME
	 * has exceeded a predefined messaging rate restriction applied by the operator.
	 */
    public static final int ESME_RTHROTTLED = 0x00000058;

    /**
	 * Invalid Scheduled Delivery Time. Scheduled delivery time is either the incorrect length or is invalid.
	 */
    public static final int ESME_RINVSCHED = 0x00000061;

    /**
	 * Invalid message validity period (Expiry time). Expiry time is either the incorrect length or is invalid.
	 */
    public static final int ESME_RINVEXPIRY = 0x00000062;

    /**
	 * Predefined Message ID is Invalid or specified predefined message was not found. The default (pre-defined) message
	 * id is either invalid or refers to a non-existent pre-defined message.
	 */
    public static final int ESME_RINVDFTMSGID = 0x00000063;

    /**
	 * ESME Receiver Temporary App Error Code. Rx or Trx ESME is unable to process a delivery due to a temporary problem
	 * and is requesting that the message be retried at some future point.
	 */
    public static final int ESME_RX_T_APPN = 0x00000064;

    /**
	 * ESME Receiver Permanent App Error Code. Rx or Trx ESME is unable to process a delivery due to a permanent problem
	 * relating to the given destination address and is requesting that the message and all other messages queued to the
	 * same destination should NOT be retried any further.
	 */
    public static final int ESME_RX_P_APPN = 0x00000065;

    /**
	 * ESME Receiver Reject Message Error Code. Rx or Trx ESME is unable to process a delivery due to a problem relating
	 * to the given message and is requesting that the message is rejected and not retried. This does not affect other
	 * messages queued for the same ESME or destination address.
	 */
    public static final int ESME_RX_R_APPN = 0x00000066;

    /**
	 * query_sm request failed. Generic failure scenario for a query request.
	 */
    public static final int ESME_RQUERYFAIL = 0x00000067;

    /**
	 * Error in the optional part of the PDU Body. Decoding of TLVs (Optional Parameters) has resulted in one of the
	 * following scenarios: i) PDU decoding completed with 1-3 octets of data remaining, indicating a corrupt PDU. ii) A
	 * TLV indicated a length that was not present in the remaining PDU data (e.g. a TLV specifying a length of 10 where
	 * only 6 octets of PDU data remain).
	 */
    public static final int ESME_RINVTLVSTREAM = 0x000000C0;

    /**
	 * TLV not allowed. A TLV has been used in an invalid context, either inappropriate or deliberately rejected by the
	 * operator.
	 */
    public static final int ESME_RTLVNOTALLWD = 0x000000C1;

    /**
	 * Invalid Parameter Length. A TLV has specified a length that is considered invalid.
	 */
    public static final int ESME_RINVTLVLEN = 0x000000C2;

    /**
	 * Expected TLV missing. A mandatory TLV such as the message_payload TLV within a data_sm PDU is missing.
	 */
    public static final int ESME_RMISSINGTLV = 0x000000C3;

    /**
	 * Invalid TLV Value. The data content of a TLV is invalid and cannot be decoded.
	 */
    public static final int ESME_RINVTLVVAL = 0x000000C4;

    /**
	 * Transaction Delivery Failure. A data_sm or submit_sm operation issued in transaction mode has resulted in a
	 * failed delivery.
	 */
    public static final int ESME_RDELIVERYFAILURE = 0x000000FE;

    /**
	 * Unknown Error.Some unexpected error has occurred.
	 */
    public static final int ESME_RUNKNOWNERR = 0x000000FF;

    /**
	 * ESME Not authorised to use specified service_type. Specific service_type has been denied for use by the given
	 * ESME.
	 */
    public static final int ESME_RSERTYPUNAUTH = 0x00000100;

    /**
	 * ESME Prohibited from using specified operation. The PDU request was recognised but is denied to the ESME.
	 */
    public static final int ESME_RPROHIBITED = 0x00000101;

    /**
	 * Specified service_type is unavailable. Due to a service outage within the SMSC, a service is unavailable.
	 */
    public static final int ESME_RSERTYPUNAVAIL = 0x00000102;

    /**
	 * Specified service_type is denied. Due to inappropriate message content wrt. the selected service_type.
	 */
    public static final int ESME_RSERTYPDENIED = 0x00000103;

    /**
	 * Invalid Data Coding Scheme. Specified DCS is invalid or SMSC does not support it.
	 */
    public static final int ESME_RINVDCS = 0x00000104;

    /**
	 * Source Address Sub unit is Invalid.
	 */
    public static final int ESME_RINVSRCADDRSUBUNIT = 0x00000105;

    /**
	 * Address Sub unit is Invalid.
	 */
    public static final int ESME_RINVDSTADDRSUBUNIT = 0x00000106;

    /**
	 * Broadcast Frequency Interval is invalid. Specified value is either invalid or not supported.
	 */
    public static final int ESME_RINVBCASTFREQINT = 0x00000107;

    /**
	 * Broadcast Alias Name is invalid. Specified value has an incorrect length or contains invalid/unsupported
	 * characters.
	 */
    public static final int ESME_RINVBCASTALIAS_NAME = 0x00000108;

    /**
	 * Broadcast Area Format is invalid. Specified value violates protocol or is unsupported.
	 */
    public static final int ESME_RINVBCASTAREAFMT = 0x00000109;

    /**
	 * Number of Broadcast Areas is invalid. Specified value violates protocol or is unsupported.
	 */
    public static final int ESME_RINVNUMBCAST_AREAS = 0x0000010A;

    /**
	 * Broadcast Content Type is invalid. Specified value violates protocol or is unsupported.
	 */
    public static final int ESME_RINVBCASTCNTTYPE = 0x0000010B;

    /**
	 * Broadcast Message Class is invalid. Specified value violates protocol or is unsupported.
	 */
    public static final int ESME_RINVBCASTMSGCLASS = 0x0000010C;

    /**
	 * broadcast_sm operation failed.
	 */
    public static final int ESME_RBCASTFAIL = 0x0000010D;

    /**
	 * query_broadcast_sm operation failed.
	 */
    public static final int ESME_RBCASTQUERYFAIL = 0x0000010E;

    /**
	 * cancel_broadcast_sm operation failed
	 */
    public static final int ESME_RBCASTCANCELFAIL = 0x0000010F;

    /**
	 * Number of Repeated Broadcasts is invalid. Specified value violates protocol or is unsupported.
	 */
    public static final int ESME_RINVBCAST_REP = 0x00000110;

    /**
	 * Broadcast Service Group is invalid. Specified value violates protocol or is unsupported.
	 */
    public static final int ESME_RINVBCASTSRVGRP = 0x00000111;

    /**
	 * Broadcast Channel Indicator is invalid. Specified value violates protocol or is unsupported.
	 */
    public static final int ESME_RINVBCASTCHANIND = 0x00000112;

    /**
	 * Returns a unique identifier that identifies this transaction.
	 * 
	 * @returns the unique identifier of this transaction.
	 */
    public long getId();

    public SmppSession getSmppSession();
}
