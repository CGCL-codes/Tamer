package org.lcelb.accounts.manager.data.impl;

import java.util.Collection;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;
import org.lcelb.accounts.manager.data.AbstractOwner;
import org.lcelb.accounts.manager.data.AccountManager;
import org.lcelb.accounts.manager.data.Bank;
import org.lcelb.accounts.manager.data.DataPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Account Manager</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.lcelb.accounts.manager.data.impl.AccountManagerImpl#getBanks <em>Banks</em>}</li>
 *   <li>{@link org.lcelb.accounts.manager.data.impl.AccountManagerImpl#getOwners <em>Owners</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class AccountManagerImpl extends ModelElementWithIdImpl implements AccountManager {

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public static final String copyright = "Copyright (c) 2007, 2009 La Carotte Et Le Baton.\r\nAll rights reserved. This program and the accompanying materials\r\nare made available under the terms of the Eclipse Public License v1.0\r\nwhich accompanies this distribution, and is available at\r\nhttp://www.eclipse.org/legal/epl-v10.html\r\n \r\n  Contributors:\r\n      La Carotte Et Le Baton - initial API and implementation";

    /**
   * The cached value of the '{@link #getBanks() <em>Banks</em>}' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getBanks()
   * @generated
   * @ordered
   */
    protected EList<Bank> banks;

    /**
   * The cached value of the '{@link #getOwners() <em>Owners</em>}' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getOwners()
   * @generated
   * @ordered
   */
    protected EList<AbstractOwner> owners;

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    protected AccountManagerImpl() {
        super();
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    @Override
    protected EClass eStaticClass() {
        return DataPackage.Literals.ACCOUNT_MANAGER;
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    @SuppressWarnings("unchecked")
    public EList<Bank> getBanks() {
        if (banks == null) {
            banks = new EObjectContainmentEList.Resolving<Bank>(Bank.class, this, DataPackage.ACCOUNT_MANAGER__BANKS);
        }
        return banks;
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    @SuppressWarnings("unchecked")
    public EList<AbstractOwner> getOwners() {
        if (owners == null) {
            owners = new EObjectContainmentEList.Resolving<AbstractOwner>(AbstractOwner.class, this, DataPackage.ACCOUNT_MANAGER__OWNERS);
        }
        return owners;
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch(featureID) {
            case DataPackage.ACCOUNT_MANAGER__BANKS:
                return ((InternalEList<?>) getBanks()).basicRemove(otherEnd, msgs);
            case DataPackage.ACCOUNT_MANAGER__OWNERS:
                return ((InternalEList<?>) getOwners()).basicRemove(otherEnd, msgs);
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
            case DataPackage.ACCOUNT_MANAGER__BANKS:
                return getBanks();
            case DataPackage.ACCOUNT_MANAGER__OWNERS:
                return getOwners();
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
            case DataPackage.ACCOUNT_MANAGER__BANKS:
                getBanks().clear();
                getBanks().addAll((Collection<? extends Bank>) newValue);
                return;
            case DataPackage.ACCOUNT_MANAGER__OWNERS:
                getOwners().clear();
                getOwners().addAll((Collection<? extends AbstractOwner>) newValue);
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
            case DataPackage.ACCOUNT_MANAGER__BANKS:
                getBanks().clear();
                return;
            case DataPackage.ACCOUNT_MANAGER__OWNERS:
                getOwners().clear();
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
            case DataPackage.ACCOUNT_MANAGER__BANKS:
                return banks != null && !banks.isEmpty();
            case DataPackage.ACCOUNT_MANAGER__OWNERS:
                return owners != null && !owners.isEmpty();
        }
        return super.eIsSet(featureID);
    }
}
