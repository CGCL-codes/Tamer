package org.debellor.core.exception.data;

import org.debellor.core.exception.DebellorException;

/**
 * @author Marcin Wojnarski
 *
 */
public class DataException extends DebellorException {

    public DataException() {
        super();
    }

    public DataException(String message, Throwable cause) {
        super(message, cause);
    }

    public DataException(String message) {
        super(message);
    }

    public DataException(Throwable cause) {
        super(cause);
    }
}
