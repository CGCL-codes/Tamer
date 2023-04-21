package net.sourceforge.hachikaduki.dao;

public class DuplicateKeyException extends DataAccessException {

    private static final long serialVersionUID = 1L;

    public DuplicateKeyException() {
        super();
    }

    public DuplicateKeyException(String message, Throwable cause) {
        super(message, cause);
    }

    public DuplicateKeyException(String message) {
        super(message);
    }

    public DuplicateKeyException(Throwable cause) {
        super(cause);
    }
}
