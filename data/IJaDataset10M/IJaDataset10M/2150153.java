package org.contract.ist.schema.ist.contract.impl;

import org.contract.ist.schema.ist.contract.ActionDescriptors;
import org.contract.ist.schema.ist.contract.ContractPackage;
import org.contract.ist.schema.ist.contract.DomainOntology;
import org.contract.ist.schema.ist.contract.KnowledgeBase;
import org.contract.ist.schema.ist.contract.ObserverPredicateList;
import org.contract.ist.schema.ist.contract.PolicyManagement;
import org.contract.ist.schema.ist.contract.VariableList;
import org.contract.ist.schema.ist.contract.WorldModel;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>World Model</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.contract.ist.schema.ist.contract.impl.WorldModelImpl#getSystemClock <em>System Clock</em>}</li>
 *   <li>{@link org.contract.ist.schema.ist.contract.impl.WorldModelImpl#getDomainOntology <em>Domain Ontology</em>}</li>
 *   <li>{@link org.contract.ist.schema.ist.contract.impl.WorldModelImpl#getVariableList <em>Variable List</em>}</li>
 *   <li>{@link org.contract.ist.schema.ist.contract.impl.WorldModelImpl#getObserverPredicateList <em>Observer Predicate List</em>}</li>
 *   <li>{@link org.contract.ist.schema.ist.contract.impl.WorldModelImpl#getActionDescriptors <em>Action Descriptors</em>}</li>
 *   <li>{@link org.contract.ist.schema.ist.contract.impl.WorldModelImpl#getKnowledgeBase <em>Knowledge Base</em>}</li>
 *   <li>{@link org.contract.ist.schema.ist.contract.impl.WorldModelImpl#getPolicyManagement <em>Policy Management</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class WorldModelImpl extends EObjectImpl implements WorldModel {

    /**
	 * The default value of the '{@link #getSystemClock() <em>System Clock</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSystemClock()
	 * @generated
	 * @ordered
	 */
    protected static final String SYSTEM_CLOCK_EDEFAULT = null;

    /**
	 * The cached value of the '{@link #getSystemClock() <em>System Clock</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSystemClock()
	 * @generated
	 * @ordered
	 */
    protected String systemClock = SYSTEM_CLOCK_EDEFAULT;

    /**
	 * The cached value of the '{@link #getDomainOntology() <em>Domain Ontology</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDomainOntology()
	 * @generated
	 * @ordered
	 */
    protected DomainOntology domainOntology;

    /**
	 * The cached value of the '{@link #getVariableList() <em>Variable List</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getVariableList()
	 * @generated
	 * @ordered
	 */
    protected VariableList variableList;

    /**
	 * This is true if the Variable List containment reference has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    protected boolean variableListESet;

    /**
	 * The cached value of the '{@link #getObserverPredicateList() <em>Observer Predicate List</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getObserverPredicateList()
	 * @generated
	 * @ordered
	 */
    protected ObserverPredicateList observerPredicateList;

    /**
	 * The cached value of the '{@link #getActionDescriptors() <em>Action Descriptors</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getActionDescriptors()
	 * @generated
	 * @ordered
	 */
    protected ActionDescriptors actionDescriptors;

    /**
	 * This is true if the Action Descriptors containment reference has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    protected boolean actionDescriptorsESet;

    /**
	 * The cached value of the '{@link #getKnowledgeBase() <em>Knowledge Base</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getKnowledgeBase()
	 * @generated
	 * @ordered
	 */
    protected KnowledgeBase knowledgeBase;

    /**
	 * This is true if the Knowledge Base containment reference has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    protected boolean knowledgeBaseESet;

    /**
	 * The cached value of the '{@link #getPolicyManagement() <em>Policy Management</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPolicyManagement()
	 * @generated
	 * @ordered
	 */
    protected PolicyManagement policyManagement;

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    protected WorldModelImpl() {
        super();
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    protected EClass eStaticClass() {
        return ContractPackage.Literals.WORLD_MODEL;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public String getSystemClock() {
        return systemClock;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setSystemClock(String newSystemClock) {
        String oldSystemClock = systemClock;
        systemClock = newSystemClock;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, ContractPackage.WORLD_MODEL__SYSTEM_CLOCK, oldSystemClock, systemClock));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public DomainOntology getDomainOntology() {
        return domainOntology;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public NotificationChain basicSetDomainOntology(DomainOntology newDomainOntology, NotificationChain msgs) {
        DomainOntology oldDomainOntology = domainOntology;
        domainOntology = newDomainOntology;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, ContractPackage.WORLD_MODEL__DOMAIN_ONTOLOGY, oldDomainOntology, newDomainOntology);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setDomainOntology(DomainOntology newDomainOntology) {
        if (newDomainOntology != domainOntology) {
            NotificationChain msgs = null;
            if (domainOntology != null) msgs = ((InternalEObject) domainOntology).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - ContractPackage.WORLD_MODEL__DOMAIN_ONTOLOGY, null, msgs);
            if (newDomainOntology != null) msgs = ((InternalEObject) newDomainOntology).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - ContractPackage.WORLD_MODEL__DOMAIN_ONTOLOGY, null, msgs);
            msgs = basicSetDomainOntology(newDomainOntology, msgs);
            if (msgs != null) msgs.dispatch();
        } else if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, ContractPackage.WORLD_MODEL__DOMAIN_ONTOLOGY, newDomainOntology, newDomainOntology));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public VariableList getVariableList() {
        return variableList;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public NotificationChain basicSetVariableList(VariableList newVariableList, NotificationChain msgs) {
        VariableList oldVariableList = variableList;
        variableList = newVariableList;
        boolean oldVariableListESet = variableListESet;
        variableListESet = true;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, ContractPackage.WORLD_MODEL__VARIABLE_LIST, oldVariableList, newVariableList, !oldVariableListESet);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setVariableList(VariableList newVariableList) {
        if (newVariableList != variableList) {
            NotificationChain msgs = null;
            if (variableList != null) msgs = ((InternalEObject) variableList).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - ContractPackage.WORLD_MODEL__VARIABLE_LIST, null, msgs);
            if (newVariableList != null) msgs = ((InternalEObject) newVariableList).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - ContractPackage.WORLD_MODEL__VARIABLE_LIST, null, msgs);
            msgs = basicSetVariableList(newVariableList, msgs);
            if (msgs != null) msgs.dispatch();
        } else {
            boolean oldVariableListESet = variableListESet;
            variableListESet = true;
            if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, ContractPackage.WORLD_MODEL__VARIABLE_LIST, newVariableList, newVariableList, !oldVariableListESet));
        }
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public NotificationChain basicUnsetVariableList(NotificationChain msgs) {
        VariableList oldVariableList = variableList;
        variableList = null;
        boolean oldVariableListESet = variableListESet;
        variableListESet = false;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.UNSET, ContractPackage.WORLD_MODEL__VARIABLE_LIST, oldVariableList, null, oldVariableListESet);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void unsetVariableList() {
        if (variableList != null) {
            NotificationChain msgs = null;
            msgs = ((InternalEObject) variableList).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - ContractPackage.WORLD_MODEL__VARIABLE_LIST, null, msgs);
            msgs = basicUnsetVariableList(msgs);
            if (msgs != null) msgs.dispatch();
        } else {
            boolean oldVariableListESet = variableListESet;
            variableListESet = false;
            if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.UNSET, ContractPackage.WORLD_MODEL__VARIABLE_LIST, null, null, oldVariableListESet));
        }
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public boolean isSetVariableList() {
        return variableListESet;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public ObserverPredicateList getObserverPredicateList() {
        return observerPredicateList;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public NotificationChain basicSetObserverPredicateList(ObserverPredicateList newObserverPredicateList, NotificationChain msgs) {
        ObserverPredicateList oldObserverPredicateList = observerPredicateList;
        observerPredicateList = newObserverPredicateList;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, ContractPackage.WORLD_MODEL__OBSERVER_PREDICATE_LIST, oldObserverPredicateList, newObserverPredicateList);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setObserverPredicateList(ObserverPredicateList newObserverPredicateList) {
        if (newObserverPredicateList != observerPredicateList) {
            NotificationChain msgs = null;
            if (observerPredicateList != null) msgs = ((InternalEObject) observerPredicateList).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - ContractPackage.WORLD_MODEL__OBSERVER_PREDICATE_LIST, null, msgs);
            if (newObserverPredicateList != null) msgs = ((InternalEObject) newObserverPredicateList).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - ContractPackage.WORLD_MODEL__OBSERVER_PREDICATE_LIST, null, msgs);
            msgs = basicSetObserverPredicateList(newObserverPredicateList, msgs);
            if (msgs != null) msgs.dispatch();
        } else if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, ContractPackage.WORLD_MODEL__OBSERVER_PREDICATE_LIST, newObserverPredicateList, newObserverPredicateList));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public ActionDescriptors getActionDescriptors() {
        return actionDescriptors;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public NotificationChain basicSetActionDescriptors(ActionDescriptors newActionDescriptors, NotificationChain msgs) {
        ActionDescriptors oldActionDescriptors = actionDescriptors;
        actionDescriptors = newActionDescriptors;
        boolean oldActionDescriptorsESet = actionDescriptorsESet;
        actionDescriptorsESet = true;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, ContractPackage.WORLD_MODEL__ACTION_DESCRIPTORS, oldActionDescriptors, newActionDescriptors, !oldActionDescriptorsESet);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setActionDescriptors(ActionDescriptors newActionDescriptors) {
        if (newActionDescriptors != actionDescriptors) {
            NotificationChain msgs = null;
            if (actionDescriptors != null) msgs = ((InternalEObject) actionDescriptors).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - ContractPackage.WORLD_MODEL__ACTION_DESCRIPTORS, null, msgs);
            if (newActionDescriptors != null) msgs = ((InternalEObject) newActionDescriptors).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - ContractPackage.WORLD_MODEL__ACTION_DESCRIPTORS, null, msgs);
            msgs = basicSetActionDescriptors(newActionDescriptors, msgs);
            if (msgs != null) msgs.dispatch();
        } else {
            boolean oldActionDescriptorsESet = actionDescriptorsESet;
            actionDescriptorsESet = true;
            if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, ContractPackage.WORLD_MODEL__ACTION_DESCRIPTORS, newActionDescriptors, newActionDescriptors, !oldActionDescriptorsESet));
        }
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public NotificationChain basicUnsetActionDescriptors(NotificationChain msgs) {
        ActionDescriptors oldActionDescriptors = actionDescriptors;
        actionDescriptors = null;
        boolean oldActionDescriptorsESet = actionDescriptorsESet;
        actionDescriptorsESet = false;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.UNSET, ContractPackage.WORLD_MODEL__ACTION_DESCRIPTORS, oldActionDescriptors, null, oldActionDescriptorsESet);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void unsetActionDescriptors() {
        if (actionDescriptors != null) {
            NotificationChain msgs = null;
            msgs = ((InternalEObject) actionDescriptors).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - ContractPackage.WORLD_MODEL__ACTION_DESCRIPTORS, null, msgs);
            msgs = basicUnsetActionDescriptors(msgs);
            if (msgs != null) msgs.dispatch();
        } else {
            boolean oldActionDescriptorsESet = actionDescriptorsESet;
            actionDescriptorsESet = false;
            if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.UNSET, ContractPackage.WORLD_MODEL__ACTION_DESCRIPTORS, null, null, oldActionDescriptorsESet));
        }
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public boolean isSetActionDescriptors() {
        return actionDescriptorsESet;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public KnowledgeBase getKnowledgeBase() {
        return knowledgeBase;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public NotificationChain basicSetKnowledgeBase(KnowledgeBase newKnowledgeBase, NotificationChain msgs) {
        KnowledgeBase oldKnowledgeBase = knowledgeBase;
        knowledgeBase = newKnowledgeBase;
        boolean oldKnowledgeBaseESet = knowledgeBaseESet;
        knowledgeBaseESet = true;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, ContractPackage.WORLD_MODEL__KNOWLEDGE_BASE, oldKnowledgeBase, newKnowledgeBase, !oldKnowledgeBaseESet);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setKnowledgeBase(KnowledgeBase newKnowledgeBase) {
        if (newKnowledgeBase != knowledgeBase) {
            NotificationChain msgs = null;
            if (knowledgeBase != null) msgs = ((InternalEObject) knowledgeBase).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - ContractPackage.WORLD_MODEL__KNOWLEDGE_BASE, null, msgs);
            if (newKnowledgeBase != null) msgs = ((InternalEObject) newKnowledgeBase).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - ContractPackage.WORLD_MODEL__KNOWLEDGE_BASE, null, msgs);
            msgs = basicSetKnowledgeBase(newKnowledgeBase, msgs);
            if (msgs != null) msgs.dispatch();
        } else {
            boolean oldKnowledgeBaseESet = knowledgeBaseESet;
            knowledgeBaseESet = true;
            if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, ContractPackage.WORLD_MODEL__KNOWLEDGE_BASE, newKnowledgeBase, newKnowledgeBase, !oldKnowledgeBaseESet));
        }
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public NotificationChain basicUnsetKnowledgeBase(NotificationChain msgs) {
        KnowledgeBase oldKnowledgeBase = knowledgeBase;
        knowledgeBase = null;
        boolean oldKnowledgeBaseESet = knowledgeBaseESet;
        knowledgeBaseESet = false;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.UNSET, ContractPackage.WORLD_MODEL__KNOWLEDGE_BASE, oldKnowledgeBase, null, oldKnowledgeBaseESet);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void unsetKnowledgeBase() {
        if (knowledgeBase != null) {
            NotificationChain msgs = null;
            msgs = ((InternalEObject) knowledgeBase).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - ContractPackage.WORLD_MODEL__KNOWLEDGE_BASE, null, msgs);
            msgs = basicUnsetKnowledgeBase(msgs);
            if (msgs != null) msgs.dispatch();
        } else {
            boolean oldKnowledgeBaseESet = knowledgeBaseESet;
            knowledgeBaseESet = false;
            if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.UNSET, ContractPackage.WORLD_MODEL__KNOWLEDGE_BASE, null, null, oldKnowledgeBaseESet));
        }
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public boolean isSetKnowledgeBase() {
        return knowledgeBaseESet;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public PolicyManagement getPolicyManagement() {
        return policyManagement;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public NotificationChain basicSetPolicyManagement(PolicyManagement newPolicyManagement, NotificationChain msgs) {
        PolicyManagement oldPolicyManagement = policyManagement;
        policyManagement = newPolicyManagement;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, ContractPackage.WORLD_MODEL__POLICY_MANAGEMENT, oldPolicyManagement, newPolicyManagement);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setPolicyManagement(PolicyManagement newPolicyManagement) {
        if (newPolicyManagement != policyManagement) {
            NotificationChain msgs = null;
            if (policyManagement != null) msgs = ((InternalEObject) policyManagement).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - ContractPackage.WORLD_MODEL__POLICY_MANAGEMENT, null, msgs);
            if (newPolicyManagement != null) msgs = ((InternalEObject) newPolicyManagement).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - ContractPackage.WORLD_MODEL__POLICY_MANAGEMENT, null, msgs);
            msgs = basicSetPolicyManagement(newPolicyManagement, msgs);
            if (msgs != null) msgs.dispatch();
        } else if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, ContractPackage.WORLD_MODEL__POLICY_MANAGEMENT, newPolicyManagement, newPolicyManagement));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch(featureID) {
            case ContractPackage.WORLD_MODEL__DOMAIN_ONTOLOGY:
                return basicSetDomainOntology(null, msgs);
            case ContractPackage.WORLD_MODEL__VARIABLE_LIST:
                return basicUnsetVariableList(msgs);
            case ContractPackage.WORLD_MODEL__OBSERVER_PREDICATE_LIST:
                return basicSetObserverPredicateList(null, msgs);
            case ContractPackage.WORLD_MODEL__ACTION_DESCRIPTORS:
                return basicUnsetActionDescriptors(msgs);
            case ContractPackage.WORLD_MODEL__KNOWLEDGE_BASE:
                return basicUnsetKnowledgeBase(msgs);
            case ContractPackage.WORLD_MODEL__POLICY_MANAGEMENT:
                return basicSetPolicyManagement(null, msgs);
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
            case ContractPackage.WORLD_MODEL__SYSTEM_CLOCK:
                return getSystemClock();
            case ContractPackage.WORLD_MODEL__DOMAIN_ONTOLOGY:
                return getDomainOntology();
            case ContractPackage.WORLD_MODEL__VARIABLE_LIST:
                return getVariableList();
            case ContractPackage.WORLD_MODEL__OBSERVER_PREDICATE_LIST:
                return getObserverPredicateList();
            case ContractPackage.WORLD_MODEL__ACTION_DESCRIPTORS:
                return getActionDescriptors();
            case ContractPackage.WORLD_MODEL__KNOWLEDGE_BASE:
                return getKnowledgeBase();
            case ContractPackage.WORLD_MODEL__POLICY_MANAGEMENT:
                return getPolicyManagement();
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
            case ContractPackage.WORLD_MODEL__SYSTEM_CLOCK:
                setSystemClock((String) newValue);
                return;
            case ContractPackage.WORLD_MODEL__DOMAIN_ONTOLOGY:
                setDomainOntology((DomainOntology) newValue);
                return;
            case ContractPackage.WORLD_MODEL__VARIABLE_LIST:
                setVariableList((VariableList) newValue);
                return;
            case ContractPackage.WORLD_MODEL__OBSERVER_PREDICATE_LIST:
                setObserverPredicateList((ObserverPredicateList) newValue);
                return;
            case ContractPackage.WORLD_MODEL__ACTION_DESCRIPTORS:
                setActionDescriptors((ActionDescriptors) newValue);
                return;
            case ContractPackage.WORLD_MODEL__KNOWLEDGE_BASE:
                setKnowledgeBase((KnowledgeBase) newValue);
                return;
            case ContractPackage.WORLD_MODEL__POLICY_MANAGEMENT:
                setPolicyManagement((PolicyManagement) newValue);
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
            case ContractPackage.WORLD_MODEL__SYSTEM_CLOCK:
                setSystemClock(SYSTEM_CLOCK_EDEFAULT);
                return;
            case ContractPackage.WORLD_MODEL__DOMAIN_ONTOLOGY:
                setDomainOntology((DomainOntology) null);
                return;
            case ContractPackage.WORLD_MODEL__VARIABLE_LIST:
                unsetVariableList();
                return;
            case ContractPackage.WORLD_MODEL__OBSERVER_PREDICATE_LIST:
                setObserverPredicateList((ObserverPredicateList) null);
                return;
            case ContractPackage.WORLD_MODEL__ACTION_DESCRIPTORS:
                unsetActionDescriptors();
                return;
            case ContractPackage.WORLD_MODEL__KNOWLEDGE_BASE:
                unsetKnowledgeBase();
                return;
            case ContractPackage.WORLD_MODEL__POLICY_MANAGEMENT:
                setPolicyManagement((PolicyManagement) null);
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
            case ContractPackage.WORLD_MODEL__SYSTEM_CLOCK:
                return SYSTEM_CLOCK_EDEFAULT == null ? systemClock != null : !SYSTEM_CLOCK_EDEFAULT.equals(systemClock);
            case ContractPackage.WORLD_MODEL__DOMAIN_ONTOLOGY:
                return domainOntology != null;
            case ContractPackage.WORLD_MODEL__VARIABLE_LIST:
                return isSetVariableList();
            case ContractPackage.WORLD_MODEL__OBSERVER_PREDICATE_LIST:
                return observerPredicateList != null;
            case ContractPackage.WORLD_MODEL__ACTION_DESCRIPTORS:
                return isSetActionDescriptors();
            case ContractPackage.WORLD_MODEL__KNOWLEDGE_BASE:
                return isSetKnowledgeBase();
            case ContractPackage.WORLD_MODEL__POLICY_MANAGEMENT:
                return policyManagement != null;
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
        result.append(" (systemClock: ");
        result.append(systemClock);
        result.append(')');
        return result.toString();
    }
}
