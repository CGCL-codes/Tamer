package org.brandao.brutos.ioc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import org.brandao.brutos.BrutosException;
import org.brandao.brutos.logger.Logger;
import org.brandao.brutos.logger.LoggerProvider;
import org.brandao.brutos.old.programatic.Bean;

/**
 *
 * @author Afonso Brandao
 */
public abstract class IOCProvider {

    /**
     * @deprecated 
     */
    private Map beans;

    public IOCProvider() {
        this.beans = new HashMap();
    }

    public static IOCProvider getProvider(Properties properties) {
        Logger logger = LoggerProvider.getCurrentLoggerProvider().getLogger(IOCProvider.class.getName());
        String iocProviderName = properties.getProperty("org.brandao.brutos.ioc.provider", null);
        IOCProvider ioc = null;
        if (iocProviderName == null) return null;
        try {
            logger.info("IoC provider: " + iocProviderName);
            Class iocProvider = Class.forName(iocProviderName, true, Thread.currentThread().getContextClassLoader());
            ioc = (IOCProvider) iocProvider.newInstance();
        } catch (ClassNotFoundException e) {
            throw new BrutosException(e);
        } catch (InstantiationException e) {
            throw new BrutosException(e);
        } catch (IllegalAccessException e) {
            throw new BrutosException(e);
        }
        return ioc;
    }

    /**
     * @deprecated
     * @param name
     * @return
     */
    public boolean containsBeanDefinition(String name) {
        return beans.containsKey(name);
    }

    /**
     * @deprecated
     * @param bean
     */
    public void addBeanDefinition(Bean bean) {
        beans.put(bean.getInjectable().getName(), bean);
    }

    /**
     * @deprecated 
     * @param bean
     * @return
     */
    public Bean removeBeanDefinition(Bean bean) {
        if (bean != null) return (Bean) beans.remove(bean.getInjectable().getName()); else return null;
    }

    /**
     * @deprecated 
     * @param name
     * @return
     */
    public Bean getBeanDefinition(String name) {
        return (Bean) beans.get(name);
    }

    /**
     * @deprecated 
     * @return
     */
    public List getBeansDefinition() {
        return new ArrayList(beans.values());
    }

    public abstract Object getBean(String name);

    public abstract Object getBean(Class clazz);

    public abstract void configure(Properties properties);

    /**
     * @deprecated 
     * @param name
     * @return
     */
    public Object getInstance(String name) {
        throw new UnsupportedOperationException("use getBean(String)");
    }

    public abstract void destroy();
}
