package Leveleditor.provider;

import Leveleditor.LeveleditorFactory;
import Leveleditor.LeveleditorPackage;
import Leveleditor.Room;
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
 * This is the item provider adapter for a {@link Leveleditor.Room} object.
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 * @generated
 */
public class RoomItemProvider extends ItemProviderAdapter implements IEditingDomainItemProvider, IStructuredItemContentProvider, ITreeItemContentProvider, IItemLabelProvider, IItemPropertySource {

    /**
	 * This constructs an instance from a factory and a notifier.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public RoomItemProvider(AdapterFactory adapterFactory) {
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
            addDescriptionPropertyDescriptor(object);
            addPostDescriptionPropertyDescriptor(object);
            addPostItemsDescriptionPropertyDescriptor(object);
            addRevealedDescriptionPropertyDescriptor(object);
            addHiddenDescriptionPropertyDescriptor(object);
            addIsLockedPropertyDescriptor(object);
            addUnlockKeysPropertyDescriptor(object);
            addIsHiddenPropertyDescriptor(object);
            addIsRevealedPropertyDescriptor(object);
            addIsHiddenEventPropertyDescriptor(object);
            addNorthPropertyDescriptor(object);
            addSouthPropertyDescriptor(object);
            addWestPropertyDescriptor(object);
            addEastPropertyDescriptor(object);
            addAutosavePropertyDescriptor(object);
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
        itemPropertyDescriptors.add(createItemPropertyDescriptor(((ComposeableAdapterFactory) adapterFactory).getRootAdapterFactory(), getResourceLocator(), getString("_UI_Room_name_feature"), getString("_UI_PropertyDescriptor_description", "_UI_Room_name_feature", "_UI_Room_type"), LeveleditorPackage.Literals.ROOM__NAME, true, false, false, ItemPropertyDescriptor.GENERIC_VALUE_IMAGE, null, null));
    }

    /**
	 * This adds a property descriptor for the Description feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    protected void addDescriptionPropertyDescriptor(Object object) {
        itemPropertyDescriptors.add(createItemPropertyDescriptor(((ComposeableAdapterFactory) adapterFactory).getRootAdapterFactory(), getResourceLocator(), getString("_UI_Room_description_feature"), getString("_UI_PropertyDescriptor_description", "_UI_Room_description_feature", "_UI_Room_type"), LeveleditorPackage.Literals.ROOM__DESCRIPTION, true, false, false, ItemPropertyDescriptor.GENERIC_VALUE_IMAGE, null, null));
    }

    /**
	 * This adds a property descriptor for the Post Description feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    protected void addPostDescriptionPropertyDescriptor(Object object) {
        itemPropertyDescriptors.add(createItemPropertyDescriptor(((ComposeableAdapterFactory) adapterFactory).getRootAdapterFactory(), getResourceLocator(), getString("_UI_Room_postDescription_feature"), getString("_UI_PropertyDescriptor_description", "_UI_Room_postDescription_feature", "_UI_Room_type"), LeveleditorPackage.Literals.ROOM__POST_DESCRIPTION, true, false, false, ItemPropertyDescriptor.GENERIC_VALUE_IMAGE, null, null));
    }

    /**
	 * This adds a property descriptor for the Post Items Description feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    protected void addPostItemsDescriptionPropertyDescriptor(Object object) {
        itemPropertyDescriptors.add(createItemPropertyDescriptor(((ComposeableAdapterFactory) adapterFactory).getRootAdapterFactory(), getResourceLocator(), getString("_UI_Room_postItemsDescription_feature"), getString("_UI_PropertyDescriptor_description", "_UI_Room_postItemsDescription_feature", "_UI_Room_type"), LeveleditorPackage.Literals.ROOM__POST_ITEMS_DESCRIPTION, true, false, false, ItemPropertyDescriptor.GENERIC_VALUE_IMAGE, null, null));
    }

    /**
	 * This adds a property descriptor for the Revealed Description feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    protected void addRevealedDescriptionPropertyDescriptor(Object object) {
        itemPropertyDescriptors.add(createItemPropertyDescriptor(((ComposeableAdapterFactory) adapterFactory).getRootAdapterFactory(), getResourceLocator(), getString("_UI_Room_revealedDescription_feature"), getString("_UI_PropertyDescriptor_description", "_UI_Room_revealedDescription_feature", "_UI_Room_type"), LeveleditorPackage.Literals.ROOM__REVEALED_DESCRIPTION, true, false, false, ItemPropertyDescriptor.GENERIC_VALUE_IMAGE, null, null));
    }

    /**
	 * This adds a property descriptor for the Hidden Description feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    protected void addHiddenDescriptionPropertyDescriptor(Object object) {
        itemPropertyDescriptors.add(createItemPropertyDescriptor(((ComposeableAdapterFactory) adapterFactory).getRootAdapterFactory(), getResourceLocator(), getString("_UI_Room_hiddenDescription_feature"), getString("_UI_PropertyDescriptor_description", "_UI_Room_hiddenDescription_feature", "_UI_Room_type"), LeveleditorPackage.Literals.ROOM__HIDDEN_DESCRIPTION, true, false, false, ItemPropertyDescriptor.GENERIC_VALUE_IMAGE, null, null));
    }

    /**
	 * This adds a property descriptor for the North feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    protected void addNorthPropertyDescriptor(Object object) {
        itemPropertyDescriptors.add(createItemPropertyDescriptor(((ComposeableAdapterFactory) adapterFactory).getRootAdapterFactory(), getResourceLocator(), getString("_UI_Room_north_feature"), getString("_UI_PropertyDescriptor_description", "_UI_Room_north_feature", "_UI_Room_type"), LeveleditorPackage.Literals.ROOM__NORTH, true, false, true, null, null, null));
    }

    /**
	 * This adds a property descriptor for the South feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    protected void addSouthPropertyDescriptor(Object object) {
        itemPropertyDescriptors.add(createItemPropertyDescriptor(((ComposeableAdapterFactory) adapterFactory).getRootAdapterFactory(), getResourceLocator(), getString("_UI_Room_south_feature"), getString("_UI_PropertyDescriptor_description", "_UI_Room_south_feature", "_UI_Room_type"), LeveleditorPackage.Literals.ROOM__SOUTH, true, false, true, null, null, null));
    }

    /**
	 * This adds a property descriptor for the East feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    protected void addEastPropertyDescriptor(Object object) {
        itemPropertyDescriptors.add(createItemPropertyDescriptor(((ComposeableAdapterFactory) adapterFactory).getRootAdapterFactory(), getResourceLocator(), getString("_UI_Room_east_feature"), getString("_UI_PropertyDescriptor_description", "_UI_Room_east_feature", "_UI_Room_type"), LeveleditorPackage.Literals.ROOM__EAST, true, false, true, null, null, null));
    }

    /**
	 * This adds a property descriptor for the Autosave feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    protected void addAutosavePropertyDescriptor(Object object) {
        itemPropertyDescriptors.add(createItemPropertyDescriptor(((ComposeableAdapterFactory) adapterFactory).getRootAdapterFactory(), getResourceLocator(), getString("_UI_Room_autosave_feature"), getString("_UI_PropertyDescriptor_description", "_UI_Room_autosave_feature", "_UI_Room_type"), LeveleditorPackage.Literals.ROOM__AUTOSAVE, true, false, false, ItemPropertyDescriptor.BOOLEAN_VALUE_IMAGE, null, null));
    }

    /**
	 * This adds a property descriptor for the West feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    protected void addWestPropertyDescriptor(Object object) {
        itemPropertyDescriptors.add(createItemPropertyDescriptor(((ComposeableAdapterFactory) adapterFactory).getRootAdapterFactory(), getResourceLocator(), getString("_UI_Room_west_feature"), getString("_UI_PropertyDescriptor_description", "_UI_Room_west_feature", "_UI_Room_type"), LeveleditorPackage.Literals.ROOM__WEST, true, false, true, null, null, null));
    }

    /**
	 * This adds a property descriptor for the Is Locked feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    protected void addIsLockedPropertyDescriptor(Object object) {
        itemPropertyDescriptors.add(createItemPropertyDescriptor(((ComposeableAdapterFactory) adapterFactory).getRootAdapterFactory(), getResourceLocator(), getString("_UI_Room_isLocked_feature"), getString("_UI_PropertyDescriptor_description", "_UI_Room_isLocked_feature", "_UI_Room_type"), LeveleditorPackage.Literals.ROOM__IS_LOCKED, true, false, false, ItemPropertyDescriptor.BOOLEAN_VALUE_IMAGE, null, null));
    }

    /**
	 * This adds a property descriptor for the Unlock Keys feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    protected void addUnlockKeysPropertyDescriptor(Object object) {
        itemPropertyDescriptors.add(createItemPropertyDescriptor(((ComposeableAdapterFactory) adapterFactory).getRootAdapterFactory(), getResourceLocator(), getString("_UI_Room_unlockKeys_feature"), getString("_UI_PropertyDescriptor_description", "_UI_Room_unlockKeys_feature", "_UI_Room_type"), LeveleditorPackage.Literals.ROOM__UNLOCK_KEYS, true, false, true, null, null, null));
    }

    /**
	 * This adds a property descriptor for the Is Hidden feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    protected void addIsHiddenPropertyDescriptor(Object object) {
        itemPropertyDescriptors.add(createItemPropertyDescriptor(((ComposeableAdapterFactory) adapterFactory).getRootAdapterFactory(), getResourceLocator(), getString("_UI_Room_isHidden_feature"), getString("_UI_PropertyDescriptor_description", "_UI_Room_isHidden_feature", "_UI_Room_type"), LeveleditorPackage.Literals.ROOM__IS_HIDDEN, true, false, false, ItemPropertyDescriptor.BOOLEAN_VALUE_IMAGE, null, null));
    }

    /**
	 * This adds a property descriptor for the Is Revealed feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    protected void addIsRevealedPropertyDescriptor(Object object) {
        itemPropertyDescriptors.add(createItemPropertyDescriptor(((ComposeableAdapterFactory) adapterFactory).getRootAdapterFactory(), getResourceLocator(), getString("_UI_Room_isRevealed_feature"), getString("_UI_PropertyDescriptor_description", "_UI_Room_isRevealed_feature", "_UI_Room_type"), LeveleditorPackage.Literals.ROOM__IS_REVEALED, true, false, false, ItemPropertyDescriptor.BOOLEAN_VALUE_IMAGE, null, null));
    }

    /**
	 * This adds a property descriptor for the Is Hidden Event feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    protected void addIsHiddenEventPropertyDescriptor(Object object) {
        itemPropertyDescriptors.add(createItemPropertyDescriptor(((ComposeableAdapterFactory) adapterFactory).getRootAdapterFactory(), getResourceLocator(), getString("_UI_Room_isHiddenEvent_feature"), getString("_UI_PropertyDescriptor_description", "_UI_Room_isHiddenEvent_feature", "_UI_Room_type"), LeveleditorPackage.Literals.ROOM__IS_HIDDEN_EVENT, true, false, false, ItemPropertyDescriptor.BOOLEAN_VALUE_IMAGE, null, null));
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
            childrenFeatures.add(LeveleditorPackage.Literals.ROOM__ENEMIES);
            childrenFeatures.add(LeveleditorPackage.Literals.ROOM__NPCS);
            childrenFeatures.add(LeveleditorPackage.Literals.ROOM__ITEMS);
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
        String label = ((Room) object).getName();
        return label == null || label.length() == 0 ? getString("_UI_Room_type") : getString("_UI_Room_type") + " " + label;
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
        switch(notification.getFeatureID(Room.class)) {
            case LeveleditorPackage.ROOM__NAME:
            case LeveleditorPackage.ROOM__DESCRIPTION:
            case LeveleditorPackage.ROOM__POST_DESCRIPTION:
            case LeveleditorPackage.ROOM__POST_ITEMS_DESCRIPTION:
            case LeveleditorPackage.ROOM__REVEALED_DESCRIPTION:
            case LeveleditorPackage.ROOM__HIDDEN_DESCRIPTION:
            case LeveleditorPackage.ROOM__IS_LOCKED:
            case LeveleditorPackage.ROOM__IS_HIDDEN:
            case LeveleditorPackage.ROOM__IS_REVEALED:
            case LeveleditorPackage.ROOM__IS_HIDDEN_EVENT:
            case LeveleditorPackage.ROOM__AUTOSAVE:
                fireNotifyChanged(new ViewerNotification(notification, notification.getNotifier(), false, true));
                return;
            case LeveleditorPackage.ROOM__ENEMIES:
            case LeveleditorPackage.ROOM__NPCS:
            case LeveleditorPackage.ROOM__ITEMS:
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
        newChildDescriptors.add(createChildParameter(LeveleditorPackage.Literals.ROOM__ENEMIES, LeveleditorFactory.eINSTANCE.createEnemy()));
        newChildDescriptors.add(createChildParameter(LeveleditorPackage.Literals.ROOM__NPCS, LeveleditorFactory.eINSTANCE.createNPC()));
        newChildDescriptors.add(createChildParameter(LeveleditorPackage.Literals.ROOM__ITEMS, LeveleditorFactory.eINSTANCE.createKey()));
        newChildDescriptors.add(createChildParameter(LeveleditorPackage.Literals.ROOM__ITEMS, LeveleditorFactory.eINSTANCE.createGold()));
        newChildDescriptors.add(createChildParameter(LeveleditorPackage.Literals.ROOM__ITEMS, LeveleditorFactory.eINSTANCE.createPotion()));
        newChildDescriptors.add(createChildParameter(LeveleditorPackage.Literals.ROOM__ITEMS, LeveleditorFactory.eINSTANCE.createWeapon()));
        newChildDescriptors.add(createChildParameter(LeveleditorPackage.Literals.ROOM__ITEMS, LeveleditorFactory.eINSTANCE.createArmor()));
        newChildDescriptors.add(createChildParameter(LeveleditorPackage.Literals.ROOM__ITEMS, LeveleditorFactory.eINSTANCE.createAccessory()));
    }

    /**
	 * Return the resource locator for this item provider's resources.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    public ResourceLocator getResourceLocator() {
        return LeveleditorEditPlugin.INSTANCE;
    }
}
