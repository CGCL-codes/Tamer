package org.nakedobjects.distribution;

import org.nakedobjects.object.NakedObjectRuntimeException;

/**
 * Denotes an exception that occured on the server.
 */
public class NakedObjectsRemoteException extends NakedObjectRuntimeException {

    public NakedObjectsRemoteException() {
        super();
    }

    public NakedObjectsRemoteException(String msg) {
        super(msg);
    }

    public NakedObjectsRemoteException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public NakedObjectsRemoteException(Throwable cause) {
        super(cause);
    }
}
