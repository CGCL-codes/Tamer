package br.com.linkcom.neo.bean;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.RootBeanDefinition;
import br.com.linkcom.neo.bean.annotation.Bean;
import br.com.linkcom.neo.bean.annotation.CrudBean;
import br.com.linkcom.neo.bean.annotation.DAOBean;
import br.com.linkcom.neo.bean.annotation.Ref;
import br.com.linkcom.neo.bean.annotation.ServiceBean;
import br.com.linkcom.neo.controller.Controller;
import br.com.linkcom.neo.controller.PopUpController;
import br.com.linkcom.neo.util.ReflectionCache;
import br.com.linkcom.neo.util.ReflectionCacheFactory;
import br.com.linkcom.neo.util.Util;

public class PopUpBeanRegister implements TypeBeanRegister {

    protected int autowire = AutowireCapableBeanFactory.AUTOWIRE_AUTODETECT;

    public Class<?> getBeanClass() {
        return PopUpController.class;
    }

    public String getName(Class<?> clazz) {
        if (clazz.getAnnotation(Bean.class) != null || clazz.getAnnotation(CrudBean.class) != null || clazz.getAnnotation(ServiceBean.class) != null || clazz.getAnnotation(DAOBean.class) != null) {
            return null;
        }
        if (clazz.getAnnotation(Controller.class) == null) {
            return null;
        }
        return Util.strings.uncaptalize(clazz.getSimpleName());
    }

    public void registerBeans(List<Class<?>> classes, DefaultListableBeanFactory beanFactory) {
        for (Class<?> class1 : classes) {
            if (Modifier.isAbstract(class1.getModifiers())) continue;
            String name = getName(class1);
            if (name != null) {
                MutablePropertyValues mutablePropertyValues = createMutablePropertyValues(class1);
                RootBeanDefinition beanDefinition = createBeanDefinition(class1, mutablePropertyValues);
                beanDefinition.setAutowireMode(autowire);
                beanFactory.registerBeanDefinition(name, beanDefinition);
            }
        }
    }

    protected String getGenericServiceName(Class<?> class1) {
        String sn = StringUtils.uncapitalize(class1.getSimpleName());
        sn = sn.substring(0, sn.length() - "Popup".length());
        sn += "Service";
        return sn;
    }

    protected MutablePropertyValues createMutablePropertyValues(Class<?> class1) {
        MutablePropertyValues mutablePropertyValues = new MutablePropertyValues();
        String sn = getGenericServiceName(class1);
        mutablePropertyValues.addPropertyValue("genericService", new RuntimeBeanReference(sn));
        ReflectionCache reflectionCache = ReflectionCacheFactory.getReflectionCache();
        Method[] methods = reflectionCache.getMethods(class1);
        for (Method method : methods) {
            if (method.getName().startsWith("set")) {
                String propertyName = StringUtils.uncapitalize(method.getName().substring(3));
                Ref refAnnotation = method.getAnnotation(Ref.class);
                if (refAnnotation != null) {
                    String name = null;
                    if (!StringUtils.isEmpty(refAnnotation.bean())) {
                        name = refAnnotation.bean();
                    }
                    if ("<null>".equals(name)) {
                        mutablePropertyValues.addPropertyValue(propertyName, null);
                    } else {
                        if (autowire != 0) {
                            if (name != null) {
                                mutablePropertyValues.addPropertyValue(propertyName, new RuntimeBeanReference(name));
                            }
                        } else {
                            if (name == null) {
                                name = propertyName;
                            }
                            mutablePropertyValues.addPropertyValue(propertyName, new RuntimeBeanReference(name));
                        }
                    }
                }
            }
        }
        return mutablePropertyValues;
    }

    protected RootBeanDefinition createBeanDefinition(Class<?> class1, MutablePropertyValues mutablePropertyValues) {
        RootBeanDefinition beanDefinition = new RootBeanDefinition(class1, mutablePropertyValues);
        return beanDefinition;
    }
}
