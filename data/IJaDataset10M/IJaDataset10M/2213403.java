package com.cube42.echoverse.account;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.MessageFormat;
import com.cube42.util.database.DBConnectionStatus;
import com.cube42.util.database.DBConnectionThread;
import com.cube42.util.database.DBConnector;
import com.cube42.util.database.JDBCUtils;
import com.cube42.util.exception.Cube42Exception;
import com.cube42.util.exception.Cube42NullParameterException;
import com.cube42.util.logging.LogSeverity;
import com.cube42.util.logging.Logger;
import com.cube42.util.properties.PropertyFileIO;
import com.cube42.util.properties.PropertyStore;
import com.cube42.util.text.StringUtils;

/**
 * Class responsible for storing all account information in the database
 *
 * @author  Matt Paulin
 * @version $Id: AccountStore.java,v 1.3 2003/03/12 01:48:33 zer0wing Exp $
 */
public class AccountStore implements DBConnector {

    /**
     * Properties of the AccountManager
     */
    static final PropertyStore ACCOUNT_MANAGER_PROPERTIES = PropertyFileIO.loadFromCompressedSystemFiles("AccountManager.xml");

    /**
     * The driver to use to connect to the database
     */
    private static final String JDBC_DRIVER = ACCOUNT_MANAGER_PROPERTIES.getStringProperty("JDBCDriver").getValue();

    /**
     * The host that the database is running on
     */
    private static final String DB_HOST = ACCOUNT_MANAGER_PROPERTIES.getStringProperty("DBHost").getValue();

    /**
     * The port that the database is running on
     */
    private static final int DB_PORT = ACCOUNT_MANAGER_PROPERTIES.getIntProperty("DBPort").getValue();

    /**
     * The name of the database to connect to
     */
    private static final String DB_NAME = ACCOUNT_MANAGER_PROPERTIES.getStringProperty("DBName").getValue();

    /**
     * The Login Name to use when connecting to the database
     */
    private static final String DB_LOGIN_NAME = ACCOUNT_MANAGER_PROPERTIES.getStringProperty("DBLoginName").getValue();

    /**
     * The password to use when connecting to the database
     */
    private static final String DB_PASSWORD = ACCOUNT_MANAGER_PROPERTIES.getStringProperty("DBPassword").getValue();

    /**
     * The name of the table for the accounts in the database
     */
    private static final String ACCOUNT_TABLE_NAME = ACCOUNT_MANAGER_PROPERTIES.getStringProperty("AccountTableName").getValue();

    /**
     * The column name for the Username in the database
     */
    public static final String USERNAME_COLUMN_NAME = ACCOUNT_MANAGER_PROPERTIES.getStringProperty("UsernameColumnName").getValue();

    /**
     * Maximum length of the username to store in the database
     */
    public static final int USERNAME_COLUMN_LEN = ACCOUNT_MANAGER_PROPERTIES.getIntProperty("UsernameColumnLength").getValue();

    /**
     * The column name for the password in the database
     */
    public static final String PASSWORD_COLUMN_NAME = ACCOUNT_MANAGER_PROPERTIES.getStringProperty("PasswordColumnName").getValue();

    /**
     * Maximum length of the password to store in the database
     */
    public static final int PASSWORD_COLUMN_LEN = ACCOUNT_MANAGER_PROPERTIES.getIntProperty("PasswordColumnLength").getValue();

    /**
     * The column name for the shell names in the database
     */
    public static final String SHELL_NAMES_COLUMN_NAME = ACCOUNT_MANAGER_PROPERTIES.getStringProperty("ShellNamesColumnName").getValue();

    /**
     * The reconnect rate that the thread will attempt to reconnect in
     * milliseconds
     */
    private static final long RECONNECT_RATE = ACCOUNT_MANAGER_PROPERTIES.getLongProperty("AccountStoreReconnectRate").getValue();

    /**
     * The SQL string used to create the table
     */
    static final String CREATE_TABLE_SQL = "CREATE TABLE IF NOT EXISTS " + ACCOUNT_TABLE_NAME + " ( " + USERNAME_COLUMN_NAME + " VARCHAR(" + USERNAME_COLUMN_LEN + "), " + PASSWORD_COLUMN_NAME + " VARCHAR(" + PASSWORD_COLUMN_LEN + "), " + SHELL_NAMES_COLUMN_NAME + " TEXT)";

    /**
     * The SQL string used to set the index on the date
     */
    static final String CREATE_INDEX_SQL = "CREATE INDEX account_index ON " + ACCOUNT_TABLE_NAME + " (" + USERNAME_COLUMN_NAME + ")";

    /**
     * The SQL string used to delete the account table
     */
    static final String DELETE_TABLE_SQL = "DROP TABLE " + ACCOUNT_TABLE_NAME;

    /**
     * The SQL string used to remove all accounts from the table
     */
    static final String CLEAR_TABLE_SQL = "DELETE FROM " + ACCOUNT_TABLE_NAME;

    /**
     * The SQL string used to make the prepared insert statement
     */
    static final String INSERT_ACCOUNT_SQL = "INSERT INTO " + ACCOUNT_TABLE_NAME + " VALUES (?,?,?)";

    /**
     * The SQL string used to update an accounts information
     * <p>
     * parameter 1 is the new password
     * parameter 2 is the shell names
     * parameter 3 is the username
     */
    static final String UPDATE_ACCOUNT_SQL = "UPDATE " + ACCOUNT_TABLE_NAME + " SET " + PASSWORD_COLUMN_NAME + " = ''{0}'', " + SHELL_NAMES_COLUMN_NAME + " = ''{1}'' " + "WHERE " + USERNAME_COLUMN_NAME + " = ''{2}''";

    /**
     * The SQL string used to delete an account
     * <p>
     * parameter 1 is the username
     */
    static final String DELETE_ACCOUNT_SQL = "DELETE FROM " + ACCOUNT_TABLE_NAME + " WHERE " + USERNAME_COLUMN_NAME + " = ''{0}''";

    /**
     * The SQL string used to select a specific account
     * <p>
     * parameter 1 is the username
     */
    static final String SELECT_SPECIFIC_ACCOUNT_SQL = "SELECT * FROM " + ACCOUNT_TABLE_NAME + " WHERE " + USERNAME_COLUMN_NAME + " = ''{0}''";

    /**
     * The SQL string used to query all the accounts from the database
     */
    static final String SELECT_ALL_ACCOUNT_SQL = "SELECT * FROM " + ACCOUNT_TABLE_NAME;

    /**
     * JDBC connection to the database
     */
    private Connection con = null;

    /**
     * Status of the database that the AccountStore is connected too
     */
    private DBConnectionStatus status;

    /**
     * Connection thread for maintaining the connection
     */
    private DBConnectionThread dbConnectionThread;

    /**
     * Constructs the AccountStore
     */
    public AccountStore() {
        this.status = DBConnectionStatus.DISCONNECTED;
        try {
            JDBCUtils.loadDBDriver(JDBC_DRIVER);
            dbConnectionThread = new DBConnectionThread(this, RECONNECT_RATE, JDBCUtils.formURL(DB_HOST, DB_PORT, DB_NAME, DB_LOGIN_NAME, DB_PASSWORD));
        } catch (Cube42Exception e) {
            e.log(LogSeverity.FATAL);
        }
    }

    /**
     * Updates the DBConnector to the new status of the database connection
     *
     * @param   status  A connection status class describing the situation
     */
    public void setStatus(DBConnectionStatus status) {
        this.status = status;
    }

    /**
     * Returns the Database connection status
     *
     * @return  The database connection status
     */
    public DBConnectionStatus getStatus() {
        return this.status;
    }

    /**
     * Notifies the DBConnector that the database has been connected too
     *
     * @param   con  A good connection to the database
     */
    public void databaseConnected(Connection con) {
        this.con = con;
        this.initialize();
    }

    /**
     * Returns true if the AccountStore is connected
     *
     * @return  true    if the account store is connected
     */
    public boolean isConnected() {
        if (this.dbConnectionThread == null) return false;
        if (con == null) return false;
        return this.dbConnectionThread.isConnected();
    }

    /**
     * Sets up the tables in the database.
     */
    public void initialize() {
        if (this.isConnected()) {
            try {
                con.setAutoCommit(true);
                Statement stmt = con.createStatement();
                stmt.execute(AccountStore.CREATE_TABLE_SQL);
                try {
                    stmt.execute(AccountStore.CREATE_INDEX_SQL);
                } catch (SQLException e) {
                }
                stmt.close();
            } catch (SQLException e) {
                Logger.error(AccountSystemCodes.ACCOUNT_STORE_INIT_SQL_EXCEPTION, new Object[] { e.getMessage() });
                this.dbConnectionThread.resetConnection();
            }
        }
    }

    /**
     * Shuts the store down
     */
    public void shutdown() {
        try {
            this.dbConnectionThread.kill();
            con.close();
        } catch (SQLException e) {
            Logger.error(AccountSystemCodes.ACCOUNT_STORE_SHUTDOWN_SQL_EXCEPTION, new Object[] { e.getMessage() });
            this.dbConnectionThread.resetConnection();
        }
    }

    /**
     * Deletes all accounts
     *
     * @throws  Cube42Exception if this is not possible
     */
    public void deleteAllAccounts() throws Cube42Exception {
        if (this.isConnected()) {
            try {
                Statement stmt = con.createStatement();
                stmt.execute(AccountStore.CLEAR_TABLE_SQL);
                stmt.close();
            } catch (SQLException e) {
                Logger.error(AccountSystemCodes.ACCOUNT_STORE_CLEAR_SQL_EXCEPTION, new Object[] { e.getMessage() });
            }
        } else {
            throw new Cube42Exception(AccountSystemCodes.DATABASE_CONNECTION_LOST, new Object[] { "delete all accounts" });
        }
    }

    /**
     * Creates a new account
     *
     * @param   account     The new account to add to the store
     * @throws  Cube42Exception if the account already exist
     */
    public void createNewAccount(Account account) throws Cube42Exception {
        Cube42NullParameterException.checkNull(account, "account", "createNewAccount", this);
        PreparedStatement insertStatement;
        if (this.accountExist(account)) {
            throw new Cube42Exception(AccountSystemCodes.ACCOUNT_EXIST_ALREADY, new Object[] { account.getUsername() });
        }
        if (this.isConnected()) {
            try {
                insertStatement = con.prepareStatement(AccountStore.INSERT_ACCOUNT_SQL);
                insertStatement.setString(1, StringUtils.trimString(account.getUsername(), USERNAME_COLUMN_LEN));
                insertStatement.setString(2, StringUtils.trimString(account.getPassword(), PASSWORD_COLUMN_LEN));
                insertStatement.setString(3, account.getShellNames().flatten());
                insertStatement.execute();
                insertStatement.close();
            } catch (SQLException e) {
                Logger.error(AccountSystemCodes.INSERT_ACCOUNT_SQL_EXCEPTION, new Object[] { e.getMessage() });
            }
        } else {
            throw new Cube42Exception(AccountSystemCodes.DATABASE_CONNECTION_LOST, new Object[] { "create new account" });
        }
    }

    /**
     * Deletes the specified account
     *
     * @param   account     The account to delete
     * @throws  Cube42Exception if no account like this exist
     */
    public void deleteAccount(Account account) throws Cube42Exception {
        Cube42NullParameterException.checkNull(account, "account", "deleteAccount", this);
        if (!this.accountExist(account)) {
            throw new Cube42Exception(AccountSystemCodes.ACCOUNT_DOES_NOT_EXIST, new Object[] { account.getUsername() });
        }
        if (this.isConnected()) {
            String statement = MessageFormat.format(AccountStore.DELETE_ACCOUNT_SQL, new Object[] { account.getUsername() });
            try {
                Statement stmt = con.createStatement();
                stmt.execute(statement);
                stmt.close();
            } catch (SQLException e) {
                Logger.error(AccountSystemCodes.ACCOUNT_STORE_DELETE_SQL_EXCEPTION, new Object[] { e.getMessage() });
            }
        } else {
            throw new Cube42Exception(AccountSystemCodes.DATABASE_CONNECTION_LOST, new Object[] { "delete account" });
        }
    }

    /**
     * Updates the specified account with the new information
     *
     * @param   account     The account to update
     * @throws  Cube42Exception if this account doesn't exist
     */
    public void updateAccount(Account account) throws Cube42Exception {
        Cube42NullParameterException.checkNull(account, "account", "updateAccount", this);
        if (!this.accountExist(account)) {
            throw new Cube42Exception(AccountSystemCodes.ACCOUNT_DOES_NOT_EXIST, new Object[] { account.getUsername() });
        }
        if (this.isConnected()) {
            String statement = MessageFormat.format(AccountStore.UPDATE_ACCOUNT_SQL, new Object[] { account.getPassword(), account.getShellNames().flatten(), account.getUsername() });
            try {
                Statement stmt = con.createStatement();
                stmt.execute(statement);
                stmt.close();
            } catch (SQLException e) {
                Logger.error(AccountSystemCodes.ACCOUNT_STORE_UPDATE_SQL_EXCEPTION, new Object[] { e.getMessage() });
            }
        } else {
            throw new Cube42Exception(AccountSystemCodes.DATABASE_CONNECTION_LOST, new Object[] { "update account" });
        }
    }

    /**
     * Returns the account with the specified username
     * <p>
     * Returns null if the account doesn't exist
     *
     * @param   username    The username for the account desired
     * @throws  Cube42Exception if the database is disconnected
     */
    public Account getAccount(String username) throws Cube42Exception {
        Account account = null;
        Cube42NullParameterException.checkNull(username, "username", "getAccount", this);
        if (this.isConnected()) {
            String statement = MessageFormat.format(AccountStore.SELECT_SPECIFIC_ACCOUNT_SQL, new Object[] { username });
            try {
                Statement stmt = con.createStatement();
                stmt.execute(statement);
                ResultSet rs = stmt.executeQuery(statement);
                try {
                    if (rs.next()) {
                        account = new Account();
                        account.setUsername(rs.getString(AccountStore.USERNAME_COLUMN_NAME));
                        account.setPassword(rs.getString(AccountStore.PASSWORD_COLUMN_NAME));
                        account.setShellNames(new ShellNameCollection(rs.getString(AccountStore.SHELL_NAMES_COLUMN_NAME)));
                        if (rs.next()) {
                            Logger.error(AccountSystemCodes.DUPLICATE_ACCOUNTS, new Object[] { username });
                        }
                    }
                } catch (SQLException e) {
                    throw new Cube42Exception(AccountSystemCodes.CANNOT_ACCESS_RESULT_SET, new Object[] { e.getMessage() });
                }
                stmt.close();
            } catch (SQLException e) {
                Logger.error(AccountSystemCodes.ACCOUNT_STORE_SELECT_SPECIFIC_SQL_EXCEPTION, new Object[] { e.getMessage() });
            }
        } else {
            throw new Cube42Exception(AccountSystemCodes.DATABASE_CONNECTION_LOST, new Object[] { "get account" });
        }
        return account;
    }

    /**
     * Returns an account collection containing all accounts
     *
     * @return  Account collection containing all accounts
     * @throws  Cube42Exception if this operation is not possible
     */
    public AccountCollection getAccounts() throws Cube42Exception {
        AccountCollection acollection = new AccountCollection();
        if (this.isConnected()) {
            try {
                Statement stmt = con.createStatement();
                ResultSet rs = stmt.executeQuery(SELECT_ALL_ACCOUNT_SQL);
                Account account;
                try {
                    while (rs.next()) {
                        account = new Account();
                        account.setUsername(rs.getString(AccountStore.USERNAME_COLUMN_NAME));
                        account.setPassword(rs.getString(AccountStore.PASSWORD_COLUMN_NAME));
                        account.setShellNames(new ShellNameCollection(rs.getString(AccountStore.SHELL_NAMES_COLUMN_NAME)));
                        acollection.addAccount(account);
                    }
                } catch (SQLException e) {
                    throw new Cube42Exception(AccountSystemCodes.CANNOT_ACCESS_RESULT_SET, new Object[] { e.getMessage() });
                }
                stmt.close();
            } catch (SQLException e) {
                Logger.error(AccountSystemCodes.ACCOUNT_STORE_QUERY_SQL_EXCEPTION, new Object[] { e.getMessage() });
            }
        } else {
            throw new Cube42Exception(AccountSystemCodes.DATABASE_CONNECTION_LOST, new Object[] { "get accounts" });
        }
        return acollection;
    }

    /**
     * Returns true if an account with the specified username exist
     *
     * @param   account The account to check for
     * @return  true    if the account exist
     * @throws  Cube42Exception if the database is disconnected
     */
    private boolean accountExist(Account account) throws Cube42Exception {
        Cube42NullParameterException.checkNull(account, "account", "accountExist", this);
        if (this.getAccount(account.getUsername()) == null) return false;
        return true;
    }
}
