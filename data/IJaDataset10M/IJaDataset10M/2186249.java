package org.openuss.system;

import org.apache.log4j.Logger;
import org.hibernate.SessionFactory;
import org.springframework.test.AbstractTransactionalDataSourceSpringContextTests;
import org.openuss.TestUtility;

/**
 * JUnit Test for Spring Hibernate SystemService class.
 * @see org.openuss.system.SystemService
 */
public abstract class SystemServiceIntegrationTestBase extends AbstractTransactionalDataSourceSpringContextTests {

    protected static final Logger logger = Logger.getLogger(SystemServiceIntegrationTestBase.class);

    protected SystemService systemService;

    public SystemService getSystemService() {
        return systemService;
    }

    public void setSystemService(SystemService systemService) {
        this.systemService = systemService;
    }

    public void testSystemServiceInjection() {
        assertNotNull(systemService);
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

    protected TestUtility testUtility;

    public TestUtility getTestUtility() {
        return testUtility;
    }

    public void setTestUtility(TestUtility testUtility) {
        this.testUtility = testUtility;
    }

    protected String[] getConfigLocations() {
        return new String[] { "classpath*:applicationContext.xml", "classpath*:applicationContext-beans.xml", "classpath*:applicationContext-lucene.xml", "classpath*:applicationContext-cache.xml", "classpath*:applicationContext-messaging.xml", "classpath*:applicationContext-resources.xml", "classpath*:applicationContext-aop.xml", "classpath*:applicationContext-events.xml", "classpath*:testContext.xml", "classpath*:testDisableSecurity.xml", "classpath*:testDataSource.xml" };
    }
}
