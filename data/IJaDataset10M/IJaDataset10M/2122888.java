package org.posterita.exceptions;

public class StoreException extends OperationException {

    private static final long serialVersionUID = 1L;

    public StoreException(String msg) {
        super(msg);
    }
}
