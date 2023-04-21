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
import org.eclipse.emf.edit.provider.ViewerNotification;
import org.eclipse.uml2.uml.UMLFactory;
import org.eclipse.uml2.uml.UMLPackage;
import org.eclipse.uml2.uml.ValuePin;

/**
 * This is the item provider adapter for a {@link org.eclipse.uml2.uml.ValuePin} object.
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 * @generated
 */
public class ValuePinItemProvider extends InputPinItemProvider implements IEditingDomainItemProvider, IStructuredItemContentProvider, ITreeItemContentProvider, IItemLabelProvider, IItemPropertySource, IItemColorProvider, IItemFontProvider {

    /**
	 * This constructs an instance from a factory and a notifier.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public ValuePinItemProvider(AdapterFactory adapterFactory) {
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
            addValuePropertyDescriptor(object);
        }
        return itemPropertyDescriptors;
    }

    /**
	 * This adds a property descriptor for the Value feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    protected void addValuePropertyDescriptor(Object object) {
        itemPropertyDescriptors.add(createItemPropertyDescriptor(((ComposeableAdapterFactory) adapterFactory).getRootAdapterFactory(), getResourceLocator(), getString("_UI_ValuePin_value_feature"), getString("_UI_PropertyDescriptor_description", "_UI_ValuePin_value_feature", "_UI_ValuePin_type"), UMLPackage.Literals.VALUE_PIN__VALUE, true, false, true, null, null, new String[] { "org.eclipse.ui.views.properties.expert" }));
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
            childrenFeatures.add(UMLPackage.Literals.VALUE_PIN__VALUE);
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
	 * This returns ValuePin.gif.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    public Object getImage(Object object) {
        return overlayImage(object, getResourceLocator().getImage("full/obj16/ValuePin"));
    }

    /**
	 * This returns the label text for the adapted class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
    @Override
    public String getText(Object object) {
        return MultiplicityElementItemProvider.appendMultiplicity(appendLabel(appendType(appendKeywords(new StringBuffer(), object), "_UI_ValuePin_type"), object), object).toString();
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
        switch(notification.getFeatureID(ValuePin.class)) {
            case UMLPackage.VALUE_PIN__VALUE:
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
        newChildDescriptors.add(createChildParameter(UMLPackage.Literals.VALUE_PIN__VALUE, UMLFactory.eINSTANCE.createOpaqueExpression()));
        newChildDescriptors.add(createChildParameter(UMLPackage.Literals.VALUE_PIN__VALUE, UMLFactory.eINSTANCE.createExpression()));
        newChildDescriptors.add(createChildParameter(UMLPackage.Literals.VALUE_PIN__VALUE, UMLFactory.eINSTANCE.createStringExpression()));
        newChildDescriptors.add(createChildParameter(UMLPackage.Literals.VALUE_PIN__VALUE, UMLFactory.eINSTANCE.createLiteralInteger()));
        newChildDescriptors.add(createChildParameter(UMLPackage.Literals.VALUE_PIN__VALUE, UMLFactory.eINSTANCE.createLiteralString()));
        newChildDescriptors.add(createChildParameter(UMLPackage.Literals.VALUE_PIN__VALUE, UMLFactory.eINSTANCE.createLiteralBoolean()));
        newChildDescriptors.add(createChildParameter(UMLPackage.Literals.VALUE_PIN__VALUE, UMLFactory.eINSTANCE.createLiteralNull()));
        newChildDescriptors.add(createChildParameter(UMLPackage.Literals.VALUE_PIN__VALUE, UMLFactory.eINSTANCE.createInstanceValue()));
        newChildDescriptors.add(createChildParameter(UMLPackage.Literals.VALUE_PIN__VALUE, UMLFactory.eINSTANCE.createLiteralUnlimitedNatural()));
        newChildDescriptors.add(createChildParameter(UMLPackage.Literals.VALUE_PIN__VALUE, UMLFactory.eINSTANCE.createTimeExpression()));
        newChildDescriptors.add(createChildParameter(UMLPackage.Literals.VALUE_PIN__VALUE, UMLFactory.eINSTANCE.createDuration()));
        newChildDescriptors.add(createChildParameter(UMLPackage.Literals.VALUE_PIN__VALUE, UMLFactory.eINSTANCE.createInterval()));
        newChildDescriptors.add(createChildParameter(UMLPackage.Literals.VALUE_PIN__VALUE, UMLFactory.eINSTANCE.createDurationInterval()));
        newChildDescriptors.add(createChildParameter(UMLPackage.Literals.VALUE_PIN__VALUE, UMLFactory.eINSTANCE.createTimeInterval()));
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
        boolean qualify = childFeature == UMLPackage.Literals.NAMED_ELEMENT__NAME_EXPRESSION || childFeature == UMLPackage.Literals.OBJECT_NODE__UPPER_BOUND || childFeature == UMLPackage.Literals.MULTIPLICITY_ELEMENT__UPPER_VALUE || childFeature == UMLPackage.Literals.MULTIPLICITY_ELEMENT__LOWER_VALUE || childFeature == UMLPackage.Literals.VALUE_PIN__VALUE;
        if (qualify) {
            return getString("_UI_CreateChild_text2", new Object[] { getTypeText(childObject), getFeatureText(childFeature), getTypeText(owner) });
        }
        return super.getCreateChildText(owner, feature, child, selection);
    }
}
