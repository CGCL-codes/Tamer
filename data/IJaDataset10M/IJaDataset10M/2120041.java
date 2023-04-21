package com.sleepycat.je.config;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import com.sleepycat.je.Durability;
import com.sleepycat.je.EnvironmentConfig;
import com.sleepycat.je.dbi.EnvironmentImpl;

/**
 */
public class EnvironmentParams {

    public static final Map<String, ConfigParam> SUPPORTED_PARAMS = new HashMap<String, ConfigParam>();

    public static final LongConfigParam MAX_MEMORY = new LongConfigParam(EnvironmentConfig.MAX_MEMORY, null, null, Long.valueOf(0), true, false);

    public static final IntConfigParam MAX_MEMORY_PERCENT = new IntConfigParam(EnvironmentConfig.MAX_MEMORY_PERCENT, Integer.valueOf(1), Integer.valueOf(90), Integer.valueOf(60), true, false);

    public static final BooleanConfigParam ENV_SHARED_CACHE = new BooleanConfigParam(EnvironmentConfig.SHARED_CACHE, false, false, false);

    /**
     * Used by utilities, not exposed in the API.
     *
     * If true, an environment is created with recovery and the related daemon
     * threads are enabled.
     */
    public static final BooleanConfigParam ENV_RECOVERY = new BooleanConfigParam("je.env.recovery", true, false, false);

    public static final BooleanConfigParam ENV_RECOVERY_FORCE_CHECKPOINT = new BooleanConfigParam(EnvironmentConfig.ENV_RECOVERY_FORCE_CHECKPOINT, false, false, false);

    public static final BooleanConfigParam ENV_RUN_INCOMPRESSOR = new BooleanConfigParam(EnvironmentConfig.ENV_RUN_IN_COMPRESSOR, true, true, false);

    /**
     * As of 2.0, eviction is performed in-line.
     *
     * If true, starts up the evictor.  This parameter is false by default.
     */
    public static final BooleanConfigParam ENV_RUN_EVICTOR = new BooleanConfigParam("je.env.runEvictor", false, true, false);

    public static final DurationConfigParam EVICTOR_WAKEUP_INTERVAL = new DurationConfigParam("je.evictor.wakeupInterval", "1 s", "75 min", "5 s", false, false);

    public static final BooleanConfigParam ENV_RUN_CHECKPOINTER = new BooleanConfigParam(EnvironmentConfig.ENV_RUN_CHECKPOINTER, true, true, false);

    public static final BooleanConfigParam ENV_RUN_CLEANER = new BooleanConfigParam(EnvironmentConfig.ENV_RUN_CLEANER, true, true, false);

    public static final IntConfigParam ENV_BACKGROUND_READ_LIMIT = new IntConfigParam(EnvironmentConfig.ENV_BACKGROUND_READ_LIMIT, Integer.valueOf(0), Integer.valueOf(Integer.MAX_VALUE), Integer.valueOf(0), true, false);

    public static final IntConfigParam ENV_BACKGROUND_WRITE_LIMIT = new IntConfigParam(EnvironmentConfig.ENV_BACKGROUND_WRITE_LIMIT, Integer.valueOf(0), Integer.valueOf(Integer.MAX_VALUE), Integer.valueOf(0), true, false);

    public static final DurationConfigParam ENV_BACKGROUND_SLEEP_INTERVAL = new DurationConfigParam(EnvironmentConfig.ENV_BACKGROUND_SLEEP_INTERVAL, "1 ms", null, "1 ms", true, false);

    public static final BooleanConfigParam ENV_CHECK_LEAKS = new BooleanConfigParam(EnvironmentConfig.ENV_CHECK_LEAKS, true, false, false);

    public static final BooleanConfigParam ENV_FORCED_YIELD = new BooleanConfigParam(EnvironmentConfig.ENV_FORCED_YIELD, false, false, false);

    public static final BooleanConfigParam ENV_INIT_TXN = new BooleanConfigParam(EnvironmentConfig.ENV_IS_TRANSACTIONAL, false, false, false);

    public static final BooleanConfigParam ENV_INIT_LOCKING = new BooleanConfigParam(EnvironmentConfig.ENV_IS_LOCKING, true, false, false);

    public static final BooleanConfigParam ENV_RDONLY = new BooleanConfigParam(EnvironmentConfig.ENV_READ_ONLY, false, false, false);

    public static final BooleanConfigParam ENV_FAIR_LATCHES = new BooleanConfigParam(EnvironmentConfig.ENV_FAIR_LATCHES, false, false, false);

    /**
     * Not part of the public API. As of 3.3, is true by default.
     *
     * If true (the default), use shared latches for Btree Internal Nodes (INs)
     * to improve concurrency.
     */
    public static final BooleanConfigParam ENV_SHARED_LATCHES = new BooleanConfigParam("je.env.sharedLatches", true, false, false);

    public static final BooleanConfigParam ENV_DB_EVICTION = new BooleanConfigParam(EnvironmentConfig.ENV_DB_EVICTION, true, false, false);

    public static final IntConfigParam ADLER32_CHUNK_SIZE = new IntConfigParam(EnvironmentConfig.ADLER32_CHUNK_SIZE, Integer.valueOf(0), Integer.valueOf(1 << 20), Integer.valueOf(0), true, false);

    public static final int MIN_LOG_BUFFER_SIZE = 2048;

    public static final int NUM_LOG_BUFFERS_DEFAULT = 3;

    public static final long LOG_MEM_SIZE_MIN = NUM_LOG_BUFFERS_DEFAULT * MIN_LOG_BUFFER_SIZE;

    public static final String LOG_MEM_SIZE_MIN_STRING = Long.toString(LOG_MEM_SIZE_MIN);

    public static final LongConfigParam LOG_MEM_SIZE = new LongConfigParam(EnvironmentConfig.LOG_TOTAL_BUFFER_BYTES, Long.valueOf(LOG_MEM_SIZE_MIN), null, Long.valueOf(0), false, false);

    public static final IntConfigParam NUM_LOG_BUFFERS = new IntConfigParam(EnvironmentConfig.LOG_NUM_BUFFERS, Integer.valueOf(2), null, Integer.valueOf(NUM_LOG_BUFFERS_DEFAULT), false, false);

    public static final IntConfigParam LOG_BUFFER_MAX_SIZE = new IntConfigParam(EnvironmentConfig.LOG_BUFFER_SIZE, Integer.valueOf(1 << 10), null, Integer.valueOf(1 << 20), false, false);

    public static final IntConfigParam LOG_FAULT_READ_SIZE = new IntConfigParam(EnvironmentConfig.LOG_FAULT_READ_SIZE, Integer.valueOf(32), null, Integer.valueOf(2048), false, false);

    public static final IntConfigParam LOG_ITERATOR_READ_SIZE = new IntConfigParam(EnvironmentConfig.LOG_ITERATOR_READ_SIZE, Integer.valueOf(128), null, Integer.valueOf(8192), false, false);

    public static final IntConfigParam LOG_ITERATOR_MAX_SIZE = new IntConfigParam(EnvironmentConfig.LOG_ITERATOR_MAX_SIZE, Integer.valueOf(128), null, Integer.valueOf(16777216), false, false);

    public static final LongConfigParam LOG_FILE_MAX = (EnvironmentImpl.IS_DALVIK ? new LongConfigParam(EnvironmentConfig.LOG_FILE_MAX, Long.valueOf(10000), Long.valueOf(10000000), Long.valueOf(100000), false, false) : new LongConfigParam(EnvironmentConfig.LOG_FILE_MAX, Long.valueOf(1000000), Long.valueOf(1073741824L), Long.valueOf(10000000), false, false));

    public static final BooleanConfigParam LOG_CHECKSUM_READ = new BooleanConfigParam(EnvironmentConfig.LOG_CHECKSUM_READ, true, false, false);

    public static final BooleanConfigParam LOG_VERIFY_CHECKSUMS = new BooleanConfigParam(EnvironmentConfig.LOG_VERIFY_CHECKSUMS, false, false, false);

    public static final BooleanConfigParam LOG_MEMORY_ONLY = new BooleanConfigParam(EnvironmentConfig.LOG_MEM_ONLY, false, false, false);

    public static final IntConfigParam LOG_FILE_CACHE_SIZE = new IntConfigParam(EnvironmentConfig.LOG_FILE_CACHE_SIZE, Integer.valueOf(3), null, Integer.valueOf(100), false, false);

    public static final DurationConfigParam LOG_FSYNC_TIMEOUT = new DurationConfigParam(EnvironmentConfig.LOG_FSYNC_TIMEOUT, "10 ms", null, "500 ms", false, false);

    public static final BooleanConfigParam LOG_USE_ODSYNC = new BooleanConfigParam(EnvironmentConfig.LOG_USE_ODSYNC, false, false, false);

    public static final BooleanConfigParam LOG_USE_NIO = new BooleanConfigParam(EnvironmentConfig.LOG_USE_NIO, false, false, false);

    public static final BooleanConfigParam LOG_USE_WRITE_QUEUE = (EnvironmentImpl.IS_DALVIK ? new BooleanConfigParam(EnvironmentConfig.LOG_USE_WRITE_QUEUE, false, false, false) : new BooleanConfigParam(EnvironmentConfig.LOG_USE_WRITE_QUEUE, true, false, false));

    public static final IntConfigParam LOG_WRITE_QUEUE_SIZE = new IntConfigParam(EnvironmentConfig.LOG_WRITE_QUEUE_SIZE, Integer.valueOf(1 << 12), Integer.valueOf(1 << 25), Integer.valueOf(1 << 20), false, false);

    public static final BooleanConfigParam LOG_DIRECT_NIO = new BooleanConfigParam(EnvironmentConfig.LOG_DIRECT_NIO, false, false, false);

    public static final LongConfigParam LOG_CHUNKED_NIO = new LongConfigParam(EnvironmentConfig.LOG_CHUNKED_NIO, Long.valueOf(0L), Long.valueOf(1 << 26), Long.valueOf(0L), false, false);

    /**
     * @deprecated As of 3.3, no longer used
     *
     * Optimize cleaner operation for temporary deferred write DBs.
     */
    public static final BooleanConfigParam LOG_DEFERREDWRITE_TEMP = new BooleanConfigParam("je.deferredWrite.temp", false, false, false);

    public static final IntConfigParam NODE_MAX = new IntConfigParam(EnvironmentConfig.NODE_MAX_ENTRIES, Integer.valueOf(4), Integer.valueOf(32767), Integer.valueOf(128), false, false);

    public static final IntConfigParam NODE_MAX_DUPTREE = new IntConfigParam(EnvironmentConfig.NODE_DUP_TREE_MAX_ENTRIES, Integer.valueOf(4), Integer.valueOf(32767), Integer.valueOf(128), false, false);

    public static final IntConfigParam BIN_MAX_DELTAS = new IntConfigParam(EnvironmentConfig.TREE_MAX_DELTA, Integer.valueOf(0), Integer.valueOf(100), Integer.valueOf(10), false, false);

    public static final IntConfigParam BIN_DELTA_PERCENT = new IntConfigParam(EnvironmentConfig.TREE_BIN_DELTA, Integer.valueOf(0), Integer.valueOf(75), Integer.valueOf(25), false, false);

    public static final LongConfigParam MIN_TREE_MEMORY = new LongConfigParam(EnvironmentConfig.TREE_MIN_MEMORY, Long.valueOf(50 * 1024), null, Long.valueOf(500 * 1024), true, false);

    public static final DurationConfigParam COMPRESSOR_WAKEUP_INTERVAL = new DurationConfigParam(EnvironmentConfig.COMPRESSOR_WAKEUP_INTERVAL, "1 s", "75 min", "5 s", false, false);

    public static final IntConfigParam COMPRESSOR_RETRY = new IntConfigParam(EnvironmentConfig.COMPRESSOR_DEADLOCK_RETRY, Integer.valueOf(0), Integer.valueOf(Integer.MAX_VALUE), Integer.valueOf(3), false, false);

    public static final DurationConfigParam COMPRESSOR_LOCK_TIMEOUT = new DurationConfigParam(EnvironmentConfig.COMPRESSOR_LOCK_TIMEOUT, null, "75 min", "500 ms", false, false);

    public static final LongConfigParam EVICTOR_EVICT_BYTES = new LongConfigParam(EnvironmentConfig.EVICTOR_EVICT_BYTES, Long.valueOf(1024), null, Long.valueOf(524288), false, false);

    /**
     * @deprecated As of 2.0, this is replaced by je.evictor.evictBytes
     *
     * When eviction happens, the evictor will push memory usage to this
     * percentage of je.maxMemory.
     */
    public static final IntConfigParam EVICTOR_USEMEM_FLOOR = new IntConfigParam("je.evictor.useMemoryFloor", Integer.valueOf(50), Integer.valueOf(100), Integer.valueOf(95), false, false);

    /**
     * @deprecated As of 1.7.2, this is replaced by je.evictor.nodesPerScan
     *
     * The evictor percentage of total nodes to scan per wakeup.
     */
    public static final IntConfigParam EVICTOR_NODE_SCAN_PERCENTAGE = new IntConfigParam("je.evictor.nodeScanPercentage", Integer.valueOf(1), Integer.valueOf(100), Integer.valueOf(10), false, false);

    /**
     * @deprecated As of 1.7.2, 1 node is chosen per scan.
     *
     * The evictor percentage of scanned nodes to evict per wakeup.
     */
    public static final IntConfigParam EVICTOR_EVICTION_BATCH_PERCENTAGE = new IntConfigParam("je.evictor.evictionBatchPercentage", Integer.valueOf(1), Integer.valueOf(100), Integer.valueOf(10), false, false);

    public static final IntConfigParam EVICTOR_NODES_PER_SCAN = new IntConfigParam(EnvironmentConfig.EVICTOR_NODES_PER_SCAN, Integer.valueOf(1), Integer.valueOf(1000), Integer.valueOf(10), false, false);

    /**
     * Not part of public API. As of 2.0, eviction is performed in-line.
     *
     * At this percentage over the allotted cache, critical eviction will
     * start.
     */
    public static final IntConfigParam EVICTOR_CRITICAL_PERCENTAGE = new IntConfigParam("je.evictor.criticalPercentage", Integer.valueOf(0), Integer.valueOf(1000), Integer.valueOf(0), false, false);

    public static final IntConfigParam EVICTOR_RETRY = new IntConfigParam(EnvironmentConfig.EVICTOR_DEADLOCK_RETRY, Integer.valueOf(0), Integer.valueOf(Integer.MAX_VALUE), Integer.valueOf(3), false, false);

    public static final BooleanConfigParam EVICTOR_LRU_ONLY = new BooleanConfigParam(EnvironmentConfig.EVICTOR_LRU_ONLY, true, false, false);

    public static final BooleanConfigParam EVICTOR_FORCED_YIELD = new BooleanConfigParam(EnvironmentConfig.EVICTOR_FORCED_YIELD, false, false, false);

    public static final LongConfigParam CHECKPOINTER_BYTES_INTERVAL = new LongConfigParam(EnvironmentConfig.CHECKPOINTER_BYTES_INTERVAL, Long.valueOf(0), Long.valueOf(Long.MAX_VALUE), (EnvironmentImpl.IS_DALVIK ? Long.valueOf(200000) : Long.valueOf(20000000)), false, false);

    public static final DurationConfigParam CHECKPOINTER_WAKEUP_INTERVAL = new DurationConfigParam(EnvironmentConfig.CHECKPOINTER_WAKEUP_INTERVAL, "1 s", "75 min", "0", false, false);

    public static final IntConfigParam CHECKPOINTER_RETRY = new IntConfigParam(EnvironmentConfig.CHECKPOINTER_DEADLOCK_RETRY, Integer.valueOf(0), Integer.valueOf(Integer.MAX_VALUE), Integer.valueOf(3), false, false);

    public static final BooleanConfigParam CHECKPOINTER_HIGH_PRIORITY = new BooleanConfigParam(EnvironmentConfig.CHECKPOINTER_HIGH_PRIORITY, false, true, false);

    public static final IntConfigParam CLEANER_MIN_UTILIZATION = new IntConfigParam(EnvironmentConfig.CLEANER_MIN_UTILIZATION, Integer.valueOf(0), Integer.valueOf(90), Integer.valueOf(50), true, false);

    public static final IntConfigParam CLEANER_MIN_FILE_UTILIZATION = new IntConfigParam(EnvironmentConfig.CLEANER_MIN_FILE_UTILIZATION, Integer.valueOf(0), Integer.valueOf(50), Integer.valueOf(5), true, false);

    public static final LongConfigParam CLEANER_BYTES_INTERVAL = new LongConfigParam(EnvironmentConfig.CLEANER_BYTES_INTERVAL, Long.valueOf(0), Long.valueOf(Long.MAX_VALUE), Long.valueOf(0), true, false);

    public static final BooleanConfigParam CLEANER_FETCH_OBSOLETE_SIZE = new BooleanConfigParam(EnvironmentConfig.CLEANER_FETCH_OBSOLETE_SIZE, false, true, false);

    public static final IntConfigParam CLEANER_DEADLOCK_RETRY = new IntConfigParam(EnvironmentConfig.CLEANER_DEADLOCK_RETRY, Integer.valueOf(0), Integer.valueOf(Integer.MAX_VALUE), Integer.valueOf(3), true, false);

    public static final DurationConfigParam CLEANER_LOCK_TIMEOUT = new DurationConfigParam(EnvironmentConfig.CLEANER_LOCK_TIMEOUT, "0", "75 min", "500 ms", true, false);

    public static final BooleanConfigParam CLEANER_REMOVE = new BooleanConfigParam(EnvironmentConfig.CLEANER_EXPUNGE, true, true, false);

    /**
     * @deprecated As of 1.7.1, no longer used.
     */
    public static final IntConfigParam CLEANER_MIN_FILES_TO_DELETE = new IntConfigParam("je.cleaner.minFilesToDelete", Integer.valueOf(1), Integer.valueOf(1000000), Integer.valueOf(5), false, false);

    /**
     * @deprecated As of 2.0, no longer used.
     */
    public static final IntConfigParam CLEANER_RETRIES = new IntConfigParam("je.cleaner.retries", Integer.valueOf(0), Integer.valueOf(1000), Integer.valueOf(10), false, false);

    /**
     * @deprecated As of 2.0, no longer used.
     */
    public static final IntConfigParam CLEANER_RESTART_RETRIES = new IntConfigParam("je.cleaner.restartRetries", Integer.valueOf(0), Integer.valueOf(1000), Integer.valueOf(5), false, false);

    public static final IntConfigParam CLEANER_MIN_AGE = new IntConfigParam(EnvironmentConfig.CLEANER_MIN_AGE, Integer.valueOf(1), Integer.valueOf(1000), Integer.valueOf(2), true, false);

    /**
     * Experimental and may be removed in a future release -- not exposed in
     * the public API.
     *
     * If true, eviction and checkpointing will cluster records by key
     * value, migrating them from low utilization files if they are
     * resident.
     * The cluster and clusterAll properties may not both be set to true.
     */
    public static final BooleanConfigParam CLEANER_CLUSTER = new BooleanConfigParam("je.cleaner.cluster", false, true, false);

    /**
     * Experimental and may be removed in a future release -- not exposed in
     * the public API.
     *
     * If true, eviction and checkpointing will cluster records by key
     * value, migrating them from low utilization files whether or not
     * they are resident.
     * The cluster and clusterAll properties may not both be set to true.
     */
    public static final BooleanConfigParam CLEANER_CLUSTER_ALL = new BooleanConfigParam("je.cleaner.clusterAll", false, true, false);

    public static final IntConfigParam CLEANER_MAX_BATCH_FILES = new IntConfigParam(EnvironmentConfig.CLEANER_MAX_BATCH_FILES, Integer.valueOf(0), Integer.valueOf(100000), Integer.valueOf(0), true, false);

    public static final IntConfigParam CLEANER_READ_SIZE = new IntConfigParam(EnvironmentConfig.CLEANER_READ_SIZE, Integer.valueOf(128), null, Integer.valueOf(0), true, false);

    /**
     * Not part of public API.
     *
     * If true, the cleaner tracks and stores detailed information that is used
     * to decrease the cost of cleaning.
     */
    public static final BooleanConfigParam CLEANER_TRACK_DETAIL = new BooleanConfigParam("je.cleaner.trackDetail", true, false, false);

    public static final IntConfigParam CLEANER_DETAIL_MAX_MEMORY_PERCENTAGE = new IntConfigParam(EnvironmentConfig.CLEANER_DETAIL_MAX_MEMORY_PERCENTAGE, Integer.valueOf(1), Integer.valueOf(90), Integer.valueOf(2), true, false);

    /**
     * Not part of public API, since it applies to a very old bug.
     *
     * If true, detail information is discarded that was added by earlier
     * versions of JE (specifically 2.0.42 and 2.0.54) if it may be invalid.
     * This may be set to false for increased performance when those version of
     * JE were used but LockMode.RMW was never used.
     */
    public static final BooleanConfigParam CLEANER_RMW_FIX = new BooleanConfigParam("je.cleaner.rmwFix", true, false, false);

    public static final ConfigParam CLEANER_FORCE_CLEAN_FILES = new ConfigParam(EnvironmentConfig.CLEANER_FORCE_CLEAN_FILES, "", false, false);

    public static final IntConfigParam CLEANER_UPGRADE_TO_LOG_VERSION = new IntConfigParam(EnvironmentConfig.CLEANER_UPGRADE_TO_LOG_VERSION, Integer.valueOf(-1), null, Integer.valueOf(0), false, false);

    public static final IntConfigParam CLEANER_THREADS = new IntConfigParam(EnvironmentConfig.CLEANER_THREADS, Integer.valueOf(1), null, Integer.valueOf(1), true, false);

    public static final IntConfigParam CLEANER_LOOK_AHEAD_CACHE_SIZE = new IntConfigParam(EnvironmentConfig.CLEANER_LOOK_AHEAD_CACHE_SIZE, Integer.valueOf(0), null, Integer.valueOf(8192), true, false);

    public static final BooleanConfigParam CLEANER_FOREGROUND_PROACTIVE_MIGRATION = new BooleanConfigParam(EnvironmentConfig.CLEANER_FOREGROUND_PROACTIVE_MIGRATION, false, true, false);

    public static final BooleanConfigParam CLEANER_BACKGROUND_PROACTIVE_MIGRATION = new BooleanConfigParam(EnvironmentConfig.CLEANER_BACKGROUND_PROACTIVE_MIGRATION, false, true, false);

    public static final IntConfigParam N_LOCK_TABLES = new IntConfigParam(EnvironmentConfig.LOCK_N_LOCK_TABLES, Integer.valueOf(1), Integer.valueOf(32767), Integer.valueOf(1), false, false);

    public static final DurationConfigParam LOCK_TIMEOUT = new DurationConfigParam(EnvironmentConfig.LOCK_TIMEOUT, null, "75 min", "500 ms", false, false);

    public static final BooleanConfigParam LOCK_OLD_LOCK_EXCEPTIONS = new BooleanConfigParam(EnvironmentConfig.LOCK_OLD_LOCK_EXCEPTIONS, false, false, false);

    public static final DurationConfigParam TXN_TIMEOUT = new DurationConfigParam(EnvironmentConfig.TXN_TIMEOUT, null, "75 min", "0", false, false);

    public static final BooleanConfigParam TXN_SERIALIZABLE_ISOLATION = new BooleanConfigParam(EnvironmentConfig.TXN_SERIALIZABLE_ISOLATION, false, false, false);

    public static final BooleanConfigParam TXN_DEADLOCK_STACK_TRACE = new BooleanConfigParam(EnvironmentConfig.TXN_DEADLOCK_STACK_TRACE, false, true, false);

    public static final BooleanConfigParam TXN_DUMPLOCKS = new BooleanConfigParam(EnvironmentConfig.TXN_DUMP_LOCKS, false, true, false);

    public static final BooleanConfigParam JE_LOGGING_DBLOG = new BooleanConfigParam("je.env.logTrace", true, false, false);

    public static final ConfigParam JE_CONSOLE_LEVEL = new ConfigParam(EnvironmentConfig.CONSOLE_LOGGING_LEVEL, "OFF", true, false) {

        @Override
        public void validateValue(String level) throws NullPointerException, IllegalArgumentException {
            Level.parse(level);
        }
    };

    public static final ConfigParam JE_FILE_LEVEL = new ConfigParam(EnvironmentConfig.FILE_LOGGING_LEVEL, "INFO", true, false) {

        @Override
        public void validateValue(String level) throws NullPointerException, IllegalArgumentException {
            Level.parse(level);
        }
    };

    public static final ConfigParam JE_DURABILITY = new ConfigParam(EnvironmentConfig.TXN_DURABILITY, null, true, false) {

        @Override
        public void validateValue(String durabilityString) throws IllegalArgumentException {
            Durability.parse(durabilityString);
        }
    };

    public static void addSupportedParam(ConfigParam param) {
        SUPPORTED_PARAMS.put(param.getName(), param);
    }
}
