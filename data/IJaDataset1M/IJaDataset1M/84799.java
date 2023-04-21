package users;

import java.io.*;
import java.util.*;
import users.events.*;
import users.swing.*;
import util.*;

/**
  * Manages users, their capabilities and their associations to other objects.
  *
  * <p>The UserManager provides possibilities to manage any number of users. Users can
  * be added, retrieved using their name to identify them, and removed from the system.
  * Additionally, users can be associated to, and disassociated from, any object. Their can be a maximum of
  * one user associated to any given object at any given time.</p>
  *
  * <p>You can provide a set of default capabilities, that every new user will be provided with.</p>
  *
  * @see User
  * @see #setDefaultCaps
  * @see sale.SalesPoint
  * @see #logOn
  *
  * @author Steffen Zschaler
  * @version 2.0 05/05/1999
  * @since v2.0
  */
public class UserManager extends Object implements Serializable {

    /**
    * The map used to store all the users managed by this UserManager.
    *
    * <p>The user's name is being used as the key. The user itself is being stored as
    * the value.</p>
    *
    * @see User
    *
    * @serial
    */
    private SortedMap m_mpUsers = new TreeMap();

    /**
    * The map used to store all the users currently associated to some object.
    *
    * <p>The key of the map is the object to which a given user is associated.
    * The user itself is being stored as the value.</p>
    *
    * @see User
    * @see #logOn
    *
    * @serial
    */
    private Map m_mpCurrentUsers = new HashMap();

    /**
    * The map of default capabilities to be associated with each new user.
    *
    * <p>The keys and values are being used in the same way as {@link User} uses them, i.e.
    * the capabilities names are used as keys and the capabilities themselves as
    * values.</p>
    *
    * @see #setDefaultCaps
    * @see Capability
    * @see User#setCapabilities
    *
    * @serial
    */
    private Map m_mpDefaultCaps = new HashMap();

    /**
    * The factory used to create new User objects.
    *
    * @see #createUser
    * @see #setUserCreator
    * @see User
    *
    * @serial
    */
    private UserCreator m_ucCreator;

    /**
    * The list of listeners registered with this UserManager.
    *
    * @serial See <a href="#util.ListenerHelper">ListenerHelper's serializable form</a> for more information on
    * what listeners get a chance to be serialized.
    */
    protected ListenerHelper m_lhListeners = new ListenerHelper();

    /**
    * Create a new UserManager with an empty set of default capabilities, managing direct
    * instances of the User class.
    *
    * @see #setDefaultCaps
    * @see #createUser
    */
    public UserManager() {
        this(null, null);
    }

    /**
    * Create a new UserManager, using a specific set of default capabilities. This
    * UserManager will manage direct instances of User.
    *
    * @param mpDefaultCaps a map describing the default capabilities any new user should
    * have. The keys of this map should be the name of their associated capability. Specifying
    * <code>null</code> will result in there being no default capabilities.
    *
    * @see #setDefaultCaps
    * @see #createUser
    * @see Capability
    */
    public UserManager(Map mpDefaultCaps) {
        this(mpDefaultCaps, null);
    }

    /**
    * Create a new UserManager with an empty set of default capabilities. This UserManager
    * will create instances of a developer defined subclass of User.
    *
    * @param ucCreator the factory to be used to create new User objects. Specifying
    * <code>null</code> will result in a default implementation being used, which will
    * create direct instances of the User class.
    *
    * @see #setDefaultCaps
    * @see #createUser
    */
    public UserManager(UserCreator ucCreator) {
        this(null, ucCreator);
    }

    /**
    * Create a new UserManager providing both a set of default capabilities and a User
    * creation factory.
    *
    * @param mpDefaultCaps a map describing the default capabilities any new user should
    * have. The keys of this map should be the name of their associated capability. Specifying
    * <code>null</code> will result in there being no default capabilities.
    * @param ucCreator the factory to be used to create new User objects. Specifying
    * <code>null</code> will result in a default implementation being used, which will
    * create direct instances of the User class.
    *
    * @see #setDefaultCaps
    * @see Capability
    * @see #createUser
    * @see User
    */
    public UserManager(Map mpDefaultCaps, UserCreator ucCreator) {
        super();
        setDefaultCaps(mpDefaultCaps);
        setUserCreator(ucCreator);
    }

    /**
    * Set the factory to be used when creating new User objects.
    *
    * @param ucCreator the factory to be used to create new User objects. Specifying
    * <code>null</code> will result in a default implementation being used, which will
    * create direct instances of the User class.
    *
    * @see #createUser
    * @see User
    *
    * @override Never
    */
    public synchronized void setUserCreator(UserCreator ucCreator) {
        if (ucCreator != null) {
            m_ucCreator = ucCreator;
        } else {
            m_ucCreator = new UserCreator();
        }
    }

    /**
    * Specify the set of default capabilities to be used when creating new User objects.
    *
    * <p>Calling this method will not influence the capabilities of users already created.</p>
    *
    * @param mpDefaultCaps a map describing the default capabilities any new user should
    * have. The keys of this map should be the name of their associated capability. Specifying
    * <code>null</code> will result in there being no default capabilities.
    *
    * @see #createUser
    * @see Capability
    *
    * @override Never
    */
    public synchronized void setDefaultCaps(Map mpDefaultCaps) {
        if (mpDefaultCaps != null) {
            m_mpDefaultCaps = new HashMap(mpDefaultCaps);
        } else {
            m_mpDefaultCaps = new HashMap();
        }
    }

    /**
    * Set a capability to be used as a default capability henceforward.
    *
    * <p>Calling this method will not influence the capabilities of users already created.</p>
    *
    * <p>If a default capability of the same name as the one given did already exist, it
    * will be replaced by the new capability.</p>
    *
    * @param cap the capability to be set as a default capability.
    *
    * @see #createUser
    *
    * @override Never
    */
    public synchronized void setDefaultCapability(Capability cap) {
        m_mpDefaultCaps.put(cap.getName(), cap);
    }

    /**
    * Create a new user to be managed by this UserManager.
    *
    * <p>This method uses the defined {@link UserCreator} (see {@link #setUserCreator}) to create the
    * new <code>User</code> object. The new user will later be accessible using its name.</p>
    *
    * <p>The newly created user will get all the default capabilities defined at the time
    * this method is called.</p>
    *
    * <p>A <code>userAdded</code> event will be received by any {@link UserDataListener} that
    * registered an interest in this <code>UserManager</code>.</p>
    *
    * @param sName the name of the new user. This must be unique, i.e. there must not be
    * a user with the same name already managed by this UserManager.
    *
    * @return the newly created user.
    *
    * @exception DuplicateUserException if there already was a user with the given name.
    *
    * @see #setDefaultCaps
    * @see #setUserCreator
    * @see User
    * @see UserCreator#createUser
    * @see users.events.UserDataListener#userAdded
    *
    * @override Never Rather than overriding this method, you should provide a new {@link UserCreator}.
    */
    public synchronized User createUser(String sName) {
        if (m_mpUsers.containsKey(sName)) {
            throw new DuplicateUserException("User \"" + sName + "\" already exists ! Cannot have two users with the same user name.");
        }
        User usr = m_ucCreator.createUser(sName);
        usr.setCapabilities(m_mpDefaultCaps);
        m_mpUsers.put(sName, usr);
        fireUserAdded(usr);
        return usr;
    }

    /**
    * Add a user to the UserManager.
    *
    * <p>A <code>userAdded</code> event will be received by any {@link UserDataListener} that
    * registered an interest in this UserManager.</p>
    *
    * @param usr the user to be added.
    *
    * @exception DuplicateUserException if there already is a user of the same name.
    *
    * @see users.events.UserDataListener#userAdded
    *
    * @override Never
    */
    public synchronized void addUser(User usr) {
        if (m_mpUsers.containsKey(usr.getName())) {
            throw new DuplicateUserException("User \"" + usr.getName() + "\" already exists ! Cannot have two users with the same user name.");
        }
        m_mpUsers.put(usr.getName(), usr);
        fireUserAdded(usr);
    }

    /**
    * Retrieve a user by name.
    *
    * <p>If no user with the given name exists, this method will return <code>null</code>.</p>
    *
    * @param sName the name of the user looked for.
    *
    * @return the user corresponding to the given name, if any. <code>null</code> will
    * be returned if no such user can be found.
    *
    * @see #createUser
    *
    * @override Never
    */
    public synchronized User getUser(String sName) {
        return (User) m_mpUsers.get(sName);
    }

    /**
    * Return all user names registered with this UserManager.
    *
    * <p>The returned set is backed by the UserManager, i.e. it will reflect changes
    * made through <code>createUser()</code> or <code>removeUser()</code>. The set itself
    * is unmodifiable and ordered alphabetically.</p>
    *
    * @return an unmodifiable, ordered set of all user names in this UserManager.
    *
    * @see #createUser
    * @see #deleteUser
    *
    * @override Never
    */
    public synchronized Set getUserNames() {
        return Collections.unmodifiableSet(m_mpUsers.keySet());
    }

    /**
    * Return all users registered with this UserManager.
    *
    * <p>The returned collection is backed by the UserManager, i.e. it will reflect
    * changes made through <code>createUser()</code> or <code>removeUser()</code>. The
    * collection itself is unmodifiable and ordered alphabetically by the users' names.</p>
    *
    * @return an unmodifiable, ordered set of all users in this UserManager.
    *
    * @see #createUser
    * @see #deleteUser
    *
    * @override Never
    */
    public synchronized Collection getUsers() {
        return Collections.unmodifiableCollection(m_mpUsers.values());
    }

    /**
    * Delete a user from this UserManager.
    *
    * <p>The user will be removed from the UserManager and will no longer be available
    * via {@link #getUser}. A <code>userDeleted</code> event will be received by all
    * {@link UserDataListener UserDataListeners} registered with this UserManager if a user was removed. If no user
    * with the given name existed no exception will be thrown and no event will be fired.</p>
    *
    * <p>If the user is currently associated to some object, it will stay so until
    * it is disassociated explicitly. It will not have the possibility to log in again,
    * though.</p>
    *
    * @param sName the name of the user to be removed
    *
    * @return the user that was just removed or <code>null</code> if none.
    *
    * @see users.events.UserDataListener#userDeleted
    *
    * @override Never
    */
    public synchronized User deleteUser(String sName) {
        User usrReturn = (User) m_mpUsers.remove(sName);
        if (usrReturn != null) {
            fireUserDeleted(usrReturn);
        }
        return usrReturn;
    }

    /**
    * Add a UserDataListener. UserDataListeners will receive an event whenever a user
    * was created or removed.
    *
    * @param udl the UserDataListener to add.
    *
    * @override Never
    */
    public void addUserDataListener(UserDataListener udl) {
        m_lhListeners.add(UserDataListener.class, udl);
    }

    /**
    * Remove a UserDataListener.
    *
    * @param udl the UserDataListener to remove.
    *
    * @override Never
    */
    public void removeUserDataListener(UserDataListener udl) {
        m_lhListeners.remove(UserDataListener.class, udl);
    }

    /**
    * Fire a <code>userAdded</code> event to all interested listeners.
    *
    * @param usr the user that was added.
    *
    * @override Never
    */
    protected void fireUserAdded(User usr) {
        UserDataEvent ude = null;
        Object[] listeners = m_lhListeners.getListenerList();
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == UserDataListener.class) {
                if (ude == null) ude = new UserDataEvent(this, usr);
                ((UserDataListener) listeners[i + 1]).userAdded(ude);
            }
        }
    }

    /**
    * Fire a <code>userDeleted</code> event to all interested listeners.
    *
    * @param usr the user that was deleted.
    *
    * @override Never
    */
    protected void fireUserDeleted(User usr) {
        UserDataEvent ude = null;
        Object[] listeners = m_lhListeners.getListenerList();
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == UserDataListener.class) {
                if (ude == null) ude = new UserDataEvent(this, usr);
                ((UserDataListener) listeners[i + 1]).userDeleted(ude);
            }
        }
    }

    /**
    * Associate a user with an object.
    *
    * <p>Only one user at a time can be associated with one Object. If there is already
    * another user associated with the given Object, its association is undone and the
    * user is returned.</p>
    *
    * <p>Only users that are actually managed by this UserManager can be logged in. An
    * exception will be thrown if you try to log in a user that is unknown to this
    * UserManager. Especially, this can happen if you try to log in a user that was
    * previously removed using {@link #deleteUser}.</p>
    *
    * @param o the Object with which to associate the user.
    * @param u the user to associate with the Object
    *
    * @return the user that was previously associated with the Object or
    * <code>null</code> if none.
    *
    * @exception UnknownUserException if the user to log in is not known at this
    * UserManager. A user is known at a UserManager, if the User object is registered,
    * i.e. no <code>equals()</code> method of any kind is called.
    *
    * @see User#loggedOn
    *
    * @override Never
    */
    public synchronized User logOn(Object o, User u) {
        User usrTemp = getUser(u.getName());
        if ((usrTemp == null) || (usrTemp != u)) {
            throw new UnknownUserException(u.getName());
        }
        User usrReturn = logOff(o);
        if (u != null) {
            m_mpCurrentUsers.put(o, u);
            u.loggedOn(o);
        }
        return usrReturn;
    }

    /**
    * Disassociate the current user from an Object.
    *
    * @param o the Object from which to disassociate a user.
    *
    * @return the user just logged off or <code>null</code> if none.
    *
    * @see User#loggedOff
    *
    * @override Never
    */
    public synchronized User logOff(Object o) {
        User u = (User) m_mpCurrentUsers.remove(o);
        if (u != null) {
            u.loggedOff(o);
        }
        return u;
    }

    /**
    * Retrieve the user currently associated with some Object.
    *
    * @param o the Object with which the searched user must be associated.
    *
    * @return the user associated with the given Object or <code>null</code> if none.
    *
    * @override Never
    */
    public synchronized User getCurrentUser(Object o) {
        return (User) m_mpCurrentUsers.get(o);
    }

    /**
    * The global UserManager.
    */
    private static UserManager s_umGlobal = new UserManager();

    /**
    * Get the global UserManager.
    *
    * <p>The global UserManager can be used as a centralized instance to manage all the
    * users in an application.</p>
    *
    * @return the global UserManager.
    */
    public static synchronized UserManager getGlobalUM() {
        return s_umGlobal;
    }

    /**
    * Set a new UserManager to be the global UserManager from now on.
    *
    * @param umNew the new global UserManager.
    *
    * @return the previous UserManager.
    */
    public static synchronized UserManager setGlobalUM(UserManager umNew) {
        UserManager umReturn = s_umGlobal;
        s_umGlobal = umNew;
        return s_umGlobal;
    }
}
