package org.openuss.security.acl;

import org.apache.log4j.Logger;
import org.hibernate.SessionFactory;
import org.springframework.test.AbstractTransactionalDataSourceSpringContextTests;

/**
 * JUnit Test for Spring Hibernate ObjectIdentityDao class.
 * @see org.openuss.security.acl.ObjectIdentityDao
 */
public abstract class ObjectIdentityDaoTestBase extends AbstractTransactionalDataSourceSpringContextTests {

    protected static final Logger logger = Logger.getLogger(ObjectIdentityDaoTestBase.class);

    protected ObjectIdentityDao objectIdentityDao;

    public ObjectIdentityDao getObjectIdentityDao() {
        return objectIdentityDao;
    }

    public void setObjectIdentityDao(ObjectIdentityDao objectIdentityDao) {
        this.objectIdentityDao = objectIdentityDao;
    }

    public void testObjectIdentityDaoInjection() {
        assertNotNull(objectIdentityDao);
    }

    protected void commit() {
        setComplete();
        endTransaction();
        startNewTransaction();
    }

    protected void flush() {
        sessionFactory.getCurrentSession().flush();
    }

    protected SessionFactory sessionFactory;

    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    protected String[] getConfigLocations() {
        return new String[] { "classpath*:applicationContext.xml", "classpath*:applicationContext-beans.xml", "classpath*:applicationContext-lucene.xml", "classpath*:applicationContext-cache.xml", "classpath*:applicationContext-messaging.xml", "classpath*:applicationContext-resources.xml", "classpath*:applicationContext-aop.xml", "classpath*:testContext.xml", "classpath*:testDisableSecurity.xml", "classpath*:testDataSource.xml" };
    }
}
