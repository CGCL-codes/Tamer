package org.mediavirus.graphl.layout;

import java.beans.BeanDescriptor;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.beans.SimpleBeanInfo;

/**
 * @author Flo Ledermann <ledermann@ims.tuwien.ac.at>
 * created: 03.10.2004 03:13:11
 */
public class DirectedEdgeLayouterBeanInfo extends SimpleBeanInfo {

    private static final Class beanClass = DirectedEdgeLayouter.class;

    public BeanDescriptor getBeanDescriptor() {
        return new BeanDescriptor(beanClass);
    }

    public PropertyDescriptor[] getPropertyDescriptors() {
        try {
            PropertyDescriptor rv[] = { new PropertyDescriptor("direction", beanClass), new PropertyDescriptor("length", beanClass) };
            return rv;
        } catch (IntrospectionException e) {
            throw new Error(e.toString());
        }
    }
}
