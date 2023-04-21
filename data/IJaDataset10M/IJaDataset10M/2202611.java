package ch.hsr.orm.model.provider;

import ch.hsr.orm.model.Diagram;
import ch.hsr.orm.model.ModelFactory;
import ch.hsr.orm.model.ModelPackage;
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
 * This is the item provider adapter for a {@link ch.hsr.orm.model.Diagram} object.
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 * @generated
 */
public class DiagramItemProvider extends ItemProviderAdapter implements IEditingDomainItemProvider, IStructuredItemContentProvider, ITreeItemContentProvider, IItemLabelProvider, IItemPropertySource {

    /**
	 * This constructs an instance from a factory and a notifier.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public DiagramItemProvider(AdapterFactory adapterFactory) {
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
            addNamePropertyDescriptor(object);
            addPackagePropertyDescriptor(object);
        }
        return itemPropertyDescriptors;
    }

    /**
	 * This adds a property descriptor for the Name feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    protected void addNamePropertyDescriptor(Object object) {
        itemPropertyDescriptors.add(createItemPropertyDescriptor(((ComposeableAdapterFactory) adapterFactory).getRootAdapterFactory(), getResourceLocator(), getString("_UI_Diagram_name_feature"), getString("_UI_Diagram_name_description"), ModelPackage.Literals.DIAGRAM__NAME, true, false, false, ItemPropertyDescriptor.GENERIC_VALUE_IMAGE, null, null));
    }

    /**
	 * This adds a property descriptor for the Package feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    protected void addPackagePropertyDescriptor(Object object) {
        itemPropertyDescriptors.add(createItemPropertyDescriptor(((ComposeableAdapterFactory) adapterFactory).getRootAdapterFactory(), getResourceLocator(), getString("_UI_Diagram_package_feature"), getString("_UI_Diagram_package_description"), ModelPackage.Literals.DIAGRAM__PACKAGE, true, false, false, ItemPropertyDescriptor.GENERIC_VALUE_IMAGE, null, null));
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
            childrenFeatures.add(ModelPackage.Literals.DIAGRAM__ENTITIES);
            childrenFeatures.add(ModelPackage.Literals.DIAGRAM__RELATIONS);
            childrenFeatures.add(ModelPackage.Literals.DIAGRAM__GENERALIZATIONS);
            childrenFeatures.add(ModelPackage.Literals.DIAGRAM__EMBEDDINGS);
            childrenFeatures.add(ModelPackage.Literals.DIAGRAM__COMPOSITIONS);
            childrenFeatures.add(ModelPackage.Literals.DIAGRAM__GENERATORS);
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
	 * This returns Diagram.gif.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    public Object getImage(Object object) {
        return overlayImage(object, getResourceLocator().getImage("full/obj16/Diagram"));
    }

    /**
	 * This returns the label text for the adapted class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    public String getText(Object object) {
        String label = ((Diagram) object).getName();
        return label == null || label.length() == 0 ? getString("_UI_Diagram_type") : getString("_UI_Diagram_type") + " " + label;
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
        switch(notification.getFeatureID(Diagram.class)) {
            case ModelPackage.DIAGRAM__NAME:
            case ModelPackage.DIAGRAM__PACKAGE:
                fireNotifyChanged(new ViewerNotification(notification, notification.getNotifier(), false, true));
                return;
            case ModelPackage.DIAGRAM__ENTITIES:
            case ModelPackage.DIAGRAM__RELATIONS:
            case ModelPackage.DIAGRAM__GENERALIZATIONS:
            case ModelPackage.DIAGRAM__EMBEDDINGS:
            case ModelPackage.DIAGRAM__COMPOSITIONS:
            case ModelPackage.DIAGRAM__GENERATORS:
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
        newChildDescriptors.add(createChildParameter(ModelPackage.Literals.DIAGRAM__ENTITIES, ModelFactory.eINSTANCE.createEntity()));
        newChildDescriptors.add(createChildParameter(ModelPackage.Literals.DIAGRAM__RELATIONS, ModelFactory.eINSTANCE.createUniOneToOne()));
        newChildDescriptors.add(createChildParameter(ModelPackage.Literals.DIAGRAM__RELATIONS, ModelFactory.eINSTANCE.createUniOneToMany()));
        newChildDescriptors.add(createChildParameter(ModelPackage.Literals.DIAGRAM__RELATIONS, ModelFactory.eINSTANCE.createUniManyToMany()));
        newChildDescriptors.add(createChildParameter(ModelPackage.Literals.DIAGRAM__RELATIONS, ModelFactory.eINSTANCE.createBiOneToOne()));
        newChildDescriptors.add(createChildParameter(ModelPackage.Literals.DIAGRAM__RELATIONS, ModelFactory.eINSTANCE.createBiOneToMany()));
        newChildDescriptors.add(createChildParameter(ModelPackage.Literals.DIAGRAM__RELATIONS, ModelFactory.eINSTANCE.createBiManyToMany()));
        newChildDescriptors.add(createChildParameter(ModelPackage.Literals.DIAGRAM__GENERALIZATIONS, ModelFactory.eINSTANCE.createGeneralization()));
        newChildDescriptors.add(createChildParameter(ModelPackage.Literals.DIAGRAM__EMBEDDINGS, ModelFactory.eINSTANCE.createEmbedded()));
        newChildDescriptors.add(createChildParameter(ModelPackage.Literals.DIAGRAM__COMPOSITIONS, ModelFactory.eINSTANCE.createComposition()));
        newChildDescriptors.add(createChildParameter(ModelPackage.Literals.DIAGRAM__GENERATORS, ModelFactory.eINSTANCE.createAutoincrementGenerator()));
        newChildDescriptors.add(createChildParameter(ModelPackage.Literals.DIAGRAM__GENERATORS, ModelFactory.eINSTANCE.createTableGenerator()));
        newChildDescriptors.add(createChildParameter(ModelPackage.Literals.DIAGRAM__GENERATORS, ModelFactory.eINSTANCE.createSequenceGenerator()));
    }

    /**
	 * Return the resource locator for this item provider's resources.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    public ResourceLocator getResourceLocator() {
        return OrmmetaEditPlugin.INSTANCE;
    }
}
