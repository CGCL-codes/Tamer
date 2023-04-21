package drcl.inet.core;

import java.beans.*;

/**
 */
public class CoreServiceLayerBeanInfo extends SimpleBeanInfo {

    public PropertyDescriptor[] getPropertyDescriptors() {
        try {
            return Introspector.getBeanInfo(drcl.inet.CoreServiceLayer.class).getPropertyDescriptors();
        } catch (IntrospectionException e_) {
            return null;
        }
    }
}
