package org.imogene.model.core.impl;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.imogene.model.core.ImogenePackage;
import org.imogene.model.core.NumericField;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Numeric Field</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.imogene.model.core.impl.NumericFieldImpl#getMin <em>Min</em>}</li>
 *   <li>{@link org.imogene.model.core.impl.NumericFieldImpl#getMax <em>Max</em>}</li>
 *   <li>{@link org.imogene.model.core.impl.NumericFieldImpl#getUnit <em>Unit</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public abstract class NumericFieldImpl extends FieldEntityImpl implements NumericField {

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public static final String copyright = "Medes-IMPS 2011";

    /**
	 * The default value of the '{@link #getMin() <em>Min</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMin()
	 * @generated
	 * @ordered
	 */
    protected static final String MIN_EDEFAULT = "";

    /**
	 * The cached value of the '{@link #getMin() <em>Min</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMin()
	 * @generated
	 * @ordered
	 */
    protected String min = MIN_EDEFAULT;

    /**
	 * The default value of the '{@link #getMax() <em>Max</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMax()
	 * @generated
	 * @ordered
	 */
    protected static final String MAX_EDEFAULT = "";

    /**
	 * The cached value of the '{@link #getMax() <em>Max</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMax()
	 * @generated
	 * @ordered
	 */
    protected String max = MAX_EDEFAULT;

    /**
	 * The default value of the '{@link #getUnit() <em>Unit</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getUnit()
	 * @generated
	 * @ordered
	 */
    protected static final String UNIT_EDEFAULT = null;

    /**
	 * The cached value of the '{@link #getUnit() <em>Unit</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getUnit()
	 * @generated
	 * @ordered
	 */
    protected String unit = UNIT_EDEFAULT;

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    protected NumericFieldImpl() {
        super();
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    protected EClass eStaticClass() {
        return ImogenePackage.Literals.NUMERIC_FIELD;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public String getMin() {
        return min;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setMin(String newMin) {
        String oldMin = min;
        min = newMin;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, ImogenePackage.NUMERIC_FIELD__MIN, oldMin, min));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public String getMax() {
        return max;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setMax(String newMax) {
        String oldMax = max;
        max = newMax;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, ImogenePackage.NUMERIC_FIELD__MAX, oldMax, max));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public String getUnit() {
        return unit;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setUnit(String newUnit) {
        String oldUnit = unit;
        unit = newUnit;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, ImogenePackage.NUMERIC_FIELD__UNIT, oldUnit, unit));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch(featureID) {
            case ImogenePackage.NUMERIC_FIELD__MIN:
                return getMin();
            case ImogenePackage.NUMERIC_FIELD__MAX:
                return getMax();
            case ImogenePackage.NUMERIC_FIELD__UNIT:
                return getUnit();
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
            case ImogenePackage.NUMERIC_FIELD__MIN:
                setMin((String) newValue);
                return;
            case ImogenePackage.NUMERIC_FIELD__MAX:
                setMax((String) newValue);
                return;
            case ImogenePackage.NUMERIC_FIELD__UNIT:
                setUnit((String) newValue);
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
            case ImogenePackage.NUMERIC_FIELD__MIN:
                setMin(MIN_EDEFAULT);
                return;
            case ImogenePackage.NUMERIC_FIELD__MAX:
                setMax(MAX_EDEFAULT);
                return;
            case ImogenePackage.NUMERIC_FIELD__UNIT:
                setUnit(UNIT_EDEFAULT);
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
            case ImogenePackage.NUMERIC_FIELD__MIN:
                return MIN_EDEFAULT == null ? min != null : !MIN_EDEFAULT.equals(min);
            case ImogenePackage.NUMERIC_FIELD__MAX:
                return MAX_EDEFAULT == null ? max != null : !MAX_EDEFAULT.equals(max);
            case ImogenePackage.NUMERIC_FIELD__UNIT:
                return UNIT_EDEFAULT == null ? unit != null : !UNIT_EDEFAULT.equals(unit);
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
        result.append(" (min: ");
        result.append(min);
        result.append(", max: ");
        result.append(max);
        result.append(", unit: ");
        result.append(unit);
        result.append(')');
        return result.toString();
    }
}
