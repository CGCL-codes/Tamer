package com.generalynx.ecos.beans.orm;

import com.alesj.newsfeed.data.Contact;
import com.generalynx.ecos.data.types.Status;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.event.DeleteEvent;
import org.hibernate.event.def.DefaultDeleteEventListener;

public class BasicDeleteEventListener extends DefaultDeleteEventListener {

    protected final Log logger = LogFactory.getLog(getClass());

    public void onDelete(DeleteEvent event) throws HibernateException {
        if (Contact.class.isAssignableFrom(Hibernate.getClass(event.getObject()))) {
            Contact contact = (Contact) event.getObject();
            contact.setStatus(Status.DELETED);
            event.getSession().update(contact);
        } else {
            super.onDelete(event);
        }
    }
}
