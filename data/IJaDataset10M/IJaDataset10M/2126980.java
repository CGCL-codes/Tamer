package org.hl7.v3.impl;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.hl7.v3.UVPTS;
import org.hl7.v3.V3Package;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>UVPTS</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.hl7.v3.impl.UVPTSImpl#getProbability <em>Probability</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class UVPTSImpl extends TS1Impl implements UVPTS {

    /**
	 * The default value of the '{@link #getProbability() <em>Probability</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getProbability()
	 * @generated
	 * @ordered
	 */
    protected static final double PROBABILITY_EDEFAULT = 0.0;

    /**
	 * The cached value of the '{@link #getProbability() <em>Probability</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getProbability()
	 * @generated
	 * @ordered
	 */
    protected double probability = PROBABILITY_EDEFAULT;

    /**
	 * This is true if the Probability attribute has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    protected boolean probabilityESet;

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    protected UVPTSImpl() {
        super();
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    protected EClass eStaticClass() {
        return V3Package.eINSTANCE.getUVPTS();
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public double getProbability() {
        return probability;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setProbability(double newProbability) {
        double oldProbability = probability;
        probability = newProbability;
        boolean oldProbabilityESet = probabilityESet;
        probabilityESet = true;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, V3Package.UVPTS__PROBABILITY, oldProbability, probability, !oldProbabilityESet));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void unsetProbability() {
        double oldProbability = probability;
        boolean oldProbabilityESet = probabilityESet;
        probability = PROBABILITY_EDEFAULT;
        probabilityESet = false;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.UNSET, V3Package.UVPTS__PROBABILITY, oldProbability, PROBABILITY_EDEFAULT, oldProbabilityESet));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public boolean isSetProbability() {
        return probabilityESet;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch(featureID) {
            case V3Package.UVPTS__PROBABILITY:
                return new Double(getProbability());
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
            case V3Package.UVPTS__PROBABILITY:
                setProbability(((Double) newValue).doubleValue());
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
            case V3Package.UVPTS__PROBABILITY:
                unsetProbability();
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
            case V3Package.UVPTS__PROBABILITY:
                return isSetProbability();
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
        result.append(" (probability: ");
        if (probabilityESet) result.append(probability); else result.append("<unset>");
        result.append(')');
        return result.toString();
    }
}
