package odm.impl;

import odm.FunctionalObjectProperty;
import odm.ObjectProperty;
import odm.OdmPackage;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Functional Object Property</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link odm.impl.FunctionalObjectPropertyImpl#getObjectpropertyexpression <em>Objectpropertyexpression</em>}</li>
 *   <li>{@link odm.impl.FunctionalObjectPropertyImpl#getTag <em>Tag</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class FunctionalObjectPropertyImpl extends ObjectPropertyAxiomImpl implements FunctionalObjectProperty {

    /**
	 * The cached value of the '{@link #getObjectpropertyexpression() <em>Objectpropertyexpression</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getObjectpropertyexpression()
	 * @generated
	 * @ordered
	 */
    protected ObjectProperty objectpropertyexpression = null;

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
    protected FunctionalObjectPropertyImpl() {
        super();
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    protected EClass eStaticClass() {
        return OdmPackage.Literals.FUNCTIONAL_OBJECT_PROPERTY;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public ObjectProperty getObjectpropertyexpression() {
        if (objectpropertyexpression != null && objectpropertyexpression.eIsProxy()) {
            InternalEObject oldObjectpropertyexpression = (InternalEObject) objectpropertyexpression;
            objectpropertyexpression = (ObjectProperty) eResolveProxy(oldObjectpropertyexpression);
            if (objectpropertyexpression != oldObjectpropertyexpression) {
                if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.RESOLVE, OdmPackage.FUNCTIONAL_OBJECT_PROPERTY__OBJECTPROPERTYEXPRESSION, oldObjectpropertyexpression, objectpropertyexpression));
            }
        }
        return objectpropertyexpression;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public ObjectProperty basicGetObjectpropertyexpression() {
        return objectpropertyexpression;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setObjectpropertyexpression(ObjectProperty newObjectpropertyexpression) {
        ObjectProperty oldObjectpropertyexpression = objectpropertyexpression;
        objectpropertyexpression = newObjectpropertyexpression;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, OdmPackage.FUNCTIONAL_OBJECT_PROPERTY__OBJECTPROPERTYEXPRESSION, oldObjectpropertyexpression, objectpropertyexpression));
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
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, OdmPackage.FUNCTIONAL_OBJECT_PROPERTY__TAG, oldTag, tag));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch(featureID) {
            case OdmPackage.FUNCTIONAL_OBJECT_PROPERTY__OBJECTPROPERTYEXPRESSION:
                if (resolve) return getObjectpropertyexpression();
                return basicGetObjectpropertyexpression();
            case OdmPackage.FUNCTIONAL_OBJECT_PROPERTY__TAG:
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
            case OdmPackage.FUNCTIONAL_OBJECT_PROPERTY__OBJECTPROPERTYEXPRESSION:
                setObjectpropertyexpression((ObjectProperty) newValue);
                return;
            case OdmPackage.FUNCTIONAL_OBJECT_PROPERTY__TAG:
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
            case OdmPackage.FUNCTIONAL_OBJECT_PROPERTY__OBJECTPROPERTYEXPRESSION:
                setObjectpropertyexpression((ObjectProperty) null);
                return;
            case OdmPackage.FUNCTIONAL_OBJECT_PROPERTY__TAG:
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
            case OdmPackage.FUNCTIONAL_OBJECT_PROPERTY__OBJECTPROPERTYEXPRESSION:
                return objectpropertyexpression != null;
            case OdmPackage.FUNCTIONAL_OBJECT_PROPERTY__TAG:
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
