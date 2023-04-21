package org.eclipse.epf.uma._1._0.impl;

import java.util.Collection;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.util.BasicFeatureMap;
import org.eclipse.emf.ecore.util.FeatureMap;
import org.eclipse.emf.ecore.util.InternalEList;
import org.eclipse.epf.uma._1._0.Domain;
import org.eclipse.epf.uma._1._0._0Package;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Domain</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.epf.uma._1._0.impl.DomainImpl#getGroup2 <em>Group2</em>}</li>
 *   <li>{@link org.eclipse.epf.uma._1._0.impl.DomainImpl#getWorkProduct <em>Work Product</em>}</li>
 *   <li>{@link org.eclipse.epf.uma._1._0.impl.DomainImpl#getSubdomain <em>Subdomain</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class DomainImpl extends ContentCategoryImpl implements Domain {

    /**
	 * The cached value of the '{@link #getGroup2() <em>Group2</em>}' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getGroup2()
	 * @generated
	 * @ordered
	 */
    protected FeatureMap group2;

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    protected DomainImpl() {
        super();
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    protected EClass eStaticClass() {
        return _0Package.Literals.DOMAIN;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public FeatureMap getGroup2() {
        if (group2 == null) {
            group2 = new BasicFeatureMap(this, _0Package.DOMAIN__GROUP2);
        }
        return group2;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EList<String> getWorkProduct() {
        return getGroup2().list(_0Package.Literals.DOMAIN__WORK_PRODUCT);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EList<Domain> getSubdomain() {
        return getGroup2().list(_0Package.Literals.DOMAIN__SUBDOMAIN);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch(featureID) {
            case _0Package.DOMAIN__GROUP2:
                return ((InternalEList<?>) getGroup2()).basicRemove(otherEnd, msgs);
            case _0Package.DOMAIN__SUBDOMAIN:
                return ((InternalEList<?>) getSubdomain()).basicRemove(otherEnd, msgs);
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
            case _0Package.DOMAIN__GROUP2:
                if (coreType) return getGroup2();
                return ((FeatureMap.Internal) getGroup2()).getWrapper();
            case _0Package.DOMAIN__WORK_PRODUCT:
                return getWorkProduct();
            case _0Package.DOMAIN__SUBDOMAIN:
                return getSubdomain();
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
            case _0Package.DOMAIN__GROUP2:
                ((FeatureMap.Internal) getGroup2()).set(newValue);
                return;
            case _0Package.DOMAIN__WORK_PRODUCT:
                getWorkProduct().clear();
                getWorkProduct().addAll((Collection<? extends String>) newValue);
                return;
            case _0Package.DOMAIN__SUBDOMAIN:
                getSubdomain().clear();
                getSubdomain().addAll((Collection<? extends Domain>) newValue);
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
            case _0Package.DOMAIN__GROUP2:
                getGroup2().clear();
                return;
            case _0Package.DOMAIN__WORK_PRODUCT:
                getWorkProduct().clear();
                return;
            case _0Package.DOMAIN__SUBDOMAIN:
                getSubdomain().clear();
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
            case _0Package.DOMAIN__GROUP2:
                return group2 != null && !group2.isEmpty();
            case _0Package.DOMAIN__WORK_PRODUCT:
                return !getWorkProduct().isEmpty();
            case _0Package.DOMAIN__SUBDOMAIN:
                return !getSubdomain().isEmpty();
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
        result.append(" (group2: ");
        result.append(group2);
        result.append(')');
        return result.toString();
    }
}
