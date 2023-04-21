package org.juicyapps.app.addressbook;

import java.util.List;
import java.util.Set;
import org.hibernate.criterion.Criterion;
import org.juicyapps.persistence.pojo.JuicyAccount;
import org.juicyapps.persistence.pojo.JuicyContact;
import org.juicyapps.persistence.pojo.JuicyFolder;

/**
 * This connector can be used to connect to the addressbook functionalities of
 * JuicyApps. You can get all contact folders and contacts. You can search for
 * contacts by a set of criterions.
 * 
 * @author hendrik@hhllcks.de
 */
public class AddressbookConnector {

    private Addressbook addressbook;

    public AddressbookConnector(Addressbook addressbook) {
        this.addressbook = addressbook;
    }

    /**
	 * Saves a given contact to the database.
	 * 
	 * @param juicyContact
	 *            This contact will be saved.
	 */
    public void saveContact(JuicyContact juicyContact) {
        addressbook.saveContact(juicyContact);
    }

    /**
	 * Removes a contact from the database.
	 * 
	 * @param juicyContact
	 *            This contact will be removed.
	 */
    public void deleteContact(JuicyContact juicyContact) {
        addressbook.deleteContact(juicyContact);
    }

    /**
	 * Adds a new folder to the database.
	 * 
	 * @param name
	 *            The name of the new folder.
	 */
    public void addFolder(String name) {
        addressbook.addFolder(name);
    }

    /**
	 * Saves a folder. This happens most likely, if the name of the folder
	 * should be changed.
	 * 
	 * Do not use this method, to create a new folder, use addFolder(String
	 * name) instead!
	 * 
	 * @param folder
	 *            The folder, that should be saved.
	 */
    public void saveFolder(JuicyFolder folder) {
        addressbook.saveFolder(folder);
    }

    /**
	 * Deletes a folder from the database.
	 * 
	 * @param juicyFolder
	 *            This folder will be deleted.
	 */
    public void deleteFolder(JuicyFolder juicyFolder) {
        addressbook.deleteFolder(juicyFolder);
    }

    /**
	 * Finds contacts in the database. You have to provide a set of criterions.
	 * You can create the following criterions:
	 * 
	 * 
	 * @param criteria
	 *            The set of criterions
	 * @return A list with the found contacts or null, if nothing was found
	 */
    public List<JuicyContact> findContacts(Set<Criterion> criteria) {
        return addressbook.getContact(criteria);
    }

    /**
	 * Returns all contact folders of the active account.
	 * 
	 * @return A set of contact folders
	 */
    public Set<JuicyFolder> getFolders() {
        return addressbook.getFolders();
    }

    /**
	 * Causes the Addressbook the refresh the active JuicyAccount.
	 */
    public void refreshJuicyAccount() {
        addressbook.refreshJuicyAccount();
    }

    /**
	 * Returns all contacts of the active account.
	 * 
	 * @return A set of contacts
	 */
    public Set<JuicyContact> getContacts() {
        return addressbook.getContacts();
    }

    /**
	 * Returns the active JuicyAccount
	 * 
	 * @return the active JuicyAccount
	 */
    public JuicyAccount getActiveJuicyAccount() {
        return addressbook.getActiveJuicyAccount();
    }
}
