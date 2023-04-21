package org.xtext.example.swrtj.impl;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.xtext.example.swrtj.Parameter;
import org.xtext.example.swrtj.ParameterReference;
import org.xtext.example.swrtj.SwrtjPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Parameter Reference</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.xtext.example.swrtj.impl.ParameterReferenceImpl#getParameter <em>Parameter</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class ParameterReferenceImpl extends StartImpl implements ParameterReference {

    /**
   * The cached value of the '{@link #getParameter() <em>Parameter</em>}' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getParameter()
   * @generated
   * @ordered
   */
    protected Parameter parameter;

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    protected ParameterReferenceImpl() {
        super();
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    @Override
    protected EClass eStaticClass() {
        return SwrtjPackage.Literals.PARAMETER_REFERENCE;
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public Parameter getParameter() {
        if (parameter != null && parameter.eIsProxy()) {
            InternalEObject oldParameter = (InternalEObject) parameter;
            parameter = (Parameter) eResolveProxy(oldParameter);
            if (parameter != oldParameter) {
                if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.RESOLVE, SwrtjPackage.PARAMETER_REFERENCE__PARAMETER, oldParameter, parameter));
            }
        }
        return parameter;
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public Parameter basicGetParameter() {
        return parameter;
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public void setParameter(Parameter newParameter) {
        Parameter oldParameter = parameter;
        parameter = newParameter;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, SwrtjPackage.PARAMETER_REFERENCE__PARAMETER, oldParameter, parameter));
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch(featureID) {
            case SwrtjPackage.PARAMETER_REFERENCE__PARAMETER:
                if (resolve) return getParameter();
                return basicGetParameter();
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
            case SwrtjPackage.PARAMETER_REFERENCE__PARAMETER:
                setParameter((Parameter) newValue);
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
            case SwrtjPackage.PARAMETER_REFERENCE__PARAMETER:
                setParameter((Parameter) null);
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
            case SwrtjPackage.PARAMETER_REFERENCE__PARAMETER:
                return parameter != null;
        }
        return super.eIsSet(featureID);
    }
}
