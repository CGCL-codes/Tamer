package de.fraunhofer.isst.eastadl.userattributes.provider;

import de.fraunhofer.isst.eastadl.autosar.provider.EastEditPlugin;
import de.fraunhofer.isst.eastadl.elements.provider.EAElementItemProvider;
import de.fraunhofer.isst.eastadl.userattributes.UserAttributeDefinition;
import de.fraunhofer.isst.eastadl.userattributes.UserattributesPackage;
import java.util.Collection;
import java.util.List;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.ResourceLocator;
import org.eclipse.emf.edit.provider.ComposeableAdapterFactory;
import org.eclipse.emf.edit.provider.IEditingDomainItemProvider;
import org.eclipse.emf.edit.provider.IItemLabelProvider;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.IItemPropertySource;
import org.eclipse.emf.edit.provider.IStructuredItemContentProvider;
import org.eclipse.emf.edit.provider.ITreeItemContentProvider;
import org.eclipse.emf.edit.provider.ItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.ViewerNotification;

/**
 * This is the item provider adapter for a {@link de.fraunhofer.isst.eastadl.userattributes.UserAttributeDefinition} object.
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 * @generated
 */
public class UserAttributeDefinitionItemProvider extends EAElementItemProvider implements IEditingDomainItemProvider, IStructuredItemContentProvider, ITreeItemContentProvider, IItemLabelProvider, IItemPropertySource {

    /**
	 * This constructs an instance from a factory and a notifier.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public UserAttributeDefinitionItemProvider(AdapterFactory adapterFactory) {
        super(adapterFactory);
    }

    /**
	 * This returns the property descriptors for the adapted class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    public List<IItemPropertyDescriptor> getPropertyDescriptors(Object object) {
        if (itemPropertyDescriptors == null) {
            super.getPropertyDescriptors(object);
            addDefaultValuePropertyDescriptor(object);
            addDescriptionPropertyDescriptor(object);
            addKeyPropertyDescriptor(object);
            addTypePropertyDescriptor(object);
        }
        return itemPropertyDescriptors;
    }

    /**
	 * This adds a property descriptor for the Default Value feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    protected void addDefaultValuePropertyDescriptor(Object object) {
        itemPropertyDescriptors.add(createItemPropertyDescriptor(((ComposeableAdapterFactory) adapterFactory).getRootAdapterFactory(), getResourceLocator(), getString("_UI_UserAttributeDefinition_defaultValue_feature"), getString("_UI_PropertyDescriptor_description", "_UI_UserAttributeDefinition_defaultValue_feature", "_UI_UserAttributeDefinition_type"), UserattributesPackage.Literals.USER_ATTRIBUTE_DEFINITION__DEFAULT_VALUE, true, false, false, ItemPropertyDescriptor.GENERIC_VALUE_IMAGE, null, null));
    }

    /**
	 * This adds a property descriptor for the Description feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    protected void addDescriptionPropertyDescriptor(Object object) {
        itemPropertyDescriptors.add(createItemPropertyDescriptor(((ComposeableAdapterFactory) adapterFactory).getRootAdapterFactory(), getResourceLocator(), getString("_UI_UserAttributeDefinition_description_feature"), getString("_UI_PropertyDescriptor_description", "_UI_UserAttributeDefinition_description_feature", "_UI_UserAttributeDefinition_type"), UserattributesPackage.Literals.USER_ATTRIBUTE_DEFINITION__DESCRIPTION, true, false, false, ItemPropertyDescriptor.GENERIC_VALUE_IMAGE, null, null));
    }

    /**
	 * This adds a property descriptor for the Key feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    protected void addKeyPropertyDescriptor(Object object) {
        itemPropertyDescriptors.add(createItemPropertyDescriptor(((ComposeableAdapterFactory) adapterFactory).getRootAdapterFactory(), getResourceLocator(), getString("_UI_UserAttributeDefinition_key_feature"), getString("_UI_PropertyDescriptor_description", "_UI_UserAttributeDefinition_key_feature", "_UI_UserAttributeDefinition_type"), UserattributesPackage.Literals.USER_ATTRIBUTE_DEFINITION__KEY, true, false, false, ItemPropertyDescriptor.GENERIC_VALUE_IMAGE, null, null));
    }

    /**
	 * This adds a property descriptor for the Type feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    protected void addTypePropertyDescriptor(Object object) {
        itemPropertyDescriptors.add(createItemPropertyDescriptor(((ComposeableAdapterFactory) adapterFactory).getRootAdapterFactory(), getResourceLocator(), getString("_UI_UserAttributeDefinition_type_feature"), getString("_UI_PropertyDescriptor_description", "_UI_UserAttributeDefinition_type_feature", "_UI_UserAttributeDefinition_type"), UserattributesPackage.Literals.USER_ATTRIBUTE_DEFINITION__TYPE, true, false, true, null, null, null));
    }

    /**
	 * This returns UserAttributeDefinition.gif.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    public Object getImage(Object object) {
        return overlayImage(object, getResourceLocator().getImage("full/obj16/UserAttributeDefinition"));
    }

    /**
	 * This returns the label text for the adapted class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
    @Override
    public String getText(Object object) {
        String label = ((UserAttributeDefinition) object).getName();
        return label == null || label.length() == 0 ? "unnamed" + "    " + "<" + getString("_UI_UserAttributeDefinition_type") + ">" : label + "    " + "<" + getString("_UI_UserAttributeDefinition_type") + ">";
    }

    /**
	 * This handles model notifications by calling {@link #updateChildren} to update any cached
	 * children and by creating a viewer notification, which it passes to {@link #fireNotifyChanged}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    public void notifyChanged(Notification notification) {
        updateChildren(notification);
        switch(notification.getFeatureID(UserAttributeDefinition.class)) {
            case UserattributesPackage.USER_ATTRIBUTE_DEFINITION__DEFAULT_VALUE:
            case UserattributesPackage.USER_ATTRIBUTE_DEFINITION__DESCRIPTION:
            case UserattributesPackage.USER_ATTRIBUTE_DEFINITION__KEY:
                fireNotifyChanged(new ViewerNotification(notification, notification.getNotifier(), false, true));
                return;
        }
        super.notifyChanged(notification);
    }

    /**
	 * This adds {@link org.eclipse.emf.edit.command.CommandParameter}s describing the children
	 * that can be created under this object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    protected void collectNewChildDescriptors(Collection<Object> newChildDescriptors, Object object) {
        super.collectNewChildDescriptors(newChildDescriptors, object);
    }

    /**
	 * Return the resource locator for this item provider's resources.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    public ResourceLocator getResourceLocator() {
        return EastEditPlugin.INSTANCE;
    }
}
