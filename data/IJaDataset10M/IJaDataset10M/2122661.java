package tyrex.tm;

import tyrex.util.Messages;

/**
 * Thrown to terminate the current thread and indicate that the
 * current transaction has timed out.
 *
 * @author <a href="arkin@intalio.com">Assaf Arkin</a>
 * @version $Revision: 1.5 $ $Date: 2001/03/12 19:20:19 $
 */
public class TransactionTimeoutException extends RuntimeException {

    public TransactionTimeoutException() {
        super(Messages.message("tyrex.tx.timedOut"));
    }

    public TransactionTimeoutException(String message) {
        super(message);
    }
}
