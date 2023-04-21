package fr.msimeon.mads.mads.impl;

import fr.msimeon.mads.mads.AtomID;
import fr.msimeon.mads.mads.MadsPackage;
import fr.msimeon.mads.mads.ShiftFunc;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Shift Func</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link fr.msimeon.mads.mads.impl.ShiftFuncImpl#getShift <em>Shift</em>}</li>
 *   <li>{@link fr.msimeon.mads.mads.impl.ShiftFuncImpl#getVarRef <em>Var Ref</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class ShiftFuncImpl extends FunctionImpl implements ShiftFunc {

    /**
   * The default value of the '{@link #getShift() <em>Shift</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getShift()
   * @generated
   * @ordered
   */
    protected static final int SHIFT_EDEFAULT = 0;

    /**
   * The cached value of the '{@link #getShift() <em>Shift</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getShift()
   * @generated
   * @ordered
   */
    protected int shift = SHIFT_EDEFAULT;

    /**
   * The cached value of the '{@link #getVarRef() <em>Var Ref</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getVarRef()
   * @generated
   * @ordered
   */
    protected AtomID varRef;

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    protected ShiftFuncImpl() {
        super();
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    @Override
    protected EClass eStaticClass() {
        return MadsPackage.Literals.SHIFT_FUNC;
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public int getShift() {
        return shift;
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public void setShift(int newShift) {
        int oldShift = shift;
        shift = newShift;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, MadsPackage.SHIFT_FUNC__SHIFT, oldShift, shift));
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public AtomID getVarRef() {
        return varRef;
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public NotificationChain basicSetVarRef(AtomID newVarRef, NotificationChain msgs) {
        AtomID oldVarRef = varRef;
        varRef = newVarRef;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, MadsPackage.SHIFT_FUNC__VAR_REF, oldVarRef, newVarRef);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public void setVarRef(AtomID newVarRef) {
        if (newVarRef != varRef) {
            NotificationChain msgs = null;
            if (varRef != null) msgs = ((InternalEObject) varRef).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - MadsPackage.SHIFT_FUNC__VAR_REF, null, msgs);
            if (newVarRef != null) msgs = ((InternalEObject) newVarRef).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - MadsPackage.SHIFT_FUNC__VAR_REF, null, msgs);
            msgs = basicSetVarRef(newVarRef, msgs);
            if (msgs != null) msgs.dispatch();
        } else if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, MadsPackage.SHIFT_FUNC__VAR_REF, newVarRef, newVarRef));
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch(featureID) {
            case MadsPackage.SHIFT_FUNC__VAR_REF:
                return basicSetVarRef(null, msgs);
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
            case MadsPackage.SHIFT_FUNC__SHIFT:
                return getShift();
            case MadsPackage.SHIFT_FUNC__VAR_REF:
                return getVarRef();
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
            case MadsPackage.SHIFT_FUNC__SHIFT:
                setShift((Integer) newValue);
                return;
            case MadsPackage.SHIFT_FUNC__VAR_REF:
                setVarRef((AtomID) newValue);
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
            case MadsPackage.SHIFT_FUNC__SHIFT:
                setShift(SHIFT_EDEFAULT);
                return;
            case MadsPackage.SHIFT_FUNC__VAR_REF:
                setVarRef((AtomID) null);
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
            case MadsPackage.SHIFT_FUNC__SHIFT:
                return shift != SHIFT_EDEFAULT;
            case MadsPackage.SHIFT_FUNC__VAR_REF:
                return varRef != null;
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
        result.append(" (shift: ");
        result.append(shift);
        result.append(')');
        return result.toString();
    }
}
