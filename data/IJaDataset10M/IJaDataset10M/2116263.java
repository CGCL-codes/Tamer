package de.mpiwg.vspace.metamodel.provider;

import java.util.Collection;
import java.util.List;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.ResourceLocator;
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
import de.mpiwg.vspace.metamodel.BranchingPointChoice;
import de.mpiwg.vspace.metamodel.ExhibitionPackage;
import de.mpiwg.vspace.metamodel.extension.IExtendedItemPropertyDescriptor;
import de.mpiwg.vspace.metamodel.extension.PropertyDescriptorFactoryService;
import de.mpiwg.vspace.metamodel.properties.descriptors.SequenceChoosingPropertyDescriptor;

/**
 * This is the item provider adapter for a {@link de.mpiwg.vspace.metamodel.BranchingPointChoice} object.
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 * @generated
 */
public class BranchingPointChoiceItemProvider extends ItemProviderAdapter implements IEditingDomainItemProvider, IStructuredItemContentProvider, ITreeItemContentProvider, IItemLabelProvider, IItemPropertySource {

    /**
	 * This constructs an instance from a factory and a notifier.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public BranchingPointChoiceItemProvider(AdapterFactory adapterFactory) {
        super(adapterFactory);
    }

    /**
	 * This returns the property descriptors for the adapted class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
    @Override
    public List<IItemPropertyDescriptor> getPropertyDescriptors(Object object) {
        if (itemPropertyDescriptors == null) {
            super.getPropertyDescriptors(object);
            addTitlePropertyDescriptor(object);
            addSequencePropertyDescriptor(object);
        }
        return itemPropertyDescriptors;
    }

    /**
	 * This adds a property descriptor for the Title feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
    protected void addTitlePropertyDescriptor(Object object) {
        IExtendedItemPropertyDescriptor descriptor = PropertyDescriptorFactoryService.INSTANCE.getItemPropertyDescriptor(ExhibitionPackage.BRANCHING_POINT_CHOICE__TITLE, (ComposeableAdapterFactory) adapterFactory, getResourceLocator(), getString("_UI_BranchingPointChoice_title_feature"), getString("_UI_PropertyDescriptor_description", "_UI_BranchingPointChoice_title_feature", "_UI_BranchingPointChoice_type"), ExhibitionPackage.Literals.BRANCHING_POINT_CHOICE__TITLE);
        if (descriptor != null) {
            itemPropertyDescriptors.add(descriptor);
            return;
        }
        itemPropertyDescriptors.add(createItemPropertyDescriptor(((ComposeableAdapterFactory) adapterFactory).getRootAdapterFactory(), getResourceLocator(), getString("_UI_BranchingPointChoice_title_feature"), getString("_UI_PropertyDescriptor_description", "_UI_BranchingPointChoice_title_feature", "_UI_BranchingPointChoice_type"), ExhibitionPackage.Literals.BRANCHING_POINT_CHOICE__TITLE, true, false, false, ItemPropertyDescriptor.GENERIC_VALUE_IMAGE, null, null));
    }

    /**
	 * This adds a property descriptor for the Sequence feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
    protected void addSequencePropertyDescriptor(Object object) {
        itemPropertyDescriptors.add(new SequenceChoosingPropertyDescriptor(((ComposeableAdapterFactory) adapterFactory).getRootAdapterFactory(), getResourceLocator(), getString("_UI_BranchingPointChoice_sequence_feature"), getString("_UI_PropertyDescriptor_description", "_UI_BranchingPointChoice_sequence_feature", "_UI_BranchingPointChoice_type"), ExhibitionPackage.Literals.BRANCHING_POINT_CHOICE__SEQUENCE, true, false, true, null, null, null));
    }

    /**
	 * This adds a property descriptor for the Id feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    protected void addIdPropertyDescriptor(Object object) {
        itemPropertyDescriptors.add(createItemPropertyDescriptor(((ComposeableAdapterFactory) adapterFactory).getRootAdapterFactory(), getResourceLocator(), getString("_UI_BranchingPointChoice_id_feature"), getString("_UI_PropertyDescriptor_description", "_UI_BranchingPointChoice_id_feature", "_UI_BranchingPointChoice_type"), ExhibitionPackage.Literals.BRANCHING_POINT_CHOICE__ID, true, false, false, ItemPropertyDescriptor.GENERIC_VALUE_IMAGE, null, null));
    }

    /**
	 * This returns BranchingPointChoice.gif.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
    @Override
    public Object getImage(Object object) {
        return overlayImage(object, getResourceLocator().getImage("exhibition/objects/BranchingPointChoice"));
    }

    /**
	 * This returns the label text for the adapted class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
    @Override
    public String getText(Object object) {
        String label = ((BranchingPointChoice) object).getTitle();
        return label == null || label.length() == 0 ? getString("_UI_BranchingPointChoice_type") : getString("_UI_BranchingPointChoice_type") + " " + label;
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
        switch(notification.getFeatureID(BranchingPointChoice.class)) {
            case ExhibitionPackage.BRANCHING_POINT_CHOICE__TITLE:
            case ExhibitionPackage.BRANCHING_POINT_CHOICE__ID:
                fireNotifyChanged(new ViewerNotification(notification, notification.getNotifier(), false, true));
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
    }

    /**
	 * Return the resource locator for this item provider's resources.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    public ResourceLocator getResourceLocator() {
        return ExhibitionEditPlugin.INSTANCE;
    }
}
