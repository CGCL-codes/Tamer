package com.completex.objective.components.persistency;

/**
 * @author Gennady Krizhevsky
 */
public class LockedRecordException extends OdalPersistencyException {

    public LockedRecordException() {
    }

    public LockedRecordException(String message) {
        super(message);
    }

    public LockedRecordException(String message, Throwable cause) {
        super(message, cause);
    }

    public LockedRecordException(Throwable cause) {
        super(cause);
    }
}
