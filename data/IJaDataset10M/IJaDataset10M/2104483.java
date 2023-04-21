package br.com.cocodonto.framework.dao;

public class UpdateDaoException extends RuntimeException {

    public UpdateDaoException() {
        super();
    }

    public UpdateDaoException(String message, Throwable cause) {
        super(message, cause);
    }

    public UpdateDaoException(String message) {
        super(message);
    }

    public UpdateDaoException(Throwable cause) {
        super(cause);
    }
}
