package Slee11.impl;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;
import Slee11.DescriptionType;
import Slee11.SbbActivityContextInterfaceNameType;
import Slee11.SbbActivityContextInterfaceType;
import Slee11.Slee11Package;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Sbb Activity Context Interface Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link Slee11.impl.SbbActivityContextInterfaceTypeImpl#getDescription <em>Description</em>}</li>
 *   <li>{@link Slee11.impl.SbbActivityContextInterfaceTypeImpl#getSbbActivityContextInterfaceName <em>Sbb Activity Context Interface Name</em>}</li>
 *   <li>{@link Slee11.impl.SbbActivityContextInterfaceTypeImpl#getId <em>Id</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class SbbActivityContextInterfaceTypeImpl extends EObjectImpl implements SbbActivityContextInterfaceType {

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
	 * The cached value of the '{@link #getSbbActivityContextInterfaceName() <em>Sbb Activity Context Interface Name</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSbbActivityContextInterfaceName()
	 * @generated
	 * @ordered
	 */
    protected SbbActivityContextInterfaceNameType sbbActivityContextInterfaceName;

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
    protected SbbActivityContextInterfaceTypeImpl() {
        super();
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    protected EClass eStaticClass() {
        return Slee11Package.Literals.SBB_ACTIVITY_CONTEXT_INTERFACE_TYPE;
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
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, Slee11Package.SBB_ACTIVITY_CONTEXT_INTERFACE_TYPE__DESCRIPTION, oldDescription, newDescription);
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
            if (description != null) msgs = ((InternalEObject) description).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - Slee11Package.SBB_ACTIVITY_CONTEXT_INTERFACE_TYPE__DESCRIPTION, null, msgs);
            if (newDescription != null) msgs = ((InternalEObject) newDescription).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - Slee11Package.SBB_ACTIVITY_CONTEXT_INTERFACE_TYPE__DESCRIPTION, null, msgs);
            msgs = basicSetDescription(newDescription, msgs);
            if (msgs != null) msgs.dispatch();
        } else if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, Slee11Package.SBB_ACTIVITY_CONTEXT_INTERFACE_TYPE__DESCRIPTION, newDescription, newDescription));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public SbbActivityContextInterfaceNameType getSbbActivityContextInterfaceName() {
        return sbbActivityContextInterfaceName;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public NotificationChain basicSetSbbActivityContextInterfaceName(SbbActivityContextInterfaceNameType newSbbActivityContextInterfaceName, NotificationChain msgs) {
        SbbActivityContextInterfaceNameType oldSbbActivityContextInterfaceName = sbbActivityContextInterfaceName;
        sbbActivityContextInterfaceName = newSbbActivityContextInterfaceName;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, Slee11Package.SBB_ACTIVITY_CONTEXT_INTERFACE_TYPE__SBB_ACTIVITY_CONTEXT_INTERFACE_NAME, oldSbbActivityContextInterfaceName, newSbbActivityContextInterfaceName);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setSbbActivityContextInterfaceName(SbbActivityContextInterfaceNameType newSbbActivityContextInterfaceName) {
        if (newSbbActivityContextInterfaceName != sbbActivityContextInterfaceName) {
            NotificationChain msgs = null;
            if (sbbActivityContextInterfaceName != null) msgs = ((InternalEObject) sbbActivityContextInterfaceName).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - Slee11Package.SBB_ACTIVITY_CONTEXT_INTERFACE_TYPE__SBB_ACTIVITY_CONTEXT_INTERFACE_NAME, null, msgs);
            if (newSbbActivityContextInterfaceName != null) msgs = ((InternalEObject) newSbbActivityContextInterfaceName).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - Slee11Package.SBB_ACTIVITY_CONTEXT_INTERFACE_TYPE__SBB_ACTIVITY_CONTEXT_INTERFACE_NAME, null, msgs);
            msgs = basicSetSbbActivityContextInterfaceName(newSbbActivityContextInterfaceName, msgs);
            if (msgs != null) msgs.dispatch();
        } else if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, Slee11Package.SBB_ACTIVITY_CONTEXT_INTERFACE_TYPE__SBB_ACTIVITY_CONTEXT_INTERFACE_NAME, newSbbActivityContextInterfaceName, newSbbActivityContextInterfaceName));
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
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, Slee11Package.SBB_ACTIVITY_CONTEXT_INTERFACE_TYPE__ID, oldId, id));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch(featureID) {
            case Slee11Package.SBB_ACTIVITY_CONTEXT_INTERFACE_TYPE__DESCRIPTION:
                return basicSetDescription(null, msgs);
            case Slee11Package.SBB_ACTIVITY_CONTEXT_INTERFACE_TYPE__SBB_ACTIVITY_CONTEXT_INTERFACE_NAME:
                return basicSetSbbActivityContextInterfaceName(null, msgs);
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
            case Slee11Package.SBB_ACTIVITY_CONTEXT_INTERFACE_TYPE__DESCRIPTION:
                return getDescription();
            case Slee11Package.SBB_ACTIVITY_CONTEXT_INTERFACE_TYPE__SBB_ACTIVITY_CONTEXT_INTERFACE_NAME:
                return getSbbActivityContextInterfaceName();
            case Slee11Package.SBB_ACTIVITY_CONTEXT_INTERFACE_TYPE__ID:
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
            case Slee11Package.SBB_ACTIVITY_CONTEXT_INTERFACE_TYPE__DESCRIPTION:
                setDescription((DescriptionType) newValue);
                return;
            case Slee11Package.SBB_ACTIVITY_CONTEXT_INTERFACE_TYPE__SBB_ACTIVITY_CONTEXT_INTERFACE_NAME:
                setSbbActivityContextInterfaceName((SbbActivityContextInterfaceNameType) newValue);
                return;
            case Slee11Package.SBB_ACTIVITY_CONTEXT_INTERFACE_TYPE__ID:
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
            case Slee11Package.SBB_ACTIVITY_CONTEXT_INTERFACE_TYPE__DESCRIPTION:
                setDescription((DescriptionType) null);
                return;
            case Slee11Package.SBB_ACTIVITY_CONTEXT_INTERFACE_TYPE__SBB_ACTIVITY_CONTEXT_INTERFACE_NAME:
                setSbbActivityContextInterfaceName((SbbActivityContextInterfaceNameType) null);
                return;
            case Slee11Package.SBB_ACTIVITY_CONTEXT_INTERFACE_TYPE__ID:
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
            case Slee11Package.SBB_ACTIVITY_CONTEXT_INTERFACE_TYPE__DESCRIPTION:
                return description != null;
            case Slee11Package.SBB_ACTIVITY_CONTEXT_INTERFACE_TYPE__SBB_ACTIVITY_CONTEXT_INTERFACE_NAME:
                return sbbActivityContextInterfaceName != null;
            case Slee11Package.SBB_ACTIVITY_CONTEXT_INTERFACE_TYPE__ID:
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
