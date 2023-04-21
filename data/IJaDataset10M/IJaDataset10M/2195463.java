package org.openaion.loginserver.controller;

import java.sql.Timestamp;
import org.openaion.commons.database.dao.DAOManager;
import org.openaion.loginserver.dao.AccountTimeDAO;
import org.openaion.loginserver.model.Account;
import org.openaion.loginserver.model.AccountTime;

/**
 * This class is for account time controlling.
 * When character logins any server, it should get
 * its day online time and rest time. Some aion ingame
 * feautres also depend on player's online time
 *
 * @author EvilSpirit
 */
public class AccountTimeController {

    /**
	 * Update account time when character logins.
	 * The following field are being updated:
	 * - LastLoginTime (set to CurrentTime)
	 * - RestTime (set to (RestTime + (CurrentTime-LastLoginTime - SessionDuration))
	 * @param account
	 */
    public static void updateOnLogin(Account account) {
        AccountTime accountTime = account.getAccountTime();
        if (accountTime == null) {
            accountTime = new AccountTime();
        }
        int lastLoginDay = getDays(accountTime.getLastLoginTime().getTime());
        int currentDay = getDays(System.currentTimeMillis());
        if (lastLoginDay < currentDay) {
            accountTime.setAccumulatedOnlineTime(0);
            accountTime.setAccumulatedRestTime(0);
        } else {
            long restTime = System.currentTimeMillis() - accountTime.getLastLoginTime().getTime() - accountTime.getSessionDuration();
            accountTime.setAccumulatedRestTime(accountTime.getAccumulatedRestTime() + restTime);
        }
        accountTime.setLastLoginTime(new Timestamp(System.currentTimeMillis()));
        DAOManager.getDAO(AccountTimeDAO.class).updateAccountTime(account.getId(), accountTime);
        account.setAccountTime(accountTime);
    }

    /**
	 * Update account time when character logouts.
	 * The following field are being updated:
	 * - SessionTime (set to CurrentTime - LastLoginTime)
	 * - AccumulatedOnlineTime (set to AccumulatedOnlineTime + SessionTime)
	 * @param account
	 */
    public static void updateOnLogout(Account account) {
        AccountTime accountTime = account.getAccountTime();
        accountTime.setSessionDuration(System.currentTimeMillis() - accountTime.getLastLoginTime().getTime());
        accountTime.setAccumulatedOnlineTime(accountTime.getAccumulatedOnlineTime() + accountTime.getSessionDuration());
        DAOManager.getDAO(AccountTimeDAO.class).updateAccountTime(account.getId(), accountTime);
        account.setAccountTime(accountTime);
    }

    /**
	 * Checks if account is already expired or not
	 * @param account
	 * @return true, if account is expired, false otherwise
	 */
    public static boolean isAccountExpired(Account account) {
        AccountTime accountTime = account.getAccountTime();
        return accountTime != null && accountTime.getExpirationTime() != null && accountTime.getExpirationTime().getTime() < System.currentTimeMillis();
    }

    /**
	 * Checks if account is restricted by penalty or not
	 * @param account
	 * @return true, is penalty is active, false otherwise
	 */
    public static boolean isAccountPenaltyActive(Account account) {
        AccountTime accountTime = account.getAccountTime();
        return accountTime != null && accountTime.getPenaltyEnd() != null && (accountTime.getPenaltyEnd().getTime() == 1000 || accountTime.getPenaltyEnd().getTime() >= System.currentTimeMillis());
    }

    /**
	 * Get days from time presented in milliseconds
	 * @param millis time in ms
	 * @return  days
	 */
    public static int getDays(long millis) {
        return (int) (millis / 1000 / 3600 / 24);
    }
}
