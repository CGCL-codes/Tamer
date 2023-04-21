package org.echarts.edt.editor.eCharts.impl;

import org.echarts.edt.editor.eCharts.EChartsPackage;
import org.echarts.edt.editor.eCharts.StateMachineInvocation;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>State Machine Invocation</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.echarts.edt.editor.eCharts.impl.StateMachineInvocationImpl#getMachineRef <em>Machine Ref</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class StateMachineInvocationImpl extends MinimalEObjectImpl.Container implements StateMachineInvocation {

    /**
   * The default value of the '{@link #getMachineRef() <em>Machine Ref</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getMachineRef()
   * @generated
   * @ordered
   */
    protected static final String MACHINE_REF_EDEFAULT = null;

    /**
   * The cached value of the '{@link #getMachineRef() <em>Machine Ref</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getMachineRef()
   * @generated
   * @ordered
   */
    protected String machineRef = MACHINE_REF_EDEFAULT;

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    protected StateMachineInvocationImpl() {
        super();
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    @Override
    protected EClass eStaticClass() {
        return EChartsPackage.Literals.STATE_MACHINE_INVOCATION;
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public String getMachineRef() {
        return machineRef;
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public void setMachineRef(String newMachineRef) {
        String oldMachineRef = machineRef;
        machineRef = newMachineRef;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, EChartsPackage.STATE_MACHINE_INVOCATION__MACHINE_REF, oldMachineRef, machineRef));
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch(featureID) {
            case EChartsPackage.STATE_MACHINE_INVOCATION__MACHINE_REF:
                return getMachineRef();
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
            case EChartsPackage.STATE_MACHINE_INVOCATION__MACHINE_REF:
                setMachineRef((String) newValue);
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
            case EChartsPackage.STATE_MACHINE_INVOCATION__MACHINE_REF:
                setMachineRef(MACHINE_REF_EDEFAULT);
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
            case EChartsPackage.STATE_MACHINE_INVOCATION__MACHINE_REF:
                return MACHINE_REF_EDEFAULT == null ? machineRef != null : !MACHINE_REF_EDEFAULT.equals(machineRef);
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
        result.append(" (machineRef: ");
        result.append(machineRef);
        result.append(')');
        return result.toString();
    }
}
