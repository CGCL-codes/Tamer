package org.wfp.rita.exception;

import org.wfp.rita.base.RitaException;
import org.wfp.rita.pojo.base.VersionedRecord;

/**
 * Thrown when trying to perform an operation on a local instance that is
 * not allowed when offline, and the local instance is offline or the server
 * could not be contacted.
 * @author Chris Wilson <chris+rita@aptivate.org>
 */
public class ConnectionRequired extends RitaException {

    private static final long serialVersionUID = 1L;

    public ConnectionRequired() {
        super("This operation is not allowed on an unconfigured instance.");
    }

    public ConnectionRequired(Exception cause) {
        super("This operation is not allowed on an unconfigured instance. " + "The instance was unable to connect for the following reason.", cause);
    }

    public ConnectionRequired(VersionedRecord triedToModify) {
        super("The object that you tried to modify is not owned by this " + "Local Instance, so it can only be modified when you are " + "online: " + triedToModify + ". This instance is offline due " + "to an earlier error which was already logged.");
    }

    public ConnectionRequired(VersionedRecord triedToModify, Throwable cause) {
        super("The object that you tried to modify is not owned by this " + "Local Instance, so it can only be modified when you are " + "online: " + triedToModify + ". I was unable to " + "modify it online due to an error which has been logged.", cause);
    }
}
