package geovista.geoviz.condition;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.beans.SimpleBeanInfo;

/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:
 * @author
 * 
 */
public class ConditionManagerBeanInfo extends SimpleBeanInfo {

    Class beanClass = ConditionManager.class;

    String iconColor16x16Filename = "conditionmanager16.gif";

    String iconColor32x32Filename = "conditionmanager32.gif";

    String iconMono16x16Filename;

    String iconMono32x32Filename;

    public ConditionManagerBeanInfo() {
    }

    public PropertyDescriptor[] getPropertyDescriptors() {
        try {
            PropertyDescriptor _attributesArray = new PropertyDescriptor("attributesArray", beanClass, null, "setAttributesArray");
            PropertyDescriptor _conditionRanges = new PropertyDescriptor("conditionRanges", beanClass, null, "setConditionRanges");
            PropertyDescriptor _conditionResults = new PropertyDescriptor("conditionResults", beanClass, "getConditionResults", null);
            PropertyDescriptor _dataObject = new PropertyDescriptor("dataObject", beanClass, null, "setDataObject");
            PropertyDescriptor _doubleDataArrays = new PropertyDescriptor("doubleDataArrays", beanClass, null, "setDoubleDataArrays");
            PropertyDescriptor[] pds = new PropertyDescriptor[] { _attributesArray, _conditionRanges, _conditionResults, _dataObject, _doubleDataArrays };
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
