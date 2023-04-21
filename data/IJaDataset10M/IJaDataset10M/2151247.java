package org.esfinge.aom.model.rolemapper.metadata.descriptors;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class EntityDescriptor {

    private FieldDescriptor entityTypeDescriptor;

    private FieldDescriptor dynamicPropertiesDescriptor;

    private Map<String, FieldDescriptor> fixedPropertiesDescriptors = new HashMap<String, FieldDescriptor>();

    public FieldDescriptor getEntityTypeDescriptor() {
        return entityTypeDescriptor;
    }

    public void setEntityTypeDescriptor(FieldDescriptor entityTypeDescriptor) {
        this.entityTypeDescriptor = entityTypeDescriptor;
    }

    public FieldDescriptor getDynamicPropertiesDescriptor() {
        return dynamicPropertiesDescriptor;
    }

    public void setDynamicPropertiesDescriptor(FieldDescriptor dynamicPropertiesDescriptor) {
        this.dynamicPropertiesDescriptor = dynamicPropertiesDescriptor;
    }

    public final Map<String, FieldDescriptor> getFixedPropertiesDescriptors() {
        return fixedPropertiesDescriptors;
    }

    public FieldDescriptor getFixedPropertiesDescriptor(String name) {
        if (fixedPropertiesDescriptors.containsKey(name)) return fixedPropertiesDescriptors.get(name);
        return null;
    }

    public void setFixedPropertiesDescriptor(Collection<FieldDescriptor> descriptors) {
        this.fixedPropertiesDescriptors.clear();
        for (FieldDescriptor descriptor : descriptors) addFixedPropertiesDescriptors(descriptor);
    }

    public void addFixedPropertiesDescriptors(FieldDescriptor descriptor) {
        this.fixedPropertiesDescriptors.put(descriptor.getFieldName(), descriptor);
    }

    public void removeFixedPropertiesDescriptors(FieldDescriptor descriptor) {
        this.fixedPropertiesDescriptors.remove(descriptor.getFieldName());
    }
}
