package com.intel.gpe.clients.api.exceptions;

import com.intel.gpe.clients.api.WSRFClient;

/**
 * Exception thrown when TS-Management refuses to add a user to a TargetSystem.
 *
 * @version $Id: GPEUserNotAddedException.java,v 1.3 2006/02/18 18:42:29 nbmalysh Exp $
 * @author Dmitry Petrov
 */
public class GPEUserNotAddedException extends GPEServiceException {

    private static final long serialVersionUID = 6275959524733769870L;

    public GPEUserNotAddedException(String message, FaultWrapper fault, WSRFClient resourceClient) {
        super(message, fault, resourceClient);
    }
}
