package org.deltaj.constraints.constraints.impl;

import org.deltaj.constraints.constraints.ConstraintsPackage;
import org.deltaj.constraints.constraints.Plus;
import org.deltaj.constraints.constraints.SimpleType;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Plus</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.deltaj.constraints.constraints.impl.PlusImpl#getLeft <em>Left</em>}</li>
 *   <li>{@link org.deltaj.constraints.constraints.impl.PlusImpl#getRight <em>Right</em>}</li>
 *   <li>{@link org.deltaj.constraints.constraints.impl.PlusImpl#getPlustype <em>Plustype</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class PlusImpl extends MethodConstraintImpl implements Plus {

    /**
   * The cached value of the '{@link #getLeft() <em>Left</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getLeft()
   * @generated
   * @ordered
   */
    protected SimpleType left;

    /**
   * The cached value of the '{@link #getRight() <em>Right</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getRight()
   * @generated
   * @ordered
   */
    protected SimpleType right;

    /**
   * The cached value of the '{@link #getPlustype() <em>Plustype</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getPlustype()
   * @generated
   * @ordered
   */
    protected SimpleType plustype;

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    protected PlusImpl() {
        super();
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    @Override
    protected EClass eStaticClass() {
        return ConstraintsPackage.Literals.PLUS;
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public SimpleType getLeft() {
        return left;
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public NotificationChain basicSetLeft(SimpleType newLeft, NotificationChain msgs) {
        SimpleType oldLeft = left;
        left = newLeft;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, ConstraintsPackage.PLUS__LEFT, oldLeft, newLeft);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public void setLeft(SimpleType newLeft) {
        if (newLeft != left) {
            NotificationChain msgs = null;
            if (left != null) msgs = ((InternalEObject) left).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - ConstraintsPackage.PLUS__LEFT, null, msgs);
            if (newLeft != null) msgs = ((InternalEObject) newLeft).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - ConstraintsPackage.PLUS__LEFT, null, msgs);
            msgs = basicSetLeft(newLeft, msgs);
            if (msgs != null) msgs.dispatch();
        } else if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, ConstraintsPackage.PLUS__LEFT, newLeft, newLeft));
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public SimpleType getRight() {
        return right;
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public NotificationChain basicSetRight(SimpleType newRight, NotificationChain msgs) {
        SimpleType oldRight = right;
        right = newRight;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, ConstraintsPackage.PLUS__RIGHT, oldRight, newRight);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public void setRight(SimpleType newRight) {
        if (newRight != right) {
            NotificationChain msgs = null;
            if (right != null) msgs = ((InternalEObject) right).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - ConstraintsPackage.PLUS__RIGHT, null, msgs);
            if (newRight != null) msgs = ((InternalEObject) newRight).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - ConstraintsPackage.PLUS__RIGHT, null, msgs);
            msgs = basicSetRight(newRight, msgs);
            if (msgs != null) msgs.dispatch();
        } else if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, ConstraintsPackage.PLUS__RIGHT, newRight, newRight));
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public SimpleType getPlustype() {
        return plustype;
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public NotificationChain basicSetPlustype(SimpleType newPlustype, NotificationChain msgs) {
        SimpleType oldPlustype = plustype;
        plustype = newPlustype;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, ConstraintsPackage.PLUS__PLUSTYPE, oldPlustype, newPlustype);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public void setPlustype(SimpleType newPlustype) {
        if (newPlustype != plustype) {
            NotificationChain msgs = null;
            if (plustype != null) msgs = ((InternalEObject) plustype).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - ConstraintsPackage.PLUS__PLUSTYPE, null, msgs);
            if (newPlustype != null) msgs = ((InternalEObject) newPlustype).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - ConstraintsPackage.PLUS__PLUSTYPE, null, msgs);
            msgs = basicSetPlustype(newPlustype, msgs);
            if (msgs != null) msgs.dispatch();
        } else if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, ConstraintsPackage.PLUS__PLUSTYPE, newPlustype, newPlustype));
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch(featureID) {
            case ConstraintsPackage.PLUS__LEFT:
                return basicSetLeft(null, msgs);
            case ConstraintsPackage.PLUS__RIGHT:
                return basicSetRight(null, msgs);
            case ConstraintsPackage.PLUS__PLUSTYPE:
                return basicSetPlustype(null, msgs);
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
            case ConstraintsPackage.PLUS__LEFT:
                return getLeft();
            case ConstraintsPackage.PLUS__RIGHT:
                return getRight();
            case ConstraintsPackage.PLUS__PLUSTYPE:
                return getPlustype();
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
            case ConstraintsPackage.PLUS__LEFT:
                setLeft((SimpleType) newValue);
                return;
            case ConstraintsPackage.PLUS__RIGHT:
                setRight((SimpleType) newValue);
                return;
            case ConstraintsPackage.PLUS__PLUSTYPE:
                setPlustype((SimpleType) newValue);
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
            case ConstraintsPackage.PLUS__LEFT:
                setLeft((SimpleType) null);
                return;
            case ConstraintsPackage.PLUS__RIGHT:
                setRight((SimpleType) null);
                return;
            case ConstraintsPackage.PLUS__PLUSTYPE:
                setPlustype((SimpleType) null);
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
            case ConstraintsPackage.PLUS__LEFT:
                return left != null;
            case ConstraintsPackage.PLUS__RIGHT:
                return right != null;
            case ConstraintsPackage.PLUS__PLUSTYPE:
                return plustype != null;
        }
        return super.eIsSet(featureID);
    }
}
