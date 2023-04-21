package org.jw.web.rdc.integration;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

/**
 * @author Edwin
 * 
 */
public abstract class TransactionBase implements InitializingBean {

    private SessionFactory sessionFactory;

    /**
	 * @return the sessionFactory
	 */
    protected SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    /**
	 * @param sessionFactory the sessionFactory to set
	 */
    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    protected abstract void init();

    /**
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(sessionFactory, "'sessionFactory' can not be null.");
    }
}
