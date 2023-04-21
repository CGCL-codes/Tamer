package com.sleepycat.je.log;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;
import com.sleepycat.je.DatabaseException;
import com.sleepycat.je.cleaner.RecoveryUtilizationTracker;
import com.sleepycat.je.dbi.DatabaseId;
import com.sleepycat.je.dbi.DbTree;
import com.sleepycat.je.dbi.EnvironmentImpl;
import com.sleepycat.je.log.entry.INContainingEntry;
import com.sleepycat.je.log.entry.INLogEntry;
import com.sleepycat.je.log.entry.LNLogEntry;
import com.sleepycat.je.log.entry.LogEntry;
import com.sleepycat.je.log.entry.NodeLogEntry;
import com.sleepycat.je.recovery.VLSNRecoveryProxy;
import com.sleepycat.je.tree.FileSummaryLN;
import com.sleepycat.je.tree.IN;
import com.sleepycat.je.tree.INDeleteInfo;
import com.sleepycat.je.tree.INDupDeleteInfo;
import com.sleepycat.je.tree.MapLN;
import com.sleepycat.je.utilint.DbLsn;

/**
 * INFileReader supports recovery by scanning log files during the IN rebuild
 * pass. It looks for internal nodes (all types), segregated by whether they
 * belong to the main tree or the duplicate trees.
 *
 * <p>This file reader can also be run in tracking mode to keep track of the
 * maximum node id, database id and txn id seen so those sequences can be
 * updated properly at recovery.  In this mode it also performs utilization
 * counting.  It is only run once in tracking mode per recovery, in the first
 * phase of recovery.</p>
 */
public class INFileReader extends FileReader {

    private boolean lastEntryWasDelete;

    private boolean lastEntryWasDupDelete;

    private LogEntryType fromLogType;

    private boolean isProvisional;

    private Map<LogEntryType, LogEntry> targetEntryMap;

    private LogEntry targetLogEntry;

    private Map<LogEntryType, LogEntry> dbIdTrackingMap;

    private LNLogEntry dbIdTrackingEntry;

    private Map<LogEntryType, LogEntry> txnIdTrackingMap;

    private LNLogEntry txnIdTrackingEntry;

    private Map<LogEntryType, NodeLogEntry> otherNodeTrackingMap;

    private NodeLogEntry nodeTrackingEntry;

    private INLogEntry inTrackingEntry;

    private LNLogEntry fsTrackingEntry;

    private boolean trackIds;

    private long minReplicatedNodeId;

    private long maxNodeId;

    private int minReplicatedDbId;

    private int maxDbId;

    private long minReplicatedTxnId;

    private long maxTxnId;

    private boolean mapDbOnly;

    private long ckptEnd;

    private long partialCkptStart;

    private RecoveryUtilizationTracker tracker;

    private VLSNRecoveryProxy vlsnProxy;

    /**
     * Create this reader to start at a given LSN.
     */
    public INFileReader(EnvironmentImpl env, int readBufferSize, long startLsn, long finishLsn, boolean trackIds, boolean mapDbOnly, long partialCkptStart, long ckptEnd, RecoveryUtilizationTracker tracker) throws DatabaseException {
        super(env, readBufferSize, true, startLsn, null, DbLsn.NULL_LSN, finishLsn);
        this.trackIds = trackIds;
        this.mapDbOnly = mapDbOnly;
        this.ckptEnd = ckptEnd;
        targetEntryMap = new HashMap<LogEntryType, LogEntry>();
        if (trackIds) {
            maxNodeId = 0;
            maxDbId = 0;
            maxTxnId = 0;
            minReplicatedNodeId = 0;
            minReplicatedDbId = DbTree.NEG_DB_ID_START;
            minReplicatedTxnId = 0;
            this.tracker = tracker;
            this.partialCkptStart = partialCkptStart;
            dbIdTrackingMap = new HashMap<LogEntryType, LogEntry>();
            txnIdTrackingMap = new HashMap<LogEntryType, LogEntry>();
            otherNodeTrackingMap = new HashMap<LogEntryType, NodeLogEntry>();
            dbIdTrackingMap.put(LogEntryType.LOG_MAPLN_TRANSACTIONAL, LogEntryType.LOG_MAPLN_TRANSACTIONAL.getNewLogEntry());
            dbIdTrackingMap.put(LogEntryType.LOG_MAPLN, LogEntryType.LOG_MAPLN.getNewLogEntry());
            txnIdTrackingMap.put(LogEntryType.LOG_LN_TRANSACTIONAL, LogEntryType.LOG_LN_TRANSACTIONAL.getNewLogEntry());
            txnIdTrackingMap.put(LogEntryType.LOG_MAPLN_TRANSACTIONAL, LogEntryType.LOG_MAPLN_TRANSACTIONAL.getNewLogEntry());
            txnIdTrackingMap.put(LogEntryType.LOG_NAMELN_TRANSACTIONAL, LogEntryType.LOG_NAMELN_TRANSACTIONAL.getNewLogEntry());
            txnIdTrackingMap.put(LogEntryType.LOG_DEL_DUPLN_TRANSACTIONAL, LogEntryType.LOG_DEL_DUPLN_TRANSACTIONAL.getNewLogEntry());
            txnIdTrackingMap.put(LogEntryType.LOG_DUPCOUNTLN_TRANSACTIONAL, LogEntryType.LOG_DUPCOUNTLN_TRANSACTIONAL.getNewLogEntry());
            vlsnProxy = envImpl.getVLSNProxy();
            addTargetType(LogEntryType.LOG_ROLLBACK_START);
        }
    }

    /**
     * Configure this reader to target this kind of entry.
     */
    public void addTargetType(LogEntryType entryType) throws DatabaseException {
        targetEntryMap.put(entryType, entryType.getNewLogEntry());
    }

    /**
     * If we're tracking node, database and txn ids, we want to see all node
     * log entries. If not, we only want to see IN entries.
     * @return true if this is an IN entry.
     */
    @Override
    protected boolean isTargetEntry() throws DatabaseException {
        lastEntryWasDelete = false;
        lastEntryWasDupDelete = false;
        targetLogEntry = null;
        dbIdTrackingEntry = null;
        txnIdTrackingEntry = null;
        nodeTrackingEntry = null;
        inTrackingEntry = null;
        fsTrackingEntry = null;
        isProvisional = currentEntryHeader.getProvisional().isProvisional(getLastLsn(), ckptEnd);
        fromLogType = LogEntryType.findType(currentEntryHeader.getType());
        LogEntry possibleTarget = targetEntryMap.get(fromLogType);
        if (!isProvisional) {
            targetLogEntry = possibleTarget;
        }
        if (LogEntryType.LOG_IN_DELETE_INFO.equals(fromLogType)) {
            lastEntryWasDelete = true;
        }
        if (LogEntryType.LOG_IN_DUPDELETE_INFO.equals(fromLogType)) {
            lastEntryWasDupDelete = true;
        }
        if (!trackIds) {
            return (targetLogEntry != null);
        }
        if (!isProvisional) {
            dbIdTrackingEntry = (LNLogEntry) dbIdTrackingMap.get(fromLogType);
            txnIdTrackingEntry = (LNLogEntry) txnIdTrackingMap.get(fromLogType);
        }
        if (fromLogType.isNodeType()) {
            if (possibleTarget != null) {
                nodeTrackingEntry = (NodeLogEntry) possibleTarget;
            } else if (dbIdTrackingEntry != null) {
                nodeTrackingEntry = dbIdTrackingEntry;
            } else if (txnIdTrackingEntry != null) {
                nodeTrackingEntry = txnIdTrackingEntry;
            } else {
                nodeTrackingEntry = otherNodeTrackingMap.get(fromLogType);
                if (nodeTrackingEntry == null) {
                    nodeTrackingEntry = (NodeLogEntry) fromLogType.getNewLogEntry();
                    otherNodeTrackingMap.put(fromLogType, nodeTrackingEntry);
                }
            }
            if (nodeTrackingEntry instanceof INLogEntry) {
                inTrackingEntry = (INLogEntry) nodeTrackingEntry;
            }
            if (LogEntryType.LOG_FILESUMMARYLN.equals(fromLogType)) {
                fsTrackingEntry = (LNLogEntry) nodeTrackingEntry;
            }
        } else {
            if (!LogEntryType.LOG_FILE_HEADER.equals(fromLogType)) {
                tracker.countNewLogEntry(getLastLsn(), fromLogType, currentEntryHeader.getSize() + currentEntryHeader.getItemSize(), null);
            }
            if (LogEntryType.LOG_ROOT.equals(fromLogType)) {
                tracker.saveLastLoggedMapLN(DbTree.ID_DB_ID, getLastLsn());
                tracker.saveLastLoggedMapLN(DbTree.NAME_DB_ID, getLastLsn());
                tracker.resetDbInfo(DbTree.ID_DB_ID);
                tracker.resetDbInfo(DbTree.NAME_DB_ID);
            }
        }
        return (targetLogEntry != null) || (dbIdTrackingEntry != null) || (txnIdTrackingEntry != null) || (nodeTrackingEntry != null) || currentEntryHeader.getReplicated();
    }

    /**
     * This reader returns non-provisional INs and IN delete entries.
     * In tracking mode, it may also scan log entries that aren't returned:
     *  -to set the sequences for txn, node, and database id.
     *  -to update utilization and obsolete offset information.
     *  -for VLSN mappings for recovery
     */
    protected boolean processEntry(ByteBuffer entryBuffer) throws DatabaseException {
        boolean useEntry = false;
        boolean entryLoaded = false;
        if (targetLogEntry != null) {
            targetLogEntry.readEntry(currentEntryHeader, entryBuffer, true);
            entryLoaded = true;
            if (currentEntryHeader.getType() != LogEntryType.LOG_ROLLBACK_START.getTypeNum()) {
                DatabaseId dbId = getDatabaseId();
                boolean isMapDb = dbId.equals(DbTree.ID_DB_ID);
                useEntry = (!mapDbOnly || isMapDb);
            }
        }
        if (!trackIds) {
            return useEntry;
        }
        DatabaseId dbIdToReset = null;
        long fileNumToReset = -1;
        LNLogEntry lnEntry = null;
        if (dbIdTrackingEntry != null) {
            lnEntry = dbIdTrackingEntry;
            lnEntry.readEntry(currentEntryHeader, entryBuffer, true);
            entryLoaded = true;
            MapLN mapLN = (MapLN) lnEntry.getMainItem();
            DatabaseId dbId = mapLN.getDatabase().getId();
            int dbIdVal = dbId.getId();
            maxDbId = (dbIdVal > maxDbId) ? dbIdVal : maxDbId;
            minReplicatedDbId = (dbIdVal < minReplicatedDbId) ? dbIdVal : minReplicatedDbId;
            dbIdToReset = dbId;
            tracker.saveLastLoggedMapLN(dbId, getLastLsn());
        }
        if (txnIdTrackingEntry != null) {
            if (lnEntry == null) {
                lnEntry = txnIdTrackingEntry;
                lnEntry.readEntry(currentEntryHeader, entryBuffer, false);
                entryLoaded = true;
            }
            long txnId = lnEntry.getTxnId().longValue();
            maxTxnId = (txnId > maxTxnId) ? txnId : maxTxnId;
            minReplicatedTxnId = (txnId < minReplicatedTxnId) ? txnId : minReplicatedTxnId;
        }
        if (fsTrackingEntry != null) {
            if (!entryLoaded) {
                nodeTrackingEntry.readEntry(currentEntryHeader, entryBuffer, true);
                entryLoaded = true;
            }
            byte[] keyBytes = fsTrackingEntry.getKey();
            FileSummaryLN fsln = (FileSummaryLN) fsTrackingEntry.getMainItem();
            long fileNum = fsln.getFileNumber(keyBytes);
            fileNumToReset = fileNum;
            tracker.saveLastLoggedFileSummaryLN(fileNum, getLastLsn());
        }
        if (nodeTrackingEntry != null) {
            if (!entryLoaded) {
                nodeTrackingEntry.readEntry(currentEntryHeader, entryBuffer, false);
                entryLoaded = true;
            }
            long nodeId = nodeTrackingEntry.getNodeId();
            maxNodeId = (nodeId > maxNodeId) ? nodeId : maxNodeId;
            minReplicatedNodeId = (nodeId < minReplicatedNodeId) ? nodeId : minReplicatedNodeId;
            tracker.countNewLogEntry(getLastLsn(), fromLogType, currentEntryHeader.getSize() + currentEntryHeader.getItemSize(), nodeTrackingEntry.getDbId());
        }
        if (inTrackingEntry != null) {
            assert entryLoaded : "All nodes should have been loaded";
            long oldLsn = inTrackingEntry.getObsoleteLsn();
            if (oldLsn != DbLsn.NULL_LSN) {
                long newLsn = getLastLsn();
                tracker.countObsoleteIfUncounted(oldLsn, newLsn, fromLogType, 0, inTrackingEntry.getDbId(), false);
            }
            if (isProvisional && partialCkptStart != DbLsn.NULL_LSN) {
                oldLsn = getLastLsn();
                if (DbLsn.compareTo(partialCkptStart, oldLsn) < 0) {
                    tracker.countObsoleteUnconditional(oldLsn, fromLogType, 0, inTrackingEntry.getDbId(), false);
                }
            }
        }
        if (fileNumToReset != -1) {
            tracker.resetFileInfo(fileNumToReset);
        }
        if (dbIdToReset != null) {
            tracker.resetDbInfo(dbIdToReset);
        }
        vlsnProxy.trackMapping(getLastLsn(), currentEntryHeader, targetLogEntry);
        if (!entryLoaded) {
            int endPosition = threadSafeBufferPosition(entryBuffer) + currentEntryHeader.getItemSize();
            threadSafeBufferPosition(entryBuffer, endPosition);
        }
        return useEntry;
    }

    /**
     * Get the last IN seen by the reader.
     */
    public IN getIN() throws DatabaseException {
        return ((INContainingEntry) targetLogEntry).getIN(envImpl);
    }

    /**
     * Get the last databaseId seen by the reader.
     */
    public DatabaseId getDatabaseId() {
        if (lastEntryWasDelete) {
            return ((INDeleteInfo) targetLogEntry.getMainItem()).getDatabaseId();
        } else if (lastEntryWasDupDelete) {
            return ((INDupDeleteInfo) targetLogEntry.getMainItem()).getDatabaseId();
        } else {
            return ((INContainingEntry) targetLogEntry).getDbId();
        }
    }

    /**
     * Get the maximum node id seen by the reader.
     */
    public long getMaxNodeId() {
        return maxNodeId;
    }

    public long getMinReplicatedNodeId() {
        return minReplicatedNodeId;
    }

    /**
     * Get the maximum db id seen by the reader.
     */
    public int getMaxDbId() {
        return maxDbId;
    }

    public int getMinReplicatedDbId() {
        return minReplicatedDbId;
    }

    /**
     * Get the maximum txn id seen by the reader.
     */
    public long getMaxTxnId() {
        return maxTxnId;
    }

    public long getMinReplicatedTxnId() {
        return minReplicatedTxnId;
    }

    /**
     * @return true if the last entry was a delete info entry.
     */
    public boolean isDeleteInfo() {
        return lastEntryWasDelete;
    }

    /**
     * @return true if the last entry was a dup delete info entry.
     */
    public boolean isDupDeleteInfo() {
        return lastEntryWasDupDelete;
    }

    /**
     * Get the deleted node id stored in the last delete info log entry.
     */
    public long getDeletedNodeId() {
        return ((INDeleteInfo) targetLogEntry.getMainItem()).getDeletedNodeId();
    }

    /**
     * Get the deleted id key stored in the last delete info log entry.
     */
    public byte[] getDeletedIdKey() {
        return ((INDeleteInfo) targetLogEntry.getMainItem()).getDeletedIdKey();
    }

    /**
     * Get the deleted node id stored in the last delete info log entry.
     */
    public long getDupDeletedNodeId() {
        return ((INDupDeleteInfo) targetLogEntry.getMainItem()).getDeletedNodeId();
    }

    /**
     * Get the deleted main key stored in the last delete info log entry.
     */
    public byte[] getDupDeletedMainKey() {
        return ((INDupDeleteInfo) targetLogEntry.getMainItem()).getDeletedMainKey();
    }

    /**
     * Get the deleted main key stored in the last delete info log entry.
     */
    public byte[] getDupDeletedDupKey() {
        return ((INDupDeleteInfo) targetLogEntry.getMainItem()).getDeletedDupKey();
    }

    /**
     * Get the LSN that should represent this IN. For most INs, it's the LSN
     * that was just read. For BINDelta entries, it's the LSN of the last
     * full version.
     */
    public long getLsnOfIN() {
        return ((INContainingEntry) targetLogEntry).getLsnOfIN(getLastLsn());
    }

    public VLSNRecoveryProxy getVLSNProxy() {
        return vlsnProxy;
    }
}
