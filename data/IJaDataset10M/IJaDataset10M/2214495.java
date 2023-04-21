package org.wfp.rita.exception;

import org.wfp.rita.base.RitaException;
import org.wfp.rita.datafacade.DataFacade;

/**
 * Thrown when trying to call a method that is only valid on a 
 * {@link DataFacade.SyncState#MASTER master instance}, and the current
 * instance is not a master instance. In other words, this command probably
 * needs to be executed over RPC to the master instance instead.
 * @author Chris Wilson <chris+rita@aptivate.org>
 */
public class NotMasterInstance extends RitaException {

    private static final long serialVersionUID = 1L;

    public NotMasterInstance() {
        super("This command is only valid on a Master Instance.");
    }
}
