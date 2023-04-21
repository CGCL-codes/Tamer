package eu.medeia.ecore.dm.impl;

import eu.medeia.ecore.bm.AdditionalModelInformation;
import eu.medeia.ecore.dm.DiagnosticsBehaviourDescriptionMethod;
import eu.medeia.ecore.dm.DmPackage;
import eu.medeia.ecore.dm.PetriNet;
import eu.medeia.ecore.dm.Rule;
import java.util.Collection;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Diagnostics Behaviour Description Method</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link eu.medeia.ecore.dm.impl.DiagnosticsBehaviourDescriptionMethodImpl#getID <em>ID</em>}</li>
 *   <li>{@link eu.medeia.ecore.dm.impl.DiagnosticsBehaviourDescriptionMethodImpl#getName <em>Name</em>}</li>
 *   <li>{@link eu.medeia.ecore.dm.impl.DiagnosticsBehaviourDescriptionMethodImpl#getComment <em>Comment</em>}</li>
 *   <li>{@link eu.medeia.ecore.dm.impl.DiagnosticsBehaviourDescriptionMethodImpl#getAdditionalInformation <em>Additional Information</em>}</li>
 *   <li>{@link eu.medeia.ecore.dm.impl.DiagnosticsBehaviourDescriptionMethodImpl#getPetriNets <em>Petri Nets</em>}</li>
 *   <li>{@link eu.medeia.ecore.dm.impl.DiagnosticsBehaviourDescriptionMethodImpl#getRules <em>Rules</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class DiagnosticsBehaviourDescriptionMethodImpl extends EObjectImpl implements DiagnosticsBehaviourDescriptionMethod {

    /**
	 * The default value of the '{@link #getID() <em>ID</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getID()
	 * @generated
	 * @ordered
	 */
    protected static final String ID_EDEFAULT = null;

    /**
	 * The cached value of the '{@link #getID() <em>ID</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getID()
	 * @generated
	 * @ordered
	 */
    protected String id = ID_EDEFAULT;

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
	 * The default value of the '{@link #getComment() <em>Comment</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getComment()
	 * @generated
	 * @ordered
	 */
    protected static final String COMMENT_EDEFAULT = null;

    /**
	 * The cached value of the '{@link #getComment() <em>Comment</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getComment()
	 * @generated
	 * @ordered
	 */
    protected String comment = COMMENT_EDEFAULT;

    /**
	 * The cached value of the '{@link #getAdditionalInformation() <em>Additional Information</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getAdditionalInformation()
	 * @generated
	 * @ordered
	 */
    protected EList<AdditionalModelInformation> additionalInformation;

    /**
	 * The cached value of the '{@link #getPetriNets() <em>Petri Nets</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPetriNets()
	 * @generated
	 * @ordered
	 */
    protected EList<PetriNet> petriNets;

    /**
	 * The cached value of the '{@link #getRules() <em>Rules</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getRules()
	 * @generated
	 * @ordered
	 */
    protected EList<Rule> rules;

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    protected DiagnosticsBehaviourDescriptionMethodImpl() {
        super();
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    protected EClass eStaticClass() {
        return DmPackage.Literals.DIAGNOSTICS_BEHAVIOUR_DESCRIPTION_METHOD;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public String getID() {
        return id;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setID(String newID) {
        String oldID = id;
        id = newID;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, DmPackage.DIAGNOSTICS_BEHAVIOUR_DESCRIPTION_METHOD__ID, oldID, id));
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
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, DmPackage.DIAGNOSTICS_BEHAVIOUR_DESCRIPTION_METHOD__NAME, oldName, name));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public String getComment() {
        return comment;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setComment(String newComment) {
        String oldComment = comment;
        comment = newComment;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, DmPackage.DIAGNOSTICS_BEHAVIOUR_DESCRIPTION_METHOD__COMMENT, oldComment, comment));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EList<AdditionalModelInformation> getAdditionalInformation() {
        if (additionalInformation == null) {
            additionalInformation = new EObjectContainmentEList<AdditionalModelInformation>(AdditionalModelInformation.class, this, DmPackage.DIAGNOSTICS_BEHAVIOUR_DESCRIPTION_METHOD__ADDITIONAL_INFORMATION);
        }
        return additionalInformation;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EList<PetriNet> getPetriNets() {
        if (petriNets == null) {
            petriNets = new EObjectContainmentEList<PetriNet>(PetriNet.class, this, DmPackage.DIAGNOSTICS_BEHAVIOUR_DESCRIPTION_METHOD__PETRI_NETS);
        }
        return petriNets;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EList<Rule> getRules() {
        if (rules == null) {
            rules = new EObjectContainmentEList<Rule>(Rule.class, this, DmPackage.DIAGNOSTICS_BEHAVIOUR_DESCRIPTION_METHOD__RULES);
        }
        return rules;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch(featureID) {
            case DmPackage.DIAGNOSTICS_BEHAVIOUR_DESCRIPTION_METHOD__ADDITIONAL_INFORMATION:
                return ((InternalEList<?>) getAdditionalInformation()).basicRemove(otherEnd, msgs);
            case DmPackage.DIAGNOSTICS_BEHAVIOUR_DESCRIPTION_METHOD__PETRI_NETS:
                return ((InternalEList<?>) getPetriNets()).basicRemove(otherEnd, msgs);
            case DmPackage.DIAGNOSTICS_BEHAVIOUR_DESCRIPTION_METHOD__RULES:
                return ((InternalEList<?>) getRules()).basicRemove(otherEnd, msgs);
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
            case DmPackage.DIAGNOSTICS_BEHAVIOUR_DESCRIPTION_METHOD__ID:
                return getID();
            case DmPackage.DIAGNOSTICS_BEHAVIOUR_DESCRIPTION_METHOD__NAME:
                return getName();
            case DmPackage.DIAGNOSTICS_BEHAVIOUR_DESCRIPTION_METHOD__COMMENT:
                return getComment();
            case DmPackage.DIAGNOSTICS_BEHAVIOUR_DESCRIPTION_METHOD__ADDITIONAL_INFORMATION:
                return getAdditionalInformation();
            case DmPackage.DIAGNOSTICS_BEHAVIOUR_DESCRIPTION_METHOD__PETRI_NETS:
                return getPetriNets();
            case DmPackage.DIAGNOSTICS_BEHAVIOUR_DESCRIPTION_METHOD__RULES:
                return getRules();
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
            case DmPackage.DIAGNOSTICS_BEHAVIOUR_DESCRIPTION_METHOD__ID:
                setID((String) newValue);
                return;
            case DmPackage.DIAGNOSTICS_BEHAVIOUR_DESCRIPTION_METHOD__NAME:
                setName((String) newValue);
                return;
            case DmPackage.DIAGNOSTICS_BEHAVIOUR_DESCRIPTION_METHOD__COMMENT:
                setComment((String) newValue);
                return;
            case DmPackage.DIAGNOSTICS_BEHAVIOUR_DESCRIPTION_METHOD__ADDITIONAL_INFORMATION:
                getAdditionalInformation().clear();
                getAdditionalInformation().addAll((Collection<? extends AdditionalModelInformation>) newValue);
                return;
            case DmPackage.DIAGNOSTICS_BEHAVIOUR_DESCRIPTION_METHOD__PETRI_NETS:
                getPetriNets().clear();
                getPetriNets().addAll((Collection<? extends PetriNet>) newValue);
                return;
            case DmPackage.DIAGNOSTICS_BEHAVIOUR_DESCRIPTION_METHOD__RULES:
                getRules().clear();
                getRules().addAll((Collection<? extends Rule>) newValue);
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
            case DmPackage.DIAGNOSTICS_BEHAVIOUR_DESCRIPTION_METHOD__ID:
                setID(ID_EDEFAULT);
                return;
            case DmPackage.DIAGNOSTICS_BEHAVIOUR_DESCRIPTION_METHOD__NAME:
                setName(NAME_EDEFAULT);
                return;
            case DmPackage.DIAGNOSTICS_BEHAVIOUR_DESCRIPTION_METHOD__COMMENT:
                setComment(COMMENT_EDEFAULT);
                return;
            case DmPackage.DIAGNOSTICS_BEHAVIOUR_DESCRIPTION_METHOD__ADDITIONAL_INFORMATION:
                getAdditionalInformation().clear();
                return;
            case DmPackage.DIAGNOSTICS_BEHAVIOUR_DESCRIPTION_METHOD__PETRI_NETS:
                getPetriNets().clear();
                return;
            case DmPackage.DIAGNOSTICS_BEHAVIOUR_DESCRIPTION_METHOD__RULES:
                getRules().clear();
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
            case DmPackage.DIAGNOSTICS_BEHAVIOUR_DESCRIPTION_METHOD__ID:
                return ID_EDEFAULT == null ? id != null : !ID_EDEFAULT.equals(id);
            case DmPackage.DIAGNOSTICS_BEHAVIOUR_DESCRIPTION_METHOD__NAME:
                return NAME_EDEFAULT == null ? name != null : !NAME_EDEFAULT.equals(name);
            case DmPackage.DIAGNOSTICS_BEHAVIOUR_DESCRIPTION_METHOD__COMMENT:
                return COMMENT_EDEFAULT == null ? comment != null : !COMMENT_EDEFAULT.equals(comment);
            case DmPackage.DIAGNOSTICS_BEHAVIOUR_DESCRIPTION_METHOD__ADDITIONAL_INFORMATION:
                return additionalInformation != null && !additionalInformation.isEmpty();
            case DmPackage.DIAGNOSTICS_BEHAVIOUR_DESCRIPTION_METHOD__PETRI_NETS:
                return petriNets != null && !petriNets.isEmpty();
            case DmPackage.DIAGNOSTICS_BEHAVIOUR_DESCRIPTION_METHOD__RULES:
                return rules != null && !rules.isEmpty();
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
        result.append(" (ID: ");
        result.append(id);
        result.append(", Name: ");
        result.append(name);
        result.append(", Comment: ");
        result.append(comment);
        result.append(')');
        return result.toString();
    }
}
