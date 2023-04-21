package com.horstmann.violet.framework;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.beans.SimpleBeanInfo;

/**
   The bean info for the ClassRelationshipEdge type.
*/
public class JMatterAssociationEdgeBeanInfo extends SimpleBeanInfo {

    public PropertyDescriptor[] getPropertyDescriptors() {
        return descriptors;
    }

    private static PropertyDescriptor[] descriptors;

    static {
        try {
            descriptors = new PropertyDescriptor[] { new PropertyDescriptor("bentStyle", JMatterAssociationEdge.class), new PropertyDescriptor("startNavigable", JMatterAssociationEdge.class), new PropertyDescriptor("startName", JMatterAssociationEdge.class), new PropertyDescriptor("startMultiplicity", JMatterAssociationEdge.class), new PropertyDescriptor("startAnnotations", JMatterAssociationEdge.class), new PropertyDescriptor("endNavigable", JMatterAssociationEdge.class), new PropertyDescriptor("endName", JMatterAssociationEdge.class), new PropertyDescriptor("endMultiplicity", JMatterAssociationEdge.class), new PropertyDescriptor("endAnnotations", JMatterAssociationEdge.class) };
            for (int i = 0; i < descriptors.length; i++) descriptors[i].setValue("priority", new Integer(i));
        } catch (IntrospectionException exception) {
            exception.printStackTrace();
        }
    }
}
