package org.bulatnig.smpp.util;

import org.bulatnig.smpp.SMPPException;

/**
 * Comment here.
 * <p/>
 * User: Bulat Nigmatullin
 * Date: Nov 12, 2008
 * Time: 9:26:06 AM
 */
public class SMPPByteBufferException extends SMPPException {

    SMPPByteBufferException() {
        super();
    }

    SMPPByteBufferException(String message) {
        super(message);
    }

    SMPPByteBufferException(String message, Throwable cause) {
        super(message, cause);
    }

    SMPPByteBufferException(Throwable cause) {
        super(cause);
    }
}
