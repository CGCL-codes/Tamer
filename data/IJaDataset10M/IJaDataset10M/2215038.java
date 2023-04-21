package erki.talk.clients.erki.event;

import java.util.Collection;
import java.util.LinkedList;
import erki.talk.clients.erki.ui.User;

/**
 * This event is raised if the user list has been received from the server.
 * 
 * @author Edgar Kalkowski
 */
public class UserListReceivedEvent extends Event {

    private final Collection<User> userList;

    /**
     * Create a new {@code UserListReceivedEvent}.
     * 
     * @param userList
     *        The new user list. This list is (deep) copied to prevent
     *        sideeffects if someone alters the original one.
     */
    public UserListReceivedEvent(Collection<User> userList) {
        this.userList = new LinkedList<User>();
        for (User u : userList) {
            this.userList.add(new User(u.getNick(), u.isAuthed(), u.isEncrypted()));
        }
    }

    /**
     * @return A (deep) copy of the new user list to prevent sideeffects if
     *         someone alters the returned list.
     */
    public Collection<User> getUserList() {
        LinkedList<User> copy = new LinkedList<User>();
        for (User u : userList) {
            copy.add(new User(u.getNick(), u.isAuthed(), u.isEncrypted()));
        }
        return copy;
    }
}
