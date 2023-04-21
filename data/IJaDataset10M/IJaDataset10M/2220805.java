package jaxlib.management;

import javax.management.Attribute;
import javax.management.AttributeList;
import javax.management.AttributeNotFoundException;
import javax.management.DynamicMBean;
import javax.management.MBeanAttributeInfo;
import javax.management.MBeanConstructorInfo;
import javax.management.MBeanException;
import javax.management.MBeanInfo;
import javax.management.MBeanOperationInfo;
import javax.management.MBeanServerDelegate;
import javax.management.ReflectionException;

/**
 * @author  <a href="mailto:joerg.wassmer@web.de">Joerg Wassmer</a>
 * @version $Id: MBeanRepositoryServerDelegate.java 2818 2010-07-03 01:18:46Z joerg_wassmer $
 */
final class MBeanRepositoryServerDelegate extends MBeanServerDelegate implements DynamicMBean {

    private static MBeanInfo info;

    MBeanRepositoryServerDelegate() {
        super();
    }

    @Override
    public String getImplementationName() {
        return "JMX";
    }

    @Override
    public String getImplementationVendor() {
        return "jaxlib.sourceforge.net";
    }

    @Override
    public String getImplementationVersion() {
        return "1.0";
    }

    @Override
    public String getSpecificationVersion() {
        return "1.2 Maintenance Release";
    }

    public Object getAttribute(String name) throws AttributeNotFoundException {
        String s = getAttribute0(name);
        if (s != null) return s; else throw new AttributeNotFoundException(name);
    }

    public AttributeList getAttributes(String[] attributes) {
        AttributeList result = new AttributeList(attributes.length);
        for (String name : attributes) {
            String v = getAttribute0(name);
            if (v != null) result.add(new Attribute(name, v));
        }
        return result;
    }

    private String getAttribute0(String name) {
        if ("ImplementationName".equals(name)) return getImplementationName(); else if ("ImplementationVendor".equals(name)) return getImplementationVendor(); else if ("ImplementationVersion".equals(name)) return getImplementationVersion(); else if ("MBeanServerId".equals(name)) return getMBeanServerId(); else if ("SpecificationName".equals(name)) return getSpecificationName(); else if ("SpecificationVendor".equals(name)) return getSpecificationVendor(); else if ("SpecificationVersion".equals(name)) return getSpecificationVersion(); else return null;
    }

    public MBeanInfo getMBeanInfo() {
        if (info != null) return info;
        MBeanAttributeInfo[] attributes = new MBeanAttributeInfo[] { new MBeanAttributeInfo("ImplementationName", "java.lang.String", "The JMX implementation name", true, false, false), new MBeanAttributeInfo("ImplementationVendor", "java.lang.String", "The JMX implementation vendor", true, false, false), new MBeanAttributeInfo("ImplementationVersion", "java.lang.String", "The JMX implementation version", true, false, false), new MBeanAttributeInfo("MBeanServerId", "java.lang.String", "The MBean server agent identity.", true, false, false), new MBeanAttributeInfo("SpecificationName", "java.lang.String", "The JMX specification name", true, false, false), new MBeanAttributeInfo("SpecificationVendor", "java.lang.String", "The JMX specification vendor", true, false, false), new MBeanAttributeInfo("SpecificationVersion", "java.lang.String", "The JMX specification version", true, false, false) };
        info = new MBeanInfo(getClass().getName(), "The management interface of an object of class MBeanServerDelegate", attributes, null, null, null);
        return info;
    }

    public Object invoke(String actionName, Object[] params, String[] signature) throws MBeanException, ReflectionException {
        throw new ReflectionException(null, "No such operation: " + actionName);
    }

    public void setAttribute(Attribute attribute) throws AttributeNotFoundException {
        throw new AttributeNotFoundException("All attributes of this MBean are read-only");
    }

    public AttributeList setAttributes(AttributeList attributes) {
        return new AttributeList(0);
    }
}
