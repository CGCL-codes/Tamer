package org.rbx.sims.contact.dao;

import com.rbx.dao.support.CrudDaoSupport;
import org.rbx.sims.model.Contact;
import java.util.List;

/**
 * @author Edward
 */
public interface ContactDao extends CrudDaoSupport<Contact> {

    /**
    * Retrieves All Contacts
    * @return A list of contact Objects
    */
    List<Contact> getContacts();

    /**
    * Retrieves a contact
    * @param contactId contact id
    * @return A Contact Object
    */
    Contact getContactById(Long contactId);
}
