package net.sf.autodao.impl;

import net.sf.autodao.impl.hibernate.HibernateDaoFactoryBean;
import net.sf.autodao.impl.jpa.JpaDaoFactoryBean;
import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

/**
 * Spring namespace handler.
 * <p/>
 * NOT FOR PUBLIC USE.
 */
final class AutoDAONamespaceHandler extends NamespaceHandlerSupport {

    /** {@inheritDoc} */
    @Override
    public void init() {
        registerBeanDefinitionParser("hibernate", new AutoDAOBeanDefinitionParser(HibernateDaoFactoryBean.class, "session-factory", "sessionFactory"));
        registerBeanDefinitionParser("hibernateScan", new AutoDAOScanBeanDefinitionParser(HibernateDaoFactoryBean.class, "session-factory", "sessionFactory"));
        registerBeanDefinitionParser("jpa", new AutoDAOBeanDefinitionParser(JpaDaoFactoryBean.class, "entity-manager-factory", "entityManagerFactory"));
        registerBeanDefinitionParser("jpaScan", new AutoDAOScanBeanDefinitionParser(JpaDaoFactoryBean.class, "entity-manager-factory", "entityManagerFactory"));
    }
}
