package net.taylor.uml2.uml.edit.providers;

import java.util.Collection;
import java.util.List;
import net.taylor.uml2.uml.edit.UMLEditPlugin;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.ResourceLocator;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.edit.provider.ComposeableAdapterFactory;
import org.eclipse.emf.edit.provider.IEditingDomainItemProvider;
import org.eclipse.emf.edit.provider.IItemLabelProvider;
import org.eclipse.emf.edit.provider.IItemPropertySource;
import org.eclipse.emf.edit.provider.IStructuredItemContentProvider;
import org.eclipse.emf.edit.provider.ITreeItemContentProvider;
import org.eclipse.emf.edit.provider.ItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.ViewerNotification;
import org.eclipse.uml2.common.edit.command.SubsetAddCommand;
import org.eclipse.uml2.common.edit.command.SubsetSupersetReplaceCommand;
import org.eclipse.uml2.common.edit.command.SubsetSupersetSetCommand;
import org.eclipse.uml2.common.edit.command.SupersetRemoveCommand;
import org.eclipse.uml2.uml.Classifier;
import org.eclipse.uml2.uml.UMLPackage;

/**
 * This is the item provider adapter for a {@link org.eclipse.uml2.uml.Classifier} object.
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 * @generated
 */
public class ClassifierItemProvider extends NamespaceItemProvider implements IEditingDomainItemProvider, IStructuredItemContentProvider, ITreeItemContentProvider, IItemLabelProvider, IItemPropertySource {

    /**
	 * This constructs an instance from a factory and a notifier.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public ClassifierItemProvider(AdapterFactory adapterFactory) {
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
            addIsLeafPropertyDescriptor(object);
            addRedefinedElementPropertyDescriptor(object);
            addRedefinitionContextPropertyDescriptor(object);
            addOwningTemplateParameterPropertyDescriptor(object);
            addTemplateParameterPropertyDescriptor(object);
            addPackagePropertyDescriptor(object);
            addTemplateBindingPropertyDescriptor(object);
            addOwnedTemplateSignaturePropertyDescriptor(object);
            addIsAbstractPropertyDescriptor(object);
            addGeneralizationPropertyDescriptor(object);
            addPowertypeExtentPropertyDescriptor(object);
            addFeaturePropertyDescriptor(object);
            addInheritedMemberPropertyDescriptor(object);
            addRedefinedClassifierPropertyDescriptor(object);
            addGeneralPropertyDescriptor(object);
            addSubstitutionPropertyDescriptor(object);
            addAttributePropertyDescriptor(object);
            addRepresentationPropertyDescriptor(object);
            addCollaborationUsePropertyDescriptor(object);
            addOwnedUseCasePropertyDescriptor(object);
            addUseCasePropertyDescriptor(object);
        }
        return itemPropertyDescriptors;
    }

    /**
	 * This adds a property descriptor for the Is Leaf feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    protected void addIsLeafPropertyDescriptor(Object object) {
        itemPropertyDescriptors.add(createItemPropertyDescriptor(((ComposeableAdapterFactory) adapterFactory).getRootAdapterFactory(), getResourceLocator(), getString("_UI_RedefinableElement_isLeaf_feature"), getString("_UI_PropertyDescriptor_description", "_UI_RedefinableElement_isLeaf_feature", "_UI_RedefinableElement_type"), UMLPackage.Literals.REDEFINABLE_ELEMENT__IS_LEAF, true, ItemPropertyDescriptor.BOOLEAN_VALUE_IMAGE, null, null));
    }

    /**
	 * This adds a property descriptor for the Redefined Element feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    protected void addRedefinedElementPropertyDescriptor(Object object) {
        itemPropertyDescriptors.add(createItemPropertyDescriptor(((ComposeableAdapterFactory) adapterFactory).getRootAdapterFactory(), getResourceLocator(), getString("_UI_RedefinableElement_redefinedElement_feature"), getString("_UI_PropertyDescriptor_description", "_UI_RedefinableElement_redefinedElement_feature", "_UI_RedefinableElement_type"), UMLPackage.Literals.REDEFINABLE_ELEMENT__REDEFINED_ELEMENT, false, null, null, new String[] { "org.eclipse.ui.views.properties.expert" }));
    }

    /**
	 * This adds a property descriptor for the Redefinition Context feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    protected void addRedefinitionContextPropertyDescriptor(Object object) {
        itemPropertyDescriptors.add(createItemPropertyDescriptor(((ComposeableAdapterFactory) adapterFactory).getRootAdapterFactory(), getResourceLocator(), getString("_UI_RedefinableElement_redefinitionContext_feature"), getString("_UI_PropertyDescriptor_description", "_UI_RedefinableElement_redefinitionContext_feature", "_UI_RedefinableElement_type"), UMLPackage.Literals.REDEFINABLE_ELEMENT__REDEFINITION_CONTEXT, false, null, null, new String[] { "org.eclipse.ui.views.properties.expert" }));
    }

    /**
	 * This adds a property descriptor for the Owning Template Parameter feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    protected void addOwningTemplateParameterPropertyDescriptor(Object object) {
        itemPropertyDescriptors.add(createItemPropertyDescriptor(((ComposeableAdapterFactory) adapterFactory).getRootAdapterFactory(), getResourceLocator(), getString("_UI_ParameterableElement_owningTemplateParameter_feature"), getString("_UI_PropertyDescriptor_description", "_UI_ParameterableElement_owningTemplateParameter_feature", "_UI_ParameterableElement_type"), UMLPackage.Literals.PARAMETERABLE_ELEMENT__OWNING_TEMPLATE_PARAMETER, true, null, null, new String[] { "org.eclipse.ui.views.properties.expert" }));
    }

    /**
	 * This adds a property descriptor for the Template Parameter feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    protected void addTemplateParameterPropertyDescriptor(Object object) {
        itemPropertyDescriptors.add(createItemPropertyDescriptor(((ComposeableAdapterFactory) adapterFactory).getRootAdapterFactory(), getResourceLocator(), getString("_UI_ParameterableElement_templateParameter_feature"), getString("_UI_PropertyDescriptor_description", "_UI_ParameterableElement_templateParameter_feature", "_UI_ParameterableElement_type"), UMLPackage.Literals.PARAMETERABLE_ELEMENT__TEMPLATE_PARAMETER, true, null, null, null));
    }

    /**
	 * This adds a property descriptor for the Package feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    protected void addPackagePropertyDescriptor(Object object) {
        itemPropertyDescriptors.add(createItemPropertyDescriptor(((ComposeableAdapterFactory) adapterFactory).getRootAdapterFactory(), getResourceLocator(), getString("_UI_Type_package_feature"), getString("_UI_PropertyDescriptor_description", "_UI_Type_package_feature", "_UI_Type_type"), UMLPackage.Literals.TYPE__PACKAGE, true, null, null, new String[] { "org.eclipse.ui.views.properties.expert" }));
    }

    /**
	 * This adds a property descriptor for the Template Binding feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    protected void addTemplateBindingPropertyDescriptor(Object object) {
        itemPropertyDescriptors.add(createItemPropertyDescriptor(((ComposeableAdapterFactory) adapterFactory).getRootAdapterFactory(), getResourceLocator(), getString("_UI_TemplateableElement_templateBinding_feature"), getString("_UI_PropertyDescriptor_description", "_UI_TemplateableElement_templateBinding_feature", "_UI_TemplateableElement_type"), UMLPackage.Literals.TEMPLATEABLE_ELEMENT__TEMPLATE_BINDING, true, null, null, new String[] { "org.eclipse.ui.views.properties.expert" }));
    }

    /**
	 * This adds a property descriptor for the Owned Template Signature feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    protected void addOwnedTemplateSignaturePropertyDescriptor(Object object) {
        itemPropertyDescriptors.add(createItemPropertyDescriptor(((ComposeableAdapterFactory) adapterFactory).getRootAdapterFactory(), getResourceLocator(), getString("_UI_TemplateableElement_ownedTemplateSignature_feature"), getString("_UI_PropertyDescriptor_description", "_UI_TemplateableElement_ownedTemplateSignature_feature", "_UI_TemplateableElement_type"), UMLPackage.Literals.TEMPLATEABLE_ELEMENT__OWNED_TEMPLATE_SIGNATURE, true, null, null, new String[] { "org.eclipse.ui.views.properties.expert" }));
    }

    /**
	 * This adds a property descriptor for the Is Abstract feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    protected void addIsAbstractPropertyDescriptor(Object object) {
        itemPropertyDescriptors.add(createItemPropertyDescriptor(((ComposeableAdapterFactory) adapterFactory).getRootAdapterFactory(), getResourceLocator(), getString("_UI_Classifier_isAbstract_feature"), getString("_UI_PropertyDescriptor_description", "_UI_Classifier_isAbstract_feature", "_UI_Classifier_type"), UMLPackage.Literals.CLASSIFIER__IS_ABSTRACT, true, ItemPropertyDescriptor.BOOLEAN_VALUE_IMAGE, null, null));
    }

    /**
	 * This adds a property descriptor for the Generalization feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    protected void addGeneralizationPropertyDescriptor(Object object) {
        itemPropertyDescriptors.add(createItemPropertyDescriptor(((ComposeableAdapterFactory) adapterFactory).getRootAdapterFactory(), getResourceLocator(), getString("_UI_Classifier_generalization_feature"), getString("_UI_PropertyDescriptor_description", "_UI_Classifier_generalization_feature", "_UI_Classifier_type"), UMLPackage.Literals.CLASSIFIER__GENERALIZATION, true, null, null, new String[] { "org.eclipse.ui.views.properties.expert" }));
    }

    /**
	 * This adds a property descriptor for the Powertype Extent feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    protected void addPowertypeExtentPropertyDescriptor(Object object) {
        itemPropertyDescriptors.add(createItemPropertyDescriptor(((ComposeableAdapterFactory) adapterFactory).getRootAdapterFactory(), getResourceLocator(), getString("_UI_Classifier_powertypeExtent_feature"), getString("_UI_PropertyDescriptor_description", "_UI_Classifier_powertypeExtent_feature", "_UI_Classifier_type"), UMLPackage.Literals.CLASSIFIER__POWERTYPE_EXTENT, true, null, null, null));
    }

    /**
	 * This adds a property descriptor for the Feature feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    protected void addFeaturePropertyDescriptor(Object object) {
        itemPropertyDescriptors.add(createItemPropertyDescriptor(((ComposeableAdapterFactory) adapterFactory).getRootAdapterFactory(), getResourceLocator(), getString("_UI_Classifier_feature_feature"), getString("_UI_PropertyDescriptor_description", "_UI_Classifier_feature_feature", "_UI_Classifier_type"), UMLPackage.Literals.CLASSIFIER__FEATURE, false, null, null, new String[] { "org.eclipse.ui.views.properties.expert" }));
    }

    /**
	 * This adds a property descriptor for the Inherited Member feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    protected void addInheritedMemberPropertyDescriptor(Object object) {
        itemPropertyDescriptors.add(createItemPropertyDescriptor(((ComposeableAdapterFactory) adapterFactory).getRootAdapterFactory(), getResourceLocator(), getString("_UI_Classifier_inheritedMember_feature"), getString("_UI_PropertyDescriptor_description", "_UI_Classifier_inheritedMember_feature", "_UI_Classifier_type"), UMLPackage.Literals.CLASSIFIER__INHERITED_MEMBER, true, null, null, new String[] { "org.eclipse.ui.views.properties.expert" }));
    }

    /**
	 * This adds a property descriptor for the Redefined Classifier feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    protected void addRedefinedClassifierPropertyDescriptor(Object object) {
        itemPropertyDescriptors.add(createItemPropertyDescriptor(((ComposeableAdapterFactory) adapterFactory).getRootAdapterFactory(), getResourceLocator(), getString("_UI_Classifier_redefinedClassifier_feature"), getString("_UI_PropertyDescriptor_description", "_UI_Classifier_redefinedClassifier_feature", "_UI_Classifier_type"), UMLPackage.Literals.CLASSIFIER__REDEFINED_CLASSIFIER, true, null, null, null));
    }

    /**
	 * This adds a property descriptor for the General feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    protected void addGeneralPropertyDescriptor(Object object) {
        itemPropertyDescriptors.add(createItemPropertyDescriptor(((ComposeableAdapterFactory) adapterFactory).getRootAdapterFactory(), getResourceLocator(), getString("_UI_Classifier_general_feature"), getString("_UI_PropertyDescriptor_description", "_UI_Classifier_general_feature", "_UI_Classifier_type"), UMLPackage.Literals.CLASSIFIER__GENERAL, true, null, null, new String[] { "org.eclipse.ui.views.properties.expert" }));
    }

    /**
	 * This adds a property descriptor for the Substitution feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    protected void addSubstitutionPropertyDescriptor(Object object) {
        itemPropertyDescriptors.add(createItemPropertyDescriptor(((ComposeableAdapterFactory) adapterFactory).getRootAdapterFactory(), getResourceLocator(), getString("_UI_Classifier_substitution_feature"), getString("_UI_PropertyDescriptor_description", "_UI_Classifier_substitution_feature", "_UI_Classifier_type"), UMLPackage.Literals.CLASSIFIER__SUBSTITUTION, true, null, null, new String[] { "org.eclipse.ui.views.properties.expert" }));
    }

    /**
	 * This adds a property descriptor for the Attribute feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    protected void addAttributePropertyDescriptor(Object object) {
        itemPropertyDescriptors.add(createItemPropertyDescriptor(((ComposeableAdapterFactory) adapterFactory).getRootAdapterFactory(), getResourceLocator(), getString("_UI_Classifier_attribute_feature"), getString("_UI_PropertyDescriptor_description", "_UI_Classifier_attribute_feature", "_UI_Classifier_type"), UMLPackage.Literals.CLASSIFIER__ATTRIBUTE, false, null, null, new String[] { "org.eclipse.ui.views.properties.expert" }));
    }

    /**
	 * This adds a property descriptor for the Representation feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    protected void addRepresentationPropertyDescriptor(Object object) {
        itemPropertyDescriptors.add(createItemPropertyDescriptor(((ComposeableAdapterFactory) adapterFactory).getRootAdapterFactory(), getResourceLocator(), getString("_UI_Classifier_representation_feature"), getString("_UI_PropertyDescriptor_description", "_UI_Classifier_representation_feature", "_UI_Classifier_type"), UMLPackage.Literals.CLASSIFIER__REPRESENTATION, true, null, null, null));
    }

    /**
	 * This adds a property descriptor for the Collaboration Use feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    protected void addCollaborationUsePropertyDescriptor(Object object) {
        itemPropertyDescriptors.add(createItemPropertyDescriptor(((ComposeableAdapterFactory) adapterFactory).getRootAdapterFactory(), getResourceLocator(), getString("_UI_Classifier_collaborationUse_feature"), getString("_UI_PropertyDescriptor_description", "_UI_Classifier_collaborationUse_feature", "_UI_Classifier_type"), UMLPackage.Literals.CLASSIFIER__COLLABORATION_USE, true, null, null, new String[] { "org.eclipse.ui.views.properties.expert" }));
    }

    /**
	 * This adds a property descriptor for the Owned Use Case feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    protected void addOwnedUseCasePropertyDescriptor(Object object) {
        itemPropertyDescriptors.add(createItemPropertyDescriptor(((ComposeableAdapterFactory) adapterFactory).getRootAdapterFactory(), getResourceLocator(), getString("_UI_Classifier_ownedUseCase_feature"), getString("_UI_PropertyDescriptor_description", "_UI_Classifier_ownedUseCase_feature", "_UI_Classifier_type"), UMLPackage.Literals.CLASSIFIER__OWNED_USE_CASE, true, null, null, new String[] { "org.eclipse.ui.views.properties.expert" }));
    }

    /**
	 * This adds a property descriptor for the Use Case feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    protected void addUseCasePropertyDescriptor(Object object) {
        itemPropertyDescriptors.add(createItemPropertyDescriptor(((ComposeableAdapterFactory) adapterFactory).getRootAdapterFactory(), getResourceLocator(), getString("_UI_Classifier_useCase_feature"), getString("_UI_PropertyDescriptor_description", "_UI_Classifier_useCase_feature", "_UI_Classifier_type"), UMLPackage.Literals.CLASSIFIER__USE_CASE, true, null, null, null));
    }

    /**
	 * This specifies how to implement {@link #getChildren} and is used to deduce an appropriate feature for an
	 * {@link org.eclipse.emf.edit.command.AddCommand}, {@link org.eclipse.emf.edit.command.RemoveCommand} or
	 * {@link org.eclipse.emf.edit.command.MoveCommand} in {@link #createCommand}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public Collection getChildrenFeatures(Object object) {
        if (childrenFeatures == null) {
            super.getChildrenFeatures(object);
            childrenFeatures.add(UMLPackage.Literals.TEMPLATEABLE_ELEMENT__TEMPLATE_BINDING);
            childrenFeatures.add(UMLPackage.Literals.TEMPLATEABLE_ELEMENT__OWNED_TEMPLATE_SIGNATURE);
            childrenFeatures.add(UMLPackage.Literals.CLASSIFIER__GENERALIZATION);
            childrenFeatures.add(UMLPackage.Literals.CLASSIFIER__SUBSTITUTION);
            childrenFeatures.add(UMLPackage.Literals.CLASSIFIER__COLLABORATION_USE);
            childrenFeatures.add(UMLPackage.Literals.CLASSIFIER__OWNED_USE_CASE);
        }
        return childrenFeatures;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    protected EStructuralFeature getChildFeature(Object object, Object child) {
        return super.getChildFeature(object, child);
    }

    /**
	 * This returns the label text for the adapted class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public String getText(Object object) {
        String label = ((Classifier) object).getName();
        return label == null || label.length() == 0 ? getString("_UI_Classifier_type") : getString("_UI_Classifier_type") + " " + label;
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
        switch(notification.getFeatureID(Classifier.class)) {
            case UMLPackage.CLASSIFIER__IS_LEAF:
            case UMLPackage.CLASSIFIER__IS_ABSTRACT:
                fireNotifyChanged(new ViewerNotification(notification, notification.getNotifier(), false, true));
                return;
            case UMLPackage.CLASSIFIER__TEMPLATE_BINDING:
            case UMLPackage.CLASSIFIER__OWNED_TEMPLATE_SIGNATURE:
            case UMLPackage.CLASSIFIER__GENERALIZATION:
            case UMLPackage.CLASSIFIER__SUBSTITUTION:
            case UMLPackage.CLASSIFIER__COLLABORATION_USE:
            case UMLPackage.CLASSIFIER__OWNED_USE_CASE:
                fireNotifyChanged(new ViewerNotification(notification, notification.getNotifier(), true, false));
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
        return UMLEditPlugin.INSTANCE;
    }

    /**
	 * @see org.eclipse.emf.edit.provider.ItemProviderAdapter#createAddCommand(org.eclipse.emf.edit.domain.EditingDomain, org.eclipse.emf.ecore.EObject, org.eclipse.emf.ecore.EStructuralFeature, java.util.Collection, int)
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    protected Command createAddCommand(EditingDomain domain, EObject owner, EStructuralFeature feature, Collection collection, int index) {
        if (feature == UMLPackage.Literals.CLASSIFIER__SUBSTITUTION) {
            return new SubsetAddCommand(domain, owner, feature, new EStructuralFeature[] { UMLPackage.Literals.NAMED_ELEMENT__CLIENT_DEPENDENCY }, collection, index);
        }
        return super.createAddCommand(domain, owner, feature, collection, index);
    }

    /**
	 * @see org.eclipse.emf.edit.provider.ItemProviderAdapter#createRemoveCommand(org.eclipse.emf.edit.domain.EditingDomain, org.eclipse.emf.ecore.EObject, org.eclipse.emf.ecore.EStructuralFeature, java.util.Collection)
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    protected Command createRemoveCommand(EditingDomain domain, EObject owner, EStructuralFeature feature, Collection collection) {
        if (feature == UMLPackage.Literals.NAMED_ELEMENT__CLIENT_DEPENDENCY) {
            return new SupersetRemoveCommand(domain, owner, feature, new EStructuralFeature[] { UMLPackage.Literals.CLASSIFIER__SUBSTITUTION }, collection);
        }
        if (feature == UMLPackage.Literals.CLASSIFIER__COLLABORATION_USE) {
            return new SupersetRemoveCommand(domain, owner, feature, new EStructuralFeature[] { UMLPackage.Literals.CLASSIFIER__REPRESENTATION }, collection);
        }
        return super.createRemoveCommand(domain, owner, feature, collection);
    }

    /**
	 * @see org.eclipse.emf.edit.provider.ItemProviderAdapter#createReplaceCommand(org.eclipse.emf.edit.domain.EditingDomain, org.eclipse.emf.ecore.EObject, org.eclipse.emf.ecore.EStructuralFeature, org.eclipse.emf.ecore.EObject, java.util.Collection)
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    protected Command createReplaceCommand(EditingDomain domain, EObject owner, EStructuralFeature feature, EObject value, Collection collection) {
        if (feature == UMLPackage.Literals.CLASSIFIER__SUBSTITUTION) {
            return new SubsetSupersetReplaceCommand(domain, owner, feature, new EStructuralFeature[] { UMLPackage.Literals.NAMED_ELEMENT__CLIENT_DEPENDENCY }, null, value, collection);
        }
        if (feature == UMLPackage.Literals.NAMED_ELEMENT__CLIENT_DEPENDENCY) {
            return new SubsetSupersetReplaceCommand(domain, owner, feature, null, new EStructuralFeature[] { UMLPackage.Literals.CLASSIFIER__SUBSTITUTION }, value, collection);
        }
        if (feature == UMLPackage.Literals.CLASSIFIER__COLLABORATION_USE) {
            return new SubsetSupersetReplaceCommand(domain, owner, feature, null, new EStructuralFeature[] { UMLPackage.Literals.CLASSIFIER__REPRESENTATION }, value, collection);
        }
        return super.createReplaceCommand(domain, owner, feature, value, collection);
    }

    /**
	 * @see org.eclipse.emf.edit.provider.ItemProviderAdapter#createSetCommand(org.eclipse.emf.edit.domain.EditingDomain, org.eclipse.emf.ecore.EObject, org.eclipse.emf.ecore.EStructuralFeature, java.lang.Object)
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    protected Command createSetCommand(EditingDomain domain, EObject owner, EStructuralFeature feature, Object value) {
        if (feature == UMLPackage.Literals.PARAMETERABLE_ELEMENT__OWNING_TEMPLATE_PARAMETER) {
            return new SubsetSupersetSetCommand(domain, owner, feature, new EStructuralFeature[] { UMLPackage.Literals.PARAMETERABLE_ELEMENT__TEMPLATE_PARAMETER }, null, value);
        }
        if (feature == UMLPackage.Literals.CLASSIFIER__REPRESENTATION) {
            return new SubsetSupersetSetCommand(domain, owner, feature, new EStructuralFeature[] { UMLPackage.Literals.CLASSIFIER__COLLABORATION_USE }, null, value);
        }
        return super.createSetCommand(domain, owner, feature, value);
    }
}
