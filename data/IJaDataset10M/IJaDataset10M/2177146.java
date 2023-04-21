package org.xtext.example.swrtj.impl;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.xtext.example.swrtj.FieldName;
import org.xtext.example.swrtj.RecordExclude;
import org.xtext.example.swrtj.SwrtjPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Record Exclude</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.xtext.example.swrtj.impl.RecordExcludeImpl#getField <em>Field</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class RecordExcludeImpl extends RecordOperationImpl implements RecordExclude {

    /**
   * The cached value of the '{@link #getField() <em>Field</em>}' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getField()
   * @generated
   * @ordered
   */
    protected FieldName field;

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    protected RecordExcludeImpl() {
        super();
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    @Override
    protected EClass eStaticClass() {
        return SwrtjPackage.Literals.RECORD_EXCLUDE;
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public FieldName getField() {
        if (field != null && field.eIsProxy()) {
            InternalEObject oldField = (InternalEObject) field;
            field = (FieldName) eResolveProxy(oldField);
            if (field != oldField) {
                if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.RESOLVE, SwrtjPackage.RECORD_EXCLUDE__FIELD, oldField, field));
            }
        }
        return field;
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public FieldName basicGetField() {
        return field;
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public void setField(FieldName newField) {
        FieldName oldField = field;
        field = newField;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, SwrtjPackage.RECORD_EXCLUDE__FIELD, oldField, field));
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch(featureID) {
            case SwrtjPackage.RECORD_EXCLUDE__FIELD:
                if (resolve) return getField();
                return basicGetField();
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
            case SwrtjPackage.RECORD_EXCLUDE__FIELD:
                setField((FieldName) newValue);
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
            case SwrtjPackage.RECORD_EXCLUDE__FIELD:
                setField((FieldName) null);
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
            case SwrtjPackage.RECORD_EXCLUDE__FIELD:
                return field != null;
        }
        return super.eIsSet(featureID);
    }
}
