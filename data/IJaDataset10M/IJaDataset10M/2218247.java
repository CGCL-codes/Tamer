package assign.impl;

import assign.AssignPackage;
import assign.CopyObject;
import assign.Expression;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Copy Object</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link assign.impl.CopyObjectImpl#getObject <em>Object</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class CopyObjectImpl extends ExpressionImpl implements CopyObject {

    /**
	 * The cached value of the '{@link #getObject() <em>Object</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getObject()
	 * @generated
	 * @ordered
	 */
    protected Expression object = null;

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    protected CopyObjectImpl() {
        super();
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    protected EClass eStaticClass() {
        return AssignPackage.Literals.COPY_OBJECT;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public Expression getObject() {
        return object;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public NotificationChain basicSetObject(Expression newObject, NotificationChain msgs) {
        Expression oldObject = object;
        object = newObject;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, AssignPackage.COPY_OBJECT__OBJECT, oldObject, newObject);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setObject(Expression newObject) {
        if (newObject != object) {
            NotificationChain msgs = null;
            if (object != null) msgs = ((InternalEObject) object).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - AssignPackage.COPY_OBJECT__OBJECT, null, msgs);
            if (newObject != null) msgs = ((InternalEObject) newObject).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - AssignPackage.COPY_OBJECT__OBJECT, null, msgs);
            msgs = basicSetObject(newObject, msgs);
            if (msgs != null) msgs.dispatch();
        } else if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, AssignPackage.COPY_OBJECT__OBJECT, newObject, newObject));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch(featureID) {
            case AssignPackage.COPY_OBJECT__OBJECT:
                return basicSetObject(null, msgs);
        }
        return super.eInverseRemove(otherEnd, featureID, msgs);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch(featureID) {
            case AssignPackage.COPY_OBJECT__OBJECT:
                return getObject();
        }
        return super.eGet(featureID, resolve, coreType);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void eSet(int featureID, Object newValue) {
        switch(featureID) {
            case AssignPackage.COPY_OBJECT__OBJECT:
                setObject((Expression) newValue);
                return;
        }
        super.eSet(featureID, newValue);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void eUnset(int featureID) {
        switch(featureID) {
            case AssignPackage.COPY_OBJECT__OBJECT:
                setObject((Expression) null);
                return;
        }
        super.eUnset(featureID);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public boolean eIsSet(int featureID) {
        switch(featureID) {
            case AssignPackage.COPY_OBJECT__OBJECT:
                return object != null;
        }
        return super.eIsSet(featureID);
    }

    @Override
    public EClassifier getType() {
        return getObject().getType();
    }
}
