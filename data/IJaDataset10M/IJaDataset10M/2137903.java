package netfone;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author bml
 */
public class Contact {

    private String phoneNumber;

    private String label;

    private List<User> users = new ArrayList<User>();

    public Contact(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String toString() {
        if (label == null || label.equals("")) {
            return phoneNumber;
        } else {
            return label + " ->  " + phoneNumber;
        }
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public boolean equals(Object o) {
        if (o instanceof Contact) {
            Contact contact = (Contact) o;
            return this.getPhoneNumber().equals(contact.getPhoneNumber());
        }
        return false;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    void addUser(User user) {
        getUsers().add(user);
    }

    void removeUser(User user) {
        getUsers().remove(user);
    }
}
