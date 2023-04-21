package org.dicom4j.lds.core.dao.hibernate;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Hibernate specialized session factory
 *
 * @since 0.0.1
 * @since 14 avr. 2009
 * @author <a href="mailto:straahd@users.sourceforge.net">Laurent Lecomte 
 *
 */
public class HibernateSessionFactory {

    private static Logger logger = LoggerFactory.getLogger(HibernateSessionFactory.class);

    private final SessionFactory sessionFactory;

    public HibernateSessionFactory() throws Exception {
        try {
            sessionFactory = new Configuration().configure().buildSessionFactory();
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new Exception(e);
        }
    }

    public Session getSession() {
        return sessionFactory.getCurrentSession();
    }
}
