package org.opennms.netmgt.xmlrpcd.jmx;

import org.apache.xmlrpc.XmlRpc;
import org.opennms.core.fiber.Fiber;
import org.opennms.core.utils.BeanUtils;
import org.opennms.core.utils.ThreadCategory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Provisioner implements ProvisionerMBean {

    private ClassPathXmlApplicationContext m_context;

    int m_status = Fiber.START_PENDING;

    ApplicationContext getContext() {
        return m_context;
    }

    public void init() {
        XmlRpc.debug = "true".equalsIgnoreCase(System.getProperty("xmlrpc.debug", "false"));
    }

    public void start() {
        m_status = Fiber.STARTING;
        ThreadCategory.getInstance().debug("SPRING: thread.classLoader=" + Thread.currentThread().getContextClassLoader());
        ;
        m_context = BeanUtils.getFactory("provisionerContext", ClassPathXmlApplicationContext.class);
        ThreadCategory.getInstance().debug("SPRING: context.classLoader=" + m_context.getClassLoader());
        m_status = Fiber.RUNNING;
    }

    public void stop() {
        m_status = Fiber.STOP_PENDING;
        m_context.close();
        m_status = Fiber.STOPPED;
    }

    public String status() {
        return Fiber.STATUS_NAMES[m_status];
    }
}
