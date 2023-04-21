package org.commsuite.dao;

import java.util.List;
import java.util.Set;
import org.commsuite.model.Action;
import org.commsuite.model.Role;
import org.commsuite.model.User;

/**
 * @since 1.0
 * @author Marek Musielak
 * @author Marcin Zduniak
 */
public interface UserDao {

    public List<User> getUsers();

    public User saveUser(User user);

    public User getUserByLoginAndPassword(String login, String password);

    public User getUser(Long id);

    public User getUserByName(String name);

    public void deleteUser(Long id);

    public void deleteUser(User user);

    public boolean hasRole(User user, Role role);

    public Set<Action> getActions(User user);

    public List<User> getMatchingUsers(User user, boolean enabledSearch);

    public List<User> getUsersByRoleId(Long roleId);
}
