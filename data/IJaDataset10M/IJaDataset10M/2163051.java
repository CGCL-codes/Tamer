package com.codestudio;

public interface PoolManConstants {

    public static final String RELEASE_NAME = "PoolMan Resource Management Library";

    public static final String RELEASE_MAJOR_VERSION = "2";

    public static final String RELEASE_MINOR_VERSION = "1";

    public static final String RELEASE_FULL_NAME = RELEASE_NAME + " v" + RELEASE_MAJOR_VERSION + "." + RELEASE_MINOR_VERSION;

    public static final String XML_CONFIG_FILE = "poolman.xml";

    public static final String PROPS_CONFIG_FILE = "poolman.props";

    public static final String OLDPROPS_CONFIG_FILE = "pool.props";

    public static final String DATASOURCE_SVC_CLASSNAME = "com.codestudio.management.DataSourceService";

    public static final String GENERIC_SVC_CLASSNAME = "com.codestudio.management.GenericPoolService";

    public static final boolean DEFAULT_USE_JMX = false;

    public static final int DEFAULT_INITIAL_SIZE = 1;

    public static final int DEFAULT_TIMEOUT = 1200;

    public static final int DEFAULT_SKIMMER_SLEEP = 420;

    public static final int DEFAULT_MAX_SIZE = Integer.MAX_VALUE;

    public static final int DEFAULT_MIN_SIZE = 0;

    public static final int DEFAULT_USERTIMEOUT = 20;

    public static final int DEFAULT_SHRINKBY = 5;

    public static final int DEFAULT_ISO_LEVEL = java.sql.Connection.TRANSACTION_READ_COMMITTED;

    public static final boolean DEFAULT_EMERGENCY_CREATES = true;

    public static final boolean DEFAULT_POOL_PREP_STATEMENTS = true;

    public static final boolean DEFAULT_REMOVE_ON_EXC = false;

    public static final boolean DEFAULT_CACHE_ENABLED = false;

    public static final int DEFAULT_CACHE_SIZE = 5;

    public static final int DEFAULT_CACHE_REFRESH = 30;
}
