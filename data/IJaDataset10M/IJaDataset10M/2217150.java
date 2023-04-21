package br.com.visualmidia.exception;

public class DailyTransactionDoesNotExistsException extends QueryException {

    private static final long serialVersionUID = 3761403127508973367L;

    public DailyTransactionDoesNotExistsException(String name) {
        super("A transa��o " + name + " n�o existe.");
    }
}
