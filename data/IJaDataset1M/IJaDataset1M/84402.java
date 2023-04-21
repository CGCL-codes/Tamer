package org.nexopenframework.samples.simple.management;

import static org.springframework.util.Assert.notNull;
import java.util.Set;
import javax.management.Attribute;
import javax.management.AttributeNotFoundException;
import javax.management.InstanceNotFoundException;
import javax.management.IntrospectionException;
import javax.management.InvalidAttributeValueException;
import javax.management.MBeanException;
import javax.management.MBeanInfo;
import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectInstance;
import javax.management.ObjectName;
import javax.management.ReflectionException;
import javax.servlet.ServletContext;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.jmx.JmxException;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * <p>simple using NexOpen</p>
 * 
 * <p>TODO docuemt me</p>
 * 
 * @author Bosco Curtu
 * @author Francesc Xavier Magdaleno
 * @version 1.0
 * @since 0.4.0
 */
public class MBeanServerManager implements ServletContextAware {

    /**unique isntance of this singleton class*/
    private static MBeanServerManager me;

    /**The Application Server MBeanServer*/
    private MBeanServer server;

    /**The context related to web project*/
    private ServletContext sc;

    public MBeanServerManager() {
        me = this;
    }

    /**
	 * 
	 * @see org.springframework.web.context.ServletContextAware#setServletContext(javax.servlet.ServletContext)
	 */
    public void setServletContext(final ServletContext servletContext) {
        this.sc = servletContext;
    }

    /**
	 * <p>The JMX {@link MBeanServer} for retrieve information about registered MBeans</p>
	 * 
	 * @param server the JMX MBean Server
	 */
    public void setServer(final MBeanServer server) {
        this.server = server;
    }

    /**
	 * <p>retrieve all mbeans related to the JMX {@link MBeanServer}</p>
	 * 
	 * @return all the jmx objects in the MBean Server
	 */
    public static Set<ObjectInstance> queryAllMBeans() {
        return queryMBeansByName(null);
    }

    /**
	 * <p></p>
	 * 
	 * @param name
	 * @return
	 */
    @SuppressWarnings("unchecked")
    public static Set<ObjectInstance> queryMBeansByName(final String name) {
        notNull(me, "You MUST initialize this instance");
        try {
            final ObjectName objName = (name != null) ? new ObjectName(name) : null;
            return me.getServer().queryMBeans(objName, null);
        } catch (MalformedObjectNameException e) {
            throw new JmxException("Not correct JMX ObjectName", e);
        }
    }

    /**
	 * @param name
	 * @return
	 */
    public static MBeanData getData(final String name) {
        {
            notNull(me, "You MUST initialize this instance");
            notNull(name, "You MUST initialize this instance");
        }
        try {
            final ObjectName objName = new ObjectName(name);
            final MBeanInfo info = me.getServer().getMBeanInfo(objName);
            return new MBeanData(objName, info);
        } catch (MalformedObjectNameException e) {
            throw new JmxException("Not suitable name " + name, e);
        } catch (InstanceNotFoundException e) {
            throw new JmxException("Instance Not Found Exception with name " + name, e);
        } catch (IntrospectionException e) {
            throw new JmxException("Introspection Exception with name " + name, e);
        } catch (ReflectionException e) {
            throw new JmxException("Reflection Exception with name " + name, e);
        }
    }

    /**
	 * For a given mbean name, gets the value of its attribute
	 * 
	 * @param mbeanName
	 * @param attributeName
	 * @return
	 */
    public static Object getMBeanAttributeValue(final String mbeanName, final String attributeName) {
        try {
            final ObjectName on = ObjectName.getInstance(mbeanName);
            return getMBeanAttributeValue(on, attributeName);
        } catch (MalformedObjectNameException e) {
            throw new JmxException("Not suitable MBean Name " + mbeanName, e);
        }
    }

    /**
	 * For a given MBean ObjectName, gets the value of the attribute
	 * @param objectName
	 * @param attributeName
	 * @return
	 */
    public static Object getMBeanAttributeValue(final ObjectName objectName, final String attributeName) {
        try {
            return me.getServer().getAttribute(objectName, attributeName);
        } catch (AttributeNotFoundException e) {
            throw new JmxException("Not suitable Attribute Name " + attributeName, e);
        } catch (InstanceNotFoundException e) {
            throw new JmxException("ObjectName not found " + objectName, e);
        } catch (MBeanException e) {
            throw new JmxException("Attribute's getter exception " + attributeName, e);
        } catch (ReflectionException e) {
            throw new JmxException("Atribute's Setter exception " + attributeName, e);
        }
    }

    /**
	 * @param mbeanName
	 * @param attributeName
	 * @param attributeValue
	 */
    public static void setMBeanAttributeValue(final String mbeanName, final String attributeName, final Object attributeValue) {
        try {
            final ObjectName on = ObjectName.getInstance(mbeanName);
            setMBeanAttributeValue(on, attributeName, attributeValue);
        } catch (MalformedObjectNameException e) {
            throw new JmxException("Not suitable MBean Name " + mbeanName, e);
        }
    }

    /**
	 * @param objectName
	 * @param attributeName
	 * @param attributeValue
	 */
    public static void setMBeanAttributeValue(final ObjectName objectName, final String attributeName, final Object attributeValue) {
        try {
            final Attribute att = new Attribute(attributeName, attributeValue);
            me.getServer().setAttribute(objectName, att);
        } catch (InvalidAttributeValueException e) {
            throw new JmxException("Invalid Attribute value " + attributeValue, e);
        } catch (AttributeNotFoundException e) {
            throw new JmxException("Not suitable Attribute Name " + attributeName, e);
        } catch (InstanceNotFoundException e) {
            throw new JmxException("ObjectName not found " + objectName, e);
        } catch (MBeanException e) {
            throw new JmxException("Attribute's setter exception " + attributeName, e);
        } catch (ReflectionException e) {
            throw new JmxException("Atribute's Setter exception " + attributeName, e);
        }
    }

    /**
	 * <p>Returns the {@link MBeanServer} associated to the Application Server</p>
	 * 
	 * @return
	 */
    protected MBeanServer getServer() {
        if (this.server == null) {
            final ListableBeanFactory lbf = WebApplicationContextUtils.getWebApplicationContext(sc);
            server = (MBeanServer) BeanFactoryUtils.beanOfTypeIncludingAncestors(lbf, MBeanServer.class);
        }
        return this.server;
    }
}
