package org.hl7.v3.impl;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.hl7.v3.SXCMTS;
import org.hl7.v3.SetOperator;
import org.hl7.v3.V3Package;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>SXCMTS</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.hl7.v3.impl.SXCMTSImpl#getOperator <em>Operator</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class SXCMTSImpl extends TS1Impl implements SXCMTS {

    /**
	 * The default value of the '{@link #getOperator() <em>Operator</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getOperator()
	 * @generated
	 * @ordered
	 */
    protected static final SetOperator OPERATOR_EDEFAULT = SetOperator.I;

    /**
	 * The cached value of the '{@link #getOperator() <em>Operator</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getOperator()
	 * @generated
	 * @ordered
	 */
    protected SetOperator operator = OPERATOR_EDEFAULT;

    /**
	 * This is true if the Operator attribute has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    protected boolean operatorESet;

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    protected SXCMTSImpl() {
        super();
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    protected EClass eStaticClass() {
        return V3Package.eINSTANCE.getSXCMTS();
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public SetOperator getOperator() {
        return operator;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setOperator(SetOperator newOperator) {
        SetOperator oldOperator = operator;
        operator = newOperator == null ? OPERATOR_EDEFAULT : newOperator;
        boolean oldOperatorESet = operatorESet;
        operatorESet = true;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, V3Package.SXCMTS__OPERATOR, oldOperator, operator, !oldOperatorESet));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void unsetOperator() {
        SetOperator oldOperator = operator;
        boolean oldOperatorESet = operatorESet;
        operator = OPERATOR_EDEFAULT;
        operatorESet = false;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.UNSET, V3Package.SXCMTS__OPERATOR, oldOperator, OPERATOR_EDEFAULT, oldOperatorESet));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public boolean isSetOperator() {
        return operatorESet;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch(featureID) {
            case V3Package.SXCMTS__OPERATOR:
                return getOperator();
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
            case V3Package.SXCMTS__OPERATOR:
                setOperator((SetOperator) newValue);
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
            case V3Package.SXCMTS__OPERATOR:
                unsetOperator();
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
            case V3Package.SXCMTS__OPERATOR:
                return isSetOperator();
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
        result.append(" (operator: ");
        if (operatorESet) result.append(operator); else result.append("<unset>");
        result.append(')');
        return result.toString();
    }
}
