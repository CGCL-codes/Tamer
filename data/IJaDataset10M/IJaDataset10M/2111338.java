package org.gudy.azureus2.pluginsimpl.remote.rpexceptions;

import org.gudy.azureus2.pluginsimpl.remote.RPException;

public class RPInternalProcessException extends RPException {

    public RPInternalProcessException(Throwable t) {
        super(t);
    }

    public String getRPType() {
        return "internal-error";
    }
}
