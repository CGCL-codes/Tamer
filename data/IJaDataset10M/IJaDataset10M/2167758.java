package net.paoding.rose.web.impl.context;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import org.apache.commons.lang.ArrayUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.core.io.Resource;
import org.springframework.web.context.support.XmlWebApplicationContext;

/**
 * 
 * @author 王志亮 [qieqie.wang@gmail.com]
 * 
 */
public class ResourceXmlWebApplicationContext extends XmlWebApplicationContext {

    private List<Resource> contextResources = Collections.emptyList();

    private String[] messageBaseNames = new String[0];

    public ResourceXmlWebApplicationContext() {
    }

    public void setContextResources(List<Resource> contextResources) {
        this.contextResources = contextResources;
    }

    public void setMessageBaseNames(String[] messageBaseNames) {
        if (messageBaseNames == null) {
            messageBaseNames = new String[0];
        }
        this.messageBaseNames = messageBaseNames;
    }

    @Override
    public String toString() {
        return getDisplayName();
    }

    @Override
    protected void loadBeanDefinitions(XmlBeanDefinitionReader reader) throws BeansException, IOException {
        super.loadBeanDefinitions(reader);
        for (Resource resource : contextResources) {
            reader.loadBeanDefinitions(resource);
        }
    }

    @Override
    protected void prepareBeanFactory(ConfigurableListableBeanFactory beanFactory) {
        registerMessageSource();
        super.prepareBeanFactory(beanFactory);
    }

    private void registerMessageSource() {
        if (!ArrayUtils.contains(this.getBeanDefinitionNames(), MESSAGE_SOURCE_BEAN_NAME)) {
            BeanDefinitionRegistry registry = (BeanDefinitionRegistry) getBeanFactory();
            GenericBeanDefinition messageSource = new GenericBeanDefinition();
            messageSource.setBeanClass(ReloadableResourceBundleMessageSource.class);
            MutablePropertyValues propertyValues = new MutablePropertyValues();
            propertyValues.addPropertyValue("useCodeAsDefaultMessage", true);
            propertyValues.addPropertyValue("defaultEncoding", "UTF-8");
            propertyValues.addPropertyValue("cacheSeconds", 60);
            propertyValues.addPropertyValue("basenames", messageBaseNames);
            messageSource.setPropertyValues(propertyValues);
            registry.registerBeanDefinition(MESSAGE_SOURCE_BEAN_NAME, messageSource);
        }
    }
}
