package org.xmatthew.spy2servers.adapter.spring;

import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.util.Assert;

/**
 * @author Matthew Xie
 *
 */
public abstract class ContextServiceLocator {

    private static final String SPY2SERVERS_XML_CONF = "spy2servers.xml";

    private static AbstractApplicationContext context;

    public static AbstractApplicationContext getContext() {
        return context;
    }

    public static void setContext(AbstractApplicationContext thecontext) {
        context = thecontext;
    }

    public static void loadContext() {
        context = new ClassPathXmlApplicationContext(SPY2SERVERS_XML_CONF);
        context.start();
    }

    public static Object getBean(String beanName) {
        Assert.notNull(context, "context is null");
        return context.getBean(beanName);
    }

    public static void destroyContext() {
        if (context != null) {
            context.close();
            context = null;
        }
    }
}
