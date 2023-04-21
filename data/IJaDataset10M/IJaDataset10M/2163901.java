package net.sf.etl.parsers.grammar.provider;

import java.util.Collection;
import java.util.List;
import net.sf.etl.parsers.grammar.GrammarFactory;
import net.sf.etl.parsers.grammar.GrammarPackage;
import net.sf.etl.parsers.grammar.Sequence;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.ResourceLocator;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edit.provider.IEditingDomainItemProvider;
import org.eclipse.emf.edit.provider.IItemLabelProvider;
import org.eclipse.emf.edit.provider.IItemPropertySource;
import org.eclipse.emf.edit.provider.IStructuredItemContentProvider;
import org.eclipse.emf.edit.provider.ITreeItemContentProvider;
import org.eclipse.emf.edit.provider.ViewerNotification;

/**
 * This is the item provider adapter for a {@link net.sf.etl.parsers.grammar.Sequence} object.
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 * @generated
 */
public class SequenceItemProvider extends SyntaxItemProvider implements IEditingDomainItemProvider, IStructuredItemContentProvider, ITreeItemContentProvider, IItemLabelProvider, IItemPropertySource {

    /**
	 * This constructs an instance from a factory and a notifier.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public SequenceItemProvider(AdapterFactory adapterFactory) {
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
    public Collection getChildrenFeatures(Object object) {
        if (childrenFeatures == null) {
            super.getChildrenFeatures(object);
            childrenFeatures.add(GrammarPackage.Literals.SEQUENCE__SYNTAX);
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
	 * This returns Sequence.gif.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public Object getImage(Object object) {
        return overlayImage(object, getResourceLocator().getImage("full/obj16/Sequence"));
    }

    /**
	 * This returns the label text for the adapted class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public String getText(Object object) {
        Sequence sequence = (Sequence) object;
        return getString("_UI_Sequence_type") + " " + sequence.getStartLine();
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
        switch(notification.getFeatureID(Sequence.class)) {
            case GrammarPackage.SEQUENCE__SYNTAX:
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
    protected void collectNewChildDescriptors(Collection newChildDescriptors, Object object) {
        super.collectNewChildDescriptors(newChildDescriptors, object);
        newChildDescriptors.add(createChildParameter(GrammarPackage.Literals.SEQUENCE__SYNTAX, GrammarFactory.eINSTANCE.createLet()));
        newChildDescriptors.add(createChildParameter(GrammarPackage.Literals.SEQUENCE__SYNTAX, GrammarFactory.eINSTANCE.createExpressionStatement()));
        newChildDescriptors.add(createChildParameter(GrammarPackage.Literals.SEQUENCE__SYNTAX, GrammarFactory.eINSTANCE.createBlankSyntaxStatement()));
    }

    /**
	 * Return the resource locator for this item provider's resources.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public ResourceLocator getResourceLocator() {
        return GrammarEditPlugin.INSTANCE;
    }
}
