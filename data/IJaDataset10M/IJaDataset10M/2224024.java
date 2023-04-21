package org.codehaus.groovy.grails.orm.hibernate;

import org.codehaus.groovy.grails.commons.GrailsApplication;
import org.codehaus.groovy.grails.orm.hibernate.cfg.DefaultGrailsDomainConfiguration;
import org.codehaus.groovy.grails.orm.hibernate.cfg.GrailsDomainConfiguration;
import org.codehaus.groovy.grails.orm.hibernate.cfg.GrailsHibernateUtil;
import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.orm.hibernate3.LocalSessionFactoryBean;

/**
 * A SessionFactory bean that allows the configuration class to be changed and customise for usage within Grails
 *
 * @author Graeme Rocher
 * @since 07-Jul-2005
 */
public class ConfigurableLocalSessionFactoryBean extends LocalSessionFactoryBean implements ApplicationContextAware {

    private ClassLoader classLoader = null;

    private GrailsApplication grailsApplication;

    private Class configClass = DefaultGrailsDomainConfiguration.class;

    private ApplicationContext applicationContext;

    private Class currentSessionContextClass;

    /**
     * Sets class to be used for the Hibernate CurrentSessionContext
     *
     * @param currentSessionContextClass An implementation of the CurrentSessionContext interface
     */
    public void setCurrentSessionContextClass(Class currentSessionContextClass) {
        this.currentSessionContextClass = currentSessionContextClass;
    }

    /**
     * Sets the class to be used for Hibernate Configuration
     * @param configClass A subclass of the Hibernate Configuration class
     */
    public void setConfigClass(Class configClass) {
        this.configClass = configClass;
    }

    /**
	 * 
	 */
    public ConfigurableLocalSessionFactoryBean() {
        super();
    }

    /**
	 * @return Returns the grailsApplication.
	 */
    public GrailsApplication getGrailsApplication() {
        return grailsApplication;
    }

    /**
	 * @param grailsApplication The grailsApplication to set.
	 */
    public void setGrailsApplication(GrailsApplication grailsApplication) {
        this.grailsApplication = grailsApplication;
    }

    /**
	 * Overrides default behaviour to allow for a configurable configuration class 
	 */
    protected Configuration newConfiguration() {
        Object config = BeanUtils.instantiateClass(configClass);
        if (config instanceof GrailsDomainConfiguration) {
            GrailsDomainConfiguration grailsConfig = (GrailsDomainConfiguration) config;
            grailsConfig.setGrailsApplication(grailsApplication);
        }
        if (currentSessionContextClass != null) {
            ((Configuration) config).setProperty(Environment.CURRENT_SESSION_CONTEXT_CLASS, currentSessionContextClass.getName());
            setExposeTransactionAwareSessionFactory(false);
        }
        return (Configuration) config;
    }

    public void setClassLoader(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    protected SessionFactory newSessionFactory(Configuration config) throws HibernateException {
        SessionFactory sf = super.newSessionFactory(config);
        GrailsApplication application = getGrailsApplication();
        if (application != null) GrailsHibernateUtil.configureHibernateDomainClasses(sf, application);
        return sf;
    }

    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
