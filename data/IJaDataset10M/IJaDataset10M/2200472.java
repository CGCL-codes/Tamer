package org.motiv.tests.config;

import org.motiv.config.CacheConfiguration;

/**
 * A cache configuration factory class.
 * @author Pavlov Dm
 */
public final class CacheConfigurationFactory {

    public static final String EXPECTED_NAME = "test_cache";

    public static final int EXPECTED_MAX_ELEMENTS_IN_MEMORY = 10;

    public static final int EXPECTED_MAX_ELEMENTS_ON_DISK = 5;

    public static final boolean EXPECTED_ETERNAL = true;

    public static final long EXPECTED_TIME_TO_IDLE_SECONDS = 0;

    public static final long EXPECTED_TIME_TO_LIVE_SECONDS = 0;

    public static final boolean EXPECTED_OVERFLOW_TO_DISK = true;

    public static final int EXPECTED_DISK_SPOOL_BUFFER_SIZE_MB = 30;

    public static final String EXPECTED_MEMORY_STORE_EVICTION_POLICY = "LRU";

    /**
     * Constructor.
     */
    private CacheConfigurationFactory() {
    }

    /**
     * Create cache configuration object.
     */
    public static CacheConfiguration create() {
        CacheConfiguration configuration = new CacheConfiguration(EXPECTED_NAME, EXPECTED_MAX_ELEMENTS_IN_MEMORY);
        configuration.setEternal(EXPECTED_ETERNAL);
        configuration.setTimeToIdleSeconds(EXPECTED_TIME_TO_IDLE_SECONDS);
        configuration.setTimeToLiveSeconds(EXPECTED_TIME_TO_LIVE_SECONDS);
        configuration.setOverflowToDisk(EXPECTED_OVERFLOW_TO_DISK);
        configuration.setMaxElementsOnDisk(EXPECTED_MAX_ELEMENTS_ON_DISK);
        configuration.setDiskSpoolBufferSizeMB(EXPECTED_DISK_SPOOL_BUFFER_SIZE_MB);
        configuration.setMemoryStoreEvictionPolicy(EXPECTED_MEMORY_STORE_EVICTION_POLICY);
        return configuration;
    }
}
