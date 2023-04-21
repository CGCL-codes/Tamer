package org.freebxml.omar.common.spi;

import org.oasis.ebxml.registry.bindings.rs.RegistryRequestType;

/**
 * The interface that carries Request specific context information.
 * Implemented differently by client and server to serve their 
 * specific needs for providing state and context during the 
 * processing of a request.
 * 
 * @author Farrukh Najmi
 * @author Diego Ballve
 * @author Bernie Thuman (rewrote for HIEOS).
 *
 */
public interface RequestContext {

    /**
     * Gets the current RegistryRequest being processed by the context.
     *
     * @return the current RegistryRequestType.
     */
    public RegistryRequestType getCurrentRegistryRequest();
}
