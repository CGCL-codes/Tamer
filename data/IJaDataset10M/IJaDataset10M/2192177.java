package bpmn.provider;

import java.util.Collection;
import java.util.List;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.ResourceLocator;
import org.eclipse.emf.edit.provider.ComposeableAdapterFactory;
import org.eclipse.emf.edit.provider.IEditingDomainItemProvider;
import org.eclipse.emf.edit.provider.IItemLabelProvider;
import org.eclipse.emf.edit.provider.IItemPropertySource;
import org.eclipse.emf.edit.provider.IStructuredItemContentProvider;
import org.eclipse.emf.edit.provider.ITreeItemContentProvider;
import org.eclipse.emf.edit.provider.ItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.ViewerNotification;
import bpmn.BpmnFactory;
import bpmn.BpmnPackage;
import bpmn.XOREventGateway;

/**
 * This is the item provider adapter for a {@link bpmn.XOREventGateway} object.
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 * @generated
 */
public class XOREventGatewayItemProvider extends GatewayItemProvider implements IEditingDomainItemProvider, IStructuredItemContentProvider, ITreeItemContentProvider, IItemLabelProvider, IItemPropertySource {

    /**
	 * This constructs an instance from a factory and a notifier.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public XOREventGatewayItemProvider(AdapterFactory adapterFactory) {
        super(adapterFactory);
    }

    /**
	 * This returns the property descriptors for the adapted class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public List getPropertyDescriptors(Object object) {
        if (itemPropertyDescriptors == null) {
            super.getPropertyDescriptors(object);
            addXORTypePropertyDescriptor(object);
        }
        return itemPropertyDescriptors;
    }

    /**
	 * This adds a property descriptor for the XOR Type feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    protected void addXORTypePropertyDescriptor(Object object) {
        itemPropertyDescriptors.add(createItemPropertyDescriptor(((ComposeableAdapterFactory) adapterFactory).getRootAdapterFactory(), getResourceLocator(), getString("_UI_XOREventGateway_xORType_feature"), getString("_UI_PropertyDescriptor_description", "_UI_XOREventGateway_xORType_feature", "_UI_XOREventGateway_type"), BpmnPackage.Literals.XOR_EVENT_GATEWAY__XOR_TYPE, false, false, false, ItemPropertyDescriptor.GENERIC_VALUE_IMAGE, null, null));
    }

    /**
	 * This returns XOREventGateway.gif.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
    public Object getImage(Object object) {
        return overlayImage(object, getResourceLocator().getImage("full/obj16/gateway_xor_event.png"));
    }

    /**
	 * This returns the label text for the adapted class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public String getText(Object object) {
        String label = ((XOREventGateway) object).getName();
        return label == null || label.length() == 0 ? getString("_UI_XOREventGateway_type") : getString("_UI_XOREventGateway_type") + " " + label;
    }

    /**
	 * This handles model notifications by calling {@link #updateChildren} to update any cached
	 * children and by creating a viewer notification, which it passes to {@link #fireNotifyChanged}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void notifyChanged(Notification notification) {
        updateChildren(notification);
        switch(notification.getFeatureID(XOREventGateway.class)) {
            case BpmnPackage.XOR_EVENT_GATEWAY__XOR_TYPE:
                fireNotifyChanged(new ViewerNotification(notification, notification.getNotifier(), false, true));
                return;
        }
        super.notifyChanged(notification);
    }

    /**
	 * This adds to the collection of {@link org.eclipse.emf.edit.command.CommandParameter}s
	 * describing all of the children that can be created under this object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    protected void collectNewChildDescriptors(Collection newChildDescriptors, Object object) {
        super.collectNewChildDescriptors(newChildDescriptors, object);
    }

    /**
	 * Return the resource locator for this item provider's resources.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public ResourceLocator getResourceLocator() {
        return BpmnEditPlugin.INSTANCE;
    }
}
