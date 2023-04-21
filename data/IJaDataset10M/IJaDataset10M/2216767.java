package de.spotnik.mail.core.accounts;

import de.spotnik.mail.core.model.UserAccount;

/**
 * UserSession.
 * 
 * @author Jens Rehp�hler
 * @since 16.10.2006
 */
public final class UserSession {

    /** the singleton user account of this session */
    private static ThreadLocal<UserAccount> userAccount = new ThreadLocal<UserAccount>();

    /**
     * set the user account of this session
     * 
     * @param account the user account
     */
    public static synchronized void setCurrentAccount(UserAccount account) {
        userAccount.set(account);
    }

    /**
     * get the user account of this session
     * 
     * @return the current user account
     */
    public static UserAccount getCurrentAccount() {
        return userAccount.get();
    }
}
