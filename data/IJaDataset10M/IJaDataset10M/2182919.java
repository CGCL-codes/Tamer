package javax.transaction;

/**
 * @author Warren Levy (warrenl@redhat.com)
 * @date May 25, 2001
 */
public class RollbackException extends Exception {

    public RollbackException() {
        super();
    }

    public RollbackException(String msg) {
        super(msg);
    }
}
