package com.ms.wsdiscovery.exception;

import com.ms.wsdiscovery.exception.WsDiscoveryException;

/**
 *
 * @author Magnus Skjegstad
 */
public class WsDiscoveryXMLException extends WsDiscoveryException {

    /**
     * Creates a new instance of <code>WsDiscoveryXMLException</code> without detail message.
     */
    public WsDiscoveryXMLException() {
    }

    /**
     * Constructs an instance of <code>WsDiscoveryXMLException</code> with the specified detail message.
     * @param msg the detail message.
     */
    public WsDiscoveryXMLException(String msg) {
        super(msg);
    }

    public WsDiscoveryXMLException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
