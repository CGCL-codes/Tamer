package org.eclipse.xtext.example.fj.impl;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.xtext.example.fj.Cast;
import org.eclipse.xtext.example.fj.ClassType;
import org.eclipse.xtext.example.fj.Expression;
import org.eclipse.xtext.example.fj.FjPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Cast</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.xtext.example.fj.impl.CastImpl#getType <em>Type</em>}</li>
 *   <li>{@link org.eclipse.xtext.example.fj.impl.CastImpl#getObject <em>Object</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class CastImpl extends ExpressionImpl implements Cast {

    /**
   * The cached value of the '{@link #getType() <em>Type</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getType()
   * @generated
   * @ordered
   */
    protected ClassType type;

    /**
   * The cached value of the '{@link #getObject() <em>Object</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getObject()
   * @generated
   * @ordered
   */
    protected Expression object;

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    protected CastImpl() {
        super();
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    @Override
    protected EClass eStaticClass() {
        return FjPackage.Literals.CAST;
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public ClassType getType() {
        return type;
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public NotificationChain basicSetType(ClassType newType, NotificationChain msgs) {
        ClassType oldType = type;
        type = newType;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, FjPackage.CAST__TYPE, oldType, newType);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public void setType(ClassType newType) {
        if (newType != type) {
            NotificationChain msgs = null;
            if (type != null) msgs = ((InternalEObject) type).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - FjPackage.CAST__TYPE, null, msgs);
            if (newType != null) msgs = ((InternalEObject) newType).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - FjPackage.CAST__TYPE, null, msgs);
            msgs = basicSetType(newType, msgs);
            if (msgs != null) msgs.dispatch();
        } else if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, FjPackage.CAST__TYPE, newType, newType));
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public Expression getObject() {
        return object;
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public NotificationChain basicSetObject(Expression newObject, NotificationChain msgs) {
        Expression oldObject = object;
        object = newObject;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, FjPackage.CAST__OBJECT, oldObject, newObject);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public void setObject(Expression newObject) {
        if (newObject != object) {
            NotificationChain msgs = null;
            if (object != null) msgs = ((InternalEObject) object).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - FjPackage.CAST__OBJECT, null, msgs);
            if (newObject != null) msgs = ((InternalEObject) newObject).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - FjPackage.CAST__OBJECT, null, msgs);
            msgs = basicSetObject(newObject, msgs);
            if (msgs != null) msgs.dispatch();
        } else if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, FjPackage.CAST__OBJECT, newObject, newObject));
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch(featureID) {
            case FjPackage.CAST__TYPE:
                return basicSetType(null, msgs);
            case FjPackage.CAST__OBJECT:
                return basicSetObject(null, msgs);
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
            case FjPackage.CAST__TYPE:
                return getType();
            case FjPackage.CAST__OBJECT:
                return getObject();
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
            case FjPackage.CAST__TYPE:
                setType((ClassType) newValue);
                return;
            case FjPackage.CAST__OBJECT:
                setObject((Expression) newValue);
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
            case FjPackage.CAST__TYPE:
                setType((ClassType) null);
                return;
            case FjPackage.CAST__OBJECT:
                setObject((Expression) null);
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
            case FjPackage.CAST__TYPE:
                return type != null;
            case FjPackage.CAST__OBJECT:
                return object != null;
        }
        return super.eIsSet(featureID);
    }
}
