package org.kjkoster.zapcat.zabbix;

import java.lang.management.ManagementFactory;
import java.util.StringTokenizer;
import javax.management.AttributeNotFoundException;
import javax.management.InstanceNotFoundException;
import javax.management.MBeanException;
import javax.management.MBeanInfo;
import javax.management.MBeanOperationInfo;
import javax.management.MBeanParameterInfo;
import javax.management.MBeanServer;
import javax.management.ObjectInstance;
import javax.management.ObjectName;
import javax.management.ReflectionException;
import javax.management.openmbean.CompositeData;
import org.apache.log4j.Logger;

/**
 * A helper class that abstracts from JMX.
 * 
 * @author Kees Jan Koster &lt;kjkoster@kjkoster.org&gt;
 */
public final class JMXHelper {

    private static final Logger log = Logger.getLogger(JMXHelper.class);

    private static MBeanServer mbeanserver = null;

    /**
     * Locate the mbean server for this JVM instance. We try to look for the
     * JBoss specific mbean server. Failing that, we just use the JVM's platorm
     * mbean server.
     * 
     * @return An appropriate mbean server.
     */
    public static MBeanServer getMBeanServer() {
        if (mbeanserver == null) {
            try {
                mbeanserver = (MBeanServer) Class.forName("org.jboss.mx.util.MBeanServerLocator").getMethod("locateJBoss", (Class[]) null).invoke(null, (Object[]) null);
            } catch (Exception e) {
                mbeanserver = ManagementFactory.getPlatformMBeanServer();
            }
        }
        return mbeanserver;
    }

    /**
     * Perform a JMX query given an mbean name and the name of an attribute on
     * that mbean.
     * 
     * @param objectName
     *            The object name of the mbean to query.
     * @param attribute
     *            The attribute to query for.
     * @return The value of the attribute.
     * @throws InstanceNotFoundException
     *             When the specified mbean could not be located.
     * @throws ReflectionException
     *             When there was a problem inspecting the mbean.
     * @throws MBeanException
     *             When there was a problem inspecting the mbean.
     * @throws AttributeNotFoundException
     *             When the specified attribute could not be found.
     */
    public static String query(final ObjectName objectName, final String attribute) throws InstanceNotFoundException, AttributeNotFoundException, MBeanException, ReflectionException {
        log.debug("JMX query[" + objectName + "][" + attribute + "]");
        final ObjectInstance bean = getMBeanServer().getObjectInstance(objectName);
        log.debug("found MBean class " + bean.getClassName());
        final int dot = attribute.indexOf('.');
        if (dot < 0) {
            final Object ret = getMBeanServer().getAttribute(objectName, attribute);
            return ret == null ? null : ret.toString();
        }
        return resolveFields((CompositeData) getMBeanServer().getAttribute(objectName, attribute.substring(0, dot)), attribute.substring(dot + 1));
    }

    /**
     * Invoke a JMX operation by providing the mbean name, the operation name and arguments.
     * 
     * @param name
     *            The object name of the mbean to invoke operation on.
     * @param operation
     *            The operation to invoke.
     * @param query_args
     * 				The arguments to pass to the operation.
     * @return A String representation of the object returned by invoking the operation.
     * 
     * @throws Exception
     *             UnsupportedOperationException
     *             
     * This is based on the patch submitted anonymously to the project page on sourceforge 
     */
    public static String op_query(final String name, final String operation, final String query_args) throws Exception {
        final MBeanServer mbeanserver = getMBeanServer();
        log.debug("JMX op_query[" + name + "][" + operation + "]" + query_args.substring(1));
        final MBeanInfo info = mbeanserver.getMBeanInfo(new ObjectName(name));
        final MBeanOperationInfo[] ops = info.getOperations();
        StringTokenizer tokens = new StringTokenizer(query_args, "[],", false);
        int op_index = -1;
        boolean op_name_exist = false;
        MBeanParameterInfo[] sig = null;
        for (int i = 0; i < ops.length; i++) {
            if (ops[i].getName().equalsIgnoreCase(operation)) {
                op_name_exist = true;
                sig = ops[i].getSignature();
                if (sig.length == tokens.countTokens()) {
                    op_index = i;
                    break;
                }
            }
        }
        if (op_index == -1 && op_name_exist) throw new UnsupportedOperationException("Incorrect number of arguments."); else if (op_index == -1 && !op_name_exist) throw new UnsupportedOperationException("Operation not found in mbean.");
        String[] string_sig = new String[sig.length];
        Object[] obj_args = new Object[sig.length];
        for (int j = 0; j < sig.length; j++) {
            string_sig[j] = sig[j].getType();
            if (string_sig[j].equals("long")) {
                obj_args[j] = new Long(tokens.nextToken());
            } else if (string_sig[j].equals("int")) {
                obj_args[j] = new Integer(tokens.nextToken());
            } else if (string_sig[j].equals("java.lang.String")) {
                obj_args[j] = new String(tokens.nextToken());
            } else if (string_sig[j].equals("boolean")) {
                obj_args[j] = new Boolean(tokens.nextToken());
            } else if (string_sig[j].equals("float")) {
                obj_args[j] = new Float(tokens.nextToken());
            } else if (string_sig[j].equals("double")) {
                obj_args[j] = new Double(tokens.nextToken());
            }
        }
        return mbeanserver.invoke(new ObjectName(name), operation, obj_args, string_sig).toString();
    }

    private static String resolveFields(final CompositeData attribute, final String field) {
        final int dot = field.indexOf('.');
        if (dot < 0) {
            final Object ret = attribute.get(field);
            return ret == null ? null : ret.toString();
        }
        return resolveFields((CompositeData) attribute.get(field.substring(0, dot)), field.substring(dot + 1));
    }

    /**
     * Try to register a managed bean. Note that errors are logged but then
     * suppressed.
     * 
     * @param mbean
     *            The managed bean to register.
     * @param objectName
     *            The name under which to register the bean.
     * @return The object name of the mbean, for later deregistration.
     */
    public static ObjectName register(final Object mbean, final String objectName) {
        log.debug("registering [" + objectName + "]: " + mbean);
        ObjectName name = null;
        try {
            name = new ObjectName(objectName);
            getMBeanServer().registerMBean(mbean, name);
        } catch (Exception e) {
            log.warn("unable to register '" + name + "'", e);
        }
        return name;
    }

    /**
     * Remove the registration of a bean.
     * 
     * @param objectName
     *            The name of the bean to unregister.
     */
    public static void unregister(final ObjectName objectName) {
        log.debug("un-registering [" + objectName + "]");
        try {
            getMBeanServer().unregisterMBean(objectName);
        } catch (Exception e) {
            log.warn("unable to unregister '" + objectName + "'", e);
        }
    }
}
