package net.taylor.uml2.uml.edit.providers;

import java.util.Collection;
import java.util.List;
import net.taylor.uml2.uml.edit.UMLEditPlugin;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.ResourceLocator;
import org.eclipse.emf.edit.provider.ComposeableAdapterFactory;
import org.eclipse.emf.edit.provider.IEditingDomainItemProvider;
import org.eclipse.emf.edit.provider.IItemLabelProvider;
import org.eclipse.emf.edit.provider.IItemPropertySource;
import org.eclipse.emf.edit.provider.IStructuredItemContentProvider;
import org.eclipse.emf.edit.provider.ITreeItemContentProvider;
import org.eclipse.emf.edit.provider.ViewerNotification;
import org.eclipse.uml2.uml.ChangeEvent;
import org.eclipse.uml2.uml.UMLFactory;
import org.eclipse.uml2.uml.UMLPackage;

/**
 * This is the item provider adapter for a {@link org.eclipse.uml2.uml.ChangeEvent} object.
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 * @generated
 */
public class ChangeEventItemProvider extends EventItemProvider implements IEditingDomainItemProvider, IStructuredItemContentProvider, ITreeItemContentProvider, IItemLabelProvider, IItemPropertySource {

    /**
	 * This constructs an instance from a factory and a notifier.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public ChangeEventItemProvider(AdapterFactory adapterFactory) {
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
            addChangeExpressionPropertyDescriptor(object);
        }
        return itemPropertyDescriptors;
    }

    /**
	 * This adds a property descriptor for the Change Expression feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    protected void addChangeExpressionPropertyDescriptor(Object object) {
        itemPropertyDescriptors.add(createItemPropertyDescriptor(((ComposeableAdapterFactory) adapterFactory).getRootAdapterFactory(), getResourceLocator(), getString("_UI_ChangeEvent_changeExpression_feature"), getString("_UI_PropertyDescriptor_description", "_UI_ChangeEvent_changeExpression_feature", "_UI_ChangeEvent_type"), UMLPackage.Literals.CHANGE_EVENT__CHANGE_EXPRESSION, true, null, null, new String[] { "org.eclipse.ui.views.properties.expert" }));
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
            childrenFeatures.add(UMLPackage.Literals.CHANGE_EVENT__CHANGE_EXPRESSION);
        }
        return childrenFeatures;
    }

    /**
	 * This returns ChangeEvent.gif.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public Object getImage(Object object) {
        return overlayImage(object, getResourceLocator().getImage("full/obj16/ChangeEvent"));
    }

    /**
	 * This returns the label text for the adapted class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public String getText(Object object) {
        String label = ((ChangeEvent) object).getName();
        return label == null || label.length() == 0 ? getString("_UI_ChangeEvent_type") : getString("_UI_ChangeEvent_type") + " " + label;
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
        switch(notification.getFeatureID(ChangeEvent.class)) {
            case UMLPackage.CHANGE_EVENT__CHANGE_EXPRESSION:
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
        newChildDescriptors.add(createChildParameter(UMLPackage.Literals.CHANGE_EVENT__CHANGE_EXPRESSION, UMLFactory.eINSTANCE.createOpaqueExpression()));
        newChildDescriptors.add(createChildParameter(UMLPackage.Literals.CHANGE_EVENT__CHANGE_EXPRESSION, UMLFactory.eINSTANCE.createExpression()));
        newChildDescriptors.add(createChildParameter(UMLPackage.Literals.CHANGE_EVENT__CHANGE_EXPRESSION, UMLFactory.eINSTANCE.createStringExpression()));
        newChildDescriptors.add(createChildParameter(UMLPackage.Literals.CHANGE_EVENT__CHANGE_EXPRESSION, UMLFactory.eINSTANCE.createLiteralInteger()));
        newChildDescriptors.add(createChildParameter(UMLPackage.Literals.CHANGE_EVENT__CHANGE_EXPRESSION, UMLFactory.eINSTANCE.createLiteralString()));
        newChildDescriptors.add(createChildParameter(UMLPackage.Literals.CHANGE_EVENT__CHANGE_EXPRESSION, UMLFactory.eINSTANCE.createLiteralBoolean()));
        newChildDescriptors.add(createChildParameter(UMLPackage.Literals.CHANGE_EVENT__CHANGE_EXPRESSION, UMLFactory.eINSTANCE.createLiteralNull()));
        newChildDescriptors.add(createChildParameter(UMLPackage.Literals.CHANGE_EVENT__CHANGE_EXPRESSION, UMLFactory.eINSTANCE.createInstanceValue()));
        newChildDescriptors.add(createChildParameter(UMLPackage.Literals.CHANGE_EVENT__CHANGE_EXPRESSION, UMLFactory.eINSTANCE.createLiteralUnlimitedNatural()));
        newChildDescriptors.add(createChildParameter(UMLPackage.Literals.CHANGE_EVENT__CHANGE_EXPRESSION, UMLFactory.eINSTANCE.createTimeExpression()));
        newChildDescriptors.add(createChildParameter(UMLPackage.Literals.CHANGE_EVENT__CHANGE_EXPRESSION, UMLFactory.eINSTANCE.createDuration()));
        newChildDescriptors.add(createChildParameter(UMLPackage.Literals.CHANGE_EVENT__CHANGE_EXPRESSION, UMLFactory.eINSTANCE.createInterval()));
        newChildDescriptors.add(createChildParameter(UMLPackage.Literals.CHANGE_EVENT__CHANGE_EXPRESSION, UMLFactory.eINSTANCE.createDurationInterval()));
        newChildDescriptors.add(createChildParameter(UMLPackage.Literals.CHANGE_EVENT__CHANGE_EXPRESSION, UMLFactory.eINSTANCE.createTimeInterval()));
    }

    /**
	 * This returns the label text for {@link org.eclipse.emf.edit.command.CreateChildCommand}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public String getCreateChildText(Object owner, Object feature, Object child, Collection selection) {
        Object childFeature = feature;
        Object childObject = child;
        boolean qualify = childFeature == UMLPackage.Literals.NAMED_ELEMENT__NAME_EXPRESSION || childFeature == UMLPackage.Literals.CHANGE_EVENT__CHANGE_EXPRESSION;
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
    public ResourceLocator getResourceLocator() {
        return UMLEditPlugin.INSTANCE;
    }
}
