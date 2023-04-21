package org.xtreemfs.babudb;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import org.xtreemfs.babudb.api.dev.transaction.InMemoryProcessing;
import org.xtreemfs.babudb.api.dev.transaction.OperationInternal;
import org.xtreemfs.babudb.api.dev.transaction.TransactionInternal;
import org.xtreemfs.babudb.api.dev.transaction.TransactionManagerInternal;
import org.xtreemfs.babudb.api.exception.BabuDBException;
import org.xtreemfs.babudb.api.exception.BabuDBException.ErrorCode;
import org.xtreemfs.babudb.api.transaction.TransactionListener;
import org.xtreemfs.babudb.api.transaction.Operation;
import org.xtreemfs.babudb.log.DiskLogger;
import org.xtreemfs.babudb.log.LogEntry;
import org.xtreemfs.babudb.log.SyncListener;
import org.xtreemfs.babudb.lsmdb.LSN;
import org.xtreemfs.foundation.buffer.BufferPool;
import org.xtreemfs.foundation.buffer.ReusableBuffer;
import org.xtreemfs.foundation.logging.Logging;

/**
 * Default implementation of the {@link TransactionManagerInternal} interface using
 * the {@link DiskLogger} to let operations become persistent.
 * 
 * @author flangner
 * @since 11/03/2010
 */
class TransactionManagerImpl extends TransactionManagerInternal {

    private final AtomicReference<DiskLogger> diskLogger = new AtomicReference<DiskLogger>(null);

    /**
     * list of transaction listeners
     */
    private final List<TransactionListener> listeners = new LinkedList<TransactionListener>();

    private volatile LSN latestOnDisk;

    private final boolean isAsync;

    public TransactionManagerImpl(boolean isAsync) {
        this.isAsync = isAsync;
    }

    public void init(LSN initial) {
        latestOnDisk = initial;
    }

    public void setLogger(DiskLogger logger) {
        synchronized (diskLogger) {
            DiskLogger old = diskLogger.getAndSet(logger);
            if (logger == null) {
                latestOnDisk = old.getLatestLSN();
            } else {
                diskLogger.notify();
            }
        }
    }

    @Override
    public void makePersistent(final TransactionInternal txn, ReusableBuffer payload, BabuDBRequestResultImpl<Object> future) throws BabuDBException {
        Logging.logMessage(Logging.LEVEL_DEBUG, this, "Trying to perform transaction %s ...", txn.toString());
        try {
            Object[] result = inMemory(txn, payload);
            LogEntry entry = generateLogEntry(txn, payload, future, result);
            onDisk(txn, entry);
            if (isAsync) {
                future.finished(result, null);
                for (TransactionListener l : listeners) {
                    l.transactionPerformed(txn);
                }
            }
        } finally {
            txn.unlockWorkers();
        }
    }

    /**
     * Method to create a logEntry for the given transaction.
     * 
     * @param txn
     * @param payload
     * @param listener
     * 
     * @return a prepared logEntry. 
     */
    private final LogEntry generateLogEntry(final TransactionInternal txn, ReusableBuffer payload, final BabuDBRequestResultImpl<Object> listener, final Object[] results) {
        LogEntry result = new LogEntry(payload, new SyncListener() {

            @Override
            public void synced(LSN lsn) {
                try {
                    if (!isAsync) {
                        BabuDBException irregs = txn.getIrregularities();
                        if (irregs == null) {
                            listener.finished(results, lsn);
                        } else {
                            throw new BabuDBException(irregs.getErrorCode(), "Transaction " + "failed at the execution of the " + (txn.size() + 1) + "th operation, because: " + irregs.getMessage(), irregs);
                        }
                    }
                } catch (BabuDBException error) {
                    if (!isAsync) {
                        listener.failed(error);
                    } else {
                        Logging.logError(Logging.LEVEL_WARN, this, error);
                    }
                } finally {
                    Logging.logMessage(Logging.LEVEL_DEBUG, this, "... transaction %s finished.", txn.toString());
                    if (!isAsync) {
                        for (TransactionListener l : listeners) {
                            l.transactionPerformed(txn);
                        }
                    }
                }
            }

            @Override
            public void failed(Exception ex) {
                if (!isAsync) {
                    listener.failed((ex != null && ex instanceof BabuDBException) ? (BabuDBException) ex : new BabuDBException(ErrorCode.INTERNAL_ERROR, ex.getMessage()));
                }
            }
        }, LogEntry.PAYLOAD_TYPE_TRANSACTION);
        return result;
    }

    /**
     * Initialize the on-disk processing. The transaction's log entry is send to the diskLogger.
     * 
     * @param txn
     * @param entry
     */
    private final void onDisk(TransactionInternal txn, LogEntry entry) throws BabuDBException {
        try {
            synchronized (diskLogger) {
                while (diskLogger.get() == null) {
                    diskLogger.wait();
                }
                diskLogger.get().append(entry);
            }
        } catch (InterruptedException ie) {
            if (entry != null) entry.free();
            throw new BabuDBException(ErrorCode.INTERRUPTED, "Operation " + "could not have been stored persistent to disk and " + "will therefore be discarded.", ie.getCause());
        }
    }

    /**
     * Internal method to process the in-memory changes on BabuDB for a given transaction.
     * 
     * @param txn
     * @throws BabuDBException
     */
    private final Object[] inMemory(TransactionInternal txn, ReusableBuffer payload) throws BabuDBException {
        List<Object> operationResults = new ArrayList<Object>();
        for (int i = 0; i < txn.size(); i++) {
            try {
                OperationInternal operation = txn.get(i);
                txn.lockResponsibleWorker(operation.getDatabaseName());
                operationResults.add(inMemoryProcessing.get(operation.getType()).process(operation));
            } catch (BabuDBException be) {
                if (i > 0) {
                    txn.cutOfAt(i, be);
                    try {
                        payload.shrink(txn.getSize());
                        break;
                    } catch (IOException ioe) {
                        BufferPool.free(payload);
                        throw new BabuDBException(ErrorCode.IO_ERROR, ioe.getMessage(), ioe);
                    }
                } else {
                    BufferPool.free(payload);
                    throw be;
                }
            }
        }
        return operationResults.toArray();
    }

    @Override
    public void replayTransaction(TransactionInternal txn) throws BabuDBException {
        for (OperationInternal operation : txn) {
            byte type = operation.getType();
            if (type != Operation.TYPE_COPY_DB && type != Operation.TYPE_CREATE_DB && type != Operation.TYPE_DELETE_DB) {
                InMemoryProcessing processing = inMemoryProcessing.get(type);
                try {
                    processing.process(operation);
                } catch (BabuDBException be) {
                    if (!(type == Operation.TYPE_CREATE_SNAP && (be.getErrorCode() == ErrorCode.SNAP_EXISTS || be.getErrorCode() == ErrorCode.NO_SUCH_DB)) && !(type == Operation.TYPE_DELETE_SNAP && be.getErrorCode() == ErrorCode.NO_SUCH_SNAPSHOT) && !(type == Operation.TYPE_GROUP_INSERT && be.getErrorCode().equals(ErrorCode.NO_SUCH_DB))) {
                        throw be;
                    }
                }
            }
        }
    }

    @Override
    public void lockService() throws InterruptedException {
        DiskLogger logger = diskLogger.get();
        if (logger != null) {
            logger.lock();
        }
    }

    @Override
    public void unlockService() {
        DiskLogger logger = diskLogger.get();
        if (logger != null && logger.hasLock()) {
            logger.unlock();
        }
    }

    @Override
    public LSN getLatestOnDiskLSN() {
        DiskLogger logger = diskLogger.get();
        if (logger != null) {
            return logger.getLatestLSN();
        } else {
            return latestOnDisk;
        }
    }

    @Override
    public void addTransactionListener(TransactionListener listener) {
        if (listener == null) throw new NullPointerException();
        listeners.add(listener);
    }

    @Override
    public void removeTransactionListener(TransactionListener listener) {
        listeners.remove(listener);
    }
}
