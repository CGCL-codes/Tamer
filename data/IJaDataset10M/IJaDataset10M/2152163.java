package com.em.validation.client.metadata;

import java.util.HashSet;
import java.util.Set;
import javax.validation.metadata.BeanDescriptor;
import javax.validation.metadata.ConstraintDescriptor;
import javax.validation.metadata.ElementDescriptor;
import javax.validation.metadata.PropertyDescriptor;
import com.em.validation.client.metadata.factory.DescriptorFactory;
import com.em.validation.client.reflector.IReflector;

/**
 * The implementation of the bean descriptor class
 * 
 * @author chris
 *
 */
public class BeanDescriptorImpl extends ProtoDescriptor implements BeanDescriptor {

    public BeanDescriptorImpl(IReflector reflector) {
        super(reflector);
    }

    @Override
    public Set<PropertyDescriptor> getConstrainedProperties() {
        Set<PropertyDescriptor> propertyDescriptors = new HashSet<PropertyDescriptor>();
        if (this.backingReflector == null || this.backingReflector.getPropertyNames() == null) {
            return propertyDescriptors;
        }
        for (String propertyName : this.backingReflector.getPropertyNames()) {
            PropertyDescriptor descriptor = DescriptorFactory.INSTANCE.getPropertyDescriptor(this.backingReflector, propertyName);
            if (descriptor.hasConstraints() || descriptor.isCascaded()) {
                propertyDescriptors.add(descriptor);
            }
        }
        return propertyDescriptors;
    }

    @Override
    public PropertyDescriptor getConstraintsForProperty(String name) {
        if (name == null) {
            throw new IllegalArgumentException("Property name cannot be null.");
        }
        if (!this.backingReflector.getPropertyNames().contains(name)) {
            return null;
        }
        PropertyDescriptor descriptor = DescriptorFactory.INSTANCE.getPropertyDescriptor(this.backingReflector, name);
        if (!descriptor.hasConstraints() && !descriptor.isCascaded()) {
            return null;
        }
        return descriptor;
    }

    @Override
    public boolean isBeanConstrained() {
        return this.backingReflector.getConstraintDescriptors().size() > 0;
    }

    @Override
    public Set<ConstraintDescriptor<?>> getConstraintDescriptors() {
        return backingReflector.getClassConstraintDescriptors();
    }

    @Override
    public Class<?> getElementClass() {
        return this.backingReflector.getTargetClass();
    }

    @Override
    public boolean hasConstraints() {
        return !this.getConstraintDescriptors().isEmpty();
    }

    @Override
    public ConstraintFinder findConstraints() {
        final class PrivateConstraintFinderImpl extends BeanConstraintFinderImpl {

            public PrivateConstraintFinderImpl(IReflector reflector, ElementDescriptor descriptor) {
                super(reflector, descriptor);
            }
        }
        return new PrivateConstraintFinderImpl(this.backingReflector, this);
    }
}
