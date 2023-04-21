package br.com.bafonline.util.exception.hibernate;

import br.com.bafonline.util.exception.BafonlineExceptionHandler;

public class HibernateSessionFactoryException extends BafonlineExceptionHandler {

    private static final long serialVersionUID = 8809051903962738215L;

    public HibernateSessionFactoryException() {
        super();
    }

    public HibernateSessionFactoryException(String message) {
        super(message);
    }

    public HibernateSessionFactoryException(String message, Throwable t) {
        super(message, t);
    }

    public HibernateSessionFactoryException(Throwable t) {
        super(t);
    }
}
