package br.rfpm.dao;

public class DaoException extends RuntimeException {

    private static final long serialVersionUID = -4804201820361987472L;

    public DaoException() {
        super();
    }

    public DaoException(String message) {
        super(message);
    }

    public DaoException(Throwable t) {
        super(t);
    }

    public DaoException(String message, Throwable t) {
        super(message, t);
    }
}
