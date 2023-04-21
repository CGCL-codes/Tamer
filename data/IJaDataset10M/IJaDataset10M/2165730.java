package de.hu.dfa.structure;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.beans.SimpleBeanInfo;

public class AutomatonEdgeBeanInfo extends SimpleBeanInfo {

    private static final PropertyDescriptor[] PROPERTY_DESCRIPTORS = new PropertyDescriptor[1];

    static {
        try {
            PROPERTY_DESCRIPTORS[0] = new PropertyDescriptor("label", AutomatonEdge.class);
            PROPERTY_DESCRIPTORS[0].setDisplayName("symbol");
            PROPERTY_DESCRIPTORS[0].setShortDescription("<html>" + "The <i>symbol</i> determining the transition represented by this edge." + "</html>");
        } catch (IntrospectionException e) {
            e.printStackTrace();
        }
    }

    @Override
    public PropertyDescriptor[] getPropertyDescriptors() {
        return PROPERTY_DESCRIPTORS;
    }
}
