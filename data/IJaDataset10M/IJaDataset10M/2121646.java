package org.musicnotation.model.impl;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.musicnotation.model.Bend;
import org.musicnotation.model.MusicNotationPackage;
import org.musicnotation.model.Pitch;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Bend</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.musicnotation.model.impl.BendImpl#getInterval <em>Interval</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class BendImpl extends MobMarkImpl implements Bend {

    /**
	 * The cached value of the '{@link #getInterval() <em>Interval</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getInterval()
	 * @generated
	 * @ordered
	 */
    protected Pitch interval;

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    protected BendImpl() {
        super();
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    protected EClass eStaticClass() {
        return MusicNotationPackage.Literals.BEND;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public Pitch getInterval() {
        return interval;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public NotificationChain basicSetInterval(Pitch newInterval, NotificationChain msgs) {
        Pitch oldInterval = interval;
        interval = newInterval;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, MusicNotationPackage.BEND__INTERVAL, oldInterval, newInterval);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setInterval(Pitch newInterval) {
        if (newInterval != interval) {
            NotificationChain msgs = null;
            if (interval != null) msgs = ((InternalEObject) interval).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - MusicNotationPackage.BEND__INTERVAL, null, msgs);
            if (newInterval != null) msgs = ((InternalEObject) newInterval).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - MusicNotationPackage.BEND__INTERVAL, null, msgs);
            msgs = basicSetInterval(newInterval, msgs);
            if (msgs != null) msgs.dispatch();
        } else if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, MusicNotationPackage.BEND__INTERVAL, newInterval, newInterval));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch(featureID) {
            case MusicNotationPackage.BEND__INTERVAL:
                return basicSetInterval(null, msgs);
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
            case MusicNotationPackage.BEND__INTERVAL:
                return getInterval();
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
            case MusicNotationPackage.BEND__INTERVAL:
                setInterval((Pitch) newValue);
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
            case MusicNotationPackage.BEND__INTERVAL:
                setInterval((Pitch) null);
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
            case MusicNotationPackage.BEND__INTERVAL:
                return interval != null;
        }
        return super.eIsSet(featureID);
    }
}
