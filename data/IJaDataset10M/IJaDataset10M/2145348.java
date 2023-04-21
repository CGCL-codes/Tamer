package org.emftext.language.owl.impl;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.emftext.language.owl.DataRange;
import org.emftext.language.owl.Datatype;
import org.emftext.language.owl.OwlPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Datatype</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.emftext.language.owl.impl.DatatypeImpl#getDataRange <em>Data Range</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class DatatypeImpl extends ClassImpl implements Datatype {

    /**
	 * The cached value of the '{@link #getDataRange() <em>Data Range</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDataRange()
	 * @generated
	 * @ordered
	 */
    protected DataRange dataRange;

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    protected DatatypeImpl() {
        super();
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    protected EClass eStaticClass() {
        return OwlPackage.Literals.DATATYPE;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public DataRange getDataRange() {
        return dataRange;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public NotificationChain basicSetDataRange(DataRange newDataRange, NotificationChain msgs) {
        DataRange oldDataRange = dataRange;
        dataRange = newDataRange;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, OwlPackage.DATATYPE__DATA_RANGE, oldDataRange, newDataRange);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setDataRange(DataRange newDataRange) {
        if (newDataRange != dataRange) {
            NotificationChain msgs = null;
            if (dataRange != null) msgs = ((InternalEObject) dataRange).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - OwlPackage.DATATYPE__DATA_RANGE, null, msgs);
            if (newDataRange != null) msgs = ((InternalEObject) newDataRange).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - OwlPackage.DATATYPE__DATA_RANGE, null, msgs);
            msgs = basicSetDataRange(newDataRange, msgs);
            if (msgs != null) msgs.dispatch();
        } else if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, OwlPackage.DATATYPE__DATA_RANGE, newDataRange, newDataRange));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch(featureID) {
            case OwlPackage.DATATYPE__DATA_RANGE:
                return basicSetDataRange(null, msgs);
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
            case OwlPackage.DATATYPE__DATA_RANGE:
                return getDataRange();
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
            case OwlPackage.DATATYPE__DATA_RANGE:
                setDataRange((DataRange) newValue);
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
            case OwlPackage.DATATYPE__DATA_RANGE:
                setDataRange((DataRange) null);
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
            case OwlPackage.DATATYPE__DATA_RANGE:
                return dataRange != null;
        }
        return super.eIsSet(featureID);
    }
}
