package com.proyecto.tropero.core.excepciones;

public class ContrasenaInvalidaException extends Exception {

    public ContrasenaInvalidaException() {
        super();
    }

    public ContrasenaInvalidaException(String arg0, Throwable arg1) {
        super(arg0, arg1);
    }

    public ContrasenaInvalidaException(String arg0) {
        super(arg0);
    }

    public ContrasenaInvalidaException(Throwable arg0) {
        super(arg0);
    }
}
