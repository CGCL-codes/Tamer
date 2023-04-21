package org.eclipse.uml2.uml.edit.providers;

import java.util.Collection;
import java.util.List;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edit.domain.EditingDomain;
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
import org.eclipse.uml2.common.edit.command.SubsetAddCommand;
import org.eclipse.uml2.common.edit.command.SubsetSupersetReplaceCommand;
import org.eclipse.uml2.common.edit.command.SupersetRemoveCommand;
import org.eclipse.uml2.common.util.UML2Util;
import org.eclipse.uml2.uml.AssociationClass;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.Type;
import org.eclipse.uml2.uml.UMLFactory;
import org.eclipse.uml2.uml.UMLPackage;

/**
 * This is the item provider adapter for a {@link org.eclipse.uml2.uml.AssociationClass} object.
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 * @generated
 */
public class AssociationClassItemProvider extends ClassItemProvider implements IEditingDomainItemProvider, IStructuredItemContentProvider, ITreeItemContentProvider, IItemLabelProvider, IItemPropertySource, IItemColorProvider, IItemFontProvider {

    /**
	 * This constructs an instance from a factory and a notifier.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public AssociationClassItemProvider(AdapterFactory adapterFactory) {
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
            addRelatedElementPropertyDescriptor(object);
            addOwnedEndPropertyDescriptor(object);
            addMemberEndPropertyDescriptor(object);
            addIsDerivedPropertyDescriptor(object);
            addEndTypePropertyDescriptor(object);
            addNavigableOwnedEndPropertyDescriptor(object);
        }
        return itemPropertyDescriptors;
    }

    /**
	 * This adds a property descriptor for the Related Element feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    protected void addRelatedElementPropertyDescriptor(Object object) {
        itemPropertyDescriptors.add(createItemPropertyDescriptor(((ComposeableAdapterFactory) adapterFactory).getRootAdapterFactory(), getResourceLocator(), getString("_UI_Relationship_relatedElement_feature"), getString("_UI_PropertyDescriptor_description", "_UI_Relationship_relatedElement_feature", "_UI_Relationship_type"), UMLPackage.Literals.RELATIONSHIP__RELATED_ELEMENT, false, false, false, null, null, new String[] { "org.eclipse.ui.views.properties.expert" }));
    }

    /**
	 * This adds a property descriptor for the Is Derived feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    protected void addIsDerivedPropertyDescriptor(Object object) {
        itemPropertyDescriptors.add(createItemPropertyDescriptor(((ComposeableAdapterFactory) adapterFactory).getRootAdapterFactory(), getResourceLocator(), getString("_UI_Association_isDerived_feature"), getString("_UI_PropertyDescriptor_description", "_UI_Association_isDerived_feature", "_UI_Association_type"), UMLPackage.Literals.ASSOCIATION__IS_DERIVED, true, false, false, ItemPropertyDescriptor.BOOLEAN_VALUE_IMAGE, null, null));
    }

    /**
	 * This adds a property descriptor for the End Type feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    protected void addEndTypePropertyDescriptor(Object object) {
        itemPropertyDescriptors.add(createItemPropertyDescriptor(((ComposeableAdapterFactory) adapterFactory).getRootAdapterFactory(), getResourceLocator(), getString("_UI_Association_endType_feature"), getString("_UI_PropertyDescriptor_description", "_UI_Association_endType_feature", "_UI_Association_type"), UMLPackage.Literals.ASSOCIATION__END_TYPE, false, false, false, null, null, new String[] { "org.eclipse.ui.views.properties.expert" }));
    }

    /**
	 * This adds a property descriptor for the Member End feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    protected void addMemberEndPropertyDescriptor(Object object) {
        itemPropertyDescriptors.add(createItemPropertyDescriptor(((ComposeableAdapterFactory) adapterFactory).getRootAdapterFactory(), getResourceLocator(), getString("_UI_Association_memberEnd_feature"), getString("_UI_PropertyDescriptor_description", "_UI_Association_memberEnd_feature", "_UI_Association_type"), UMLPackage.Literals.ASSOCIATION__MEMBER_END, true, false, true, null, null, null));
    }

    /**
	 * This adds a property descriptor for the Navigable Owned End feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    protected void addNavigableOwnedEndPropertyDescriptor(Object object) {
        itemPropertyDescriptors.add(createItemPropertyDescriptor(((ComposeableAdapterFactory) adapterFactory).getRootAdapterFactory(), getResourceLocator(), getString("_UI_Association_navigableOwnedEnd_feature"), getString("_UI_PropertyDescriptor_description", "_UI_Association_navigableOwnedEnd_feature", "_UI_Association_type"), UMLPackage.Literals.ASSOCIATION__NAVIGABLE_OWNED_END, true, false, true, null, null, null));
    }

    /**
	 * This adds a property descriptor for the Owned End feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    protected void addOwnedEndPropertyDescriptor(Object object) {
        itemPropertyDescriptors.add(createItemPropertyDescriptor(((ComposeableAdapterFactory) adapterFactory).getRootAdapterFactory(), getResourceLocator(), getString("_UI_Association_ownedEnd_feature"), getString("_UI_PropertyDescriptor_description", "_UI_Association_ownedEnd_feature", "_UI_Association_type"), UMLPackage.Literals.ASSOCIATION__OWNED_END, true, false, true, null, null, new String[] { "org.eclipse.ui.views.properties.expert" }));
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
            childrenFeatures.add(UMLPackage.Literals.ASSOCIATION__OWNED_END);
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
	 * This returns AssociationClass.gif.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    public Object getImage(Object object) {
        return overlayImage(object, getResourceLocator().getImage("full/obj16/AssociationClass"));
    }

    /**
	 * This returns the label text for the adapted class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
    @Override
    public String getText(Object object) {
        StringBuffer text = appendType(appendKeywords(new StringBuffer(), object), "_UI_AssociationClass_type");
        AssociationClass associationClass = (AssociationClass) object;
        if (associationClass.isDerived()) {
            appendString(text, "/");
        }
        String label = associationClass.getLabel(shouldTranslate());
        if (!UML2Util.isEmpty(label)) {
            appendString(text, label);
        } else {
            EList<Property> memberEnds = associationClass.getMemberEnds();
            if (!memberEnds.isEmpty()) {
                appendString(text, "A");
                for (Property memberEnd : memberEnds) {
                    String memberEndName = memberEnd.getName();
                    text.append('_');
                    if (!UML2Util.isEmpty(memberEndName)) {
                        text.append(memberEndName);
                    } else {
                        Type type = memberEnd.getType();
                        if (type != null) {
                            String typeName = type.getName();
                            if (!UML2Util.isEmpty(typeName)) {
                                memberEndName = Character.toLowerCase(typeName.charAt(0)) + typeName.substring(1);
                            }
                        }
                        if (!UML2Util.isEmpty(memberEndName)) {
                            text.append(memberEndName);
                        } else {
                            text.append('<');
                            text.append(getTypeText(memberEnd));
                            text.append('>');
                        }
                    }
                }
            }
        }
        return text.toString();
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
        switch(notification.getFeatureID(AssociationClass.class)) {
            case UMLPackage.ASSOCIATION_CLASS__MEMBER_END:
            case UMLPackage.ASSOCIATION_CLASS__IS_DERIVED:
            case UMLPackage.ASSOCIATION_CLASS__NAVIGABLE_OWNED_END:
                fireNotifyChanged(new ViewerNotification(notification, notification.getNotifier(), false, true));
                return;
            case UMLPackage.ASSOCIATION_CLASS__OWNED_END:
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
        newChildDescriptors.add(createChildParameter(UMLPackage.Literals.ASSOCIATION__OWNED_END, UMLFactory.eINSTANCE.createProperty()));
        newChildDescriptors.add(createChildParameter(UMLPackage.Literals.ASSOCIATION__OWNED_END, UMLFactory.eINSTANCE.createPort()));
        newChildDescriptors.add(createChildParameter(UMLPackage.Literals.ASSOCIATION__OWNED_END, UMLFactory.eINSTANCE.createExtensionEnd()));
        newChildDescriptors.add(createChildParameter(UMLPackage.Literals.ASSOCIATION__NAVIGABLE_OWNED_END, UMLFactory.eINSTANCE.createProperty()));
        newChildDescriptors.add(createChildParameter(UMLPackage.Literals.ASSOCIATION__NAVIGABLE_OWNED_END, UMLFactory.eINSTANCE.createPort()));
        newChildDescriptors.add(createChildParameter(UMLPackage.Literals.ASSOCIATION__NAVIGABLE_OWNED_END, UMLFactory.eINSTANCE.createExtensionEnd()));
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
        boolean qualify = childFeature == UMLPackage.Literals.CLASSIFIER__REPRESENTATION || childFeature == UMLPackage.Literals.CLASSIFIER__COLLABORATION_USE || childFeature == UMLPackage.Literals.CLASSIFIER__OWNED_USE_CASE || childFeature == UMLPackage.Literals.CLASS__NESTED_CLASSIFIER || childFeature == UMLPackage.Literals.STRUCTURED_CLASSIFIER__OWNED_ATTRIBUTE || childFeature == UMLPackage.Literals.ASSOCIATION__OWNED_END || childFeature == UMLPackage.Literals.ASSOCIATION__NAVIGABLE_OWNED_END || childFeature == UMLPackage.Literals.ENCAPSULATED_CLASSIFIER__OWNED_PORT || childFeature == UMLPackage.Literals.BEHAVIORED_CLASSIFIER__OWNED_BEHAVIOR || childFeature == UMLPackage.Literals.BEHAVIORED_CLASSIFIER__CLASSIFIER_BEHAVIOR;
        if (qualify) {
            return getString("_UI_CreateChild_text2", new Object[] { getTypeText(childObject), getFeatureText(childFeature), getTypeText(owner) });
        }
        return super.getCreateChildText(owner, feature, child, selection);
    }

    /**
	 * @see org.eclipse.emf.edit.provider.ItemProviderAdapter#createAddCommand(org.eclipse.emf.edit.domain.EditingDomain, org.eclipse.emf.ecore.EObject, org.eclipse.emf.ecore.EStructuralFeature, java.util.Collection, int)
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    protected Command createAddCommand(EditingDomain domain, EObject owner, EStructuralFeature feature, Collection<?> collection, int index) {
        if (feature == UMLPackage.Literals.ASSOCIATION__OWNED_END) {
            return new SubsetAddCommand(domain, owner, feature, new EStructuralFeature[] { UMLPackage.Literals.ASSOCIATION__MEMBER_END }, collection, index);
        }
        if (feature == UMLPackage.Literals.ASSOCIATION__NAVIGABLE_OWNED_END) {
            return new SubsetAddCommand(domain, owner, feature, new EStructuralFeature[] { UMLPackage.Literals.ASSOCIATION__OWNED_END }, collection, index);
        }
        return super.createAddCommand(domain, owner, feature, collection, index);
    }

    /**
	 * @see org.eclipse.emf.edit.provider.ItemProviderAdapter#createRemoveCommand(org.eclipse.emf.edit.domain.EditingDomain, org.eclipse.emf.ecore.EObject, org.eclipse.emf.ecore.EStructuralFeature, java.util.Collection)
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    protected Command createRemoveCommand(EditingDomain domain, EObject owner, EStructuralFeature feature, Collection<?> collection) {
        if (feature == UMLPackage.Literals.ASSOCIATION__OWNED_END) {
            return new SupersetRemoveCommand(domain, owner, feature, new EStructuralFeature[] { UMLPackage.Literals.ASSOCIATION__NAVIGABLE_OWNED_END }, collection);
        }
        if (feature == UMLPackage.Literals.ASSOCIATION__MEMBER_END) {
            return new SupersetRemoveCommand(domain, owner, feature, new EStructuralFeature[] { UMLPackage.Literals.ASSOCIATION__OWNED_END }, collection);
        }
        return super.createRemoveCommand(domain, owner, feature, collection);
    }

    /**
	 * @see org.eclipse.emf.edit.provider.ItemProviderAdapter#createReplaceCommand(org.eclipse.emf.edit.domain.EditingDomain, org.eclipse.emf.ecore.EObject, org.eclipse.emf.ecore.EStructuralFeature, org.eclipse.emf.ecore.EObject, java.util.Collection)
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    protected Command createReplaceCommand(EditingDomain domain, EObject owner, EStructuralFeature feature, EObject value, Collection<?> collection) {
        if (feature == UMLPackage.Literals.ASSOCIATION__OWNED_END) {
            return new SubsetSupersetReplaceCommand(domain, owner, feature, new EStructuralFeature[] { UMLPackage.Literals.ASSOCIATION__MEMBER_END }, new EStructuralFeature[] { UMLPackage.Literals.ASSOCIATION__NAVIGABLE_OWNED_END }, value, collection);
        }
        if (feature == UMLPackage.Literals.ASSOCIATION__NAVIGABLE_OWNED_END) {
            return new SubsetSupersetReplaceCommand(domain, owner, feature, new EStructuralFeature[] { UMLPackage.Literals.ASSOCIATION__OWNED_END }, null, value, collection);
        }
        if (feature == UMLPackage.Literals.ASSOCIATION__MEMBER_END) {
            return new SubsetSupersetReplaceCommand(domain, owner, feature, null, new EStructuralFeature[] { UMLPackage.Literals.ASSOCIATION__OWNED_END }, value, collection);
        }
        return super.createReplaceCommand(domain, owner, feature, value, collection);
    }

    @Override
    public Object getForeground(Object object) {
        for (Property memberEnd : ((AssociationClass) object).getMemberEnds()) {
            if (memberEnd.eIsProxy()) {
                return IItemColorProvider.GRAYED_OUT_COLOR;
            }
        }
        return super.getForeground(object);
    }
}
