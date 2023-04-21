package fr.msimeon.mads.mads.impl;

import fr.msimeon.mads.mads.AtomID;
import fr.msimeon.mads.mads.MadsPackage;
import fr.msimeon.mads.mads.PurchFunc;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Purch Func</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link fr.msimeon.mads.mads.impl.PurchFuncImpl#getItemConsRef <em>Item Cons Ref</em>}</li>
 *   <li>{@link fr.msimeon.mads.mads.impl.PurchFuncImpl#getItemProdRef <em>Item Prod Ref</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class PurchFuncImpl extends FunctionImpl implements PurchFunc {

    /**
   * The cached value of the '{@link #getItemConsRef() <em>Item Cons Ref</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getItemConsRef()
   * @generated
   * @ordered
   */
    protected AtomID itemConsRef;

    /**
   * The cached value of the '{@link #getItemProdRef() <em>Item Prod Ref</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getItemProdRef()
   * @generated
   * @ordered
   */
    protected AtomID itemProdRef;

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    protected PurchFuncImpl() {
        super();
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    @Override
    protected EClass eStaticClass() {
        return MadsPackage.Literals.PURCH_FUNC;
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public AtomID getItemConsRef() {
        return itemConsRef;
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public NotificationChain basicSetItemConsRef(AtomID newItemConsRef, NotificationChain msgs) {
        AtomID oldItemConsRef = itemConsRef;
        itemConsRef = newItemConsRef;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, MadsPackage.PURCH_FUNC__ITEM_CONS_REF, oldItemConsRef, newItemConsRef);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public void setItemConsRef(AtomID newItemConsRef) {
        if (newItemConsRef != itemConsRef) {
            NotificationChain msgs = null;
            if (itemConsRef != null) msgs = ((InternalEObject) itemConsRef).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - MadsPackage.PURCH_FUNC__ITEM_CONS_REF, null, msgs);
            if (newItemConsRef != null) msgs = ((InternalEObject) newItemConsRef).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - MadsPackage.PURCH_FUNC__ITEM_CONS_REF, null, msgs);
            msgs = basicSetItemConsRef(newItemConsRef, msgs);
            if (msgs != null) msgs.dispatch();
        } else if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, MadsPackage.PURCH_FUNC__ITEM_CONS_REF, newItemConsRef, newItemConsRef));
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public AtomID getItemProdRef() {
        return itemProdRef;
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public NotificationChain basicSetItemProdRef(AtomID newItemProdRef, NotificationChain msgs) {
        AtomID oldItemProdRef = itemProdRef;
        itemProdRef = newItemProdRef;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, MadsPackage.PURCH_FUNC__ITEM_PROD_REF, oldItemProdRef, newItemProdRef);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public void setItemProdRef(AtomID newItemProdRef) {
        if (newItemProdRef != itemProdRef) {
            NotificationChain msgs = null;
            if (itemProdRef != null) msgs = ((InternalEObject) itemProdRef).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - MadsPackage.PURCH_FUNC__ITEM_PROD_REF, null, msgs);
            if (newItemProdRef != null) msgs = ((InternalEObject) newItemProdRef).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - MadsPackage.PURCH_FUNC__ITEM_PROD_REF, null, msgs);
            msgs = basicSetItemProdRef(newItemProdRef, msgs);
            if (msgs != null) msgs.dispatch();
        } else if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, MadsPackage.PURCH_FUNC__ITEM_PROD_REF, newItemProdRef, newItemProdRef));
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch(featureID) {
            case MadsPackage.PURCH_FUNC__ITEM_CONS_REF:
                return basicSetItemConsRef(null, msgs);
            case MadsPackage.PURCH_FUNC__ITEM_PROD_REF:
                return basicSetItemProdRef(null, msgs);
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
            case MadsPackage.PURCH_FUNC__ITEM_CONS_REF:
                return getItemConsRef();
            case MadsPackage.PURCH_FUNC__ITEM_PROD_REF:
                return getItemProdRef();
        }
        return super.eGet(featureID, resolve, coreType);
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    @Override
    public void eSet(int featureID, Object newValue) {
        switch(featureID) {
            case MadsPackage.PURCH_FUNC__ITEM_CONS_REF:
                setItemConsRef((AtomID) newValue);
                return;
            case MadsPackage.PURCH_FUNC__ITEM_PROD_REF:
                setItemProdRef((AtomID) newValue);
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
            case MadsPackage.PURCH_FUNC__ITEM_CONS_REF:
                setItemConsRef((AtomID) null);
                return;
            case MadsPackage.PURCH_FUNC__ITEM_PROD_REF:
                setItemProdRef((AtomID) null);
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
            case MadsPackage.PURCH_FUNC__ITEM_CONS_REF:
                return itemConsRef != null;
            case MadsPackage.PURCH_FUNC__ITEM_PROD_REF:
                return itemProdRef != null;
        }
        return super.eIsSet(featureID);
    }
}
