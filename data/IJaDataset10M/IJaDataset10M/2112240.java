package odm.impl;

import odm.AntisymmetricObjectProperty;
import odm.ObjectProperty;
import odm.OdmPackage;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Antisymmetric Object Property</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link odm.impl.AntisymmetricObjectPropertyImpl#getObjectPropertyExpression <em>Object Property Expression</em>}</li>
 *   <li>{@link odm.impl.AntisymmetricObjectPropertyImpl#getTag <em>Tag</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class AntisymmetricObjectPropertyImpl extends ObjectPropertyAxiomImpl implements AntisymmetricObjectProperty {

    /**
	 * The cached value of the '{@link #getObjectPropertyExpression() <em>Object Property Expression</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getObjectPropertyExpression()
	 * @generated
	 * @ordered
	 */
    protected ObjectProperty objectPropertyExpression = null;

    /**
	 * The default value of the '{@link #getTag() <em>Tag</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getTag()
	 * @generated
	 * @ordered
	 */
    protected static final String TAG_EDEFAULT = null;

    /**
	 * The cached value of the '{@link #getTag() <em>Tag</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getTag()
	 * @generated
	 * @ordered
	 */
    protected String tag = TAG_EDEFAULT;

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    protected AntisymmetricObjectPropertyImpl() {
        super();
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    protected EClass eStaticClass() {
        return OdmPackage.Literals.ANTISYMMETRIC_OBJECT_PROPERTY;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public ObjectProperty getObjectPropertyExpression() {
        if (objectPropertyExpression != null && objectPropertyExpression.eIsProxy()) {
            InternalEObject oldObjectPropertyExpression = (InternalEObject) objectPropertyExpression;
            objectPropertyExpression = (ObjectProperty) eResolveProxy(oldObjectPropertyExpression);
            if (objectPropertyExpression != oldObjectPropertyExpression) {
                if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.RESOLVE, OdmPackage.ANTISYMMETRIC_OBJECT_PROPERTY__OBJECT_PROPERTY_EXPRESSION, oldObjectPropertyExpression, objectPropertyExpression));
            }
        }
        return objectPropertyExpression;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public ObjectProperty basicGetObjectPropertyExpression() {
        return objectPropertyExpression;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setObjectPropertyExpression(ObjectProperty newObjectPropertyExpression) {
        ObjectProperty oldObjectPropertyExpression = objectPropertyExpression;
        objectPropertyExpression = newObjectPropertyExpression;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, OdmPackage.ANTISYMMETRIC_OBJECT_PROPERTY__OBJECT_PROPERTY_EXPRESSION, oldObjectPropertyExpression, objectPropertyExpression));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public String getTag() {
        return tag;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setTag(String newTag) {
        String oldTag = tag;
        tag = newTag;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, OdmPackage.ANTISYMMETRIC_OBJECT_PROPERTY__TAG, oldTag, tag));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch(featureID) {
            case OdmPackage.ANTISYMMETRIC_OBJECT_PROPERTY__OBJECT_PROPERTY_EXPRESSION:
                if (resolve) return getObjectPropertyExpression();
                return basicGetObjectPropertyExpression();
            case OdmPackage.ANTISYMMETRIC_OBJECT_PROPERTY__TAG:
                return getTag();
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
            case OdmPackage.ANTISYMMETRIC_OBJECT_PROPERTY__OBJECT_PROPERTY_EXPRESSION:
                setObjectPropertyExpression((ObjectProperty) newValue);
                return;
            case OdmPackage.ANTISYMMETRIC_OBJECT_PROPERTY__TAG:
                setTag((String) newValue);
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
            case OdmPackage.ANTISYMMETRIC_OBJECT_PROPERTY__OBJECT_PROPERTY_EXPRESSION:
                setObjectPropertyExpression((ObjectProperty) null);
                return;
            case OdmPackage.ANTISYMMETRIC_OBJECT_PROPERTY__TAG:
                setTag(TAG_EDEFAULT);
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
            case OdmPackage.ANTISYMMETRIC_OBJECT_PROPERTY__OBJECT_PROPERTY_EXPRESSION:
                return objectPropertyExpression != null;
            case OdmPackage.ANTISYMMETRIC_OBJECT_PROPERTY__TAG:
                return TAG_EDEFAULT == null ? tag != null : !TAG_EDEFAULT.equals(tag);
        }
        return super.eIsSet(featureID);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public String toString() {
        if (eIsProxy()) return super.toString();
        StringBuffer result = new StringBuffer(super.toString());
        result.append(" (tag: ");
        result.append(tag);
        result.append(')');
        return result.toString();
    }
}
