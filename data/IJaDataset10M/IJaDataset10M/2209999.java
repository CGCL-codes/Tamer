package org.hl7.v3.impl;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.hl7.v3.HXITPQ;
import org.hl7.v3.IVLTS;
import org.hl7.v3.V3Package;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>HXITPQ</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.hl7.v3.impl.HXITPQImpl#getValidTime <em>Valid Time</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class HXITPQImpl extends PQImpl implements HXITPQ {

    /**
	 * The cached value of the '{@link #getValidTime() <em>Valid Time</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getValidTime()
	 * @generated
	 * @ordered
	 */
    protected IVLTS validTime;

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    protected HXITPQImpl() {
        super();
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    protected EClass eStaticClass() {
        return V3Package.eINSTANCE.getHXITPQ();
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public IVLTS getValidTime() {
        return validTime;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public NotificationChain basicSetValidTime(IVLTS newValidTime, NotificationChain msgs) {
        IVLTS oldValidTime = validTime;
        validTime = newValidTime;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, V3Package.HXITPQ__VALID_TIME, oldValidTime, newValidTime);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setValidTime(IVLTS newValidTime) {
        if (newValidTime != validTime) {
            NotificationChain msgs = null;
            if (validTime != null) msgs = ((InternalEObject) validTime).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - V3Package.HXITPQ__VALID_TIME, null, msgs);
            if (newValidTime != null) msgs = ((InternalEObject) newValidTime).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - V3Package.HXITPQ__VALID_TIME, null, msgs);
            msgs = basicSetValidTime(newValidTime, msgs);
            if (msgs != null) msgs.dispatch();
        } else if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, V3Package.HXITPQ__VALID_TIME, newValidTime, newValidTime));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch(featureID) {
            case V3Package.HXITPQ__VALID_TIME:
                return basicSetValidTime(null, msgs);
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
            case V3Package.HXITPQ__VALID_TIME:
                return getValidTime();
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
            case V3Package.HXITPQ__VALID_TIME:
                setValidTime((IVLTS) newValue);
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
            case V3Package.HXITPQ__VALID_TIME:
                setValidTime((IVLTS) null);
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
            case V3Package.HXITPQ__VALID_TIME:
                return validTime != null;
        }
        return super.eIsSet(featureID);
    }
}
