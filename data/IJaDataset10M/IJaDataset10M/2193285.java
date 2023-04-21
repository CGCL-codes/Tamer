package org.contract.ist.schema.ist.contract.impl;

import java.util.Collection;
import org.contract.ist.schema.ist.contract.ContractPackage;
import org.contract.ist.schema.ist.contract.ListOfWSDL;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.EObjectImpl;
import org.eclipse.emf.ecore.util.BasicFeatureMap;
import org.eclipse.emf.ecore.util.FeatureMap;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>List Of WSDL</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.contract.ist.schema.ist.contract.impl.ListOfWSDLImpl#getGroup <em>Group</em>}</li>
 *   <li>{@link org.contract.ist.schema.ist.contract.impl.ListOfWSDLImpl#getWSDLLink <em>WSDL Link</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class ListOfWSDLImpl extends EObjectImpl implements ListOfWSDL {

    /**
	 * The cached value of the '{@link #getGroup() <em>Group</em>}' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getGroup()
	 * @generated
	 * @ordered
	 */
    protected FeatureMap group;

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    protected ListOfWSDLImpl() {
        super();
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    protected EClass eStaticClass() {
        return ContractPackage.Literals.LIST_OF_WSDL;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public FeatureMap getGroup() {
        if (group == null) {
            group = new BasicFeatureMap(this, ContractPackage.LIST_OF_WSDL__GROUP);
        }
        return group;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EList<String> getWSDLLink() {
        return getGroup().list(ContractPackage.Literals.LIST_OF_WSDL__WSDL_LINK);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch(featureID) {
            case ContractPackage.LIST_OF_WSDL__GROUP:
                return ((InternalEList<?>) getGroup()).basicRemove(otherEnd, msgs);
        }
        return super.eInverseRemove(otherEnd, featureID, msgs);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch(featureID) {
            case ContractPackage.LIST_OF_WSDL__GROUP:
                if (coreType) return getGroup();
                return ((FeatureMap.Internal) getGroup()).getWrapper();
            case ContractPackage.LIST_OF_WSDL__WSDL_LINK:
                return getWSDLLink();
        }
        return super.eGet(featureID, resolve, coreType);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @SuppressWarnings("unchecked")
    @Override
    public void eSet(int featureID, Object newValue) {
        switch(featureID) {
            case ContractPackage.LIST_OF_WSDL__GROUP:
                ((FeatureMap.Internal) getGroup()).set(newValue);
                return;
            case ContractPackage.LIST_OF_WSDL__WSDL_LINK:
                getWSDLLink().clear();
                getWSDLLink().addAll((Collection<? extends String>) newValue);
                return;
        }
        super.eSet(featureID, newValue);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    public void eUnset(int featureID) {
        switch(featureID) {
            case ContractPackage.LIST_OF_WSDL__GROUP:
                getGroup().clear();
                return;
            case ContractPackage.LIST_OF_WSDL__WSDL_LINK:
                getWSDLLink().clear();
                return;
        }
        super.eUnset(featureID);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    public boolean eIsSet(int featureID) {
        switch(featureID) {
            case ContractPackage.LIST_OF_WSDL__GROUP:
                return group != null && !group.isEmpty();
            case ContractPackage.LIST_OF_WSDL__WSDL_LINK:
                return !getWSDLLink().isEmpty();
        }
        return super.eIsSet(featureID);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    public String toString() {
        if (eIsProxy()) return super.toString();
        StringBuffer result = new StringBuffer(super.toString());
        result.append(" (group: ");
        result.append(group);
        result.append(')');
        return result.toString();
    }
}
