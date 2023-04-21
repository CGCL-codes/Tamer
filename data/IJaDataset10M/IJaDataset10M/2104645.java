package com.sleepycat.je.log.entry;

import java.nio.ByteBuffer;
import com.sleepycat.je.DatabaseException;
import com.sleepycat.je.dbi.DatabaseId;
import com.sleepycat.je.dbi.EnvironmentImpl;
import com.sleepycat.je.log.LogEntryHeader;
import com.sleepycat.je.log.LogUtils;
import com.sleepycat.je.tree.IN;
import com.sleepycat.je.utilint.DbLsn;

/**
 * INLogEntry embodies all IN log entries.
 * On disk, an IN log entry contains:
 * <pre>
 *        IN
 *        database id
 *        obsolete LSN  -- in version 2
 * </pre>
 */
public class INLogEntry extends BaseEntry implements LogEntry, NodeLogEntry, INContainingEntry {

    private IN in;

    private DatabaseId dbId;

    private long obsoleteLsn;

    private long nodeId;

    /**
     * Construct a log entry for reading.
     */
    public INLogEntry(Class<? extends IN> INClass) {
        super(INClass);
    }

    /**
     * Construct a log entry for writing to the log.
     */
    public INLogEntry(IN in) {
        setLogType(in.getLogType());
        this.in = in;
        this.dbId = in.getDatabase().getId();
        this.nodeId = in.getNodeId();
        this.obsoleteLsn = in.getLastFullVersion();
    }

    /**
     * Read in an IN entry.
     */
    public void readEntry(LogEntryHeader header, ByteBuffer entryBuffer, boolean readFullItem) throws DatabaseException {
        int logVersion = header.getVersion();
        boolean version6OrLater = (logVersion >= 6);
        if (version6OrLater) {
            dbId = new DatabaseId();
            dbId.readFromLog(entryBuffer, logVersion);
            obsoleteLsn = LogUtils.readLong(entryBuffer, false);
        }
        if (readFullItem) {
            in = (IN) newInstanceOfType();
            in.readFromLog(entryBuffer, logVersion);
            nodeId = in.getNodeId();
        } else {
            int position = entryBuffer.position() + header.getItemSize();
            if (logVersion == 1) {
                position -= LogUtils.UNSIGNED_INT_BYTES;
            } else if (logVersion >= 2) {
                if (version6OrLater) {
                    position -= LogUtils.getPackedLongLogSize(obsoleteLsn);
                } else {
                    position -= LogUtils.LONG_BYTES;
                }
            }
            if (!version6OrLater) {
                position -= LogUtils.INT_BYTES;
            } else {
                position -= LogUtils.getPackedIntLogSize(dbId.getId());
            }
            nodeId = LogUtils.readLong(entryBuffer, !version6OrLater);
            entryBuffer.position(position);
            in = null;
        }
        if (!version6OrLater) {
            dbId = new DatabaseId();
            dbId.readFromLog(entryBuffer, logVersion);
        }
        if (logVersion < 1) {
            obsoleteLsn = DbLsn.NULL_LSN;
        } else if (logVersion == 1) {
            long fileNum = LogUtils.readUnsignedInt(entryBuffer);
            if (fileNum == 0xffffffffL) {
                obsoleteLsn = DbLsn.NULL_LSN;
            } else {
                obsoleteLsn = DbLsn.makeLsn(fileNum, 0);
            }
        } else if (!version6OrLater) {
            obsoleteLsn = LogUtils.readLong(entryBuffer, true);
        }
    }

    /**
     * Returns the LSN of the prior version of this node.  Used for counting
     * the prior version as obsolete.  If the offset of the LSN is zero, only
     * the file number is known because we read a version 1 log entry.
     */
    public long getObsoleteLsn() {
        return obsoleteLsn;
    }

    /**
     * Print out the contents of an entry.
     */
    @Override
    public StringBuilder dumpEntry(StringBuilder sb, boolean verbose) {
        in.dumpLog(sb, verbose);
        dbId.dumpLog(sb, verbose);
        return sb;
    }

    /** Never replicated. */
    public void dumpRep(@SuppressWarnings("unused") StringBuilder sb) {
    }

    /**
     * @return the item in the log entry
     */
    public Object getMainItem() {
        return in;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    /**
     * @see LogEntry#getTransactionId
     */
    public long getTransactionId() {
        return 0;
    }

    /**
     */
    public int getSize() {
        return (in.getLogSize() + dbId.getLogSize() + LogUtils.getPackedLongLogSize(obsoleteLsn));
    }

    /**
     * @see LogEntry#writeEntry
     */
    public void writeEntry(@SuppressWarnings("unused") LogEntryHeader header, ByteBuffer destBuffer) {
        dbId.writeToLog(destBuffer);
        LogUtils.writePackedLong(destBuffer, obsoleteLsn);
        in.writeToLog(destBuffer);
    }

    public IN getIN(@SuppressWarnings("unused") EnvironmentImpl env) {
        return in;
    }

    /**
     * @see NodeLogEntry#getNodeId
     */
    public long getNodeId() {
        return nodeId;
    }

    /**
     * @see INContainingEntry#getDbId()
     */
    public DatabaseId getDbId() {
        return dbId;
    }

    /**
     * @return the LSN that represents this IN. For a vanilla IN entry, it's
     * the last lsn read by the log reader.
     */
    public long getLsnOfIN(long lastReadLsn) {
        return lastReadLsn;
    }

    /**
     * @see LogEntry#logicalEquals
     *
     * INs from two different environments are never considered equal,
     * because they have lsns that are environment-specific.
     */
    public boolean logicalEquals(@SuppressWarnings("unused") LogEntry other) {
        return false;
    }
}
