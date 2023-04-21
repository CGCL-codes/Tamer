package hu.cubussapiens.modembed.model.atomicimpl.impl;

import hu.cubussapiens.modembed.model.atomicimpl.AtomicImplPackage;
import hu.cubussapiens.modembed.model.atomicimpl.VariableInstance;
import hu.cubussapiens.modembed.model.datatypes.Datatype;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Variable Instance</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link hu.cubussapiens.modembed.model.atomicimpl.impl.VariableInstanceImpl#getType <em>Type</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class VariableInstanceImpl extends VariableImpl implements VariableInstance {

    /**
	 * The cached value of the '{@link #getType() <em>Type</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getType()
	 * @generated
	 * @ordered
	 */
    protected Datatype type;

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    protected VariableInstanceImpl() {
        super();
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    protected EClass eStaticClass() {
        return AtomicImplPackage.Literals.VARIABLE_INSTANCE;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public Datatype getType() {
        if (type != null && type.eIsProxy()) {
            InternalEObject oldType = (InternalEObject) type;
            type = (Datatype) eResolveProxy(oldType);
            if (type != oldType) {
                if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.RESOLVE, AtomicImplPackage.VARIABLE_INSTANCE__TYPE, oldType, type));
            }
        }
        return type;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public Datatype basicGetType() {
        return type;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setType(Datatype newType) {
        Datatype oldType = type;
        type = newType;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, AtomicImplPackage.VARIABLE_INSTANCE__TYPE, oldType, type));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch(featureID) {
            case AtomicImplPackage.VARIABLE_INSTANCE__TYPE:
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
            case AtomicImplPackage.VARIABLE_INSTANCE__TYPE:
                setType((Datatype) newValue);
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
            case AtomicImplPackage.VARIABLE_INSTANCE__TYPE:
                setType((Datatype) null);
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
            case AtomicImplPackage.VARIABLE_INSTANCE__TYPE:
                return type != null;
        }
        return super.eIsSet(featureID);
    }
}
