package org.az.paccman.services;

import java.util.List;
import org.az.model.Account;
import org.az.model.User;
import org.az.model.User.Status;
import org.springframework.util.CollectionUtils;

/**
 * 
 * @author Markin
 *
 */
public class UserUtil {

    private UserUtil() {
    }

    /**
     * From the input list of the users return the first with account. If noone with account was
     * found, return the first user.
     * @param users original list of users.
     * @return first user with account, if none was found, first user by order, or null if list is empty.
     */
    public static User getRegisteredOrAnyUser(List<User> users) {
        User result = null;
        if (!CollectionUtils.isEmpty(users)) {
            result = users.get(0);
            for (User user : users) {
                if (user.getAccount() != null) {
                    result = user;
                    break;
                }
            }
        }
        return result;
    }

    public static Status getUserStatus(User user) {
        if (user == null) {
            return Status.NOUSER;
        } else if (user.getAccount() == null) {
            return Status.TEMPORAL;
        } else if (user.getAccount().getStatus().equals(Account.Status.NEW)) {
            return Status.PREACCOUNT;
        } else {
            return Status.ACCOUNT;
        }
    }
}
