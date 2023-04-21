package com.intel.gpe.clients.api.exceptions;

import com.intel.gpe.clients.api.ServiceClient;

/**
 * Exception thrown when TS-management fails to parse an incarnation template.
 *
 * @version $Id: GPEIncarnationCreationFailedException.java,v 1.3 2006/02/18 18:42:29 nbmalysh Exp $
 * @author Dmitry Petrov
 */
public class GPEIncarnationCreationFailedException extends GPEClientException {

    private static final long serialVersionUID = -4003052673732415374L;

    private String repr;

    public GPEIncarnationCreationFailedException(String message, Throwable cause, ServiceClient resourceClient, String repr) {
        super(message, cause, resourceClient);
        this.repr = repr;
    }

    /**
     * Get original incarnation template
     * @return original incarnation template as String
     */
    public String getRepresentation() {
        return repr;
    }
}
