package gate.persist;

import java.sql.*;
import java.util.HashMap;
import gate.Gate;

public class DBHelper {

    /** class name of the Oracle jdbc driver */
    private static final String jdbcOracleDriverName = "oracle.jdbc.driver.OracleDriver";

    private static final String jdbcPostgresDriverName = "org.postgresql.Driver";

    public static final int CHINK_SIZE_SMALL = 30;

    public static final int CHINK_SIZE_MEDIUM = 60;

    public static final int CHINK_SIZE_LARGE = 100;

    /** user defined error codes in Oracle start with -21000 */
    public static final int X_ORACLE_START = 20100;

    /**  this should be thrown if an attempt to create a group with duplicated name is made */
    public static final int X_ORACLE_DUPLICATE_GROUP_NAME = X_ORACLE_START + 1;

    /** see above */
    public static final int X_ORACLE_DUPLICATE_USER_NAME = X_ORACLE_START + 2;

    /** no such user failure upon login */
    public static final int X_ORACLE_INVALID_USER_NAME = X_ORACLE_START + 3;

    /** - */
    public static final int X_ORACLE_INVALID_USER_PASS = X_ORACLE_START + 4;

    /** invalid group id supplied for operation requiring such specifier */
    public static final int X_ORACLE_INVALID_USER_GROUP = X_ORACLE_START + 5;

    /** access to LR by id fails - no such resource */
    public static final int X_ORACLE_INVALID_LR = X_ORACLE_START + 6;

    /** attempt to access resource in mode that does not exist */
    public static final int X_ORACLE_INVALID_ACCESS_MODE = X_ORACLE_START + 7;

    /** huh? */
    public static final int X_ORACLE_INVALID_ARGUMENT = X_ORACLE_START + 8;

    /** this should not be in use anymore */
    public static final int X_ORACLE_NOT_IMPLEMENTED = X_ORACLE_START + 9;

    /** attempt to delete a group that owns resources is made */
    public static final int X_ORACLE_GROUP_OWNS_RESOURCES = X_ORACLE_START + 10;

    /** attempt to delete a user that owns resources is made */
    public static final int X_ORACLE_USER_OWNS_RESOURCES = X_ORACLE_START + 11;

    /** huh? */
    public static final int X_ORACLE_INCOMPLETE_DATA = X_ORACLE_START + 12;

    /** attempt to access resources by type is made, but no such type exists */
    public static final int X_ORACLE_INVALID_LR_TYPE = X_ORACLE_START + 13;

    /** this is obsolete now? */
    public static final int X_ORACLE_INVALID_ANNOTATION_TYPE = X_ORACLE_START + 14;

    /** attempt to create a feature with invalid value type is made
   *  since value types are automatically assigned in the java code, this errror
   *  should indicate that the java code was changed but no changes were made to the
   *  relevant pl/sql code
   *  */
    public static final int X_ORACLE_INVALID_FEATURE_TYPE = X_ORACLE_START + 15;

    /**
   * not supported content type - we support only character/binary/empty content
   * since there are no many other options this error shoudkl indicate that the
   * java code was not synced with the pl/sql one
   *
   *  */
    public static final int X_ORACLE_INVALID_CONTENT_TYPE = X_ORACLE_START + 16;

    /** attempt to remove annotation that does not exist is made */
    public static final int X_ORACLE_INVALID_ANNOTATION = X_ORACLE_START + 17;

    /** attempt to perform an operation that requres more privileged is made */
    public static final int X_ORACLE_INSUFFICIENT_PRIVILEGES = X_ORACLE_START + 18;

    /** attempt to remove annotation set that does not exist is made */
    public static final int X_ORACLE_INVALID_ANNOTATION_SET = X_ORACLE_START + 19;

    public static final int TRUE = 1;

    public static final int FALSE = 0;

    /** character content (may make difference for the database) */
    public static final int CHARACTER_CONTENT = 1;

    /** binary content (may make difference for the database) */
    public static final int BINARY_CONTENT = 2;

    /** document has no content*/
    public static final int EMPTY_CONTENT = 3;

    /** LR classes supported at present */
    public static final String DOCUMENT_CLASS = "gate.corpora.DatabaseDocumentImpl";

    /** LR classes supported at present */
    public static final String CORPUS_CLASS = "gate.corpora.DatabaseCorpusImpl";

    /** key in T_PARAMETER that defines a unique id for the data store */
    public static final String DB_PARAMETER_GUID = "DB_GUID";

    /** dummy feature key, do not use it */
    public static final String DUMMY_FEATURE_KEY = "--NO--SUCH--KEY--";

    /** dummy encoding type, do not use it */
    public static final String DUMMY_ENCODING = "-!-";

    /** used internaly, may change in the future */
    public static final int READ_ACCESS = 0;

    /** used internaly, may change in the future */
    public static final int WRITE_ACCESS = 1;

    /** huh? */
    public static final Long DUMMY_ID;

    /** used to store corpus' features */
    protected static final int FEATURE_OWNER_CORPUS = 1;

    /** used to store document's features */
    protected static final int FEATURE_OWNER_DOCUMENT = 2;

    /** used to store annotation's features */
    protected static final int FEATURE_OWNER_ANNOTATION = 3;

    /** feature value is null  */
    public static final int VALUE_TYPE_NULL = 100;

    /** feature value is int  */
    public static final int VALUE_TYPE_INTEGER = 101;

    /** feature value is long */
    public static final int VALUE_TYPE_LONG = 102;

    /** feature value is boolean */
    public static final int VALUE_TYPE_BOOLEAN = 103;

    /** feature value is string less than 4000 bytes */
    public static final int VALUE_TYPE_STRING = 104;

    /** feature value is binary */
    public static final int VALUE_TYPE_BINARY = 105;

    /** feature value is float */
    public static final int VALUE_TYPE_FLOAT = 106;

    /** feature value is array of ints */
    public static final int VALUE_TYPE_INTEGER_ARR = 107;

    /** feature value is array of longs */
    public static final int VALUE_TYPE_LONG_ARR = 108;

    /** feature value is array of bools */
    public static final int VALUE_TYPE_BOOLEAN_ARR = 109;

    /** feature value is array of strings */
    public static final int VALUE_TYPE_STRING_ARR = 110;

    /** feature value is array of binary values */
    public static final int VALUE_TYPE_BINARY_ARR = 111;

    /** feature value is array of floats */
    public static final int VALUE_TYPE_FLOAT_ARR = 112;

    /** feature value is array of floats */
    public static final int VALUE_TYPE_EMPTY_ARR = 113;

    /** Oracle database type */
    public static final int ORACLE_DB = 101;

    /** PostgreSQL database type */
    public static final int POSTGRES_DB = 102;

    private static final boolean DEBUG = false;

    private static boolean oracleLoaded = false;

    private static boolean postgresLoaded = false;

    private static HashMap pools;

    /** size (in elements) of the jdbc connection pool (if any) */
    private static final int POOL_SIZE = 20;

    static {
        DUMMY_ID = new Long(Long.MIN_VALUE);
        pools = new HashMap();
    }

    protected DBHelper() {
    }

    /** --- */
    private static synchronized void loadDrivers(final int dbType) throws ClassNotFoundException {
        if (!oracleLoaded && dbType == ORACLE_DB) {
            Class.forName(jdbcOracleDriverName);
        } else if (!postgresLoaded && dbType == POSTGRES_DB) {
            Class.forName(jdbcPostgresDriverName);
        }
    }

    /**
   *  closes a result set
   *  note that Oracle jdbc classes do not have finalize() implementations so if
   *  they're not closed leaks may occur
   */
    public static void cleanup(ResultSet rs) throws PersistenceException {
        try {
            if (rs != null) rs.close();
        } catch (SQLException sqle) {
            throw new PersistenceException("an SQL exception occured [" + sqle.getMessage() + "]");
        }
    }

    /**
   *  closes a statement
   *  note that Oracle jdbc classes do not have finalize() implementations so if
   *  they're not closed leaks may occur
   */
    public static void cleanup(Statement stmt) throws PersistenceException {
        try {
            if (stmt != null) stmt.close();
        } catch (SQLException sqle) {
            throw new PersistenceException("an SQL exception occured [" + sqle.getMessage() + "]");
        }
    }

    /**
   *  connects to DB
   */
    public static Connection connect(final String connectURL) throws SQLException, ClassNotFoundException {
        loadDrivers(getDatabaseType(connectURL));
        Connection conn = DriverManager.getConnection(connectURL);
        if (DEBUG) {
            DatabaseMetaData meta = conn.getMetaData();
            gate.util.Out.println("JDBC driver name=[" + meta.getDriverName() + "] version=[" + meta.getDriverVersion() + "]");
        }
        return conn;
    }

    /**
   *  connects to DB
   */
    public static Connection connect(final String connectURL, final String user, final String pass) throws SQLException, ClassNotFoundException {
        loadDrivers(getDatabaseType(connectURL));
        Connection conn = DriverManager.getConnection(connectURL, user, pass);
        if (DEBUG) {
            DatabaseMetaData meta = conn.getMetaData();
            gate.util.Err.println("JDBC driver name=[" + meta.getDriverName() + "] version=[" + meta.getDriverVersion() + "]");
        }
        return conn;
    }

    /**
   * disconnects from DB, may return connection to pool if such exists
   *
   * any uncommited transactions are rolled back
   */
    public static void disconnect(Connection conn) throws PersistenceException {
        try {
            conn.rollback();
            conn.close();
        } catch (SQLException sqle) {
            throw new PersistenceException("cannot close JDBC connection, DB error is [" + sqle.getMessage() + "]");
        }
    }

    /**
   *  connects to DB
   * gets connection from pool if such exists
   */
    public static Connection connect(String connectURL, boolean usePool) throws SQLException, ClassNotFoundException {
        if (false == usePool) {
            return connect(connectURL);
        } else {
            ConnectionPool currPool = null;
            synchronized (pools) {
                if (false == pools.containsKey(connectURL)) {
                    currPool = new ConnectionPool(POOL_SIZE, connectURL);
                    pools.put(connectURL, currPool);
                } else {
                    currPool = (ConnectionPool) pools.get(connectURL);
                }
            }
            return currPool.get();
        }
    }

    /**
   * disconnects from DB, may return connection to pool if such exists
   *
   * any uncommited transactions are rolled back
   */
    public static void disconnect(Connection conn, boolean usePool) throws PersistenceException {
        if (false == usePool) {
            disconnect(conn);
        } else {
            String jdbcURL = null;
            try {
                jdbcURL = conn.getMetaData().getURL();
                conn.rollback();
            } catch (SQLException sqle) {
                throw new PersistenceException(sqle);
            }
            ConnectionPool currPool = (ConnectionPool) pools.get(jdbcURL);
            currPool.put(conn);
        }
    }

    public static String getSchemaPrefix(String jdbcURL) {
        if (jdbcURL.startsWith("jdbc:oracle")) {
            return Gate.DB_OWNER + ".";
        } else if (jdbcURL.startsWith("jdbc:postgres")) {
            return "";
        } else {
            throw new IllegalArgumentException();
        }
    }

    public static int getDatabaseType(String jdbcURL) {
        if (jdbcURL.startsWith("jdbc:oracle")) {
            return DBHelper.ORACLE_DB;
        } else if (jdbcURL.startsWith("jdbc:postgres")) {
            return DBHelper.POSTGRES_DB;
        } else {
            throw new IllegalArgumentException();
        }
    }
}
