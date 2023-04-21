package net.jakubholy.testing.dbunit.embeddeddb;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dbunit.IDatabaseTester;
import org.dbunit.PropertiesBasedJdbcDatabaseTester;
import org.dbunit.database.IDatabaseConnection;

/**
 * Utility class for initializing the embedded Derby test database.
 * The database must be created and filled with tables needed for testing
 * before it can be used in unit tests.
 * <p>
 * It creates a test Derby database at the default location and
 * fills it with data structures defined in a DDL file expected at
 * the default location {@value #DDL_FILE_PATH}.
 * <p>
 * You should run this as a java console application (i.e. the main method)
 * to create the DB.
 *
 * @see #createDbSchemaFromDdl(Connection)
 */
public class DatabaseCreator {

    /** Name of the DDL file. */
    public static final String DDL_FILE_NAME = "create_db_content.ddl";

    /** Path to the DDL file, normally below {@link EmbeddedDbTester#TEST_DATA_FOLDER}. */
    public static final String DDL_FILE_PATH = EmbeddedDbTester.TEST_DATA_FOLDER + File.separator + DDL_FILE_NAME;

    private static final Log LOG = LogFactory.getLog(DatabaseCreator.class);

    /**
	 * Run a DDL on the (empty) test database to create the schemas and tables
	 * to fill with test date.
	 * The DDL is read from the file {@link #DDL_FILE_PATH}.
	 * <p>
	 * WARNING: The code cannot handle all the stuff you may use in a DDL.
	 * More exactly everything that you can pass to {@link Statement#execute(String)}
	 * is OK, in addition to that we can handle empty lines and comment lines
	 * starting with '--'.
	 *
	 * @param connection (required) A connection to the test db; e.g. {@link AbstractEmbeddedDbTestCase#getDatabaseTester()}#getConnection()#getConnection().
	 * @throws FileNotFoundException The DDL file not found
	 * @throws IOException Failed to read the DDL
	 * @throws SQLException Failure during execution of the DDL
	 * @throws Exception
	 */
    public static void createDbSchemaFromDdl(final Connection connection) throws FileNotFoundException, IOException, Exception, SQLException {
        if (connection == null) {
            throw new IllegalArgumentException("The argument connection: java.sql.Connection may not be null.");
        }
        LOG.info("createDbSchemaFromDdl: Going to initialize the test DB by creating the schema there...");
        final String sql = readDdlFromFile();
        LOG.info("createDbSchemaFromDdl: DDL read: " + sql);
        executeDdl(connection, sql);
        LOG.info("createDbSchemaFromDdl: done");
    }

    /**
     * Execute the ddlStatements on the connection.
     * @param connection (required) connection to the target database.
     * @param ddlStatements (required) DDL statements separated by semi-colon (';').
     * It shouldn't contain any -- comments as JDBC may not be able to ignore them as appropriate.
     * @throws SQLException
     */
    private static void executeDdl(final Connection connection, final String ddlStatements) throws SQLException {
        final java.sql.Statement ddlStmt = connection.createStatement();
        try {
            final String[] statements = ddlStatements.split(";");
            for (int i = 0; i < statements.length; i++) {
                if (statements[i].trim().length() > 0) {
                    LOG.info("createDbSchemaFromDdl: Adding batch stmt: " + statements[i]);
                    ddlStmt.addBatch(statements[i]);
                }
            }
            ddlStmt.executeBatch();
        } finally {
            try {
                ddlStmt.close();
            } catch (SQLException e) {
                LOG.warn("Failed to close the statement", e);
            }
        }
    }

    /**
     * Read the DDL from the file {@link #DDL_FILE_PATH} into a String.
     * @return the DDL
     * @throws FileNotFoundException
     * @throws IOException
     */
    private static String readDdlFromFile() throws FileNotFoundException, IOException {
        final FileReader fileReader = new FileReader(DDL_FILE_PATH);
        final BufferedReader sqlReader = new BufferedReader(fileReader);
        final StringBuffer sqlBuffer = new StringBuffer();
        String line;
        while ((line = sqlReader.readLine()) != null) {
            line = line.trim();
            if (line.length() > 0 && !line.startsWith("--")) {
                sqlBuffer.append(line).append('\n');
            }
        }
        try {
            sqlReader.close();
        } catch (IOException e) {
            LOG.info("readDdlFromFile: Failed to close the DDL input stream", e);
        }
        return sqlBuffer.toString();
    }

    /**
     * Run as a java console program to create and initialize the embedded Derby DB.
     * DB connection info is taken over from the {@link AbstractEmbeddedDbTestCase}.
     * <p>
     * To retry, simply delete the database (normally testData/testDB/).
     *
     * @param args ignored
     * @throws Exception
     *
     * @see #createAndInitializeTestDb
     */
    public static void main(String[] args) throws Exception {
        createAndInitializeTestDb();
    }

    /**
     * Create and from a DDL initialize the embedded Derby DB.
     * DB connection info is taken over from the {@link AbstractEmbeddedDbTestCase}
     * (where it is hard-coded).
     * <p>
     * To retry, simply delete the database (normally testData/testDB/).
     *
     * @throws Exception
     *
     * @see #createDbSchemaFromDdl(java.sql.Connection)
     * @see #DDL_FILE_PATH
     * @see EmbeddedDbTester#createAndInitDatabaseTester()
     */
    public static void createAndInitializeTestDb() throws Exception, FileNotFoundException, IOException, SQLException {
        final IDatabaseTester databaseTester = new EmbeddedDbTester().createAndInitDatabaseTester();
        final String oldDbConnUrl = System.getProperty(PropertiesBasedJdbcDatabaseTester.DBUNIT_CONNECTION_URL);
        if (oldDbConnUrl == null) {
            throw new IllegalStateException("The required property " + PropertiesBasedJdbcDatabaseTester.DBUNIT_CONNECTION_URL + " is not set (yet).");
        }
        final String selfCreatingDbConnUrl = oldDbConnUrl + ";create=true";
        System.setProperty(PropertiesBasedJdbcDatabaseTester.DBUNIT_CONNECTION_URL, selfCreatingDbConnUrl);
        final IDatabaseConnection dbUnitConnection = databaseTester.getConnection();
        try {
            DatabaseCreator.createDbSchemaFromDdl(dbUnitConnection.getConnection());
        } finally {
            try {
                dbUnitConnection.close();
            } catch (SQLException e) {
                LOG.warn("Failed to close the connection", e);
            }
        }
    }
}
