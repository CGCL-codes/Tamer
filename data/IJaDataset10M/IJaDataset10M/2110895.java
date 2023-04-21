package org.hl7.v3.provider;

import java.util.Collection;
import java.util.List;
import kr.ac.knu.bkm0714.hl7.provider.Hl7EditPlugin;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.Enumerator;
import org.eclipse.emf.common.util.ResourceLocator;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edit.provider.IEditingDomainItemProvider;
import org.eclipse.emf.edit.provider.IItemLabelProvider;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.IItemPropertySource;
import org.eclipse.emf.edit.provider.IStructuredItemContentProvider;
import org.eclipse.emf.edit.provider.ITreeItemContentProvider;
import org.eclipse.emf.edit.provider.ViewerNotification;
import org.hl7.v3.IVLMO;
import org.hl7.v3.V3Factory;
import org.hl7.v3.V3Package;

/**
 * This is the item provider adapter for a {@link org.hl7.v3.IVLMO} object.
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 * @generated
 */
public class IVLMOItemProvider extends SXCMMOItemProvider implements IEditingDomainItemProvider, IStructuredItemContentProvider, ITreeItemContentProvider, IItemLabelProvider, IItemPropertySource {

    /**
	 * This constructs an instance from a factory and a notifier.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public IVLMOItemProvider(AdapterFactory adapterFactory) {
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
            childrenFeatures.add(V3Package.eINSTANCE.getIVLMO_Low());
            childrenFeatures.add(V3Package.eINSTANCE.getIVLMO_Width());
            childrenFeatures.add(V3Package.eINSTANCE.getIVLMO_High());
            childrenFeatures.add(V3Package.eINSTANCE.getIVLMO_High1());
            childrenFeatures.add(V3Package.eINSTANCE.getIVLMO_Width1());
            childrenFeatures.add(V3Package.eINSTANCE.getIVLMO_High2());
            childrenFeatures.add(V3Package.eINSTANCE.getIVLMO_Center());
            childrenFeatures.add(V3Package.eINSTANCE.getIVLMO_Width2());
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
	 * This returns IVLMO.gif.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    public Object getImage(Object object) {
        return overlayImage(object, getResourceLocator().getImage("full/obj16/IVLMO"));
    }

    /**
	 * This returns the label text for the adapted class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    public String getText(Object object) {
        Enumerator labelValue = ((IVLMO) object).getNullFlavor();
        String label = labelValue == null ? null : labelValue.toString();
        return label == null || label.length() == 0 ? getString("_UI_IVLMO_type") : getString("_UI_IVLMO_type") + " " + label;
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
        switch(notification.getFeatureID(IVLMO.class)) {
            case V3Package.IVLMO__LOW:
            case V3Package.IVLMO__WIDTH:
            case V3Package.IVLMO__HIGH:
            case V3Package.IVLMO__HIGH1:
            case V3Package.IVLMO__WIDTH1:
            case V3Package.IVLMO__HIGH2:
            case V3Package.IVLMO__CENTER:
            case V3Package.IVLMO__WIDTH2:
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
        newChildDescriptors.add(createChildParameter(V3Package.eINSTANCE.getIVLMO_Low(), V3Factory.eINSTANCE.createIVXBMO()));
        newChildDescriptors.add(createChildParameter(V3Package.eINSTANCE.getIVLMO_Width(), V3Factory.eINSTANCE.createMO()));
        newChildDescriptors.add(createChildParameter(V3Package.eINSTANCE.getIVLMO_Width(), V3Factory.eINSTANCE.createSXCMMO()));
        newChildDescriptors.add(createChildParameter(V3Package.eINSTANCE.getIVLMO_Width(), V3Factory.eINSTANCE.createIVLMO()));
        newChildDescriptors.add(createChildParameter(V3Package.eINSTANCE.getIVLMO_Width(), V3Factory.eINSTANCE.createIVXBMO()));
        newChildDescriptors.add(createChildParameter(V3Package.eINSTANCE.getIVLMO_High(), V3Factory.eINSTANCE.createIVXBMO()));
        newChildDescriptors.add(createChildParameter(V3Package.eINSTANCE.getIVLMO_High1(), V3Factory.eINSTANCE.createIVXBMO()));
        newChildDescriptors.add(createChildParameter(V3Package.eINSTANCE.getIVLMO_Width1(), V3Factory.eINSTANCE.createMO()));
        newChildDescriptors.add(createChildParameter(V3Package.eINSTANCE.getIVLMO_Width1(), V3Factory.eINSTANCE.createSXCMMO()));
        newChildDescriptors.add(createChildParameter(V3Package.eINSTANCE.getIVLMO_Width1(), V3Factory.eINSTANCE.createIVLMO()));
        newChildDescriptors.add(createChildParameter(V3Package.eINSTANCE.getIVLMO_Width1(), V3Factory.eINSTANCE.createIVXBMO()));
        newChildDescriptors.add(createChildParameter(V3Package.eINSTANCE.getIVLMO_High2(), V3Factory.eINSTANCE.createIVXBMO()));
        newChildDescriptors.add(createChildParameter(V3Package.eINSTANCE.getIVLMO_Center(), V3Factory.eINSTANCE.createMO()));
        newChildDescriptors.add(createChildParameter(V3Package.eINSTANCE.getIVLMO_Center(), V3Factory.eINSTANCE.createSXCMMO()));
        newChildDescriptors.add(createChildParameter(V3Package.eINSTANCE.getIVLMO_Center(), V3Factory.eINSTANCE.createIVLMO()));
        newChildDescriptors.add(createChildParameter(V3Package.eINSTANCE.getIVLMO_Center(), V3Factory.eINSTANCE.createIVXBMO()));
        newChildDescriptors.add(createChildParameter(V3Package.eINSTANCE.getIVLMO_Width2(), V3Factory.eINSTANCE.createMO()));
        newChildDescriptors.add(createChildParameter(V3Package.eINSTANCE.getIVLMO_Width2(), V3Factory.eINSTANCE.createSXCMMO()));
        newChildDescriptors.add(createChildParameter(V3Package.eINSTANCE.getIVLMO_Width2(), V3Factory.eINSTANCE.createIVLMO()));
        newChildDescriptors.add(createChildParameter(V3Package.eINSTANCE.getIVLMO_Width2(), V3Factory.eINSTANCE.createIVXBMO()));
    }

    /**
	 * This returns the label text for {@link org.eclipse.emf.edit.command.CreateChildCommand}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    public String getCreateChildText(Object owner, Object feature, Object child, Collection<?> selection) {
        Object childFeature = feature;
        Object childObject = child;
        boolean qualify = childFeature == V3Package.eINSTANCE.getIVLMO_Low() || childFeature == V3Package.eINSTANCE.getIVLMO_Width() || childFeature == V3Package.eINSTANCE.getIVLMO_High() || childFeature == V3Package.eINSTANCE.getIVLMO_High1() || childFeature == V3Package.eINSTANCE.getIVLMO_Width1() || childFeature == V3Package.eINSTANCE.getIVLMO_High2() || childFeature == V3Package.eINSTANCE.getIVLMO_Center() || childFeature == V3Package.eINSTANCE.getIVLMO_Width2();
        if (qualify) {
            return getString("_UI_CreateChild_text2", new Object[] { getTypeText(childObject), getFeatureText(childFeature), getTypeText(owner) });
        }
        return super.getCreateChildText(owner, feature, child, selection);
    }

    /**
	 * Return the resource locator for this item provider's resources.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    public ResourceLocator getResourceLocator() {
        return Hl7EditPlugin.INSTANCE;
    }
}
