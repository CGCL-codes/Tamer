package org.eclipse.uml2.uml.edit.providers;

import java.util.Collection;
import java.util.List;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edit.provider.ComposeableAdapterFactory;
import org.eclipse.emf.edit.provider.IEditingDomainItemProvider;
import org.eclipse.emf.edit.provider.IItemColorProvider;
import org.eclipse.emf.edit.provider.IItemFontProvider;
import org.eclipse.emf.edit.provider.IItemLabelProvider;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.IItemPropertySource;
import org.eclipse.emf.edit.provider.IStructuredItemContentProvider;
import org.eclipse.emf.edit.provider.ITreeItemContentProvider;
import org.eclipse.emf.edit.provider.ItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.ViewerNotification;
import org.eclipse.uml2.uml.StructuralFeature;
import org.eclipse.uml2.uml.UMLFactory;
import org.eclipse.uml2.uml.UMLPackage;

/**
 * This is the item provider adapter for a {@link org.eclipse.uml2.uml.StructuralFeature} object.
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 * @generated
 */
public class StructuralFeatureItemProvider extends FeatureItemProvider implements IEditingDomainItemProvider, IStructuredItemContentProvider, ITreeItemContentProvider, IItemLabelProvider, IItemPropertySource, IItemColorProvider, IItemFontProvider {

    /**
	 * This constructs an instance from a factory and a notifier.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public StructuralFeatureItemProvider(AdapterFactory adapterFactory) {
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
            addTypePropertyDescriptor(object);
            addIsOrderedPropertyDescriptor(object);
            addIsUniquePropertyDescriptor(object);
            addUpperPropertyDescriptor(object);
            addLowerPropertyDescriptor(object);
            addUpperValuePropertyDescriptor(object);
            addLowerValuePropertyDescriptor(object);
            addIsReadOnlyPropertyDescriptor(object);
        }
        return itemPropertyDescriptors;
    }

    /**
	 * This adds a property descriptor for the Type feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    protected void addTypePropertyDescriptor(Object object) {
        itemPropertyDescriptors.add(createItemPropertyDescriptor(((ComposeableAdapterFactory) adapterFactory).getRootAdapterFactory(), getResourceLocator(), getString("_UI_TypedElement_type_feature"), getString("_UI_PropertyDescriptor_description", "_UI_TypedElement_type_feature", "_UI_TypedElement_type"), UMLPackage.Literals.TYPED_ELEMENT__TYPE, true, false, true, null, null, null));
    }

    /**
	 * This adds a property descriptor for the Is Ordered feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    protected void addIsOrderedPropertyDescriptor(Object object) {
        itemPropertyDescriptors.add(createItemPropertyDescriptor(((ComposeableAdapterFactory) adapterFactory).getRootAdapterFactory(), getResourceLocator(), getString("_UI_MultiplicityElement_isOrdered_feature"), getString("_UI_PropertyDescriptor_description", "_UI_MultiplicityElement_isOrdered_feature", "_UI_MultiplicityElement_type"), UMLPackage.Literals.MULTIPLICITY_ELEMENT__IS_ORDERED, true, false, false, ItemPropertyDescriptor.BOOLEAN_VALUE_IMAGE, null, null));
    }

    /**
	 * This adds a property descriptor for the Is Unique feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    protected void addIsUniquePropertyDescriptor(Object object) {
        itemPropertyDescriptors.add(createItemPropertyDescriptor(((ComposeableAdapterFactory) adapterFactory).getRootAdapterFactory(), getResourceLocator(), getString("_UI_MultiplicityElement_isUnique_feature"), getString("_UI_PropertyDescriptor_description", "_UI_MultiplicityElement_isUnique_feature", "_UI_MultiplicityElement_type"), UMLPackage.Literals.MULTIPLICITY_ELEMENT__IS_UNIQUE, true, false, false, ItemPropertyDescriptor.BOOLEAN_VALUE_IMAGE, null, null));
    }

    /**
	 * This adds a property descriptor for the Upper feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    protected void addUpperPropertyDescriptor(Object object) {
        itemPropertyDescriptors.add(createItemPropertyDescriptor(((ComposeableAdapterFactory) adapterFactory).getRootAdapterFactory(), getResourceLocator(), getString("_UI_MultiplicityElement_upper_feature"), getString("_UI_PropertyDescriptor_description", "_UI_MultiplicityElement_upper_feature", "_UI_MultiplicityElement_type"), UMLPackage.Literals.MULTIPLICITY_ELEMENT__UPPER, true, false, false, ItemPropertyDescriptor.INTEGRAL_VALUE_IMAGE, null, null));
    }

    /**
	 * This adds a property descriptor for the Lower feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    protected void addLowerPropertyDescriptor(Object object) {
        itemPropertyDescriptors.add(createItemPropertyDescriptor(((ComposeableAdapterFactory) adapterFactory).getRootAdapterFactory(), getResourceLocator(), getString("_UI_MultiplicityElement_lower_feature"), getString("_UI_PropertyDescriptor_description", "_UI_MultiplicityElement_lower_feature", "_UI_MultiplicityElement_type"), UMLPackage.Literals.MULTIPLICITY_ELEMENT__LOWER, true, false, false, ItemPropertyDescriptor.INTEGRAL_VALUE_IMAGE, null, null));
    }

    /**
	 * This adds a property descriptor for the Upper Value feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    protected void addUpperValuePropertyDescriptor(Object object) {
        itemPropertyDescriptors.add(createItemPropertyDescriptor(((ComposeableAdapterFactory) adapterFactory).getRootAdapterFactory(), getResourceLocator(), getString("_UI_MultiplicityElement_upperValue_feature"), getString("_UI_PropertyDescriptor_description", "_UI_MultiplicityElement_upperValue_feature", "_UI_MultiplicityElement_type"), UMLPackage.Literals.MULTIPLICITY_ELEMENT__UPPER_VALUE, true, false, true, null, null, new String[] { "org.eclipse.ui.views.properties.expert" }));
    }

    /**
	 * This adds a property descriptor for the Lower Value feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    protected void addLowerValuePropertyDescriptor(Object object) {
        itemPropertyDescriptors.add(createItemPropertyDescriptor(((ComposeableAdapterFactory) adapterFactory).getRootAdapterFactory(), getResourceLocator(), getString("_UI_MultiplicityElement_lowerValue_feature"), getString("_UI_PropertyDescriptor_description", "_UI_MultiplicityElement_lowerValue_feature", "_UI_MultiplicityElement_type"), UMLPackage.Literals.MULTIPLICITY_ELEMENT__LOWER_VALUE, true, false, true, null, null, new String[] { "org.eclipse.ui.views.properties.expert" }));
    }

    /**
	 * This adds a property descriptor for the Is Read Only feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    protected void addIsReadOnlyPropertyDescriptor(Object object) {
        itemPropertyDescriptors.add(createItemPropertyDescriptor(((ComposeableAdapterFactory) adapterFactory).getRootAdapterFactory(), getResourceLocator(), getString("_UI_StructuralFeature_isReadOnly_feature"), getString("_UI_PropertyDescriptor_description", "_UI_StructuralFeature_isReadOnly_feature", "_UI_StructuralFeature_type"), UMLPackage.Literals.STRUCTURAL_FEATURE__IS_READ_ONLY, true, false, false, ItemPropertyDescriptor.BOOLEAN_VALUE_IMAGE, null, null));
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
            childrenFeatures.add(UMLPackage.Literals.MULTIPLICITY_ELEMENT__UPPER_VALUE);
            childrenFeatures.add(UMLPackage.Literals.MULTIPLICITY_ELEMENT__LOWER_VALUE);
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
        String label = ((StructuralFeature) object).getName();
        return label == null || label.length() == 0 ? getString("_UI_StructuralFeature_type") : getString("_UI_StructuralFeature_type") + " " + label;
    }

    /**
	 * This handles model notifications by calling {@link #updateChildren} to update any cached
	 * children and by creating a viewer notification, which it passes to {@link #fireNotifyChanged}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void notifyChangedGen(Notification notification) {
        updateChildren(notification);
        switch(notification.getFeatureID(StructuralFeature.class)) {
            case UMLPackage.STRUCTURAL_FEATURE__TYPE:
            case UMLPackage.STRUCTURAL_FEATURE__IS_ORDERED:
            case UMLPackage.STRUCTURAL_FEATURE__IS_UNIQUE:
            case UMLPackage.STRUCTURAL_FEATURE__UPPER:
            case UMLPackage.STRUCTURAL_FEATURE__LOWER:
            case UMLPackage.STRUCTURAL_FEATURE__IS_READ_ONLY:
                fireNotifyChanged(new ViewerNotification(notification, notification.getNotifier(), false, true));
                return;
            case UMLPackage.STRUCTURAL_FEATURE__UPPER_VALUE:
            case UMLPackage.STRUCTURAL_FEATURE__LOWER_VALUE:
                fireNotifyChanged(new ViewerNotification(notification, notification.getNotifier(), true, false));
                return;
        }
        super.notifyChanged(notification);
    }

    @Override
    public void notifyChanged(Notification notification) {
        switch(notification.getFeatureID(StructuralFeature.class)) {
            case UMLPackage.STRUCTURAL_FEATURE__UPPER_VALUE:
            case UMLPackage.STRUCTURAL_FEATURE__LOWER_VALUE:
                updateChildren(notification);
                fireNotifyChanged(new ViewerNotification(notification, notification.getNotifier(), true, true));
                return;
        }
        notifyChangedGen(notification);
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
        newChildDescriptors.add(createChildParameter(UMLPackage.Literals.MULTIPLICITY_ELEMENT__UPPER_VALUE, UMLFactory.eINSTANCE.createOpaqueExpression()));
        newChildDescriptors.add(createChildParameter(UMLPackage.Literals.MULTIPLICITY_ELEMENT__UPPER_VALUE, UMLFactory.eINSTANCE.createExpression()));
        newChildDescriptors.add(createChildParameter(UMLPackage.Literals.MULTIPLICITY_ELEMENT__UPPER_VALUE, UMLFactory.eINSTANCE.createStringExpression()));
        newChildDescriptors.add(createChildParameter(UMLPackage.Literals.MULTIPLICITY_ELEMENT__UPPER_VALUE, UMLFactory.eINSTANCE.createLiteralInteger()));
        newChildDescriptors.add(createChildParameter(UMLPackage.Literals.MULTIPLICITY_ELEMENT__UPPER_VALUE, UMLFactory.eINSTANCE.createLiteralString()));
        newChildDescriptors.add(createChildParameter(UMLPackage.Literals.MULTIPLICITY_ELEMENT__UPPER_VALUE, UMLFactory.eINSTANCE.createLiteralBoolean()));
        newChildDescriptors.add(createChildParameter(UMLPackage.Literals.MULTIPLICITY_ELEMENT__UPPER_VALUE, UMLFactory.eINSTANCE.createLiteralNull()));
        newChildDescriptors.add(createChildParameter(UMLPackage.Literals.MULTIPLICITY_ELEMENT__UPPER_VALUE, UMLFactory.eINSTANCE.createInstanceValue()));
        newChildDescriptors.add(createChildParameter(UMLPackage.Literals.MULTIPLICITY_ELEMENT__UPPER_VALUE, UMLFactory.eINSTANCE.createLiteralUnlimitedNatural()));
        newChildDescriptors.add(createChildParameter(UMLPackage.Literals.MULTIPLICITY_ELEMENT__UPPER_VALUE, UMLFactory.eINSTANCE.createTimeExpression()));
        newChildDescriptors.add(createChildParameter(UMLPackage.Literals.MULTIPLICITY_ELEMENT__UPPER_VALUE, UMLFactory.eINSTANCE.createDuration()));
        newChildDescriptors.add(createChildParameter(UMLPackage.Literals.MULTIPLICITY_ELEMENT__UPPER_VALUE, UMLFactory.eINSTANCE.createInterval()));
        newChildDescriptors.add(createChildParameter(UMLPackage.Literals.MULTIPLICITY_ELEMENT__UPPER_VALUE, UMLFactory.eINSTANCE.createDurationInterval()));
        newChildDescriptors.add(createChildParameter(UMLPackage.Literals.MULTIPLICITY_ELEMENT__UPPER_VALUE, UMLFactory.eINSTANCE.createTimeInterval()));
        newChildDescriptors.add(createChildParameter(UMLPackage.Literals.MULTIPLICITY_ELEMENT__LOWER_VALUE, UMLFactory.eINSTANCE.createOpaqueExpression()));
        newChildDescriptors.add(createChildParameter(UMLPackage.Literals.MULTIPLICITY_ELEMENT__LOWER_VALUE, UMLFactory.eINSTANCE.createExpression()));
        newChildDescriptors.add(createChildParameter(UMLPackage.Literals.MULTIPLICITY_ELEMENT__LOWER_VALUE, UMLFactory.eINSTANCE.createStringExpression()));
        newChildDescriptors.add(createChildParameter(UMLPackage.Literals.MULTIPLICITY_ELEMENT__LOWER_VALUE, UMLFactory.eINSTANCE.createLiteralInteger()));
        newChildDescriptors.add(createChildParameter(UMLPackage.Literals.MULTIPLICITY_ELEMENT__LOWER_VALUE, UMLFactory.eINSTANCE.createLiteralString()));
        newChildDescriptors.add(createChildParameter(UMLPackage.Literals.MULTIPLICITY_ELEMENT__LOWER_VALUE, UMLFactory.eINSTANCE.createLiteralBoolean()));
        newChildDescriptors.add(createChildParameter(UMLPackage.Literals.MULTIPLICITY_ELEMENT__LOWER_VALUE, UMLFactory.eINSTANCE.createLiteralNull()));
        newChildDescriptors.add(createChildParameter(UMLPackage.Literals.MULTIPLICITY_ELEMENT__LOWER_VALUE, UMLFactory.eINSTANCE.createInstanceValue()));
        newChildDescriptors.add(createChildParameter(UMLPackage.Literals.MULTIPLICITY_ELEMENT__LOWER_VALUE, UMLFactory.eINSTANCE.createLiteralUnlimitedNatural()));
        newChildDescriptors.add(createChildParameter(UMLPackage.Literals.MULTIPLICITY_ELEMENT__LOWER_VALUE, UMLFactory.eINSTANCE.createTimeExpression()));
        newChildDescriptors.add(createChildParameter(UMLPackage.Literals.MULTIPLICITY_ELEMENT__LOWER_VALUE, UMLFactory.eINSTANCE.createDuration()));
        newChildDescriptors.add(createChildParameter(UMLPackage.Literals.MULTIPLICITY_ELEMENT__LOWER_VALUE, UMLFactory.eINSTANCE.createInterval()));
        newChildDescriptors.add(createChildParameter(UMLPackage.Literals.MULTIPLICITY_ELEMENT__LOWER_VALUE, UMLFactory.eINSTANCE.createDurationInterval()));
        newChildDescriptors.add(createChildParameter(UMLPackage.Literals.MULTIPLICITY_ELEMENT__LOWER_VALUE, UMLFactory.eINSTANCE.createTimeInterval()));
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
        boolean qualify = childFeature == UMLPackage.Literals.NAMED_ELEMENT__NAME_EXPRESSION || childFeature == UMLPackage.Literals.MULTIPLICITY_ELEMENT__UPPER_VALUE || childFeature == UMLPackage.Literals.MULTIPLICITY_ELEMENT__LOWER_VALUE;
        if (qualify) {
            return getString("_UI_CreateChild_text2", new Object[] { getTypeText(childObject), getFeatureText(childFeature), getTypeText(owner) });
        }
        return super.getCreateChildText(owner, feature, child, selection);
    }
}
