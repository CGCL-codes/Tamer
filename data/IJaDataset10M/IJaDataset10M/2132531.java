package M3Actions.provider;

import M3Actions.M3ActionsFactory;
import M3Actions.M3ActionsPackage;
import M3Actions.MActionGroup;
import java.util.Collection;
import java.util.List;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.ResourceLocator;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edit.provider.IEditingDomainItemProvider;
import org.eclipse.emf.edit.provider.IItemLabelProvider;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.IItemPropertySource;
import org.eclipse.emf.edit.provider.IStructuredItemContentProvider;
import org.eclipse.emf.edit.provider.ITreeItemContentProvider;
import org.eclipse.emf.edit.provider.ViewerNotification;

/**
 * This is the item provider adapter for a {@link M3Actions.MActionGroup} object.
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 * @generated
 */
public class MActionGroupItemProvider extends MActionItemProvider implements IEditingDomainItemProvider, IStructuredItemContentProvider, ITreeItemContentProvider, IItemLabelProvider, IItemPropertySource {

    /**
	 * This constructs an instance from a factory and a notifier.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public MActionGroupItemProvider(AdapterFactory adapterFactory) {
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
        }
        return itemPropertyDescriptors;
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
            childrenFeatures.add(M3ActionsPackage.Literals.MACTION_GROUP__MEMBER_NODES);
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
        String label = ((MActionGroup) object).getName();
        return label == null || label.length() == 0 ? getString("_UI_MActionGroup_type") : getString("_UI_MActionGroup_type") + " " + label;
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
        switch(notification.getFeatureID(MActionGroup.class)) {
            case M3ActionsPackage.MACTION_GROUP__MEMBER_NODES:
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
        newChildDescriptors.add(createChildParameter(M3ActionsPackage.Literals.MACTION_GROUP__MEMBER_NODES, M3ActionsFactory.eINSTANCE.createMAssignAction()));
        newChildDescriptors.add(createChildParameter(M3ActionsPackage.Literals.MACTION_GROUP__MEMBER_NODES, M3ActionsFactory.eINSTANCE.createMCreateAction()));
        newChildDescriptors.add(createChildParameter(M3ActionsPackage.Literals.MACTION_GROUP__MEMBER_NODES, M3ActionsFactory.eINSTANCE.createMDecisionMergeNode()));
        newChildDescriptors.add(createChildParameter(M3ActionsPackage.Literals.MACTION_GROUP__MEMBER_NODES, M3ActionsFactory.eINSTANCE.createMFinalNode()));
        newChildDescriptors.add(createChildParameter(M3ActionsPackage.Literals.MACTION_GROUP__MEMBER_NODES, M3ActionsFactory.eINSTANCE.createMForkJoinNode()));
        newChildDescriptors.add(createChildParameter(M3ActionsPackage.Literals.MACTION_GROUP__MEMBER_NODES, M3ActionsFactory.eINSTANCE.createMInitialNode()));
        newChildDescriptors.add(createChildParameter(M3ActionsPackage.Literals.MACTION_GROUP__MEMBER_NODES, M3ActionsFactory.eINSTANCE.createMInvocationAction()));
        newChildDescriptors.add(createChildParameter(M3ActionsPackage.Literals.MACTION_GROUP__MEMBER_NODES, M3ActionsFactory.eINSTANCE.createMIterateAction()));
        newChildDescriptors.add(createChildParameter(M3ActionsPackage.Literals.MACTION_GROUP__MEMBER_NODES, M3ActionsFactory.eINSTANCE.createMObjectNode()));
        newChildDescriptors.add(createChildParameter(M3ActionsPackage.Literals.MACTION_GROUP__MEMBER_NODES, M3ActionsFactory.eINSTANCE.createMPin()));
        newChildDescriptors.add(createChildParameter(M3ActionsPackage.Literals.MACTION_GROUP__MEMBER_NODES, M3ActionsFactory.eINSTANCE.createMQueryAction()));
        newChildDescriptors.add(createChildParameter(M3ActionsPackage.Literals.MACTION_GROUP__MEMBER_NODES, M3ActionsFactory.eINSTANCE.createMInput()));
        newChildDescriptors.add(createChildParameter(M3ActionsPackage.Literals.MACTION_GROUP__MEMBER_NODES, M3ActionsFactory.eINSTANCE.createMOutput()));
        newChildDescriptors.add(createChildParameter(M3ActionsPackage.Literals.MACTION_GROUP__MEMBER_NODES, M3ActionsFactory.eINSTANCE.createMAtomicGroup()));
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
        boolean qualify = childFeature == M3ActionsPackage.Literals.MACTION__OWNED_PINS || childFeature == M3ActionsPackage.Literals.MACTION_GROUP__MEMBER_NODES;
        if (qualify) {
            return getString("_UI_CreateChild_text2", new Object[] { getTypeText(childObject), getFeatureText(childFeature), getTypeText(owner) });
        }
        return super.getCreateChildText(owner, feature, child, selection);
    }
}
