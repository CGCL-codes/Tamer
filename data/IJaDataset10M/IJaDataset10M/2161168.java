package org.limewire.mojito.exceptions;

import org.limewire.mojito.messages.ResponseMessage;

/**
 * The DHTNoSuchElementException is thrown if an attempt was made to get
 * a DHTValue from a remote Node and the Node did not have the requested
 * value.
 */
public class DHTNoSuchElementException extends DHTResponseException {

    private static final long serialVersionUID = -5472718370950904407L;

    public DHTNoSuchElementException(ResponseMessage response, String message) {
        super(response, message);
    }
}
