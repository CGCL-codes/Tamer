package zeroj.orm;

/**
 * 
 * @author lyl
 * 
 * @since 1.0
 * 
 * 2008-9-12
 */
public class DaoException extends Exception {

    private static final long serialVersionUID = -4840677955191986981L;

    public DaoException() {
        super();
    }

    public DaoException(String reason) {
        super(reason);
    }

    public DaoException(String reason, Throwable cause) {
        super(reason, cause);
    }

    public DaoException(Throwable cause) {
        super(cause);
    }
}
