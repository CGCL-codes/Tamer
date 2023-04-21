package model.domain.impl;

import model.domain.Bridge;
import model.domain.Domain;
import model.domain.DomainPackage;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Bridge</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link model.domain.impl.BridgeImpl#getName <em>Name</em>}</li>
 *   <li>{@link model.domain.impl.BridgeImpl#getDescription <em>Description</em>}</li>
 *   <li>{@link model.domain.impl.BridgeImpl#getADomain <em>ADomain</em>}</li>
 *   <li>{@link model.domain.impl.BridgeImpl#getBDomain <em>BDomain</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class BridgeImpl extends EObjectImpl implements Bridge {

    /**
	 * The default value of the '{@link #getName() <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getName()
	 * @generated
	 * @ordered
	 */
    protected static final String NAME_EDEFAULT = null;

    /**
	 * The cached value of the '{@link #getName() <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getName()
	 * @generated
	 * @ordered
	 */
    protected String name = NAME_EDEFAULT;

    /**
	 * The default value of the '{@link #getDescription() <em>Description</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDescription()
	 * @generated
	 * @ordered
	 */
    protected static final String DESCRIPTION_EDEFAULT = null;

    /**
	 * The cached value of the '{@link #getDescription() <em>Description</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDescription()
	 * @generated
	 * @ordered
	 */
    protected String description = DESCRIPTION_EDEFAULT;

    /**
	 * The cached value of the '{@link #getADomain() <em>ADomain</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getADomain()
	 * @generated
	 * @ordered
	 */
    protected Domain aDomain;

    /**
	 * The cached value of the '{@link #getBDomain() <em>BDomain</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getBDomain()
	 * @generated
	 * @ordered
	 */
    protected Domain bDomain;

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    protected BridgeImpl() {
        super();
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    protected EClass eStaticClass() {
        return DomainPackage.Literals.BRIDGE;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public String getName() {
        return name;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setName(String newName) {
        String oldName = name;
        name = newName;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, DomainPackage.BRIDGE__NAME, oldName, name));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public String getDescription() {
        return description;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setDescription(String newDescription) {
        String oldDescription = description;
        description = newDescription;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, DomainPackage.BRIDGE__DESCRIPTION, oldDescription, description));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public Domain getADomain() {
        if (aDomain != null && aDomain.eIsProxy()) {
            InternalEObject oldADomain = (InternalEObject) aDomain;
            aDomain = (Domain) eResolveProxy(oldADomain);
            if (aDomain != oldADomain) {
                if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.RESOLVE, DomainPackage.BRIDGE__ADOMAIN, oldADomain, aDomain));
            }
        }
        return aDomain;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public Domain basicGetADomain() {
        return aDomain;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setADomain(Domain newADomain) {
        Domain oldADomain = aDomain;
        aDomain = newADomain;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, DomainPackage.BRIDGE__ADOMAIN, oldADomain, aDomain));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public Domain getBDomain() {
        if (bDomain != null && bDomain.eIsProxy()) {
            InternalEObject oldBDomain = (InternalEObject) bDomain;
            bDomain = (Domain) eResolveProxy(oldBDomain);
            if (bDomain != oldBDomain) {
                if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.RESOLVE, DomainPackage.BRIDGE__BDOMAIN, oldBDomain, bDomain));
            }
        }
        return bDomain;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public Domain basicGetBDomain() {
        return bDomain;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setBDomain(Domain newBDomain) {
        Domain oldBDomain = bDomain;
        bDomain = newBDomain;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, DomainPackage.BRIDGE__BDOMAIN, oldBDomain, bDomain));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch(featureID) {
            case DomainPackage.BRIDGE__NAME:
                return getName();
            case DomainPackage.BRIDGE__DESCRIPTION:
                return getDescription();
            case DomainPackage.BRIDGE__ADOMAIN:
                if (resolve) return getADomain();
                return basicGetADomain();
            case DomainPackage.BRIDGE__BDOMAIN:
                if (resolve) return getBDomain();
                return basicGetBDomain();
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
            case DomainPackage.BRIDGE__NAME:
                setName((String) newValue);
                return;
            case DomainPackage.BRIDGE__DESCRIPTION:
                setDescription((String) newValue);
                return;
            case DomainPackage.BRIDGE__ADOMAIN:
                setADomain((Domain) newValue);
                return;
            case DomainPackage.BRIDGE__BDOMAIN:
                setBDomain((Domain) newValue);
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
            case DomainPackage.BRIDGE__NAME:
                setName(NAME_EDEFAULT);
                return;
            case DomainPackage.BRIDGE__DESCRIPTION:
                setDescription(DESCRIPTION_EDEFAULT);
                return;
            case DomainPackage.BRIDGE__ADOMAIN:
                setADomain((Domain) null);
                return;
            case DomainPackage.BRIDGE__BDOMAIN:
                setBDomain((Domain) null);
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
            case DomainPackage.BRIDGE__NAME:
                return NAME_EDEFAULT == null ? name != null : !NAME_EDEFAULT.equals(name);
            case DomainPackage.BRIDGE__DESCRIPTION:
                return DESCRIPTION_EDEFAULT == null ? description != null : !DESCRIPTION_EDEFAULT.equals(description);
            case DomainPackage.BRIDGE__ADOMAIN:
                return aDomain != null;
            case DomainPackage.BRIDGE__BDOMAIN:
                return bDomain != null;
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
        result.append(" (name: ");
        result.append(name);
        result.append(", description: ");
        result.append(description);
        result.append(')');
        return result.toString();
    }
}
