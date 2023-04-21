package org.nakedobjects.nof.reflect.javax.cglib.factory;

public class RuntimeExceptionWrapper extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private final RuntimeException runtimeException;

    public RuntimeExceptionWrapper(final RuntimeException runtimeException) {
        this.runtimeException = runtimeException;
    }

    public RuntimeException getRuntimeException() {
        return runtimeException;
    }
}
