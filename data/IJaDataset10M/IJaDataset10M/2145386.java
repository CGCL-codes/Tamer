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
import org.hl7.v3.AD;
import org.hl7.v3.CE;
import org.hl7.v3.CS1;
import org.hl7.v3.II;
import org.hl7.v3.POCDMT000040Device;
import org.hl7.v3.POCDMT000040Entity;
import org.hl7.v3.POCDMT000040InfrastructureRootTypeId;
import org.hl7.v3.POCDMT000040ParticipantRole;
import org.hl7.v3.POCDMT000040PlayingEntity;
import org.hl7.v3.TEL;
import org.hl7.v3.V3Package;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>POCDMT000040 Participant Role</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.hl7.v3.impl.POCDMT000040ParticipantRoleImpl#getRealmCode <em>Realm Code</em>}</li>
 *   <li>{@link org.hl7.v3.impl.POCDMT000040ParticipantRoleImpl#getTypeId <em>Type Id</em>}</li>
 *   <li>{@link org.hl7.v3.impl.POCDMT000040ParticipantRoleImpl#getTemplateId <em>Template Id</em>}</li>
 *   <li>{@link org.hl7.v3.impl.POCDMT000040ParticipantRoleImpl#getId <em>Id</em>}</li>
 *   <li>{@link org.hl7.v3.impl.POCDMT000040ParticipantRoleImpl#getCode <em>Code</em>}</li>
 *   <li>{@link org.hl7.v3.impl.POCDMT000040ParticipantRoleImpl#getAddr <em>Addr</em>}</li>
 *   <li>{@link org.hl7.v3.impl.POCDMT000040ParticipantRoleImpl#getTelecom <em>Telecom</em>}</li>
 *   <li>{@link org.hl7.v3.impl.POCDMT000040ParticipantRoleImpl#getPlayingDevice <em>Playing Device</em>}</li>
 *   <li>{@link org.hl7.v3.impl.POCDMT000040ParticipantRoleImpl#getPlayingEntity <em>Playing Entity</em>}</li>
 *   <li>{@link org.hl7.v3.impl.POCDMT000040ParticipantRoleImpl#getScopingEntity <em>Scoping Entity</em>}</li>
 *   <li>{@link org.hl7.v3.impl.POCDMT000040ParticipantRoleImpl#getClassCode <em>Class Code</em>}</li>
 *   <li>{@link org.hl7.v3.impl.POCDMT000040ParticipantRoleImpl#getNullFlavor <em>Null Flavor</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class POCDMT000040ParticipantRoleImpl extends EObjectImpl implements POCDMT000040ParticipantRole {

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
	 * The cached value of the '{@link #getId() <em>Id</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getId()
	 * @generated
	 * @ordered
	 */
    protected EList<II> id;

    /**
	 * The cached value of the '{@link #getCode() <em>Code</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCode()
	 * @generated
	 * @ordered
	 */
    protected CE code;

    /**
	 * The cached value of the '{@link #getAddr() <em>Addr</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getAddr()
	 * @generated
	 * @ordered
	 */
    protected EList<AD> addr;

    /**
	 * The cached value of the '{@link #getTelecom() <em>Telecom</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getTelecom()
	 * @generated
	 * @ordered
	 */
    protected EList<TEL> telecom;

    /**
	 * The cached value of the '{@link #getPlayingDevice() <em>Playing Device</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPlayingDevice()
	 * @generated
	 * @ordered
	 */
    protected POCDMT000040Device playingDevice;

    /**
	 * The cached value of the '{@link #getPlayingEntity() <em>Playing Entity</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPlayingEntity()
	 * @generated
	 * @ordered
	 */
    protected POCDMT000040PlayingEntity playingEntity;

    /**
	 * The cached value of the '{@link #getScopingEntity() <em>Scoping Entity</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getScopingEntity()
	 * @generated
	 * @ordered
	 */
    protected POCDMT000040Entity scopingEntity;

    /**
	 * The default value of the '{@link #getClassCode() <em>Class Code</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getClassCode()
	 * @generated
	 * @ordered
	 */
    protected static final Object CLASS_CODE_EDEFAULT = new Object();

    /**
	 * The cached value of the '{@link #getClassCode() <em>Class Code</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getClassCode()
	 * @generated
	 * @ordered
	 */
    protected Object classCode = CLASS_CODE_EDEFAULT;

    /**
	 * This is true if the Class Code attribute has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    protected boolean classCodeESet;

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
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    protected POCDMT000040ParticipantRoleImpl() {
        super();
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    protected EClass eStaticClass() {
        return V3Package.eINSTANCE.getPOCDMT000040ParticipantRole();
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EList<CS1> getRealmCode() {
        if (realmCode == null) {
            realmCode = new EObjectContainmentEList<CS1>(CS1.class, this, V3Package.POCDMT000040_PARTICIPANT_ROLE__REALM_CODE);
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
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, V3Package.POCDMT000040_PARTICIPANT_ROLE__TYPE_ID, oldTypeId, newTypeId);
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
            if (typeId != null) msgs = ((InternalEObject) typeId).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - V3Package.POCDMT000040_PARTICIPANT_ROLE__TYPE_ID, null, msgs);
            if (newTypeId != null) msgs = ((InternalEObject) newTypeId).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - V3Package.POCDMT000040_PARTICIPANT_ROLE__TYPE_ID, null, msgs);
            msgs = basicSetTypeId(newTypeId, msgs);
            if (msgs != null) msgs.dispatch();
        } else if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, V3Package.POCDMT000040_PARTICIPANT_ROLE__TYPE_ID, newTypeId, newTypeId));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EList<II> getTemplateId() {
        if (templateId == null) {
            templateId = new EObjectContainmentEList<II>(II.class, this, V3Package.POCDMT000040_PARTICIPANT_ROLE__TEMPLATE_ID);
        }
        return templateId;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EList<II> getId() {
        if (id == null) {
            id = new EObjectContainmentEList<II>(II.class, this, V3Package.POCDMT000040_PARTICIPANT_ROLE__ID);
        }
        return id;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public CE getCode() {
        return code;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public NotificationChain basicSetCode(CE newCode, NotificationChain msgs) {
        CE oldCode = code;
        code = newCode;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, V3Package.POCDMT000040_PARTICIPANT_ROLE__CODE, oldCode, newCode);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setCode(CE newCode) {
        if (newCode != code) {
            NotificationChain msgs = null;
            if (code != null) msgs = ((InternalEObject) code).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - V3Package.POCDMT000040_PARTICIPANT_ROLE__CODE, null, msgs);
            if (newCode != null) msgs = ((InternalEObject) newCode).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - V3Package.POCDMT000040_PARTICIPANT_ROLE__CODE, null, msgs);
            msgs = basicSetCode(newCode, msgs);
            if (msgs != null) msgs.dispatch();
        } else if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, V3Package.POCDMT000040_PARTICIPANT_ROLE__CODE, newCode, newCode));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EList<AD> getAddr() {
        if (addr == null) {
            addr = new EObjectContainmentEList<AD>(AD.class, this, V3Package.POCDMT000040_PARTICIPANT_ROLE__ADDR);
        }
        return addr;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EList<TEL> getTelecom() {
        if (telecom == null) {
            telecom = new EObjectContainmentEList<TEL>(TEL.class, this, V3Package.POCDMT000040_PARTICIPANT_ROLE__TELECOM);
        }
        return telecom;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public POCDMT000040Device getPlayingDevice() {
        return playingDevice;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public NotificationChain basicSetPlayingDevice(POCDMT000040Device newPlayingDevice, NotificationChain msgs) {
        POCDMT000040Device oldPlayingDevice = playingDevice;
        playingDevice = newPlayingDevice;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, V3Package.POCDMT000040_PARTICIPANT_ROLE__PLAYING_DEVICE, oldPlayingDevice, newPlayingDevice);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setPlayingDevice(POCDMT000040Device newPlayingDevice) {
        if (newPlayingDevice != playingDevice) {
            NotificationChain msgs = null;
            if (playingDevice != null) msgs = ((InternalEObject) playingDevice).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - V3Package.POCDMT000040_PARTICIPANT_ROLE__PLAYING_DEVICE, null, msgs);
            if (newPlayingDevice != null) msgs = ((InternalEObject) newPlayingDevice).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - V3Package.POCDMT000040_PARTICIPANT_ROLE__PLAYING_DEVICE, null, msgs);
            msgs = basicSetPlayingDevice(newPlayingDevice, msgs);
            if (msgs != null) msgs.dispatch();
        } else if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, V3Package.POCDMT000040_PARTICIPANT_ROLE__PLAYING_DEVICE, newPlayingDevice, newPlayingDevice));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public POCDMT000040PlayingEntity getPlayingEntity() {
        return playingEntity;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public NotificationChain basicSetPlayingEntity(POCDMT000040PlayingEntity newPlayingEntity, NotificationChain msgs) {
        POCDMT000040PlayingEntity oldPlayingEntity = playingEntity;
        playingEntity = newPlayingEntity;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, V3Package.POCDMT000040_PARTICIPANT_ROLE__PLAYING_ENTITY, oldPlayingEntity, newPlayingEntity);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setPlayingEntity(POCDMT000040PlayingEntity newPlayingEntity) {
        if (newPlayingEntity != playingEntity) {
            NotificationChain msgs = null;
            if (playingEntity != null) msgs = ((InternalEObject) playingEntity).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - V3Package.POCDMT000040_PARTICIPANT_ROLE__PLAYING_ENTITY, null, msgs);
            if (newPlayingEntity != null) msgs = ((InternalEObject) newPlayingEntity).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - V3Package.POCDMT000040_PARTICIPANT_ROLE__PLAYING_ENTITY, null, msgs);
            msgs = basicSetPlayingEntity(newPlayingEntity, msgs);
            if (msgs != null) msgs.dispatch();
        } else if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, V3Package.POCDMT000040_PARTICIPANT_ROLE__PLAYING_ENTITY, newPlayingEntity, newPlayingEntity));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public POCDMT000040Entity getScopingEntity() {
        return scopingEntity;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public NotificationChain basicSetScopingEntity(POCDMT000040Entity newScopingEntity, NotificationChain msgs) {
        POCDMT000040Entity oldScopingEntity = scopingEntity;
        scopingEntity = newScopingEntity;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, V3Package.POCDMT000040_PARTICIPANT_ROLE__SCOPING_ENTITY, oldScopingEntity, newScopingEntity);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setScopingEntity(POCDMT000040Entity newScopingEntity) {
        if (newScopingEntity != scopingEntity) {
            NotificationChain msgs = null;
            if (scopingEntity != null) msgs = ((InternalEObject) scopingEntity).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - V3Package.POCDMT000040_PARTICIPANT_ROLE__SCOPING_ENTITY, null, msgs);
            if (newScopingEntity != null) msgs = ((InternalEObject) newScopingEntity).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - V3Package.POCDMT000040_PARTICIPANT_ROLE__SCOPING_ENTITY, null, msgs);
            msgs = basicSetScopingEntity(newScopingEntity, msgs);
            if (msgs != null) msgs.dispatch();
        } else if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, V3Package.POCDMT000040_PARTICIPANT_ROLE__SCOPING_ENTITY, newScopingEntity, newScopingEntity));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public Object getClassCode() {
        return classCode;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setClassCode(Object newClassCode) {
        Object oldClassCode = classCode;
        classCode = newClassCode;
        boolean oldClassCodeESet = classCodeESet;
        classCodeESet = true;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, V3Package.POCDMT000040_PARTICIPANT_ROLE__CLASS_CODE, oldClassCode, classCode, !oldClassCodeESet));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void unsetClassCode() {
        Object oldClassCode = classCode;
        boolean oldClassCodeESet = classCodeESet;
        classCode = CLASS_CODE_EDEFAULT;
        classCodeESet = false;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.UNSET, V3Package.POCDMT000040_PARTICIPANT_ROLE__CLASS_CODE, oldClassCode, CLASS_CODE_EDEFAULT, oldClassCodeESet));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public boolean isSetClassCode() {
        return classCodeESet;
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
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, V3Package.POCDMT000040_PARTICIPANT_ROLE__NULL_FLAVOR, oldNullFlavor, nullFlavor));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch(featureID) {
            case V3Package.POCDMT000040_PARTICIPANT_ROLE__REALM_CODE:
                return ((InternalEList<?>) getRealmCode()).basicRemove(otherEnd, msgs);
            case V3Package.POCDMT000040_PARTICIPANT_ROLE__TYPE_ID:
                return basicSetTypeId(null, msgs);
            case V3Package.POCDMT000040_PARTICIPANT_ROLE__TEMPLATE_ID:
                return ((InternalEList<?>) getTemplateId()).basicRemove(otherEnd, msgs);
            case V3Package.POCDMT000040_PARTICIPANT_ROLE__ID:
                return ((InternalEList<?>) getId()).basicRemove(otherEnd, msgs);
            case V3Package.POCDMT000040_PARTICIPANT_ROLE__CODE:
                return basicSetCode(null, msgs);
            case V3Package.POCDMT000040_PARTICIPANT_ROLE__ADDR:
                return ((InternalEList<?>) getAddr()).basicRemove(otherEnd, msgs);
            case V3Package.POCDMT000040_PARTICIPANT_ROLE__TELECOM:
                return ((InternalEList<?>) getTelecom()).basicRemove(otherEnd, msgs);
            case V3Package.POCDMT000040_PARTICIPANT_ROLE__PLAYING_DEVICE:
                return basicSetPlayingDevice(null, msgs);
            case V3Package.POCDMT000040_PARTICIPANT_ROLE__PLAYING_ENTITY:
                return basicSetPlayingEntity(null, msgs);
            case V3Package.POCDMT000040_PARTICIPANT_ROLE__SCOPING_ENTITY:
                return basicSetScopingEntity(null, msgs);
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
            case V3Package.POCDMT000040_PARTICIPANT_ROLE__REALM_CODE:
                return getRealmCode();
            case V3Package.POCDMT000040_PARTICIPANT_ROLE__TYPE_ID:
                return getTypeId();
            case V3Package.POCDMT000040_PARTICIPANT_ROLE__TEMPLATE_ID:
                return getTemplateId();
            case V3Package.POCDMT000040_PARTICIPANT_ROLE__ID:
                return getId();
            case V3Package.POCDMT000040_PARTICIPANT_ROLE__CODE:
                return getCode();
            case V3Package.POCDMT000040_PARTICIPANT_ROLE__ADDR:
                return getAddr();
            case V3Package.POCDMT000040_PARTICIPANT_ROLE__TELECOM:
                return getTelecom();
            case V3Package.POCDMT000040_PARTICIPANT_ROLE__PLAYING_DEVICE:
                return getPlayingDevice();
            case V3Package.POCDMT000040_PARTICIPANT_ROLE__PLAYING_ENTITY:
                return getPlayingEntity();
            case V3Package.POCDMT000040_PARTICIPANT_ROLE__SCOPING_ENTITY:
                return getScopingEntity();
            case V3Package.POCDMT000040_PARTICIPANT_ROLE__CLASS_CODE:
                return getClassCode();
            case V3Package.POCDMT000040_PARTICIPANT_ROLE__NULL_FLAVOR:
                return getNullFlavor();
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
            case V3Package.POCDMT000040_PARTICIPANT_ROLE__REALM_CODE:
                getRealmCode().clear();
                getRealmCode().addAll((Collection<? extends CS1>) newValue);
                return;
            case V3Package.POCDMT000040_PARTICIPANT_ROLE__TYPE_ID:
                setTypeId((POCDMT000040InfrastructureRootTypeId) newValue);
                return;
            case V3Package.POCDMT000040_PARTICIPANT_ROLE__TEMPLATE_ID:
                getTemplateId().clear();
                getTemplateId().addAll((Collection<? extends II>) newValue);
                return;
            case V3Package.POCDMT000040_PARTICIPANT_ROLE__ID:
                getId().clear();
                getId().addAll((Collection<? extends II>) newValue);
                return;
            case V3Package.POCDMT000040_PARTICIPANT_ROLE__CODE:
                setCode((CE) newValue);
                return;
            case V3Package.POCDMT000040_PARTICIPANT_ROLE__ADDR:
                getAddr().clear();
                getAddr().addAll((Collection<? extends AD>) newValue);
                return;
            case V3Package.POCDMT000040_PARTICIPANT_ROLE__TELECOM:
                getTelecom().clear();
                getTelecom().addAll((Collection<? extends TEL>) newValue);
                return;
            case V3Package.POCDMT000040_PARTICIPANT_ROLE__PLAYING_DEVICE:
                setPlayingDevice((POCDMT000040Device) newValue);
                return;
            case V3Package.POCDMT000040_PARTICIPANT_ROLE__PLAYING_ENTITY:
                setPlayingEntity((POCDMT000040PlayingEntity) newValue);
                return;
            case V3Package.POCDMT000040_PARTICIPANT_ROLE__SCOPING_ENTITY:
                setScopingEntity((POCDMT000040Entity) newValue);
                return;
            case V3Package.POCDMT000040_PARTICIPANT_ROLE__CLASS_CODE:
                setClassCode(newValue);
                return;
            case V3Package.POCDMT000040_PARTICIPANT_ROLE__NULL_FLAVOR:
                setNullFlavor((Enumerator) newValue);
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
            case V3Package.POCDMT000040_PARTICIPANT_ROLE__REALM_CODE:
                getRealmCode().clear();
                return;
            case V3Package.POCDMT000040_PARTICIPANT_ROLE__TYPE_ID:
                setTypeId((POCDMT000040InfrastructureRootTypeId) null);
                return;
            case V3Package.POCDMT000040_PARTICIPANT_ROLE__TEMPLATE_ID:
                getTemplateId().clear();
                return;
            case V3Package.POCDMT000040_PARTICIPANT_ROLE__ID:
                getId().clear();
                return;
            case V3Package.POCDMT000040_PARTICIPANT_ROLE__CODE:
                setCode((CE) null);
                return;
            case V3Package.POCDMT000040_PARTICIPANT_ROLE__ADDR:
                getAddr().clear();
                return;
            case V3Package.POCDMT000040_PARTICIPANT_ROLE__TELECOM:
                getTelecom().clear();
                return;
            case V3Package.POCDMT000040_PARTICIPANT_ROLE__PLAYING_DEVICE:
                setPlayingDevice((POCDMT000040Device) null);
                return;
            case V3Package.POCDMT000040_PARTICIPANT_ROLE__PLAYING_ENTITY:
                setPlayingEntity((POCDMT000040PlayingEntity) null);
                return;
            case V3Package.POCDMT000040_PARTICIPANT_ROLE__SCOPING_ENTITY:
                setScopingEntity((POCDMT000040Entity) null);
                return;
            case V3Package.POCDMT000040_PARTICIPANT_ROLE__CLASS_CODE:
                unsetClassCode();
                return;
            case V3Package.POCDMT000040_PARTICIPANT_ROLE__NULL_FLAVOR:
                setNullFlavor(NULL_FLAVOR_EDEFAULT);
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
            case V3Package.POCDMT000040_PARTICIPANT_ROLE__REALM_CODE:
                return realmCode != null && !realmCode.isEmpty();
            case V3Package.POCDMT000040_PARTICIPANT_ROLE__TYPE_ID:
                return typeId != null;
            case V3Package.POCDMT000040_PARTICIPANT_ROLE__TEMPLATE_ID:
                return templateId != null && !templateId.isEmpty();
            case V3Package.POCDMT000040_PARTICIPANT_ROLE__ID:
                return id != null && !id.isEmpty();
            case V3Package.POCDMT000040_PARTICIPANT_ROLE__CODE:
                return code != null;
            case V3Package.POCDMT000040_PARTICIPANT_ROLE__ADDR:
                return addr != null && !addr.isEmpty();
            case V3Package.POCDMT000040_PARTICIPANT_ROLE__TELECOM:
                return telecom != null && !telecom.isEmpty();
            case V3Package.POCDMT000040_PARTICIPANT_ROLE__PLAYING_DEVICE:
                return playingDevice != null;
            case V3Package.POCDMT000040_PARTICIPANT_ROLE__PLAYING_ENTITY:
                return playingEntity != null;
            case V3Package.POCDMT000040_PARTICIPANT_ROLE__SCOPING_ENTITY:
                return scopingEntity != null;
            case V3Package.POCDMT000040_PARTICIPANT_ROLE__CLASS_CODE:
                return isSetClassCode();
            case V3Package.POCDMT000040_PARTICIPANT_ROLE__NULL_FLAVOR:
                return NULL_FLAVOR_EDEFAULT == null ? nullFlavor != null : !NULL_FLAVOR_EDEFAULT.equals(nullFlavor);
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
        result.append(" (classCode: ");
        if (classCodeESet) result.append(classCode); else result.append("<unset>");
        result.append(", nullFlavor: ");
        result.append(nullFlavor);
        result.append(')');
        return result.toString();
    }
}
