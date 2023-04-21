package eu.medeia.ecore.apmm.bm.impl;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import eu.medeia.ecore.apmm.bm.AlarmPort;
import eu.medeia.ecore.apmm.bm.BmPackage;
import eu.medeia.ecore.apmm.library.AlarmPortStencil;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Alarm Port</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link eu.medeia.ecore.apmm.bm.impl.AlarmPortImpl#getType <em>Type</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class AlarmPortImpl extends ComponentPortImpl implements AlarmPort {

    /**
	 * The cached value of the '{@link #getType() <em>Type</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getType()
	 * @generated
	 * @ordered
	 */
    protected AlarmPortStencil type;

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    protected AlarmPortImpl() {
        super();
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    protected EClass eStaticClass() {
        return BmPackage.Literals.ALARM_PORT;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public AlarmPortStencil getType() {
        if (type != null && type.eIsProxy()) {
            InternalEObject oldType = (InternalEObject) type;
            type = (AlarmPortStencil) eResolveProxy(oldType);
            if (type != oldType) {
                if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.RESOLVE, BmPackage.ALARM_PORT__TYPE, oldType, type));
            }
        }
        return type;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public AlarmPortStencil basicGetType() {
        return type;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setType(AlarmPortStencil newType) {
        AlarmPortStencil oldType = type;
        type = newType;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, BmPackage.ALARM_PORT__TYPE, oldType, type));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch(featureID) {
            case BmPackage.ALARM_PORT__TYPE:
                if (resolve) return getType();
                return basicGetType();
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
            case BmPackage.ALARM_PORT__TYPE:
                setType((AlarmPortStencil) newValue);
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
            case BmPackage.ALARM_PORT__TYPE:
                setType((AlarmPortStencil) null);
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
            case BmPackage.ALARM_PORT__TYPE:
                return type != null;
        }
        return super.eIsSet(featureID);
    }
}
