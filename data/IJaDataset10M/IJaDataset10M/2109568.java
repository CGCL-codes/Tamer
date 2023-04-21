package org.compiere.swing;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.beans.SimpleBeanInfo;

/**
 * Generated
 * @version $Id: CTabbedPaneBeanInfo.java,v 1.2 2003/09/27 11:08:52 jjanke Exp $
 */
public class CTabbedPaneBeanInfo extends SimpleBeanInfo {

    /** Descripción de Campo */
    private Class beanClass = CTabbedPane.class;

    /** Descripción de Campo */
    private String iconColor16x16Filename;

    /** Descripción de Campo */
    private String iconColor32x32Filename;

    /** Descripción de Campo */
    private String iconMono16x16Filename;

    /** Descripción de Campo */
    private String iconMono32x32Filename;

    /**
     * Constructor ...
     *
     */
    public CTabbedPaneBeanInfo() {
    }

    /**
     * Descripción de Método
     *
     *
     * @return
     */
    public BeanInfo[] getAdditionalBeanInfo() {
        Class superclass = beanClass.getSuperclass();
        try {
            BeanInfo superBeanInfo = Introspector.getBeanInfo(superclass);
            return new BeanInfo[] { superBeanInfo };
        } catch (IntrospectionException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    /**
     * Descripción de Método
     *
     *
     * @param iconKind
     *
     * @return
     */
    public java.awt.Image getIcon(int iconKind) {
        switch(iconKind) {
            case BeanInfo.ICON_COLOR_16x16:
                return (iconColor16x16Filename != null) ? loadImage(iconColor16x16Filename) : null;
            case BeanInfo.ICON_COLOR_32x32:
                return (iconColor32x32Filename != null) ? loadImage(iconColor32x32Filename) : null;
            case BeanInfo.ICON_MONO_16x16:
                return (iconMono16x16Filename != null) ? loadImage(iconMono16x16Filename) : null;
            case BeanInfo.ICON_MONO_32x32:
                return (iconMono32x32Filename != null) ? loadImage(iconMono32x32Filename) : null;
        }
        return null;
    }

    /**
     * Descripción de Método
     *
     *
     * @return
     */
    public PropertyDescriptor[] getPropertyDescriptors() {
        try {
            PropertyDescriptor _background = new PropertyDescriptor("background", beanClass, null, "setBackground");
            PropertyDescriptor _backgroundColor = new PropertyDescriptor("backgroundColor", beanClass, "getBackgroundColor", "setBackgroundColor");
            PropertyDescriptor[] pds = new PropertyDescriptor[] { _background, _backgroundColor };
            return pds;
        } catch (IntrospectionException ex) {
            ex.printStackTrace();
            return null;
        }
    }
}
