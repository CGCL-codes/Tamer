package org.openuss.registration;

import org.apache.log4j.Logger;
import org.hibernate.SessionFactory;
import org.springframework.test.AbstractTransactionalDataSourceSpringContextTests;

/**
 * JUnit Test for Spring Hibernate InstituteActivationCodeDao class.
 * @see org.openuss.registration.InstituteActivationCodeDao
 */
public abstract class InstituteActivationCodeDaoTestBase extends AbstractTransactionalDataSourceSpringContextTests {

    protected static final Logger logger = Logger.getLogger(InstituteActivationCodeDaoTestBase.class);

    protected InstituteActivationCodeDao instituteActivationCodeDao;

    public InstituteActivationCodeDao getInstituteActivationCodeDao() {
        return instituteActivationCodeDao;
    }

    public void setInstituteActivationCodeDao(InstituteActivationCodeDao instituteActivationCodeDao) {
        this.instituteActivationCodeDao = instituteActivationCodeDao;
    }

    public void testInstituteActivationCodeDaoInjection() {
        assertNotNull(instituteActivationCodeDao);
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
