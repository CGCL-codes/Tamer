package net.etherstorm.jopenrpg.swing.nodehandlers;

import java.beans.*;

/**
 * Title: 		 jOpenRPG
 * Description:  jOpenRPG is an OpenRPG compatible online gaming client.
 * Copyright:  Copyright (c) 2001
 * Company: 		 Etherstorm Software
 * @author $Author: tedberg $
 * @version $Revision: 1.4 $
 */
public class SystemNodeBeanInfo extends SimpleBeanInfo {

    Class beanClass = SystemNode.class;

    String iconColor16x16Filename;

    String iconColor32x32Filename;

    String iconMono16x16Filename;

    String iconMono32x32Filename;

    public SystemNodeBeanInfo() {
    }

    public PropertyDescriptor[] getPropertyDescriptors() {
        try {
            PropertyDescriptor _version = new PropertyDescriptor("version", beanClass, "getVersion", null);
            _version.setDisplayName("Version");
            _version.setShortDescription("Version");
            PropertyDescriptor[] pds = new PropertyDescriptor[] { _version };
            return pds;
        } catch (IntrospectionException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public java.awt.Image getIcon(int iconKind) {
        switch(iconKind) {
            case BeanInfo.ICON_COLOR_16x16:
                return iconColor16x16Filename != null ? loadImage(iconColor16x16Filename) : null;
            case BeanInfo.ICON_COLOR_32x32:
                return iconColor32x32Filename != null ? loadImage(iconColor32x32Filename) : null;
            case BeanInfo.ICON_MONO_16x16:
                return iconMono16x16Filename != null ? loadImage(iconMono16x16Filename) : null;
            case BeanInfo.ICON_MONO_32x32:
                return iconMono32x32Filename != null ? loadImage(iconMono32x32Filename) : null;
        }
        return null;
    }
}
