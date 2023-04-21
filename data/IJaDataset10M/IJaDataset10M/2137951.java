package com.amazon.carbonado.repo.dirmi;

import java.io.IOException;
import org.cojen.dirmi.Pipe;
import com.amazon.carbonado.FetchException;
import com.amazon.carbonado.RepositoryException;
import com.amazon.carbonado.Storable;
import com.amazon.carbonado.capability.RemoteProcedure;
import com.amazon.carbonado.util.AbstractPool;

/**
 * 
 *
 * @author Brian S O'Neill
 */
class RemoteProcedureExecutorServer implements RemoteProcedureExecutor, ProcedureOpCodes {

    final RemoteRepositoryServer mRepositoryServer;

    private final RemoteStorageRequestor mStorageRequestor;

    private final WriterPool mStorableWriters;

    RemoteProcedureExecutorServer(RemoteRepositoryServer repo, RemoteStorageRequestor r) {
        mRepositoryServer = repo;
        mStorageRequestor = r;
        mStorableWriters = new WriterPool();
    }

    public Pipe remoteCall(RemoteTransaction txn, RemoteProcedure proc, Pipe pipe) {
        if (!mRepositoryServer.attach(txn)) {
            try {
                try {
                    pipe.writeByte(OP_THROWABLE);
                    pipe.writeThrowable(new RepositoryException(ClientStorage.TXN_INVALID_MSG));
                } finally {
                    pipe.close();
                }
            } catch (IOException e) {
            }
            return null;
        }
        ProcedureRequest request = new ProcedureRequest(this, pipe, txn);
        try {
            try {
                if (proc.handleRequest(mRepositoryServer.mRepository, request)) {
                    request.silentFinish();
                }
            } catch (LinkageError e) {
                request.silentFinish(new FetchException("Remote procedure class is malformed or " + "is missing from server: " + proc.getClass().getName() + ", caused by: " + e));
            } catch (Throwable e) {
                request.silentFinish(e);
            }
        } catch (Throwable e) {
            mRepositoryServer.detach(txn);
            Thread t = Thread.currentThread();
            t.getUncaughtExceptionHandler().uncaughtException(t, e);
        }
        return null;
    }

    <S extends Storable> StorableWriter<S> writerFor(Class<S> type) throws RepositoryException {
        return (StorableWriter<S>) mStorableWriters.get(type);
    }

    private class WriterPool extends AbstractPool<Class, StorableWriter, RepositoryException> {

        protected StorableWriter create(Class type) throws RepositoryException {
            return ((RemoteStorageServer) mStorageRequestor.serverStorageFor(type)).storableWriter();
        }
    }
}
