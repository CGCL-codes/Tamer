package org.mobicents.media.server.spi;

import java.io.Serializable;

/**
 *
 * @author kulikov
 */
public interface NamingService extends Serializable {

    public Endpoint lookup(String endpointName, boolean allowInUse) throws ResourceUnavailableException;

    public Endpoint lookup(String endpointName) throws ResourceUnavailableException;

    public int getEndpointCount();
}
