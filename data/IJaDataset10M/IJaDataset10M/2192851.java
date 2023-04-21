package com.peusoft.ptcollect.server.service.persistance.dao;

import com.peusoft.ptcollect.core.persistance.domain.ContactPerson;
import org.springframework.stereotype.Repository;

/**
 * DAO for User.
 * 
 * @author zhenja
 *
 */
@Repository("contactPersonDao")
public class ContactPersonDaoImpl extends AbstractDaoImpl<Long, ContactPerson> {

    /**
     *@see AbstractDAO#AbstractDAO()
     */
    public ContactPersonDaoImpl() {
        super(ContactPerson.class);
    }
}
