package odm.impl;

import odm.Description;
import odm.ObjectProperty;
import odm.ObjectSomeValuesFrom;
import odm.OdmPackage;
import odm.PropertyExpression;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Object Some Values From</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link odm.impl.ObjectSomeValuesFromImpl#getDescription <em>Description</em>}</li>
 *   <li>{@link odm.impl.ObjectSomeValuesFromImpl#getObjectPropertyExpression <em>Object Property Expression</em>}</li>
 *   <li>{@link odm.impl.ObjectSomeValuesFromImpl#getTag <em>Tag</em>}</li>
 *   <li>{@link odm.impl.ObjectSomeValuesFromImpl#getSomeValuesFromSourceDescription <em>Some Values From Source Description</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class ObjectSomeValuesFromImpl extends DescriptionImpl implements ObjectSomeValuesFrom {

    /**
	 * The cached value of the '{@link #getDescription() <em>Description</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDescription()
	 * @generated
	 * @ordered
	 */
    protected Description description = null;

    /**
	 * The cached value of the '{@link #getObjectPropertyExpression() <em>Object Property Expression</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getObjectPropertyExpression()
	 * @generated
	 * @ordered
	 */
    protected PropertyExpression objectPropertyExpression = null;

    /**
	 * The default value of the '{@link #getTag() <em>Tag</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getTag()
	 * @generated
	 * @ordered
	 */
    protected static final String TAG_EDEFAULT = "someValuesFrom";

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
	 * The cached value of the '{@link #getSomeValuesFromSourceDescription() <em>Some Values From Source Description</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSomeValuesFromSourceDescription()
	 * @generated
	 * @ordered
	 */
    protected Description someValuesFromSourceDescription = null;

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    protected ObjectSomeValuesFromImpl() {
        super();
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    protected EClass eStaticClass() {
        return OdmPackage.Literals.OBJECT_SOME_VALUES_FROM;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public Description getDescription() {
        if (description != null && description.eIsProxy()) {
            InternalEObject oldDescription = (InternalEObject) description;
            description = (Description) eResolveProxy(oldDescription);
            if (description != oldDescription) {
                if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.RESOLVE, OdmPackage.OBJECT_SOME_VALUES_FROM__DESCRIPTION, oldDescription, description));
            }
        }
        return description;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public Description basicGetDescription() {
        return description;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setDescription(Description newDescription) {
        Description oldDescription = description;
        description = newDescription;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, OdmPackage.OBJECT_SOME_VALUES_FROM__DESCRIPTION, oldDescription, description));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public PropertyExpression getObjectPropertyExpression() {
        if (objectPropertyExpression != null && objectPropertyExpression.eIsProxy()) {
            InternalEObject oldObjectPropertyExpression = (InternalEObject) objectPropertyExpression;
            objectPropertyExpression = (PropertyExpression) eResolveProxy(oldObjectPropertyExpression);
            if (objectPropertyExpression != oldObjectPropertyExpression) {
                if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.RESOLVE, OdmPackage.OBJECT_SOME_VALUES_FROM__OBJECT_PROPERTY_EXPRESSION, oldObjectPropertyExpression, objectPropertyExpression));
            }
        }
        return objectPropertyExpression;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public PropertyExpression basicGetObjectPropertyExpression() {
        return objectPropertyExpression;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setObjectPropertyExpression(PropertyExpression newObjectPropertyExpression) {
        PropertyExpression oldObjectPropertyExpression = objectPropertyExpression;
        objectPropertyExpression = newObjectPropertyExpression;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, OdmPackage.OBJECT_SOME_VALUES_FROM__OBJECT_PROPERTY_EXPRESSION, oldObjectPropertyExpression, objectPropertyExpression));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
    public String getTag() {
        return "<<" + tag + ">>" + (getObjectPropertyExpression() != null ? "\n" + ((ObjectProperty) getObjectPropertyExpression()).getTag() : "");
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setTag(String newTag) {
        String oldTag = tag;
        tag = newTag;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, OdmPackage.OBJECT_SOME_VALUES_FROM__TAG, oldTag, tag));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public Description getSomeValuesFromSourceDescription() {
        if (someValuesFromSourceDescription != null && someValuesFromSourceDescription.eIsProxy()) {
            InternalEObject oldSomeValuesFromSourceDescription = (InternalEObject) someValuesFromSourceDescription;
            someValuesFromSourceDescription = (Description) eResolveProxy(oldSomeValuesFromSourceDescription);
            if (someValuesFromSourceDescription != oldSomeValuesFromSourceDescription) {
                if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.RESOLVE, OdmPackage.OBJECT_SOME_VALUES_FROM__SOME_VALUES_FROM_SOURCE_DESCRIPTION, oldSomeValuesFromSourceDescription, someValuesFromSourceDescription));
            }
        }
        return someValuesFromSourceDescription;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public Description basicGetSomeValuesFromSourceDescription() {
        return someValuesFromSourceDescription;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setSomeValuesFromSourceDescription(Description newSomeValuesFromSourceDescription) {
        Description oldSomeValuesFromSourceDescription = someValuesFromSourceDescription;
        someValuesFromSourceDescription = newSomeValuesFromSourceDescription;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, OdmPackage.OBJECT_SOME_VALUES_FROM__SOME_VALUES_FROM_SOURCE_DESCRIPTION, oldSomeValuesFromSourceDescription, someValuesFromSourceDescription));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch(featureID) {
            case OdmPackage.OBJECT_SOME_VALUES_FROM__DESCRIPTION:
                if (resolve) return getDescription();
                return basicGetDescription();
            case OdmPackage.OBJECT_SOME_VALUES_FROM__OBJECT_PROPERTY_EXPRESSION:
                if (resolve) return getObjectPropertyExpression();
                return basicGetObjectPropertyExpression();
            case OdmPackage.OBJECT_SOME_VALUES_FROM__TAG:
                return getTag();
            case OdmPackage.OBJECT_SOME_VALUES_FROM__SOME_VALUES_FROM_SOURCE_DESCRIPTION:
                if (resolve) return getSomeValuesFromSourceDescription();
                return basicGetSomeValuesFromSourceDescription();
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
            case OdmPackage.OBJECT_SOME_VALUES_FROM__DESCRIPTION:
                setDescription((Description) newValue);
                return;
            case OdmPackage.OBJECT_SOME_VALUES_FROM__OBJECT_PROPERTY_EXPRESSION:
                setObjectPropertyExpression((PropertyExpression) newValue);
                return;
            case OdmPackage.OBJECT_SOME_VALUES_FROM__TAG:
                setTag((String) newValue);
                return;
            case OdmPackage.OBJECT_SOME_VALUES_FROM__SOME_VALUES_FROM_SOURCE_DESCRIPTION:
                setSomeValuesFromSourceDescription((Description) newValue);
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
            case OdmPackage.OBJECT_SOME_VALUES_FROM__DESCRIPTION:
                setDescription((Description) null);
                return;
            case OdmPackage.OBJECT_SOME_VALUES_FROM__OBJECT_PROPERTY_EXPRESSION:
                setObjectPropertyExpression((PropertyExpression) null);
                return;
            case OdmPackage.OBJECT_SOME_VALUES_FROM__TAG:
                setTag(TAG_EDEFAULT);
                return;
            case OdmPackage.OBJECT_SOME_VALUES_FROM__SOME_VALUES_FROM_SOURCE_DESCRIPTION:
                setSomeValuesFromSourceDescription((Description) null);
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
            case OdmPackage.OBJECT_SOME_VALUES_FROM__DESCRIPTION:
                return description != null;
            case OdmPackage.OBJECT_SOME_VALUES_FROM__OBJECT_PROPERTY_EXPRESSION:
                return objectPropertyExpression != null;
            case OdmPackage.OBJECT_SOME_VALUES_FROM__TAG:
                return TAG_EDEFAULT == null ? tag != null : !TAG_EDEFAULT.equals(tag);
            case OdmPackage.OBJECT_SOME_VALUES_FROM__SOME_VALUES_FROM_SOURCE_DESCRIPTION:
                return someValuesFromSourceDescription != null;
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
