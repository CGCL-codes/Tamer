package org.quickfix;

public class RuntimeError extends RuntimeException {

    public RuntimeError() {
        super();
    }

    public RuntimeError(String message) {
        super(message);
    }
}
