package bpmn.provider;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.util.ResourceLocator;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edit.provider.ItemPropertyDescriptor;

public class DynamicReadOnlyPropertyDescriptor extends ItemPropertyDescriptor {

    private ReadOnlyValidator validator;

    public DynamicReadOnlyPropertyDescriptor(AdapterFactory adapterFactory, ResourceLocator resourceLocator, String displayName, String description, EStructuralFeature feature, boolean isSettable, Object staticImage, String category, String[] filterFlags, ReadOnlyValidator validator) {
        super(adapterFactory, resourceLocator, displayName, description, feature, isSettable, staticImage, category, filterFlags);
        this.validator = validator;
    }

    @Override
    public boolean canSetProperty(Object object) {
        if (validator != null) return !validator.isReadOnly(object);
        return super.canSetProperty(object);
    }
}
