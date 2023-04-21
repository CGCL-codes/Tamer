package iec61970.generation.generationdynamics.impl;

import iec61970.core.impl.CurveImpl;
import iec61970.generation.generationdynamics.CTTempActivePowerCurve;
import iec61970.generation.generationdynamics.CombustionTurbine;
import iec61970.generation.generationdynamics.GenerationdynamicsPackage;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>CT Temp Active Power Curve</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link iec61970.generation.generationdynamics.impl.CTTempActivePowerCurveImpl#getCombustionTurbine <em>Combustion Turbine</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class CTTempActivePowerCurveImpl extends CurveImpl implements CTTempActivePowerCurve {

    /**
	 * The cached value of the '{@link #getCombustionTurbine() <em>Combustion Turbine</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCombustionTurbine()
	 * @generated
	 * @ordered
	 */
    protected CombustionTurbine combustionTurbine;

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    protected CTTempActivePowerCurveImpl() {
        super();
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    protected EClass eStaticClass() {
        return GenerationdynamicsPackage.Literals.CT_TEMP_ACTIVE_POWER_CURVE;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public CombustionTurbine getCombustionTurbine() {
        if (combustionTurbine != null && combustionTurbine.eIsProxy()) {
            InternalEObject oldCombustionTurbine = (InternalEObject) combustionTurbine;
            combustionTurbine = (CombustionTurbine) eResolveProxy(oldCombustionTurbine);
            if (combustionTurbine != oldCombustionTurbine) {
                if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.RESOLVE, GenerationdynamicsPackage.CT_TEMP_ACTIVE_POWER_CURVE__COMBUSTION_TURBINE, oldCombustionTurbine, combustionTurbine));
            }
        }
        return combustionTurbine;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public CombustionTurbine basicGetCombustionTurbine() {
        return combustionTurbine;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public NotificationChain basicSetCombustionTurbine(CombustionTurbine newCombustionTurbine, NotificationChain msgs) {
        CombustionTurbine oldCombustionTurbine = combustionTurbine;
        combustionTurbine = newCombustionTurbine;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, GenerationdynamicsPackage.CT_TEMP_ACTIVE_POWER_CURVE__COMBUSTION_TURBINE, oldCombustionTurbine, newCombustionTurbine);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setCombustionTurbine(CombustionTurbine newCombustionTurbine) {
        if (newCombustionTurbine != combustionTurbine) {
            NotificationChain msgs = null;
            if (combustionTurbine != null) msgs = ((InternalEObject) combustionTurbine).eInverseRemove(this, GenerationdynamicsPackage.COMBUSTION_TURBINE__CT_TEMP_ACTIVE_POWER_CURVE, CombustionTurbine.class, msgs);
            if (newCombustionTurbine != null) msgs = ((InternalEObject) newCombustionTurbine).eInverseAdd(this, GenerationdynamicsPackage.COMBUSTION_TURBINE__CT_TEMP_ACTIVE_POWER_CURVE, CombustionTurbine.class, msgs);
            msgs = basicSetCombustionTurbine(newCombustionTurbine, msgs);
            if (msgs != null) msgs.dispatch();
        } else if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, GenerationdynamicsPackage.CT_TEMP_ACTIVE_POWER_CURVE__COMBUSTION_TURBINE, newCombustionTurbine, newCombustionTurbine));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    public NotificationChain eInverseAdd(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch(featureID) {
            case GenerationdynamicsPackage.CT_TEMP_ACTIVE_POWER_CURVE__COMBUSTION_TURBINE:
                if (combustionTurbine != null) msgs = ((InternalEObject) combustionTurbine).eInverseRemove(this, GenerationdynamicsPackage.COMBUSTION_TURBINE__CT_TEMP_ACTIVE_POWER_CURVE, CombustionTurbine.class, msgs);
                return basicSetCombustionTurbine((CombustionTurbine) otherEnd, msgs);
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
            case GenerationdynamicsPackage.CT_TEMP_ACTIVE_POWER_CURVE__COMBUSTION_TURBINE:
                return basicSetCombustionTurbine(null, msgs);
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
            case GenerationdynamicsPackage.CT_TEMP_ACTIVE_POWER_CURVE__COMBUSTION_TURBINE:
                if (resolve) return getCombustionTurbine();
                return basicGetCombustionTurbine();
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
            case GenerationdynamicsPackage.CT_TEMP_ACTIVE_POWER_CURVE__COMBUSTION_TURBINE:
                setCombustionTurbine((CombustionTurbine) newValue);
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
            case GenerationdynamicsPackage.CT_TEMP_ACTIVE_POWER_CURVE__COMBUSTION_TURBINE:
                setCombustionTurbine((CombustionTurbine) null);
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
            case GenerationdynamicsPackage.CT_TEMP_ACTIVE_POWER_CURVE__COMBUSTION_TURBINE:
                return combustionTurbine != null;
        }
        return super.eIsSet(featureID);
    }
}
