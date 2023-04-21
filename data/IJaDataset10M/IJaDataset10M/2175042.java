package org.lcelb.accounts.manager.data.impl;

import java.util.Collection;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.EObjectResolvingEList;
import org.eclipse.emf.ecore.util.InternalEList;
import org.lcelb.accounts.manager.data.AbstractOwner;
import org.lcelb.accounts.manager.data.Bank;
import org.lcelb.accounts.manager.data.DataPackage;
import org.lcelb.accounts.manager.data.transaction.category.AbstractCategory;
import org.lcelb.accounts.manager.data.transaction.payment.AbstractPayment;
import org.lcelb.accounts.manager.data.transaction.validity.AbstractValidity;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Abstract Owner</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.lcelb.accounts.manager.data.impl.AbstractOwnerImpl#getName <em>Name</em>}</li>
 *   <li>{@link org.lcelb.accounts.manager.data.impl.AbstractOwnerImpl#getBanks <em>Banks</em>}</li>
 *   <li>{@link org.lcelb.accounts.manager.data.impl.AbstractOwnerImpl#getCategories <em>Categories</em>}</li>
 *   <li>{@link org.lcelb.accounts.manager.data.impl.AbstractOwnerImpl#getPayments <em>Payments</em>}</li>
 *   <li>{@link org.lcelb.accounts.manager.data.impl.AbstractOwnerImpl#getValidities <em>Validities</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public abstract class AbstractOwnerImpl extends ModelElementWithIdImpl implements AbstractOwner {

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public static final String copyright = "Copyright (c) 2007, 2009 La Carotte Et Le Baton.\r\nAll rights reserved. This program and the accompanying materials\r\nare made available under the terms of the Eclipse Public License v1.0\r\nwhich accompanies this distribution, and is available at\r\nhttp://www.eclipse.org/legal/epl-v10.html\r\n \r\n  Contributors:\r\n      La Carotte Et Le Baton - initial API and implementation";

    /**
   * The default value of the '{@link #getName() <em>Name</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getName()
   * @generated
   * @ordered
   */
    protected static final String NAME_EDEFAULT = null;

    /**
   * The cached value of the '{@link #getName() <em>Name</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getName()
   * @generated
   * @ordered
   */
    protected String name = NAME_EDEFAULT;

    /**
   * The cached value of the '{@link #getBanks() <em>Banks</em>}' reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getBanks()
   * @generated
   * @ordered
   */
    protected EList<Bank> banks;

    /**
   * The cached value of the '{@link #getCategories() <em>Categories</em>}' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getCategories()
   * @generated
   * @ordered
   */
    protected EList<AbstractCategory> categories;

    /**
   * The cached value of the '{@link #getPayments() <em>Payments</em>}' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getPayments()
   * @generated
   * @ordered
   */
    protected EList<AbstractPayment> payments;

    /**
   * The cached value of the '{@link #getValidities() <em>Validities</em>}' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getValidities()
   * @generated
   * @ordered
   */
    protected EList<AbstractValidity> validities;

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    protected AbstractOwnerImpl() {
        super();
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    @Override
    protected EClass eStaticClass() {
        return DataPackage.Literals.ABSTRACT_OWNER;
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    @Override
    public String getName() {
        return name;
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public void setName(String newName) {
        String oldName = name;
        name = newName;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, DataPackage.ABSTRACT_OWNER__NAME, oldName, name));
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    @SuppressWarnings("unchecked")
    public EList<Bank> getBanks() {
        if (banks == null) {
            banks = new EObjectResolvingEList<Bank>(Bank.class, this, DataPackage.ABSTRACT_OWNER__BANKS);
        }
        return banks;
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    @SuppressWarnings("unchecked")
    public EList<AbstractCategory> getCategories() {
        if (categories == null) {
            categories = new EObjectContainmentEList.Resolving<AbstractCategory>(AbstractCategory.class, this, DataPackage.ABSTRACT_OWNER__CATEGORIES);
        }
        return categories;
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    @SuppressWarnings("unchecked")
    public EList<AbstractPayment> getPayments() {
        if (payments == null) {
            payments = new EObjectContainmentEList.Resolving<AbstractPayment>(AbstractPayment.class, this, DataPackage.ABSTRACT_OWNER__PAYMENTS);
        }
        return payments;
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    @SuppressWarnings("unchecked")
    public EList<AbstractValidity> getValidities() {
        if (validities == null) {
            validities = new EObjectContainmentEList.Resolving<AbstractValidity>(AbstractValidity.class, this, DataPackage.ABSTRACT_OWNER__VALIDITIES);
        }
        return validities;
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch(featureID) {
            case DataPackage.ABSTRACT_OWNER__CATEGORIES:
                return ((InternalEList<?>) getCategories()).basicRemove(otherEnd, msgs);
            case DataPackage.ABSTRACT_OWNER__PAYMENTS:
                return ((InternalEList<?>) getPayments()).basicRemove(otherEnd, msgs);
            case DataPackage.ABSTRACT_OWNER__VALIDITIES:
                return ((InternalEList<?>) getValidities()).basicRemove(otherEnd, msgs);
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
            case DataPackage.ABSTRACT_OWNER__NAME:
                return getName();
            case DataPackage.ABSTRACT_OWNER__BANKS:
                return getBanks();
            case DataPackage.ABSTRACT_OWNER__CATEGORIES:
                return getCategories();
            case DataPackage.ABSTRACT_OWNER__PAYMENTS:
                return getPayments();
            case DataPackage.ABSTRACT_OWNER__VALIDITIES:
                return getValidities();
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
            case DataPackage.ABSTRACT_OWNER__NAME:
                setName((String) newValue);
                return;
            case DataPackage.ABSTRACT_OWNER__BANKS:
                getBanks().clear();
                getBanks().addAll((Collection<? extends Bank>) newValue);
                return;
            case DataPackage.ABSTRACT_OWNER__CATEGORIES:
                getCategories().clear();
                getCategories().addAll((Collection<? extends AbstractCategory>) newValue);
                return;
            case DataPackage.ABSTRACT_OWNER__PAYMENTS:
                getPayments().clear();
                getPayments().addAll((Collection<? extends AbstractPayment>) newValue);
                return;
            case DataPackage.ABSTRACT_OWNER__VALIDITIES:
                getValidities().clear();
                getValidities().addAll((Collection<? extends AbstractValidity>) newValue);
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
            case DataPackage.ABSTRACT_OWNER__NAME:
                setName(NAME_EDEFAULT);
                return;
            case DataPackage.ABSTRACT_OWNER__BANKS:
                getBanks().clear();
                return;
            case DataPackage.ABSTRACT_OWNER__CATEGORIES:
                getCategories().clear();
                return;
            case DataPackage.ABSTRACT_OWNER__PAYMENTS:
                getPayments().clear();
                return;
            case DataPackage.ABSTRACT_OWNER__VALIDITIES:
                getValidities().clear();
                return;
        }
        super.eUnset(featureID);
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    @SuppressWarnings("unchecked")
    @Override
    public boolean eIsSet(int featureID) {
        switch(featureID) {
            case DataPackage.ABSTRACT_OWNER__NAME:
                return NAME_EDEFAULT == null ? name != null : !NAME_EDEFAULT.equals(name);
            case DataPackage.ABSTRACT_OWNER__BANKS:
                return banks != null && !banks.isEmpty();
            case DataPackage.ABSTRACT_OWNER__CATEGORIES:
                return categories != null && !categories.isEmpty();
            case DataPackage.ABSTRACT_OWNER__PAYMENTS:
                return payments != null && !payments.isEmpty();
            case DataPackage.ABSTRACT_OWNER__VALIDITIES:
                return validities != null && !validities.isEmpty();
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
        result.append(" (name: ");
        result.append(name);
        result.append(')');
        return result.toString();
    }
}
