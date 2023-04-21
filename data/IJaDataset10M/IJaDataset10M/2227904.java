package org.pubcurator.model.projectstore.impl;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.internal.cdo.CDOObjectImpl;
import org.pubcurator.model.projectstore.PubFillerType;
import org.pubcurator.model.projectstore.PubFormElement;
import org.pubcurator.model.projectstore.PubGraphElement;
import org.pubcurator.model.projectstore.PubProjectStorePackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Pub Form Element</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.pubcurator.model.projectstore.impl.PubFormElementImpl#getName <em>Name</em>}</li>
 *   <li>{@link org.pubcurator.model.projectstore.impl.PubFormElementImpl#getDescription <em>Description</em>}</li>
 *   <li>{@link org.pubcurator.model.projectstore.impl.PubFormElementImpl#getLink <em>Link</em>}</li>
 *   <li>{@link org.pubcurator.model.projectstore.impl.PubFormElementImpl#getFillerType <em>Filler Type</em>}</li>
 *   <li>{@link org.pubcurator.model.projectstore.impl.PubFormElementImpl#getConstantValue <em>Constant Value</em>}</li>
 *   <li>{@link org.pubcurator.model.projectstore.impl.PubFormElementImpl#getIdentifierTypeNames <em>Identifier Type Names</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public abstract class PubFormElementImpl extends CDOObjectImpl implements PubFormElement {

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    protected PubFormElementImpl() {
        super();
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    protected EClass eStaticClass() {
        return PubProjectStorePackage.Literals.PUB_FORM_ELEMENT;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    protected int eStaticFeatureCount() {
        return 0;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public String getName() {
        return (String) eGet(PubProjectStorePackage.Literals.PUB_FORM_ELEMENT__NAME, true);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setName(String newName) {
        eSet(PubProjectStorePackage.Literals.PUB_FORM_ELEMENT__NAME, newName);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public String getDescription() {
        return (String) eGet(PubProjectStorePackage.Literals.PUB_FORM_ELEMENT__DESCRIPTION, true);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setDescription(String newDescription) {
        eSet(PubProjectStorePackage.Literals.PUB_FORM_ELEMENT__DESCRIPTION, newDescription);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public PubGraphElement getLink() {
        return (PubGraphElement) eGet(PubProjectStorePackage.Literals.PUB_FORM_ELEMENT__LINK, true);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setLink(PubGraphElement newLink) {
        eSet(PubProjectStorePackage.Literals.PUB_FORM_ELEMENT__LINK, newLink);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public PubFillerType getFillerType() {
        return (PubFillerType) eGet(PubProjectStorePackage.Literals.PUB_FORM_ELEMENT__FILLER_TYPE, true);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setFillerType(PubFillerType newFillerType) {
        eSet(PubProjectStorePackage.Literals.PUB_FORM_ELEMENT__FILLER_TYPE, newFillerType);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public String getConstantValue() {
        return (String) eGet(PubProjectStorePackage.Literals.PUB_FORM_ELEMENT__CONSTANT_VALUE, true);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setConstantValue(String newConstantValue) {
        eSet(PubProjectStorePackage.Literals.PUB_FORM_ELEMENT__CONSTANT_VALUE, newConstantValue);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @SuppressWarnings("unchecked")
    public EList<String> getIdentifierTypeNames() {
        return (EList<String>) eGet(PubProjectStorePackage.Literals.PUB_FORM_ELEMENT__IDENTIFIER_TYPE_NAMES, true);
    }
}
