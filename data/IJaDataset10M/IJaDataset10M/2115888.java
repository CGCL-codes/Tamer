package com.buschmais.maexo.framework.commons.mbean;

import javax.management.Attribute;
import javax.management.JMException;
import javax.management.MBeanException;
import javax.management.MBeanRegistration;
import javax.management.MBeanServer;
import javax.management.NotificationBroadcasterSupport;
import javax.management.ObjectName;

/**
 * This class provides support for MBean implementations.
 * <p>
 * The following functionality is offered to derived classes:
 * <ul>
 * <li>Access to the MBean server where the MBean has been registered</li>
 * <li>Get and set attributes of other MBeans registered on the same MBean
 * server</li>
 * </ul>
 */
public abstract class MBeanSupport extends NotificationBroadcasterSupport implements MBeanRegistration {

    /**
	 * The mbean server where this mbean is registered. It may be used to lookup
	 * other mbeans by their object name.
	 */
    private MBeanServer mbeanServer;

    /**
	 * {@inheritDoc}
	 */
    public void postDeregister() {
    }

    /**
	 * {@inheritDoc}
	 */
    public void postRegister(Boolean registrationDone) {
    }

    /**
	 * {@inheritDoc}
	 */
    public void preDeregister() throws Exception {
    }

    /**
	 * {@inheritDoc}
	 */
    public final ObjectName preRegister(MBeanServer server, ObjectName name) throws Exception {
        this.mbeanServer = server;
        return name;
    }

    /**
	 * Returns the MBean server.
	 *
	 * @return The MBean server.
	 */
    protected final MBeanServer getMBeanServer() {
        return this.mbeanServer;
    }

    /**
	 * Gets the value of a specific attribute of a named MBean. The MBean is
	 * identified by its object name.
	 *
	 * @param objectName
	 *            The object name of the MBean from which the attribute is to be
	 *            retrieved.
	 * @param attribute
	 *            A String specifying the name of the attribute to be retrieved.
	 *
	 * @return The value of the retrieved attribute.
	 */
    protected final Object getAttribute(ObjectName objectName, String attribute) {
        try {
            return this.mbeanServer.getAttribute(objectName, attribute);
        } catch (MBeanException e) {
            throw new RuntimeException(e.getTargetException().toString());
        } catch (JMException e) {
            throw new IllegalArgumentException(String.format("cannot get attribute %s from mbean %s", attribute, objectName), e);
        }
    }

    /**
	 * Sets the value of a specific attribute of a named MBean. The MBean is
	 * identified by its object name.
	 *
	 * @param objectName
	 *            The object name of the MBean from which the attribute is to be
	 *            set.
	 * @param name
	 *            A String specifying the name of the attribute to be set.
	 * @param value
	 *            The new value of the attribute.
	 */
    protected final void setAttribute(ObjectName objectName, String name, Object value) {
        try {
            Attribute attribute = new Attribute(name, value);
            this.mbeanServer.setAttribute(objectName, attribute);
        } catch (MBeanException e) {
            throw new RuntimeException(e.getTargetException().toString());
        } catch (JMException e) {
            throw new IllegalArgumentException(String.format("cannot set attribute %s from mbean %s", name, objectName), e);
        }
    }
}
