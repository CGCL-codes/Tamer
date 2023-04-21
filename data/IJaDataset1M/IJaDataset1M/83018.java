package uk.org.ogsadai.dbcreate;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;

/**
 * This program creates N tables and populates these with M entries
 * consisting of a name (First, Surname). Address and telephone
 * number.
 * <p>
 * The <code>rows</code> parameter may be modified to generate a table
 * with any desired number of rows. The Ith row in each table will
 * be identical across all tables (assuming Random is implemented the
 * same on all Java JDKs).
 * </p>
 * <p>
 * Tables are named from <code>PREFIX00</code> to <code>PREFIXM</code>
 * where <code>PREFIX</code> is a table name prefix and <code>M</code>
 * ranges from 00 to the number of tables to create minus one.
 * </p>
 * <p>
 * If the number of rows is -1 then no new tables are created. This
 * can therefore be used to drop a sequence of tables.
 * </p>
 * <pre>
 * Usage:
 * java uk.org.ogsadai.dbcreate.CreateTestMySQLTables \
 *     [-driverclass MySQLDriverClass] \
 *     [-host MySQLHostName] [-port MySQLPort] \
 *     [-database MySQLDatabaseName] \
 *     [-username MySQLUserName] [-password MySQLPassword] \
 *     [-tablename PrefixOfTableToCreate] [-rows NumberOfRowsToCreate] \
 *     [-tables NumberOfTablesToCreate]
 * Default Settings:
 *     MySQLDriverClass:       org.gjt.mm.mysql.Driver
 *     MySQLHostName:          localhost
 *     MySQLPort:              3306
 *     MySQLDatabaseName:      ogsadai
 *     MySQLUserName:          ogsadai
 *     MySQLPassword:          ogsadai
 *     PrefixOfTableToCreate   course
 *     NumberOfRowsToCreate:   0
 *     NumberOfTablesToCreate: 100
 * </pre>
 * 
 * @author The OGSA-DAI Project Team.
 */
public class CreateTestMySQLTables extends CreateTestDB {

    /** Copyright. */
    private static final String COPYRIGHT_NOTICE = "Copyright (c) International Business Machines Corporation, 2002-2005, Copyright (c) The University of Edinburgh, 2002-2009.";

    /** Number of tables to create. */
    private int mNumTables;

    /** Table prefix. */
    private String mTablePrefix;

    /**
     * Creates and populates MySQL database tables according to the
     * specified settings or the default settings if no arguments are
     * used.
     * 
     * @param args
     *     Client arguments.
     */
    public CreateTestMySQLTables(String[] args) {
        super(args);
    }

    /**
     * Creates and populates MySQL database tables according to the
     * specified settings or the default settings if no arguments are
     * used.
     * 
     * @param args
     *     Client arguments.
     */
    public static void main(String[] args) {
        new CreateTestMySQLTables(args);
    }

    /**
     * {@inheritDoc}
     */
    protected void setDefaultSettings() {
        mDBMS = "MySQL";
        mDriver = "org.gjt.mm.mysql.Driver";
        mHost = "localhost";
        mPort = "3306";
        mDatabase = "ogsadai";
        mUsername = "ogsadai";
        mPassword = "ogsadai";
        mTable = "course";
        mTablePrefix = mTable;
        mNumTables = 0;
        mNumberOfEntries = 0;
    }

    /**
     * {@inheritDoc}
     */
    protected String getConnectionURL() {
        return "jdbc:mysql://" + mHost + ":" + mPort + "/" + mDatabase;
    }

    /**
     * Drops the N tables with the table prefix.
     *
     * {@inheritDoc}
     */
    protected void dropTableIfExists(Connection connection) throws SQLException {
        Statement statement = connection.createStatement();
        System.out.println("Dropping table if it already exists");
        for (int i = 0; ((i < mNumTables) && (i < 10)); i++) {
            statement.execute("DROP TABLE IF EXISTS " + mTablePrefix + "0" + i);
            System.out.println("DROP TABLE IF EXISTS " + mTablePrefix + "0" + i);
        }
        for (int i = 10; i < mNumTables; i++) {
            statement.execute("DROP TABLE IF EXISTS " + mTablePrefix + i);
            System.out.println("DROP TABLE IF EXISTS " + mTablePrefix + i);
        }
        statement.close();
    }

    /**
     * Returns a <code>CREATE TABLE</code> statement for the
     * current table name.
     *
     * {@inheritDoc}
     */
    protected String getCreateTableStatement() {
        return "CREATE TABLE IF NOT EXISTS " + mTable + " (id INTEGER, name VARCHAR(64), address VARCHAR(128), phone VARCHAR(20))";
    }

    /**
     * Overrides <code>createTable</code> of the super-class.
     * Calls the <code>createTable</code> method of the super-class
     * once per number of tables to create. On each iteration sets the
     * <code>mTable</code> attribute to be the string formed by
     * <code>mTablePrefix</code> and N where N is 0..number of tables
     * minus one.
     *
     * {@inheritDoc}
     */
    public void createTable(Connection connection) throws SQLException {
        if (mNumberOfEntries > -1) {
            for (int i = 0; ((i < mNumTables) && (i < 10)); i++) {
                mTable = mTablePrefix + "0" + i;
                super.createTable(connection);
            }
            for (int i = 10; i < mNumTables; i++) {
                mTable = mTablePrefix + i;
                super.createTable(connection);
            }
        }
    }

    /**
     * Overrides the <code>setupArguments</code> method of the
     * super-class. Checks for the <code>-tables</code> argument
     * before calling the overridden method.
     *
     * {@inheritDoc} 
     */
    protected boolean setupArguments(String[] args) {
        Vector newArgs = new Vector();
        for (int i = 0; i < args.length; i += 2) {
            if (args[i].toLowerCase().equals("-tables")) {
                mNumTables = Integer.parseInt(args[i + 1]);
            } else if (args[i].toLowerCase().equals("-tablename")) {
                mTable = args[i + 1];
                mTablePrefix = args[i + 1];
            } else {
                newArgs.add(args[i]);
                newArgs.add(args[i + 1]);
            }
        }
        return super.setupArguments((String[]) newArgs.toArray(new String[newArgs.size()]));
    }

    /**
     * Overrides the <code>printVariables</code> method of the
     * super-class. Calls the overridden method then prints the
     * default value of <code>-tables</code> argument.
     *
     * {@inheritDoc} 
     */
    protected void printVariables(String prefix) {
        super.printVariables(prefix);
        System.out.println("\tNumberOfTablesToCreate:    \t" + mNumTables);
    }

    /**
     * Overrides the <code>printUsage</code> method of the
     * super-class. Calls the overridden method then prints the
     * <code>-tables</code> argument.
     *
     * {@inheritDoc} 
     */
    protected void printUsage() {
        super.printUsage();
        System.out.println("\t\t[-tables NumberOfTablesToCreate]");
    }
}
