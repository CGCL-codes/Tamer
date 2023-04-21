package com.versant.core.jdo.tools.ant;

import com.versant.core.jdo.QueryDetails;
import com.versant.core.common.*;
import com.versant.core.common.config.ConfigInfo;
import com.versant.core.common.config.ConfigParser;
import com.versant.core.jdbc.JdbcConfig;
import com.versant.core.metadata.*;
import com.versant.core.common.NewObjectOID;
import com.versant.core.logging.LogEventStore;
import com.versant.core.server.*;
import com.versant.core.jdbc.*;
import com.versant.core.jdbc.query.JdbcCompiledQuery;
import com.versant.core.jdbc.metadata.JdbcLinkCollectionField;
import com.versant.core.jdbc.metadata.JdbcTable;
import com.versant.core.jdbc.metadata.JdbcClass;
import com.versant.core.jdbc.sql.SqlDriver;
import com.versant.core.util.CharBuf;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;
import com.versant.core.common.BindingSupportImpl;
import com.versant.core.storagemanager.DummyApplicationContext;
import com.versant.core.storagemanager.StorageManagerFactoryBuilder;
import com.versant.core.logging.LogEventStore;

/**
 * Configurable utility bean to copy one Open Access JDBC database to a
 * different URL.
 */
public class CopyDatabaseBean {

    private Properties srcProps;

    private String datastore;

    private Properties destProps;

    private String db;

    private String url;

    private String driver;

    private String user;

    private String password;

    private String properties;

    private boolean dropTables = true;

    private boolean createTables = true;

    private PrintStream out = System.out;

    private PrintWriter outw;

    private boolean stopFlag;

    private ClassLoader loader = getClass().getClassLoader();

    private int rowsPerTransaction = 1000;

    private String logEvents = LogEventStore.LOG_EVENTS_ERRORS;

    private boolean logEventsToSysOut = true;

    private MiniServer dest;

    private MiniServer src;

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd MMM yyyy HH:mm:ss");

    private static final int DOTS_PER_LINE = 70;

    private static final DummyKeyGen DUMMY_KEY_GEN = new DummyKeyGen();

    public CopyDatabaseBean() {
    }

    public Properties getSrcProps() {
        return srcProps;
    }

    public void setSrcProps(Properties srcProps) {
        this.srcProps = srcProps;
    }

    public String getDatastore() {
        return datastore;
    }

    public void setDatastore(String datastore) {
        this.datastore = datastore;
    }

    public Properties getDestProps() {
        return destProps;
    }

    public void setDestProps(Properties destProps) {
        this.destProps = destProps;
    }

    public String getDb() {
        return db;
    }

    public void setDb(String db) {
        this.db = db;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDriver() {
        return driver;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getProperties() {
        return properties;
    }

    public void setProperties(String properties) {
        this.properties = properties;
    }

    public boolean isCreateTables() {
        return createTables;
    }

    public void setCreateTables(boolean createTables) {
        this.createTables = createTables;
    }

    public boolean isDropTables() {
        return dropTables;
    }

    public void setDropTables(boolean dropTables) {
        this.dropTables = dropTables;
    }

    public PrintStream getOut() {
        return out;
    }

    public void setOut(PrintStream out) {
        this.out = out;
    }

    public boolean isStopFlag() {
        return stopFlag;
    }

    public void setStopFlag(boolean stopFlag) {
        this.stopFlag = stopFlag;
    }

    public String getLogEvents() {
        return logEvents;
    }

    public void setLogEvents(String logEvents) {
        this.logEvents = logEvents;
    }

    public boolean isLogEventsToSysOut() {
        return logEventsToSysOut;
    }

    public void setLogEventsToSysOut(boolean logEventsToSysOut) {
        this.logEventsToSysOut = logEventsToSysOut;
    }

    public ClassLoader getLoader() {
        return loader;
    }

    public void setLoader(ClassLoader loader) {
        this.loader = loader;
    }

    public int getRowsPerTransaction() {
        return rowsPerTransaction;
    }

    public void setRowsPerTransaction(int rowsPerTransaction) {
        this.rowsPerTransaction = rowsPerTransaction;
    }

    private void checkStopFlag() {
        if (stopFlag) throw new StoppedException();
    }

    /**
     * Check properties and get ready to start.
     */
    private void prepare() {
        if (srcProps == null) {
            throw BindingSupportImpl.getInstance().illegalArgument("srcProps property not set");
        }
        if (destProps == null) destProps = (Properties) srcProps.clone();
        if (db != null) destProps.setProperty(ConfigParser.STORE_DB, db);
        if (driver != null) {
            destProps.setProperty(ConfigParser.STD_CON_DRIVER_NAME, driver);
        }
        if (url != null) {
            destProps.setProperty(ConfigParser.STD_CON_URL, url);
        }
        if (user != null) {
            destProps.setProperty(ConfigParser.STD_CON_USER_NAME, user);
        }
        if (password != null) {
            destProps.setProperty(ConfigParser.STD_CON_PASSWORD, password);
        }
        if (properties != null) {
            properties = properties.trim().replace('\n', ';');
            destProps.setProperty(ConfigParser.STORE_PROPERTIES, properties);
        }
        outw = new PrintWriter(out, true);
    }

    /**
     * Copy datastore from src to dest.
     */
    public void copyDatabase() throws Exception {
        prepare();
        long start = System.currentTimeMillis();
        out.println("Started copy at " + DATE_FORMAT.format(new Date(start)) + "\n");
        src = dest = null;
        try {
            dest = new MiniServer(destProps);
            checkStopFlag();
            src = new MiniServer(srcProps);
            checkStopFlag();
            if (dropTables) dest.dropAllTables();
            if (createTables) dest.createAllTables();
            ClassMetaData[] srcClasses = src.getStoreClasses();
            ClassMetaData[] destClasses = dest.getStoreClasses();
            out.println("=== Copying main table for each class (one dot = " + rowsPerTransaction + " rows) ===\n");
            long mainStart = System.currentTimeMillis();
            int mainTotRows = 0;
            int numClasses = srcClasses.length;
            for (int i = 0; i < numClasses; i++) {
                mainTotRows += copyClassMainTable(srcClasses[i], destClasses[i]);
                checkStopFlag();
            }
            int mainTime = (int) (System.currentTimeMillis() - mainStart);
            out.println();
            out.println("=== Copying link tables for each class (one dot = " + rowsPerTransaction + " rows) ===\n");
            long linkStart = System.currentTimeMillis();
            int linkTotRows = 0;
            for (int i = 0; i < numClasses; i++) {
                ClassMetaData sc = srcClasses[i];
                for (int j = 0; j < sc.fields.length; j++) {
                    FieldMetaData sfmd = sc.fields[j];
                    if (sfmd.storeField instanceof JdbcLinkCollectionField) {
                        JdbcLinkCollectionField srcField = (JdbcLinkCollectionField) sfmd.storeField;
                        if (!srcField.readOnly) {
                            linkTotRows += copyLinkTable(srcField, (JdbcLinkCollectionField) destClasses[i].fields[j].storeField);
                            checkStopFlag();
                        }
                    }
                }
            }
            int linkTime = (int) (System.currentTimeMillis() - linkStart);
            out.println();
            if (createTables) {
                dest.createAllIndexes();
                dest.createAllConstraints();
            }
            out.println("Copied " + mainTotRows + "  main table rows " + "in " + fmtSec(mainTime) + " secs (" + perSec(mainTotRows, mainTime) + " per second)");
            out.println("Copied " + linkTotRows + "  link table rows " + "in " + fmtSec(linkTime) + " secs (" + perSec(linkTotRows, linkTime) + " per second)");
        } catch (StoppedException x) {
            out.println("\n*** Copy aborted ***");
        } finally {
            if (src != null) src.shutdown();
            if (dest != null) dest.shutdown();
            src = dest = null;
            out.println("\nFinished at " + DATE_FORMAT.format(new Date()));
        }
    }

    private String fmtSec(int ms) {
        StringBuffer s = new StringBuffer();
        s.append(ms / 1000);
        s.append('.');
        ms = ms % 1000;
        if (ms < 10) s.append("00");
        if (ms < 100) s.append('0');
        s.append(ms);
        return s.toString();
    }

    private String perSec(int rows, int ms) {
        float f = Math.round(rows * 10000.0f / ms) / 10;
        return String.valueOf(f);
    }

    private int copyClassMainTable(ClassMetaData srcClass, ClassMetaData destClass) throws Exception {
        out.println(((JdbcClass) srcClass.storeClass).table.name + " - " + srcClass.qname);
        JdbcKeyGenerator keygen = ((JdbcClass) destClass.storeClass).jdbcKeyGenerator;
        boolean autoinc = keygen != null && keygen.isPostInsertGenerator();
        ((JdbcClass) destClass.storeClass).jdbcKeyGenerator = DUMMY_KEY_GEN;
        JdbcQueryResult rs = null;
        try {
            Connection destCon = dest.pool.getConnection(false, false);
            if (autoinc) {
                dest.sqlDriver.enableIdentityInsert(destCon, ((JdbcClass) destClass.storeClass).table.name, true);
            }
            PersistGraph pg = new PersistGraph(dest.getJmd(), rowsPerTransaction);
            QueryDetails qp = new QueryDetails();
            qp.setCandidateClass(srcClass.cls);
            qp.setSubClasses(false);
            qp.setFetchGroupIndex(srcClass.getFetchGroup(FetchGroup.ALL_COLS_NAME).index);
            qp.setResultBatchSize(rowsPerTransaction);
            JdbcCompiledQuery cq = (JdbcCompiledQuery) src.getSm().compileQuery(qp);
            cq.setCacheable(false);
            int tot = 0;
            int dots = 0;
            rs = (JdbcQueryResult) src.getSm().executeQuery(DummyApplicationContext.INSTANCE, null, cq, null).getRunningQuery();
            StatesReturned container = new StatesReturned(DummyApplicationContext.INSTANCE);
            for (; ; ) {
                int c = 0;
                boolean more = false;
                for (; c < rowsPerTransaction && (more = rs.next(0)); c++) {
                    JdbcGenericOID oid = (JdbcGenericOID) rs.getResultOID();
                    JdbcGenericState state = (JdbcGenericState) rs.getResultState(false, container);
                    oid.setCmd(destClass);
                    state.setClassMetaData(destClass);
                    NewObjectOID noid = destClass.createNewObjectOID();
                    noid.realOID = oid;
                    pg.add(noid, null, state);
                    container.clear();
                }
                if (c > 0) {
                    dest.getSm().begin(false);
                    dest.getSm().persistPass1(pg);
                    dest.getSm().commit();
                    tot += c;
                    if (++dots == DOTS_PER_LINE) {
                        out.println("." + tot);
                        dots = 0;
                    } else {
                        out.print('.');
                    }
                    pg.clear();
                }
                if (!more) break;
                checkStopFlag();
            }
            if (dots > 0) out.println(tot);
            if (autoinc) {
                dest.sqlDriver.enableIdentityInsert(destCon, ((JdbcClass) destClass.storeClass).table.name, false);
            }
            return tot;
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (Exception e) {
                }
            }
            if (src.getSm().isActive()) {
                src.getSm().rollback();
            }
            if (dest.getSm().isActive()) {
                dest.getSm().rollback();
            }
        }
    }

    private void close(ResultSet rs) {
        if (rs == null) return;
        try {
            rs.close();
        } catch (SQLException e) {
        }
    }

    private void close(Statement stat) {
        if (stat == null) return;
        try {
            stat.close();
        } catch (SQLException e) {
        }
    }

    private int copyLinkTable(JdbcLinkCollectionField srcField, JdbcLinkCollectionField destField) throws Exception {
        out.println(srcField.linkTable.name + " - " + srcField.fmd.getQName());
        Connection srcCon = null;
        Connection destCon = null;
        ResultSet rs = null;
        PreparedStatement ps = null;
        Statement stat = null;
        try {
            srcCon = src.getAutoCommitCon();
            destCon = dest.getSm().con();
            String insSql = srcField.getInsertLinkTableRowSql(new CharBuf());
            boolean batch = dest.getSm().isUseBatchInsert();
            stat = srcCon.createStatement();
            rs = stat.executeQuery(srcField.getFetchAllRowsSql(src.getSm()));
            if (src.getSm().getSqlDriver().isFetchSizeSupported()) {
                rs.setFetchSize(rowsPerTransaction);
            }
            JdbcLinkCollectionField.LinkRow row = new JdbcLinkCollectionField.LinkRow();
            int tot = 0;
            int dots = 0;
            for (; ; ) {
                ps = destCon.prepareStatement(insSql);
                int c = 0;
                boolean more = false;
                for (; c < rowsPerTransaction && (more = rs.next()); c++) {
                    srcField.readRow(rs, row);
                    destField.writeRow(ps, row);
                    if (batch) {
                        ps.addBatch();
                    } else {
                        ps.execute();
                    }
                }
                if (c > 0) {
                    if (batch) ps.executeBatch();
                    destCon.commit();
                    ps.close();
                    tot += c;
                    if (++dots == DOTS_PER_LINE) {
                        out.println("." + tot);
                        dots = 0;
                    } else {
                        out.print('.');
                    }
                }
                if (!more) break;
                checkStopFlag();
            }
            if (dots > 0) out.println(tot);
            return tot;
        } finally {
            close(rs);
            close(stat);
            close(ps);
            src.closeAutoCommitCon(srcCon);
            close(destCon);
        }
    }

    private void close(Connection con) {
        if (con == null) return;
        try {
            con.rollback();
        } catch (SQLException e) {
        }
        try {
            con.close();
        } catch (SQLException e) {
        }
    }

    /**
     * Stripped down JDO Genie server instance just so we can get at the
     * database.
     */
    private class MiniServer {

        private final JdbcStorageManagerFactory factory;

        private final JdbcStorageManager sm;

        private final JdbcConnectionSource pool;

        private final ModelMetaData jmd;

        private final ConfigInfo config;

        private final SqlDriver sqlDriver;

        public MiniServer(Properties props) throws Exception {
            props.put(ConfigParser.STORE_MAX_ACTIVE, "2");
            props.put(ConfigParser.STORE_MAX_IDLE, "2");
            props.put(ConfigParser.STORE_TEST_ON_ALLOC, "false");
            props.put(ConfigParser.STORE_TEST_ON_RELEASE, "false");
            props.put(ConfigParser.STORE_TEST_ON_EXCEPTION, "false");
            props.put(ConfigParser.STORE_RETRY_COUNT, "-1");
            ConfigParser p = new ConfigParser();
            config = p.parse(props);
            config.validate();
            config.hyperdrive = false;
            LogEventStore pes = new LogEventStore();
            pes.setLogEvents(logEvents);
            pes.setLogEventsToSysOut(logEventsToSysOut);
            StorageManagerFactoryBuilder b = new StorageManagerFactoryBuilder();
            b.setConfig(config);
            b.setLoader(loader);
            b.setFullInit(false);
            factory = (JdbcStorageManagerFactory) b.createStorageManagerFactory();
            pool = factory.getConnectionSource();
            jmd = factory.getModelMetaData();
            sqlDriver = factory.getSqlDriver();
            sm = (JdbcStorageManager) factory.getStorageManager();
        }

        public JdbcStorageManager getSm() {
            return sm;
        }

        public ModelMetaData getJmd() {
            return jmd;
        }

        public void shutdown() {
            factory.returnStorageManager(sm);
            factory.destroy();
        }

        public Connection getAutoCommitCon() throws Exception {
            return pool.getConnection(false, true);
        }

        public void closeAutoCommitCon(Connection con) throws SQLException {
            if (con == null) return;
            pool.returnConnection(con);
        }

        /**
         * Drop all the tables in store with names matching tables in the
         * schema.
         */
        public void dropAllTables() throws Exception {
            out.println("=== Dropping tables in: " + pool.getURL() + " ===");
            Connection con = null;
            try {
                con = getAutoCommitCon();
                HashMap dbTableNames = sm.getDatabaseTableNames(con);
                checkStopFlag();
                ArrayList a = sm.getJdbcMetaData().getTables();
                for (int i = 0; i < a.size(); i++) {
                    JdbcTable t = (JdbcTable) a.get(i);
                    String name = (String) dbTableNames.get(t.name.toLowerCase());
                    if (name != null) {
                        out.println("  Dropping " + name);
                        sqlDriver.dropTable(con, name);
                        checkStopFlag();
                    }
                }
            } finally {
                closeAutoCommitCon(con);
            }
            out.println();
        }

        /**
         * Create all tables but do not do indexes or constraints.
         */
        public void createAllTables() throws Exception {
            out.println("=== Creating tables in: " + pool.getURL() + " ===");
            Connection con = null;
            Statement stat = null;
            try {
                con = getAutoCommitCon();
                stat = con.createStatement();
                ArrayList tables = sm.getJdbcMetaData().getTables();
                int n = tables.size();
                for (int i = 0; i < n; i++) {
                    JdbcTable t = (JdbcTable) tables.get(i);
                    sqlDriver.generateCreateTable(t, stat, outw, false);
                    checkStopFlag();
                }
            } finally {
                close(stat);
                closeAutoCommitCon(con);
            }
            out.println();
        }

        /**
         * Create all indexes.
         */
        public void createAllIndexes() throws Exception {
            out.println("=== Creating indexes in: " + pool.getURL() + " ===");
            Connection con = null;
            Statement stat = null;
            try {
                con = getAutoCommitCon();
                stat = con.createStatement();
                ArrayList tables = sm.getJdbcMetaData().getTables();
                int n = tables.size();
                for (int i = 0; i < n; i++) {
                    JdbcTable t = (JdbcTable) tables.get(i);
                    if (t.indexes != null && t.indexes.length == 0) continue;
                    sqlDriver.generateCreateIndexes(t, stat, outw, false);
                    checkStopFlag();
                }
            } finally {
                close(stat);
                closeAutoCommitCon(con);
            }
            out.println();
        }

        /**
         * Create all constraints.
         */
        public void createAllConstraints() throws Exception {
            out.println("=== Adding constraints in: " + pool.getURL() + " ===");
            Connection con = null;
            Statement stat = null;
            try {
                con = getAutoCommitCon();
                stat = con.createStatement();
                ArrayList tables = sm.getJdbcMetaData().getTables();
                int n = tables.size();
                for (int i = 0; i < n; i++) {
                    JdbcTable t = (JdbcTable) tables.get(i);
                    if (t.constraints != null && t.constraints.length == 0) continue;
                    sqlDriver.generateConstraints(t, stat, outw, false);
                    checkStopFlag();
                }
            } finally {
                close(stat);
                closeAutoCommitCon(con);
            }
            out.println();
        }

        private void close(Statement stat) {
            if (stat == null) return;
            try {
                stat.close();
            } catch (SQLException e) {
            }
        }

        /**
         * Get all the classes in our store sorted in the correct order so
         * we can do inserts without tripping constraints.
         */
        public ClassMetaData[] getStoreClasses() {
            ArrayList a = new ArrayList();
            for (int i = 0; i < jmd.classes.length; i++) {
                ClassMetaData cmd = jmd.classes[i];
                if (cmd.storeClass != null) {
                    a.add(cmd);
                }
            }
            Collections.sort(a, new Comparator() {

                public int compare(Object o1, Object o2) {
                    ClassMetaData a = (ClassMetaData) o1;
                    ClassMetaData b = (ClassMetaData) o2;
                    int diff = b.referenceGraphIndex - a.referenceGraphIndex;
                    if (diff != 0) return diff;
                    return b.index - a.index;
                }
            });
            ClassMetaData[] x = new ClassMetaData[a.size()];
            a.toArray(x);
            return x;
        }
    }

    /**
     * Thrown by tasks when they detect that the stop flag is set.
     */
    private static class StoppedException extends RuntimeException {
    }

    /**
     * Dummy key generator set on all destination classes so that new pks
     * are not generated even for autoincrement classes.
     */
    private static class DummyKeyGen implements JdbcKeyGenerator {

        public void addKeyGenTables(HashSet set, JdbcMetaDataBuilder mdb) {
        }

        public boolean isPostInsertGenerator() {
            return false;
        }

        public void init(String className, JdbcTable classTable, Connection con) throws SQLException {
        }

        public boolean isRequiresOwnConnection() {
            return false;
        }

        public void generatePrimaryKeyPre(String className, JdbcTable classTable, int newObjectCount, Object[] data, Connection con) throws SQLException {
        }

        public String getPostInsertSQLSuffix(JdbcTable classTable) {
            return null;
        }

        public void generatePrimaryKeyPost(String className, JdbcTable classTable, Object[] data, Connection con, Statement stat) throws SQLException {
        }
    }
}
