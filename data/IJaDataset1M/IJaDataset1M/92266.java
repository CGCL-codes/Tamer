package whf.framework.security.entity;

import java.util.HashSet;
import java.util.Set;
import whf.framework.entity.AbstractEntity;

/**
 * @author wanghaifeng
 *
 */
public class Group extends AbstractEntity {

    /**
	 * 包含的用户
	 */
    private Set<User> users = new HashSet<User>();

    /**
	 * 包含的角色
	 */
    private Set<Role> roles = new HashSet<Role>();

    public Set<User> getUsers() {
        return this.users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }

    /**
	 * whether a member in such a group
	 * @param user
	 * @return
	 */
    public boolean isMember(User user) {
        return this.users.contains(user);
    }

    /**
	 * @param user
	 */
    public void addUser(User user) {
        this.users.add(user);
    }

    /**
	 * @param user
	 */
    public void removeUser(User user) {
        this.users.remove(user);
    }

    public Set<Role> getRoles() {
        return this.roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    /**
	 * @param role
	 */
    public void addRole(Role role) {
        this.roles.add(role);
    }

    /**
	 * @param role
	 */
    public void removeRole(Role role) {
        this.roles.remove(role);
    }

    /**
	 * @modify wanghaifeng Aug 31, 2006 12:57:23 PM
	 * @param user
	 * @return
	 */
    public boolean contains(User user) {
        for (User u : users) {
            if (u.idEquals(user)) {
                return true;
            }
        }
        return false;
    }

    /**
	 * @modify wanghaifeng Aug 31, 2006 12:57:25 PM
	 * @param role
	 * @return
	 */
    public boolean contains(Role role) {
        for (Role r : roles) {
            if (r.idEquals(role)) {
                return true;
            }
        }
        return false;
    }
}
