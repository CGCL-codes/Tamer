package com.versant.core.common.config;

import com.versant.core.jdo.VersantPersistenceManager;
import com.versant.core.metadata.MDStatics;
import com.versant.core.metadata.parser.JdoExtension;
import com.versant.core.metadata.parser.JdoRoot;
import com.versant.core.util.StringListParser;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import com.versant.core.common.BindingSupportImpl;
import com.versant.core.common.Utils;

/**
 * Parser for our property files. This will parse a file and return a Config
 * instance.
 */
public class ConfigParser {

    public static final String VERSANT_JDO_META_DATA = "versant.jdoMetaData";

    public static final String META_DATA_PRE_PROCESSOR = "versant.metadata.preprocessor";

    public static final String PROJECT_DESCRIPTION = "versant.workbench.projectDescription";

    public static final String PROPERTIES_FILE_MODE = "versant.workbench.propertiesFile";

    public static final String PROPERTIES_SPLIT_FILE = "versant.workbench.splitPropertiesFiles";

    public static final String SERVER = "versant.server";

    public static final String REMOTE_ACCESS = "versant.remoteAccess";

    public static final String ALLOW_REMOTE_PMS = "versant.remotePMs";

    public static final String RMI_REGISTRY_PORT = "versant.rmiRegistryPort";

    public static final String SERVER_PORT = "versant.serverPort";

    public static final String RMI_CLIENT_SF = "versant.rmiClientSocketFactory";

    public static final String RMI_CSF_IS_SSF = "versant.rmiClientSocketFactoryIsServerSF";

    public static final String RMI_SERVER_SF = "versant.rmiServerSocketFactory";

    public static final String ALLOW_PM_CLOSE_WITH_OPEN_TX = "versant.allowPmCloseWithOpenTx";

    public static final String PRECOMPILE_NAMED_QUERIES = "versant.precompileNamedQueries";

    public static final String CHECK_MODEL_CONSISTENCY_ON_COMMIT = "versant.checkModelConsistencyOnCommit";

    public static final String INTERCEPT_DFG_FIELD_ACCESS = "versant.interceptDfgFieldAccess";

    public static final String PM_CACHE_REF_TYPE = "versant.pmCacheRefType";

    public static final String PMF_CLASS = "javax.jdo.PersistenceManagerFactoryClass";

    public static final String OPTION_OPTIMISTIC = "javax.jdo.option.Optimistic";

    public static final String OPTION_RETAINVALUES = "javax.jdo.option.RetainValues";

    public static final String OPTION_RESTORE_VALUES = "javax.jdo.option.RestoreValues";

    public static final String OPTION_NON_TRANSACTIONAL_READ = "javax.jdo.option.NontransactionalRead";

    public static final String OPTION_NON_TRANSACTIONAL_WRITE = "javax.jdo.option.NontransactionalWrite";

    public static final String OPTION_MULTITHREADED = "javax.jdo.option.Multithreaded";

    public static final String OPTION_IGNORE_CACHE = "javax.jdo.option.IgnoreCache";

    public static final String OPTION_CONNECTION_FACTORY_NAME = "javax.jdo.option.ConnectionFactoryName";

    public static final String OPTION_CONNECTION_FACTORY2_NAME = "javax.jdo.option.ConnectionFactory2Name";

    public static final boolean PARALLEL_COLLECTION_FETCH_DEFAULT = true;

    public static final boolean DEFAULT_ALLOW_PM_CLOSE_WITH_OPEN_TX = false;

    public static final boolean DEFAULT_ALLOW_REMOTE_ACCESS = true;

    public static final boolean DEFAULT_ALLOW_REMOTE_PMS = true;

    public static final boolean DEFAULT_RMI_CSF_IS_SSF = false;

    public static final boolean DEFAULT_HYPERDRIVE = true;

    public static final boolean DEFAULT_PMPOOL_ENABLED = true;

    public static final boolean DEFAULT_REMOTE_PMPOOL_ENABLED = true;

    public static final boolean DEFAULT_PRECOMPILE_NAMED_QUERIES = true;

    public static final boolean DEFAULT_CHECK_MODEL_CONSISTENCY_ON_COMMIT = false;

    public static final boolean DEFAULT_ANT_DISABLED = false;

    public static final boolean DEFAULT_OPTION_OPTIMISTIC = true;

    public static final boolean DEFAULT_OPTION_RETAINVALUES = false;

    public static final boolean DEFAULT_OPTION_RESTORE_VALUES = false;

    public static final boolean DEFAULT_OPTION_IGNORE_CACHE = false;

    public static final boolean DEFAULT_OPTION_NON_TRANSACTIONAL_READ = false;

    public static final boolean DEFAULT_OPTION_NON_TRANSACTIONAL_WRITE = false;

    public static final boolean DEFAULT_OPTION_MULTITHREADED = false;

    public static final boolean DEFAULT_CACHE_ENABLED = true;

    public static final boolean DEFAULT_QUERY_CACHE_ENABLED = true;

    public static final boolean DEFAULT_ANT_SHOW_ALL_TARGETS = false;

    public static final int DEFAULT_STORE_MAX_ACTIVE = 10;

    public static final int DEFAULT_STORE_MAX_IDLE = 10;

    public static final int DEFAULT_STORE_MIN_IDLE = 2;

    public static final int DEFAULT_STORE_RESERVED = 1;

    public static final boolean DEFAULT_STORE_WAIT_FOR_CON_ON_STARTUP = false;

    public static final boolean DEFAULT_STORE_TEST_ON_ALLOC = false;

    public static final boolean DEFAULT_STORE_TEST_ON_RELEASE = false;

    public static final boolean DEFAULT_STORE_TEST_ON_EXCEPTION = true;

    public static final int DEFAULT_STORE_RETRY_INTERVAL_MS = 1000;

    public static final int DEFAULT_STORE_RETRY_COUNT = 30;

    public static final boolean DEFAULT_STORE_MANAGED_ONE_TO_MANY = false;

    public static final boolean DEFAULT_STORE_MANAGED_MANY_TO_MANY = false;

    public static final int DEFAULT_RMI_REGISTRY_PORT = 2388;

    public static final String HYPERDRIVE = "versant.hyperdrive";

    public static final String HYPERDRIVE_SRC_DIR = "versant.hyperdriveSrcDir";

    public static final String HYPERDRIVE_CLASS_DIR = "versant.hyperdriveClassDir";

    public static final String PMPOOL_ENABLED = "versant.pmpoolEnabled";

    public static final String PMPOOL_MAX_IDLE = "versant.pmpoolMaxIdle";

    public static final int DEFAULT_PMPOOL_MAX_IDLE = 8;

    public static final String REMOTE_PMPOOL_ENABLED = "versant.remotePmpoolEnabled";

    public static final String REMOTE_PMPOOL_MAX_IDLE = "versant.remotePmpoolMaxIdle";

    public static final String REMOTE_PMPOOL_MAX_ACTIVE = "versant.remotePmpoolMaxActive";

    public static final int DEFAULT_REMOTE_PMPOOL_MAX_IDLE = 2;

    public static final int DEFAULT_REMOTE_PMPOOL_MAX_ACTIVE = 5;

    public static final String FLUSH_THRESHOLD = "versant.flushThreshold";

    public static final int DEFAULT_FLUSH_THRESHOLD = 0;

    public static final String STORE_TYPE = "versant.type";

    public static final String STORE_DB = "versant.db";

    public static final String STORE_PROPERTIES = "versant.properties";

    public static final String STORE_MAX_ACTIVE = "versant.maxActive";

    public static final String STORE_MAX_IDLE = "versant.maxIdle";

    public static final String STORE_MIN_IDLE = "versant.minIdle";

    public static final String STORE_RESERVED = "versant.reserved";

    public static final String STORE_EXT = "versant.ext.";

    public static final int MAX_STORE_TYPE_MAPPING_COUNT = 100;

    public static final String STORE_TYPE_MAPPING = "versant.jdbcType.";

    public static final int MAX_STORE_JAVATYPE_MAPPING_COUNT = 100;

    public static final String STORE_JAVATYPE_MAPPING = "versant.jdbcJavaType.";

    public static final String STORE_NAMEGEN = "versant.jdbcNamegen";

    public static final String STORE_MIGRATION_CONTROLS = "versant.jdbcMigration";

    public static final String STORE_DISABLE_BATCHING = "versant.jdbcNobatching";

    public static final String STORE_DISABLE_PS_CACHE = "versant.jdbcDisablePsCache";

    public static final String STORE_PS_CACHE_MAX = "versant.psCacheMax";

    public static final String STORE_VALIDATE_SQL = "versant.validateSql";

    public static final String STORE_INIT_SQL = "versant.initSql";

    public static final String STORE_WAIT_FOR_CON_ON_STARTUP = "versant.waitForConOnStartup";

    public static final String STORE_TEST_ON_ALLOC = "versant.testOnAlloc";

    public static final String STORE_TEST_ON_RELEASE = "versant.testOnRelease";

    public static final String STORE_TEST_ON_EXCEPTION = "versant.testOnException";

    public static final String STORE_TEST_WHEN_IDLE = "versant.testWhenIdle";

    public static final String STORE_RETRY_INTERVAL_MS = "versant.retryIntervalMs";

    public static final String STORE_RETRY_COUNT = "versant.retryCount";

    public static final String STORE_VALIDATE_MAPPING_ON_STARTUP = "versant.validateMappingOnStartup";

    public static final String STORE_CON_TIMEOUT = "versant.conTimeout";

    public static final String STORE_TEST_INTERVAL = "versant.testInterval";

    public static final String STORE_ISOLATION_LEVEL = "versant.isolationLevel";

    public static final String STORE_BLOCK_WHEN_FULL = "versant.blockWhenFull";

    public static final String STORE_MAX_CON_AGE = "versant.maxConAge";

    public static final String STORE_MANAGED_ONE_TO_MANY = "versant.managedOneToMany";

    public static final String STORE_MANAGED_MANY_TO_MANY = "versant.managedManyToMany";

    public static final int MAX_STORE_SCO_FACTORY_COUNT = 100;

    public static final String STORE_SCO_FACTORY_MAPPING = "versant.scoFactoryMapping.";

    public static final String VDS_OID_BATCH_SIZE = "versant.vdsOidBatchSize";

    public static final String VDS_SCHEMA_DEFINITION = "versant.vdsSchemaDefine";

    public static final String VDS_SCHEMA_EVOLUTION = "versant.vdsSchemaEvolve";

    public static final String VDS_NAMING_POLICY = "versant.vdsNamingPolicy";

    public static final String STD_CON_DRIVER_NAME = "javax.jdo.option.ConnectionDriverName";

    public static final String STD_CON_USER_NAME = "javax.jdo.option.ConnectionUserName";

    public static final String STD_CON_PASSWORD = "javax.jdo.option.ConnectionPassword";

    public static final String STD_CON_URL = "javax.jdo.option.ConnectionURL";

    public static final String STD_CON_FACTORY_NAME = "javax.jdo.option.ConnectionFactoryName";

    public static final String STD_CON2_FACTORY_NAME = "javax.jdo.option.ConnectionFactory2Name";

    public static final String CON2_DRIVER_NAME = "versant.Connection2DriverName";

    public static final String CON2_USER_NAME = "versant.Connection2UserName";

    public static final String CON2_PASSWORD = "versant.Connection2Password";

    public static final String CON2_URL = "versant.Connection2URL";

    public static final String CON2_PROPERTIES = "versant.properties2";

    public static final int DEFAULT_MAX_CON_AGE = 1000;

    public static final String ISOLATION_LEVEL_READ_UNCOMMITTED = "READ_UNCOMMITTED";

    public static final String ISOLATION_LEVEL_READ_COMMITTED = "READ_COMMITTED";

    public static final String ISOLATION_LEVEL_REPEATABLE_READ = "REPEATABLE_READ";

    public static final String ISOLATION_LEVEL_SERIALIZABLE = "SERIALIZABLE";

    public static final int MAX_JDO_FILE_COUNT = 1000;

    public static final String JDO = "versant.metadata.";

    public static final String EVENT_LOGGING = "versant.logging";

    public static final String DATASTORE_TX_LOCKING = "versant.datastoreTxLocking";

    public static final String DATASTORE_TX_LOCKING_NONE = "LOCKING_NONE";

    public static final String DATASTORE_TX_LOCKING_FIRST = "LOCKING_FIRST";

    public static final String DATASTORE_TX_LOCKING_ALL = "LOCKING_ALL";

    public static final String RETAIN_CONNECTION_IN_OPT_TX = "versant.retainConnectionInOptTx";

    public static final String CACHE_ENABLED = "versant.l2CacheEnabled";

    public static final String CACHE_MAX_OBJECTS = "versant.l2CacheMaxObjects";

    public static final String CACHE_LISTENER = "versant.l2CacheListener";

    public static final String CACHE_CLUSTER_TRANSPORT = "versant.l2CacheClusterTransport";

    public static final String CLUSTER_JGROUPS = "JGROUPS";

    public static final String CLUSTER_TANGOSOL_COHERENCE = "TANGOSOL_COHERENCE";

    public static final String QUERY_CACHE_ENABLED = "versant.l2QueryCacheEnabled";

    public static final String QUERY_CACHE_MAX_QUERIES = "versant.l2QueryCacheMaxQueries";

    public static final String COMPILED_QUERY_CACHE_SIZE = "versant.compiledQueryCacheSize";

    public static final int DEFAULT_CACHE_MAX_OBJECTS = 10000;

    public static final int DEFAULT_CACHE_MAX_QUERIES = 1000;

    public static final int DEFAULT_METRIC_SNAPSHOT_INTERVAL_MS = 1000;

    public static final int DEFAULT_METRIC_STORE_CAPACITY = 60 * 60;

    public static final String METRIC_USER = "versant.userMetric.";

    public static final int MAX_METRIC_USER_COUNT = 100;

    public static final String METRIC_SNAPSHOT_INTERVAL_MS = "versant.metricSnapshotIntervalMs";

    public static final String METRIC_STORE_CAPACITY = "versant.metricStoreCapacity";

    public static final String LOG_DOWNLOADER = "versant.logDownloader";

    public static final String NAMING_POLICY_CLASS_NAME = "versant.namingPolicy";

    public static final String JDBC_INHERITANCE_NO_CLASSID = "jdbc-inheritance-no-classid";

    public static final String PM_CACHE_REF_TYPE_STRONG = "STRONG";

    public static final String PM_CACHE_REF_TYPE_SOFT = "SOFT";

    public static final String PM_CACHE_REF_TYPE_WEAK = "WEAK";

    public static final int MAX_EXTERNALIZER_COUNT = 100;

    public static final String EXTERNALIZER = "versant.externalizer.";

    public static final String TESTING = "versant.testing";

    public static final String MDEDIT_SRC_PATH = "versant.workbench.srcPath";

    public static final int MAX_MDEDIT_CP_COUNT = 1000;

    public static final String MDEDIT_CP = "versant.workbench.classpath.";

    public static final String ANT_DISABLED = "versant.workbench.antDisabled";

    public static final String ANT_BUILDFILE = "versant.workbench.antBuildfile";

    public static final String ANT_RUN_TARGET = "versant.workbench.antRunTarget";

    public static final String ANT_COMPILE = "versant.workbench.antCompile";

    public static final String ANT_ARGS = "versant.workbench.antArgs";

    public static final String ANT_SHOW_ALL_TARGETS = "versant.workbench.antShowAllTargets";

    public static final String SCRIPT_DIR = "versant.workbench.scriptDir";

    public static final String DIAGRAM = "versant.workbench.diagram";

    public static final String DIAGRAM_COUNT = ".count";

    public static final String DIAGRAM_CLASS = ".class";

    public static final String DIAGRAM_NAME = ".name";

    public static final String DIAGRAM_LEGEND = ".legend";

    public static final String EXTERNALIZER_TYPE = ".type";

    public static final String EXTERNALIZER_ENABLED = ".enabled";

    public static final String EXTERNALIZER_CLASS = ".class";

    public ConfigParser() {
    }

    /**
     * Parse the supplied properties file and create a Config instance.
     */
    public ConfigInfo parse(String filename) {
        try {
            InputStream in = null;
            try {
                in = new FileInputStream(filename);
                return parse(in);
            } finally {
                if (in != null) in.close();
            }
        } catch (IOException e) {
            handleException(e);
            return null;
        }
    }

    private void handleException(Throwable t) {
        throw BindingSupportImpl.getInstance().runtime(t.getClass().getName() + ": " + t.getMessage(), t);
    }

    /**
     * Parse the supplied properties resoutce and create a Config instance.
     */
    public ConfigInfo parseResource(String filename, ClassLoader cl) {
        try {
            InputStream in = null;
            try {
                if (filename.startsWith("/")) filename = filename.substring(1);
                in = cl.getResourceAsStream(filename);
                if (in == null) {
                    throw BindingSupportImpl.getInstance().runtime("Resource not found: " + filename);
                }
                return parse(in);
            } finally {
                if (in != null) in.close();
            }
        } catch (IOException e) {
            handleException(e);
            return null;
        }
    }

    public ConfigInfo parseResource(File file) {
        try {
            InputStream in = new FileInputStream(file);
            try {
                return parse(in);
            } finally {
                if (in != null) in.close();
            }
        } catch (IOException e) {
            handleException(e);
            return null;
        }
    }

    /**
     * Parse the supplied properties resoutce and create a Config instance.
     */
    public ConfigInfo parseResource(String filename) {
        try {
            InputStream in = null;
            try {
                in = getClass().getResourceAsStream(filename);
                if (in == null) {
                    throw BindingSupportImpl.getInstance().runtime("Resource not found: " + filename);
                }
                return parse(in);
            } finally {
                if (in != null) in.close();
            }
        } catch (IOException e) {
            handleException(e);
            return null;
        }
    }

    /**
     * Parse the supplied config stream and create a Config instance.
     */
    public ConfigInfo parse(InputStream in) {
        Properties p = new Properties();
        try {
            p.load(in);
        } catch (IOException e) {
            throw BindingSupportImpl.getInstance().runtime(e.getClass() + ": " + e.getMessage(), e);
        }
        return parse(p);
    }

    /**
     * Parse the supplied config properties and create a Config instance.
     */
    public ConfigInfo parse(Properties p) {
        PropertyConverter.convert(p);
        String s;
        ConfigInfo c = new ConfigInfo();
        c.metaDataPreProcessor = p.getProperty(META_DATA_PRE_PROCESSOR);
        c.props = p;
        c.jdoMetaData = (JdoRoot[]) p.get(VERSANT_JDO_META_DATA);
        c.serverName = p.getProperty(SERVER, "versant");
        c.hyperdrive = getBoolean(p, HYPERDRIVE, DEFAULT_HYPERDRIVE);
        c.hyperdriveSrcDir = p.getProperty(HYPERDRIVE_SRC_DIR);
        c.hyperdriveClassDir = p.getProperty(HYPERDRIVE_CLASS_DIR);
        c.flushThreshold = getInt(p, FLUSH_THRESHOLD, DEFAULT_FLUSH_THRESHOLD);
        c.allowPmCloseWithOpenTx = getBoolean(p, ALLOW_PM_CLOSE_WITH_OPEN_TX, DEFAULT_ALLOW_PM_CLOSE_WITH_OPEN_TX);
        c.precompileNamedQueries = getBoolean(p, PRECOMPILE_NAMED_QUERIES, DEFAULT_PRECOMPILE_NAMED_QUERIES);
        c.checkModelConsistencyOnCommit = getBoolean(p, CHECK_MODEL_CONSISTENCY_ON_COMMIT, DEFAULT_CHECK_MODEL_CONSISTENCY_ON_COMMIT);
        c.interceptDfgFieldAccess = getBoolean(p, INTERCEPT_DFG_FIELD_ACCESS, false);
        c.testing = getBoolean(p, TESTING, false);
        c.remoteAccess = p.getProperty(REMOTE_ACCESS);
        s = p.getProperty(PM_CACHE_REF_TYPE, PM_CACHE_REF_TYPE_SOFT);
        if (s.equals(PM_CACHE_REF_TYPE_SOFT)) {
            c.pmCacheRefType = VersantPersistenceManager.PM_CACHE_REF_TYPE_SOFT;
        } else if (s.equals(PM_CACHE_REF_TYPE_WEAK)) {
            c.pmCacheRefType = VersantPersistenceManager.PM_CACHE_REF_TYPE_WEAK;
        } else if (s.equals(PM_CACHE_REF_TYPE_STRONG)) {
            c.pmCacheRefType = VersantPersistenceManager.PM_CACHE_REF_TYPE_STRONG;
        } else {
            throw BindingSupportImpl.getInstance().runtime("Invalid " + PM_CACHE_REF_TYPE + ": '" + s + "', expected " + PM_CACHE_REF_TYPE_SOFT + ", " + PM_CACHE_REF_TYPE_WEAK + " or " + PM_CACHE_REF_TYPE_STRONG);
        }
        c.retainValues = getBoolean(p, OPTION_RETAINVALUES, DEFAULT_OPTION_RETAINVALUES);
        c.restoreValues = getBoolean(p, OPTION_RESTORE_VALUES, DEFAULT_OPTION_RESTORE_VALUES);
        c.optimistic = getBoolean(p, OPTION_OPTIMISTIC, DEFAULT_OPTION_OPTIMISTIC);
        c.nontransactionalRead = getBoolean(p, OPTION_NON_TRANSACTIONAL_READ, DEFAULT_OPTION_NON_TRANSACTIONAL_READ);
        c.nontransactionalWrite = getBoolean(p, OPTION_NON_TRANSACTIONAL_WRITE, DEFAULT_OPTION_NON_TRANSACTIONAL_WRITE);
        c.ignoreCache = getBoolean(p, OPTION_IGNORE_CACHE, DEFAULT_OPTION_IGNORE_CACHE);
        c.multithreaded = getBoolean(p, OPTION_MULTITHREADED, DEFAULT_OPTION_MULTITHREADED);
        c.connectionFactoryName = p.getProperty(OPTION_CONNECTION_FACTORY_NAME);
        c.connectionFactory2Name = p.getProperty(OPTION_CONNECTION_FACTORY2_NAME);
        s = p.getProperty(DATASTORE_TX_LOCKING);
        if (s == null) {
            c.datastoreTxLocking = VersantPersistenceManager.LOCKING_FIRST;
        } else {
            if (s.equals(DATASTORE_TX_LOCKING_ALL)) {
                c.datastoreTxLocking = VersantPersistenceManager.LOCKING_ALL;
            } else if (s.equals(DATASTORE_TX_LOCKING_FIRST)) {
                c.datastoreTxLocking = VersantPersistenceManager.LOCKING_FIRST;
            } else if (s.equals(DATASTORE_TX_LOCKING_NONE)) {
                c.datastoreTxLocking = VersantPersistenceManager.LOCKING_NONE;
            } else {
                throw BindingSupportImpl.getInstance().runtime("Invalid datastore.tx.locking: '" + s + "', expected none, first or all.");
            }
        }
        s = p.getProperty(RETAIN_CONNECTION_IN_OPT_TX);
        if ("true".equals(s)) {
            c.retainConnectionInOptTx = MDStatics.TRUE;
        } else if ("false".equals(s)) {
            c.retainConnectionInOptTx = MDStatics.FALSE;
        } else {
            c.retainConnectionInOptTx = MDStatics.NOT_SET;
        }
        c.pmpoolEnabled = getBoolean(p, PMPOOL_ENABLED, DEFAULT_PMPOOL_ENABLED);
        c.pmpoolMaxIdle = getInt(p, PMPOOL_MAX_IDLE, DEFAULT_PMPOOL_MAX_IDLE);
        c.remotePmpoolEnabled = getBoolean(p, REMOTE_PMPOOL_ENABLED, DEFAULT_REMOTE_PMPOOL_ENABLED);
        c.remotePmpoolMaxIdle = getInt(p, REMOTE_PMPOOL_MAX_IDLE, DEFAULT_REMOTE_PMPOOL_MAX_IDLE);
        c.remotePmpoolMaxActive = getInt(p, REMOTE_PMPOOL_MAX_ACTIVE, DEFAULT_REMOTE_PMPOOL_MAX_ACTIVE);
        c.queryCacheEnabled = getBoolean(p, QUERY_CACHE_ENABLED, DEFAULT_QUERY_CACHE_ENABLED);
        c.maxQueriesToCache = getInt(p, QUERY_CACHE_MAX_QUERIES, DEFAULT_CACHE_MAX_QUERIES);
        c.compiledQueryCacheSize = getInt(p, COMPILED_QUERY_CACHE_SIZE, 0);
        c.useCache = getBoolean(p, CACHE_ENABLED, DEFAULT_CACHE_ENABLED);
        c.cacheMaxObjects = getInt(p, CACHE_MAX_OBJECTS, DEFAULT_CACHE_MAX_OBJECTS);
        c.cacheListenerClass = getClassAndProps(p, CACHE_CLUSTER_TRANSPORT, c.cacheListenerProps = new HashMap());
        if (c.cacheListenerClass == null && c.cacheListenerProps == null) {
            c.cacheListenerClass = getClassAndProps(p, CACHE_LISTENER, c.cacheListenerProps = new HashMap());
        }
        readExternalizers(p, c);
        readSCOFactoryMappings(p, c.scoFactoryRegistryMappings);
        String db = p.getProperty(STORE_DB);
        if (Utils.isVersantDatabaseType(db)) {
            db = "versant";
            p.put(STORE_DB, db);
        }
        c.db = p.getProperty(STORE_DB);
        c.url = p.getProperty(STD_CON_URL);
        getClassAndProps(p, EVENT_LOGGING, c.perfProps = new HashMap());
        c.logDownloaderClass = getClassAndProps(p, LOG_DOWNLOADER, c.logDownloaderProps = new HashMap());
        c.metricStoreCapacity = getInt(p, METRIC_STORE_CAPACITY, DEFAULT_METRIC_STORE_CAPACITY);
        c.metricSnapshotIntervalMs = getInt(p, METRIC_SNAPSHOT_INTERVAL_MS, DEFAULT_METRIC_SNAPSHOT_INTERVAL_MS);
        for (int i = 0; i < MAX_METRIC_USER_COUNT; i++) {
            s = p.getProperty(METRIC_USER + i);
            if (s == null) continue;
            StringListParser lp = new StringListParser(s);
            ConfigInfo.UserBaseMetric u = new ConfigInfo.UserBaseMetric();
            u.name = lp.nextString();
            u.displayName = lp.nextQuotedString();
            u.category = lp.nextQuotedString();
            u.description = lp.nextQuotedString();
            u.defaultCalc = lp.nextInt();
            u.decimals = lp.nextInt();
            c.userBaseMetrics.add(u);
        }
        int n = MAX_JDO_FILE_COUNT;
        for (int i = 0; i < n; i++) {
            String base = JDO + i;
            String f = p.getProperty(base);
            if (f == null) continue;
            c.jdoResources.add(f);
        }
        return c;
    }

    private void readSCOFactoryMappings(Properties p, Map map) {
        String s;
        for (int x = 0; x < MAX_STORE_SCO_FACTORY_COUNT; x++) {
            s = p.getProperty(STORE_SCO_FACTORY_MAPPING + x);
            if (s != null) {
                StringListParser lp = new StringListParser(s);
                String javaClassName = lp.nextString();
                String factoryName = lp.nextString();
                map.put(javaClassName, factoryName);
            }
        }
    }

    private void readExternalizers(Properties p, ConfigInfo c) {
        String s;
        String ext = null;
        for (int i = 0; i < MAX_EXTERNALIZER_COUNT; i++) {
            ext = EXTERNALIZER + i;
            s = p.getProperty(ext + EXTERNALIZER_TYPE);
            if (s != null) {
                ConfigInfo.ExternalizerInfo ei = new ConfigInfo.ExternalizerInfo();
                ei.typeName = s;
                ei.enabled = getBoolean(p, ext + EXTERNALIZER_ENABLED, false);
                ei.externalizerName = p.getProperty(ext + EXTERNALIZER_CLASS);
                Set all = p.keySet();
                String extProps = ext + EXTERNALIZER_CLASS + ".";
                for (Iterator iter = all.iterator(); iter.hasNext(); ) {
                    String key = (String) iter.next();
                    if (key.startsWith(extProps)) {
                        String newKey = key.substring(extProps.length() + 1);
                        ei.args.put(newKey, p.getProperty(key));
                    }
                }
                c.externalizers.add(ei);
            }
        }
    }

    public static String trim(String s) {
        if (s == null) return s;
        s = s.trim();
        if (s.length() == 0) return null;
        return s;
    }

    public static int getExtEnum(Properties p, String be, int ext, Map map, int def) {
        JdoExtension e = getExt(p, be, ext);
        if (e == null) return def;
        return e.getEnum(map);
    }

    public static boolean getExtBoolean(Properties p, String be, int ext, boolean def) {
        JdoExtension e = getExt(p, be, ext);
        if (e == null) return def;
        return e.getBoolean();
    }

    public static JdoExtension getExt(Properties p, String be, int ext) {
        String s = p.getProperty(be + JdoExtension.toKeyString(ext));
        if (s == null) return null;
        JdoExtension e = new JdoExtension();
        e.key = ext;
        e.value = s;
        return e;
    }

    public static String getReq(Properties p, String s, String alt) {
        String v = p.getProperty(s);
        if (v == null && alt != null) v = p.getProperty(alt);
        if (v == null) {
            if (alt == null) {
                throw BindingSupportImpl.getInstance().runtime("Expected property: '" + s + "'");
            } else {
                throw BindingSupportImpl.getInstance().runtime("Expected property: '" + s + "' or '" + alt + "'");
            }
        }
        return v;
    }

    public static String getReq(Properties p, String s) {
        return getReq(p, s, null);
    }

    public static int getInt(Properties p, String s, int def) {
        String v = p.getProperty(s);
        if (v == null || v.equals("null") || v.length() == 0) return def;
        try {
            return Integer.parseInt(v);
        } catch (NumberFormatException e) {
            throw BindingSupportImpl.getInstance().runtime("Expected int: " + s);
        }
    }

    public static boolean getBoolean(Properties p, String s, boolean def) {
        String v = p.getProperty(s);
        if (v == null) return def;
        return v.equals("true") || v.equals("on");
    }

    public static String getClassAndProps(Properties p, String key, Map map) {
        Set all = p.keySet();
        String nextKey = key + ".";
        for (Iterator iter = all.iterator(); iter.hasNext(); ) {
            String s = (String) iter.next();
            if (s.startsWith(nextKey)) {
                String newKey = s.substring(nextKey.length());
                map.put(newKey, p.getProperty(s));
            }
        }
        return p.getProperty(key);
    }
}
