package fr.msimeon.mads.mads.impl;

import fr.msimeon.mads.mads.MadsPackage;
import fr.msimeon.mads.mads.Variable;
import fr.msimeon.mads.mads.VariableReference;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Variable Reference</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link fr.msimeon.mads.mads.impl.VariableReferenceImpl#getVarRef <em>Var Ref</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class VariableReferenceImpl extends AtomIDImpl implements VariableReference {

    /**
   * The cached value of the '{@link #getVarRef() <em>Var Ref</em>}' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getVarRef()
   * @generated
   * @ordered
   */
    protected Variable varRef;

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    protected VariableReferenceImpl() {
        super();
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    @Override
    protected EClass eStaticClass() {
        return MadsPackage.Literals.VARIABLE_REFERENCE;
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public Variable getVarRef() {
        if (varRef != null && varRef.eIsProxy()) {
            InternalEObject oldVarRef = (InternalEObject) varRef;
            varRef = (Variable) eResolveProxy(oldVarRef);
            if (varRef != oldVarRef) {
                if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.RESOLVE, MadsPackage.VARIABLE_REFERENCE__VAR_REF, oldVarRef, varRef));
            }
        }
        return varRef;
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public Variable basicGetVarRef() {
        return varRef;
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public void setVarRef(Variable newVarRef) {
        Variable oldVarRef = varRef;
        varRef = newVarRef;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, MadsPackage.VARIABLE_REFERENCE__VAR_REF, oldVarRef, varRef));
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch(featureID) {
            case MadsPackage.VARIABLE_REFERENCE__VAR_REF:
                if (resolve) return getVarRef();
                return basicGetVarRef();
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
            case MadsPackage.VARIABLE_REFERENCE__VAR_REF:
                setVarRef((Variable) newValue);
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
            case MadsPackage.VARIABLE_REFERENCE__VAR_REF:
                setVarRef((Variable) null);
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
            case MadsPackage.VARIABLE_REFERENCE__VAR_REF:
                return varRef != null;
        }
        return super.eIsSet(featureID);
    }
}
