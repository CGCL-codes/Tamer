package org.enerj.server.logentry;

import java.io.*;

/** 
 * Log Entry for "Start Database Checkpoint".
 *
 * @version $Id: StartDatabaseCheckpointLogEntry.java,v 1.3 2005/08/12 02:56:48 dsyrstad Exp $
 * @author <a href="mailto:dsyrstad@ener-j.org">Dan Syrstad</a>
 * @see RedoLogServer
 */
public class StartDatabaseCheckpointLogEntry extends LogEntry {

    private long[] mActiveTransactionIds;

    private long[] mActiveTransactionPositions;

    /**
     * Constructs an empty StartDatabaseCheckpointLogEntry.
     */
    protected StartDatabaseCheckpointLogEntry() {
    }

    /**
     * Constructs a StartDatabaseCheckpointLogEntry.
     *
     * @param anActiveTransactionIdArray an array of transaction ids that were
     *  active at the time of this log entry.
     * @param anActiveTransactionPositionArray an array of log positions to
     *  either the BeginTransactionLogEntry or last CheckpointTransactionLogEntry.
     *  This array has a one-to-one correspondence with anActiveTransactionIdArray,
     *  so they must be the same size.
     */
    public StartDatabaseCheckpointLogEntry(long[] anActiveTransactionIdArray, long[] anActiveTransactionPositionArray) {
        if (anActiveTransactionIdArray.length != anActiveTransactionPositionArray.length) {
            throw new IllegalArgumentException("Arrays must be the same length");
        }
        mActiveTransactionIds = anActiveTransactionIdArray;
        mActiveTransactionPositions = anActiveTransactionPositionArray;
    }

    /**
     * Gets the entry type of this log entry.
     *
     * @return START_DB_CHECKPOINT_ENTRY_TYPE.
     */
    public byte getEntryType() {
        return START_DB_CHECKPOINT_ENTRY_TYPE;
    }

    /**
     * {@inheritDoc}
     */
    protected int loadFromLog(DataInput aDataInput) throws IOException {
        int numTransactions = aDataInput.readInt();
        int totalRead = 4;
        mActiveTransactionIds = new long[numTransactions];
        mActiveTransactionPositions = new long[numTransactions];
        for (int i = 0; i < numTransactions; i++) {
            mActiveTransactionIds[i] = aDataInput.readLong();
            mActiveTransactionPositions[i] = aDataInput.readLong();
        }
        return 4 + (8 * 2 * numTransactions);
    }

    /**
     * {@inheritDoc}
     */
    public void appendToLog(DataOutput aDataOutput) throws IOException {
        super.appendToLog(aDataOutput);
        aDataOutput.writeInt(mActiveTransactionIds.length);
        for (int i = 0; i < mActiveTransactionIds.length; i++) {
            aDataOutput.writeLong(mActiveTransactionIds[i]);
            aDataOutput.writeLong(mActiveTransactionPositions[i]);
        }
    }

    /**
     * Gets the array of transaction ids that were active at the time of this log entry.
     *
     * @return the array of active transaction ids.
     */
    public long[] getActiveTransactionIds() {
        return mActiveTransactionIds;
    }

    /**
     * Gets the array of log positions to either the BeginTransactionLogEntry or 
     * last CheckpointTransactionLogEntry
     * for the transactions that were active at the time of this log entry.
     *
     * @return the array of log positions.
     */
    public long[] getActiveTransactionPositions() {
        return mActiveTransactionPositions;
    }

    /**
     * {@inheritDoc}
     */
    public String toString() {
        String name = this.getClass().getName();
        StringBuffer buf = new StringBuffer(name.substring(name.lastIndexOf('.') + 1) + "[ numActiveTxns=" + mActiveTransactionIds.length + " txnId/Pos=(");
        for (int i = 0; i < mActiveTransactionIds.length; i++) {
            if (i != 0) {
                buf.append(',');
            }
            buf.append(mActiveTransactionIds[i]);
            buf.append('/');
            buf.append(mActiveTransactionPositions[i]);
        }
        buf.append(") ]");
        return buf.toString();
    }
}
