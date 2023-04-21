package com.horstmann.violet.product.diagram.activity;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.beans.SimpleBeanInfo;

/**
 * The bean info for the ActivityEdge type.
 */
public class ActivityTransitionEdgeBeanInfo extends SimpleBeanInfo {

    @Override
    public PropertyDescriptor[] getPropertyDescriptors() {
        try {
            PropertyDescriptor[] descriptors = new PropertyDescriptor[] { new PropertyDescriptor("startLabel", ActivityTransitionEdge.class), new PropertyDescriptor("middleLabel", ActivityTransitionEdge.class), new PropertyDescriptor("endLabel", ActivityTransitionEdge.class), new PropertyDescriptor("bentStyle", ActivityTransitionEdge.class) };
            for (int i = 0; i < descriptors.length; i++) {
                descriptors[i].setValue("priority", new Integer(i));
            }
            return descriptors;
        } catch (IntrospectionException exception) {
            exception.printStackTrace();
            return null;
        }
    }
}
