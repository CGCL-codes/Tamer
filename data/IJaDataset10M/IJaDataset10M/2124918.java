package org.exist.storage.journal;

import org.exist.EXistException;

public class LogException extends EXistException {

    private static final long serialVersionUID = 555708654980577412L;

    public LogException(String message) {
        super(message);
    }

    public LogException(String message, Throwable cause) {
        super(message, cause);
    }
}
