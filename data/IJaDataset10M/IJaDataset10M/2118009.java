package org.omnisys.devices;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.zkoss.zkplus.hibernate.*;

/**
 *  @netbeans.hibernate.facade beanClass=org.omnisys.devices.Protocol
 */
public class ProtocolFacade {

    public void saveProtocol(Protocol protocol) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction tx = session.beginTransaction();
        session.saveOrUpdate(protocol);
        tx.commit();
    }

    public java.util.List getAllProtocols() {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        org.hibernate.Query query = session.createQuery(" select protocol " + " from  " + " Protocol as protocol ");
        return query.list();
    }

    public org.omnisys.devices.Protocol getProtocolByName(java.lang.String protocolName) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction tx = session.beginTransaction();
        org.hibernate.Query query = session.createQuery(" select protocol " + " from  " + " Protocol as protocol " + "  where  " + " protocol.name = ? ");
        query.setParameter(0, protocolName);
        return (org.omnisys.devices.Protocol) query.uniqueResult();
    }
}
