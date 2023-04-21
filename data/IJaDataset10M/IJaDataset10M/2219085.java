package org.openuss.messaging;

import org.apache.log4j.Logger;
import org.hibernate.SessionFactory;
import org.springframework.test.AbstractTransactionalDataSourceSpringContextTests;

/**
 * JUnit Test for Spring Hibernate MessageDao class.
 * @see org.openuss.messaging.MessageDao
 */
public abstract class MessageDaoTestBase extends AbstractTransactionalDataSourceSpringContextTests {

    protected static final Logger logger = Logger.getLogger(MessageDaoTestBase.class);

    protected MessageDao messageDao;

    public MessageDao getMessageDao() {
        return messageDao;
    }

    public void setMessageDao(MessageDao messageDao) {
        this.messageDao = messageDao;
    }

    public void testMessageDaoInjection() {
        assertNotNull(messageDao);
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
