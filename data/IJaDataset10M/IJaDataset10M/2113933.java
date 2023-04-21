package org.xtext.example.swrtj.impl;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.xtext.example.swrtj.FieldName;
import org.xtext.example.swrtj.SwrtjPackage;
import org.xtext.example.swrtj.TraitFieldRename;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Trait Field Rename</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.xtext.example.swrtj.impl.TraitFieldRenameImpl#getOriginalField <em>Original Field</em>}</li>
 *   <li>{@link org.xtext.example.swrtj.impl.TraitFieldRenameImpl#getNewField <em>New Field</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class TraitFieldRenameImpl extends TraitOperationImpl implements TraitFieldRename {

    /**
   * The cached value of the '{@link #getOriginalField() <em>Original Field</em>}' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getOriginalField()
   * @generated
   * @ordered
   */
    protected FieldName originalField;

    /**
   * The cached value of the '{@link #getNewField() <em>New Field</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getNewField()
   * @generated
   * @ordered
   */
    protected FieldName newField;

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    protected TraitFieldRenameImpl() {
        super();
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    @Override
    protected EClass eStaticClass() {
        return SwrtjPackage.Literals.TRAIT_FIELD_RENAME;
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public FieldName getOriginalField() {
        if (originalField != null && originalField.eIsProxy()) {
            InternalEObject oldOriginalField = (InternalEObject) originalField;
            originalField = (FieldName) eResolveProxy(oldOriginalField);
            if (originalField != oldOriginalField) {
                if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.RESOLVE, SwrtjPackage.TRAIT_FIELD_RENAME__ORIGINAL_FIELD, oldOriginalField, originalField));
            }
        }
        return originalField;
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public FieldName basicGetOriginalField() {
        return originalField;
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public void setOriginalField(FieldName newOriginalField) {
        FieldName oldOriginalField = originalField;
        originalField = newOriginalField;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, SwrtjPackage.TRAIT_FIELD_RENAME__ORIGINAL_FIELD, oldOriginalField, originalField));
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public FieldName getNewField() {
        return newField;
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public NotificationChain basicSetNewField(FieldName newNewField, NotificationChain msgs) {
        FieldName oldNewField = newField;
        newField = newNewField;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, SwrtjPackage.TRAIT_FIELD_RENAME__NEW_FIELD, oldNewField, newNewField);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public void setNewField(FieldName newNewField) {
        if (newNewField != newField) {
            NotificationChain msgs = null;
            if (newField != null) msgs = ((InternalEObject) newField).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - SwrtjPackage.TRAIT_FIELD_RENAME__NEW_FIELD, null, msgs);
            if (newNewField != null) msgs = ((InternalEObject) newNewField).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - SwrtjPackage.TRAIT_FIELD_RENAME__NEW_FIELD, null, msgs);
            msgs = basicSetNewField(newNewField, msgs);
            if (msgs != null) msgs.dispatch();
        } else if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, SwrtjPackage.TRAIT_FIELD_RENAME__NEW_FIELD, newNewField, newNewField));
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch(featureID) {
            case SwrtjPackage.TRAIT_FIELD_RENAME__NEW_FIELD:
                return basicSetNewField(null, msgs);
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
            case SwrtjPackage.TRAIT_FIELD_RENAME__ORIGINAL_FIELD:
                if (resolve) return getOriginalField();
                return basicGetOriginalField();
            case SwrtjPackage.TRAIT_FIELD_RENAME__NEW_FIELD:
                return getNewField();
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
            case SwrtjPackage.TRAIT_FIELD_RENAME__ORIGINAL_FIELD:
                setOriginalField((FieldName) newValue);
                return;
            case SwrtjPackage.TRAIT_FIELD_RENAME__NEW_FIELD:
                setNewField((FieldName) newValue);
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
            case SwrtjPackage.TRAIT_FIELD_RENAME__ORIGINAL_FIELD:
                setOriginalField((FieldName) null);
                return;
            case SwrtjPackage.TRAIT_FIELD_RENAME__NEW_FIELD:
                setNewField((FieldName) null);
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
            case SwrtjPackage.TRAIT_FIELD_RENAME__ORIGINAL_FIELD:
                return originalField != null;
            case SwrtjPackage.TRAIT_FIELD_RENAME__NEW_FIELD:
                return newField != null;
        }
        return super.eIsSet(featureID);
    }
}
