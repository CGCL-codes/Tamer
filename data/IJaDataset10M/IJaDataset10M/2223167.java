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
import org.hl7.v3.POCDMT000040ParticipantRole;
import org.hl7.v3.V3Factory;
import org.hl7.v3.V3Package;

/**
 * This is the item provider adapter for a {@link org.hl7.v3.POCDMT000040ParticipantRole} object.
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 * @generated
 */
public class POCDMT000040ParticipantRoleItemProvider extends ItemProviderAdapter implements IEditingDomainItemProvider, IStructuredItemContentProvider, ITreeItemContentProvider, IItemLabelProvider, IItemPropertySource {

    /**
	 * This constructs an instance from a factory and a notifier.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public POCDMT000040ParticipantRoleItemProvider(AdapterFactory adapterFactory) {
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
            addClassCodePropertyDescriptor(object);
            addNullFlavorPropertyDescriptor(object);
        }
        return itemPropertyDescriptors;
    }

    /**
	 * This adds a property descriptor for the Class Code feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    protected void addClassCodePropertyDescriptor(Object object) {
        itemPropertyDescriptors.add(createItemPropertyDescriptor(((ComposeableAdapterFactory) adapterFactory).getRootAdapterFactory(), getResourceLocator(), getString("_UI_POCDMT000040ParticipantRole_classCode_feature"), getString("_UI_PropertyDescriptor_description", "_UI_POCDMT000040ParticipantRole_classCode_feature", "_UI_POCDMT000040ParticipantRole_type"), V3Package.eINSTANCE.getPOCDMT000040ParticipantRole_ClassCode(), true, false, false, ItemPropertyDescriptor.GENERIC_VALUE_IMAGE, null, null));
    }

    /**
	 * This adds a property descriptor for the Null Flavor feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    protected void addNullFlavorPropertyDescriptor(Object object) {
        itemPropertyDescriptors.add(createItemPropertyDescriptor(((ComposeableAdapterFactory) adapterFactory).getRootAdapterFactory(), getResourceLocator(), getString("_UI_POCDMT000040ParticipantRole_nullFlavor_feature"), getString("_UI_PropertyDescriptor_description", "_UI_POCDMT000040ParticipantRole_nullFlavor_feature", "_UI_POCDMT000040ParticipantRole_type"), V3Package.eINSTANCE.getPOCDMT000040ParticipantRole_NullFlavor(), true, false, false, ItemPropertyDescriptor.GENERIC_VALUE_IMAGE, null, null));
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
            childrenFeatures.add(V3Package.eINSTANCE.getPOCDMT000040ParticipantRole_RealmCode());
            childrenFeatures.add(V3Package.eINSTANCE.getPOCDMT000040ParticipantRole_TypeId());
            childrenFeatures.add(V3Package.eINSTANCE.getPOCDMT000040ParticipantRole_TemplateId());
            childrenFeatures.add(V3Package.eINSTANCE.getPOCDMT000040ParticipantRole_Id());
            childrenFeatures.add(V3Package.eINSTANCE.getPOCDMT000040ParticipantRole_Code());
            childrenFeatures.add(V3Package.eINSTANCE.getPOCDMT000040ParticipantRole_Addr());
            childrenFeatures.add(V3Package.eINSTANCE.getPOCDMT000040ParticipantRole_Telecom());
            childrenFeatures.add(V3Package.eINSTANCE.getPOCDMT000040ParticipantRole_PlayingDevice());
            childrenFeatures.add(V3Package.eINSTANCE.getPOCDMT000040ParticipantRole_PlayingEntity());
            childrenFeatures.add(V3Package.eINSTANCE.getPOCDMT000040ParticipantRole_ScopingEntity());
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
	 * This returns POCDMT000040ParticipantRole.gif.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    public Object getImage(Object object) {
        return overlayImage(object, getResourceLocator().getImage("full/obj16/POCDMT000040ParticipantRole"));
    }

    /**
	 * This returns the label text for the adapted class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    public String getText(Object object) {
        Object labelValue = ((POCDMT000040ParticipantRole) object).getClassCode();
        String label = labelValue == null ? null : labelValue.toString();
        return label == null || label.length() == 0 ? getString("_UI_POCDMT000040ParticipantRole_type") : getString("_UI_POCDMT000040ParticipantRole_type") + " " + label;
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
        switch(notification.getFeatureID(POCDMT000040ParticipantRole.class)) {
            case V3Package.POCDMT000040_PARTICIPANT_ROLE__CLASS_CODE:
            case V3Package.POCDMT000040_PARTICIPANT_ROLE__NULL_FLAVOR:
                fireNotifyChanged(new ViewerNotification(notification, notification.getNotifier(), false, true));
                return;
            case V3Package.POCDMT000040_PARTICIPANT_ROLE__REALM_CODE:
            case V3Package.POCDMT000040_PARTICIPANT_ROLE__TYPE_ID:
            case V3Package.POCDMT000040_PARTICIPANT_ROLE__TEMPLATE_ID:
            case V3Package.POCDMT000040_PARTICIPANT_ROLE__ID:
            case V3Package.POCDMT000040_PARTICIPANT_ROLE__CODE:
            case V3Package.POCDMT000040_PARTICIPANT_ROLE__ADDR:
            case V3Package.POCDMT000040_PARTICIPANT_ROLE__TELECOM:
            case V3Package.POCDMT000040_PARTICIPANT_ROLE__PLAYING_DEVICE:
            case V3Package.POCDMT000040_PARTICIPANT_ROLE__PLAYING_ENTITY:
            case V3Package.POCDMT000040_PARTICIPANT_ROLE__SCOPING_ENTITY:
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
        newChildDescriptors.add(createChildParameter(V3Package.eINSTANCE.getPOCDMT000040ParticipantRole_RealmCode(), V3Factory.eINSTANCE.createCS1()));
        newChildDescriptors.add(createChildParameter(V3Package.eINSTANCE.getPOCDMT000040ParticipantRole_TypeId(), V3Factory.eINSTANCE.createPOCDMT000040InfrastructureRootTypeId()));
        newChildDescriptors.add(createChildParameter(V3Package.eINSTANCE.getPOCDMT000040ParticipantRole_TemplateId(), V3Factory.eINSTANCE.createII()));
        newChildDescriptors.add(createChildParameter(V3Package.eINSTANCE.getPOCDMT000040ParticipantRole_TemplateId(), V3Factory.eINSTANCE.createPOCDMT000040InfrastructureRootTypeId()));
        newChildDescriptors.add(createChildParameter(V3Package.eINSTANCE.getPOCDMT000040ParticipantRole_Id(), V3Factory.eINSTANCE.createII()));
        newChildDescriptors.add(createChildParameter(V3Package.eINSTANCE.getPOCDMT000040ParticipantRole_Id(), V3Factory.eINSTANCE.createPOCDMT000040InfrastructureRootTypeId()));
        newChildDescriptors.add(createChildParameter(V3Package.eINSTANCE.getPOCDMT000040ParticipantRole_Code(), V3Factory.eINSTANCE.createCE()));
        newChildDescriptors.add(createChildParameter(V3Package.eINSTANCE.getPOCDMT000040ParticipantRole_Code(), V3Factory.eINSTANCE.createCV()));
        newChildDescriptors.add(createChildParameter(V3Package.eINSTANCE.getPOCDMT000040ParticipantRole_Code(), V3Factory.eINSTANCE.createCO()));
        newChildDescriptors.add(createChildParameter(V3Package.eINSTANCE.getPOCDMT000040ParticipantRole_Code(), V3Factory.eINSTANCE.createCS1()));
        newChildDescriptors.add(createChildParameter(V3Package.eINSTANCE.getPOCDMT000040ParticipantRole_Code(), V3Factory.eINSTANCE.createEIVLEvent()));
        newChildDescriptors.add(createChildParameter(V3Package.eINSTANCE.getPOCDMT000040ParticipantRole_Code(), V3Factory.eINSTANCE.createHXITCE()));
        newChildDescriptors.add(createChildParameter(V3Package.eINSTANCE.getPOCDMT000040ParticipantRole_Code(), V3Factory.eINSTANCE.createPQR()));
        newChildDescriptors.add(createChildParameter(V3Package.eINSTANCE.getPOCDMT000040ParticipantRole_Addr(), V3Factory.eINSTANCE.createAD()));
        newChildDescriptors.add(createChildParameter(V3Package.eINSTANCE.getPOCDMT000040ParticipantRole_Telecom(), V3Factory.eINSTANCE.createTEL()));
        newChildDescriptors.add(createChildParameter(V3Package.eINSTANCE.getPOCDMT000040ParticipantRole_PlayingDevice(), V3Factory.eINSTANCE.createPOCDMT000040Device()));
        newChildDescriptors.add(createChildParameter(V3Package.eINSTANCE.getPOCDMT000040ParticipantRole_PlayingEntity(), V3Factory.eINSTANCE.createPOCDMT000040PlayingEntity()));
        newChildDescriptors.add(createChildParameter(V3Package.eINSTANCE.getPOCDMT000040ParticipantRole_ScopingEntity(), V3Factory.eINSTANCE.createPOCDMT000040Entity()));
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
        boolean qualify = childFeature == V3Package.eINSTANCE.getPOCDMT000040ParticipantRole_RealmCode() || childFeature == V3Package.eINSTANCE.getPOCDMT000040ParticipantRole_Code() || childFeature == V3Package.eINSTANCE.getPOCDMT000040ParticipantRole_TypeId() || childFeature == V3Package.eINSTANCE.getPOCDMT000040ParticipantRole_TemplateId() || childFeature == V3Package.eINSTANCE.getPOCDMT000040ParticipantRole_Id();
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
