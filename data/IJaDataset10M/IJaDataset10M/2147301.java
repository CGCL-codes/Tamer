package jfb.examples.gmf.school.impl;

import jfb.examples.gmf.school.Diagram;
import jfb.examples.gmf.school.School;
import jfb.examples.gmf.school.SchoolPackage;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Diagram</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link jfb.examples.gmf.school.impl.DiagramImpl#getSchool <em>School</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class DiagramImpl extends EObjectImpl implements Diagram {

    /**
	 * The cached value of the '{@link #getSchool() <em>School</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSchool()
	 * @generated
	 * @ordered
	 */
    protected School school;

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    protected DiagramImpl() {
        super();
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    protected EClass eStaticClass() {
        return SchoolPackage.Literals.DIAGRAM;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public School getSchool() {
        return school;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public NotificationChain basicSetSchool(School newSchool, NotificationChain msgs) {
        School oldSchool = school;
        school = newSchool;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, SchoolPackage.DIAGRAM__SCHOOL, oldSchool, newSchool);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setSchool(School newSchool) {
        if (newSchool != school) {
            NotificationChain msgs = null;
            if (school != null) msgs = ((InternalEObject) school).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - SchoolPackage.DIAGRAM__SCHOOL, null, msgs);
            if (newSchool != null) msgs = ((InternalEObject) newSchool).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - SchoolPackage.DIAGRAM__SCHOOL, null, msgs);
            msgs = basicSetSchool(newSchool, msgs);
            if (msgs != null) msgs.dispatch();
        } else if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, SchoolPackage.DIAGRAM__SCHOOL, newSchool, newSchool));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch(featureID) {
            case SchoolPackage.DIAGRAM__SCHOOL:
                return basicSetSchool(null, msgs);
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
            case SchoolPackage.DIAGRAM__SCHOOL:
                return getSchool();
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
            case SchoolPackage.DIAGRAM__SCHOOL:
                setSchool((School) newValue);
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
            case SchoolPackage.DIAGRAM__SCHOOL:
                setSchool((School) null);
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
            case SchoolPackage.DIAGRAM__SCHOOL:
                return school != null;
        }
        return super.eIsSet(featureID);
    }
}
