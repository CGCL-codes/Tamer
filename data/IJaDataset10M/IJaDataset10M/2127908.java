package org.remus.infomngmnt.provider;

import java.util.Collection;
import java.util.List;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.ResourceLocator;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edit.provider.ComposeableAdapterFactory;
import org.eclipse.emf.edit.provider.IEditingDomainItemProvider;
import org.eclipse.emf.edit.provider.IItemColorProvider;
import org.eclipse.emf.edit.provider.IItemLabelProvider;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.IItemPropertySource;
import org.eclipse.emf.edit.provider.IStructuredItemContentProvider;
import org.eclipse.emf.edit.provider.ITreeItemContentProvider;
import org.eclipse.emf.edit.provider.ItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.ItemProviderAdapter;
import org.eclipse.emf.edit.provider.ViewerNotification;
import org.remus.infomngmnt.InfomngmntFactory;
import org.remus.infomngmnt.InfomngmntPackage;
import org.remus.infomngmnt.InformationStructure;
import org.remus.infomngmnt.InformationStructureType;

/**
 * This is the item provider adapter for a {@link org.remus.infomngmnt.InformationStructure} object.
 * <!-- begin-user-doc
 * --> <!-- end-user-doc -->
 * @generated
 */
public class InformationStructureItemProvider extends ItemProviderAdapter implements IEditingDomainItemProvider, IStructuredItemContentProvider, ITreeItemContentProvider, IItemLabelProvider, IItemPropertySource, IItemColorProvider {

    /**
	 * This constructs an instance from a factory and a notifier. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
    public InformationStructureItemProvider(AdapterFactory adapterFactory) {
        super(adapterFactory);
    }

    /**
	 * This returns the property descriptors for the adapted class. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
    @Override
    public List<IItemPropertyDescriptor> getPropertyDescriptors(Object object) {
        if (itemPropertyDescriptors == null) {
            super.getPropertyDescriptors(object);
            addTypePropertyDescriptor(object);
            addReferencedStructureItemsPropertyDescriptor(object);
            addCanHaveBinaryReferencesPropertyDescriptor(object);
            addLabelPropertyDescriptor(object);
        }
        return itemPropertyDescriptors;
    }

    /**
	 * This adds a property descriptor for the Type feature.
	 * <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 * @generated
	 */
    protected void addTypePropertyDescriptor(Object object) {
        itemPropertyDescriptors.add(createItemPropertyDescriptor(((ComposeableAdapterFactory) adapterFactory).getRootAdapterFactory(), getResourceLocator(), getString("_UI_InformationStructure_type_feature"), getString("_UI_PropertyDescriptor_description", "_UI_InformationStructure_type_feature", "_UI_InformationStructure_type"), InfomngmntPackage.Literals.INFORMATION_STRUCTURE__TYPE, true, false, false, ItemPropertyDescriptor.GENERIC_VALUE_IMAGE, null, null));
    }

    /**
	 * This adds a property descriptor for the Referenced Structure Items feature.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
    protected void addReferencedStructureItemsPropertyDescriptor(Object object) {
        itemPropertyDescriptors.add(createItemPropertyDescriptor(((ComposeableAdapterFactory) adapterFactory).getRootAdapterFactory(), getResourceLocator(), getString("_UI_InformationStructure_referencedStructureItems_feature"), getString("_UI_PropertyDescriptor_description", "_UI_InformationStructure_referencedStructureItems_feature", "_UI_InformationStructure_type"), InfomngmntPackage.Literals.INFORMATION_STRUCTURE__REFERENCED_STRUCTURE_ITEMS, true, false, true, null, null, null));
    }

    /**
	 * This adds a property descriptor for the Can Have Binary References feature.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
    protected void addCanHaveBinaryReferencesPropertyDescriptor(Object object) {
        itemPropertyDescriptors.add(createItemPropertyDescriptor(((ComposeableAdapterFactory) adapterFactory).getRootAdapterFactory(), getResourceLocator(), getString("_UI_InformationStructure_canHaveBinaryReferences_feature"), getString("_UI_PropertyDescriptor_description", "_UI_InformationStructure_canHaveBinaryReferences_feature", "_UI_InformationStructure_type"), InfomngmntPackage.Literals.INFORMATION_STRUCTURE__CAN_HAVE_BINARY_REFERENCES, true, false, false, ItemPropertyDescriptor.BOOLEAN_VALUE_IMAGE, null, null));
    }

    /**
	 * This adds a property descriptor for the Label feature. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
    protected void addLabelPropertyDescriptor(Object object) {
        itemPropertyDescriptors.add(createItemPropertyDescriptor(((ComposeableAdapterFactory) adapterFactory).getRootAdapterFactory(), getResourceLocator(), getString("_UI_InformationStructure_label_feature"), getString("_UI_PropertyDescriptor_description", "_UI_InformationStructure_label_feature", "_UI_InformationStructure_type"), InfomngmntPackage.Literals.INFORMATION_STRUCTURE__LABEL, true, false, false, ItemPropertyDescriptor.GENERIC_VALUE_IMAGE, null, null));
    }

    /**
	 * This specifies how to implement {@link #getChildren} and is used to deduce an appropriate feature for an
	 * {@link org.eclipse.emf.edit.command.AddCommand}, {@link org.eclipse.emf.edit.command.RemoveCommand} or
	 * {@link org.eclipse.emf.edit.command.MoveCommand} in {@link #createCommand}.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    public Collection<? extends EStructuralFeature> getChildrenFeatures(Object object) {
        if (childrenFeatures == null) {
            super.getChildrenFeatures(object);
            childrenFeatures.add(InfomngmntPackage.Literals.INFORMATION_STRUCTURE__STRUCTURE_ITEMS);
        }
        return childrenFeatures;
    }

    /**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    protected EStructuralFeature getChildFeature(Object object, Object child) {
        return super.getChildFeature(object, child);
    }

    /**
	 * This returns InformationStructure.gif.
	 * <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * @generated
	 */
    @Override
    public Object getImage(Object object) {
        return overlayImage(object, getResourceLocator().getImage("full/obj16/InformationStructure"));
    }

    /**
	 * This returns the label text for the adapted class.
	 * <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    public String getText(Object object) {
        InformationStructureType labelValue = ((InformationStructure) object).getType();
        String label = labelValue == null ? null : labelValue.toString();
        return label == null || label.length() == 0 ? getString("_UI_InformationStructure_type") : getString("_UI_InformationStructure_type") + " " + label;
    }

    /**
	 * This handles model notifications by calling {@link #updateChildren} to update any cached
	 * children and by creating a viewer notification, which it passes to {@link #fireNotifyChanged}.
	 * <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * @generated
	 */
    @Override
    public void notifyChanged(Notification notification) {
        updateChildren(notification);
        switch(notification.getFeatureID(InformationStructure.class)) {
            case InfomngmntPackage.INFORMATION_STRUCTURE__TYPE:
            case InfomngmntPackage.INFORMATION_STRUCTURE__CAN_HAVE_BINARY_REFERENCES:
            case InfomngmntPackage.INFORMATION_STRUCTURE__LABEL:
                fireNotifyChanged(new ViewerNotification(notification, notification.getNotifier(), false, true));
                return;
            case InfomngmntPackage.INFORMATION_STRUCTURE__STRUCTURE_ITEMS:
                fireNotifyChanged(new ViewerNotification(notification, notification.getNotifier(), true, false));
                return;
        }
        super.notifyChanged(notification);
    }

    /**
	 * This adds {@link org.eclipse.emf.edit.command.CommandParameter}s
	 * describing the children that can be created under this object. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
    @Override
    protected void collectNewChildDescriptors(Collection<Object> newChildDescriptors, Object object) {
        super.collectNewChildDescriptors(newChildDescriptors, object);
        newChildDescriptors.add(createChildParameter(InfomngmntPackage.Literals.INFORMATION_STRUCTURE__STRUCTURE_ITEMS, InfomngmntFactory.eINSTANCE.createInformationStructureItem()));
        newChildDescriptors.add(createChildParameter(InfomngmntPackage.Literals.INFORMATION_STRUCTURE__STRUCTURE_ITEMS, InfomngmntFactory.eINSTANCE.createDynamicStructure()));
    }

    /**
	 * Return the resource locator for this item provider's resources. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
    @Override
    public ResourceLocator getResourceLocator() {
        return InfomngmntEditPlugin.INSTANCE;
    }
}
