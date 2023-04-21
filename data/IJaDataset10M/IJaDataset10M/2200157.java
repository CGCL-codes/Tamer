package org.restfaces.beans;

import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import javax.faces.FacesException;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.xml.parsers.DocumentBuilderFactory;
import org.restfaces.FacesConfigConstants;
import org.restfaces.RestFacesException;
import org.restfaces.annotation.Conversation;
import org.restfaces.annotation.In;
import org.restfaces.annotation.Out;
import org.restfaces.util.ContextManager;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import com.sun.faces.spi.ManagedBeanFactory.Scope;

public class ManagedBeanRegister {

    public static enum BeanScope {

        NONE, REQUEST, SESSION, APPLICATION
    }

    private HashMap<String, ManagedBean> NORMAL_BEAN_MAP;

    private HashMap<String, ManagedBeanFactory> INJECTABLE_BEAN_MAP;

    private HashMap<String, String> INJECTABLE_BEAN_CLASS_MAP;

    private static ManagedBeanRegister instance;

    /**
	 * bean manager must be initialized when the web application starts
	 * @param externalContext
	 * @param configurations
	 */
    private ManagedBeanRegister() {
        INJECTABLE_BEAN_MAP = new HashMap<String, ManagedBeanFactory>();
        INJECTABLE_BEAN_CLASS_MAP = new HashMap<String, String>();
        NORMAL_BEAN_MAP = new HashMap<String, ManagedBean>();
    }

    public static ManagedBeanRegister getInstance() {
        if (instance == null) {
            instance = new ManagedBeanRegister();
        }
        return instance;
    }

    public Map<String, ManagedBeanFactory> getManagedBeanRegisterMap() {
        return INJECTABLE_BEAN_MAP;
    }

    public void registerNormalBean(String beanName, ManagedBean mb) {
        NORMAL_BEAN_MAP.put(beanName, mb);
    }

    public ManagedBean getNormalBean(String beanName) {
        return NORMAL_BEAN_MAP.get(beanName);
    }

    public String getAlias(Object bean) {
        if (bean != null) {
            String name = bean.getClass().getName();
            String beanName = INJECTABLE_BEAN_CLASS_MAP.get(name);
            if (beanName != null) {
                return beanName;
            }
        }
        return null;
    }

    /**
	 * get managed-bean description defined in faces-config.xml
	 * @param alias
	 * @return
	 */
    public ManagedBeanFactory getManagedBeanFactory(String alias) {
        return INJECTABLE_BEAN_MAP.get(alias);
    }

    /**
	 * register managed bean 
	 * @param beanName 
	 * @param beanClass
	 * @param mbf
	 */
    public void registerManagedBeanFactory(String beanName, String beanClass, ManagedBeanFactory beanFactory) {
        INJECTABLE_BEAN_MAP.put(beanName, beanFactory);
        INJECTABLE_BEAN_CLASS_MAP.put(beanClass, beanName);
    }

    /**
	 * get managedBean by bean class name
	 * @param beanClassName
	 * @return
	 */
    public ManagedBean getManagedBeanByClass(String beanClassName) {
        ManagedBean bean = null;
        String beanName = INJECTABLE_BEAN_CLASS_MAP.get(beanClassName);
        if (beanName != null) {
            ManagedBeanFactory beanFactory = INJECTABLE_BEAN_MAP.get(beanName);
            if (beanFactory != null) {
                return beanFactory.getManagedBean();
            }
        }
        return bean;
    }

    /**
	 * get managed bean by registered bean name
	 * @param beanName
	 * @return
	 */
    public ManagedBean getManagedBeanByName(String beanName) {
        ManagedBean bean = null;
        ManagedBeanFactory beanFactory = INJECTABLE_BEAN_MAP.get(beanName);
        if (beanFactory != null) {
            return beanFactory.getManagedBean();
        }
        return bean;
    }

    /**
	 * judge if the bean with given name is registered 
	 * @param managedBeanName
	 * @return
	 */
    public boolean isBeanRegistered(String managedBeanName) {
        return INJECTABLE_BEAN_MAP.containsKey(managedBeanName);
    }

    /**
	 * call bean factory to make new instance 
	 * and judge if this instance needs to be stored in scoped map 
	 * @param context
	 * @param managedBeanName
	 * @return
	 * @throws FacesException
	 */
    public Object createAndMaybeStoreManagedBeans(FacesContext context, String managedBeanName) throws FacesException {
        ManagedBeanFactory beanFactory = INJECTABLE_BEAN_MAP.get(managedBeanName);
        if (beanFactory == null || beanFactory.getManagedBean() == null) {
            return null;
        }
        ManagedBean managedBean = beanFactory.getManagedBean();
        BeanScope scope = managedBean.getBeanScope();
        Object bean = beanFactory.newInstance(context);
        Map<String, Object> scopeMap = null;
        if (scope == BeanScope.APPLICATION) {
            scopeMap = context.getExternalContext().getApplicationMap();
        } else if (scope == BeanScope.SESSION) {
            scopeMap = context.getExternalContext().getSessionMap();
        } else if (scope == BeanScope.REQUEST) {
            scopeMap = context.getExternalContext().getRequestMap();
        }
        if (scopeMap != null) {
            synchronized (scopeMap) {
                scopeMap.put(managedBeanName, bean);
            }
        }
        return bean;
    }
}
