package org.jjsip.sip.header;

import org.jjsip.tools.Parser;

/**
 * SIP Header Call-ID. The Call-ID header field acts as a unique identifier to
 * group together a series of messages. It MUST be the same for all requests and
 * responses sent by either UA in a dialog. It SHOULD be the same in each
 * registration from a UA. <BR>
 * In a new request created by a UAC outside of any dialog, the Call-ID header
 * field MUST be selected by the UAC as a globally unique identifier over space
 * and time unless overridden by method-specific behavior. <BR>
 * Use of cryptographically random identifiers in the generation of Call-IDs is
 * RECOMMENDED. Implementations MAY use the form "localid@host". Call-IDs are
 * case-sensitive and are simply compared byte-by-byte.
 */
public class CallIdHeader extends Header {

    /** Creates a CallIdHeader with value <i>hvalue</i> */
    public CallIdHeader(String hvalue) {
        super(SipHeaderType.CALL_ID, hvalue);
    }

    /** Creates a new CallIdHeader equal to CallIdHeader <i>hd</i> */
    public CallIdHeader(Header hd) {
        super(hd);
    }

    /** Gets Call-Id of CallIdHeader */
    public String getCallId() {
        return (new Parser(value)).getString();
    }

    /** Sets Call-Id of CallIdHeader */
    public void setCallId(String callId) {
        value = callId;
    }
}
