package de.schlund.pfixcore.webservice.jsonws;

import java.util.HashMap;
import java.util.Map;

public class BeanDescriptorFactory {

    Map<Class, BeanDescriptor> descriptors;

    public BeanDescriptorFactory() {
        descriptors = new HashMap<Class, BeanDescriptor>();
    }

    public synchronized BeanDescriptor getBeanDescriptor(Class clazz) {
        BeanDescriptor desc = descriptors.get(clazz);
        if (desc == null) {
            desc = new BeanDescriptor(clazz);
            descriptors.put(clazz, desc);
        }
        return desc;
    }
}
