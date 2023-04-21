package fi.hip.gb.gridbank.db.SQL;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;
import fi.hip.gb.gridbank.AccountOwner;
import fi.hip.gb.gridbank.GridBankAccount;
import fi.hip.gb.gridbank.cheques.GridCheque;
import fi.hip.gb.gridbank.db.AccountStore;
import fi.hip.gb.gridbank.db.DBException;
import org.apache.log4j.Logger;

/**
 * Database implementation for AccountStore...
 * TODO: Fetching the gridcheques from the database and adding
 * them to the account to be returned
 * @author Antti.Solonen@cern.ch
 * @version $Id:
 */
public class AccountStoreDBImpl implements AccountStore {

    static Logger logger = Logger.getLogger(AccountStoreDBImpl.class.getName());

    MySqlConnection mycon = null;

    private static AccountStore store = null;

    /**
	 * Opens a new database connection for the instance
	 */
    public AccountStoreDBImpl() throws DBException {
        MySqlSettings set = MySqlSettings.getSettings();
        try {
            this.mycon = new MySqlConnection(set);
        } catch (SQLException e) {
            throw new DBException("Database connection failed", e);
        }
    }

    /**
	 * Singleton,
	 * returns AccountStore -instance if possible, null otherwise
	 */
    public static synchronized AccountStore getInstance() {
        if (store == null) {
            try {
                store = new AccountStoreDBImpl();
            } catch (DBException e) {
                return null;
            }
        }
        return store;
    }

    /**
	 * Uses an existing database connection instance
	 * @param mycon
	 */
    public AccountStoreDBImpl(MySqlConnection mycon) {
        this.mycon = mycon;
    }

    /**
	 * Retrieves an account from the database,
	 * returns the account if found, null otherwise
	 * @param String accountID
	 * @return GridBankAccount (null if not found)
	 */
    public GridBankAccount getAccount(String accountID) {
        String query = "SELECT * FROM Accounts WHERE accountID='" + accountID + "'";
        GridCheque[] fundsProvisions = null;
        try {
            ResultSet rs = this.mycon.SQLQuery(query);
            rs.first();
            GridBankAccount returnable = new GridBankAccount(rs.getString("accountID"), rs.getString("accountOwnerID"), rs.getDouble("balance"), fundsProvisions, rs.getString("currency"));
            return returnable;
        } catch (Exception e) {
            return null;
        }
    }

    /**
	 * Adds all the owner's accounts found from the database to an owner -instance
	 * @param owner
	 * @throws DBException 	(if one or more adds fail)
	 */
    public void getAccounts(AccountOwner owner) throws DBException {
        String id = owner.getOwnerId();
        String query = "SELECT * FROM Accounts WHERE accountOwnerID='" + id + "'";
        logger.debug(query);
        owner.resetAccounts();
        try {
            ResultSet rs = this.mycon.SQLQuery(query);
            while (rs.next()) {
                GridCheque[] fundsProvisions = null;
                GridBankAccount addable = new GridBankAccount(rs.getString("accountID"), id, rs.getDouble("balance"), fundsProvisions, rs.getString("currency"));
                owner.addAccount(addable);
            }
        } catch (Exception e) {
            throw new DBException("Could not add one or more accounts to: " + owner.getOwnerName() + ". Result should be checked.");
        }
    }

    public GridBankAccount[] getAccounts() throws DBException {
        String query = "SELECT * FROM Accounts";
        Vector<GridBankAccount> v = new Vector<GridBankAccount>();
        try {
            ResultSet rs = this.mycon.SQLQuery(query);
            while (rs.next()) {
                GridCheque[] fundsProvisions = null;
                GridBankAccount addable = new GridBankAccount(rs.getString("accountID"), rs.getString("accountOwnerID"), rs.getDouble("balance"), fundsProvisions, rs.getString("currency"));
                v.add(addable);
            }
        } catch (Exception e) {
            throw new DBException("Database access failed.", e);
        }
        return v.toArray(new GridBankAccount[0]);
    }

    /**
	 * Stores an account to the database
	 * An account can only be added 
	 * to an existing user!
	 * @param GridBankAccount account
	 * @throws DBException 		If the query fails. 
	 */
    public void storeAccount(GridBankAccount account) throws DBException {
        String query = "INSERT INTO Accounts VALUES ('" + account.getAccountId() + "','" + account.getAccountOwnerId() + "','" + account.getBalance() + "','" + account.getCurrency() + "')";
        if (getAccount(account.getAccountId()) == null) {
            query = "INSERT INTO Accounts VALUES ('" + account.getAccountId() + "','" + account.getAccountOwnerId() + "','" + account.getBalance() + "','" + account.getCurrency() + "')";
        } else {
            query = "UPDATE Accounts SET accountOwnerID='" + account.getAccountOwnerId() + "', balance='" + account.getBalance() + "', currency='" + account.getCurrency() + "' WHERE accountID='" + account.getAccountId() + "'";
        }
        logger.debug(query);
        try {
            this.mycon.SQLUpdate(query);
        } catch (SQLException e) {
            throw new DBException("Storing the Account in query:" + query, e);
        }
    }

    /**
	 * Removes an account from the database,
	 * ALL THE CHEQUES RELATED TO THE USER
	 * ARE AUTOMATICALLY DELETED!
	 * @param GridBankAccount account
	 * @throws DBException 		If the query fails. 
	 */
    public void removeAccount(GridBankAccount account) throws DBException {
        String query = "DELETE FROM Accounts WHERE accountID='" + account.getAccountId() + "'";
        logger.debug(query);
        try {
            this.mycon.SQLUpdate(query);
        } catch (SQLException e) {
            throw new DBException("Deleting an Account failed in query:" + query + "\n " + "Messsage: " + e.getMessage(), e);
        }
    }
}
