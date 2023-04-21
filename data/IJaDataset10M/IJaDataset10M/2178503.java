package org.modelversioning.operations.detection.operationoccurrence.provider;

import java.util.Collection;
import java.util.List;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.ResourceLocator;
import org.eclipse.emf.compare.diff.metamodel.DiffPackage;
import org.eclipse.emf.compare.diff.provider.DiffElementItemProvider;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edit.provider.ComposeableAdapterFactory;
import org.eclipse.emf.edit.provider.IEditingDomainItemProvider;
import org.eclipse.emf.edit.provider.IItemLabelProvider;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.IItemPropertySource;
import org.eclipse.emf.edit.provider.IStructuredItemContentProvider;
import org.eclipse.emf.edit.provider.ITreeItemContentProvider;
import org.eclipse.emf.edit.provider.ItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.ViewerNotification;
import org.modelversioning.core.conditions.templatebindings.TemplatebindingsFactory;
import org.modelversioning.operations.detection.operationoccurrence.OperationOccurrence;
import org.modelversioning.operations.detection.operationoccurrence.OperationoccurrenceFactory;
import org.modelversioning.operations.detection.operationoccurrence.OperationoccurrencePackage;

/**
 * This is the item provider adapter for a
 * {@link org.modelversioning.operations.detection.operationoccurrence.OperationOccurrence}
 * object. <!-- begin-user-doc --> <!-- end-user-doc -->
 * 
 * @generated
 */
public class OperationOccurrenceItemProvider extends DiffElementItemProvider implements IEditingDomainItemProvider, IStructuredItemContentProvider, ITreeItemContentProvider, IItemLabelProvider, IItemPropertySource {

    /**
	 * This constructs an instance from a factory and a notifier. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
    public OperationOccurrenceItemProvider(AdapterFactory adapterFactory) {
        super(adapterFactory);
    }

    /**
	 * This returns the property descriptors for the adapted class. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
    @Override
    public List<IItemPropertyDescriptor> getPropertyDescriptors(Object object) {
        if (itemPropertyDescriptors == null) {
            super.getPropertyDescriptors(object);
            addHideElementsPropertyDescriptor(object);
            addIsCollapsedPropertyDescriptor(object);
            addAppliedOperationIdPropertyDescriptor(object);
            addTitlePropertyDescriptor(object);
            addAppliedOperationNamePropertyDescriptor(object);
            addAppliedOperationPropertyDescriptor(object);
            addHiddenChangesPropertyDescriptor(object);
        }
        return itemPropertyDescriptors;
    }

    /**
	 * This adds a property descriptor for the Hide Elements feature. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
    protected void addHideElementsPropertyDescriptor(Object object) {
        itemPropertyDescriptors.add(createItemPropertyDescriptor(((ComposeableAdapterFactory) adapterFactory).getRootAdapterFactory(), getResourceLocator(), getString("_UI_AbstractDiffExtension_hideElements_feature"), getString("_UI_PropertyDescriptor_description", "_UI_AbstractDiffExtension_hideElements_feature", "_UI_AbstractDiffExtension_type"), DiffPackage.Literals.ABSTRACT_DIFF_EXTENSION__HIDE_ELEMENTS, true, false, true, null, null, null));
    }

    /**
	 * This adds a property descriptor for the Is Collapsed feature. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
    protected void addIsCollapsedPropertyDescriptor(Object object) {
        itemPropertyDescriptors.add(createItemPropertyDescriptor(((ComposeableAdapterFactory) adapterFactory).getRootAdapterFactory(), getResourceLocator(), getString("_UI_AbstractDiffExtension_isCollapsed_feature"), getString("_UI_PropertyDescriptor_description", "_UI_AbstractDiffExtension_isCollapsed_feature", "_UI_AbstractDiffExtension_type"), DiffPackage.Literals.ABSTRACT_DIFF_EXTENSION__IS_COLLAPSED, true, false, false, ItemPropertyDescriptor.BOOLEAN_VALUE_IMAGE, null, null));
    }

    /**
	 * This adds a property descriptor for the Applied Operation Id feature.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
    protected void addAppliedOperationIdPropertyDescriptor(Object object) {
        itemPropertyDescriptors.add(createItemPropertyDescriptor(((ComposeableAdapterFactory) adapterFactory).getRootAdapterFactory(), getResourceLocator(), getString("_UI_OperationOccurrence_appliedOperationId_feature"), getString("_UI_PropertyDescriptor_description", "_UI_OperationOccurrence_appliedOperationId_feature", "_UI_OperationOccurrence_type"), OperationoccurrencePackage.Literals.OPERATION_OCCURRENCE__APPLIED_OPERATION_ID, true, false, false, ItemPropertyDescriptor.GENERIC_VALUE_IMAGE, null, null));
    }

    /**
	 * This adds a property descriptor for the Title feature. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
    protected void addTitlePropertyDescriptor(Object object) {
        itemPropertyDescriptors.add(createItemPropertyDescriptor(((ComposeableAdapterFactory) adapterFactory).getRootAdapterFactory(), getResourceLocator(), getString("_UI_OperationOccurrence_title_feature"), getString("_UI_PropertyDescriptor_description", "_UI_OperationOccurrence_title_feature", "_UI_OperationOccurrence_type"), OperationoccurrencePackage.Literals.OPERATION_OCCURRENCE__TITLE, true, false, false, ItemPropertyDescriptor.GENERIC_VALUE_IMAGE, null, null));
    }

    /**
	 * This adds a property descriptor for the Applied Operation Name feature.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
    protected void addAppliedOperationNamePropertyDescriptor(Object object) {
        itemPropertyDescriptors.add(createItemPropertyDescriptor(((ComposeableAdapterFactory) adapterFactory).getRootAdapterFactory(), getResourceLocator(), getString("_UI_OperationOccurrence_appliedOperationName_feature"), getString("_UI_PropertyDescriptor_description", "_UI_OperationOccurrence_appliedOperationName_feature", "_UI_OperationOccurrence_type"), OperationoccurrencePackage.Literals.OPERATION_OCCURRENCE__APPLIED_OPERATION_NAME, true, false, false, ItemPropertyDescriptor.GENERIC_VALUE_IMAGE, null, null));
    }

    /**
	 * This adds a property descriptor for the Applied Operation feature. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
    protected void addAppliedOperationPropertyDescriptor(Object object) {
        itemPropertyDescriptors.add(createItemPropertyDescriptor(((ComposeableAdapterFactory) adapterFactory).getRootAdapterFactory(), getResourceLocator(), getString("_UI_OperationOccurrence_appliedOperation_feature"), getString("_UI_PropertyDescriptor_description", "_UI_OperationOccurrence_appliedOperation_feature", "_UI_OperationOccurrence_type"), OperationoccurrencePackage.Literals.OPERATION_OCCURRENCE__APPLIED_OPERATION, true, false, true, null, null, null));
    }

    /**
	 * This adds a property descriptor for the Hidden Changes feature. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
    protected void addHiddenChangesPropertyDescriptor(Object object) {
        itemPropertyDescriptors.add(createItemPropertyDescriptor(((ComposeableAdapterFactory) adapterFactory).getRootAdapterFactory(), getResourceLocator(), getString("_UI_OperationOccurrence_hiddenChanges_feature"), getString("_UI_PropertyDescriptor_description", "_UI_OperationOccurrence_hiddenChanges_feature", "_UI_OperationOccurrence_type"), OperationoccurrencePackage.Literals.OPERATION_OCCURRENCE__HIDDEN_CHANGES, true, false, true, null, null, null));
    }

    /**
	 * This specifies how to implement {@link #getChildren} and is used to
	 * deduce an appropriate feature for an
	 * {@link org.eclipse.emf.edit.command.AddCommand},
	 * {@link org.eclipse.emf.edit.command.RemoveCommand} or
	 * {@link org.eclipse.emf.edit.command.MoveCommand} in
	 * {@link #createCommand}. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
    @Override
    public Collection<? extends EStructuralFeature> getChildrenFeatures(Object object) {
        if (childrenFeatures == null) {
            super.getChildrenFeatures(object);
            childrenFeatures.add(OperationoccurrencePackage.Literals.OPERATION_OCCURRENCE__PRE_CONDITION_BINDING);
            childrenFeatures.add(OperationoccurrencePackage.Literals.OPERATION_OCCURRENCE__POST_CONDITION_BINDING);
        }
        return childrenFeatures;
    }

    /**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
    @Override
    protected EStructuralFeature getChildFeature(Object object, Object child) {
        return super.getChildFeature(object, child);
    }

    /**
	 * This returns OperationOccurrence.gif. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 */
    @Override
    public Object getImage(Object object) {
        return overlayImage(object, getResourceLocator().getImage("full/obj16/OperationOccurrence"));
    }

    /**
	 * This returns the label text for the adapted class. <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
    @Override
    public String getText(Object object) {
        String label = ((OperationOccurrence) object).getTitle();
        return label == null || label.length() == 0 ? getString("_UI_OperationOccurrence_type") + "\"" + ((OperationOccurrence) object).getAppliedOperationName() + "\"" : label;
    }

    /**
	 * This handles model notifications by calling {@link #updateChildren} to
	 * update any cached children and by creating a viewer notification, which
	 * it passes to {@link #fireNotifyChanged}. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 */
    @Override
    public void notifyChanged(Notification notification) {
        updateChildren(notification);
        switch(notification.getFeatureID(OperationOccurrence.class)) {
            case OperationoccurrencePackage.OPERATION_OCCURRENCE__IS_COLLAPSED:
            case OperationoccurrencePackage.OPERATION_OCCURRENCE__APPLIED_OPERATION_ID:
            case OperationoccurrencePackage.OPERATION_OCCURRENCE__TITLE:
            case OperationoccurrencePackage.OPERATION_OCCURRENCE__APPLIED_OPERATION_NAME:
                fireNotifyChanged(new ViewerNotification(notification, notification.getNotifier(), false, true));
                return;
            case OperationoccurrencePackage.OPERATION_OCCURRENCE__PRE_CONDITION_BINDING:
            case OperationoccurrencePackage.OPERATION_OCCURRENCE__POST_CONDITION_BINDING:
                fireNotifyChanged(new ViewerNotification(notification, notification.getNotifier(), true, false));
                return;
        }
        super.notifyChanged(notification);
    }

    /**
	 * This adds {@link org.eclipse.emf.edit.command.CommandParameter}s
	 * describing the children that can be created under this object. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
    @Override
    protected void collectNewChildDescriptors(Collection<Object> newChildDescriptors, Object object) {
        super.collectNewChildDescriptors(newChildDescriptors, object);
        newChildDescriptors.add(createChildParameter(DiffPackage.Literals.DIFF_ELEMENT__SUB_DIFF_ELEMENTS, OperationoccurrenceFactory.eINSTANCE.createOperationOccurrence()));
        newChildDescriptors.add(createChildParameter(OperationoccurrencePackage.Literals.OPERATION_OCCURRENCE__PRE_CONDITION_BINDING, TemplatebindingsFactory.eINSTANCE.createTemplateBindingCollection()));
        newChildDescriptors.add(createChildParameter(OperationoccurrencePackage.Literals.OPERATION_OCCURRENCE__POST_CONDITION_BINDING, TemplatebindingsFactory.eINSTANCE.createTemplateBindingCollection()));
    }

    /**
	 * This returns the label text for
	 * {@link org.eclipse.emf.edit.command.CreateChildCommand}. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
    @Override
    public String getCreateChildText(Object owner, Object feature, Object child, Collection<?> selection) {
        Object childFeature = feature;
        Object childObject = child;
        boolean qualify = childFeature == OperationoccurrencePackage.Literals.OPERATION_OCCURRENCE__PRE_CONDITION_BINDING || childFeature == OperationoccurrencePackage.Literals.OPERATION_OCCURRENCE__POST_CONDITION_BINDING;
        if (qualify) {
            return getString("_UI_CreateChild_text2", new Object[] { getTypeText(childObject), getFeatureText(childFeature), getTypeText(owner) });
        }
        return super.getCreateChildText(owner, feature, child, selection);
    }

    /**
	 * Return the resource locator for this item provider's resources. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
    @Override
    public ResourceLocator getResourceLocator() {
        return OperationOccurrenceEditPlugin.INSTANCE;
    }
}
