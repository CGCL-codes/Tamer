package demo.ria.logic.memmory;

import java.util.Map;
import demo.ria.logic.ContactsManager;
import demo.ria.model.Contact;
import demo.ria.model.Contacts;

public class MemmoryContactsManager extends ContactsManager {

    private static Contacts contacts = new Contacts();

    static {
        contacts.addContact(new Contact("boguslav.romanovic", "Boguslav", "Romanovic", "1970-02-16", "M", "+44 0 7770 101999", "rom@example.com", "Platform protagonist"));
        contacts.addContact(new Contact("kadezda.krasna", "Nadezda", "Krasna", "1986-04-16", "F", "+44 0 7770 101998", "krasna@example.com", "Secretary"));
    }

    public MemmoryContactsManager() {
    }

    @Override
    public Contacts selectContacts() {
        return MemmoryContactsManager.contacts;
    }

    @Override
    public Contact selectContact(String id) throws Exception {
        return MemmoryContactsManager.contacts.getContacts().get(id);
    }

    @Override
    public Contacts findContact(String pattern) throws Exception {
        Map<String, Contact> contacts = MemmoryContactsManager.contacts.getContacts();
        Contacts foundContacts = new Contacts();
        for (String key : contacts.keySet()) {
            if (key.matches(pattern)) {
                foundContacts.addContact(contacts.get(key));
            }
        }
        return foundContacts;
    }

    @Override
    public void insertContact(Contact contact) throws Exception {
        MemmoryContactsManager.contacts.addContact(contact);
    }

    @Override
    public void updateContact(Contact contact) throws Exception {
        Map<String, Contact> contacts = MemmoryContactsManager.contacts.getContacts();
        Contact e = contacts.get(contact.getId());
        if (e == null) {
            throw new Exception("no such contact");
        }
        contacts.put(contact.getId(), contact);
    }

    @Override
    public void deleteContact(String id) throws Exception {
        Map<String, Contact> contacts = MemmoryContactsManager.contacts.getContacts();
        Contact e = contacts.get(id);
        if (e == null) {
            throw new Exception("no such contact");
        } else {
            contacts.remove(id);
        }
    }

    public static void main(String[] args) {
        MemmoryContactsManager contactManager = new MemmoryContactsManager();
        Contacts contacts = contactManager.selectContacts();
        System.out.println(contacts);
    }
}
