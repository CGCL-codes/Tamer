package se.mdh.mrtc.saveccm.provider;

import java.util.Collection;
import java.util.List;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.ResourceLocator;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edit.provider.IEditingDomainItemProvider;
import org.eclipse.emf.edit.provider.IItemLabelProvider;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.IItemPropertySource;
import org.eclipse.emf.edit.provider.IStructuredItemContentProvider;
import org.eclipse.emf.edit.provider.ITreeItemContentProvider;
import org.eclipse.emf.edit.provider.ViewerNotification;
import se.mdh.mrtc.saveccm.Element;
import se.mdh.mrtc.saveccm.SaveccmFactory;
import se.mdh.mrtc.saveccm.SaveccmPackage;

/**
 * This is the item provider adapter for a {@link se.mdh.mrtc.saveccm.Element} object.
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 * @generated
 */
public class ElementItemProvider extends NamedItemProvider implements IEditingDomainItemProvider, IStructuredItemContentProvider, ITreeItemContentProvider, IItemLabelProvider, IItemPropertySource {

    /**
	 * This constructs an instance from a factory and a notifier.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public ElementItemProvider(AdapterFactory adapterFactory) {
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
            childrenFeatures.add(SaveccmPackage.Literals.ELEMENT__OFFER);
            childrenFeatures.add(SaveccmPackage.Literals.ELEMENT__SPECIFIE);
            childrenFeatures.add(SaveccmPackage.Literals.ELEMENT__BEHAVIOUR);
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
	 * This returns the label text for the adapted class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    public String getText(Object object) {
        String label = ((Element) object).getName();
        return label == null || label.length() == 0 ? getString("_UI_Element_type") : getString("_UI_Element_type") + " " + label;
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
        switch(notification.getFeatureID(Element.class)) {
            case SaveccmPackage.ELEMENT__OFFER:
            case SaveccmPackage.ELEMENT__SPECIFIE:
            case SaveccmPackage.ELEMENT__BEHAVIOUR:
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
        newChildDescriptors.add(createChildParameter(SaveccmPackage.Literals.ELEMENT__OFFER, SaveccmFactory.eINSTANCE.createTriggerIn()));
        newChildDescriptors.add(createChildParameter(SaveccmPackage.Literals.ELEMENT__OFFER, SaveccmFactory.eINSTANCE.createTriggerOut()));
        newChildDescriptors.add(createChildParameter(SaveccmPackage.Literals.ELEMENT__OFFER, SaveccmFactory.eINSTANCE.createDataIn()));
        newChildDescriptors.add(createChildParameter(SaveccmPackage.Literals.ELEMENT__OFFER, SaveccmFactory.eINSTANCE.createDataOut()));
        newChildDescriptors.add(createChildParameter(SaveccmPackage.Literals.ELEMENT__OFFER, SaveccmFactory.eINSTANCE.createCombinedIn()));
        newChildDescriptors.add(createChildParameter(SaveccmPackage.Literals.ELEMENT__OFFER, SaveccmFactory.eINSTANCE.createCombinedOut()));
        newChildDescriptors.add(createChildParameter(SaveccmPackage.Literals.ELEMENT__SPECIFIE, SaveccmFactory.eINSTANCE.createAttribut()));
        newChildDescriptors.add(createChildParameter(SaveccmPackage.Literals.ELEMENT__BEHAVIOUR, SaveccmFactory.eINSTANCE.createModel()));
    }

    /**
	 * Return the resource locator for this item provider's resources.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    public ResourceLocator getResourceLocator() {
        return SaveccmEditPlugin.INSTANCE;
    }
}
