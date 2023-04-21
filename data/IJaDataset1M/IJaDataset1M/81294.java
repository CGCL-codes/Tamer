package net.sf.nanomvc.flow.runtime;

import net.sf.nanomvc.core.NanoRuntimeException;

public class IllegalEventException extends NanoRuntimeException {

    private static final long serialVersionUID = 1L;

    public IllegalEventException(String message) {
        super(message);
    }
}
