package org.deltaj.constraints.constraints.impl;

import org.deltaj.constraints.constraints.ClassType;
import org.deltaj.constraints.constraints.ConstraintsPackage;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.impl.ENotificationImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Class Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.deltaj.constraints.constraints.impl.ClassTypeImpl#getClassref <em>Classref</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class ClassTypeImpl extends SimpleTypeImpl implements ClassType {

    /**
   * The default value of the '{@link #getClassref() <em>Classref</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getClassref()
   * @generated
   * @ordered
   */
    protected static final String CLASSREF_EDEFAULT = null;

    /**
   * The cached value of the '{@link #getClassref() <em>Classref</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getClassref()
   * @generated
   * @ordered
   */
    protected String classref = CLASSREF_EDEFAULT;

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    protected ClassTypeImpl() {
        super();
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    @Override
    protected EClass eStaticClass() {
        return ConstraintsPackage.Literals.CLASS_TYPE;
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public String getClassref() {
        return classref;
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public void setClassref(String newClassref) {
        String oldClassref = classref;
        classref = newClassref;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, ConstraintsPackage.CLASS_TYPE__CLASSREF, oldClassref, classref));
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch(featureID) {
            case ConstraintsPackage.CLASS_TYPE__CLASSREF:
                return getClassref();
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
            case ConstraintsPackage.CLASS_TYPE__CLASSREF:
                setClassref((String) newValue);
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
            case ConstraintsPackage.CLASS_TYPE__CLASSREF:
                setClassref(CLASSREF_EDEFAULT);
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
            case ConstraintsPackage.CLASS_TYPE__CLASSREF:
                return CLASSREF_EDEFAULT == null ? classref != null : !CLASSREF_EDEFAULT.equals(classref);
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
        result.append(" (classref: ");
        result.append(classref);
        result.append(')');
        return result.toString();
    }
}
