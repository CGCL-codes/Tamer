package com.amazon.carbonado.repo.dirmi;

import java.rmi.Remote;
import org.cojen.dirmi.RemoteFailure;
import com.amazon.carbonado.RepositoryException;
import com.amazon.carbonado.Storable;
import com.amazon.carbonado.capability.Capability;

public interface RemoteResyncCapability extends Capability, Remote {

    /**
     * Calls the server ResyncCapability with the arguments.
     *
     * @param type type of storable to re-sync
     * @param desiredSpeed throttling parameter - 1.0 = full speed, 0.5 = half
     * speed, 0.1 = one-tenth speed, etc
     * @param filter optional query filter to limit which objects get re-sync'ed
     * @param filterValues filter values for optional filter
     */
    @RemoteFailure(exception = RepositoryException.class)
    <S extends Storable> void resync(Class<S> type, double desiredSpeed, String filter, Object... filterValues) throws RepositoryException;
}
