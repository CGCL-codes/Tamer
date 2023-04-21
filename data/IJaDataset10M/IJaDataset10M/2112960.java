package iec61970.scada.impl;

import iec61970.core.impl.IdentifiedObjectImpl;
import iec61970.scada.RemotePoint;
import iec61970.scada.RemoteUnit;
import iec61970.scada.ScadaPackage;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Remote Point</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link iec61970.scada.impl.RemotePointImpl#getMemberOf_RemoteUnit <em>Member Of Remote Unit</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class RemotePointImpl extends IdentifiedObjectImpl implements RemotePoint {

    /**
	 * The cached value of the '{@link #getMemberOf_RemoteUnit() <em>Member Of Remote Unit</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMemberOf_RemoteUnit()
	 * @generated
	 * @ordered
	 */
    protected RemoteUnit memberOf_RemoteUnit;

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    protected RemotePointImpl() {
        super();
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    protected EClass eStaticClass() {
        return ScadaPackage.Literals.REMOTE_POINT;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public RemoteUnit getMemberOf_RemoteUnit() {
        if (memberOf_RemoteUnit != null && memberOf_RemoteUnit.eIsProxy()) {
            InternalEObject oldMemberOf_RemoteUnit = (InternalEObject) memberOf_RemoteUnit;
            memberOf_RemoteUnit = (RemoteUnit) eResolveProxy(oldMemberOf_RemoteUnit);
            if (memberOf_RemoteUnit != oldMemberOf_RemoteUnit) {
                if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.RESOLVE, ScadaPackage.REMOTE_POINT__MEMBER_OF_REMOTE_UNIT, oldMemberOf_RemoteUnit, memberOf_RemoteUnit));
            }
        }
        return memberOf_RemoteUnit;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public RemoteUnit basicGetMemberOf_RemoteUnit() {
        return memberOf_RemoteUnit;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public NotificationChain basicSetMemberOf_RemoteUnit(RemoteUnit newMemberOf_RemoteUnit, NotificationChain msgs) {
        RemoteUnit oldMemberOf_RemoteUnit = memberOf_RemoteUnit;
        memberOf_RemoteUnit = newMemberOf_RemoteUnit;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, ScadaPackage.REMOTE_POINT__MEMBER_OF_REMOTE_UNIT, oldMemberOf_RemoteUnit, newMemberOf_RemoteUnit);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setMemberOf_RemoteUnit(RemoteUnit newMemberOf_RemoteUnit) {
        if (newMemberOf_RemoteUnit != memberOf_RemoteUnit) {
            NotificationChain msgs = null;
            if (memberOf_RemoteUnit != null) msgs = ((InternalEObject) memberOf_RemoteUnit).eInverseRemove(this, ScadaPackage.REMOTE_UNIT__CONTAINS_REMOTE_POINTS, RemoteUnit.class, msgs);
            if (newMemberOf_RemoteUnit != null) msgs = ((InternalEObject) newMemberOf_RemoteUnit).eInverseAdd(this, ScadaPackage.REMOTE_UNIT__CONTAINS_REMOTE_POINTS, RemoteUnit.class, msgs);
            msgs = basicSetMemberOf_RemoteUnit(newMemberOf_RemoteUnit, msgs);
            if (msgs != null) msgs.dispatch();
        } else if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, ScadaPackage.REMOTE_POINT__MEMBER_OF_REMOTE_UNIT, newMemberOf_RemoteUnit, newMemberOf_RemoteUnit));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    public NotificationChain eInverseAdd(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch(featureID) {
            case ScadaPackage.REMOTE_POINT__MEMBER_OF_REMOTE_UNIT:
                if (memberOf_RemoteUnit != null) msgs = ((InternalEObject) memberOf_RemoteUnit).eInverseRemove(this, ScadaPackage.REMOTE_UNIT__CONTAINS_REMOTE_POINTS, RemoteUnit.class, msgs);
                return basicSetMemberOf_RemoteUnit((RemoteUnit) otherEnd, msgs);
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
            case ScadaPackage.REMOTE_POINT__MEMBER_OF_REMOTE_UNIT:
                return basicSetMemberOf_RemoteUnit(null, msgs);
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
            case ScadaPackage.REMOTE_POINT__MEMBER_OF_REMOTE_UNIT:
                if (resolve) return getMemberOf_RemoteUnit();
                return basicGetMemberOf_RemoteUnit();
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
            case ScadaPackage.REMOTE_POINT__MEMBER_OF_REMOTE_UNIT:
                setMemberOf_RemoteUnit((RemoteUnit) newValue);
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
            case ScadaPackage.REMOTE_POINT__MEMBER_OF_REMOTE_UNIT:
                setMemberOf_RemoteUnit((RemoteUnit) null);
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
            case ScadaPackage.REMOTE_POINT__MEMBER_OF_REMOTE_UNIT:
                return memberOf_RemoteUnit != null;
        }
        return super.eIsSet(featureID);
    }
}
