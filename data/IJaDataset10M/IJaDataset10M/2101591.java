package com.sun.midp.push.reservation.impl;

import com.sun.midp.push.reservation.ProtocolFactory;
import java.util.HashMap;
import java.util.Map;

/**
 * Simple in memory implementation of <code>ProtocolRegistry</code>.
 * 
 * <p>Might be used in OSGi being exposed as a servie.</p>
 */
public final class MapProtocolRegistry implements ProtocolRegistry {

    /** Mapping from protocols to factories. */
    private final Map map = new HashMap();

    /**
     * Binds a protocol to a factory.
     * 
     * <p><code>protocol</code> argument gets checked before putting and
     * <code>IllegalArgumentException</code> might be thrown.
     * 
     * @param protocol protocol to bind to
     * @param factory factory to use
     *
     * @return previous factory or <code>null</code> if none
     */
    public ProtocolFactory bind(final String protocol, final ProtocolFactory factory) {
        final String p = protocol.toLowerCase();
        ConnectionName.checkProtocol(p);
        return (ProtocolFactory) map.put(p, factory);
    }

    /** {@inheritDoc} */
    public ProtocolFactory get(final String protocol) {
        return (ProtocolFactory) map.get(protocol);
    }
}
