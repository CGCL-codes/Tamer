package com.amazon.carbonado.repo.dirmi;

import java.rmi.Remote;
import org.cojen.dirmi.Asynchronous;
import org.cojen.dirmi.CallMode;
import org.cojen.dirmi.Pipe;
import org.cojen.dirmi.RemoteFailure;
import com.amazon.carbonado.RepositoryException;
import com.amazon.carbonado.capability.RemoteProcedure;

/**
 * Remote interface for executing remote procedure calls. Defining this as a
 * separate interface (instead of folding into RemoteRepository) allows the
 * server implementation to cache session-specific serialization logic.
 *
 * @author Brian S O'Neill
 */
public interface RemoteProcedureExecutor extends Remote {

    @Asynchronous(CallMode.REQUEST_REPLY)
    @RemoteFailure(exception = RepositoryException.class)
    Pipe remoteCall(RemoteTransaction txn, RemoteProcedure proc, Pipe pipe) throws RepositoryException;
}
