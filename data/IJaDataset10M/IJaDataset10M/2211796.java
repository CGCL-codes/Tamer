package SBVR.impl;

import SBVR.NonNegativeInteger;
import SBVR.NumericRangeQuantification;
import SBVR.SBVRPackage;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Numeric Range Quantification</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link SBVR.impl.NumericRangeQuantificationImpl#getMinimumCardinality <em>Minimum Cardinality</em>}</li>
 *   <li>{@link SBVR.impl.NumericRangeQuantificationImpl#getMaximumCardinality <em>Maximum Cardinality</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class NumericRangeQuantificationImpl extends QuantificationImpl implements NumericRangeQuantification {

    /**
	 * The cached value of the '{@link #getMinimumCardinality() <em>Minimum Cardinality</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMinimumCardinality()
	 * @generated
	 * @ordered
	 */
    protected NonNegativeInteger minimumCardinality;

    /**
	 * The cached value of the '{@link #getMaximumCardinality() <em>Maximum Cardinality</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMaximumCardinality()
	 * @generated
	 * @ordered
	 */
    protected NonNegativeInteger maximumCardinality;

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    protected NumericRangeQuantificationImpl() {
        super();
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    protected EClass eStaticClass() {
        return SBVRPackage.Literals.NUMERIC_RANGE_QUANTIFICATION;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public NonNegativeInteger getMinimumCardinality() {
        if (minimumCardinality != null && minimumCardinality.eIsProxy()) {
            InternalEObject oldMinimumCardinality = (InternalEObject) minimumCardinality;
            minimumCardinality = (NonNegativeInteger) eResolveProxy(oldMinimumCardinality);
            if (minimumCardinality != oldMinimumCardinality) {
                if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.RESOLVE, SBVRPackage.NUMERIC_RANGE_QUANTIFICATION__MINIMUM_CARDINALITY, oldMinimumCardinality, minimumCardinality));
            }
        }
        return minimumCardinality;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public NonNegativeInteger basicGetMinimumCardinality() {
        return minimumCardinality;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setMinimumCardinality(NonNegativeInteger newMinimumCardinality) {
        NonNegativeInteger oldMinimumCardinality = minimumCardinality;
        minimumCardinality = newMinimumCardinality;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, SBVRPackage.NUMERIC_RANGE_QUANTIFICATION__MINIMUM_CARDINALITY, oldMinimumCardinality, minimumCardinality));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public NonNegativeInteger getMaximumCardinality() {
        if (maximumCardinality != null && maximumCardinality.eIsProxy()) {
            InternalEObject oldMaximumCardinality = (InternalEObject) maximumCardinality;
            maximumCardinality = (NonNegativeInteger) eResolveProxy(oldMaximumCardinality);
            if (maximumCardinality != oldMaximumCardinality) {
                if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.RESOLVE, SBVRPackage.NUMERIC_RANGE_QUANTIFICATION__MAXIMUM_CARDINALITY, oldMaximumCardinality, maximumCardinality));
            }
        }
        return maximumCardinality;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public NonNegativeInteger basicGetMaximumCardinality() {
        return maximumCardinality;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setMaximumCardinality(NonNegativeInteger newMaximumCardinality) {
        NonNegativeInteger oldMaximumCardinality = maximumCardinality;
        maximumCardinality = newMaximumCardinality;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, SBVRPackage.NUMERIC_RANGE_QUANTIFICATION__MAXIMUM_CARDINALITY, oldMaximumCardinality, maximumCardinality));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch(featureID) {
            case SBVRPackage.NUMERIC_RANGE_QUANTIFICATION__MINIMUM_CARDINALITY:
                if (resolve) return getMinimumCardinality();
                return basicGetMinimumCardinality();
            case SBVRPackage.NUMERIC_RANGE_QUANTIFICATION__MAXIMUM_CARDINALITY:
                if (resolve) return getMaximumCardinality();
                return basicGetMaximumCardinality();
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
            case SBVRPackage.NUMERIC_RANGE_QUANTIFICATION__MINIMUM_CARDINALITY:
                setMinimumCardinality((NonNegativeInteger) newValue);
                return;
            case SBVRPackage.NUMERIC_RANGE_QUANTIFICATION__MAXIMUM_CARDINALITY:
                setMaximumCardinality((NonNegativeInteger) newValue);
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
            case SBVRPackage.NUMERIC_RANGE_QUANTIFICATION__MINIMUM_CARDINALITY:
                setMinimumCardinality((NonNegativeInteger) null);
                return;
            case SBVRPackage.NUMERIC_RANGE_QUANTIFICATION__MAXIMUM_CARDINALITY:
                setMaximumCardinality((NonNegativeInteger) null);
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
            case SBVRPackage.NUMERIC_RANGE_QUANTIFICATION__MINIMUM_CARDINALITY:
                return minimumCardinality != null;
            case SBVRPackage.NUMERIC_RANGE_QUANTIFICATION__MAXIMUM_CARDINALITY:
                return maximumCardinality != null;
        }
        return super.eIsSet(featureID);
    }
}
