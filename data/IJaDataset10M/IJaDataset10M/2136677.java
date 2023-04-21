package net.sf.lucis.core;

public class StoreException extends RuntimeException {

    private static final long serialVersionUID = 9197701772137276168L;

    public StoreException() {
        super();
    }

    public StoreException(String message, Throwable cause) {
        super(message, cause);
    }

    public StoreException(Throwable cause) {
        super(cause);
    }
}
