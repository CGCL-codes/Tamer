package org.hl7.v3.impl;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.hl7.v3.PQ;
import org.hl7.v3.RTOPQPQ;
import org.hl7.v3.V3Package;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>RTOPQPQ</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.hl7.v3.impl.RTOPQPQImpl#getNumerator <em>Numerator</em>}</li>
 *   <li>{@link org.hl7.v3.impl.RTOPQPQImpl#getDenominator <em>Denominator</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class RTOPQPQImpl extends QTYImpl implements RTOPQPQ {

    /**
	 * The cached value of the '{@link #getNumerator() <em>Numerator</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getNumerator()
	 * @generated
	 * @ordered
	 */
    protected PQ numerator;

    /**
	 * The cached value of the '{@link #getDenominator() <em>Denominator</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDenominator()
	 * @generated
	 * @ordered
	 */
    protected PQ denominator;

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    protected RTOPQPQImpl() {
        super();
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    protected EClass eStaticClass() {
        return V3Package.eINSTANCE.getRTOPQPQ();
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public PQ getNumerator() {
        return numerator;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public NotificationChain basicSetNumerator(PQ newNumerator, NotificationChain msgs) {
        PQ oldNumerator = numerator;
        numerator = newNumerator;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, V3Package.RTOPQPQ__NUMERATOR, oldNumerator, newNumerator);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setNumerator(PQ newNumerator) {
        if (newNumerator != numerator) {
            NotificationChain msgs = null;
            if (numerator != null) msgs = ((InternalEObject) numerator).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - V3Package.RTOPQPQ__NUMERATOR, null, msgs);
            if (newNumerator != null) msgs = ((InternalEObject) newNumerator).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - V3Package.RTOPQPQ__NUMERATOR, null, msgs);
            msgs = basicSetNumerator(newNumerator, msgs);
            if (msgs != null) msgs.dispatch();
        } else if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, V3Package.RTOPQPQ__NUMERATOR, newNumerator, newNumerator));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public PQ getDenominator() {
        return denominator;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public NotificationChain basicSetDenominator(PQ newDenominator, NotificationChain msgs) {
        PQ oldDenominator = denominator;
        denominator = newDenominator;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, V3Package.RTOPQPQ__DENOMINATOR, oldDenominator, newDenominator);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setDenominator(PQ newDenominator) {
        if (newDenominator != denominator) {
            NotificationChain msgs = null;
            if (denominator != null) msgs = ((InternalEObject) denominator).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - V3Package.RTOPQPQ__DENOMINATOR, null, msgs);
            if (newDenominator != null) msgs = ((InternalEObject) newDenominator).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - V3Package.RTOPQPQ__DENOMINATOR, null, msgs);
            msgs = basicSetDenominator(newDenominator, msgs);
            if (msgs != null) msgs.dispatch();
        } else if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, V3Package.RTOPQPQ__DENOMINATOR, newDenominator, newDenominator));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch(featureID) {
            case V3Package.RTOPQPQ__NUMERATOR:
                return basicSetNumerator(null, msgs);
            case V3Package.RTOPQPQ__DENOMINATOR:
                return basicSetDenominator(null, msgs);
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
            case V3Package.RTOPQPQ__NUMERATOR:
                return getNumerator();
            case V3Package.RTOPQPQ__DENOMINATOR:
                return getDenominator();
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
            case V3Package.RTOPQPQ__NUMERATOR:
                setNumerator((PQ) newValue);
                return;
            case V3Package.RTOPQPQ__DENOMINATOR:
                setDenominator((PQ) newValue);
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
            case V3Package.RTOPQPQ__NUMERATOR:
                setNumerator((PQ) null);
                return;
            case V3Package.RTOPQPQ__DENOMINATOR:
                setDenominator((PQ) null);
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
            case V3Package.RTOPQPQ__NUMERATOR:
                return numerator != null;
            case V3Package.RTOPQPQ__DENOMINATOR:
                return denominator != null;
        }
        return super.eIsSet(featureID);
    }
}
