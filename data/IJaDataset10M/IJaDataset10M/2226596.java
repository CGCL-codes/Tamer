package fi.hip.gb.gridbank.db.inmemory;

import java.util.*;
import fi.hip.gb.gridbank.AccountOwner;
import fi.hip.gb.gridbank.GridBankAccount;
import fi.hip.gb.gridbank.db.AccountStore;
import fi.hip.gb.gridbank.db.DBException;

/**
 * 
 * @author Tuomas.Nissi@hip.fi
 * @version $Id:
 */
public class AccountStoreInMemoryImpl implements AccountStore {

    private static AccountStore store = null;

    private Hashtable<String, GridBankAccount> accounts = null;

    private AccountStoreInMemoryImpl() {
        accounts = new Hashtable<String, GridBankAccount>();
    }

    /**
	 * Singleton
	 */
    public static synchronized AccountStore getInstance() {
        if (store == null) {
            store = new AccountStoreInMemoryImpl();
        }
        return store;
    }

    /**
	 *
	 */
    public GridBankAccount getAccount(String accountId) {
        return accounts.get(accountId);
    }

    /**
	 * Adds the accounts from the store to the AccountOwner.
	 * 
	 *  @throws DBException   	If one of the accounts fail the others may be 
	 *  						in place regardless of the excpetion. Check
	 *  						the result in case of an exception!
	 */
    public void getAccounts(AccountOwner owner) throws DBException {
        Collection<GridBankAccount> c = accounts.values();
        boolean errors = false;
        for (GridBankAccount gba : c) {
            if (gba.getAccountOwnerId().equals(owner.getOwnerId())) {
                owner.addAccount(gba);
            }
        }
        if (errors) {
            throw new DBException("Could not add one or more accounts to the " + "AccountOwner. Result should be checked.");
        }
    }

    public GridBankAccount[] getAccounts() throws DBException {
        return accounts.values().toArray(new GridBankAccount[0]);
    }

    /**
	 *
	 */
    public synchronized void storeAccount(GridBankAccount account) {
        accounts.put(account.getAccountId(), account);
    }

    /**
	 *
	 */
    public synchronized void removeAccount(GridBankAccount account) {
        accounts.remove(account.getAccountId());
    }
}
