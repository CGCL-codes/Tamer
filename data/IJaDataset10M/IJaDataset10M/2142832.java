package Slee11.impl;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;
import Slee11.DescriptionType;
import Slee11.EventClassNameType;
import Slee11.EventDefinitionType;
import Slee11.EventTypeNameType;
import Slee11.EventTypeVendorType;
import Slee11.EventTypeVersionType;
import Slee11.Slee11Package;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Event Definition Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link Slee11.impl.EventDefinitionTypeImpl#getDescription <em>Description</em>}</li>
 *   <li>{@link Slee11.impl.EventDefinitionTypeImpl#getEventTypeName <em>Event Type Name</em>}</li>
 *   <li>{@link Slee11.impl.EventDefinitionTypeImpl#getEventTypeVendor <em>Event Type Vendor</em>}</li>
 *   <li>{@link Slee11.impl.EventDefinitionTypeImpl#getEventTypeVersion <em>Event Type Version</em>}</li>
 *   <li>{@link Slee11.impl.EventDefinitionTypeImpl#getEventClassName <em>Event Class Name</em>}</li>
 *   <li>{@link Slee11.impl.EventDefinitionTypeImpl#getId <em>Id</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class EventDefinitionTypeImpl extends EObjectImpl implements EventDefinitionType {

    /**
	 * The cached value of the '{@link #getDescription() <em>Description</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDescription()
	 * @generated
	 * @ordered
	 */
    protected DescriptionType description;

    /**
	 * The cached value of the '{@link #getEventTypeName() <em>Event Type Name</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getEventTypeName()
	 * @generated
	 * @ordered
	 */
    protected EventTypeNameType eventTypeName;

    /**
	 * The cached value of the '{@link #getEventTypeVendor() <em>Event Type Vendor</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getEventTypeVendor()
	 * @generated
	 * @ordered
	 */
    protected EventTypeVendorType eventTypeVendor;

    /**
	 * The cached value of the '{@link #getEventTypeVersion() <em>Event Type Version</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getEventTypeVersion()
	 * @generated
	 * @ordered
	 */
    protected EventTypeVersionType eventTypeVersion;

    /**
	 * The cached value of the '{@link #getEventClassName() <em>Event Class Name</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getEventClassName()
	 * @generated
	 * @ordered
	 */
    protected EventClassNameType eventClassName;

    /**
	 * The default value of the '{@link #getId() <em>Id</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getId()
	 * @generated
	 * @ordered
	 */
    protected static final String ID_EDEFAULT = null;

    /**
	 * The cached value of the '{@link #getId() <em>Id</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getId()
	 * @generated
	 * @ordered
	 */
    protected String id = ID_EDEFAULT;

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    protected EventDefinitionTypeImpl() {
        super();
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    protected EClass eStaticClass() {
        return Slee11Package.Literals.EVENT_DEFINITION_TYPE;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public DescriptionType getDescription() {
        return description;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public NotificationChain basicSetDescription(DescriptionType newDescription, NotificationChain msgs) {
        DescriptionType oldDescription = description;
        description = newDescription;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, Slee11Package.EVENT_DEFINITION_TYPE__DESCRIPTION, oldDescription, newDescription);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setDescription(DescriptionType newDescription) {
        if (newDescription != description) {
            NotificationChain msgs = null;
            if (description != null) msgs = ((InternalEObject) description).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - Slee11Package.EVENT_DEFINITION_TYPE__DESCRIPTION, null, msgs);
            if (newDescription != null) msgs = ((InternalEObject) newDescription).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - Slee11Package.EVENT_DEFINITION_TYPE__DESCRIPTION, null, msgs);
            msgs = basicSetDescription(newDescription, msgs);
            if (msgs != null) msgs.dispatch();
        } else if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, Slee11Package.EVENT_DEFINITION_TYPE__DESCRIPTION, newDescription, newDescription));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EventTypeNameType getEventTypeName() {
        return eventTypeName;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public NotificationChain basicSetEventTypeName(EventTypeNameType newEventTypeName, NotificationChain msgs) {
        EventTypeNameType oldEventTypeName = eventTypeName;
        eventTypeName = newEventTypeName;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, Slee11Package.EVENT_DEFINITION_TYPE__EVENT_TYPE_NAME, oldEventTypeName, newEventTypeName);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setEventTypeName(EventTypeNameType newEventTypeName) {
        if (newEventTypeName != eventTypeName) {
            NotificationChain msgs = null;
            if (eventTypeName != null) msgs = ((InternalEObject) eventTypeName).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - Slee11Package.EVENT_DEFINITION_TYPE__EVENT_TYPE_NAME, null, msgs);
            if (newEventTypeName != null) msgs = ((InternalEObject) newEventTypeName).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - Slee11Package.EVENT_DEFINITION_TYPE__EVENT_TYPE_NAME, null, msgs);
            msgs = basicSetEventTypeName(newEventTypeName, msgs);
            if (msgs != null) msgs.dispatch();
        } else if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, Slee11Package.EVENT_DEFINITION_TYPE__EVENT_TYPE_NAME, newEventTypeName, newEventTypeName));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EventTypeVendorType getEventTypeVendor() {
        return eventTypeVendor;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public NotificationChain basicSetEventTypeVendor(EventTypeVendorType newEventTypeVendor, NotificationChain msgs) {
        EventTypeVendorType oldEventTypeVendor = eventTypeVendor;
        eventTypeVendor = newEventTypeVendor;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, Slee11Package.EVENT_DEFINITION_TYPE__EVENT_TYPE_VENDOR, oldEventTypeVendor, newEventTypeVendor);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setEventTypeVendor(EventTypeVendorType newEventTypeVendor) {
        if (newEventTypeVendor != eventTypeVendor) {
            NotificationChain msgs = null;
            if (eventTypeVendor != null) msgs = ((InternalEObject) eventTypeVendor).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - Slee11Package.EVENT_DEFINITION_TYPE__EVENT_TYPE_VENDOR, null, msgs);
            if (newEventTypeVendor != null) msgs = ((InternalEObject) newEventTypeVendor).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - Slee11Package.EVENT_DEFINITION_TYPE__EVENT_TYPE_VENDOR, null, msgs);
            msgs = basicSetEventTypeVendor(newEventTypeVendor, msgs);
            if (msgs != null) msgs.dispatch();
        } else if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, Slee11Package.EVENT_DEFINITION_TYPE__EVENT_TYPE_VENDOR, newEventTypeVendor, newEventTypeVendor));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EventTypeVersionType getEventTypeVersion() {
        return eventTypeVersion;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public NotificationChain basicSetEventTypeVersion(EventTypeVersionType newEventTypeVersion, NotificationChain msgs) {
        EventTypeVersionType oldEventTypeVersion = eventTypeVersion;
        eventTypeVersion = newEventTypeVersion;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, Slee11Package.EVENT_DEFINITION_TYPE__EVENT_TYPE_VERSION, oldEventTypeVersion, newEventTypeVersion);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setEventTypeVersion(EventTypeVersionType newEventTypeVersion) {
        if (newEventTypeVersion != eventTypeVersion) {
            NotificationChain msgs = null;
            if (eventTypeVersion != null) msgs = ((InternalEObject) eventTypeVersion).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - Slee11Package.EVENT_DEFINITION_TYPE__EVENT_TYPE_VERSION, null, msgs);
            if (newEventTypeVersion != null) msgs = ((InternalEObject) newEventTypeVersion).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - Slee11Package.EVENT_DEFINITION_TYPE__EVENT_TYPE_VERSION, null, msgs);
            msgs = basicSetEventTypeVersion(newEventTypeVersion, msgs);
            if (msgs != null) msgs.dispatch();
        } else if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, Slee11Package.EVENT_DEFINITION_TYPE__EVENT_TYPE_VERSION, newEventTypeVersion, newEventTypeVersion));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EventClassNameType getEventClassName() {
        return eventClassName;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public NotificationChain basicSetEventClassName(EventClassNameType newEventClassName, NotificationChain msgs) {
        EventClassNameType oldEventClassName = eventClassName;
        eventClassName = newEventClassName;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, Slee11Package.EVENT_DEFINITION_TYPE__EVENT_CLASS_NAME, oldEventClassName, newEventClassName);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setEventClassName(EventClassNameType newEventClassName) {
        if (newEventClassName != eventClassName) {
            NotificationChain msgs = null;
            if (eventClassName != null) msgs = ((InternalEObject) eventClassName).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - Slee11Package.EVENT_DEFINITION_TYPE__EVENT_CLASS_NAME, null, msgs);
            if (newEventClassName != null) msgs = ((InternalEObject) newEventClassName).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - Slee11Package.EVENT_DEFINITION_TYPE__EVENT_CLASS_NAME, null, msgs);
            msgs = basicSetEventClassName(newEventClassName, msgs);
            if (msgs != null) msgs.dispatch();
        } else if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, Slee11Package.EVENT_DEFINITION_TYPE__EVENT_CLASS_NAME, newEventClassName, newEventClassName));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public String getId() {
        return id;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setId(String newId) {
        String oldId = id;
        id = newId;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, Slee11Package.EVENT_DEFINITION_TYPE__ID, oldId, id));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch(featureID) {
            case Slee11Package.EVENT_DEFINITION_TYPE__DESCRIPTION:
                return basicSetDescription(null, msgs);
            case Slee11Package.EVENT_DEFINITION_TYPE__EVENT_TYPE_NAME:
                return basicSetEventTypeName(null, msgs);
            case Slee11Package.EVENT_DEFINITION_TYPE__EVENT_TYPE_VENDOR:
                return basicSetEventTypeVendor(null, msgs);
            case Slee11Package.EVENT_DEFINITION_TYPE__EVENT_TYPE_VERSION:
                return basicSetEventTypeVersion(null, msgs);
            case Slee11Package.EVENT_DEFINITION_TYPE__EVENT_CLASS_NAME:
                return basicSetEventClassName(null, msgs);
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
            case Slee11Package.EVENT_DEFINITION_TYPE__DESCRIPTION:
                return getDescription();
            case Slee11Package.EVENT_DEFINITION_TYPE__EVENT_TYPE_NAME:
                return getEventTypeName();
            case Slee11Package.EVENT_DEFINITION_TYPE__EVENT_TYPE_VENDOR:
                return getEventTypeVendor();
            case Slee11Package.EVENT_DEFINITION_TYPE__EVENT_TYPE_VERSION:
                return getEventTypeVersion();
            case Slee11Package.EVENT_DEFINITION_TYPE__EVENT_CLASS_NAME:
                return getEventClassName();
            case Slee11Package.EVENT_DEFINITION_TYPE__ID:
                return getId();
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
            case Slee11Package.EVENT_DEFINITION_TYPE__DESCRIPTION:
                setDescription((DescriptionType) newValue);
                return;
            case Slee11Package.EVENT_DEFINITION_TYPE__EVENT_TYPE_NAME:
                setEventTypeName((EventTypeNameType) newValue);
                return;
            case Slee11Package.EVENT_DEFINITION_TYPE__EVENT_TYPE_VENDOR:
                setEventTypeVendor((EventTypeVendorType) newValue);
                return;
            case Slee11Package.EVENT_DEFINITION_TYPE__EVENT_TYPE_VERSION:
                setEventTypeVersion((EventTypeVersionType) newValue);
                return;
            case Slee11Package.EVENT_DEFINITION_TYPE__EVENT_CLASS_NAME:
                setEventClassName((EventClassNameType) newValue);
                return;
            case Slee11Package.EVENT_DEFINITION_TYPE__ID:
                setId((String) newValue);
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
            case Slee11Package.EVENT_DEFINITION_TYPE__DESCRIPTION:
                setDescription((DescriptionType) null);
                return;
            case Slee11Package.EVENT_DEFINITION_TYPE__EVENT_TYPE_NAME:
                setEventTypeName((EventTypeNameType) null);
                return;
            case Slee11Package.EVENT_DEFINITION_TYPE__EVENT_TYPE_VENDOR:
                setEventTypeVendor((EventTypeVendorType) null);
                return;
            case Slee11Package.EVENT_DEFINITION_TYPE__EVENT_TYPE_VERSION:
                setEventTypeVersion((EventTypeVersionType) null);
                return;
            case Slee11Package.EVENT_DEFINITION_TYPE__EVENT_CLASS_NAME:
                setEventClassName((EventClassNameType) null);
                return;
            case Slee11Package.EVENT_DEFINITION_TYPE__ID:
                setId(ID_EDEFAULT);
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
            case Slee11Package.EVENT_DEFINITION_TYPE__DESCRIPTION:
                return description != null;
            case Slee11Package.EVENT_DEFINITION_TYPE__EVENT_TYPE_NAME:
                return eventTypeName != null;
            case Slee11Package.EVENT_DEFINITION_TYPE__EVENT_TYPE_VENDOR:
                return eventTypeVendor != null;
            case Slee11Package.EVENT_DEFINITION_TYPE__EVENT_TYPE_VERSION:
                return eventTypeVersion != null;
            case Slee11Package.EVENT_DEFINITION_TYPE__EVENT_CLASS_NAME:
                return eventClassName != null;
            case Slee11Package.EVENT_DEFINITION_TYPE__ID:
                return ID_EDEFAULT == null ? id != null : !ID_EDEFAULT.equals(id);
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
        result.append(" (id: ");
        result.append(id);
        result.append(')');
        return result.toString();
    }
}
