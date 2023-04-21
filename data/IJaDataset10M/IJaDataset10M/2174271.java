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
import org.hl7.v3.ActClassSupply;
import org.hl7.v3.POCDMT000040Supply;
import org.hl7.v3.V3Factory;
import org.hl7.v3.V3Package;

/**
 * This is the item provider adapter for a {@link org.hl7.v3.POCDMT000040Supply} object.
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 * @generated
 */
public class POCDMT000040SupplyItemProvider extends ItemProviderAdapter implements IEditingDomainItemProvider, IStructuredItemContentProvider, ITreeItemContentProvider, IItemLabelProvider, IItemPropertySource {

    /**
	 * This constructs an instance from a factory and a notifier.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public POCDMT000040SupplyItemProvider(AdapterFactory adapterFactory) {
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
            addMoodCodePropertyDescriptor(object);
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
        itemPropertyDescriptors.add(createItemPropertyDescriptor(((ComposeableAdapterFactory) adapterFactory).getRootAdapterFactory(), getResourceLocator(), getString("_UI_POCDMT000040Supply_classCode_feature"), getString("_UI_PropertyDescriptor_description", "_UI_POCDMT000040Supply_classCode_feature", "_UI_POCDMT000040Supply_type"), V3Package.eINSTANCE.getPOCDMT000040Supply_ClassCode(), true, false, false, ItemPropertyDescriptor.GENERIC_VALUE_IMAGE, null, null));
    }

    /**
	 * This adds a property descriptor for the Mood Code feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    protected void addMoodCodePropertyDescriptor(Object object) {
        itemPropertyDescriptors.add(createItemPropertyDescriptor(((ComposeableAdapterFactory) adapterFactory).getRootAdapterFactory(), getResourceLocator(), getString("_UI_POCDMT000040Supply_moodCode_feature"), getString("_UI_PropertyDescriptor_description", "_UI_POCDMT000040Supply_moodCode_feature", "_UI_POCDMT000040Supply_type"), V3Package.eINSTANCE.getPOCDMT000040Supply_MoodCode(), true, false, false, ItemPropertyDescriptor.GENERIC_VALUE_IMAGE, null, null));
    }

    /**
	 * This adds a property descriptor for the Null Flavor feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    protected void addNullFlavorPropertyDescriptor(Object object) {
        itemPropertyDescriptors.add(createItemPropertyDescriptor(((ComposeableAdapterFactory) adapterFactory).getRootAdapterFactory(), getResourceLocator(), getString("_UI_POCDMT000040Supply_nullFlavor_feature"), getString("_UI_PropertyDescriptor_description", "_UI_POCDMT000040Supply_nullFlavor_feature", "_UI_POCDMT000040Supply_type"), V3Package.eINSTANCE.getPOCDMT000040Supply_NullFlavor(), true, false, false, ItemPropertyDescriptor.GENERIC_VALUE_IMAGE, null, null));
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
            childrenFeatures.add(V3Package.eINSTANCE.getPOCDMT000040Supply_RealmCode());
            childrenFeatures.add(V3Package.eINSTANCE.getPOCDMT000040Supply_TypeId());
            childrenFeatures.add(V3Package.eINSTANCE.getPOCDMT000040Supply_TemplateId());
            childrenFeatures.add(V3Package.eINSTANCE.getPOCDMT000040Supply_Id());
            childrenFeatures.add(V3Package.eINSTANCE.getPOCDMT000040Supply_Code());
            childrenFeatures.add(V3Package.eINSTANCE.getPOCDMT000040Supply_Text());
            childrenFeatures.add(V3Package.eINSTANCE.getPOCDMT000040Supply_StatusCode());
            childrenFeatures.add(V3Package.eINSTANCE.getPOCDMT000040Supply_EffectiveTime());
            childrenFeatures.add(V3Package.eINSTANCE.getPOCDMT000040Supply_PriorityCode());
            childrenFeatures.add(V3Package.eINSTANCE.getPOCDMT000040Supply_RepeatNumber());
            childrenFeatures.add(V3Package.eINSTANCE.getPOCDMT000040Supply_IndependentInd());
            childrenFeatures.add(V3Package.eINSTANCE.getPOCDMT000040Supply_Quantity());
            childrenFeatures.add(V3Package.eINSTANCE.getPOCDMT000040Supply_ExpectedUseTime());
            childrenFeatures.add(V3Package.eINSTANCE.getPOCDMT000040Supply_Subject());
            childrenFeatures.add(V3Package.eINSTANCE.getPOCDMT000040Supply_Specimen());
            childrenFeatures.add(V3Package.eINSTANCE.getPOCDMT000040Supply_Product());
            childrenFeatures.add(V3Package.eINSTANCE.getPOCDMT000040Supply_Performer());
            childrenFeatures.add(V3Package.eINSTANCE.getPOCDMT000040Supply_Author());
            childrenFeatures.add(V3Package.eINSTANCE.getPOCDMT000040Supply_Informant());
            childrenFeatures.add(V3Package.eINSTANCE.getPOCDMT000040Supply_Participant());
            childrenFeatures.add(V3Package.eINSTANCE.getPOCDMT000040Supply_EntryRelationship());
            childrenFeatures.add(V3Package.eINSTANCE.getPOCDMT000040Supply_Reference());
            childrenFeatures.add(V3Package.eINSTANCE.getPOCDMT000040Supply_Precondition());
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
	 * This returns POCDMT000040Supply.gif.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    public Object getImage(Object object) {
        return overlayImage(object, getResourceLocator().getImage("full/obj16/POCDMT000040Supply"));
    }

    /**
	 * This returns the label text for the adapted class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    public String getText(Object object) {
        ActClassSupply labelValue = ((POCDMT000040Supply) object).getClassCode();
        String label = labelValue == null ? null : labelValue.toString();
        return label == null || label.length() == 0 ? getString("_UI_POCDMT000040Supply_type") : getString("_UI_POCDMT000040Supply_type") + " " + label;
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
        switch(notification.getFeatureID(POCDMT000040Supply.class)) {
            case V3Package.POCDMT000040_SUPPLY__CLASS_CODE:
            case V3Package.POCDMT000040_SUPPLY__MOOD_CODE:
            case V3Package.POCDMT000040_SUPPLY__NULL_FLAVOR:
                fireNotifyChanged(new ViewerNotification(notification, notification.getNotifier(), false, true));
                return;
            case V3Package.POCDMT000040_SUPPLY__REALM_CODE:
            case V3Package.POCDMT000040_SUPPLY__TYPE_ID:
            case V3Package.POCDMT000040_SUPPLY__TEMPLATE_ID:
            case V3Package.POCDMT000040_SUPPLY__ID:
            case V3Package.POCDMT000040_SUPPLY__CODE:
            case V3Package.POCDMT000040_SUPPLY__TEXT:
            case V3Package.POCDMT000040_SUPPLY__STATUS_CODE:
            case V3Package.POCDMT000040_SUPPLY__EFFECTIVE_TIME:
            case V3Package.POCDMT000040_SUPPLY__PRIORITY_CODE:
            case V3Package.POCDMT000040_SUPPLY__REPEAT_NUMBER:
            case V3Package.POCDMT000040_SUPPLY__INDEPENDENT_IND:
            case V3Package.POCDMT000040_SUPPLY__QUANTITY:
            case V3Package.POCDMT000040_SUPPLY__EXPECTED_USE_TIME:
            case V3Package.POCDMT000040_SUPPLY__SUBJECT:
            case V3Package.POCDMT000040_SUPPLY__SPECIMEN:
            case V3Package.POCDMT000040_SUPPLY__PRODUCT:
            case V3Package.POCDMT000040_SUPPLY__PERFORMER:
            case V3Package.POCDMT000040_SUPPLY__AUTHOR:
            case V3Package.POCDMT000040_SUPPLY__INFORMANT:
            case V3Package.POCDMT000040_SUPPLY__PARTICIPANT:
            case V3Package.POCDMT000040_SUPPLY__ENTRY_RELATIONSHIP:
            case V3Package.POCDMT000040_SUPPLY__REFERENCE:
            case V3Package.POCDMT000040_SUPPLY__PRECONDITION:
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
        newChildDescriptors.add(createChildParameter(V3Package.eINSTANCE.getPOCDMT000040Supply_RealmCode(), V3Factory.eINSTANCE.createCS1()));
        newChildDescriptors.add(createChildParameter(V3Package.eINSTANCE.getPOCDMT000040Supply_TypeId(), V3Factory.eINSTANCE.createPOCDMT000040InfrastructureRootTypeId()));
        newChildDescriptors.add(createChildParameter(V3Package.eINSTANCE.getPOCDMT000040Supply_TemplateId(), V3Factory.eINSTANCE.createII()));
        newChildDescriptors.add(createChildParameter(V3Package.eINSTANCE.getPOCDMT000040Supply_TemplateId(), V3Factory.eINSTANCE.createPOCDMT000040InfrastructureRootTypeId()));
        newChildDescriptors.add(createChildParameter(V3Package.eINSTANCE.getPOCDMT000040Supply_Id(), V3Factory.eINSTANCE.createII()));
        newChildDescriptors.add(createChildParameter(V3Package.eINSTANCE.getPOCDMT000040Supply_Id(), V3Factory.eINSTANCE.createPOCDMT000040InfrastructureRootTypeId()));
        newChildDescriptors.add(createChildParameter(V3Package.eINSTANCE.getPOCDMT000040Supply_Code(), V3Factory.eINSTANCE.createCD()));
        newChildDescriptors.add(createChildParameter(V3Package.eINSTANCE.getPOCDMT000040Supply_Code(), V3Factory.eINSTANCE.createBXITCD()));
        newChildDescriptors.add(createChildParameter(V3Package.eINSTANCE.getPOCDMT000040Supply_Code(), V3Factory.eINSTANCE.createCE()));
        newChildDescriptors.add(createChildParameter(V3Package.eINSTANCE.getPOCDMT000040Supply_Code(), V3Factory.eINSTANCE.createCV()));
        newChildDescriptors.add(createChildParameter(V3Package.eINSTANCE.getPOCDMT000040Supply_Code(), V3Factory.eINSTANCE.createCO()));
        newChildDescriptors.add(createChildParameter(V3Package.eINSTANCE.getPOCDMT000040Supply_Code(), V3Factory.eINSTANCE.createCS1()));
        newChildDescriptors.add(createChildParameter(V3Package.eINSTANCE.getPOCDMT000040Supply_Code(), V3Factory.eINSTANCE.createEIVLEvent()));
        newChildDescriptors.add(createChildParameter(V3Package.eINSTANCE.getPOCDMT000040Supply_Code(), V3Factory.eINSTANCE.createHXITCE()));
        newChildDescriptors.add(createChildParameter(V3Package.eINSTANCE.getPOCDMT000040Supply_Code(), V3Factory.eINSTANCE.createPQR()));
        newChildDescriptors.add(createChildParameter(V3Package.eINSTANCE.getPOCDMT000040Supply_Code(), V3Factory.eINSTANCE.createSXCMCD()));
        newChildDescriptors.add(createChildParameter(V3Package.eINSTANCE.getPOCDMT000040Supply_Text(), V3Factory.eINSTANCE.createED()));
        newChildDescriptors.add(createChildParameter(V3Package.eINSTANCE.getPOCDMT000040Supply_Text(), V3Factory.eINSTANCE.createST1()));
        newChildDescriptors.add(createChildParameter(V3Package.eINSTANCE.getPOCDMT000040Supply_Text(), V3Factory.eINSTANCE.createADXP()));
        newChildDescriptors.add(createChildParameter(V3Package.eINSTANCE.getPOCDMT000040Supply_Text(), V3Factory.eINSTANCE.createAdxpAdditionalLocator()));
        newChildDescriptors.add(createChildParameter(V3Package.eINSTANCE.getPOCDMT000040Supply_Text(), V3Factory.eINSTANCE.createAdxpBuildingNumberSuffix()));
        newChildDescriptors.add(createChildParameter(V3Package.eINSTANCE.getPOCDMT000040Supply_Text(), V3Factory.eINSTANCE.createAdxpCareOf()));
        newChildDescriptors.add(createChildParameter(V3Package.eINSTANCE.getPOCDMT000040Supply_Text(), V3Factory.eINSTANCE.createAdxpCensusTract()));
        newChildDescriptors.add(createChildParameter(V3Package.eINSTANCE.getPOCDMT000040Supply_Text(), V3Factory.eINSTANCE.createAdxpCity()));
        newChildDescriptors.add(createChildParameter(V3Package.eINSTANCE.getPOCDMT000040Supply_Text(), V3Factory.eINSTANCE.createAdxpCountry()));
        newChildDescriptors.add(createChildParameter(V3Package.eINSTANCE.getPOCDMT000040Supply_Text(), V3Factory.eINSTANCE.createAdxpCounty()));
        newChildDescriptors.add(createChildParameter(V3Package.eINSTANCE.getPOCDMT000040Supply_Text(), V3Factory.eINSTANCE.createAdxpDelimiter()));
        newChildDescriptors.add(createChildParameter(V3Package.eINSTANCE.getPOCDMT000040Supply_Text(), V3Factory.eINSTANCE.createAdxpDeliveryAddressLine()));
        newChildDescriptors.add(createChildParameter(V3Package.eINSTANCE.getPOCDMT000040Supply_Text(), V3Factory.eINSTANCE.createAdxpDeliveryInstallationArea()));
        newChildDescriptors.add(createChildParameter(V3Package.eINSTANCE.getPOCDMT000040Supply_Text(), V3Factory.eINSTANCE.createAdxpDeliveryInstallationQualifier()));
        newChildDescriptors.add(createChildParameter(V3Package.eINSTANCE.getPOCDMT000040Supply_Text(), V3Factory.eINSTANCE.createAdxpDeliveryInstallationType()));
        newChildDescriptors.add(createChildParameter(V3Package.eINSTANCE.getPOCDMT000040Supply_Text(), V3Factory.eINSTANCE.createAdxpDeliveryMode()));
        newChildDescriptors.add(createChildParameter(V3Package.eINSTANCE.getPOCDMT000040Supply_Text(), V3Factory.eINSTANCE.createAdxpDeliveryModeIdentifier()));
        newChildDescriptors.add(createChildParameter(V3Package.eINSTANCE.getPOCDMT000040Supply_Text(), V3Factory.eINSTANCE.createAdxpDirection()));
        newChildDescriptors.add(createChildParameter(V3Package.eINSTANCE.getPOCDMT000040Supply_Text(), V3Factory.eINSTANCE.createAdxpHouseNumber()));
        newChildDescriptors.add(createChildParameter(V3Package.eINSTANCE.getPOCDMT000040Supply_Text(), V3Factory.eINSTANCE.createAdxpHouseNumberNumeric()));
        newChildDescriptors.add(createChildParameter(V3Package.eINSTANCE.getPOCDMT000040Supply_Text(), V3Factory.eINSTANCE.createAdxpPostalCode()));
        newChildDescriptors.add(createChildParameter(V3Package.eINSTANCE.getPOCDMT000040Supply_Text(), V3Factory.eINSTANCE.createAdxpPostBox()));
        newChildDescriptors.add(createChildParameter(V3Package.eINSTANCE.getPOCDMT000040Supply_Text(), V3Factory.eINSTANCE.createAdxpPrecinct()));
        newChildDescriptors.add(createChildParameter(V3Package.eINSTANCE.getPOCDMT000040Supply_Text(), V3Factory.eINSTANCE.createAdxpState()));
        newChildDescriptors.add(createChildParameter(V3Package.eINSTANCE.getPOCDMT000040Supply_Text(), V3Factory.eINSTANCE.createAdxpStreetAddressLine()));
        newChildDescriptors.add(createChildParameter(V3Package.eINSTANCE.getPOCDMT000040Supply_Text(), V3Factory.eINSTANCE.createAdxpStreetName()));
        newChildDescriptors.add(createChildParameter(V3Package.eINSTANCE.getPOCDMT000040Supply_Text(), V3Factory.eINSTANCE.createAdxpStreetNameBase()));
        newChildDescriptors.add(createChildParameter(V3Package.eINSTANCE.getPOCDMT000040Supply_Text(), V3Factory.eINSTANCE.createAdxpStreetNameType()));
        newChildDescriptors.add(createChildParameter(V3Package.eINSTANCE.getPOCDMT000040Supply_Text(), V3Factory.eINSTANCE.createAdxpUnitID()));
        newChildDescriptors.add(createChildParameter(V3Package.eINSTANCE.getPOCDMT000040Supply_Text(), V3Factory.eINSTANCE.createAdxpUnitType()));
        newChildDescriptors.add(createChildParameter(V3Package.eINSTANCE.getPOCDMT000040Supply_Text(), V3Factory.eINSTANCE.createENXP()));
        newChildDescriptors.add(createChildParameter(V3Package.eINSTANCE.getPOCDMT000040Supply_Text(), V3Factory.eINSTANCE.createEnDelimiter()));
        newChildDescriptors.add(createChildParameter(V3Package.eINSTANCE.getPOCDMT000040Supply_Text(), V3Factory.eINSTANCE.createEnFamily()));
        newChildDescriptors.add(createChildParameter(V3Package.eINSTANCE.getPOCDMT000040Supply_Text(), V3Factory.eINSTANCE.createEnGiven()));
        newChildDescriptors.add(createChildParameter(V3Package.eINSTANCE.getPOCDMT000040Supply_Text(), V3Factory.eINSTANCE.createEnPrefix()));
        newChildDescriptors.add(createChildParameter(V3Package.eINSTANCE.getPOCDMT000040Supply_Text(), V3Factory.eINSTANCE.createEnSuffix()));
        newChildDescriptors.add(createChildParameter(V3Package.eINSTANCE.getPOCDMT000040Supply_Text(), V3Factory.eINSTANCE.createSC()));
        newChildDescriptors.add(createChildParameter(V3Package.eINSTANCE.getPOCDMT000040Supply_Text(), V3Factory.eINSTANCE.createThumbnail()));
        newChildDescriptors.add(createChildParameter(V3Package.eINSTANCE.getPOCDMT000040Supply_StatusCode(), V3Factory.eINSTANCE.createCS1()));
        newChildDescriptors.add(createChildParameter(V3Package.eINSTANCE.getPOCDMT000040Supply_EffectiveTime(), V3Factory.eINSTANCE.createSXCMTS()));
        newChildDescriptors.add(createChildParameter(V3Package.eINSTANCE.getPOCDMT000040Supply_EffectiveTime(), V3Factory.eINSTANCE.createEIVLTS()));
        newChildDescriptors.add(createChildParameter(V3Package.eINSTANCE.getPOCDMT000040Supply_EffectiveTime(), V3Factory.eINSTANCE.createIVLTS()));
        newChildDescriptors.add(createChildParameter(V3Package.eINSTANCE.getPOCDMT000040Supply_EffectiveTime(), V3Factory.eINSTANCE.createPIVLTS()));
        newChildDescriptors.add(createChildParameter(V3Package.eINSTANCE.getPOCDMT000040Supply_EffectiveTime(), V3Factory.eINSTANCE.createSXPRTS()));
        newChildDescriptors.add(createChildParameter(V3Package.eINSTANCE.getPOCDMT000040Supply_PriorityCode(), V3Factory.eINSTANCE.createCE()));
        newChildDescriptors.add(createChildParameter(V3Package.eINSTANCE.getPOCDMT000040Supply_PriorityCode(), V3Factory.eINSTANCE.createCV()));
        newChildDescriptors.add(createChildParameter(V3Package.eINSTANCE.getPOCDMT000040Supply_PriorityCode(), V3Factory.eINSTANCE.createCO()));
        newChildDescriptors.add(createChildParameter(V3Package.eINSTANCE.getPOCDMT000040Supply_PriorityCode(), V3Factory.eINSTANCE.createCS1()));
        newChildDescriptors.add(createChildParameter(V3Package.eINSTANCE.getPOCDMT000040Supply_PriorityCode(), V3Factory.eINSTANCE.createEIVLEvent()));
        newChildDescriptors.add(createChildParameter(V3Package.eINSTANCE.getPOCDMT000040Supply_PriorityCode(), V3Factory.eINSTANCE.createHXITCE()));
        newChildDescriptors.add(createChildParameter(V3Package.eINSTANCE.getPOCDMT000040Supply_PriorityCode(), V3Factory.eINSTANCE.createPQR()));
        newChildDescriptors.add(createChildParameter(V3Package.eINSTANCE.getPOCDMT000040Supply_RepeatNumber(), V3Factory.eINSTANCE.createIVLINT()));
        newChildDescriptors.add(createChildParameter(V3Package.eINSTANCE.getPOCDMT000040Supply_IndependentInd(), V3Factory.eINSTANCE.createBL1()));
        newChildDescriptors.add(createChildParameter(V3Package.eINSTANCE.getPOCDMT000040Supply_Quantity(), V3Factory.eINSTANCE.createPQ()));
        newChildDescriptors.add(createChildParameter(V3Package.eINSTANCE.getPOCDMT000040Supply_Quantity(), V3Factory.eINSTANCE.createSXCMPQ()));
        newChildDescriptors.add(createChildParameter(V3Package.eINSTANCE.getPOCDMT000040Supply_Quantity(), V3Factory.eINSTANCE.createIVLPQ()));
        newChildDescriptors.add(createChildParameter(V3Package.eINSTANCE.getPOCDMT000040Supply_Quantity(), V3Factory.eINSTANCE.createBXITIVLPQ()));
        newChildDescriptors.add(createChildParameter(V3Package.eINSTANCE.getPOCDMT000040Supply_Quantity(), V3Factory.eINSTANCE.createHXITPQ()));
        newChildDescriptors.add(createChildParameter(V3Package.eINSTANCE.getPOCDMT000040Supply_Quantity(), V3Factory.eINSTANCE.createPPDPQ()));
        newChildDescriptors.add(createChildParameter(V3Package.eINSTANCE.getPOCDMT000040Supply_Quantity(), V3Factory.eINSTANCE.createSXCMPPDPQ()));
        newChildDescriptors.add(createChildParameter(V3Package.eINSTANCE.getPOCDMT000040Supply_Quantity(), V3Factory.eINSTANCE.createIVLPPDPQ()));
        newChildDescriptors.add(createChildParameter(V3Package.eINSTANCE.getPOCDMT000040Supply_Quantity(), V3Factory.eINSTANCE.createIVXBPPDPQ()));
        newChildDescriptors.add(createChildParameter(V3Package.eINSTANCE.getPOCDMT000040Supply_Quantity(), V3Factory.eINSTANCE.createIVXBPQ()));
        newChildDescriptors.add(createChildParameter(V3Package.eINSTANCE.getPOCDMT000040Supply_ExpectedUseTime(), V3Factory.eINSTANCE.createIVLTS()));
        newChildDescriptors.add(createChildParameter(V3Package.eINSTANCE.getPOCDMT000040Supply_Subject(), V3Factory.eINSTANCE.createPOCDMT000040Subject()));
        newChildDescriptors.add(createChildParameter(V3Package.eINSTANCE.getPOCDMT000040Supply_Specimen(), V3Factory.eINSTANCE.createPOCDMT000040Specimen()));
        newChildDescriptors.add(createChildParameter(V3Package.eINSTANCE.getPOCDMT000040Supply_Product(), V3Factory.eINSTANCE.createPOCDMT000040Product()));
        newChildDescriptors.add(createChildParameter(V3Package.eINSTANCE.getPOCDMT000040Supply_Performer(), V3Factory.eINSTANCE.createPOCDMT000040Performer2()));
        newChildDescriptors.add(createChildParameter(V3Package.eINSTANCE.getPOCDMT000040Supply_Author(), V3Factory.eINSTANCE.createPOCDMT000040Author()));
        newChildDescriptors.add(createChildParameter(V3Package.eINSTANCE.getPOCDMT000040Supply_Informant(), V3Factory.eINSTANCE.createPOCDMT000040Informant12()));
        newChildDescriptors.add(createChildParameter(V3Package.eINSTANCE.getPOCDMT000040Supply_Participant(), V3Factory.eINSTANCE.createPOCDMT000040Participant2()));
        newChildDescriptors.add(createChildParameter(V3Package.eINSTANCE.getPOCDMT000040Supply_EntryRelationship(), V3Factory.eINSTANCE.createPOCDMT000040EntryRelationship()));
        newChildDescriptors.add(createChildParameter(V3Package.eINSTANCE.getPOCDMT000040Supply_Reference(), V3Factory.eINSTANCE.createPOCDMT000040Reference()));
        newChildDescriptors.add(createChildParameter(V3Package.eINSTANCE.getPOCDMT000040Supply_Precondition(), V3Factory.eINSTANCE.createPOCDMT000040Precondition()));
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
        boolean qualify = childFeature == V3Package.eINSTANCE.getPOCDMT000040Supply_RealmCode() || childFeature == V3Package.eINSTANCE.getPOCDMT000040Supply_Code() || childFeature == V3Package.eINSTANCE.getPOCDMT000040Supply_StatusCode() || childFeature == V3Package.eINSTANCE.getPOCDMT000040Supply_PriorityCode() || childFeature == V3Package.eINSTANCE.getPOCDMT000040Supply_TypeId() || childFeature == V3Package.eINSTANCE.getPOCDMT000040Supply_TemplateId() || childFeature == V3Package.eINSTANCE.getPOCDMT000040Supply_Id() || childFeature == V3Package.eINSTANCE.getPOCDMT000040Supply_EffectiveTime() || childFeature == V3Package.eINSTANCE.getPOCDMT000040Supply_ExpectedUseTime();
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
