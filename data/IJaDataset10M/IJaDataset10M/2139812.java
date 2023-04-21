package net.sf.arcus_judge;

import com.sleepycat.je.DatabaseException;
import com.sleepycat.persist.EntityStore;
import com.sleepycat.persist.PrimaryIndex;

public class AccountManager {

    private static AccountManager theManager = new AccountManager();

    public static AccountManager getManager() {
        return theManager;
    }

    private EntityStore accountStore;

    private PrimaryIndex<String, Account> index;

    private AccountManager() {
        try {
            accountStore = DB.getDB().openEntityStore("account");
            index = accountStore.getPrimaryIndex(String.class, Account.class);
            Account root = index.get(Account.ROOT_USERNAME);
            if (root == null) index.put(Account.createDefaultRootAccount());
        } catch (DatabaseException e) {
            throw new RuntimeException("An exception raised in opening account database", e);
        }
    }

    public Account getAccountByUsername(String username) {
        try {
            return index.get(username);
        } catch (DatabaseException e) {
            throw new RuntimeException(e);
        }
    }

    public Account authenticate(String username, String password) {
        Account account = getAccountByUsername(username);
        if (account == null) return null;
        if (!account.matchPassword(password)) return null;
        return account;
    }

    public Account updateAccount(Account account) {
        try {
            return index.put(account);
        } catch (DatabaseException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean removeAccount(Account account) {
        try {
            return index.delete(account.getUsername());
        } catch (DatabaseException e) {
            throw new RuntimeException(e);
        }
    }
}
