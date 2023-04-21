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
import org.hl7.v3.CD;
import org.hl7.v3.CE;
import org.hl7.v3.CS1;
import org.hl7.v3.ED;
import org.hl7.v3.II;
import org.hl7.v3.IVLTS;
import org.hl7.v3.POCDMT000040Author;
import org.hl7.v3.POCDMT000040EntryRelationship;
import org.hl7.v3.POCDMT000040Informant12;
import org.hl7.v3.POCDMT000040InfrastructureRootTypeId;
import org.hl7.v3.POCDMT000040Participant2;
import org.hl7.v3.POCDMT000040Performer2;
import org.hl7.v3.POCDMT000040Precondition;
import org.hl7.v3.POCDMT000040Procedure;
import org.hl7.v3.POCDMT000040Reference;
import org.hl7.v3.POCDMT000040Specimen;
import org.hl7.v3.POCDMT000040Subject;
import org.hl7.v3.V3Package;
import org.hl7.v3.XDocumentProcedureMood;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>POCDMT000040 Procedure</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.hl7.v3.impl.POCDMT000040ProcedureImpl#getRealmCode <em>Realm Code</em>}</li>
 *   <li>{@link org.hl7.v3.impl.POCDMT000040ProcedureImpl#getTypeId <em>Type Id</em>}</li>
 *   <li>{@link org.hl7.v3.impl.POCDMT000040ProcedureImpl#getTemplateId <em>Template Id</em>}</li>
 *   <li>{@link org.hl7.v3.impl.POCDMT000040ProcedureImpl#getId <em>Id</em>}</li>
 *   <li>{@link org.hl7.v3.impl.POCDMT000040ProcedureImpl#getCode <em>Code</em>}</li>
 *   <li>{@link org.hl7.v3.impl.POCDMT000040ProcedureImpl#getText <em>Text</em>}</li>
 *   <li>{@link org.hl7.v3.impl.POCDMT000040ProcedureImpl#getStatusCode <em>Status Code</em>}</li>
 *   <li>{@link org.hl7.v3.impl.POCDMT000040ProcedureImpl#getEffectiveTime <em>Effective Time</em>}</li>
 *   <li>{@link org.hl7.v3.impl.POCDMT000040ProcedureImpl#getPriorityCode <em>Priority Code</em>}</li>
 *   <li>{@link org.hl7.v3.impl.POCDMT000040ProcedureImpl#getLanguageCode <em>Language Code</em>}</li>
 *   <li>{@link org.hl7.v3.impl.POCDMT000040ProcedureImpl#getMethodCode <em>Method Code</em>}</li>
 *   <li>{@link org.hl7.v3.impl.POCDMT000040ProcedureImpl#getApproachSiteCode <em>Approach Site Code</em>}</li>
 *   <li>{@link org.hl7.v3.impl.POCDMT000040ProcedureImpl#getTargetSiteCode <em>Target Site Code</em>}</li>
 *   <li>{@link org.hl7.v3.impl.POCDMT000040ProcedureImpl#getSubject <em>Subject</em>}</li>
 *   <li>{@link org.hl7.v3.impl.POCDMT000040ProcedureImpl#getSpecimen <em>Specimen</em>}</li>
 *   <li>{@link org.hl7.v3.impl.POCDMT000040ProcedureImpl#getPerformer <em>Performer</em>}</li>
 *   <li>{@link org.hl7.v3.impl.POCDMT000040ProcedureImpl#getAuthor <em>Author</em>}</li>
 *   <li>{@link org.hl7.v3.impl.POCDMT000040ProcedureImpl#getInformant <em>Informant</em>}</li>
 *   <li>{@link org.hl7.v3.impl.POCDMT000040ProcedureImpl#getParticipant <em>Participant</em>}</li>
 *   <li>{@link org.hl7.v3.impl.POCDMT000040ProcedureImpl#getEntryRelationship <em>Entry Relationship</em>}</li>
 *   <li>{@link org.hl7.v3.impl.POCDMT000040ProcedureImpl#getReference <em>Reference</em>}</li>
 *   <li>{@link org.hl7.v3.impl.POCDMT000040ProcedureImpl#getPrecondition <em>Precondition</em>}</li>
 *   <li>{@link org.hl7.v3.impl.POCDMT000040ProcedureImpl#getClassCode <em>Class Code</em>}</li>
 *   <li>{@link org.hl7.v3.impl.POCDMT000040ProcedureImpl#getMoodCode <em>Mood Code</em>}</li>
 *   <li>{@link org.hl7.v3.impl.POCDMT000040ProcedureImpl#isNegationInd <em>Negation Ind</em>}</li>
 *   <li>{@link org.hl7.v3.impl.POCDMT000040ProcedureImpl#getNullFlavor <em>Null Flavor</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class POCDMT000040ProcedureImpl extends EObjectImpl implements POCDMT000040Procedure {

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
    protected CD code;

    /**
	 * The cached value of the '{@link #getText() <em>Text</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getText()
	 * @generated
	 * @ordered
	 */
    protected ED text;

    /**
	 * The cached value of the '{@link #getStatusCode() <em>Status Code</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getStatusCode()
	 * @generated
	 * @ordered
	 */
    protected CS1 statusCode;

    /**
	 * The cached value of the '{@link #getEffectiveTime() <em>Effective Time</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getEffectiveTime()
	 * @generated
	 * @ordered
	 */
    protected IVLTS effectiveTime;

    /**
	 * The cached value of the '{@link #getPriorityCode() <em>Priority Code</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPriorityCode()
	 * @generated
	 * @ordered
	 */
    protected CE priorityCode;

    /**
	 * The cached value of the '{@link #getLanguageCode() <em>Language Code</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLanguageCode()
	 * @generated
	 * @ordered
	 */
    protected CS1 languageCode;

    /**
	 * The cached value of the '{@link #getMethodCode() <em>Method Code</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMethodCode()
	 * @generated
	 * @ordered
	 */
    protected EList<CE> methodCode;

    /**
	 * The cached value of the '{@link #getApproachSiteCode() <em>Approach Site Code</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getApproachSiteCode()
	 * @generated
	 * @ordered
	 */
    protected EList<CD> approachSiteCode;

    /**
	 * The cached value of the '{@link #getTargetSiteCode() <em>Target Site Code</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getTargetSiteCode()
	 * @generated
	 * @ordered
	 */
    protected EList<CD> targetSiteCode;

    /**
	 * The cached value of the '{@link #getSubject() <em>Subject</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSubject()
	 * @generated
	 * @ordered
	 */
    protected POCDMT000040Subject subject;

    /**
	 * The cached value of the '{@link #getSpecimen() <em>Specimen</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSpecimen()
	 * @generated
	 * @ordered
	 */
    protected EList<POCDMT000040Specimen> specimen;

    /**
	 * The cached value of the '{@link #getPerformer() <em>Performer</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPerformer()
	 * @generated
	 * @ordered
	 */
    protected EList<POCDMT000040Performer2> performer;

    /**
	 * The cached value of the '{@link #getAuthor() <em>Author</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getAuthor()
	 * @generated
	 * @ordered
	 */
    protected EList<POCDMT000040Author> author;

    /**
	 * The cached value of the '{@link #getInformant() <em>Informant</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getInformant()
	 * @generated
	 * @ordered
	 */
    protected EList<POCDMT000040Informant12> informant;

    /**
	 * The cached value of the '{@link #getParticipant() <em>Participant</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getParticipant()
	 * @generated
	 * @ordered
	 */
    protected EList<POCDMT000040Participant2> participant;

    /**
	 * The cached value of the '{@link #getEntryRelationship() <em>Entry Relationship</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getEntryRelationship()
	 * @generated
	 * @ordered
	 */
    protected EList<POCDMT000040EntryRelationship> entryRelationship;

    /**
	 * The cached value of the '{@link #getReference() <em>Reference</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getReference()
	 * @generated
	 * @ordered
	 */
    protected EList<POCDMT000040Reference> reference;

    /**
	 * The cached value of the '{@link #getPrecondition() <em>Precondition</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPrecondition()
	 * @generated
	 * @ordered
	 */
    protected EList<POCDMT000040Precondition> precondition;

    /**
	 * The default value of the '{@link #getClassCode() <em>Class Code</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getClassCode()
	 * @generated
	 * @ordered
	 */
    protected static final Enumerator CLASS_CODE_EDEFAULT = null;

    /**
	 * The cached value of the '{@link #getClassCode() <em>Class Code</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getClassCode()
	 * @generated
	 * @ordered
	 */
    protected Enumerator classCode = CLASS_CODE_EDEFAULT;

    /**
	 * The default value of the '{@link #getMoodCode() <em>Mood Code</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMoodCode()
	 * @generated
	 * @ordered
	 */
    protected static final XDocumentProcedureMood MOOD_CODE_EDEFAULT = XDocumentProcedureMood.APT;

    /**
	 * The cached value of the '{@link #getMoodCode() <em>Mood Code</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMoodCode()
	 * @generated
	 * @ordered
	 */
    protected XDocumentProcedureMood moodCode = MOOD_CODE_EDEFAULT;

    /**
	 * This is true if the Mood Code attribute has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    protected boolean moodCodeESet;

    /**
	 * The default value of the '{@link #isNegationInd() <em>Negation Ind</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isNegationInd()
	 * @generated
	 * @ordered
	 */
    protected static final boolean NEGATION_IND_EDEFAULT = false;

    /**
	 * The cached value of the '{@link #isNegationInd() <em>Negation Ind</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isNegationInd()
	 * @generated
	 * @ordered
	 */
    protected boolean negationInd = NEGATION_IND_EDEFAULT;

    /**
	 * This is true if the Negation Ind attribute has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    protected boolean negationIndESet;

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
    protected POCDMT000040ProcedureImpl() {
        super();
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    protected EClass eStaticClass() {
        return V3Package.eINSTANCE.getPOCDMT000040Procedure();
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EList<CS1> getRealmCode() {
        if (realmCode == null) {
            realmCode = new EObjectContainmentEList<CS1>(CS1.class, this, V3Package.POCDMT000040_PROCEDURE__REALM_CODE);
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
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, V3Package.POCDMT000040_PROCEDURE__TYPE_ID, oldTypeId, newTypeId);
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
            if (typeId != null) msgs = ((InternalEObject) typeId).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - V3Package.POCDMT000040_PROCEDURE__TYPE_ID, null, msgs);
            if (newTypeId != null) msgs = ((InternalEObject) newTypeId).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - V3Package.POCDMT000040_PROCEDURE__TYPE_ID, null, msgs);
            msgs = basicSetTypeId(newTypeId, msgs);
            if (msgs != null) msgs.dispatch();
        } else if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, V3Package.POCDMT000040_PROCEDURE__TYPE_ID, newTypeId, newTypeId));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EList<II> getTemplateId() {
        if (templateId == null) {
            templateId = new EObjectContainmentEList<II>(II.class, this, V3Package.POCDMT000040_PROCEDURE__TEMPLATE_ID);
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
            id = new EObjectContainmentEList<II>(II.class, this, V3Package.POCDMT000040_PROCEDURE__ID);
        }
        return id;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public CD getCode() {
        return code;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public NotificationChain basicSetCode(CD newCode, NotificationChain msgs) {
        CD oldCode = code;
        code = newCode;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, V3Package.POCDMT000040_PROCEDURE__CODE, oldCode, newCode);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setCode(CD newCode) {
        if (newCode != code) {
            NotificationChain msgs = null;
            if (code != null) msgs = ((InternalEObject) code).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - V3Package.POCDMT000040_PROCEDURE__CODE, null, msgs);
            if (newCode != null) msgs = ((InternalEObject) newCode).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - V3Package.POCDMT000040_PROCEDURE__CODE, null, msgs);
            msgs = basicSetCode(newCode, msgs);
            if (msgs != null) msgs.dispatch();
        } else if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, V3Package.POCDMT000040_PROCEDURE__CODE, newCode, newCode));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public ED getText() {
        return text;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public NotificationChain basicSetText(ED newText, NotificationChain msgs) {
        ED oldText = text;
        text = newText;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, V3Package.POCDMT000040_PROCEDURE__TEXT, oldText, newText);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setText(ED newText) {
        if (newText != text) {
            NotificationChain msgs = null;
            if (text != null) msgs = ((InternalEObject) text).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - V3Package.POCDMT000040_PROCEDURE__TEXT, null, msgs);
            if (newText != null) msgs = ((InternalEObject) newText).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - V3Package.POCDMT000040_PROCEDURE__TEXT, null, msgs);
            msgs = basicSetText(newText, msgs);
            if (msgs != null) msgs.dispatch();
        } else if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, V3Package.POCDMT000040_PROCEDURE__TEXT, newText, newText));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public CS1 getStatusCode() {
        return statusCode;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public NotificationChain basicSetStatusCode(CS1 newStatusCode, NotificationChain msgs) {
        CS1 oldStatusCode = statusCode;
        statusCode = newStatusCode;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, V3Package.POCDMT000040_PROCEDURE__STATUS_CODE, oldStatusCode, newStatusCode);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setStatusCode(CS1 newStatusCode) {
        if (newStatusCode != statusCode) {
            NotificationChain msgs = null;
            if (statusCode != null) msgs = ((InternalEObject) statusCode).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - V3Package.POCDMT000040_PROCEDURE__STATUS_CODE, null, msgs);
            if (newStatusCode != null) msgs = ((InternalEObject) newStatusCode).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - V3Package.POCDMT000040_PROCEDURE__STATUS_CODE, null, msgs);
            msgs = basicSetStatusCode(newStatusCode, msgs);
            if (msgs != null) msgs.dispatch();
        } else if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, V3Package.POCDMT000040_PROCEDURE__STATUS_CODE, newStatusCode, newStatusCode));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public IVLTS getEffectiveTime() {
        return effectiveTime;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public NotificationChain basicSetEffectiveTime(IVLTS newEffectiveTime, NotificationChain msgs) {
        IVLTS oldEffectiveTime = effectiveTime;
        effectiveTime = newEffectiveTime;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, V3Package.POCDMT000040_PROCEDURE__EFFECTIVE_TIME, oldEffectiveTime, newEffectiveTime);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setEffectiveTime(IVLTS newEffectiveTime) {
        if (newEffectiveTime != effectiveTime) {
            NotificationChain msgs = null;
            if (effectiveTime != null) msgs = ((InternalEObject) effectiveTime).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - V3Package.POCDMT000040_PROCEDURE__EFFECTIVE_TIME, null, msgs);
            if (newEffectiveTime != null) msgs = ((InternalEObject) newEffectiveTime).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - V3Package.POCDMT000040_PROCEDURE__EFFECTIVE_TIME, null, msgs);
            msgs = basicSetEffectiveTime(newEffectiveTime, msgs);
            if (msgs != null) msgs.dispatch();
        } else if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, V3Package.POCDMT000040_PROCEDURE__EFFECTIVE_TIME, newEffectiveTime, newEffectiveTime));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public CE getPriorityCode() {
        return priorityCode;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public NotificationChain basicSetPriorityCode(CE newPriorityCode, NotificationChain msgs) {
        CE oldPriorityCode = priorityCode;
        priorityCode = newPriorityCode;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, V3Package.POCDMT000040_PROCEDURE__PRIORITY_CODE, oldPriorityCode, newPriorityCode);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setPriorityCode(CE newPriorityCode) {
        if (newPriorityCode != priorityCode) {
            NotificationChain msgs = null;
            if (priorityCode != null) msgs = ((InternalEObject) priorityCode).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - V3Package.POCDMT000040_PROCEDURE__PRIORITY_CODE, null, msgs);
            if (newPriorityCode != null) msgs = ((InternalEObject) newPriorityCode).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - V3Package.POCDMT000040_PROCEDURE__PRIORITY_CODE, null, msgs);
            msgs = basicSetPriorityCode(newPriorityCode, msgs);
            if (msgs != null) msgs.dispatch();
        } else if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, V3Package.POCDMT000040_PROCEDURE__PRIORITY_CODE, newPriorityCode, newPriorityCode));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public CS1 getLanguageCode() {
        return languageCode;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public NotificationChain basicSetLanguageCode(CS1 newLanguageCode, NotificationChain msgs) {
        CS1 oldLanguageCode = languageCode;
        languageCode = newLanguageCode;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, V3Package.POCDMT000040_PROCEDURE__LANGUAGE_CODE, oldLanguageCode, newLanguageCode);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setLanguageCode(CS1 newLanguageCode) {
        if (newLanguageCode != languageCode) {
            NotificationChain msgs = null;
            if (languageCode != null) msgs = ((InternalEObject) languageCode).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - V3Package.POCDMT000040_PROCEDURE__LANGUAGE_CODE, null, msgs);
            if (newLanguageCode != null) msgs = ((InternalEObject) newLanguageCode).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - V3Package.POCDMT000040_PROCEDURE__LANGUAGE_CODE, null, msgs);
            msgs = basicSetLanguageCode(newLanguageCode, msgs);
            if (msgs != null) msgs.dispatch();
        } else if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, V3Package.POCDMT000040_PROCEDURE__LANGUAGE_CODE, newLanguageCode, newLanguageCode));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EList<CE> getMethodCode() {
        if (methodCode == null) {
            methodCode = new EObjectContainmentEList<CE>(CE.class, this, V3Package.POCDMT000040_PROCEDURE__METHOD_CODE);
        }
        return methodCode;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EList<CD> getApproachSiteCode() {
        if (approachSiteCode == null) {
            approachSiteCode = new EObjectContainmentEList<CD>(CD.class, this, V3Package.POCDMT000040_PROCEDURE__APPROACH_SITE_CODE);
        }
        return approachSiteCode;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EList<CD> getTargetSiteCode() {
        if (targetSiteCode == null) {
            targetSiteCode = new EObjectContainmentEList<CD>(CD.class, this, V3Package.POCDMT000040_PROCEDURE__TARGET_SITE_CODE);
        }
        return targetSiteCode;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public POCDMT000040Subject getSubject() {
        return subject;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public NotificationChain basicSetSubject(POCDMT000040Subject newSubject, NotificationChain msgs) {
        POCDMT000040Subject oldSubject = subject;
        subject = newSubject;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, V3Package.POCDMT000040_PROCEDURE__SUBJECT, oldSubject, newSubject);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setSubject(POCDMT000040Subject newSubject) {
        if (newSubject != subject) {
            NotificationChain msgs = null;
            if (subject != null) msgs = ((InternalEObject) subject).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - V3Package.POCDMT000040_PROCEDURE__SUBJECT, null, msgs);
            if (newSubject != null) msgs = ((InternalEObject) newSubject).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - V3Package.POCDMT000040_PROCEDURE__SUBJECT, null, msgs);
            msgs = basicSetSubject(newSubject, msgs);
            if (msgs != null) msgs.dispatch();
        } else if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, V3Package.POCDMT000040_PROCEDURE__SUBJECT, newSubject, newSubject));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EList<POCDMT000040Specimen> getSpecimen() {
        if (specimen == null) {
            specimen = new EObjectContainmentEList<POCDMT000040Specimen>(POCDMT000040Specimen.class, this, V3Package.POCDMT000040_PROCEDURE__SPECIMEN);
        }
        return specimen;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EList<POCDMT000040Performer2> getPerformer() {
        if (performer == null) {
            performer = new EObjectContainmentEList<POCDMT000040Performer2>(POCDMT000040Performer2.class, this, V3Package.POCDMT000040_PROCEDURE__PERFORMER);
        }
        return performer;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EList<POCDMT000040Author> getAuthor() {
        if (author == null) {
            author = new EObjectContainmentEList<POCDMT000040Author>(POCDMT000040Author.class, this, V3Package.POCDMT000040_PROCEDURE__AUTHOR);
        }
        return author;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EList<POCDMT000040Informant12> getInformant() {
        if (informant == null) {
            informant = new EObjectContainmentEList<POCDMT000040Informant12>(POCDMT000040Informant12.class, this, V3Package.POCDMT000040_PROCEDURE__INFORMANT);
        }
        return informant;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EList<POCDMT000040Participant2> getParticipant() {
        if (participant == null) {
            participant = new EObjectContainmentEList<POCDMT000040Participant2>(POCDMT000040Participant2.class, this, V3Package.POCDMT000040_PROCEDURE__PARTICIPANT);
        }
        return participant;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EList<POCDMT000040EntryRelationship> getEntryRelationship() {
        if (entryRelationship == null) {
            entryRelationship = new EObjectContainmentEList<POCDMT000040EntryRelationship>(POCDMT000040EntryRelationship.class, this, V3Package.POCDMT000040_PROCEDURE__ENTRY_RELATIONSHIP);
        }
        return entryRelationship;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EList<POCDMT000040Reference> getReference() {
        if (reference == null) {
            reference = new EObjectContainmentEList<POCDMT000040Reference>(POCDMT000040Reference.class, this, V3Package.POCDMT000040_PROCEDURE__REFERENCE);
        }
        return reference;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EList<POCDMT000040Precondition> getPrecondition() {
        if (precondition == null) {
            precondition = new EObjectContainmentEList<POCDMT000040Precondition>(POCDMT000040Precondition.class, this, V3Package.POCDMT000040_PROCEDURE__PRECONDITION);
        }
        return precondition;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public Enumerator getClassCode() {
        return classCode;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setClassCode(Enumerator newClassCode) {
        Enumerator oldClassCode = classCode;
        classCode = newClassCode;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, V3Package.POCDMT000040_PROCEDURE__CLASS_CODE, oldClassCode, classCode));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public XDocumentProcedureMood getMoodCode() {
        return moodCode;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setMoodCode(XDocumentProcedureMood newMoodCode) {
        XDocumentProcedureMood oldMoodCode = moodCode;
        moodCode = newMoodCode == null ? MOOD_CODE_EDEFAULT : newMoodCode;
        boolean oldMoodCodeESet = moodCodeESet;
        moodCodeESet = true;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, V3Package.POCDMT000040_PROCEDURE__MOOD_CODE, oldMoodCode, moodCode, !oldMoodCodeESet));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void unsetMoodCode() {
        XDocumentProcedureMood oldMoodCode = moodCode;
        boolean oldMoodCodeESet = moodCodeESet;
        moodCode = MOOD_CODE_EDEFAULT;
        moodCodeESet = false;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.UNSET, V3Package.POCDMT000040_PROCEDURE__MOOD_CODE, oldMoodCode, MOOD_CODE_EDEFAULT, oldMoodCodeESet));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public boolean isSetMoodCode() {
        return moodCodeESet;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public boolean isNegationInd() {
        return negationInd;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setNegationInd(boolean newNegationInd) {
        boolean oldNegationInd = negationInd;
        negationInd = newNegationInd;
        boolean oldNegationIndESet = negationIndESet;
        negationIndESet = true;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, V3Package.POCDMT000040_PROCEDURE__NEGATION_IND, oldNegationInd, negationInd, !oldNegationIndESet));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void unsetNegationInd() {
        boolean oldNegationInd = negationInd;
        boolean oldNegationIndESet = negationIndESet;
        negationInd = NEGATION_IND_EDEFAULT;
        negationIndESet = false;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.UNSET, V3Package.POCDMT000040_PROCEDURE__NEGATION_IND, oldNegationInd, NEGATION_IND_EDEFAULT, oldNegationIndESet));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public boolean isSetNegationInd() {
        return negationIndESet;
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
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, V3Package.POCDMT000040_PROCEDURE__NULL_FLAVOR, oldNullFlavor, nullFlavor));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch(featureID) {
            case V3Package.POCDMT000040_PROCEDURE__REALM_CODE:
                return ((InternalEList<?>) getRealmCode()).basicRemove(otherEnd, msgs);
            case V3Package.POCDMT000040_PROCEDURE__TYPE_ID:
                return basicSetTypeId(null, msgs);
            case V3Package.POCDMT000040_PROCEDURE__TEMPLATE_ID:
                return ((InternalEList<?>) getTemplateId()).basicRemove(otherEnd, msgs);
            case V3Package.POCDMT000040_PROCEDURE__ID:
                return ((InternalEList<?>) getId()).basicRemove(otherEnd, msgs);
            case V3Package.POCDMT000040_PROCEDURE__CODE:
                return basicSetCode(null, msgs);
            case V3Package.POCDMT000040_PROCEDURE__TEXT:
                return basicSetText(null, msgs);
            case V3Package.POCDMT000040_PROCEDURE__STATUS_CODE:
                return basicSetStatusCode(null, msgs);
            case V3Package.POCDMT000040_PROCEDURE__EFFECTIVE_TIME:
                return basicSetEffectiveTime(null, msgs);
            case V3Package.POCDMT000040_PROCEDURE__PRIORITY_CODE:
                return basicSetPriorityCode(null, msgs);
            case V3Package.POCDMT000040_PROCEDURE__LANGUAGE_CODE:
                return basicSetLanguageCode(null, msgs);
            case V3Package.POCDMT000040_PROCEDURE__METHOD_CODE:
                return ((InternalEList<?>) getMethodCode()).basicRemove(otherEnd, msgs);
            case V3Package.POCDMT000040_PROCEDURE__APPROACH_SITE_CODE:
                return ((InternalEList<?>) getApproachSiteCode()).basicRemove(otherEnd, msgs);
            case V3Package.POCDMT000040_PROCEDURE__TARGET_SITE_CODE:
                return ((InternalEList<?>) getTargetSiteCode()).basicRemove(otherEnd, msgs);
            case V3Package.POCDMT000040_PROCEDURE__SUBJECT:
                return basicSetSubject(null, msgs);
            case V3Package.POCDMT000040_PROCEDURE__SPECIMEN:
                return ((InternalEList<?>) getSpecimen()).basicRemove(otherEnd, msgs);
            case V3Package.POCDMT000040_PROCEDURE__PERFORMER:
                return ((InternalEList<?>) getPerformer()).basicRemove(otherEnd, msgs);
            case V3Package.POCDMT000040_PROCEDURE__AUTHOR:
                return ((InternalEList<?>) getAuthor()).basicRemove(otherEnd, msgs);
            case V3Package.POCDMT000040_PROCEDURE__INFORMANT:
                return ((InternalEList<?>) getInformant()).basicRemove(otherEnd, msgs);
            case V3Package.POCDMT000040_PROCEDURE__PARTICIPANT:
                return ((InternalEList<?>) getParticipant()).basicRemove(otherEnd, msgs);
            case V3Package.POCDMT000040_PROCEDURE__ENTRY_RELATIONSHIP:
                return ((InternalEList<?>) getEntryRelationship()).basicRemove(otherEnd, msgs);
            case V3Package.POCDMT000040_PROCEDURE__REFERENCE:
                return ((InternalEList<?>) getReference()).basicRemove(otherEnd, msgs);
            case V3Package.POCDMT000040_PROCEDURE__PRECONDITION:
                return ((InternalEList<?>) getPrecondition()).basicRemove(otherEnd, msgs);
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
            case V3Package.POCDMT000040_PROCEDURE__REALM_CODE:
                return getRealmCode();
            case V3Package.POCDMT000040_PROCEDURE__TYPE_ID:
                return getTypeId();
            case V3Package.POCDMT000040_PROCEDURE__TEMPLATE_ID:
                return getTemplateId();
            case V3Package.POCDMT000040_PROCEDURE__ID:
                return getId();
            case V3Package.POCDMT000040_PROCEDURE__CODE:
                return getCode();
            case V3Package.POCDMT000040_PROCEDURE__TEXT:
                return getText();
            case V3Package.POCDMT000040_PROCEDURE__STATUS_CODE:
                return getStatusCode();
            case V3Package.POCDMT000040_PROCEDURE__EFFECTIVE_TIME:
                return getEffectiveTime();
            case V3Package.POCDMT000040_PROCEDURE__PRIORITY_CODE:
                return getPriorityCode();
            case V3Package.POCDMT000040_PROCEDURE__LANGUAGE_CODE:
                return getLanguageCode();
            case V3Package.POCDMT000040_PROCEDURE__METHOD_CODE:
                return getMethodCode();
            case V3Package.POCDMT000040_PROCEDURE__APPROACH_SITE_CODE:
                return getApproachSiteCode();
            case V3Package.POCDMT000040_PROCEDURE__TARGET_SITE_CODE:
                return getTargetSiteCode();
            case V3Package.POCDMT000040_PROCEDURE__SUBJECT:
                return getSubject();
            case V3Package.POCDMT000040_PROCEDURE__SPECIMEN:
                return getSpecimen();
            case V3Package.POCDMT000040_PROCEDURE__PERFORMER:
                return getPerformer();
            case V3Package.POCDMT000040_PROCEDURE__AUTHOR:
                return getAuthor();
            case V3Package.POCDMT000040_PROCEDURE__INFORMANT:
                return getInformant();
            case V3Package.POCDMT000040_PROCEDURE__PARTICIPANT:
                return getParticipant();
            case V3Package.POCDMT000040_PROCEDURE__ENTRY_RELATIONSHIP:
                return getEntryRelationship();
            case V3Package.POCDMT000040_PROCEDURE__REFERENCE:
                return getReference();
            case V3Package.POCDMT000040_PROCEDURE__PRECONDITION:
                return getPrecondition();
            case V3Package.POCDMT000040_PROCEDURE__CLASS_CODE:
                return getClassCode();
            case V3Package.POCDMT000040_PROCEDURE__MOOD_CODE:
                return getMoodCode();
            case V3Package.POCDMT000040_PROCEDURE__NEGATION_IND:
                return isNegationInd() ? Boolean.TRUE : Boolean.FALSE;
            case V3Package.POCDMT000040_PROCEDURE__NULL_FLAVOR:
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
            case V3Package.POCDMT000040_PROCEDURE__REALM_CODE:
                getRealmCode().clear();
                getRealmCode().addAll((Collection<? extends CS1>) newValue);
                return;
            case V3Package.POCDMT000040_PROCEDURE__TYPE_ID:
                setTypeId((POCDMT000040InfrastructureRootTypeId) newValue);
                return;
            case V3Package.POCDMT000040_PROCEDURE__TEMPLATE_ID:
                getTemplateId().clear();
                getTemplateId().addAll((Collection<? extends II>) newValue);
                return;
            case V3Package.POCDMT000040_PROCEDURE__ID:
                getId().clear();
                getId().addAll((Collection<? extends II>) newValue);
                return;
            case V3Package.POCDMT000040_PROCEDURE__CODE:
                setCode((CD) newValue);
                return;
            case V3Package.POCDMT000040_PROCEDURE__TEXT:
                setText((ED) newValue);
                return;
            case V3Package.POCDMT000040_PROCEDURE__STATUS_CODE:
                setStatusCode((CS1) newValue);
                return;
            case V3Package.POCDMT000040_PROCEDURE__EFFECTIVE_TIME:
                setEffectiveTime((IVLTS) newValue);
                return;
            case V3Package.POCDMT000040_PROCEDURE__PRIORITY_CODE:
                setPriorityCode((CE) newValue);
                return;
            case V3Package.POCDMT000040_PROCEDURE__LANGUAGE_CODE:
                setLanguageCode((CS1) newValue);
                return;
            case V3Package.POCDMT000040_PROCEDURE__METHOD_CODE:
                getMethodCode().clear();
                getMethodCode().addAll((Collection<? extends CE>) newValue);
                return;
            case V3Package.POCDMT000040_PROCEDURE__APPROACH_SITE_CODE:
                getApproachSiteCode().clear();
                getApproachSiteCode().addAll((Collection<? extends CD>) newValue);
                return;
            case V3Package.POCDMT000040_PROCEDURE__TARGET_SITE_CODE:
                getTargetSiteCode().clear();
                getTargetSiteCode().addAll((Collection<? extends CD>) newValue);
                return;
            case V3Package.POCDMT000040_PROCEDURE__SUBJECT:
                setSubject((POCDMT000040Subject) newValue);
                return;
            case V3Package.POCDMT000040_PROCEDURE__SPECIMEN:
                getSpecimen().clear();
                getSpecimen().addAll((Collection<? extends POCDMT000040Specimen>) newValue);
                return;
            case V3Package.POCDMT000040_PROCEDURE__PERFORMER:
                getPerformer().clear();
                getPerformer().addAll((Collection<? extends POCDMT000040Performer2>) newValue);
                return;
            case V3Package.POCDMT000040_PROCEDURE__AUTHOR:
                getAuthor().clear();
                getAuthor().addAll((Collection<? extends POCDMT000040Author>) newValue);
                return;
            case V3Package.POCDMT000040_PROCEDURE__INFORMANT:
                getInformant().clear();
                getInformant().addAll((Collection<? extends POCDMT000040Informant12>) newValue);
                return;
            case V3Package.POCDMT000040_PROCEDURE__PARTICIPANT:
                getParticipant().clear();
                getParticipant().addAll((Collection<? extends POCDMT000040Participant2>) newValue);
                return;
            case V3Package.POCDMT000040_PROCEDURE__ENTRY_RELATIONSHIP:
                getEntryRelationship().clear();
                getEntryRelationship().addAll((Collection<? extends POCDMT000040EntryRelationship>) newValue);
                return;
            case V3Package.POCDMT000040_PROCEDURE__REFERENCE:
                getReference().clear();
                getReference().addAll((Collection<? extends POCDMT000040Reference>) newValue);
                return;
            case V3Package.POCDMT000040_PROCEDURE__PRECONDITION:
                getPrecondition().clear();
                getPrecondition().addAll((Collection<? extends POCDMT000040Precondition>) newValue);
                return;
            case V3Package.POCDMT000040_PROCEDURE__CLASS_CODE:
                setClassCode((Enumerator) newValue);
                return;
            case V3Package.POCDMT000040_PROCEDURE__MOOD_CODE:
                setMoodCode((XDocumentProcedureMood) newValue);
                return;
            case V3Package.POCDMT000040_PROCEDURE__NEGATION_IND:
                setNegationInd(((Boolean) newValue).booleanValue());
                return;
            case V3Package.POCDMT000040_PROCEDURE__NULL_FLAVOR:
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
            case V3Package.POCDMT000040_PROCEDURE__REALM_CODE:
                getRealmCode().clear();
                return;
            case V3Package.POCDMT000040_PROCEDURE__TYPE_ID:
                setTypeId((POCDMT000040InfrastructureRootTypeId) null);
                return;
            case V3Package.POCDMT000040_PROCEDURE__TEMPLATE_ID:
                getTemplateId().clear();
                return;
            case V3Package.POCDMT000040_PROCEDURE__ID:
                getId().clear();
                return;
            case V3Package.POCDMT000040_PROCEDURE__CODE:
                setCode((CD) null);
                return;
            case V3Package.POCDMT000040_PROCEDURE__TEXT:
                setText((ED) null);
                return;
            case V3Package.POCDMT000040_PROCEDURE__STATUS_CODE:
                setStatusCode((CS1) null);
                return;
            case V3Package.POCDMT000040_PROCEDURE__EFFECTIVE_TIME:
                setEffectiveTime((IVLTS) null);
                return;
            case V3Package.POCDMT000040_PROCEDURE__PRIORITY_CODE:
                setPriorityCode((CE) null);
                return;
            case V3Package.POCDMT000040_PROCEDURE__LANGUAGE_CODE:
                setLanguageCode((CS1) null);
                return;
            case V3Package.POCDMT000040_PROCEDURE__METHOD_CODE:
                getMethodCode().clear();
                return;
            case V3Package.POCDMT000040_PROCEDURE__APPROACH_SITE_CODE:
                getApproachSiteCode().clear();
                return;
            case V3Package.POCDMT000040_PROCEDURE__TARGET_SITE_CODE:
                getTargetSiteCode().clear();
                return;
            case V3Package.POCDMT000040_PROCEDURE__SUBJECT:
                setSubject((POCDMT000040Subject) null);
                return;
            case V3Package.POCDMT000040_PROCEDURE__SPECIMEN:
                getSpecimen().clear();
                return;
            case V3Package.POCDMT000040_PROCEDURE__PERFORMER:
                getPerformer().clear();
                return;
            case V3Package.POCDMT000040_PROCEDURE__AUTHOR:
                getAuthor().clear();
                return;
            case V3Package.POCDMT000040_PROCEDURE__INFORMANT:
                getInformant().clear();
                return;
            case V3Package.POCDMT000040_PROCEDURE__PARTICIPANT:
                getParticipant().clear();
                return;
            case V3Package.POCDMT000040_PROCEDURE__ENTRY_RELATIONSHIP:
                getEntryRelationship().clear();
                return;
            case V3Package.POCDMT000040_PROCEDURE__REFERENCE:
                getReference().clear();
                return;
            case V3Package.POCDMT000040_PROCEDURE__PRECONDITION:
                getPrecondition().clear();
                return;
            case V3Package.POCDMT000040_PROCEDURE__CLASS_CODE:
                setClassCode(CLASS_CODE_EDEFAULT);
                return;
            case V3Package.POCDMT000040_PROCEDURE__MOOD_CODE:
                unsetMoodCode();
                return;
            case V3Package.POCDMT000040_PROCEDURE__NEGATION_IND:
                unsetNegationInd();
                return;
            case V3Package.POCDMT000040_PROCEDURE__NULL_FLAVOR:
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
            case V3Package.POCDMT000040_PROCEDURE__REALM_CODE:
                return realmCode != null && !realmCode.isEmpty();
            case V3Package.POCDMT000040_PROCEDURE__TYPE_ID:
                return typeId != null;
            case V3Package.POCDMT000040_PROCEDURE__TEMPLATE_ID:
                return templateId != null && !templateId.isEmpty();
            case V3Package.POCDMT000040_PROCEDURE__ID:
                return id != null && !id.isEmpty();
            case V3Package.POCDMT000040_PROCEDURE__CODE:
                return code != null;
            case V3Package.POCDMT000040_PROCEDURE__TEXT:
                return text != null;
            case V3Package.POCDMT000040_PROCEDURE__STATUS_CODE:
                return statusCode != null;
            case V3Package.POCDMT000040_PROCEDURE__EFFECTIVE_TIME:
                return effectiveTime != null;
            case V3Package.POCDMT000040_PROCEDURE__PRIORITY_CODE:
                return priorityCode != null;
            case V3Package.POCDMT000040_PROCEDURE__LANGUAGE_CODE:
                return languageCode != null;
            case V3Package.POCDMT000040_PROCEDURE__METHOD_CODE:
                return methodCode != null && !methodCode.isEmpty();
            case V3Package.POCDMT000040_PROCEDURE__APPROACH_SITE_CODE:
                return approachSiteCode != null && !approachSiteCode.isEmpty();
            case V3Package.POCDMT000040_PROCEDURE__TARGET_SITE_CODE:
                return targetSiteCode != null && !targetSiteCode.isEmpty();
            case V3Package.POCDMT000040_PROCEDURE__SUBJECT:
                return subject != null;
            case V3Package.POCDMT000040_PROCEDURE__SPECIMEN:
                return specimen != null && !specimen.isEmpty();
            case V3Package.POCDMT000040_PROCEDURE__PERFORMER:
                return performer != null && !performer.isEmpty();
            case V3Package.POCDMT000040_PROCEDURE__AUTHOR:
                return author != null && !author.isEmpty();
            case V3Package.POCDMT000040_PROCEDURE__INFORMANT:
                return informant != null && !informant.isEmpty();
            case V3Package.POCDMT000040_PROCEDURE__PARTICIPANT:
                return participant != null && !participant.isEmpty();
            case V3Package.POCDMT000040_PROCEDURE__ENTRY_RELATIONSHIP:
                return entryRelationship != null && !entryRelationship.isEmpty();
            case V3Package.POCDMT000040_PROCEDURE__REFERENCE:
                return reference != null && !reference.isEmpty();
            case V3Package.POCDMT000040_PROCEDURE__PRECONDITION:
                return precondition != null && !precondition.isEmpty();
            case V3Package.POCDMT000040_PROCEDURE__CLASS_CODE:
                return CLASS_CODE_EDEFAULT == null ? classCode != null : !CLASS_CODE_EDEFAULT.equals(classCode);
            case V3Package.POCDMT000040_PROCEDURE__MOOD_CODE:
                return isSetMoodCode();
            case V3Package.POCDMT000040_PROCEDURE__NEGATION_IND:
                return isSetNegationInd();
            case V3Package.POCDMT000040_PROCEDURE__NULL_FLAVOR:
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
        result.append(classCode);
        result.append(", moodCode: ");
        if (moodCodeESet) result.append(moodCode); else result.append("<unset>");
        result.append(", negationInd: ");
        if (negationIndESet) result.append(negationInd); else result.append("<unset>");
        result.append(", nullFlavor: ");
        result.append(nullFlavor);
        result.append(')');
        return result.toString();
    }
}
