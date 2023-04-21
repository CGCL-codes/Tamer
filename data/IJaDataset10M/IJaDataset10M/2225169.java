package edu.ucdavis.genomics.metabolomics.util.database.test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.sql.Connection;
import java.sql.SQLException;
import org.apache.log4j.Logger;
import org.dbunit.DatabaseUnitException;
import org.dbunit.database.DatabaseConfig;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.ForwardOnlyResultSetTableFactory;
import org.dbunit.dataset.IDataSet;
import org.dbunit.operation.DatabaseOperation;
import edu.ucdavis.genomics.metabolomics.util.database.DatabaseUtilities;
import edu.ucdavis.genomics.metabolomics.util.database.DriverUtilities;
import edu.ucdavis.genomics.metabolomics.util.io.Copy;
import edu.ucdavis.genomics.metabolomics.util.io.source.ByteArraySource;
import edu.ucdavis.genomics.metabolomics.util.io.source.ResourceSource;
import edu.ucdavis.genomics.metabolomics.util.io.source.Source;

/**
 * simple utility to initialize the binbase from a schema
 * 
 * @author wohlgemuth
 */
public class InitializeDBUtil {

    /**
	 * initializes the database and works only for postgres
	 * 
	 * @param username
	 *            = the username with admin access
	 * @param password
	 *            = the password
	 * @param type
	 *            = the type of the database
	 * @param host
	 *            = the host name or ip
	 * @param database
	 *            = the database name
	 * @param schema
	 *            = the schema to create the database from
	 * @param populate
	 *            = the dataset to populate the database
	 * @throws SQLException
	 * @throws IOException
	 * @throws DdlUtilsException
	 * @throws DatabaseUnitException
	 */
    public static void initialize(String username, String password, String host, String database, Source createScript, IDataSet populate) throws Exception {
        Connection connection = DatabaseUtilities.createConnection(DriverUtilities.POSTGRES, username, password, host, database);
        initialize(connection, createScript, populate);
    }

    /**
	 * initialize it from the given conneciton
	 * 
	 * @param connection
	 * @param createScript
	 * @param dropScript
	 * @param populate
	 * @throws Exception
	 */
    public static void initialize(Connection connection, Source createScript, IDataSet populate, Source postScript) throws Exception {
        Logger logger = Logger.getLogger(InitializeDBUtil.class);
        executeSQLDropSql(connection, logger);
        executeSQLCreateSql(connection, createScript, logger);
        executeDBUnit(connection, populate);
        if (postScript != null) {
            executeSQLPostSql(connection, postScript, logger);
        }
    }

    public static void initialize(Connection connection, Source createScript, IDataSet populate) throws Exception {
        initialize(connection, createScript, populate, null);
    }

    /**
	 * creates the db schema
	 * 
	 * @param connection
	 * @param createScript
	 * @param dropScript
	 * @param logger
	 * @throws SQLException
	 * @throws Exception
	 */
    public static void executeSQLDropSql(Connection connection, Logger logger) throws SQLException, Exception {
        logger.debug("starting transaction");
        connection.commit();
        logger.debug("dropping schema...");
        try {
            connection.setAutoCommit(false);
            if (connection.createStatement().executeQuery("select * from pg_tables where schemaname = '" + connection.getMetaData().getUserName() + "'").next()) {
                logger.info("need to drop existing schema...");
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                PrintStream ous = new PrintStream(out);
                ous.println("drop schema " + connection.getMetaData().getUserName() + " cascade;");
                ous.flush();
                ous.close();
                executeSQLScript(connection, new ByteArraySource(out.toByteArray()));
            } else {
                logger.info("schema does not exist, so we ignore it!");
            }
            connection.commit();
        } catch (Exception e) {
            logger.warn("error: " + e.getMessage(), e);
            throw new SQLException(e);
        } finally {
            connection.setAutoCommit(true);
        }
    }

    public static void executeSQLCreateSql(Connection connection, Source createScript, Logger logger) throws SQLException, Exception {
        logger.debug("starting transaction");
        connection.commit();
        try {
            logger.info("creating schema: " + connection.getMetaData().getUserName());
            connection.setAutoCommit(false);
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            PrintStream ous = new PrintStream(out);
            ous.println("create schema " + connection.getMetaData().getUserName() + ";");
            ous.println("SET search_path = " + connection.getMetaData().getUserName().toUpperCase() + ", pg_catalog;");
            ous.flush();
            Copy.copy(createScript.getStream(), ous);
            ous.flush();
            ous.close();
            logger.debug("creating schema...");
            executeSQLScript(connection, new ByteArraySource(out.toByteArray()));
            connection.commit();
        } finally {
            connection.setAutoCommit(true);
        }
    }

    public static void executeSQLPostSql(Connection connection, Source createScript, Logger logger) throws SQLException, Exception {
        logger.debug("starting transaction");
        try {
            connection.setAutoCommit(false);
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            PrintStream ous = new PrintStream(out);
            ous.println("SET search_path = " + connection.getMetaData().getUserName().toUpperCase() + ", pg_catalog;");
            ous.flush();
            Copy.copy(createScript.getStream(), ous);
            ous.flush();
            ous.close();
            logger.debug("execute post sql...");
            executeSQLScript(connection, new ByteArraySource(out.toByteArray()));
            connection.commit();
        } finally {
            connection.setAutoCommit(true);
        }
    }

    public static void initialize(Connection connection, IDataSet populate) throws Exception {
        Logger logger = Logger.getLogger(InitializeDBUtil.class);
        logger.debug("loading resources");
        ResourceSource createScript = new ResourceSource("/sql/binbase-create.sql");
        ResourceSource postScript = new ResourceSource("/sql/binbase-post.sql");
        if (createScript.exist() == false) {
            throw new IOException("couldn't load /sql/binbase-create.sql");
        }
        if (postScript.exist() == false) {
            logger.warn("couldn't load /sql/binbase-post.sql");
            postScript = null;
        }
        logger.debug("disabling auto commit");
        connection.setAutoCommit(false);
        executeSQLDropSql(connection, logger);
        executeSQLCreateSql(connection, createScript, logger);
        executeDBUnit(connection, populate);
        if (postScript != null) {
            executeSQLPostSql(connection, postScript, logger);
        }
    }

    /**
	 * actually inserts the data
	 * 
	 * @param connection
	 * @param populate
	 * @throws DatabaseUnitException
	 * @throws SQLException
	 */
    private static void executeDBUnit(Connection connection, IDataSet populate) throws DatabaseUnitException, SQLException {
        DatabaseConnection dbunit = new DatabaseConnection(connection, connection.getMetaData().getUserName());
        dbunit.getConfig().setFeature(DatabaseConfig.FEATURE_BATCHED_STATEMENTS, true);
        dbunit.getConfig().setProperty(DatabaseConfig.PROPERTY_RESULTSET_TABLE_FACTORY, new ForwardOnlyResultSetTableFactory());
        dbunit.getConfig().setProperty(DatabaseConfig.PROPERTY_ESCAPE_PATTERN, "\"?\"");
        DatabaseOperation.INSERT.execute(dbunit, populate);
    }

    /**
	 * initializes without a drop script
	 * 
	 * @param username
	 * @param password
	 * @param host
	 * @param database
	 * @param createScript
	 * @param populate
	 * @throws Exception
	 */
    public static void initialize(String username, String password, String host, String database, IDataSet populate) throws Exception {
        ResourceSource create = new ResourceSource("/sql/binbase-create.sql");
        if (create.exist() == false) {
            throw new IOException("couldn't load /sql/binbase-create.sql");
        }
        initialize(username, password, host, database, create, populate);
    }

    /**
	 * creates our objects
	 * 
	 * @param connection
	 * @param data
	 * @throws Exception
	 */
    protected static void executeSQLScript(Connection connection, Source script) throws Exception {
        Logger logger = Logger.getLogger(InitializeDBUtil.class);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Copy.copy(script.getStream(), out);
        String runScript = new String(out.toByteArray());
        logger.info("executing script:");
        logger.info(runScript);
        DatabaseUtilities.execCommand(connection, runScript);
    }
}
