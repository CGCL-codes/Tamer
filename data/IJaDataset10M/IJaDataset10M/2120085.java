package org.mitre.rt.client.exceptions;

/**
 * This class represents an exception in the SynchronizationManager
 * @author BAKERJ
 */
public class SynchronizationManagerException extends RTException {

    public SynchronizationManagerException() {
        super("", "", null);
    }

    public SynchronizationManagerException(java.lang.String msg) {
        super(msg, "", null);
    }

    public SynchronizationManagerException(java.lang.String msg, Exception ex) {
        super(msg, "", ex);
    }

    public SynchronizationManagerException(java.lang.String msg, java.lang.String details, Exception ex) {
        super(msg, details, ex);
    }

    public SynchronizationManagerException(java.lang.String msg, java.lang.String details) {
        super(msg, details);
    }
}
