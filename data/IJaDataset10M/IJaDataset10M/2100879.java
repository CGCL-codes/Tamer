package lichen.hibernate;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

/**
 * 针对hibernate的session管理类.
 * 构造hibernate的{@link Configuration}.
 * @author Jun Tsai
 * @version $Revision: 42 $
 * @since 0.0.3
 * 
 */
public interface HibernateSessionFactory {

    /** Returns the SessionFactory from which Hibernate sessions are created. */
    SessionFactory getSessionFactory();
}
