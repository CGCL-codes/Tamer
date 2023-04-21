package org.opennms.protocols.snmp;

import java.lang.Exception;

/**
 * This class is thrown by the SNMP classes when an 
 * encoding exception occurs at the SNMP level and not
 * via the AsnEncoder class.
 *
 * @see org.opennms.protocols.snmp.asn1.AsnEncoder
 * @version $Revision: 1.7 $
 */
public class SnmpPduEncodingException extends Exception {

    /**
	 * The default exception constructor
	 *
	 */
    public SnmpPduEncodingException() {
        super();
    }

    /**
	 * The exception constructor
	 *
	 * @param why The reason the exception is being raised
	 *
	 */
    public SnmpPduEncodingException(String why) {
        super(why);
    }
}
