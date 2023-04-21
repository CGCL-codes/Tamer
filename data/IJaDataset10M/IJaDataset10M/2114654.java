package com.ixora.rms.agents.impl.jmx;

import java.util.Set;
import javax.management.AttributeList;
import javax.management.MBeanInfo;
import javax.management.ObjectInstance;
import javax.management.ObjectName;
import javax.management.QueryExp;

/**
 * Interface that hides the real MBeanServer interface in order to support
 * older versions of the JMX spec where MBeanServerConnection is not
 * available. It exposes a subset of MBeanServerConnections methods
 * that are needed for monitoring and the actual delegate will be either
 * an MBeanServerConnection or an MBeanServer for older JMX.
 */
public interface JMXConnection {

    String getDefaultDomain() throws Exception;

    String[] getDomains() throws Exception;

    AttributeList getAttributes(ObjectName name, String[] attributes) throws Exception;

    Object getAttribute(ObjectName name, String attribute) throws Exception;

    Set<ObjectName> queryNames(ObjectName name, QueryExp query) throws Exception;

    Set<ObjectInstance> queryMBeans(ObjectName name, QueryExp query) throws Exception;

    MBeanInfo getMBeanInfo(ObjectName objectName) throws Exception;
}
