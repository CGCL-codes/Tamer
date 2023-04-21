package net.sf.jpasecurity.spring.contacts;

import java.util.List;
import net.sf.jpasecurity.contacts.model.Contact;
import net.sf.jpasecurity.contacts.model.User;

/**
 * @author Arne Limburg
 */
public interface ContactsDao {

    List<User> getAllUsers();

    User getUser(String name);

    List<Contact> getAllContacts();
}
