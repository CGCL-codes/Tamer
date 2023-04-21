package org.nakedobjects.noa.adapter;

import org.nakedobjects.noa.NakedObjectRuntimeException;

/**
 * Indicates that a request to resolve an object has failed. Unresolved objects should never be used as they
 * will cause further errors.
 */
public class ResolveException extends NakedObjectRuntimeException {

    private static final long serialVersionUID = 1L;

    public ResolveException() {
        super();
    }

    public ResolveException(final String msg, final Throwable cause) {
        super(msg, cause);
    }

    public ResolveException(final String msg) {
        super(msg);
    }

    public ResolveException(final Throwable cause) {
        super(cause);
    }
}
