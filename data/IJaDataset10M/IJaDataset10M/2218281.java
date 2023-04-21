package de.hu.example.structure;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.beans.SimpleBeanInfo;

/**
 * This is the <b>BeanInfo</b>-class for the {@link SimpleVertex}.
 * 
 * The only reason to provide a BeanInfo-class for this vertex is to
 * hide the property <b>isTheGoodVertex</b> from GrALoG, since
 * it should not be accessible to the user.
 * 
 * @author Sebastian
 *
 */
public class SimpleVertexBeanInfo extends SimpleBeanInfo {

    private static final PropertyDescriptor[] PROPERTY_DESCRIPTORS = new PropertyDescriptor[2];

    static {
        try {
            PROPERTY_DESCRIPTORS[0] = new PropertyDescriptor("label", SimpleVertex.class);
            PROPERTY_DESCRIPTORS[0].setShortDescription("<html> " + "A label for this vertex." + "</html>");
            PROPERTY_DESCRIPTORS[1] = new PropertyDescriptor("number", SimpleVertex.class);
            PROPERTY_DESCRIPTORS[1].setShortDescription("<html> " + "A number for this vertex." + "</html>");
        } catch (IntrospectionException e) {
            e.printStackTrace();
        }
    }

    @Override
    public PropertyDescriptor[] getPropertyDescriptors() {
        return PROPERTY_DESCRIPTORS;
    }
}
