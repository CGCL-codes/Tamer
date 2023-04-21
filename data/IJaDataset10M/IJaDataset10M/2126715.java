package org.greenscape.opencontact.api;

import java.util.List;
import java.util.Map;
import org.greenscape.openaccount.Account;
import org.greenscape.openaccount.Service;

/**
 * Service for handling user groups and contacts
 * @author smsajid
 */
public interface ContactService extends Service {

    /**
     * Get the map of contact groups for the given account.
     * The key of the map is the group name.
     * @param account the account used to search contact groups
     * @return the list of contact groups
     */
    public Map<String, ContactGroup> getGroups(Account account);

    /**
     * Get the list of contacts for the given account
     * @param account the account used to search contact groups
     * @return the list of contacts
     */
    public List<Contact> getContacts(Account account);

    /**
     * Get the list of contacts for the given account and contact group
     * @param account the account used to search contact groups
     * @param group the group to search for contacts
     * @return the list of contacts
     */
    public List<Contact> getContacts(Account account, ContactGroup group);

    /**
     * Loads the photo of the contact as <code>Blob</code> object and sets it
     * in the photo property
     * @param contact the contact whose photo is to be retrieved
     */
    public void loadContactPhoto(Contact contact);

    /**
     * Saves the group info
     * @param group the group to be saved
     * @param groupAccount the associated account
     */
    public void save(ContactGroup group, GroupAccount groupAccount);

    /**
     * Saves the contact info
     * @param contact the contact to be saved
     */
    public void save(Contact contact);
}
