package iwallet.client.requester;

import java.lang.Throwable;
import java.lang.Exception;

/**
 * @author 黄源河
 *
 */
public class ServerCommunicationException extends Exception {

    public ServerCommunicationException() {
        super();
    }

    public ServerCommunicationException(String message) {
        super(message);
    }

    public ServerCommunicationException(String message, Throwable cause) {
        super(message, cause);
    }
}
