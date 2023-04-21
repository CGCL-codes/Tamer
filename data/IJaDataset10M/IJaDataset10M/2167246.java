package com.earnware.james.userrepository;

import java.util.Iterator;
import org.apache.james.services.UsersRepository;
import org.apache.james.services.User;
import com.earnware.james.logger.EarnwareLogEnabled;

public abstract class ReadOnlyUsersRepository extends EarnwareLogEnabled implements UsersRepository {

    public boolean addUser(User user) {
        logger.error("addUser()");
        throw new UnsupportedOperationException();
    }

    public void addUser(String username, Object attributes) {
        logger.error("addUser()");
        throw new UnsupportedOperationException();
    }

    public boolean addUser(String username, String password) {
        logger.error("addUser()");
        throw new UnsupportedOperationException();
    }

    public boolean updateUser(User user) {
        logger.error("updateUser()");
        throw new UnsupportedOperationException();
    }

    public void removeUser(String name) {
        logger.error("removeUser()");
        throw new UnsupportedOperationException();
    }

    @SuppressWarnings("unchecked")
    public Iterator list() {
        logger.error("list()");
        throw new UnsupportedOperationException();
    }

    public int countUsers() {
        logger.error("countUsers()");
        throw new UnsupportedOperationException();
    }

    public User getUserByNameCaseInsensitive(String name) {
        logger.trace("getUserByNameCaseInsensitive()");
        return getUserByName(name);
    }

    public boolean containsCaseInsensitive(String name) {
        logger.trace("containsCaseInsensitive()");
        return contains(name);
    }
}
