package com.mercadopago.exceptions;

public class MercadoPagoException extends Exception {

    private static final long serialVersionUID = -1900756593135232287L;

    public MercadoPagoException() {
    }

    public MercadoPagoException(String message) {
        super(message);
    }

    public MercadoPagoException(Throwable throwable) {
        super(throwable);
    }

    public MercadoPagoException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
