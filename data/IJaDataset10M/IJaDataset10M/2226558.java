package org.openoss.opennms.spring.qosdrx.jmx;

import org.opennms.core.fiber.Fiber;
import org.opennms.core.utils.ThreadCategory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.beans.factory.access.BeanFactoryLocator;
import org.springframework.beans.factory.access.BeanFactoryReference;
import org.springframework.context.access.DefaultLocatorFactory;

/**
 * This JMX bean loads the QoSDrx daemon as a spring bean using the 
 * qosdrx-context.xml file.
 */
public class QoSDrx implements QoSDrxMBean {

    private static final String NAME = org.openoss.opennms.spring.qosdrx.QoSDrx.NAME;

    private ClassPathXmlApplicationContext m_context;

    int m_status = Fiber.START_PENDING;

    ApplicationContext getContext() {
        return m_context;
    }

    public void init() {
        ThreadCategory.setPrefix(QoSDrx.NAME);
    }

    public void start() {
        ThreadCategory.setPrefix(QoSDrx.NAME);
        m_status = Fiber.STARTING;
        ThreadCategory.getInstance().debug("SPRING: thread.classLoader=" + Thread.currentThread().getContextClassLoader());
        BeanFactoryLocator bfl = DefaultLocatorFactory.getInstance();
        BeanFactoryReference bf = bfl.useBeanFactory("daoContext");
        ApplicationContext daoContext = (ApplicationContext) bf.getFactory();
        ThreadCategory.getInstance().debug("QoSDrx using /org/openoss/opennms/spring/qosdrx/qosdrx-spring-context.xml");
        m_context = new ClassPathXmlApplicationContext(new String[] { "/org/openoss/opennms/spring/qosdrx/qosdrx-spring-context.xml" }, daoContext);
        ThreadCategory.getInstance().debug("SPRING: context.classLoader=" + m_context.getClassLoader());
        getQoSDrx().init();
        getQoSDrx().start();
        m_status = Fiber.RUNNING;
    }

    public void stop() {
        ThreadCategory.setPrefix(QoSDrx.NAME);
        m_status = Fiber.STOP_PENDING;
        getQoSDrx().stop();
        m_context.close();
        m_status = Fiber.STOPPED;
    }

    /**
	 * Method to return statistics from the running receivers to MX4J
	 * @return string representation of the statistics for the running receivers
	 */
    public String getRuntimeStatistics() {
        return getQoSDrx().getRuntimeStatistics();
    }

    public String status() {
        ThreadCategory.setPrefix(QoSDrx.NAME);
        return Fiber.STATUS_NAMES[m_status];
    }

    public int getStatus() {
        return m_status;
    }

    public String getStats() {
        return getQoSDrx().getStats();
    }

    /**
	 * Returns the qosdrx singleton
	 * @return qosdrx
	 */
    private org.openoss.opennms.spring.qosdrx.QoSDrx getQoSDrx() {
        org.openoss.opennms.spring.qosdrx.QoSDrx qosdrx = (org.openoss.opennms.spring.qosdrx.QoSDrx) m_context.getBean("QoSDrx");
        qosdrx.setapplicationcontext(m_context);
        return qosdrx;
    }
}
