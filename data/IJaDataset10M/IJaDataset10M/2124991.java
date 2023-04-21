package com.tesisutn.restsoft.dominio.usuario;

public class VistaNoDisponibleException extends RuntimeException {

    public VistaNoDisponibleException() {
        super();
    }

    public VistaNoDisponibleException(String message) {
        super(message);
    }

    public VistaNoDisponibleException(String message, Throwable cause) {
        super(message, cause);
    }
}
