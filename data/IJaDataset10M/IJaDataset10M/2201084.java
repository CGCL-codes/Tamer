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
import org.hl7.v3.CE;
import org.hl7.v3.CS1;
import org.hl7.v3.II;
import org.hl7.v3.IVLTS;
import org.hl7.v3.POCDMT000040AssignedEntity;
import org.hl7.v3.POCDMT000040InfrastructureRootTypeId;
import org.hl7.v3.POCDMT000040Performer1;
import org.hl7.v3.V3Package;
import org.hl7.v3.XServiceEventPerformer;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>POCDMT000040 Performer1</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.hl7.v3.impl.POCDMT000040Performer1Impl#getRealmCode <em>Realm Code</em>}</li>
 *   <li>{@link org.hl7.v3.impl.POCDMT000040Performer1Impl#getTypeId <em>Type Id</em>}</li>
 *   <li>{@link org.hl7.v3.impl.POCDMT000040Performer1Impl#getTemplateId <em>Template Id</em>}</li>
 *   <li>{@link org.hl7.v3.impl.POCDMT000040Performer1Impl#getFunctionCode <em>Function Code</em>}</li>
 *   <li>{@link org.hl7.v3.impl.POCDMT000040Performer1Impl#getTime <em>Time</em>}</li>
 *   <li>{@link org.hl7.v3.impl.POCDMT000040Performer1Impl#getAssignedEntity <em>Assigned Entity</em>}</li>
 *   <li>{@link org.hl7.v3.impl.POCDMT000040Performer1Impl#getNullFlavor <em>Null Flavor</em>}</li>
 *   <li>{@link org.hl7.v3.impl.POCDMT000040Performer1Impl#getTypeCode <em>Type Code</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class POCDMT000040Performer1Impl extends EObjectImpl implements POCDMT000040Performer1 {

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
	 * The cached value of the '{@link #getFunctionCode() <em>Function Code</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getFunctionCode()
	 * @generated
	 * @ordered
	 */
    protected CE functionCode;

    /**
	 * The cached value of the '{@link #getTime() <em>Time</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getTime()
	 * @generated
	 * @ordered
	 */
    protected IVLTS time;

    /**
	 * The cached value of the '{@link #getAssignedEntity() <em>Assigned Entity</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getAssignedEntity()
	 * @generated
	 * @ordered
	 */
    protected POCDMT000040AssignedEntity assignedEntity;

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
    protected static final XServiceEventPerformer TYPE_CODE_EDEFAULT = XServiceEventPerformer.PRF;

    /**
	 * The cached value of the '{@link #getTypeCode() <em>Type Code</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getTypeCode()
	 * @generated
	 * @ordered
	 */
    protected XServiceEventPerformer typeCode = TYPE_CODE_EDEFAULT;

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
    protected POCDMT000040Performer1Impl() {
        super();
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    protected EClass eStaticClass() {
        return V3Package.eINSTANCE.getPOCDMT000040Performer1();
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EList<CS1> getRealmCode() {
        if (realmCode == null) {
            realmCode = new EObjectContainmentEList<CS1>(CS1.class, this, V3Package.POCDMT000040_PERFORMER1__REALM_CODE);
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
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, V3Package.POCDMT000040_PERFORMER1__TYPE_ID, oldTypeId, newTypeId);
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
            if (typeId != null) msgs = ((InternalEObject) typeId).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - V3Package.POCDMT000040_PERFORMER1__TYPE_ID, null, msgs);
            if (newTypeId != null) msgs = ((InternalEObject) newTypeId).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - V3Package.POCDMT000040_PERFORMER1__TYPE_ID, null, msgs);
            msgs = basicSetTypeId(newTypeId, msgs);
            if (msgs != null) msgs.dispatch();
        } else if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, V3Package.POCDMT000040_PERFORMER1__TYPE_ID, newTypeId, newTypeId));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EList<II> getTemplateId() {
        if (templateId == null) {
            templateId = new EObjectContainmentEList<II>(II.class, this, V3Package.POCDMT000040_PERFORMER1__TEMPLATE_ID);
        }
        return templateId;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public CE getFunctionCode() {
        return functionCode;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public NotificationChain basicSetFunctionCode(CE newFunctionCode, NotificationChain msgs) {
        CE oldFunctionCode = functionCode;
        functionCode = newFunctionCode;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, V3Package.POCDMT000040_PERFORMER1__FUNCTION_CODE, oldFunctionCode, newFunctionCode);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setFunctionCode(CE newFunctionCode) {
        if (newFunctionCode != functionCode) {
            NotificationChain msgs = null;
            if (functionCode != null) msgs = ((InternalEObject) functionCode).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - V3Package.POCDMT000040_PERFORMER1__FUNCTION_CODE, null, msgs);
            if (newFunctionCode != null) msgs = ((InternalEObject) newFunctionCode).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - V3Package.POCDMT000040_PERFORMER1__FUNCTION_CODE, null, msgs);
            msgs = basicSetFunctionCode(newFunctionCode, msgs);
            if (msgs != null) msgs.dispatch();
        } else if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, V3Package.POCDMT000040_PERFORMER1__FUNCTION_CODE, newFunctionCode, newFunctionCode));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public IVLTS getTime() {
        return time;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public NotificationChain basicSetTime(IVLTS newTime, NotificationChain msgs) {
        IVLTS oldTime = time;
        time = newTime;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, V3Package.POCDMT000040_PERFORMER1__TIME, oldTime, newTime);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setTime(IVLTS newTime) {
        if (newTime != time) {
            NotificationChain msgs = null;
            if (time != null) msgs = ((InternalEObject) time).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - V3Package.POCDMT000040_PERFORMER1__TIME, null, msgs);
            if (newTime != null) msgs = ((InternalEObject) newTime).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - V3Package.POCDMT000040_PERFORMER1__TIME, null, msgs);
            msgs = basicSetTime(newTime, msgs);
            if (msgs != null) msgs.dispatch();
        } else if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, V3Package.POCDMT000040_PERFORMER1__TIME, newTime, newTime));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public POCDMT000040AssignedEntity getAssignedEntity() {
        return assignedEntity;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public NotificationChain basicSetAssignedEntity(POCDMT000040AssignedEntity newAssignedEntity, NotificationChain msgs) {
        POCDMT000040AssignedEntity oldAssignedEntity = assignedEntity;
        assignedEntity = newAssignedEntity;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, V3Package.POCDMT000040_PERFORMER1__ASSIGNED_ENTITY, oldAssignedEntity, newAssignedEntity);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setAssignedEntity(POCDMT000040AssignedEntity newAssignedEntity) {
        if (newAssignedEntity != assignedEntity) {
            NotificationChain msgs = null;
            if (assignedEntity != null) msgs = ((InternalEObject) assignedEntity).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - V3Package.POCDMT000040_PERFORMER1__ASSIGNED_ENTITY, null, msgs);
            if (newAssignedEntity != null) msgs = ((InternalEObject) newAssignedEntity).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - V3Package.POCDMT000040_PERFORMER1__ASSIGNED_ENTITY, null, msgs);
            msgs = basicSetAssignedEntity(newAssignedEntity, msgs);
            if (msgs != null) msgs.dispatch();
        } else if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, V3Package.POCDMT000040_PERFORMER1__ASSIGNED_ENTITY, newAssignedEntity, newAssignedEntity));
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
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, V3Package.POCDMT000040_PERFORMER1__NULL_FLAVOR, oldNullFlavor, nullFlavor));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public XServiceEventPerformer getTypeCode() {
        return typeCode;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setTypeCode(XServiceEventPerformer newTypeCode) {
        XServiceEventPerformer oldTypeCode = typeCode;
        typeCode = newTypeCode == null ? TYPE_CODE_EDEFAULT : newTypeCode;
        boolean oldTypeCodeESet = typeCodeESet;
        typeCodeESet = true;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, V3Package.POCDMT000040_PERFORMER1__TYPE_CODE, oldTypeCode, typeCode, !oldTypeCodeESet));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void unsetTypeCode() {
        XServiceEventPerformer oldTypeCode = typeCode;
        boolean oldTypeCodeESet = typeCodeESet;
        typeCode = TYPE_CODE_EDEFAULT;
        typeCodeESet = false;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.UNSET, V3Package.POCDMT000040_PERFORMER1__TYPE_CODE, oldTypeCode, TYPE_CODE_EDEFAULT, oldTypeCodeESet));
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
            case V3Package.POCDMT000040_PERFORMER1__REALM_CODE:
                return ((InternalEList<?>) getRealmCode()).basicRemove(otherEnd, msgs);
            case V3Package.POCDMT000040_PERFORMER1__TYPE_ID:
                return basicSetTypeId(null, msgs);
            case V3Package.POCDMT000040_PERFORMER1__TEMPLATE_ID:
                return ((InternalEList<?>) getTemplateId()).basicRemove(otherEnd, msgs);
            case V3Package.POCDMT000040_PERFORMER1__FUNCTION_CODE:
                return basicSetFunctionCode(null, msgs);
            case V3Package.POCDMT000040_PERFORMER1__TIME:
                return basicSetTime(null, msgs);
            case V3Package.POCDMT000040_PERFORMER1__ASSIGNED_ENTITY:
                return basicSetAssignedEntity(null, msgs);
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
            case V3Package.POCDMT000040_PERFORMER1__REALM_CODE:
                return getRealmCode();
            case V3Package.POCDMT000040_PERFORMER1__TYPE_ID:
                return getTypeId();
            case V3Package.POCDMT000040_PERFORMER1__TEMPLATE_ID:
                return getTemplateId();
            case V3Package.POCDMT000040_PERFORMER1__FUNCTION_CODE:
                return getFunctionCode();
            case V3Package.POCDMT000040_PERFORMER1__TIME:
                return getTime();
            case V3Package.POCDMT000040_PERFORMER1__ASSIGNED_ENTITY:
                return getAssignedEntity();
            case V3Package.POCDMT000040_PERFORMER1__NULL_FLAVOR:
                return getNullFlavor();
            case V3Package.POCDMT000040_PERFORMER1__TYPE_CODE:
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
            case V3Package.POCDMT000040_PERFORMER1__REALM_CODE:
                getRealmCode().clear();
                getRealmCode().addAll((Collection<? extends CS1>) newValue);
                return;
            case V3Package.POCDMT000040_PERFORMER1__TYPE_ID:
                setTypeId((POCDMT000040InfrastructureRootTypeId) newValue);
                return;
            case V3Package.POCDMT000040_PERFORMER1__TEMPLATE_ID:
                getTemplateId().clear();
                getTemplateId().addAll((Collection<? extends II>) newValue);
                return;
            case V3Package.POCDMT000040_PERFORMER1__FUNCTION_CODE:
                setFunctionCode((CE) newValue);
                return;
            case V3Package.POCDMT000040_PERFORMER1__TIME:
                setTime((IVLTS) newValue);
                return;
            case V3Package.POCDMT000040_PERFORMER1__ASSIGNED_ENTITY:
                setAssignedEntity((POCDMT000040AssignedEntity) newValue);
                return;
            case V3Package.POCDMT000040_PERFORMER1__NULL_FLAVOR:
                setNullFlavor((Enumerator) newValue);
                return;
            case V3Package.POCDMT000040_PERFORMER1__TYPE_CODE:
                setTypeCode((XServiceEventPerformer) newValue);
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
            case V3Package.POCDMT000040_PERFORMER1__REALM_CODE:
                getRealmCode().clear();
                return;
            case V3Package.POCDMT000040_PERFORMER1__TYPE_ID:
                setTypeId((POCDMT000040InfrastructureRootTypeId) null);
                return;
            case V3Package.POCDMT000040_PERFORMER1__TEMPLATE_ID:
                getTemplateId().clear();
                return;
            case V3Package.POCDMT000040_PERFORMER1__FUNCTION_CODE:
                setFunctionCode((CE) null);
                return;
            case V3Package.POCDMT000040_PERFORMER1__TIME:
                setTime((IVLTS) null);
                return;
            case V3Package.POCDMT000040_PERFORMER1__ASSIGNED_ENTITY:
                setAssignedEntity((POCDMT000040AssignedEntity) null);
                return;
            case V3Package.POCDMT000040_PERFORMER1__NULL_FLAVOR:
                setNullFlavor(NULL_FLAVOR_EDEFAULT);
                return;
            case V3Package.POCDMT000040_PERFORMER1__TYPE_CODE:
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
            case V3Package.POCDMT000040_PERFORMER1__REALM_CODE:
                return realmCode != null && !realmCode.isEmpty();
            case V3Package.POCDMT000040_PERFORMER1__TYPE_ID:
                return typeId != null;
            case V3Package.POCDMT000040_PERFORMER1__TEMPLATE_ID:
                return templateId != null && !templateId.isEmpty();
            case V3Package.POCDMT000040_PERFORMER1__FUNCTION_CODE:
                return functionCode != null;
            case V3Package.POCDMT000040_PERFORMER1__TIME:
                return time != null;
            case V3Package.POCDMT000040_PERFORMER1__ASSIGNED_ENTITY:
                return assignedEntity != null;
            case V3Package.POCDMT000040_PERFORMER1__NULL_FLAVOR:
                return NULL_FLAVOR_EDEFAULT == null ? nullFlavor != null : !NULL_FLAVOR_EDEFAULT.equals(nullFlavor);
            case V3Package.POCDMT000040_PERFORMER1__TYPE_CODE:
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
