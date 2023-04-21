package org.bulatnig.smpp.pdu.tlv;

/**
 * Comment here.
 * <p/>
 * User: Bulat Nigmatullin
 * Date: Nov 12, 2008
 * Time: 9:44:10 AM
 */
public class ParameterTagNotFoundException extends TLVNotFoundException {

    ParameterTagNotFoundException() {
        super();
    }

    ParameterTagNotFoundException(String message) {
        super(message);
    }

    ParameterTagNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    ParameterTagNotFoundException(Throwable cause) {
        super(cause);
    }
}
