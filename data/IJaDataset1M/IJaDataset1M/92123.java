package org.scopemvc.view.swing;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import org.scopemvc.view.swing.beaninfo.BasicBeanInfo;

/**
 * Beaninfo for SRadioButton
 *
 * @author ludovicc
 * @version $Revision: 1.5 $
 * @created May 28, 2002
 */
public class SRadioButtonBeanInfo extends BasicBeanInfo {

    /**
     * Constructor for the SRadioButtonBeanInfo object
     */
    public SRadioButtonBeanInfo() {
        super(SRadioButton.class);
    }

    /**
     * Gets the property descriptors
     *
     * @return The propertyDescriptors value
     */
    public PropertyDescriptor[] getPropertyDescriptors() {
        try {
            PropertyDescriptor controlID = new PropertyDescriptor("controlID", getBeanClass(), "getControlID", "setControlID");
            controlID.setDisplayName("The control ID");
            controlID.setShortDescription("The ID of the Control that issued when the button state is changed. " + "If null no Control will be issued.");
            PropertyDescriptor selector = new PropertyDescriptor("selector", getBeanClass(), "getSelector", "setSelector");
            selector.setDisplayName("The selector for the model property");
            selector.setShortDescription("The selector identifying the model property containing the " + "button state");
            PropertyDescriptor[] pds = new PropertyDescriptor[] { controlID, selector };
            return pds;
        } catch (IntrospectionException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    /**
     * Gets the additional bean info
     *
     * @return The additionalBeanInfo value
     */
    public BeanInfo[] getAdditionalBeanInfo() {
        Class superclass = getBeanClass().getSuperclass();
        try {
            BeanInfo superBeanInfo = Introspector.getBeanInfo(superclass);
            return new BeanInfo[] { superBeanInfo };
        } catch (IntrospectionException ex) {
            ex.printStackTrace();
            return null;
        }
    }
}
