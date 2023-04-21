package simon.core.entity;

/**
 * User: ramil
 * Date: 15.02.2009
 * Time: 15:24:58
 */
public class UsersContacts extends IdentifiedEntity {

    private User user;

    private ContactsType contactsType;

    private String value;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public ContactsType getContactsType() {
        return contactsType;
    }

    public void setContactsType(ContactsType contactsType) {
        this.contactsType = contactsType;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
