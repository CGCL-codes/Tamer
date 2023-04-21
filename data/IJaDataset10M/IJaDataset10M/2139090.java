package org.soda.dpws;

import org.soda.dpws.addressing.EndpointReference;
import org.soda.dpws.registry.discovery.service.DiscoveryMessageType;

/**
 * Event passed to the classes implementing {@link DeviceProxyListener}.
 *
 */
public interface DeviceProxyEvent {

    /**
   * The type of the message.
   * @return the {@link DiscoveryMessageType}
   */
    public DiscoveryMessageType getMsgType();

    /**
   *
   * @return The {@link EndpointReference} of the device.
   */
    public EndpointReference getEndpointReference();
}
