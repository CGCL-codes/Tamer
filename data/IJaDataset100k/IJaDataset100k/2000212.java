package org.nexopenframework.deployment.processor;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.ejb.EJB;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

/**
 * <p>NexOpen Framework</p>
 * 
 * <p>Comment here</p>
 * 
 * TODO handle injections
 * @author <a href="mailto:fme@nextret.net">Francesc Xavier Magdaleno</a>
 * @version 1.0
 * @since 1.0
 */
public class EJBProcessor implements BeanPostProcessor {

    /** {@link org.apache.commons.logging} logging facility  */
    private final Log logger = LogFactory.getLog(getClass());

    /**
	 * 
	 * @see org.springframework.beans.factory.config.BeanPostProcessor#postProcessAfterInitialization(java.lang.Object, java.lang.String)
	 */
    public Object postProcessAfterInitialization(final Object bean, final String beanName) throws BeansException {
        if (logger.isDebugEnabled()) {
            logger.debug("Processing bean " + bean + " with bean name " + beanName);
        }
        Class<?> clazz = bean.getClass();
        EJB ejb = clazz.getAnnotation(EJB.class);
        if (ejb != null) {
            handleEJB(ejb, bean, null);
        }
        return null;
    }

    /**
	 * 
	 * @see org.springframework.beans.factory.config.BeanPostProcessor#postProcessBeforeInitialization(java.lang.Object, java.lang.String)
	 */
    public Object postProcessBeforeInitialization(final Object bean, final String beanName) throws BeansException {
        return bean;
    }

    protected final void handleEJB(EJB ejb, Object bean, Class<?> type) {
    }

    /**
	 * @param obj
	 * @param classType
	 * @param proxy
	 */
    protected void injectReference(Object obj, Class classType, Object proxy) {
        BeanWrapper wrapper = new BeanWrapperImpl(obj);
        PropertyDescriptor pd = this.findPropertyDescriptorByType(obj, classType);
        wrapper.setPropertyValue(pd.getName(), proxy);
    }

    /**
     * @param bean
     * @param type
     * @return
     */
    private PropertyDescriptor findPropertyDescriptorByType(Object obj, Class type) {
        Method[] methods = getAllMethods(obj);
        for (Method m : methods) {
            if (isSetterMethod(m)) {
                Class<?> _type = m.getParameterTypes()[0];
                if (_type.isAssignableFrom(type)) {
                    PropertyDescriptor p = BeanUtils.findPropertyForMethod(m);
                    return p;
                }
            }
        }
        return null;
    }

    private Method[] getAllMethods(Object obj) {
        Class beanClass = obj.getClass();
        List<Method> methods = new ArrayList<Method>();
        while (beanClass != Object.class) {
            methods.addAll(Arrays.asList(beanClass.getDeclaredMethods()));
            beanClass = beanClass.getSuperclass();
        }
        return methods.toArray(new Method[methods.size()]);
    }

    private boolean isSetterMethod(Method m) {
        return m != null && m.getName().startsWith("set") && m.getParameterTypes().length == 1;
    }
}
