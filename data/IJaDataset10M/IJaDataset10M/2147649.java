package javax.webbeans;

import javax.webbeans.manager.Context;

/**
 * If the {@link Context} is not avalaible in the time of
 * web beans component getting, this exception is thrown.
 * 
 * @author <a href="mailto:gurkanerdogdu@yahoo.com">Gurkan Erdogdu</a>
 * @since 1.0
 */
public class ContextNotActiveException extends ExecutionException {

    private static final long serialVersionUID = 4783816486073845333L;

    public ContextNotActiveException(String message) {
        super(message);
    }

    public ContextNotActiveException(Throwable e) {
        super(e);
    }

    public ContextNotActiveException(String message, Throwable e) {
        super(message, e);
    }
}
