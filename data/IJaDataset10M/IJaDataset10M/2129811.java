package org.opennms.core.soa.support;

import org.opennms.core.soa.Registration;
import org.opennms.core.soa.ServiceRegistry;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

/**
 * ServiceRegistrationBean
 *
 * @author brozow
 */
public class ServiceFactoryBean implements FactoryBean, BeanFactoryAware, InitializingBean, DisposableBean {

    private BeanFactory m_beanFactory;

    private ServiceRegistry m_serviceRegistry;

    private String m_targetBeanName;

    private Object m_target;

    private Class<?>[] m_serviceInterfaces;

    private Registration m_registration;

    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        m_beanFactory = beanFactory;
    }

    public void setTargetBeanName(String targetBeanName) {
        m_targetBeanName = targetBeanName;
    }

    public void setTarget(Object target) {
        m_target = target;
    }

    public void setInterfaces(Class<?>[] serviceInterfaces) {
        m_serviceInterfaces = serviceInterfaces;
    }

    public void setServiceRegistry(ServiceRegistry serviceRegistry) {
        m_serviceRegistry = serviceRegistry;
    }

    public void afterPropertiesSet() throws Exception {
        boolean hasText = StringUtils.hasText(m_targetBeanName);
        Assert.isTrue(hasText || m_target != null, "targetBeanName or target must be set");
        Assert.notEmpty(m_serviceInterfaces, "interfaces must be set");
        if (m_target == null) {
            Assert.notNull(m_beanFactory, "beanFactory must not be null");
        }
        Object provider = m_target != null ? m_target : m_beanFactory.getBean(m_targetBeanName);
        m_registration = m_serviceRegistry.register(provider, m_serviceInterfaces);
    }

    public void destroy() throws Exception {
        if (m_registration != null) {
            m_registration.unregister();
        }
    }

    public Object getObject() throws Exception {
        return m_registration;
    }

    public Class<?> getObjectType() {
        return (m_registration == null ? Registration.class : m_registration.getClass());
    }

    public boolean isSingleton() {
        return true;
    }
}
