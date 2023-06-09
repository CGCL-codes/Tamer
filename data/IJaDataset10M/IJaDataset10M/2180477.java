package iec61970.meas.impl;

import iec61970.meas.AnalogLimit;
import iec61970.meas.AnalogLimitSet;
import iec61970.meas.MeasPackage;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Analog Limit</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link iec61970.meas.impl.AnalogLimitImpl#getValue <em>Value</em>}</li>
 *   <li>{@link iec61970.meas.impl.AnalogLimitImpl#getLimitSet <em>Limit Set</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class AnalogLimitImpl extends LimitImpl implements AnalogLimit {

    /**
	 * The default value of the '{@link #getValue() <em>Value</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getValue()
	 * @generated
	 * @ordered
	 */
    protected static final String VALUE_EDEFAULT = null;

    /**
	 * The cached value of the '{@link #getValue() <em>Value</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getValue()
	 * @generated
	 * @ordered
	 */
    protected String value = VALUE_EDEFAULT;

    /**
	 * The cached value of the '{@link #getLimitSet() <em>Limit Set</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLimitSet()
	 * @generated
	 * @ordered
	 */
    protected AnalogLimitSet limitSet;

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    protected AnalogLimitImpl() {
        super();
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    protected EClass eStaticClass() {
        return MeasPackage.Literals.ANALOG_LIMIT;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public String getValue() {
        return value;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setValue(String newValue) {
        String oldValue = value;
        value = newValue;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, MeasPackage.ANALOG_LIMIT__VALUE, oldValue, value));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public AnalogLimitSet getLimitSet() {
        if (limitSet != null && limitSet.eIsProxy()) {
            InternalEObject oldLimitSet = (InternalEObject) limitSet;
            limitSet = (AnalogLimitSet) eResolveProxy(oldLimitSet);
            if (limitSet != oldLimitSet) {
                if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.RESOLVE, MeasPackage.ANALOG_LIMIT__LIMIT_SET, oldLimitSet, limitSet));
            }
        }
        return limitSet;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public AnalogLimitSet basicGetLimitSet() {
        return limitSet;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public NotificationChain basicSetLimitSet(AnalogLimitSet newLimitSet, NotificationChain msgs) {
        AnalogLimitSet oldLimitSet = limitSet;
        limitSet = newLimitSet;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, MeasPackage.ANALOG_LIMIT__LIMIT_SET, oldLimitSet, newLimitSet);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setLimitSet(AnalogLimitSet newLimitSet) {
        if (newLimitSet != limitSet) {
            NotificationChain msgs = null;
            if (limitSet != null) msgs = ((InternalEObject) limitSet).eInverseRemove(this, MeasPackage.ANALOG_LIMIT_SET__LIMITS, AnalogLimitSet.class, msgs);
            if (newLimitSet != null) msgs = ((InternalEObject) newLimitSet).eInverseAdd(this, MeasPackage.ANALOG_LIMIT_SET__LIMITS, AnalogLimitSet.class, msgs);
            msgs = basicSetLimitSet(newLimitSet, msgs);
            if (msgs != null) msgs.dispatch();
        } else if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, MeasPackage.ANALOG_LIMIT__LIMIT_SET, newLimitSet, newLimitSet));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    public NotificationChain eInverseAdd(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch(featureID) {
            case MeasPackage.ANALOG_LIMIT__LIMIT_SET:
                if (limitSet != null) msgs = ((InternalEObject) limitSet).eInverseRemove(this, MeasPackage.ANALOG_LIMIT_SET__LIMITS, AnalogLimitSet.class, msgs);
                return basicSetLimitSet((AnalogLimitSet) otherEnd, msgs);
        }
        return super.eInverseAdd(otherEnd, featureID, msgs);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch(featureID) {
            case MeasPackage.ANALOG_LIMIT__LIMIT_SET:
                return basicSetLimitSet(null, msgs);
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
            case MeasPackage.ANALOG_LIMIT__VALUE:
                return getValue();
            case MeasPackage.ANALOG_LIMIT__LIMIT_SET:
                if (resolve) return getLimitSet();
                return basicGetLimitSet();
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
            case MeasPackage.ANALOG_LIMIT__VALUE:
                setValue((String) newValue);
                return;
            case MeasPackage.ANALOG_LIMIT__LIMIT_SET:
                setLimitSet((AnalogLimitSet) newValue);
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
            case MeasPackage.ANALOG_LIMIT__VALUE:
                setValue(VALUE_EDEFAULT);
                return;
            case MeasPackage.ANALOG_LIMIT__LIMIT_SET:
                setLimitSet((AnalogLimitSet) null);
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
            case MeasPackage.ANALOG_LIMIT__VALUE:
                return VALUE_EDEFAULT == null ? value != null : !VALUE_EDEFAULT.equals(value);
            case MeasPackage.ANALOG_LIMIT__LIMIT_SET:
                return limitSet != null;
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
        result.append(" (value: ");
        result.append(value);
        result.append(')');
        return result.toString();
    }
}
