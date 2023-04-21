package com.ssd.mda.core.metadata.model;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.lang.ref.WeakReference;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import com.ssd.mda.core.metadata.MetadataProvider;
import com.ssd.mda.core.metadata.model.descriptor.MetadataOperationDescriptor;
import com.ssd.mda.core.metadata.model.descriptor.MetadataAttributeDescriptor;
import com.ssd.mda.core.metadata.util.MetadataUtils;

/**
 * Implementation of type metadata
 * 
 * @author Flavius Burca
 */
public class MetadataType extends AbstractMetadataObject implements IMetadataType {

    private List<MetadataAttributeDescriptor> properties = new ArrayList<MetadataAttributeDescriptor>();

    private List<MetadataOperationDescriptor> methods = new ArrayList<MetadataOperationDescriptor>();

    public Iterable<? extends MetadataAttributeDescriptor> getAttributes() {
        return properties;
    }

    public Iterable<? extends MetadataOperationDescriptor> getOperations() {
        return methods;
    }

    public Iterable<String> getMetadataProperties() {
        List<String> properties = new ArrayList<String>();
        for (WeakReference<MetadataProvider> provider : getProviders()) {
            Class<?> clazz = provider.get().getTypeClass();
            try {
                BeanInfo beanInfo = Introspector.getBeanInfo(clazz);
                java.beans.PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
                if (propertyDescriptors != null) {
                    for (java.beans.PropertyDescriptor descriptor : propertyDescriptors) {
                        Method readMethod = descriptor.getReadMethod();
                        if (readMethod != null) {
                            if (!MetadataUtils.isJSDKMember(readMethod)) {
                                properties.add(descriptor.getName());
                            }
                        }
                    }
                }
            } catch (IntrospectionException e) {
                e.printStackTrace();
            }
        }
        return properties;
    }
}
