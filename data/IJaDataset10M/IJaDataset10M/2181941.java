package org.hl7.v3.impl;

import java.util.Collection;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.Enumerator;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;
import org.hl7.v3.CS1;
import org.hl7.v3.II;
import org.hl7.v3.POCDMT000040Authorization;
import org.hl7.v3.POCDMT000040Consent;
import org.hl7.v3.POCDMT000040InfrastructureRootTypeId;
import org.hl7.v3.V3Package;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>POCDMT000040 Authorization</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.hl7.v3.impl.POCDMT000040AuthorizationImpl#getRealmCode <em>Realm Code</em>}</li>
 *   <li>{@link org.hl7.v3.impl.POCDMT000040AuthorizationImpl#getTypeId <em>Type Id</em>}</li>
 *   <li>{@link org.hl7.v3.impl.POCDMT000040AuthorizationImpl#getTemplateId <em>Template Id</em>}</li>
 *   <li>{@link org.hl7.v3.impl.POCDMT000040AuthorizationImpl#getConsent <em>Consent</em>}</li>
 *   <li>{@link org.hl7.v3.impl.POCDMT000040AuthorizationImpl#getNullFlavor <em>Null Flavor</em>}</li>
 *   <li>{@link org.hl7.v3.impl.POCDMT000040AuthorizationImpl#getTypeCode <em>Type Code</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class POCDMT000040AuthorizationImpl extends EObjectImpl implements POCDMT000040Authorization {

    /**
	 * The cached value of the '{@link #getRealmCode() <em>Realm Code</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getRealmCode()
	 * @generated
	 * @ordered
	 */
    protected EList<CS1> realmCode;

    /**
	 * The cached value of the '{@link #getTypeId() <em>Type Id</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getTypeId()
	 * @generated
	 * @ordered
	 */
    protected POCDMT000040InfrastructureRootTypeId typeId;

    /**
	 * The cached value of the '{@link #getTemplateId() <em>Template Id</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getTemplateId()
	 * @generated
	 * @ordered
	 */
    protected EList<II> templateId;

    /**
	 * The cached value of the '{@link #getConsent() <em>Consent</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getConsent()
	 * @generated
	 * @ordered
	 */
    protected POCDMT000040Consent consent;

    /**
	 * The default value of the '{@link #getNullFlavor() <em>Null Flavor</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getNullFlavor()
	 * @generated
	 * @ordered
	 */
    protected static final Enumerator NULL_FLAVOR_EDEFAULT = null;

    /**
	 * The cached value of the '{@link #getNullFlavor() <em>Null Flavor</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getNullFlavor()
	 * @generated
	 * @ordered
	 */
    protected Enumerator nullFlavor = NULL_FLAVOR_EDEFAULT;

    /**
	 * The default value of the '{@link #getTypeCode() <em>Type Code</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getTypeCode()
	 * @generated
	 * @ordered
	 */
    protected static final Object TYPE_CODE_EDEFAULT = new Object();

    /**
	 * The cached value of the '{@link #getTypeCode() <em>Type Code</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getTypeCode()
	 * @generated
	 * @ordered
	 */
    protected Object typeCode = TYPE_CODE_EDEFAULT;

    /**
	 * This is true if the Type Code attribute has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    protected boolean typeCodeESet;

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    protected POCDMT000040AuthorizationImpl() {
        super();
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    protected EClass eStaticClass() {
        return V3Package.eINSTANCE.getPOCDMT000040Authorization();
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EList<CS1> getRealmCode() {
        if (realmCode == null) {
            realmCode = new EObjectContainmentEList<CS1>(CS1.class, this, V3Package.POCDMT000040_AUTHORIZATION__REALM_CODE);
        }
        return realmCode;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public POCDMT000040InfrastructureRootTypeId getTypeId() {
        return typeId;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public NotificationChain basicSetTypeId(POCDMT000040InfrastructureRootTypeId newTypeId, NotificationChain msgs) {
        POCDMT000040InfrastructureRootTypeId oldTypeId = typeId;
        typeId = newTypeId;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, V3Package.POCDMT000040_AUTHORIZATION__TYPE_ID, oldTypeId, newTypeId);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setTypeId(POCDMT000040InfrastructureRootTypeId newTypeId) {
        if (newTypeId != typeId) {
            NotificationChain msgs = null;
            if (typeId != null) msgs = ((InternalEObject) typeId).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - V3Package.POCDMT000040_AUTHORIZATION__TYPE_ID, null, msgs);
            if (newTypeId != null) msgs = ((InternalEObject) newTypeId).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - V3Package.POCDMT000040_AUTHORIZATION__TYPE_ID, null, msgs);
            msgs = basicSetTypeId(newTypeId, msgs);
            if (msgs != null) msgs.dispatch();
        } else if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, V3Package.POCDMT000040_AUTHORIZATION__TYPE_ID, newTypeId, newTypeId));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EList<II> getTemplateId() {
        if (templateId == null) {
            templateId = new EObjectContainmentEList<II>(II.class, this, V3Package.POCDMT000040_AUTHORIZATION__TEMPLATE_ID);
        }
        return templateId;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public POCDMT000040Consent getConsent() {
        return consent;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public NotificationChain basicSetConsent(POCDMT000040Consent newConsent, NotificationChain msgs) {
        POCDMT000040Consent oldConsent = consent;
        consent = newConsent;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, V3Package.POCDMT000040_AUTHORIZATION__CONSENT, oldConsent, newConsent);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setConsent(POCDMT000040Consent newConsent) {
        if (newConsent != consent) {
            NotificationChain msgs = null;
            if (consent != null) msgs = ((InternalEObject) consent).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - V3Package.POCDMT000040_AUTHORIZATION__CONSENT, null, msgs);
            if (newConsent != null) msgs = ((InternalEObject) newConsent).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - V3Package.POCDMT000040_AUTHORIZATION__CONSENT, null, msgs);
            msgs = basicSetConsent(newConsent, msgs);
            if (msgs != null) msgs.dispatch();
        } else if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, V3Package.POCDMT000040_AUTHORIZATION__CONSENT, newConsent, newConsent));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public Enumerator getNullFlavor() {
        return nullFlavor;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setNullFlavor(Enumerator newNullFlavor) {
        Enumerator oldNullFlavor = nullFlavor;
        nullFlavor = newNullFlavor;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, V3Package.POCDMT000040_AUTHORIZATION__NULL_FLAVOR, oldNullFlavor, nullFlavor));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public Object getTypeCode() {
        return typeCode;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setTypeCode(Object newTypeCode) {
        Object oldTypeCode = typeCode;
        typeCode = newTypeCode;
        boolean oldTypeCodeESet = typeCodeESet;
        typeCodeESet = true;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, V3Package.POCDMT000040_AUTHORIZATION__TYPE_CODE, oldTypeCode, typeCode, !oldTypeCodeESet));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void unsetTypeCode() {
        Object oldTypeCode = typeCode;
        boolean oldTypeCodeESet = typeCodeESet;
        typeCode = TYPE_CODE_EDEFAULT;
        typeCodeESet = false;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.UNSET, V3Package.POCDMT000040_AUTHORIZATION__TYPE_CODE, oldTypeCode, TYPE_CODE_EDEFAULT, oldTypeCodeESet));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public boolean isSetTypeCode() {
        return typeCodeESet;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch(featureID) {
            case V3Package.POCDMT000040_AUTHORIZATION__REALM_CODE:
                return ((InternalEList<?>) getRealmCode()).basicRemove(otherEnd, msgs);
            case V3Package.POCDMT000040_AUTHORIZATION__TYPE_ID:
                return basicSetTypeId(null, msgs);
            case V3Package.POCDMT000040_AUTHORIZATION__TEMPLATE_ID:
                return ((InternalEList<?>) getTemplateId()).basicRemove(otherEnd, msgs);
            case V3Package.POCDMT000040_AUTHORIZATION__CONSENT:
                return basicSetConsent(null, msgs);
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
            case V3Package.POCDMT000040_AUTHORIZATION__REALM_CODE:
                return getRealmCode();
            case V3Package.POCDMT000040_AUTHORIZATION__TYPE_ID:
                return getTypeId();
            case V3Package.POCDMT000040_AUTHORIZATION__TEMPLATE_ID:
                return getTemplateId();
            case V3Package.POCDMT000040_AUTHORIZATION__CONSENT:
                return getConsent();
            case V3Package.POCDMT000040_AUTHORIZATION__NULL_FLAVOR:
                return getNullFlavor();
            case V3Package.POCDMT000040_AUTHORIZATION__TYPE_CODE:
                return getTypeCode();
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
            case V3Package.POCDMT000040_AUTHORIZATION__REALM_CODE:
                getRealmCode().clear();
                getRealmCode().addAll((Collection<? extends CS1>) newValue);
                return;
            case V3Package.POCDMT000040_AUTHORIZATION__TYPE_ID:
                setTypeId((POCDMT000040InfrastructureRootTypeId) newValue);
                return;
            case V3Package.POCDMT000040_AUTHORIZATION__TEMPLATE_ID:
                getTemplateId().clear();
                getTemplateId().addAll((Collection<? extends II>) newValue);
                return;
            case V3Package.POCDMT000040_AUTHORIZATION__CONSENT:
                setConsent((POCDMT000040Consent) newValue);
                return;
            case V3Package.POCDMT000040_AUTHORIZATION__NULL_FLAVOR:
                setNullFlavor((Enumerator) newValue);
                return;
            case V3Package.POCDMT000040_AUTHORIZATION__TYPE_CODE:
                setTypeCode(newValue);
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
            case V3Package.POCDMT000040_AUTHORIZATION__REALM_CODE:
                getRealmCode().clear();
                return;
            case V3Package.POCDMT000040_AUTHORIZATION__TYPE_ID:
                setTypeId((POCDMT000040InfrastructureRootTypeId) null);
                return;
            case V3Package.POCDMT000040_AUTHORIZATION__TEMPLATE_ID:
                getTemplateId().clear();
                return;
            case V3Package.POCDMT000040_AUTHORIZATION__CONSENT:
                setConsent((POCDMT000040Consent) null);
                return;
            case V3Package.POCDMT000040_AUTHORIZATION__NULL_FLAVOR:
                setNullFlavor(NULL_FLAVOR_EDEFAULT);
                return;
            case V3Package.POCDMT000040_AUTHORIZATION__TYPE_CODE:
                unsetTypeCode();
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
            case V3Package.POCDMT000040_AUTHORIZATION__REALM_CODE:
                return realmCode != null && !realmCode.isEmpty();
            case V3Package.POCDMT000040_AUTHORIZATION__TYPE_ID:
                return typeId != null;
            case V3Package.POCDMT000040_AUTHORIZATION__TEMPLATE_ID:
                return templateId != null && !templateId.isEmpty();
            case V3Package.POCDMT000040_AUTHORIZATION__CONSENT:
                return consent != null;
            case V3Package.POCDMT000040_AUTHORIZATION__NULL_FLAVOR:
                return NULL_FLAVOR_EDEFAULT == null ? nullFlavor != null : !NULL_FLAVOR_EDEFAULT.equals(nullFlavor);
            case V3Package.POCDMT000040_AUTHORIZATION__TYPE_CODE:
                return isSetTypeCode();
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
        result.append(" (nullFlavor: ");
        result.append(nullFlavor);
        result.append(", typeCode: ");
        if (typeCodeESet) result.append(typeCode); else result.append("<unset>");
        result.append(')');
        return result.toString();
    }
}
