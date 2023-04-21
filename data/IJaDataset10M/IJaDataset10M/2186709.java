package com.pjsofts.eurobudget.beans;

import java.beans.*;

public class AccountPELBeanInfo extends SimpleBeanInfo {

    ;

    private static BeanDescriptor getBdescriptor() {
        BeanDescriptor beanDescriptor = new BeanDescriptor(AccountPEL.class, null);
        return beanDescriptor;
    }

    private static final int PROPERTY_iban = 0;

    private static final int PROPERTY_rate = 1;

    private static final int PROPERTY_id = 2;

    private static final int PROPERTY_ccy = 3;

    private static final int PROPERTY_creationDate = 4;

    private static final int PROPERTY_limit = 5;

    private static final int PROPERTY_refs = 6;

    private static final int PROPERTY_bank = 7;

    private static final int PROPERTY_name = 8;

    ;

    private static PropertyDescriptor[] getPdescriptor() {
        PropertyDescriptor[] properties = new PropertyDescriptor[9];
        try {
            properties[PROPERTY_iban] = new PropertyDescriptor("iban", AccountPEL.class, "getIban", "setIban");
            properties[PROPERTY_rate] = new PropertyDescriptor("rate", AccountPEL.class, "getRate", "setRate");
            properties[PROPERTY_id] = new PropertyDescriptor("id", AccountPEL.class, "getId", "setId");
            properties[PROPERTY_ccy] = new PropertyDescriptor("ccy", AccountPEL.class, "getCcy", "setCcy");
            properties[PROPERTY_creationDate] = new PropertyDescriptor("creationDate", AccountPEL.class, "getCreationDate", "setCreationDate");
            properties[PROPERTY_limit] = new PropertyDescriptor("limit", AccountPEL.class, "getLimit", "setLimit");
            properties[PROPERTY_refs] = new PropertyDescriptor("refs", AccountPEL.class, "getRefs", "setRefs");
            properties[PROPERTY_bank] = new PropertyDescriptor("bank", AccountPEL.class, "getBank", "setBank");
            properties[PROPERTY_name] = new PropertyDescriptor("name", AccountPEL.class, "getName", "setName");
        } catch (IntrospectionException e) {
        }
        return properties;
    }

    ;

    private static EventSetDescriptor[] getEdescriptor() {
        EventSetDescriptor[] eventSets = new EventSetDescriptor[0];
        return eventSets;
    }

    private static final int METHOD_toString0 = 0;

    ;

    private static MethodDescriptor[] getMdescriptor() {
        MethodDescriptor[] methods = new MethodDescriptor[1];
        try {
            methods[METHOD_toString0] = new MethodDescriptor(com.pjsofts.eurobudget.beans.AccountPEL.class.getMethod("toString", new Class[] {}));
            methods[METHOD_toString0].setDisplayName("");
        } catch (Exception e) {
        }
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
