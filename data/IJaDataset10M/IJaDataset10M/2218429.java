package br.com.bafonline.util.exception.hibernate;

public class HibernateTransactionException extends HibernateSessionFactoryException {

    private static final long serialVersionUID = -1379453645971148678L;

    public HibernateTransactionException() {
        super();
    }

    public HibernateTransactionException(String message) {
        super(message);
    }

    public HibernateTransactionException(String message, Throwable t) {
        super(message, t);
    }

    public HibernateTransactionException(Throwable t) {
        super(t);
    }
}
