package org.hl7.v3.provider;

import java.util.Collection;
import java.util.List;
import kr.ac.knu.bkm0714.hl7.provider.Hl7EditPlugin;
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
import org.eclipse.emf.edit.provider.ItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.ItemProviderAdapter;
import org.eclipse.emf.edit.provider.ViewerNotification;
import org.hl7.v3.POCDMT000040Component5;
import org.hl7.v3.V3Factory;
import org.hl7.v3.V3Package;

/**
 * This is the item provider adapter for a {@link org.hl7.v3.POCDMT000040Component5} object.
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 * @generated
 */
public class POCDMT000040Component5ItemProvider extends ItemProviderAdapter implements IEditingDomainItemProvider, IStructuredItemContentProvider, ITreeItemContentProvider, IItemLabelProvider, IItemPropertySource {

    /**
	 * This constructs an instance from a factory and a notifier.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public POCDMT000040Component5ItemProvider(AdapterFactory adapterFactory) {
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
            addContextConductionIndPropertyDescriptor(object);
            addNullFlavorPropertyDescriptor(object);
            addTypeCodePropertyDescriptor(object);
        }
        return itemPropertyDescriptors;
    }

    /**
	 * This adds a property descriptor for the Context Conduction Ind feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    protected void addContextConductionIndPropertyDescriptor(Object object) {
        itemPropertyDescriptors.add(createItemPropertyDescriptor(((ComposeableAdapterFactory) adapterFactory).getRootAdapterFactory(), getResourceLocator(), getString("_UI_POCDMT000040Component5_contextConductionInd_feature"), getString("_UI_PropertyDescriptor_description", "_UI_POCDMT000040Component5_contextConductionInd_feature", "_UI_POCDMT000040Component5_type"), V3Package.eINSTANCE.getPOCDMT000040Component5_ContextConductionInd(), true, false, false, ItemPropertyDescriptor.BOOLEAN_VALUE_IMAGE, null, null));
    }

    /**
	 * This adds a property descriptor for the Null Flavor feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    protected void addNullFlavorPropertyDescriptor(Object object) {
        itemPropertyDescriptors.add(createItemPropertyDescriptor(((ComposeableAdapterFactory) adapterFactory).getRootAdapterFactory(), getResourceLocator(), getString("_UI_POCDMT000040Component5_nullFlavor_feature"), getString("_UI_PropertyDescriptor_description", "_UI_POCDMT000040Component5_nullFlavor_feature", "_UI_POCDMT000040Component5_type"), V3Package.eINSTANCE.getPOCDMT000040Component5_NullFlavor(), true, false, false, ItemPropertyDescriptor.GENERIC_VALUE_IMAGE, null, null));
    }

    /**
	 * This adds a property descriptor for the Type Code feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    protected void addTypeCodePropertyDescriptor(Object object) {
        itemPropertyDescriptors.add(createItemPropertyDescriptor(((ComposeableAdapterFactory) adapterFactory).getRootAdapterFactory(), getResourceLocator(), getString("_UI_POCDMT000040Component5_typeCode_feature"), getString("_UI_PropertyDescriptor_description", "_UI_POCDMT000040Component5_typeCode_feature", "_UI_POCDMT000040Component5_type"), V3Package.eINSTANCE.getPOCDMT000040Component5_TypeCode(), true, false, false, ItemPropertyDescriptor.GENERIC_VALUE_IMAGE, null, null));
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
            childrenFeatures.add(V3Package.eINSTANCE.getPOCDMT000040Component5_RealmCode());
            childrenFeatures.add(V3Package.eINSTANCE.getPOCDMT000040Component5_TypeId());
            childrenFeatures.add(V3Package.eINSTANCE.getPOCDMT000040Component5_TemplateId());
            childrenFeatures.add(V3Package.eINSTANCE.getPOCDMT000040Component5_Section());
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
	 * This returns POCDMT000040Component5.gif.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    public Object getImage(Object object) {
        return overlayImage(object, getResourceLocator().getImage("full/obj16/POCDMT000040Component5"));
    }

    /**
	 * This returns the label text for the adapted class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    public String getText(Object object) {
        POCDMT000040Component5 pocdmt000040Component5 = (POCDMT000040Component5) object;
        return getString("_UI_POCDMT000040Component5_type") + " " + pocdmt000040Component5.isContextConductionInd();
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
        switch(notification.getFeatureID(POCDMT000040Component5.class)) {
            case V3Package.POCDMT000040_COMPONENT5__CONTEXT_CONDUCTION_IND:
            case V3Package.POCDMT000040_COMPONENT5__NULL_FLAVOR:
            case V3Package.POCDMT000040_COMPONENT5__TYPE_CODE:
                fireNotifyChanged(new ViewerNotification(notification, notification.getNotifier(), false, true));
                return;
            case V3Package.POCDMT000040_COMPONENT5__REALM_CODE:
            case V3Package.POCDMT000040_COMPONENT5__TYPE_ID:
            case V3Package.POCDMT000040_COMPONENT5__TEMPLATE_ID:
            case V3Package.POCDMT000040_COMPONENT5__SECTION:
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
        newChildDescriptors.add(createChildParameter(V3Package.eINSTANCE.getPOCDMT000040Component5_RealmCode(), V3Factory.eINSTANCE.createCS1()));
        newChildDescriptors.add(createChildParameter(V3Package.eINSTANCE.getPOCDMT000040Component5_TypeId(), V3Factory.eINSTANCE.createPOCDMT000040InfrastructureRootTypeId()));
        newChildDescriptors.add(createChildParameter(V3Package.eINSTANCE.getPOCDMT000040Component5_TemplateId(), V3Factory.eINSTANCE.createII()));
        newChildDescriptors.add(createChildParameter(V3Package.eINSTANCE.getPOCDMT000040Component5_TemplateId(), V3Factory.eINSTANCE.createPOCDMT000040InfrastructureRootTypeId()));
        newChildDescriptors.add(createChildParameter(V3Package.eINSTANCE.getPOCDMT000040Component5_Section(), V3Factory.eINSTANCE.createPOCDMT000040Section()));
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
        boolean qualify = childFeature == V3Package.eINSTANCE.getPOCDMT000040Component5_TypeId() || childFeature == V3Package.eINSTANCE.getPOCDMT000040Component5_TemplateId();
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
