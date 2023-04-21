package org.eclipse.xtext.example.fj.impl;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.xtext.example.fj.FjPackage;
import org.eclipse.xtext.example.fj.Parameter;
import org.eclipse.xtext.example.fj.Variable;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Variable</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.xtext.example.fj.impl.VariableImpl#getParamref <em>Paramref</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class VariableImpl extends ExpressionImpl implements Variable {

    /**
   * The cached value of the '{@link #getParamref() <em>Paramref</em>}' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getParamref()
   * @generated
   * @ordered
   */
    protected Parameter paramref;

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    protected VariableImpl() {
        super();
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    @Override
    protected EClass eStaticClass() {
        return FjPackage.Literals.VARIABLE;
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public Parameter getParamref() {
        if (paramref != null && paramref.eIsProxy()) {
            InternalEObject oldParamref = (InternalEObject) paramref;
            paramref = (Parameter) eResolveProxy(oldParamref);
            if (paramref != oldParamref) {
                if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.RESOLVE, FjPackage.VARIABLE__PARAMREF, oldParamref, paramref));
            }
        }
        return paramref;
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public Parameter basicGetParamref() {
        return paramref;
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public void setParamref(Parameter newParamref) {
        Parameter oldParamref = paramref;
        paramref = newParamref;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, FjPackage.VARIABLE__PARAMREF, oldParamref, paramref));
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch(featureID) {
            case FjPackage.VARIABLE__PARAMREF:
                if (resolve) return getParamref();
                return basicGetParamref();
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
            case FjPackage.VARIABLE__PARAMREF:
                setParamref((Parameter) newValue);
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
            case FjPackage.VARIABLE__PARAMREF:
                setParamref((Parameter) null);
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
            case FjPackage.VARIABLE__PARAMREF:
                return paramref != null;
        }
        return super.eIsSet(featureID);
    }
}
