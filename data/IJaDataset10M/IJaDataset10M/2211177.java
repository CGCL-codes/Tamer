package org.turnlink.sclm.dao;

import java.util.List;
import org.hibernate.criterion.DetachedCriteria;
import org.turnlink.sclm.model.Contact;

public interface ContactDao {

    public Contact findById(Integer contactId);

    public List<Contact> findByDetatchedCriteria(DetachedCriteria criteria);

    public void saveOrUpdate(Contact contact);

    public void remove(Contact contact);
}
