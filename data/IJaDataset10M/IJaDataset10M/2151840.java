package com.dbxml.db.core.trigger;

import com.dbxml.db.core.DBException;

/**
 * A TriggerException is thrown if an exception occurs in the
 * lookup, or execution of a Trigger.
 */
public class TriggerException extends DBException {

    public TriggerException(int faultCode) {
        super(faultCode);
    }

    public TriggerException(int faultCode, Throwable wrapped) {
        super(faultCode, wrapped);
    }

    public TriggerException(int faultCode, String message) {
        super(faultCode, message);
    }

    public TriggerException(int faultCode, String message, Throwable wrapped) {
        super(faultCode, message, wrapped);
    }
}
