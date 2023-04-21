package br.org.databasetools.core.exception;

public class AbortException extends ExceptionGeral {

    private static final long serialVersionUID = -1555309435121038824L;

    public AbortException() {
        super("Operação cancelada!");
    }

    public AbortException(String message, Throwable cause) {
        super(message, cause);
    }

    public AbortException(String message) {
        super(message);
    }

    public AbortException(Throwable cause) {
        super(cause);
    }
}
