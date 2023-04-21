package redora.exceptions;

/**
 * Wrapper for exceptions when a lazy loaded field is retrieved.
 *
 * @author Nanjing RedOrange (http://www.red-orange.cn)
 * */
public class LazyException extends RedoraException {

    private static final long serialVersionUID = 1L;

    public LazyException(String s, Exception e) {
        super(s, e);
    }

    public LazyException(String s) {
        super(s);
    }
}
