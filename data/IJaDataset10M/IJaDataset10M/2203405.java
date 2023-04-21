package com.antilia.hibernate.dao.impl;

import java.io.Serializable;
import org.hibernate.SessionFactory;
import org.springframework.orm.hibernate3.HibernateTemplate;
import com.antilia.common.dao.IDaoLocator;
import com.antilia.common.dao.IQuerableDao;
import com.antilia.common.dao.IQuerableUpdatableDao;

/**
 * 
 * @author Ernesto Reinaldo Barreiro (reirn70@gmail.com)
 */
public class SpringDaoLocator implements IDaoLocator {

    private static SpringDaoLocator instance;

    private HibernateTemplate template;

    private SessionFactory sessionFactory;

    /**
	 * 
	 */
    protected SpringDaoLocator() {
    }

    public <T extends Serializable> IQuerableDao<T> locateQuerableDao(Class<? extends T> beanClass, String extraId) {
        SpringHibernateQuerableDao<T> dao = new SpringHibernateQuerableDao<T>();
        configureDao(dao);
        return dao;
    }

    public <T extends Serializable> IQuerableUpdatableDao<T> locateQuerableUpdatableDao(Class<? extends T> beanClass, String extraId) {
        SpringHibernateQuerableUpdatableDao<T> dao = new SpringHibernateQuerableUpdatableDao<T>();
        configureDao(dao);
        return dao;
    }

    protected <T extends Serializable> void configureDao(SpringHibernateQuerableDao<T> dao) {
        if (dao.getHibernateTemplate() != null) {
            return;
        }
        if (getTemplate() != null) dao.setHibernateTemplate(getTemplate()); else if (getSessionFactory() != null) dao.setSessionFactory(getSessionFactory());
    }

    /**
	 * @return the instance
	 */
    public static SpringDaoLocator getInstance() {
        if (instance == null) {
            instance = new SpringDaoLocator();
        }
        return instance;
    }

    /**
	 * @return the template
	 */
    public HibernateTemplate getTemplate() {
        return template;
    }

    /**
	 * @param template the template to set
	 */
    public void setTemplate(HibernateTemplate template) {
        this.template = template;
    }

    /**
	 * @return the sessionFactory
	 */
    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    /**
	 * @param sessionFactory the sessionFactory to set
	 */
    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }
}
