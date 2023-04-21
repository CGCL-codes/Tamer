package br.com.bafonline.util.exception.dao;

public class SaveException extends DAOException {

    private static final long serialVersionUID = -6880262920185307328L;

    public SaveException() {
        super();
    }

    public SaveException(String message) {
        super(message);
    }

    public SaveException(String message, Throwable t) {
        super(message, t);
    }

    public SaveException(Throwable t) {
        super(t);
    }
}
