package com.carlos.projects.billing.dao.hibernate;

import com.carlos.projects.billing.dao.ComponentDAO;
import com.carlos.projects.billing.domain.Component;
import org.hibernate.SessionFactory;

/**
 * Hibernate implementation of {@link com.carlos.projects.billing.dao.ComponentDAO}
 *
 * @author Carlos Fernandez
 * @date 15-Nov-2009
 */
public class ComponentHibernateDAO extends HibernateDAO<Component, String> implements ComponentDAO {

    public ComponentHibernateDAO() {
    }

    public ComponentHibernateDAO(SessionFactory hibernateSessionFactory) {
        super(hibernateSessionFactory);
    }
}
