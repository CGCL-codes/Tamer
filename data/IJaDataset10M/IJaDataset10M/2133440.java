package com.skjegstad.soapoverudp.interfaces;

import com.skjegstad.soapoverudp.configurations.SOAPOverUDPConfiguration;

/**
 * Configurable base classes used for SOAPOverUDP should implement this interface.
 *
 * @author Magnus Skjegstad
 */
public interface ISOAPOverUDPConfigurable {

    /**
     * Set SOAPOverUDP configuration.
     *
     * @param configuration SOAPOverUDP configuration.
     */
    void setConfiguration(SOAPOverUDPConfiguration configuration);
}
