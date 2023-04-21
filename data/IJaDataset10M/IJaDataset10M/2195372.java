package org.mitre.rt.common;

import org.mitre.rt.client.exceptions.RTException;

/**
 *
 * @author BAKERJ
 */
public class DataValidationException extends RTException {

    public DataValidationException() {
        super("", "", null);
    }

    public DataValidationException(java.lang.String msg) {
        super(msg, "", null);
    }

    public DataValidationException(java.lang.String msg, Exception ex) {
        super(msg, "", ex);
    }

    public DataValidationException(java.lang.String msg, java.lang.String details, Exception ex) {
        super(msg, details, ex);
    }

    public DataValidationException(java.lang.String msg, java.lang.String details) {
        super(msg, details, null);
    }
}
