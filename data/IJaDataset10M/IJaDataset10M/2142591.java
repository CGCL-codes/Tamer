package org.starobjects.sprung;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import org.springframework.beans.factory.BeanDefinitionStoreException;
import org.springframework.beans.factory.UnsatisfiedDependencyException;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.util.ClassUtils;

public abstract class SpringContextLoaderAbstract {

    private ClassPathXmlApplicationContext applicationContext;

    private GenericApplicationContext parentContext;

    private final Map<String, String> propertiesMap = new HashMap<String, String>();

    private final Map<String, Object> beanMap = new HashMap<String, Object>();

    protected void bindProperty(String key, String value) {
        propertiesMap.put(key, value);
    }

    protected Map<String, String> getBoundProperties() {
        return Collections.unmodifiableMap(propertiesMap);
    }

    protected void bindBean(String beanId, Object bean) {
        beanMap.put(beanId, bean);
    }

    protected Map<String, Object> getBoundBeans() {
        return Collections.unmodifiableMap(beanMap);
    }

    protected void load(String applicationContextFileName) {
        try {
            parentContext = new GenericApplicationContext();
            bindBeansInto(parentContext);
            applicationContext = new ClassPathXmlApplicationContext(parentContext);
            bindPropertiesInto(applicationContext);
            applicationContext.setConfigLocation(applicationContextFileName);
            applicationContext.refresh();
        } catch (BeanDefinitionStoreException ex) {
            throw new IllegalArgumentException(ex);
        } catch (UnsatisfiedDependencyException ex) {
            throw new IllegalArgumentException(ex);
        }
    }

    private void bindBeansInto(GenericApplicationContext context) {
        for (String beanId : beanMap.keySet()) {
            context.getBeanFactory().registerSingleton(beanId, beanMap.get(beanId));
        }
        context.refresh();
    }

    private void bindPropertiesInto(ConfigurableApplicationContext context) {
        PropertyPlaceholderConfigurer configurer = new PropertyPlaceholderConfigurer();
        configurer.setProperties(asProperties(propertiesMap));
        context.addBeanFactoryPostProcessor(configurer);
    }

    /**
     * Infers name of application context from provided class.
     * 
     * <p>
     * For example, <tt>com.foo.Bar.class</tt> maps to <tt>com/foo/Bar.xml</tt>.
     */
    protected void load(Class<?> componentClass) {
        StringBuilder buf = new StringBuilder();
        buf.append(ClassUtils.classPackageAsResourcePath(componentClass));
        buf.append("/");
        buf.append(componentClass.getSimpleName());
        buf.append(".xml");
        String applicationContextFileName = buf.toString();
        load(applicationContextFileName);
    }

    private Properties asProperties(Map<String, String> map) {
        Properties properties = new Properties();
        properties.putAll(map);
        return properties;
    }

    protected ApplicationContext getContext() {
        return applicationContext;
    }

    protected Object getBean(String beanId) {
        return applicationContext.getBean(beanId);
    }
}
