package edu.asu.vogon.embryo.embryomodel.provider;

import edu.asu.vogon.embryo.embryomodel.EmbryoMetadata;
import edu.asu.vogon.embryo.embryomodel.EmbryomodelPackage;
import edu.asu.vogon.model.provider.TextMetadataItemProvider;
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
import org.eclipse.emf.edit.provider.ViewerNotification;

/**
 * This is the item provider adapter for a {@link edu.asu.vogon.embryo.embryomodel.EmbryoMetadata} object.
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 * @generated
 */
public class EmbryoMetadataItemProvider extends TextMetadataItemProvider implements IEditingDomainItemProvider, IStructuredItemContentProvider, ITreeItemContentProvider, IItemLabelProvider, IItemPropertySource {

    /**
	 * This constructs an instance from a factory and a notifier.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EmbryoMetadataItemProvider(AdapterFactory adapterFactory) {
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
            addPidPropertyDescriptor(object);
            addCreatorPropertyDescriptor(object);
            addDescriptionPropertyDescriptor(object);
            addSubjectFieldPropertyDescriptor(object);
            addSubjectMetatagsPropertyDescriptor(object);
            addSubjectCategoryPropertyDescriptor(object);
            addKeywordsPropertyDescriptor(object);
        }
        return itemPropertyDescriptors;
    }

    /**
	 * This adds a property descriptor for the Pid feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    protected void addPidPropertyDescriptor(Object object) {
        itemPropertyDescriptors.add(createItemPropertyDescriptor(((ComposeableAdapterFactory) adapterFactory).getRootAdapterFactory(), getResourceLocator(), getString("_UI_EmbryoMetadata_pid_feature"), getString("_UI_PropertyDescriptor_description", "_UI_EmbryoMetadata_pid_feature", "_UI_EmbryoMetadata_type"), EmbryomodelPackage.Literals.EMBRYO_METADATA__PID, true, false, false, ItemPropertyDescriptor.GENERIC_VALUE_IMAGE, null, null));
    }

    /**
	 * This adds a property descriptor for the Creator feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    protected void addCreatorPropertyDescriptor(Object object) {
        itemPropertyDescriptors.add(createItemPropertyDescriptor(((ComposeableAdapterFactory) adapterFactory).getRootAdapterFactory(), getResourceLocator(), getString("_UI_EmbryoMetadata_creator_feature"), getString("_UI_PropertyDescriptor_description", "_UI_EmbryoMetadata_creator_feature", "_UI_EmbryoMetadata_type"), EmbryomodelPackage.Literals.EMBRYO_METADATA__CREATOR, true, false, false, ItemPropertyDescriptor.GENERIC_VALUE_IMAGE, null, null));
    }

    /**
	 * This adds a property descriptor for the Description feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    protected void addDescriptionPropertyDescriptor(Object object) {
        itemPropertyDescriptors.add(createItemPropertyDescriptor(((ComposeableAdapterFactory) adapterFactory).getRootAdapterFactory(), getResourceLocator(), getString("_UI_EmbryoMetadata_description_feature"), getString("_UI_PropertyDescriptor_description", "_UI_EmbryoMetadata_description_feature", "_UI_EmbryoMetadata_type"), EmbryomodelPackage.Literals.EMBRYO_METADATA__DESCRIPTION, true, false, false, ItemPropertyDescriptor.GENERIC_VALUE_IMAGE, null, null));
    }

    /**
	 * This adds a property descriptor for the Subject Field feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    protected void addSubjectFieldPropertyDescriptor(Object object) {
        itemPropertyDescriptors.add(createItemPropertyDescriptor(((ComposeableAdapterFactory) adapterFactory).getRootAdapterFactory(), getResourceLocator(), getString("_UI_EmbryoMetadata_subjectField_feature"), getString("_UI_PropertyDescriptor_description", "_UI_EmbryoMetadata_subjectField_feature", "_UI_EmbryoMetadata_type"), EmbryomodelPackage.Literals.EMBRYO_METADATA__SUBJECT_FIELD, true, false, false, ItemPropertyDescriptor.GENERIC_VALUE_IMAGE, null, null));
    }

    /**
	 * This adds a property descriptor for the Subject Metatags feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    protected void addSubjectMetatagsPropertyDescriptor(Object object) {
        itemPropertyDescriptors.add(createItemPropertyDescriptor(((ComposeableAdapterFactory) adapterFactory).getRootAdapterFactory(), getResourceLocator(), getString("_UI_EmbryoMetadata_subjectMetatags_feature"), getString("_UI_PropertyDescriptor_description", "_UI_EmbryoMetadata_subjectMetatags_feature", "_UI_EmbryoMetadata_type"), EmbryomodelPackage.Literals.EMBRYO_METADATA__SUBJECT_METATAGS, true, false, false, ItemPropertyDescriptor.GENERIC_VALUE_IMAGE, null, null));
    }

    /**
	 * This adds a property descriptor for the Subject Category feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    protected void addSubjectCategoryPropertyDescriptor(Object object) {
        itemPropertyDescriptors.add(createItemPropertyDescriptor(((ComposeableAdapterFactory) adapterFactory).getRootAdapterFactory(), getResourceLocator(), getString("_UI_EmbryoMetadata_subjectCategory_feature"), getString("_UI_PropertyDescriptor_description", "_UI_EmbryoMetadata_subjectCategory_feature", "_UI_EmbryoMetadata_type"), EmbryomodelPackage.Literals.EMBRYO_METADATA__SUBJECT_CATEGORY, true, false, false, ItemPropertyDescriptor.GENERIC_VALUE_IMAGE, null, null));
    }

    /**
	 * This adds a property descriptor for the Keywords feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    protected void addKeywordsPropertyDescriptor(Object object) {
        itemPropertyDescriptors.add(createItemPropertyDescriptor(((ComposeableAdapterFactory) adapterFactory).getRootAdapterFactory(), getResourceLocator(), getString("_UI_EmbryoMetadata_keywords_feature"), getString("_UI_PropertyDescriptor_description", "_UI_EmbryoMetadata_keywords_feature", "_UI_EmbryoMetadata_type"), EmbryomodelPackage.Literals.EMBRYO_METADATA__KEYWORDS, true, false, false, ItemPropertyDescriptor.GENERIC_VALUE_IMAGE, null, null));
    }

    /**
	 * This returns EmbryoMetadata.gif.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    public Object getImage(Object object) {
        return overlayImage(object, getResourceLocator().getImage("full/obj16/EmbryoMetadata"));
    }

    /**
	 * This returns the label text for the adapted class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    public String getText(Object object) {
        String label = ((EmbryoMetadata) object).getTitle();
        return label == null || label.length() == 0 ? getString("_UI_EmbryoMetadata_type") : getString("_UI_EmbryoMetadata_type") + " " + label;
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
        switch(notification.getFeatureID(EmbryoMetadata.class)) {
            case EmbryomodelPackage.EMBRYO_METADATA__PID:
            case EmbryomodelPackage.EMBRYO_METADATA__CREATOR:
            case EmbryomodelPackage.EMBRYO_METADATA__DESCRIPTION:
            case EmbryomodelPackage.EMBRYO_METADATA__SUBJECT_FIELD:
            case EmbryomodelPackage.EMBRYO_METADATA__SUBJECT_METATAGS:
            case EmbryomodelPackage.EMBRYO_METADATA__SUBJECT_CATEGORY:
            case EmbryomodelPackage.EMBRYO_METADATA__KEYWORDS:
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
        return EmbryomodelEditPlugin.INSTANCE;
    }
}
