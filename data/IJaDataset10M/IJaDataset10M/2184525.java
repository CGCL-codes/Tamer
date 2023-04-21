package ursus.server.plugins.core;

import java.util.List;

/**
 * Interface for core group feature in Ursus.
 * @author Anthony
 */
public interface Group extends Entity {

    /**
     * Adds a User to this group.
     *
     * @param user The User to add to this group.
     *
     */
    public void addUser(User user);

    /**
     * Removes a User from this group.
     *
     * @param user the User to be removed.
     */
    public void removeUser(User user);

    public boolean containsUser(User user);

    /**
     * Returns a List of User objects. 
     * So...
     * list = getUsers();
     * User user = (User)list.get(0);
     *
     * @return A list of User objects.
     *
     */
    public List getUsers();
}
