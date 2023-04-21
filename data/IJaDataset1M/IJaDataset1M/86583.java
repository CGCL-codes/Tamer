package org.openremote.web.console;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.transaction.interceptor.TransactionProxyFactoryBean;

/**
 * ApplicationContext for Spring container.
 * 
 */
public class SpringTestContext {

    /** The m_instance. */
    private static SpringTestContext instance;

    /** The context files. */
    private static String[] contextFiles = new String[] { "applicationContext.xml", "annomvc-servlet.xml" };

    /** The ctx. */
    private ApplicationContext ctx;

    /**
    * Instantiates a new spring context.
    */
    public SpringTestContext() {
        ctx = new ClassPathXmlApplicationContext(contextFiles);
    }

    /**
    * Instantiates a new spring context.
    * 
    * @param setting
    *           the setting
    */
    public SpringTestContext(String[] setting) {
        ctx = new ClassPathXmlApplicationContext(setting);
    }

    /**
    * Gets a instance of <code>SpringContext</code>.
    * 
    * @return the instance of <code>SpringContext</code>
    */
    public static synchronized SpringTestContext getInstance() {
        if (instance == null) {
            instance = new SpringTestContext(contextFiles);
        }
        return instance;
    }

    /**
    * Gets a bean instance with the given bean identifier.
    * 
    * @param beanId
    *           the given bean identifier
    * 
    * @return a bean instance
    */
    public Object getBean(String beanId) {
        Object o = ctx.getBean(beanId);
        if (o instanceof TransactionProxyFactoryBean) {
            TransactionProxyFactoryBean factoryBean = (TransactionProxyFactoryBean) o;
            o = factoryBean.getObject();
        }
        return o;
    }
}
