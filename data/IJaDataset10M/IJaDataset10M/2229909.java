package com.aelitis.azureus.core.networkmanager.impl.udp;

import com.aelitis.azureus.core.networkmanager.ProtocolEndpoint;
import com.aelitis.azureus.core.networkmanager.TransportEndpoint;

public class TransportEndpointUDP implements TransportEndpoint {

    private ProtocolEndpoint pe;

    public TransportEndpointUDP(ProtocolEndpoint _pe) {
        pe = _pe;
    }

    public ProtocolEndpoint getProtocolEndpoint() {
        return (pe);
    }
}
