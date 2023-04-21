package org.xtext.example.swrtj.impl;

import java.util.Collection;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;
import org.xtext.example.swrtj.AnonimousRecord;
import org.xtext.example.swrtj.Field;
import org.xtext.example.swrtj.SwrtjPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Anonimous Record</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.xtext.example.swrtj.impl.AnonimousRecordImpl#getDeclarationList <em>Declaration List</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class AnonimousRecordImpl extends BaseRecordImpl implements AnonimousRecord {

    /**
   * The cached value of the '{@link #getDeclarationList() <em>Declaration List</em>}' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getDeclarationList()
   * @generated
   * @ordered
   */
    protected EList<Field> declarationList;

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    protected AnonimousRecordImpl() {
        super();
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    @Override
    protected EClass eStaticClass() {
        return SwrtjPackage.Literals.ANONIMOUS_RECORD;
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public EList<Field> getDeclarationList() {
        if (declarationList == null) {
            declarationList = new EObjectContainmentEList<Field>(Field.class, this, SwrtjPackage.ANONIMOUS_RECORD__DECLARATION_LIST);
        }
        return declarationList;
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch(featureID) {
            case SwrtjPackage.ANONIMOUS_RECORD__DECLARATION_LIST:
                return ((InternalEList<?>) getDeclarationList()).basicRemove(otherEnd, msgs);
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
            case SwrtjPackage.ANONIMOUS_RECORD__DECLARATION_LIST:
                return getDeclarationList();
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
            case SwrtjPackage.ANONIMOUS_RECORD__DECLARATION_LIST:
                getDeclarationList().clear();
                getDeclarationList().addAll((Collection<? extends Field>) newValue);
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
            case SwrtjPackage.ANONIMOUS_RECORD__DECLARATION_LIST:
                getDeclarationList().clear();
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
            case SwrtjPackage.ANONIMOUS_RECORD__DECLARATION_LIST:
                return declarationList != null && !declarationList.isEmpty();
        }
        return super.eIsSet(featureID);
    }
}
