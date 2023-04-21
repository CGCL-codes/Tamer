package com.safi.core.saflet.provider;

import com.safi.core.CorePackage;
import com.safi.core.actionstep.ActionStepFactory;
import com.safi.core.initiator.InitiatorFactory;
import com.safi.core.provider.CoreEditPluginProv;
import com.safi.core.provider.ThreadSensitiveItemProvider;
import com.safi.core.saflet.Saflet;
import com.safi.core.saflet.SafletFactory;
import com.safi.core.saflet.SafletPackage;
import java.util.Collection;
import java.util.List;
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

/**
 * This is the item provider adapter for a {@link com.safi.core.saflet.Saflet} object.
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 * @generated
 */
public class SafletItemProvider extends ThreadSensitiveItemProvider implements IEditingDomainItemProvider, IStructuredItemContentProvider, ITreeItemContentProvider, IItemLabelProvider, IItemPropertySource {

    /**
	 * This constructs an instance from a factory and a notifier.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 */
    public SafletItemProvider(AdapterFactory adapterFactory) {
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
            addPlatformIDPropertyDescriptor(object);
            addPlatformDependantPropertyDescriptor(object);
            addNamePropertyDescriptor(object);
            addVersionPropertyDescriptor(object);
            addDescriptionPropertyDescriptor(object);
            addFinallyPropertyDescriptor(object);
        }
        return itemPropertyDescriptors;
    }

    /**
	 * This adds a property descriptor for the Platform ID feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    protected void addPlatformIDPropertyDescriptor(Object object) {
        itemPropertyDescriptors.add(createItemPropertyDescriptor(((ComposeableAdapterFactory) adapterFactory).getRootAdapterFactory(), getResourceLocator(), getString("_UI_PlatformDisposition_platformID_feature"), getString("_UI_PropertyDescriptor_description", "_UI_PlatformDisposition_platformID_feature", "_UI_PlatformDisposition_type"), CorePackage.Literals.PLATFORM_DISPOSITION__PLATFORM_ID, false, false, false, ItemPropertyDescriptor.GENERIC_VALUE_IMAGE, null, null));
    }

    /**
	 * This adds a property descriptor for the Platform Dependant feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    protected void addPlatformDependantPropertyDescriptor(Object object) {
        itemPropertyDescriptors.add(createItemPropertyDescriptor(((ComposeableAdapterFactory) adapterFactory).getRootAdapterFactory(), getResourceLocator(), getString("_UI_PlatformDisposition_platformDependant_feature"), getString("_UI_PropertyDescriptor_description", "_UI_PlatformDisposition_platformDependant_feature", "_UI_PlatformDisposition_type"), CorePackage.Literals.PLATFORM_DISPOSITION__PLATFORM_DEPENDANT, false, false, false, ItemPropertyDescriptor.BOOLEAN_VALUE_IMAGE, null, null));
    }

    /**
	 * This adds a property descriptor for the Name feature.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 */
    protected void addNamePropertyDescriptor(Object object) {
        itemPropertyDescriptors.add(createItemPropertyDescriptor(((ComposeableAdapterFactory) adapterFactory).getRootAdapterFactory(), getResourceLocator(), getString("_UI_Saflet_name_feature"), getString("_UI_Saflet_name_description"), SafletPackage.Literals.SAFLET__NAME, true, false, false, ItemPropertyDescriptor.GENERIC_VALUE_IMAGE, null, null));
    }

    /**
	 * This adds a property descriptor for the Version feature.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 */
    protected void addVersionPropertyDescriptor(Object object) {
        itemPropertyDescriptors.add(createItemPropertyDescriptor(((ComposeableAdapterFactory) adapterFactory).getRootAdapterFactory(), getResourceLocator(), getString("_UI_Saflet_version_feature"), getString("_UI_Saflet_version_description"), SafletPackage.Literals.SAFLET__VERSION, false, false, false, ItemPropertyDescriptor.GENERIC_VALUE_IMAGE, null, null));
    }

    /**
	 * This adds a property descriptor for the Description feature.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 */
    protected void addDescriptionPropertyDescriptor(Object object) {
        itemPropertyDescriptors.add(createItemPropertyDescriptor(((ComposeableAdapterFactory) adapterFactory).getRootAdapterFactory(), getResourceLocator(), getString("_UI_Saflet_description_feature"), getString("_UI_PropertyDescriptor_description", "_UI_Saflet_description_feature", "_UI_Saflet_type"), SafletPackage.Literals.SAFLET__DESCRIPTION, true, true, false, ItemPropertyDescriptor.GENERIC_VALUE_IMAGE, null, null));
    }

    /**
	 * This adds a property descriptor for the Finally feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    protected void addFinallyPropertyDescriptor(Object object) {
        itemPropertyDescriptors.add(createItemPropertyDescriptor(((ComposeableAdapterFactory) adapterFactory).getRootAdapterFactory(), getResourceLocator(), getString("_UI_Saflet_finally_feature"), getString("_UI_PropertyDescriptor_description", "_UI_Saflet_finally_feature", "_UI_Saflet_type"), SafletPackage.Literals.SAFLET__FINALLY, true, false, true, null, null, null));
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
            childrenFeatures.add(SafletPackage.Literals.SAFLET__SAFLET_CONTEXT);
            childrenFeatures.add(SafletPackage.Literals.SAFLET__ACTIONSTEPS);
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
	 * This returns Saflet.gif.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    public Object getImage(Object object) {
        return overlayImage(object, getResourceLocator().getImage("full/obj16/Saflet"));
    }

    /**
	 * This returns the label text for the adapted class.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    public String getText(Object object) {
        String label = ((Saflet) object).getName();
        return label == null || label.length() == 0 ? getString("_UI_Saflet_type") : getString("_UI_Saflet_type") + " " + label;
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
        switch(notification.getFeatureID(Saflet.class)) {
            case SafletPackage.SAFLET__PLATFORM_ID:
            case SafletPackage.SAFLET__PLATFORM_DEPENDANT:
            case SafletPackage.SAFLET__ACTIVE:
            case SafletPackage.SAFLET__NAME:
            case SafletPackage.SAFLET__VERSION:
            case SafletPackage.SAFLET__DESCRIPTION:
            case SafletPackage.SAFLET__ID:
                fireNotifyChanged(new ViewerNotification(notification, notification.getNotifier(), false, true));
                return;
            case SafletPackage.SAFLET__SAFLET_CONTEXT:
            case SafletPackage.SAFLET__ACTIONSTEPS:
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
        newChildDescriptors.add(createChildParameter(SafletPackage.Literals.SAFLET__ACTIONSTEPS, ActionStepFactory.eINSTANCE.createAssignment()));
        newChildDescriptors.add(createChildParameter(SafletPackage.Literals.SAFLET__ACTIONSTEPS, ActionStepFactory.eINSTANCE.createChoice()));
        newChildDescriptors.add(createChildParameter(SafletPackage.Literals.SAFLET__ACTIONSTEPS, ActionStepFactory.eINSTANCE.createIfThen()));
        newChildDescriptors.add(createChildParameter(SafletPackage.Literals.SAFLET__ACTIONSTEPS, ActionStepFactory.eINSTANCE.createExecuteScript()));
        newChildDescriptors.add(createChildParameter(SafletPackage.Literals.SAFLET__ACTIONSTEPS, ActionStepFactory.eINSTANCE.createInvokeSaflet()));
        newChildDescriptors.add(createChildParameter(SafletPackage.Literals.SAFLET__ACTIONSTEPS, ActionStepFactory.eINSTANCE.createDebugLog()));
        newChildDescriptors.add(createChildParameter(SafletPackage.Literals.SAFLET__ACTIONSTEPS, ActionStepFactory.eINSTANCE.createOpenDBConnection()));
        newChildDescriptors.add(createChildParameter(SafletPackage.Literals.SAFLET__ACTIONSTEPS, ActionStepFactory.eINSTANCE.createCloseDBConnection()));
        newChildDescriptors.add(createChildParameter(SafletPackage.Literals.SAFLET__ACTIONSTEPS, ActionStepFactory.eINSTANCE.createOpenQuery()));
        newChildDescriptors.add(createChildParameter(SafletPackage.Literals.SAFLET__ACTIONSTEPS, ActionStepFactory.eINSTANCE.createSetQueryParam()));
        newChildDescriptors.add(createChildParameter(SafletPackage.Literals.SAFLET__ACTIONSTEPS, ActionStepFactory.eINSTANCE.createExecuteUpdate()));
        newChildDescriptors.add(createChildParameter(SafletPackage.Literals.SAFLET__ACTIONSTEPS, ActionStepFactory.eINSTANCE.createExecuteQuery()));
        newChildDescriptors.add(createChildParameter(SafletPackage.Literals.SAFLET__ACTIONSTEPS, ActionStepFactory.eINSTANCE.createNextRow()));
        newChildDescriptors.add(createChildParameter(SafletPackage.Literals.SAFLET__ACTIONSTEPS, ActionStepFactory.eINSTANCE.createGetColValue()));
        newChildDescriptors.add(createChildParameter(SafletPackage.Literals.SAFLET__ACTIONSTEPS, ActionStepFactory.eINSTANCE.createGetColValues()));
        newChildDescriptors.add(createChildParameter(SafletPackage.Literals.SAFLET__ACTIONSTEPS, ActionStepFactory.eINSTANCE.createSetColValue()));
        newChildDescriptors.add(createChildParameter(SafletPackage.Literals.SAFLET__ACTIONSTEPS, ActionStepFactory.eINSTANCE.createSetColValues()));
        newChildDescriptors.add(createChildParameter(SafletPackage.Literals.SAFLET__ACTIONSTEPS, ActionStepFactory.eINSTANCE.createUpdatetRow()));
        newChildDescriptors.add(createChildParameter(SafletPackage.Literals.SAFLET__ACTIONSTEPS, ActionStepFactory.eINSTANCE.createMoveToRow()));
        newChildDescriptors.add(createChildParameter(SafletPackage.Literals.SAFLET__ACTIONSTEPS, ActionStepFactory.eINSTANCE.createMoveToLastRow()));
        newChildDescriptors.add(createChildParameter(SafletPackage.Literals.SAFLET__ACTIONSTEPS, ActionStepFactory.eINSTANCE.createDeleteRow()));
        newChildDescriptors.add(createChildParameter(SafletPackage.Literals.SAFLET__ACTIONSTEPS, ActionStepFactory.eINSTANCE.createMoveToInsertRow()));
        newChildDescriptors.add(createChildParameter(SafletPackage.Literals.SAFLET__ACTIONSTEPS, ActionStepFactory.eINSTANCE.createInsertRow()));
        newChildDescriptors.add(createChildParameter(SafletPackage.Literals.SAFLET__ACTIONSTEPS, ActionStepFactory.eINSTANCE.createMoveToFirstRow()));
        newChildDescriptors.add(createChildParameter(SafletPackage.Literals.SAFLET__ACTIONSTEPS, ActionStepFactory.eINSTANCE.createPreviousRow()));
        newChildDescriptors.add(createChildParameter(SafletPackage.Literals.SAFLET__ACTIONSTEPS, ActionStepFactory.eINSTANCE.createRunQuery()));
        newChildDescriptors.add(createChildParameter(SafletPackage.Literals.SAFLET__ACTIONSTEPS, ActionStepFactory.eINSTANCE.createFinally()));
    }

    /**
	 * Return the resource locator for this item provider's resources.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    public ResourceLocator getResourceLocator() {
        return CoreEditPluginProv.INSTANCE;
    }
}
