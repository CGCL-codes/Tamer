package org.systemsbiology.apps.corragui.server.provider.user;

import java.util.List;
import org.systemsbiology.apps.corragui.domain.User;

public interface IUserInfoProvider {

    public abstract User authenticateUser(String userLoginName, String password);

    public abstract User getUser(String userLoginName);

    public abstract boolean validatePassword(User user, String password);

    public abstract boolean changeUserPassword(User user, String newPassword);

    public abstract boolean addUser(User user);

    public abstract boolean deleteUser(User user);

    public abstract List<User> getUsers();
}
