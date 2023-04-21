package org.authorsite.mailarchive.services.storage;

import java.util.*;
import org.authorsite.mailarchive.model.*;

/**
 * Stub implementation of EmailAddressDAO for use in testing.
 * 
 * 
 * @author jejking
 * @version $Revision: 1.2 $
 */
public class MemoryEmailAddressDAO implements EmailAddressDAO {

    private HashMap addresses;

    public MemoryEmailAddressDAO() {
        addresses = new HashMap();
    }

    /**
     * @see org.authorsite.mailarchive.services.storage.EmailAddressDAO#saveEmailAddress(org.authorsite.mailarchive.model.EmailAddress)
     */
    public void saveEmailAddress(EmailAddress addressToSave) throws ArchiveStorageException {
        addresses.put(addressToSave.getAddress(), addressToSave);
    }

    /**
     * @see org.authorsite.mailarchive.services.storage.EmailAddressDAO#saveEmailAddresses(java.util.List)
     */
    public void saveEmailAddresses(List addressesToSave) throws ArchiveStorageException {
        Iterator it = addressesToSave.iterator();
        while (it.hasNext()) {
            saveEmailAddress((EmailAddress) it.next());
        }
    }

    /**
     * @see org.authorsite.mailarchive.services.storage.EmailAddressDAO#deleteEmailAddress(org.authorsite.mailarchive.model.EmailAddress)
     */
    public void deleteEmailAddress(EmailAddress addressToDelete) throws ArchiveStorageException {
        addresses.remove(addressToDelete);
    }

    /**
     * @see org.authorsite.mailarchive.services.storage.EmailAddressDAO#deleteEmailAddresses(java.util.List)
     */
    public void deleteEmailAddresses(List addressesToDelete) throws ArchiveStorageException {
        Iterator it = addressesToDelete.iterator();
        while (it.hasNext()) {
            deleteEmailAddress((EmailAddress) it.next());
        }
    }

    /**
     * @see org.authorsite.mailarchive.services.storage.EmailAddressDAO#getEmailAddress(java.lang.String)
     */
    public EmailAddress getEmailAddress(String addressString) throws ArchiveStorageException {
        Object address = addresses.get(addressString);
        if (address == null) {
            return null;
        } else {
            return (EmailAddress) address;
        }
    }

    /**
     * @see org.authorsite.mailarchive.services.storage.EmailAddressDAO#getEmailAddressesLike(java.lang.String)
     */
    public List getEmailAddressesLike(String addressPattern) throws ArchiveStorageException {
        throw new UnsupportedOperationException();
    }

    /**
     * @see org.authorsite.mailarchive.services.storage.EmailAddressDAO#getEmailAddressesByPerson(org.authorsite.mailarchive.model.Person)
     */
    public List getEmailAddressesByPerson(Person person) throws ArchiveStorageException {
        throw new UnsupportedOperationException();
    }

    /**
     * @see org.authorsite.mailarchive.services.storage.EmailAddressDAO#getEmailAddressesByPersonalName(java.lang.String)
     */
    public List getEmailAddressesByPersonalName(String personalName) throws ArchiveStorageException {
        throw new UnsupportedOperationException();
    }

    /**
     * @see org.authorsite.mailarchive.services.storage.EmailAddressDAO#getEmailAddressesByPersonalNameLike(java.lang.String)
     */
    public List getEmailAddressesByPersonalNameLike(String personalNamePattern) throws ArchiveStorageException {
        throw new UnsupportedOperationException();
    }
}
