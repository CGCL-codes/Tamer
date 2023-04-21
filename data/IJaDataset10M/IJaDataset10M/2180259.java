package org.jdiagnose.library.config;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import org.jdiagnose.config.ConfigChangeEvent;
import org.jdiagnose.config.ConfigChangeListener;
import org.jdiagnose.config.ConfigException;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * @author jmccrindle
 */
public class SetPropertyListener implements ConfigChangeListener, ApplicationContextAware {

    private String key;

    private String bean;

    private Method propertyWriteMethod;

    private String property;

    private ApplicationContext applicationContext;

    private Object object;

    public SetPropertyListener() {
    }

    public synchronized void onConfigChange(ConfigChangeEvent event) throws ConfigException {
        if (object == null) {
            try {
                init();
            } catch (IntrospectionException e) {
                throw new ConfigException(e);
            }
        }
        if (bean != null && propertyWriteMethod != null && key != null && key.equals(event.getKey())) {
            try {
                propertyWriteMethod.invoke(object, new Object[] { event.getNewValue() });
            } catch (IllegalArgumentException e) {
                throw new ConfigException(e);
            } catch (IllegalAccessException e) {
                throw new ConfigException(e);
            } catch (InvocationTargetException e) {
                throw new ConfigException(e.getTargetException());
            }
        }
    }

    public void setProperty(String property) {
        this.property = property;
    }

    public void init() throws IntrospectionException {
        object = applicationContext.getBean(bean);
        BeanInfo beanInfo = Introspector.getBeanInfo(object.getClass());
        PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
        for (int i = 0; i < propertyDescriptors.length; i++) {
            PropertyDescriptor descriptor = propertyDescriptors[i];
            if (property.equals(descriptor.getName())) {
                propertyWriteMethod = descriptor.getWriteMethod();
            }
        }
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setBean(String bean) {
        this.bean = bean;
    }

    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
