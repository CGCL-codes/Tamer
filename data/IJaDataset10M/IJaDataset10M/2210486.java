package org.radmail.dao.xml;

import org.hibernate.EntityMode;
import org.hibernate.Session;
import org.radmail.dao.RadmailDaoImpl;

/**
 *
 * @author pettyjohn
 */
public class RadmailDaoXmlImpl extends RadmailDaoImpl {

    @Override
    protected Session getSession() {
        return super.getSession().getSession(EntityMode.DOM4J);
    }
}
