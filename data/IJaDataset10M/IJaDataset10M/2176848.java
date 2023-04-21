package hu.cubussapiens.modembed.model.atomicimpl.impl;

import hu.cubussapiens.modembed.model.atomicimpl.AtomicImplPackage;
import hu.cubussapiens.modembed.model.atomicimpl.DataElementVariable;
import hu.cubussapiens.modembed.model.components.DataElement;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Data Element Variable</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link hu.cubussapiens.modembed.model.atomicimpl.impl.DataElementVariableImpl#getDataelement <em>Dataelement</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class DataElementVariableImpl extends VariableImpl implements DataElementVariable {

    /**
	 * The cached value of the '{@link #getDataelement() <em>Dataelement</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDataelement()
	 * @generated
	 * @ordered
	 */
    protected DataElement dataelement;

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    protected DataElementVariableImpl() {
        super();
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    protected EClass eStaticClass() {
        return AtomicImplPackage.Literals.DATA_ELEMENT_VARIABLE;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public DataElement getDataelement() {
        if (dataelement != null && dataelement.eIsProxy()) {
            InternalEObject oldDataelement = (InternalEObject) dataelement;
            dataelement = (DataElement) eResolveProxy(oldDataelement);
            if (dataelement != oldDataelement) {
                if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.RESOLVE, AtomicImplPackage.DATA_ELEMENT_VARIABLE__DATAELEMENT, oldDataelement, dataelement));
            }
        }
        return dataelement;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public DataElement basicGetDataelement() {
        return dataelement;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setDataelement(DataElement newDataelement) {
        DataElement oldDataelement = dataelement;
        dataelement = newDataelement;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, AtomicImplPackage.DATA_ELEMENT_VARIABLE__DATAELEMENT, oldDataelement, dataelement));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch(featureID) {
            case AtomicImplPackage.DATA_ELEMENT_VARIABLE__DATAELEMENT:
                if (resolve) return getDataelement();
                return basicGetDataelement();
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
            case AtomicImplPackage.DATA_ELEMENT_VARIABLE__DATAELEMENT:
                setDataelement((DataElement) newValue);
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
            case AtomicImplPackage.DATA_ELEMENT_VARIABLE__DATAELEMENT:
                setDataelement((DataElement) null);
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
            case AtomicImplPackage.DATA_ELEMENT_VARIABLE__DATAELEMENT:
                return dataelement != null;
        }
        return super.eIsSet(featureID);
    }
}
