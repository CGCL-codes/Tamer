package SBVR.impl;

import SBVR.LogicalFormulation;
import SBVR.ModalFormulation;
import SBVR.SBVRPackage;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Modal Formulation</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link SBVR.impl.ModalFormulationImpl#getLogicalFormulation <em>Logical Formulation</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class ModalFormulationImpl extends LogicalFormulationImpl implements ModalFormulation {

    /**
	 * The cached value of the '{@link #getLogicalFormulation() <em>Logical Formulation</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLogicalFormulation()
	 * @generated
	 * @ordered
	 */
    protected LogicalFormulation logicalFormulation;

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    protected ModalFormulationImpl() {
        super();
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    protected EClass eStaticClass() {
        return SBVRPackage.Literals.MODAL_FORMULATION;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public LogicalFormulation getLogicalFormulation() {
        if (logicalFormulation != null && logicalFormulation.eIsProxy()) {
            InternalEObject oldLogicalFormulation = (InternalEObject) logicalFormulation;
            logicalFormulation = (LogicalFormulation) eResolveProxy(oldLogicalFormulation);
            if (logicalFormulation != oldLogicalFormulation) {
                if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.RESOLVE, SBVRPackage.MODAL_FORMULATION__LOGICAL_FORMULATION, oldLogicalFormulation, logicalFormulation));
            }
        }
        return logicalFormulation;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public LogicalFormulation basicGetLogicalFormulation() {
        return logicalFormulation;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setLogicalFormulation(LogicalFormulation newLogicalFormulation) {
        LogicalFormulation oldLogicalFormulation = logicalFormulation;
        logicalFormulation = newLogicalFormulation;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, SBVRPackage.MODAL_FORMULATION__LOGICAL_FORMULATION, oldLogicalFormulation, logicalFormulation));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch(featureID) {
            case SBVRPackage.MODAL_FORMULATION__LOGICAL_FORMULATION:
                if (resolve) return getLogicalFormulation();
                return basicGetLogicalFormulation();
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
            case SBVRPackage.MODAL_FORMULATION__LOGICAL_FORMULATION:
                setLogicalFormulation((LogicalFormulation) newValue);
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
            case SBVRPackage.MODAL_FORMULATION__LOGICAL_FORMULATION:
                setLogicalFormulation((LogicalFormulation) null);
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
            case SBVRPackage.MODAL_FORMULATION__LOGICAL_FORMULATION:
                return logicalFormulation != null;
        }
        return super.eIsSet(featureID);
    }
}
