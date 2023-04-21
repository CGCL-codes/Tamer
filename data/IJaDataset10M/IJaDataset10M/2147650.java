package org.archive.util;

import java.lang.reflect.Constructor;
import java.util.List;
import javax.management.Attribute;
import javax.management.AttributeList;
import javax.management.AttributeNotFoundException;
import javax.management.DynamicMBean;
import javax.management.InvalidAttributeValueException;
import javax.management.MBeanAttributeInfo;
import javax.management.MBeanConstructorInfo;
import javax.management.MBeanException;
import javax.management.MBeanInfo;
import javax.management.MBeanNotificationInfo;
import javax.management.MBeanOperationInfo;
import com.sleepycat.je.DatabaseException;
import com.sleepycat.je.Environment;

/**
 * JEApplicationMBean is an example of how a JE application can incorporate JE
 * monitoring into its existing MBean.  It may be installed as is, or used as a
 * starting point for building a MBean which includes JE support.
 * <p>
 * JE management is divided between the JEApplicationMBean class and
 * JEMBeanHelper class. JEApplicationMBean contains an instance of
 * JEMBeanHelper, which knows about JE attributes, operations and
 * notifications. JEApplicationMBean itself has the responsibility of
 * configuring, opening and closing the JE environment along with any other
 * resources used by the application, and maintains a
 * com.sleepycat.je.Environment handle.
 * <p>
 * The approach taken for accessing the environment is an application specific
 * choice. Some of the salient considerations are:
 * <ul>
 * <li>Applications may open one or many Environment objects per process 
 * against a given environment.</li> 
 *
 * <li>All Environment handles reference the same underlying JE environment
 * implementation object.</li>

 * <li> The first Environment object instantiated in the process does the real
 * work of configuring and opening the environment. Follow-on instantiations of
 * Environment merely increment a reference count. Likewise,
 * Environment.close() only does real work when it's called by the last
 * Environment object in the process. </li>
 * </ul>
 * <p>
 * Another MBean approach for environment access can be seen in
 * com.sleepycat.je.jmx.JEMonitor. That MBean does not take responsibility for
 * opening and closing environments, and can only operate against already-open
 * environments.
 * <p>This bean was copied from bdb je 2.0 source and modified so could pass
 * in and monitor an environment created externally.  Also added toString
 * versions of the locks and stats calls since the objects don't seem to
 * make it over the RMI divide (Not serializable.  St.Ack
 */
public class JEApplicationMBean implements DynamicMBean {

    private static final String DESCRIPTION = "A MBean for an application which uses JE. Provides open and close " + "operations which configure and open a JE environment as part of the " + "applications's resources. Also supports general JE monitoring.";

    private MBeanInfo mbeanInfo;

    private JEMBeanHelper jeHelper;

    private Environment targetEnv;

    /**
     * This MBean provides an open operation to open the JE environment.
     */
    public static final String OP_OPEN = "openJE";

    /**
     * This MBean provides a close operation to release the JE environment.
     * Note that environments must be closed to release resources.
     */
    public static final String OP_CLOSE = "closeJE";

    /**
     * Instantiate a JEApplicationMBean
     *
     * @param env Environment to use.  Externally managed.
     * @throws DatabaseException
     */
    public JEApplicationMBean(Environment env) throws DatabaseException {
        this.targetEnv = env;
        jeHelper = new JEMBeanHelper(env.getConfig(), env.getHome(), true);
        resetMBeanInfo();
    }

    /**
     * @see DynamicMBean#getAttribute
     */
    public Object getAttribute(String attributeName) throws AttributeNotFoundException, MBeanException {
        return jeHelper.getAttribute(targetEnv, attributeName);
    }

    /**
     * @see DynamicMBean#setAttribute
     */
    public void setAttribute(Attribute attribute) throws AttributeNotFoundException, InvalidAttributeValueException {
        jeHelper.setAttribute(targetEnv, attribute);
    }

    /**
     * @see DynamicMBean#getAttributes
     */
    public AttributeList getAttributes(String[] attributes) {
        if (attributes == null) {
            throw new IllegalArgumentException("Attributes cannot be null");
        }
        AttributeList results = new AttributeList();
        for (int i = 0; i < attributes.length; i++) {
            try {
                String name = attributes[i];
                Object value = jeHelper.getAttribute(targetEnv, name);
                results.add(new Attribute(name, value));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return results;
    }

    /**
     * @see DynamicMBean#setAttributes
     */
    public AttributeList setAttributes(AttributeList attributes) {
        if (attributes == null) {
            throw new IllegalArgumentException("attribute list can't be null");
        }
        AttributeList results = new AttributeList();
        for (int i = 0; i < attributes.size(); i++) {
            Attribute attr = (Attribute) attributes.get(i);
            try {
                jeHelper.setAttribute(targetEnv, attr);
                String name = attr.getName();
                Object newValue = jeHelper.getAttribute(targetEnv, name);
                results.add(new Attribute(name, newValue));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return results;
    }

    /**
     * @see DynamicMBean#invoke
     */
    public Object invoke(String actionName, Object[] params, String[] signature) throws MBeanException {
        Object result = null;
        if (actionName == null) {
            throw new IllegalArgumentException("actionName cannot be null");
        }
        result = jeHelper.invoke(targetEnv, actionName, params, signature);
        return result;
    }

    /**
     * @see DynamicMBean#getMBeanInfo
     */
    public MBeanInfo getMBeanInfo() {
        return mbeanInfo;
    }

    /**
     * Create the available management interface for this environment.
     * The attributes and operations available vary according to
     * environment configuration.
     *
     */
    private synchronized void resetMBeanInfo() {
        List<MBeanAttributeInfo> attributeList = jeHelper.getAttributeList(targetEnv);
        MBeanAttributeInfo[] attributeInfo = new MBeanAttributeInfo[attributeList.size()];
        attributeList.toArray(attributeInfo);
        Constructor[] constructors = this.getClass().getConstructors();
        MBeanConstructorInfo[] constructorInfo = new MBeanConstructorInfo[constructors.length];
        for (int i = 0; i < constructors.length; i++) {
            constructorInfo[i] = new MBeanConstructorInfo(this.getClass().getName(), constructors[i]);
        }
        List<MBeanOperationInfo> operationList = jeHelper.getOperationList(targetEnv);
        MBeanOperationInfo[] operationInfo = new MBeanOperationInfo[operationList.size()];
        operationList.toArray(operationInfo);
        MBeanNotificationInfo[] notificationInfo = jeHelper.getNotificationInfo(targetEnv);
        mbeanInfo = new MBeanInfo(this.getClass().getName(), DESCRIPTION, attributeInfo, constructorInfo, operationInfo, notificationInfo);
    }
}
