package org.opennms.protocols.snmp;

import java.lang.RuntimeException;

/**
 * Defines a runtime exception when the program attempts to send
 * a SnmpPduPacket and there is no default handler defined. This
 * is considered a runtime exception since if there isn't a handler
 * registered yet, is there likely to be one after the exception?
 *
 * @version	$Revision: 1.7 $
 *
 */
public class SnmpHandlerNotDefinedException extends RuntimeException {

    /**
	 * The exception constructor
	 *
	 * @param why The reason the exception is being raised
	 *
	 */
    public SnmpHandlerNotDefinedException(String why) {
        super(why);
    }

    /**
	 * Default exception constructor
	 *
	 */
    public SnmpHandlerNotDefinedException() {
        super();
    }
}
