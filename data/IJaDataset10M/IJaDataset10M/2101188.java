package br.org.databasetools.core.exception;

public class TransactionException extends ExceptionGeral {

    private static final long serialVersionUID = 1L;

    public TransactionException() {
        super("Erro na conexão!");
    }

    public TransactionException(String message, Throwable cause) {
        super(message, cause);
    }

    public TransactionException(String message) {
        super(message);
    }

    public TransactionException(Throwable cause) {
        super(cause);
    }
}
