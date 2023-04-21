package iec61970.meas.impl;

import iec61970.meas.Command;
import iec61970.meas.Discrete;
import iec61970.meas.DiscreteValue;
import iec61970.meas.MeasPackage;
import iec61970.meas.ValueAliasSet;
import java.util.Collection;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.util.EObjectWithInverseResolvingEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Discrete</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link iec61970.meas.impl.DiscreteImpl#getMaxValue <em>Max Value</em>}</li>
 *   <li>{@link iec61970.meas.impl.DiscreteImpl#getMinValue <em>Min Value</em>}</li>
 *   <li>{@link iec61970.meas.impl.DiscreteImpl#getNormalValue <em>Normal Value</em>}</li>
 *   <li>{@link iec61970.meas.impl.DiscreteImpl#getContain_MeasurementValues <em>Contain Measurement Values</em>}</li>
 *   <li>{@link iec61970.meas.impl.DiscreteImpl#getValueAliasSet <em>Value Alias Set</em>}</li>
 *   <li>{@link iec61970.meas.impl.DiscreteImpl#getControlledBy_Control <em>Controlled By Control</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class DiscreteImpl extends MeasurementImpl implements Discrete {

    /**
	 * The default value of the '{@link #getMaxValue() <em>Max Value</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMaxValue()
	 * @generated
	 * @ordered
	 */
    protected static final String MAX_VALUE_EDEFAULT = null;

    /**
	 * The cached value of the '{@link #getMaxValue() <em>Max Value</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMaxValue()
	 * @generated
	 * @ordered
	 */
    protected String maxValue = MAX_VALUE_EDEFAULT;

    /**
	 * The default value of the '{@link #getMinValue() <em>Min Value</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMinValue()
	 * @generated
	 * @ordered
	 */
    protected static final String MIN_VALUE_EDEFAULT = null;

    /**
	 * The cached value of the '{@link #getMinValue() <em>Min Value</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMinValue()
	 * @generated
	 * @ordered
	 */
    protected String minValue = MIN_VALUE_EDEFAULT;

    /**
	 * The default value of the '{@link #getNormalValue() <em>Normal Value</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getNormalValue()
	 * @generated
	 * @ordered
	 */
    protected static final String NORMAL_VALUE_EDEFAULT = null;

    /**
	 * The cached value of the '{@link #getNormalValue() <em>Normal Value</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getNormalValue()
	 * @generated
	 * @ordered
	 */
    protected String normalValue = NORMAL_VALUE_EDEFAULT;

    /**
	 * The cached value of the '{@link #getContain_MeasurementValues() <em>Contain Measurement Values</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getContain_MeasurementValues()
	 * @generated
	 * @ordered
	 */
    protected EList<DiscreteValue> contain_MeasurementValues;

    /**
	 * The cached value of the '{@link #getValueAliasSet() <em>Value Alias Set</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getValueAliasSet()
	 * @generated
	 * @ordered
	 */
    protected ValueAliasSet valueAliasSet;

    /**
	 * The cached value of the '{@link #getControlledBy_Control() <em>Controlled By Control</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getControlledBy_Control()
	 * @generated
	 * @ordered
	 */
    protected Command controlledBy_Control;

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    protected DiscreteImpl() {
        super();
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    protected EClass eStaticClass() {
        return MeasPackage.Literals.DISCRETE;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public String getMaxValue() {
        return maxValue;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setMaxValue(String newMaxValue) {
        String oldMaxValue = maxValue;
        maxValue = newMaxValue;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, MeasPackage.DISCRETE__MAX_VALUE, oldMaxValue, maxValue));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public String getMinValue() {
        return minValue;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setMinValue(String newMinValue) {
        String oldMinValue = minValue;
        minValue = newMinValue;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, MeasPackage.DISCRETE__MIN_VALUE, oldMinValue, minValue));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public String getNormalValue() {
        return normalValue;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setNormalValue(String newNormalValue) {
        String oldNormalValue = normalValue;
        normalValue = newNormalValue;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, MeasPackage.DISCRETE__NORMAL_VALUE, oldNormalValue, normalValue));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EList<DiscreteValue> getContain_MeasurementValues() {
        if (contain_MeasurementValues == null) {
            contain_MeasurementValues = new EObjectWithInverseResolvingEList<DiscreteValue>(DiscreteValue.class, this, MeasPackage.DISCRETE__CONTAIN_MEASUREMENT_VALUES, MeasPackage.DISCRETE_VALUE__MEMBER_OF_MEASUREMENT);
        }
        return contain_MeasurementValues;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public ValueAliasSet getValueAliasSet() {
        if (valueAliasSet != null && valueAliasSet.eIsProxy()) {
            InternalEObject oldValueAliasSet = (InternalEObject) valueAliasSet;
            valueAliasSet = (ValueAliasSet) eResolveProxy(oldValueAliasSet);
            if (valueAliasSet != oldValueAliasSet) {
                if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.RESOLVE, MeasPackage.DISCRETE__VALUE_ALIAS_SET, oldValueAliasSet, valueAliasSet));
            }
        }
        return valueAliasSet;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public ValueAliasSet basicGetValueAliasSet() {
        return valueAliasSet;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public NotificationChain basicSetValueAliasSet(ValueAliasSet newValueAliasSet, NotificationChain msgs) {
        ValueAliasSet oldValueAliasSet = valueAliasSet;
        valueAliasSet = newValueAliasSet;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, MeasPackage.DISCRETE__VALUE_ALIAS_SET, oldValueAliasSet, newValueAliasSet);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setValueAliasSet(ValueAliasSet newValueAliasSet) {
        if (newValueAliasSet != valueAliasSet) {
            NotificationChain msgs = null;
            if (valueAliasSet != null) msgs = ((InternalEObject) valueAliasSet).eInverseRemove(this, MeasPackage.VALUE_ALIAS_SET__MEASUREMENTS, ValueAliasSet.class, msgs);
            if (newValueAliasSet != null) msgs = ((InternalEObject) newValueAliasSet).eInverseAdd(this, MeasPackage.VALUE_ALIAS_SET__MEASUREMENTS, ValueAliasSet.class, msgs);
            msgs = basicSetValueAliasSet(newValueAliasSet, msgs);
            if (msgs != null) msgs.dispatch();
        } else if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, MeasPackage.DISCRETE__VALUE_ALIAS_SET, newValueAliasSet, newValueAliasSet));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public Command getControlledBy_Control() {
        if (controlledBy_Control != null && controlledBy_Control.eIsProxy()) {
            InternalEObject oldControlledBy_Control = (InternalEObject) controlledBy_Control;
            controlledBy_Control = (Command) eResolveProxy(oldControlledBy_Control);
            if (controlledBy_Control != oldControlledBy_Control) {
                if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.RESOLVE, MeasPackage.DISCRETE__CONTROLLED_BY_CONTROL, oldControlledBy_Control, controlledBy_Control));
            }
        }
        return controlledBy_Control;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public Command basicGetControlledBy_Control() {
        return controlledBy_Control;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public NotificationChain basicSetControlledBy_Control(Command newControlledBy_Control, NotificationChain msgs) {
        Command oldControlledBy_Control = controlledBy_Control;
        controlledBy_Control = newControlledBy_Control;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, MeasPackage.DISCRETE__CONTROLLED_BY_CONTROL, oldControlledBy_Control, newControlledBy_Control);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setControlledBy_Control(Command newControlledBy_Control) {
        if (newControlledBy_Control != controlledBy_Control) {
            NotificationChain msgs = null;
            if (controlledBy_Control != null) msgs = ((InternalEObject) controlledBy_Control).eInverseRemove(this, MeasPackage.COMMAND__MEASURED_BY_MEASUREMENT, Command.class, msgs);
            if (newControlledBy_Control != null) msgs = ((InternalEObject) newControlledBy_Control).eInverseAdd(this, MeasPackage.COMMAND__MEASURED_BY_MEASUREMENT, Command.class, msgs);
            msgs = basicSetControlledBy_Control(newControlledBy_Control, msgs);
            if (msgs != null) msgs.dispatch();
        } else if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, MeasPackage.DISCRETE__CONTROLLED_BY_CONTROL, newControlledBy_Control, newControlledBy_Control));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @SuppressWarnings("unchecked")
    @Override
    public NotificationChain eInverseAdd(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch(featureID) {
            case MeasPackage.DISCRETE__CONTAIN_MEASUREMENT_VALUES:
                return ((InternalEList<InternalEObject>) (InternalEList<?>) getContain_MeasurementValues()).basicAdd(otherEnd, msgs);
            case MeasPackage.DISCRETE__VALUE_ALIAS_SET:
                if (valueAliasSet != null) msgs = ((InternalEObject) valueAliasSet).eInverseRemove(this, MeasPackage.VALUE_ALIAS_SET__MEASUREMENTS, ValueAliasSet.class, msgs);
                return basicSetValueAliasSet((ValueAliasSet) otherEnd, msgs);
            case MeasPackage.DISCRETE__CONTROLLED_BY_CONTROL:
                if (controlledBy_Control != null) msgs = ((InternalEObject) controlledBy_Control).eInverseRemove(this, MeasPackage.COMMAND__MEASURED_BY_MEASUREMENT, Command.class, msgs);
                return basicSetControlledBy_Control((Command) otherEnd, msgs);
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
            case MeasPackage.DISCRETE__CONTAIN_MEASUREMENT_VALUES:
                return ((InternalEList<?>) getContain_MeasurementValues()).basicRemove(otherEnd, msgs);
            case MeasPackage.DISCRETE__VALUE_ALIAS_SET:
                return basicSetValueAliasSet(null, msgs);
            case MeasPackage.DISCRETE__CONTROLLED_BY_CONTROL:
                return basicSetControlledBy_Control(null, msgs);
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
            case MeasPackage.DISCRETE__MAX_VALUE:
                return getMaxValue();
            case MeasPackage.DISCRETE__MIN_VALUE:
                return getMinValue();
            case MeasPackage.DISCRETE__NORMAL_VALUE:
                return getNormalValue();
            case MeasPackage.DISCRETE__CONTAIN_MEASUREMENT_VALUES:
                return getContain_MeasurementValues();
            case MeasPackage.DISCRETE__VALUE_ALIAS_SET:
                if (resolve) return getValueAliasSet();
                return basicGetValueAliasSet();
            case MeasPackage.DISCRETE__CONTROLLED_BY_CONTROL:
                if (resolve) return getControlledBy_Control();
                return basicGetControlledBy_Control();
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
            case MeasPackage.DISCRETE__MAX_VALUE:
                setMaxValue((String) newValue);
                return;
            case MeasPackage.DISCRETE__MIN_VALUE:
                setMinValue((String) newValue);
                return;
            case MeasPackage.DISCRETE__NORMAL_VALUE:
                setNormalValue((String) newValue);
                return;
            case MeasPackage.DISCRETE__CONTAIN_MEASUREMENT_VALUES:
                getContain_MeasurementValues().clear();
                getContain_MeasurementValues().addAll((Collection<? extends DiscreteValue>) newValue);
                return;
            case MeasPackage.DISCRETE__VALUE_ALIAS_SET:
                setValueAliasSet((ValueAliasSet) newValue);
                return;
            case MeasPackage.DISCRETE__CONTROLLED_BY_CONTROL:
                setControlledBy_Control((Command) newValue);
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
            case MeasPackage.DISCRETE__MAX_VALUE:
                setMaxValue(MAX_VALUE_EDEFAULT);
                return;
            case MeasPackage.DISCRETE__MIN_VALUE:
                setMinValue(MIN_VALUE_EDEFAULT);
                return;
            case MeasPackage.DISCRETE__NORMAL_VALUE:
                setNormalValue(NORMAL_VALUE_EDEFAULT);
                return;
            case MeasPackage.DISCRETE__CONTAIN_MEASUREMENT_VALUES:
                getContain_MeasurementValues().clear();
                return;
            case MeasPackage.DISCRETE__VALUE_ALIAS_SET:
                setValueAliasSet((ValueAliasSet) null);
                return;
            case MeasPackage.DISCRETE__CONTROLLED_BY_CONTROL:
                setControlledBy_Control((Command) null);
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
            case MeasPackage.DISCRETE__MAX_VALUE:
                return MAX_VALUE_EDEFAULT == null ? maxValue != null : !MAX_VALUE_EDEFAULT.equals(maxValue);
            case MeasPackage.DISCRETE__MIN_VALUE:
                return MIN_VALUE_EDEFAULT == null ? minValue != null : !MIN_VALUE_EDEFAULT.equals(minValue);
            case MeasPackage.DISCRETE__NORMAL_VALUE:
                return NORMAL_VALUE_EDEFAULT == null ? normalValue != null : !NORMAL_VALUE_EDEFAULT.equals(normalValue);
            case MeasPackage.DISCRETE__CONTAIN_MEASUREMENT_VALUES:
                return contain_MeasurementValues != null && !contain_MeasurementValues.isEmpty();
            case MeasPackage.DISCRETE__VALUE_ALIAS_SET:
                return valueAliasSet != null;
            case MeasPackage.DISCRETE__CONTROLLED_BY_CONTROL:
                return controlledBy_Control != null;
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
        result.append(" (maxValue: ");
        result.append(maxValue);
        result.append(", minValue: ");
        result.append(minValue);
        result.append(", normalValue: ");
        result.append(normalValue);
        result.append(')');
        return result.toString();
    }
}
