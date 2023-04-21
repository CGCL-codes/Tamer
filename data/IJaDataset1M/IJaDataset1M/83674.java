package net.taylor.uml2.uml.edit.providers;

import java.util.Collection;
import java.util.List;
import net.taylor.uml2.uml.edit.UMLEditPlugin;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.ResourceLocator;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edit.provider.ComposeableAdapterFactory;
import org.eclipse.emf.edit.provider.IEditingDomainItemProvider;
import org.eclipse.emf.edit.provider.IItemLabelProvider;
import org.eclipse.emf.edit.provider.IItemPropertySource;
import org.eclipse.emf.edit.provider.IStructuredItemContentProvider;
import org.eclipse.emf.edit.provider.ITreeItemContentProvider;
import org.eclipse.emf.edit.provider.ViewerNotification;
import org.eclipse.uml2.uml.Namespace;
import org.eclipse.uml2.uml.UMLPackage;

/**
 * This is the item provider adapter for a {@link org.eclipse.uml2.uml.Namespace} object.
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 * @generated
 */
public class NamespaceItemProvider extends NamedElementItemProvider implements IEditingDomainItemProvider, IStructuredItemContentProvider, ITreeItemContentProvider, IItemLabelProvider, IItemPropertySource {

    /**
	 * This constructs an instance from a factory and a notifier.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public NamespaceItemProvider(AdapterFactory adapterFactory) {
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
            addElementImportPropertyDescriptor(object);
            addPackageImportPropertyDescriptor(object);
            addOwnedRulePropertyDescriptor(object);
            addMemberPropertyDescriptor(object);
            addImportedMemberPropertyDescriptor(object);
            addOwnedMemberPropertyDescriptor(object);
        }
        return itemPropertyDescriptors;
    }

    /**
	 * This adds a property descriptor for the Element Import feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    protected void addElementImportPropertyDescriptor(Object object) {
        itemPropertyDescriptors.add(createItemPropertyDescriptor(((ComposeableAdapterFactory) adapterFactory).getRootAdapterFactory(), getResourceLocator(), getString("_UI_Namespace_elementImport_feature"), getString("_UI_PropertyDescriptor_description", "_UI_Namespace_elementImport_feature", "_UI_Namespace_type"), UMLPackage.Literals.NAMESPACE__ELEMENT_IMPORT, true, null, null, new String[] { "org.eclipse.ui.views.properties.expert" }));
    }

    /**
	 * This adds a property descriptor for the Package Import feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    protected void addPackageImportPropertyDescriptor(Object object) {
        itemPropertyDescriptors.add(createItemPropertyDescriptor(((ComposeableAdapterFactory) adapterFactory).getRootAdapterFactory(), getResourceLocator(), getString("_UI_Namespace_packageImport_feature"), getString("_UI_PropertyDescriptor_description", "_UI_Namespace_packageImport_feature", "_UI_Namespace_type"), UMLPackage.Literals.NAMESPACE__PACKAGE_IMPORT, true, null, null, new String[] { "org.eclipse.ui.views.properties.expert" }));
    }

    /**
	 * This adds a property descriptor for the Owned Rule feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    protected void addOwnedRulePropertyDescriptor(Object object) {
        itemPropertyDescriptors.add(createItemPropertyDescriptor(((ComposeableAdapterFactory) adapterFactory).getRootAdapterFactory(), getResourceLocator(), getString("_UI_Namespace_ownedRule_feature"), getString("_UI_PropertyDescriptor_description", "_UI_Namespace_ownedRule_feature", "_UI_Namespace_type"), UMLPackage.Literals.NAMESPACE__OWNED_RULE, true, null, null, new String[] { "org.eclipse.ui.views.properties.expert" }));
    }

    /**
	 * This adds a property descriptor for the Member feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    protected void addMemberPropertyDescriptor(Object object) {
        itemPropertyDescriptors.add(createItemPropertyDescriptor(((ComposeableAdapterFactory) adapterFactory).getRootAdapterFactory(), getResourceLocator(), getString("_UI_Namespace_member_feature"), getString("_UI_PropertyDescriptor_description", "_UI_Namespace_member_feature", "_UI_Namespace_type"), UMLPackage.Literals.NAMESPACE__MEMBER, false, null, null, new String[] { "org.eclipse.ui.views.properties.expert" }));
    }

    /**
	 * This adds a property descriptor for the Imported Member feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    protected void addImportedMemberPropertyDescriptor(Object object) {
        itemPropertyDescriptors.add(createItemPropertyDescriptor(((ComposeableAdapterFactory) adapterFactory).getRootAdapterFactory(), getResourceLocator(), getString("_UI_Namespace_importedMember_feature"), getString("_UI_PropertyDescriptor_description", "_UI_Namespace_importedMember_feature", "_UI_Namespace_type"), UMLPackage.Literals.NAMESPACE__IMPORTED_MEMBER, false, null, null, new String[] { "org.eclipse.ui.views.properties.expert" }));
    }

    /**
	 * This adds a property descriptor for the Owned Member feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    protected void addOwnedMemberPropertyDescriptor(Object object) {
        itemPropertyDescriptors.add(createItemPropertyDescriptor(((ComposeableAdapterFactory) adapterFactory).getRootAdapterFactory(), getResourceLocator(), getString("_UI_Namespace_ownedMember_feature"), getString("_UI_PropertyDescriptor_description", "_UI_Namespace_ownedMember_feature", "_UI_Namespace_type"), UMLPackage.Literals.NAMESPACE__OWNED_MEMBER, false, null, null, new String[] { "org.eclipse.ui.views.properties.expert" }));
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
            childrenFeatures.add(UMLPackage.Literals.NAMESPACE__ELEMENT_IMPORT);
            childrenFeatures.add(UMLPackage.Literals.NAMESPACE__PACKAGE_IMPORT);
            childrenFeatures.add(UMLPackage.Literals.NAMESPACE__OWNED_RULE);
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
        String label = ((Namespace) object).getName();
        return label == null || label.length() == 0 ? getString("_UI_Namespace_type") : getString("_UI_Namespace_type") + " " + label;
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
        switch(notification.getFeatureID(Namespace.class)) {
            case UMLPackage.NAMESPACE__ELEMENT_IMPORT:
            case UMLPackage.NAMESPACE__PACKAGE_IMPORT:
            case UMLPackage.NAMESPACE__OWNED_RULE:
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
}
