package com.ctb.provider;

import com.ctb.CtbFactory;
import com.ctb.CtbPackage;
import com.ctb.Diagram;
import java.util.Collection;
import java.util.List;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.ResourceLocator;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edit.provider.ComposeableAdapterFactory;
import org.eclipse.emf.edit.provider.IEditingDomainItemProvider;
import org.eclipse.emf.edit.provider.IItemLabelProvider;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.IItemPropertySource;
import org.eclipse.emf.edit.provider.IStructuredItemContentProvider;
import org.eclipse.emf.edit.provider.ITreeItemContentProvider;
import org.eclipse.emf.edit.provider.ItemProviderAdapter;
import org.eclipse.emf.edit.provider.ViewerNotification;

/**
 * This is the item provider adapter for a {@link com.ctb.Diagram} object.
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 * @generated
 */
public class DiagramItemProvider extends ItemProviderAdapter implements IEditingDomainItemProvider, IStructuredItemContentProvider, ITreeItemContentProvider, IItemLabelProvider, IItemPropertySource {

    /**
	 * This constructs an instance from a factory and a notifier.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public DiagramItemProvider(AdapterFactory adapterFactory) {
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
        }
        return itemPropertyDescriptors;
    }

    /**
	 * This specifies how to implement {@link #getChildren} and is used to deduce an appropriate feature for an
	 * {@link org.eclipse.emf.edit.command.AddCommand}, {@link org.eclipse.emf.edit.command.RemoveCommand} or
	 * {@link org.eclipse.emf.edit.command.MoveCommand} in {@link #createCommand}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    public Collection<? extends EStructuralFeature> getChildrenFeatures(Object object) {
        if (childrenFeatures == null) {
            super.getChildrenFeatures(object);
            childrenFeatures.add(CtbPackage.Literals.DIAGRAM__PHYSICAL_COMPONENT);
            childrenFeatures.add(CtbPackage.Literals.DIAGRAM__PHYSICAL_CONNECTION);
            childrenFeatures.add(CtbPackage.Literals.DIAGRAM__DEFAULT_CONNECTION);
            childrenFeatures.add(CtbPackage.Literals.DIAGRAM__PHYSICAL_CONNECTOR);
        }
        return childrenFeatures;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    protected EStructuralFeature getChildFeature(Object object, Object child) {
        return super.getChildFeature(object, child);
    }

    /**
	 * This returns Diagram.gif.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    public Object getImage(Object object) {
        return overlayImage(object, getResourceLocator().getImage("full/obj16/Diagram"));
    }

    /**
	 * This returns the label text for the adapted class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    public String getText(Object object) {
        return getString("_UI_Diagram_type");
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
        switch(notification.getFeatureID(Diagram.class)) {
            case CtbPackage.DIAGRAM__PHYSICAL_COMPONENT:
            case CtbPackage.DIAGRAM__PHYSICAL_CONNECTION:
            case CtbPackage.DIAGRAM__DEFAULT_CONNECTION:
            case CtbPackage.DIAGRAM__PHYSICAL_CONNECTOR:
                fireNotifyChanged(new ViewerNotification(notification, notification.getNotifier(), true, false));
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
        newChildDescriptors.add(createChildParameter(CtbPackage.Literals.DIAGRAM__PHYSICAL_COMPONENT, CtbFactory.eINSTANCE.createSmartCard()));
        newChildDescriptors.add(createChildParameter(CtbPackage.Literals.DIAGRAM__PHYSICAL_COMPONENT, CtbFactory.eINSTANCE.createMobileDevice()));
        newChildDescriptors.add(createChildParameter(CtbPackage.Literals.DIAGRAM__PHYSICAL_COMPONENT, CtbFactory.eINSTANCE.createWorkstation()));
        newChildDescriptors.add(createChildParameter(CtbPackage.Literals.DIAGRAM__PHYSICAL_COMPONENT, CtbFactory.eINSTANCE.createEnterpriseServer()));
        newChildDescriptors.add(createChildParameter(CtbPackage.Literals.DIAGRAM__PHYSICAL_COMPONENT, CtbFactory.eINSTANCE.createOfficeServer()));
        newChildDescriptors.add(createChildParameter(CtbPackage.Literals.DIAGRAM__PHYSICAL_COMPONENT, CtbFactory.eINSTANCE.createInfrastructureComponent()));
        newChildDescriptors.add(createChildParameter(CtbPackage.Literals.DIAGRAM__PHYSICAL_CONNECTION, CtbFactory.eINSTANCE.createPhysicalConnection()));
        newChildDescriptors.add(createChildParameter(CtbPackage.Literals.DIAGRAM__DEFAULT_CONNECTION, CtbFactory.eINSTANCE.createDefaultConnection()));
        newChildDescriptors.add(createChildParameter(CtbPackage.Literals.DIAGRAM__PHYSICAL_CONNECTOR, CtbFactory.eINSTANCE.createWire()));
        newChildDescriptors.add(createChildParameter(CtbPackage.Literals.DIAGRAM__PHYSICAL_CONNECTOR, CtbFactory.eINSTANCE.createWireless()));
        newChildDescriptors.add(createChildParameter(CtbPackage.Literals.DIAGRAM__PHYSICAL_CONNECTOR, CtbFactory.eINSTANCE.createShortrange()));
        newChildDescriptors.add(createChildParameter(CtbPackage.Literals.DIAGRAM__PHYSICAL_CONNECTOR, CtbFactory.eINSTANCE.createMobileCellNetwork()));
    }

    /**
	 * Return the resource locator for this item provider's resources.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    public ResourceLocator getResourceLocator() {
        return CtbEditPlugin.INSTANCE;
    }
}
