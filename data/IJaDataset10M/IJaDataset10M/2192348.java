package org.joone.io;

import java.beans.*;

public class JDBCInputSynapseBeanInfo extends SimpleBeanInfo {

    private static BeanDescriptor getBdescriptor() {
        BeanDescriptor beanDescriptor = new BeanDescriptor(JDBCInputSynapse.class, null);
        return beanDescriptor;
    }

    private static final int PROPERTY_advancedColumnSelector = 0;

    private static final int PROPERTY_buffered = 1;

    private static final int PROPERTY_dbURL = 2;

    private static final int PROPERTY_driverName = 3;

    private static final int PROPERTY_enabled = 4;

    private static final int PROPERTY_firstRow = 5;

    private static final int PROPERTY_inputPatterns = 6;

    private static final int PROPERTY_lastRow = 7;

    private static final int PROPERTY_maxBufSize = 8;

    private static final int PROPERTY_monitor = 9;

    private static final int PROPERTY_name = 10;

    private static final int PROPERTY_plugIn = 11;

    private static final int PROPERTY_SQLQuery = 12;

    private static final int PROPERTY_stepCounter = 13;

    private static PropertyDescriptor[] getPdescriptor() {
        PropertyDescriptor[] properties = new PropertyDescriptor[14];
        try {
            properties[PROPERTY_advancedColumnSelector] = new PropertyDescriptor("advancedColumnSelector", JDBCInputSynapse.class, "getAdvancedColumnSelector", "setAdvancedColumnSelector");
            properties[PROPERTY_advancedColumnSelector].setDisplayName("Advanced Column Selector");
            properties[PROPERTY_buffered] = new PropertyDescriptor("buffered", JDBCInputSynapse.class, "isBuffered", "setBuffered");
            properties[PROPERTY_dbURL] = new PropertyDescriptor("dbURL", JDBCInputSynapse.class, "getdbURL", "setdbURL");
            properties[PROPERTY_driverName] = new PropertyDescriptor("driverName", JDBCInputSynapse.class, "getdriverName", "setdriverName");
            properties[PROPERTY_enabled] = new PropertyDescriptor("enabled", JDBCInputSynapse.class, "isEnabled", "setEnabled");
            properties[PROPERTY_firstRow] = new PropertyDescriptor("firstRow", JDBCInputSynapse.class, "getFirstRow", "setFirstRow");
            properties[PROPERTY_inputPatterns] = new PropertyDescriptor("inputPatterns", JDBCInputSynapse.class, "getInputPatterns", "setInputPatterns");
            properties[PROPERTY_inputPatterns].setExpert(true);
            properties[PROPERTY_lastRow] = new PropertyDescriptor("lastRow", JDBCInputSynapse.class, "getLastRow", "setLastRow");
            properties[PROPERTY_maxBufSize] = new PropertyDescriptor("maxBufSize", JDBCInputSynapse.class, "getMaxBufSize", "setMaxBufSize");
            properties[PROPERTY_monitor] = new PropertyDescriptor("monitor", JDBCInputSynapse.class, "getMonitor", "setMonitor");
            properties[PROPERTY_monitor].setExpert(true);
            properties[PROPERTY_name] = new PropertyDescriptor("name", JDBCInputSynapse.class, "getName", "setName");
            properties[PROPERTY_plugIn] = new PropertyDescriptor("plugIn", JDBCInputSynapse.class, "getPlugIn", "setPlugIn");
            properties[PROPERTY_plugIn].setExpert(true);
            properties[PROPERTY_SQLQuery] = new PropertyDescriptor("SQLQuery", JDBCInputSynapse.class, "getSQLQuery", "setSQLQuery");
            properties[PROPERTY_stepCounter] = new PropertyDescriptor("stepCounter", JDBCInputSynapse.class, "isStepCounter", "setStepCounter");
        } catch (IntrospectionException e) {
        }
        return properties;
    }

    private static EventSetDescriptor[] getEdescriptor() {
        EventSetDescriptor[] eventSets = new EventSetDescriptor[0];
        return eventSets;
    }

    private static MethodDescriptor[] getMdescriptor() {
        MethodDescriptor[] methods = new MethodDescriptor[0];
        return methods;
    }

    private static final int defaultPropertyIndex = -1;

    private static final int defaultEventIndex = -1;

    /**
     * Gets the bean's <code>BeanDescriptor</code>s.
     *
     * @return BeanDescriptor describing the editable
     * properties of this bean.  May return null if the
     * information should be obtained by automatic analysis.
     */
    public BeanDescriptor getBeanDescriptor() {
        return getBdescriptor();
    }

    /**
     * Gets the bean's <code>PropertyDescriptor</code>s.
     *
     * @return An array of PropertyDescriptors describing the editable
     * properties supported by this bean.  May return null if the
     * information should be obtained by automatic analysis.
     * <p>
     * If a property is indexed, then its entry in the result array will
     * belong to the IndexedPropertyDescriptor subclass of PropertyDescriptor.
     * A client of getPropertyDescriptors can use "instanceof" to check
     * if a given PropertyDescriptor is an IndexedPropertyDescriptor.
     */
    public PropertyDescriptor[] getPropertyDescriptors() {
        return getPdescriptor();
    }

    /**
     * Gets the bean's <code>EventSetDescriptor</code>s.
     *
     * @return  An array of EventSetDescriptors describing the kinds of
     * events fired by this bean.  May return null if the information
     * should be obtained by automatic analysis.
     */
    public EventSetDescriptor[] getEventSetDescriptors() {
        return getEdescriptor();
    }

    /**
     * Gets the bean's <code>MethodDescriptor</code>s.
     *
     * @return  An array of MethodDescriptors describing the methods
     * implemented by this bean.  May return null if the information
     * should be obtained by automatic analysis.
     */
    public MethodDescriptor[] getMethodDescriptors() {
        return getMdescriptor();
    }

    /**
     * A bean may have a "default" property that is the property that will
     * mostly commonly be initially chosen for update by human's who are
     * customizing the bean.
     * @return  Index of default property in the PropertyDescriptor array
     * 		returned by getPropertyDescriptors.
     * <P>	Returns -1 if there is no default property.
     */
    public int getDefaultPropertyIndex() {
        return defaultPropertyIndex;
    }

    /**
     * A bean may have a "default" event that is the event that will
     * mostly commonly be used by human's when using the bean.
     * @return Index of default event in the EventSetDescriptor array
     *		returned by getEventSetDescriptors.
     * <P>	Returns -1 if there is no default event.
     */
    public int getDefaultEventIndex() {
        return defaultEventIndex;
    }
}
