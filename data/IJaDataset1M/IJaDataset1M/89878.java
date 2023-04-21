package com.javable.cese.models;

import java.awt.Image;
import java.beans.BeanDescriptor;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.beans.SimpleBeanInfo;

public class hodgkin_huxley_2008BeanInfo extends SimpleBeanInfo {

    /** Icon for image data objects. */
    private Image icon;

    /** Extended descriptor for this bean. */
    private BeanDescriptor descriptor;

    /** Array of property descriptors. */
    public hodgkin_huxley_2008BeanInfo() {
        icon = loadImage("model_neuronal.png");
    }

    /** Provides the Models's icon */
    public Image getIcon(int type) {
        return icon;
    }

    /** Provides the Models's descriptor */
    public BeanDescriptor getBeanDescriptor() {
        descriptor = new BeanDescriptor(hodgkin_huxley_2008.class);
        descriptor.setDisplayName("my Hodgkin-Huxley Squid Axon Model, 2008");
        descriptor.setShortDescription("The Hodgkin-Huxley Squid Axon Model, 1952 <p>" + "J Physiol 1952;117:500-544.");
        return descriptor;
    }

    /** Create a new property descriptor
   * @return Property Descriptor
   */
    private PropertyDescriptor createPropertyDescriptor(String name, String gett, String sett, String group) {
        PropertyDescriptor pd = null;
        try {
            pd = new PropertyDescriptor(name, hodgkin_huxley_2008.class, gett, sett);
            pd.setShortDescription(group);
        } catch (IntrospectionException ex) {
        }
        return pd;
    }

    /** Descriptor of valid properties
   * @return array of properties
   */
    public PropertyDescriptor[] getPropertyDescriptors() {
        PropertyDescriptor[] desc = { createPropertyDescriptor("Membrane voltage (mV)", "getv", "setv", "Voltage"), createPropertyDescriptor("Time step (ms)", "getdt", "setdt", "Time Step"), createPropertyDescriptor("Initial time (ms)", "gett", "sett", "Time Step"), createPropertyDescriptor("|OUT Fast Na current (uA/uF)", "getI_Na", null, "Fast Sodium Current (time dependent)"), createPropertyDescriptor("|OUT Time-dependent K current (uA/uF)", "getI_K", null, "Time-dependent Potassium Current"), createPropertyDescriptor("|OUT Leakage current (uA/uF)", "getI_L", null, "Leakage Current"), createPropertyDescriptor("Number of iterations", "getsteps", null, "Time Step") };
        return desc;
    }
}
