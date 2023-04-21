package de.hu.dfa.structure;

import java.beans.BeanDescriptor;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.beans.SimpleBeanInfo;
import de.hu.gralog.beans.propertydescriptor.ChooseStructureElementPropertyDescriptor;
import de.hu.gralog.beans.propertydescriptor.StructureElementFilter.VertexStructureElementFilter;

public class AutomatonBeanBeanInfo extends SimpleBeanInfo {

    private static final BeanDescriptor BEAN_DESCRIPTOR = new BeanDescriptor(AutomatonBean.class);

    private static final PropertyDescriptor[] PROPERTY_DESCRIPTORS = new PropertyDescriptor[1];

    static {
        try {
            BEAN_DESCRIPTOR.setShortDescription("Here you can select the starting-state for that automaton.");
            PROPERTY_DESCRIPTORS[0] = new ChooseStructureElementPropertyDescriptor("startVertex", AutomatonBean.class, new VertexStructureElementFilter());
            PROPERTY_DESCRIPTORS[0].setShortDescription("<html>" + "The start-state of the automaton. " + "</html>");
        } catch (IntrospectionException e) {
            e.printStackTrace();
        }
    }

    @Override
    public BeanDescriptor getBeanDescriptor() {
        return BEAN_DESCRIPTOR;
    }

    @Override
    public PropertyDescriptor[] getPropertyDescriptors() {
        return PROPERTY_DESCRIPTORS;
    }
}
