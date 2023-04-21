package org.eclipse.emf.edit.provider.resource;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.ResourceLocator;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.edit.EMFEditPlugin;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.edit.provider.IEditingDomainItemProvider;
import org.eclipse.emf.edit.provider.IItemLabelProvider;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.IItemPropertySource;
import org.eclipse.emf.edit.provider.IStructuredItemContentProvider;
import org.eclipse.emf.edit.provider.ITreeItemContentProvider;
import org.eclipse.emf.edit.provider.ItemProviderAdapter;
import org.eclipse.emf.edit.provider.ViewerNotification;

/**
 * This is the item provider adapter for a {@link org.eclipse.emf.ecore.resource.ResourceSet} object.
 */
public class ResourceSetItemProvider extends ItemProviderAdapter implements IEditingDomainItemProvider, IStructuredItemContentProvider, ITreeItemContentProvider, IItemLabelProvider, IItemPropertySource {

    /**
   * This constructs an instance from a factory and a notifier.
   */
    public ResourceSetItemProvider(AdapterFactory adapterFactory) {
        super(adapterFactory);
    }

    /**
   * This returns the property descriptors for the adapted class.
   */
    @Override
    public List<IItemPropertyDescriptor> getPropertyDescriptors(Object object) {
        if (itemPropertyDescriptors == null) {
            super.getPropertyDescriptors(object);
        }
        return itemPropertyDescriptors;
    }

    @Override
    public Collection<?> getChildren(Object object) {
        ResourceSet resourceSet = (ResourceSet) object;
        return resourceSet.getResources();
    }

    /**
   * This specifies how to implement {@link #getChildren} and is used to deduce an appropriate feature for an
   * {@link org.eclipse.emf.edit.command.AddCommand}, {@link org.eclipse.emf.edit.command.RemoveCommand} or
   * {@link org.eclipse.emf.edit.command.MoveCommand} in {@link #createCommand(Object, EditingDomain, Class, org.eclipse.emf.edit.command.CommandParameter) createCommand}.
   */
    @Override
    public Collection<? extends EStructuralFeature> getChildrenFeatures(Object object) {
        if (childrenFeatures == null) {
            super.getChildrenFeatures(object);
        }
        return childrenFeatures;
    }

    /**
   * This returns the parent of the ResourceSet.
   */
    @Override
    public Object getParent(Object object) {
        return null;
    }

    /**
   * This returns ResourceSet.gif.
   */
    @Override
    public Object getImage(Object object) {
        return getResourceLocator().getImage("full/obj16/ResourceSet");
    }

    /**
   * This returns the label text for the adapted class.
   */
    @Override
    public String getText(Object object) {
        return EMFEditPlugin.INSTANCE.getString("_UI_ResourceSet_label");
    }

    /**
   * This handles notification by calling {@link #fireNotifyChanged(Notification) fireNotifyChanged}.
   */
    @Override
    public void notifyChanged(Notification notification) {
        switch(notification.getFeatureID(ResourceSet.class)) {
            case ResourceSet.RESOURCE_SET__RESOURCES:
                {
                    fireNotifyChanged(new ViewerNotification(notification, notification.getNotifier(), true, false));
                    return;
                }
        }
        super.notifyChanged(notification);
    }

    @Override
    public Collection<?> getNewChildDescriptors(Object object, EditingDomain editingDomain, Object sibling) {
        return Collections.emptyList();
    }

    /**
   * This adds {@link org.eclipse.emf.edit.command.CommandParameter}s describing the children
   * that can be created under this object.
   */
    @Override
    protected void collectNewChildDescriptors(Collection<Object> newChildDescriptors, Object object) {
        super.collectNewChildDescriptors(newChildDescriptors, object);
    }

    /**
   * Return the resource locator for this item provider's resources.
   */
    @Override
    public ResourceLocator getResourceLocator() {
        return EMFEditPlugin.INSTANCE;
    }
}
