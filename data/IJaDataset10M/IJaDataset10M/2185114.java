package com.germinus.merlin.dao;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.SessionFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.springframework.orm.hibernate3.support.OpenSessionInViewUtil;

public abstract class TestCase extends junit.framework.TestCase {

    protected final Log logger = LogFactory.getLog(getClass());

    protected static final ApplicationContext appContext;

    protected static SessionFactory sessionFactory;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        sessionFactory = (SessionFactory) appContext.getBean("sessionFactory");
        OpenSessionInViewUtil.openSession(sessionFactory);
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        OpenSessionInViewUtil.closeSession(sessionFactory);
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    static {
        try {
            appContext = new FileSystemXmlApplicationContext(new String[] { "classpath*:/context/applicationContext-web.xml", "classpath*:/context/applicationContext-dao.xml", "classpath*:/context/applicationContext-resources.xml", "classpath*:/context/applicationContext-util.xml", "classpath*:/context/applicationContext-manager.xml", "classpath*:/context/applicationContext-pageTemplates.xml" });
        } catch (Throwable ex) {
            throw new ExceptionInInitializerError(ex);
        }
    }
}
