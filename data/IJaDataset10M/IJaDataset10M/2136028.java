package users.events;

/**
  * An abstract adapter class for receiving user data events. The methods in this class are
  * empty. This class exists as convenience for creating listener objects.
  *
  * <p>Extend this class to create a UserDataEvent listener and override the methods for the
  * events of interest. (If you implement the UserDataListener interface, you have to define
  * all of the methods in it. This abstract class defines empty method bodies for them all, so you
  * can concentrate on defining methods only for events you care about.)</p>
  *
  * <p>Create a listener object using the extended class and then register it with a
  * {@link users.UserManager} using the UserManager's {@link users.UserManager#addUserDataListener} method.
  * When a user is added or removed, the relevant method in the listener object is invoked, and the
  * {@link UserDataEvent} is passed to it.</p>
  *
  * @see UserDataEvent
  * @see UserDataListener
  * @see users.UserManager
  *
  * @author Steffen Zschaler
  * @version 2.0 06/05/1999
  * @since v2.0
  */
public abstract class UserDataAdapter implements UserDataListener {

    /**
    * Called whenever a user was added to the source. The new user will be contained
    * in the event object.
    *
    * @param e the event object describing the event.
    *
    * @override Sometimes
    */
    public void userAdded(UserDataEvent e) {
    }

    /**
    * Called whenever a user was deleted from the source. The deleted user will be
    * contained in the event object.
    *
    * @param e the event object describing the event.
    *
    * @override Sometimes
    */
    public void userDeleted(UserDataEvent e) {
    }
}
