package org.lcelb.accounts.manager.data.impl;

import java.util.Collection;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.util.EObjectResolvingEList;
import org.lcelb.accounts.manager.data.AbstractAccountType;
import org.lcelb.accounts.manager.data.AbstractOwner;
import org.lcelb.accounts.manager.data.Account;
import org.lcelb.accounts.manager.data.DataPackage;
import org.lcelb.accounts.manager.data.transaction.AbstractTransaction;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Account</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.lcelb.accounts.manager.data.impl.AccountImpl#getTransactions <em>Transactions</em>}</li>
 *   <li>{@link org.lcelb.accounts.manager.data.impl.AccountImpl#getOwner <em>Owner</em>}</li>
 *   <li>{@link org.lcelb.accounts.manager.data.impl.AccountImpl#getNumber <em>Number</em>}</li>
 *   <li>{@link org.lcelb.accounts.manager.data.impl.AccountImpl#getType <em>Type</em>}</li>
 *   <li>{@link org.lcelb.accounts.manager.data.impl.AccountImpl#isClosed <em>Closed</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class AccountImpl extends ModelElementWithIdImpl implements Account {

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public static final String copyright = "Copyright (c) 2007, 2009 La Carotte Et Le Baton.\r\nAll rights reserved. This program and the accompanying materials\r\nare made available under the terms of the Eclipse Public License v1.0\r\nwhich accompanies this distribution, and is available at\r\nhttp://www.eclipse.org/legal/epl-v10.html\r\n \r\n  Contributors:\r\n      La Carotte Et Le Baton - initial API and implementation";

    /**
   * The cached value of the '{@link #getTransactions() <em>Transactions</em>}' reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getTransactions()
   * @generated
   * @ordered
   */
    protected EList<AbstractTransaction> transactions;

    /**
   * The cached value of the '{@link #getOwner() <em>Owner</em>}' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getOwner()
   * @generated
   * @ordered
   */
    protected AbstractOwner owner;

    /**
   * The default value of the '{@link #getNumber() <em>Number</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getNumber()
   * @generated
   * @ordered
   */
    protected static final String NUMBER_EDEFAULT = null;

    /**
   * The cached value of the '{@link #getNumber() <em>Number</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getNumber()
   * @generated
   * @ordered
   */
    protected String number = NUMBER_EDEFAULT;

    /**
   * The cached value of the '{@link #getType() <em>Type</em>}' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getType()
   * @generated
   * @ordered
   */
    protected AbstractAccountType type;

    /**
   * The default value of the '{@link #isClosed() <em>Closed</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #isClosed()
   * @generated
   * @ordered
   */
    protected static final boolean CLOSED_EDEFAULT = false;

    /**
   * The flag representing the value of the '{@link #isClosed() <em>Closed</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #isClosed()
   * @generated
   * @ordered
   */
    protected static final int CLOSED_EFLAG = 1 << 0;

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    protected AccountImpl() {
        super();
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    @Override
    protected EClass eStaticClass() {
        return DataPackage.Literals.ACCOUNT;
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    @SuppressWarnings("unchecked")
    public EList<AbstractTransaction> getTransactions() {
        if (transactions == null) {
            transactions = new EObjectResolvingEList<AbstractTransaction>(AbstractTransaction.class, this, DataPackage.ACCOUNT__TRANSACTIONS);
        }
        return transactions;
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public AbstractOwner getOwner() {
        if (owner != null && owner.eIsProxy()) {
            InternalEObject oldOwner = (InternalEObject) owner;
            owner = (AbstractOwner) eResolveProxy(oldOwner);
            if (owner != oldOwner) {
                if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.RESOLVE, DataPackage.ACCOUNT__OWNER, oldOwner, owner));
            }
        }
        return owner;
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public AbstractOwner basicGetOwner() {
        return owner;
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public void setOwner(AbstractOwner newOwner) {
        AbstractOwner oldOwner = owner;
        owner = newOwner;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, DataPackage.ACCOUNT__OWNER, oldOwner, owner));
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public String getNumber() {
        return number;
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public void setNumber(String newNumber) {
        String oldNumber = number;
        number = newNumber;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, DataPackage.ACCOUNT__NUMBER, oldNumber, number));
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public AbstractAccountType getType() {
        if (type != null && type.eIsProxy()) {
            InternalEObject oldType = (InternalEObject) type;
            type = (AbstractAccountType) eResolveProxy(oldType);
            if (type != oldType) {
                if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.RESOLVE, DataPackage.ACCOUNT__TYPE, oldType, type));
            }
        }
        return type;
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public AbstractAccountType basicGetType() {
        return type;
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public void setType(AbstractAccountType newType) {
        AbstractAccountType oldType = type;
        type = newType;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, DataPackage.ACCOUNT__TYPE, oldType, type));
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public boolean isClosed() {
        return (flags & CLOSED_EFLAG) != 0;
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public void setClosed(boolean newClosed) {
        boolean oldClosed = (flags & CLOSED_EFLAG) != 0;
        if (newClosed) flags |= CLOSED_EFLAG; else flags &= ~CLOSED_EFLAG;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, DataPackage.ACCOUNT__CLOSED, oldClosed, newClosed));
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch(featureID) {
            case DataPackage.ACCOUNT__TRANSACTIONS:
                return getTransactions();
            case DataPackage.ACCOUNT__OWNER:
                if (resolve) return getOwner();
                return basicGetOwner();
            case DataPackage.ACCOUNT__NUMBER:
                return getNumber();
            case DataPackage.ACCOUNT__TYPE:
                if (resolve) return getType();
                return basicGetType();
            case DataPackage.ACCOUNT__CLOSED:
                return isClosed();
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
            case DataPackage.ACCOUNT__TRANSACTIONS:
                getTransactions().clear();
                getTransactions().addAll((Collection<? extends AbstractTransaction>) newValue);
                return;
            case DataPackage.ACCOUNT__OWNER:
                setOwner((AbstractOwner) newValue);
                return;
            case DataPackage.ACCOUNT__NUMBER:
                setNumber((String) newValue);
                return;
            case DataPackage.ACCOUNT__TYPE:
                setType((AbstractAccountType) newValue);
                return;
            case DataPackage.ACCOUNT__CLOSED:
                setClosed((Boolean) newValue);
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
            case DataPackage.ACCOUNT__TRANSACTIONS:
                getTransactions().clear();
                return;
            case DataPackage.ACCOUNT__OWNER:
                setOwner((AbstractOwner) null);
                return;
            case DataPackage.ACCOUNT__NUMBER:
                setNumber(NUMBER_EDEFAULT);
                return;
            case DataPackage.ACCOUNT__TYPE:
                setType((AbstractAccountType) null);
                return;
            case DataPackage.ACCOUNT__CLOSED:
                setClosed(CLOSED_EDEFAULT);
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
            case DataPackage.ACCOUNT__TRANSACTIONS:
                return transactions != null && !transactions.isEmpty();
            case DataPackage.ACCOUNT__OWNER:
                return owner != null;
            case DataPackage.ACCOUNT__NUMBER:
                return NUMBER_EDEFAULT == null ? number != null : !NUMBER_EDEFAULT.equals(number);
            case DataPackage.ACCOUNT__TYPE:
                return type != null;
            case DataPackage.ACCOUNT__CLOSED:
                return ((flags & CLOSED_EFLAG) != 0) != CLOSED_EDEFAULT;
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
        result.append(" (number: ");
        result.append(number);
        result.append(", closed: ");
        result.append((flags & CLOSED_EFLAG) != 0);
        result.append(')');
        return result.toString();
    }
}
