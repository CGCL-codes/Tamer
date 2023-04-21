package org.contract.ist.schema.ist.contract.impl;

import org.contract.ist.schema.ist.contract.ContractPackage;
import org.contract.ist.schema.ist.contract.Modality;
import org.contract.ist.schema.ist.contract.What;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Modality</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.contract.ist.schema.ist.contract.impl.ModalityImpl#getOBLIGATION <em>OBLIGATION</em>}</li>
 *   <li>{@link org.contract.ist.schema.ist.contract.impl.ModalityImpl#getPROHIBITION <em>PROHIBITION</em>}</li>
 *   <li>{@link org.contract.ist.schema.ist.contract.impl.ModalityImpl#getPERMISSION <em>PERMISSION</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class ModalityImpl extends EObjectImpl implements Modality {

    /**
	 * The cached value of the '{@link #getOBLIGATION() <em>OBLIGATION</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getOBLIGATION()
	 * @generated
	 * @ordered
	 */
    protected What oBLIGATION;

    /**
	 * The cached value of the '{@link #getPROHIBITION() <em>PROHIBITION</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPROHIBITION()
	 * @generated
	 * @ordered
	 */
    protected What pROHIBITION;

    /**
	 * The cached value of the '{@link #getPERMISSION() <em>PERMISSION</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPERMISSION()
	 * @generated
	 * @ordered
	 */
    protected What pERMISSION;

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    protected ModalityImpl() {
        super();
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    protected EClass eStaticClass() {
        return ContractPackage.Literals.MODALITY;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public What getOBLIGATION() {
        return oBLIGATION;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public NotificationChain basicSetOBLIGATION(What newOBLIGATION, NotificationChain msgs) {
        What oldOBLIGATION = oBLIGATION;
        oBLIGATION = newOBLIGATION;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, ContractPackage.MODALITY__OBLIGATION, oldOBLIGATION, newOBLIGATION);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setOBLIGATION(What newOBLIGATION) {
        if (newOBLIGATION != oBLIGATION) {
            NotificationChain msgs = null;
            if (oBLIGATION != null) msgs = ((InternalEObject) oBLIGATION).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - ContractPackage.MODALITY__OBLIGATION, null, msgs);
            if (newOBLIGATION != null) msgs = ((InternalEObject) newOBLIGATION).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - ContractPackage.MODALITY__OBLIGATION, null, msgs);
            msgs = basicSetOBLIGATION(newOBLIGATION, msgs);
            if (msgs != null) msgs.dispatch();
        } else if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, ContractPackage.MODALITY__OBLIGATION, newOBLIGATION, newOBLIGATION));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public What getPROHIBITION() {
        return pROHIBITION;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public NotificationChain basicSetPROHIBITION(What newPROHIBITION, NotificationChain msgs) {
        What oldPROHIBITION = pROHIBITION;
        pROHIBITION = newPROHIBITION;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, ContractPackage.MODALITY__PROHIBITION, oldPROHIBITION, newPROHIBITION);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setPROHIBITION(What newPROHIBITION) {
        if (newPROHIBITION != pROHIBITION) {
            NotificationChain msgs = null;
            if (pROHIBITION != null) msgs = ((InternalEObject) pROHIBITION).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - ContractPackage.MODALITY__PROHIBITION, null, msgs);
            if (newPROHIBITION != null) msgs = ((InternalEObject) newPROHIBITION).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - ContractPackage.MODALITY__PROHIBITION, null, msgs);
            msgs = basicSetPROHIBITION(newPROHIBITION, msgs);
            if (msgs != null) msgs.dispatch();
        } else if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, ContractPackage.MODALITY__PROHIBITION, newPROHIBITION, newPROHIBITION));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public What getPERMISSION() {
        return pERMISSION;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public NotificationChain basicSetPERMISSION(What newPERMISSION, NotificationChain msgs) {
        What oldPERMISSION = pERMISSION;
        pERMISSION = newPERMISSION;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, ContractPackage.MODALITY__PERMISSION, oldPERMISSION, newPERMISSION);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setPERMISSION(What newPERMISSION) {
        if (newPERMISSION != pERMISSION) {
            NotificationChain msgs = null;
            if (pERMISSION != null) msgs = ((InternalEObject) pERMISSION).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - ContractPackage.MODALITY__PERMISSION, null, msgs);
            if (newPERMISSION != null) msgs = ((InternalEObject) newPERMISSION).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - ContractPackage.MODALITY__PERMISSION, null, msgs);
            msgs = basicSetPERMISSION(newPERMISSION, msgs);
            if (msgs != null) msgs.dispatch();
        } else if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, ContractPackage.MODALITY__PERMISSION, newPERMISSION, newPERMISSION));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch(featureID) {
            case ContractPackage.MODALITY__OBLIGATION:
                return basicSetOBLIGATION(null, msgs);
            case ContractPackage.MODALITY__PROHIBITION:
                return basicSetPROHIBITION(null, msgs);
            case ContractPackage.MODALITY__PERMISSION:
                return basicSetPERMISSION(null, msgs);
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
            case ContractPackage.MODALITY__OBLIGATION:
                return getOBLIGATION();
            case ContractPackage.MODALITY__PROHIBITION:
                return getPROHIBITION();
            case ContractPackage.MODALITY__PERMISSION:
                return getPERMISSION();
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
            case ContractPackage.MODALITY__OBLIGATION:
                setOBLIGATION((What) newValue);
                return;
            case ContractPackage.MODALITY__PROHIBITION:
                setPROHIBITION((What) newValue);
                return;
            case ContractPackage.MODALITY__PERMISSION:
                setPERMISSION((What) newValue);
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
            case ContractPackage.MODALITY__OBLIGATION:
                setOBLIGATION((What) null);
                return;
            case ContractPackage.MODALITY__PROHIBITION:
                setPROHIBITION((What) null);
                return;
            case ContractPackage.MODALITY__PERMISSION:
                setPERMISSION((What) null);
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
            case ContractPackage.MODALITY__OBLIGATION:
                return oBLIGATION != null;
            case ContractPackage.MODALITY__PROHIBITION:
                return pROHIBITION != null;
            case ContractPackage.MODALITY__PERMISSION:
                return pERMISSION != null;
        }
        return super.eIsSet(featureID);
    }
}
