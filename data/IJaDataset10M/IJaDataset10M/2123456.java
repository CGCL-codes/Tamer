package net.sf.brightside.chocolatefever.core.spring;

import org.springframework.context.ApplicationContext;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.orm.hibernate3.HibernateOperations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.AfterMethod;
import org.hibernate.SessionFactory;
import net.sf.brightside.chocolatefever.core.spring.ApplicationContextProviderSingleton;
import net.sf.brightside.chocolatefever.core.hibernate.PersistenceManager;

public class AbstractSpringHibernateTemplateTest {

    protected HibernateOperations hibernateTemplate;

    protected PersistenceManager persistenceManager;

    protected SessionFactory sessionFactory;

    private ApplicationContext factory = new ApplicationContextProviderSingleton().getContext();

    public ApplicationContext getFactory() {
        return factory;
    }

    public PersistenceManager getManager() {
        return this.persistenceManager;
    }

    @BeforeMethod
    public void setUp() throws Exception {
        persistenceManager = (PersistenceManager) getFactory().getBean(PersistenceManager.class.getName());
        sessionFactory = (SessionFactory) getFactory().getBean("sessionFactory");
        hibernateTemplate = new HibernateTemplate(sessionFactory);
        persistenceManager.setHibernateTemplate(hibernateTemplate);
    }

    @AfterMethod
    public void flush() throws Exception {
        hibernateTemplate.flush();
    }
}
