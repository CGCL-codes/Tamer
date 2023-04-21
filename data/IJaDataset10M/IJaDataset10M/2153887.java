package com.horstmann.violet.product.diagram.object;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.beans.SimpleBeanInfo;

/**
 * The bean info for the ClassRelationshipEdge type.
 */
public class ObjectRelationshipEdgeBeanInfo extends SimpleBeanInfo {

    @Override
    public PropertyDescriptor[] getPropertyDescriptors() {
        try {
            PropertyDescriptor[] descriptors = new PropertyDescriptor[] { new PropertyDescriptor("startLabel", ObjectRelationshipEdge.class), new PropertyDescriptor("middleLabel", ObjectRelationshipEdge.class), new PropertyDescriptor("endLabel", ObjectRelationshipEdge.class), new PropertyDescriptor("bentStyle", ObjectRelationshipEdge.class) };
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
