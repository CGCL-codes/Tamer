package org.xvr.s3D.impl;

import java.util.Collection;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;
import org.eclipse.emf.ecore.util.EDataTypeEList;
import org.xvr.s3D.S3DPackage;
import org.xvr.s3D.formalParamList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>formal Param List</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.xvr.s3D.impl.formalParamListImpl#getParams <em>Params</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class formalParamListImpl extends MinimalEObjectImpl.Container implements formalParamList {

    /**
   * The cached value of the '{@link #getParams() <em>Params</em>}' attribute list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getParams()
   * @generated
   * @ordered
   */
    protected EList<String> params;

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    protected formalParamListImpl() {
        super();
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    @Override
    protected EClass eStaticClass() {
        return S3DPackage.Literals.FORMAL_PARAM_LIST;
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public EList<String> getParams() {
        if (params == null) {
            params = new EDataTypeEList<String>(String.class, this, S3DPackage.FORMAL_PARAM_LIST__PARAMS);
        }
        return params;
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch(featureID) {
            case S3DPackage.FORMAL_PARAM_LIST__PARAMS:
                return getParams();
        }
        return super.eGet(featureID, resolve, coreType);
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    @SuppressWarnings("unchecked")
    @Override
    public void eSet(int featureID, Object newValue) {
        switch(featureID) {
            case S3DPackage.FORMAL_PARAM_LIST__PARAMS:
                getParams().clear();
                getParams().addAll((Collection<? extends String>) newValue);
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
            case S3DPackage.FORMAL_PARAM_LIST__PARAMS:
                getParams().clear();
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
            case S3DPackage.FORMAL_PARAM_LIST__PARAMS:
                return params != null && !params.isEmpty();
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
        result.append(" (params: ");
        result.append(params);
        result.append(')');
        return result.toString();
    }
}
