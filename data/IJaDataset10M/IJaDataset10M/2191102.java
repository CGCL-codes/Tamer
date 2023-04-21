package org.parallelj.mda.controlflow.model.controlflow.impl;

import java.util.Collection;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;
import org.parallelj.mda.controlflow.model.controlflow.CompositeTask;
import org.parallelj.mda.controlflow.model.controlflow.ControlFlowPackage;
import org.parallelj.mda.controlflow.model.controlflow.FinalState;
import org.parallelj.mda.controlflow.model.controlflow.FlowElement;
import org.parallelj.mda.controlflow.model.controlflow.InitialState;
import org.parallelj.mda.controlflow.model.controlflow.Predicate;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Composite Task</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.parallelj.mda.controlflow.model.controlflow.impl.CompositeTaskImpl#getElements <em>Elements</em>}</li>
 *   <li>{@link org.parallelj.mda.controlflow.model.controlflow.impl.CompositeTaskImpl#getFinalState <em>Final State</em>}</li>
 *   <li>{@link org.parallelj.mda.controlflow.model.controlflow.impl.CompositeTaskImpl#getInitialState <em>Initial State</em>}</li>
 *   <li>{@link org.parallelj.mda.controlflow.model.controlflow.impl.CompositeTaskImpl#getPredicates <em>Predicates</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class CompositeTaskImpl extends TaskImpl implements CompositeTask {

    /**
	 * The cached value of the '{@link #getElements() <em>Elements</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getElements()
	 * @generated
	 * @ordered
	 */
    protected EList<FlowElement> elements;

    /**
	 * The cached value of the '{@link #getFinalState() <em>Final State</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getFinalState()
	 * @generated
	 * @ordered
	 */
    protected FinalState finalState;

    /**
	 * The cached value of the '{@link #getInitialState() <em>Initial State</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getInitialState()
	 * @generated
	 * @ordered
	 */
    protected InitialState initialState;

    /**
	 * The cached value of the '{@link #getPredicates() <em>Predicates</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPredicates()
	 * @generated
	 * @ordered
	 */
    protected EList<Predicate> predicates;

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    protected CompositeTaskImpl() {
        super();
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    protected EClass eStaticClass() {
        return ControlFlowPackage.Literals.COMPOSITE_TASK;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EList<FlowElement> getElements() {
        if (elements == null) {
            elements = new EObjectContainmentEList<FlowElement>(FlowElement.class, this, ControlFlowPackage.COMPOSITE_TASK__ELEMENTS);
        }
        return elements;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public FinalState getFinalState() {
        if (finalState != null && finalState.eIsProxy()) {
            InternalEObject oldFinalState = (InternalEObject) finalState;
            finalState = (FinalState) eResolveProxy(oldFinalState);
            if (finalState != oldFinalState) {
                if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.RESOLVE, ControlFlowPackage.COMPOSITE_TASK__FINAL_STATE, oldFinalState, finalState));
            }
        }
        return finalState;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public FinalState basicGetFinalState() {
        return finalState;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setFinalState(FinalState newFinalState) {
        FinalState oldFinalState = finalState;
        finalState = newFinalState;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, ControlFlowPackage.COMPOSITE_TASK__FINAL_STATE, oldFinalState, finalState));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public InitialState getInitialState() {
        if (initialState != null && initialState.eIsProxy()) {
            InternalEObject oldInitialState = (InternalEObject) initialState;
            initialState = (InitialState) eResolveProxy(oldInitialState);
            if (initialState != oldInitialState) {
                if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.RESOLVE, ControlFlowPackage.COMPOSITE_TASK__INITIAL_STATE, oldInitialState, initialState));
            }
        }
        return initialState;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public InitialState basicGetInitialState() {
        return initialState;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setInitialState(InitialState newInitialState) {
        InitialState oldInitialState = initialState;
        initialState = newInitialState;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, ControlFlowPackage.COMPOSITE_TASK__INITIAL_STATE, oldInitialState, initialState));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EList<Predicate> getPredicates() {
        if (predicates == null) {
            predicates = new EObjectContainmentEList<Predicate>(Predicate.class, this, ControlFlowPackage.COMPOSITE_TASK__PREDICATES);
        }
        return predicates;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch(featureID) {
            case ControlFlowPackage.COMPOSITE_TASK__ELEMENTS:
                return ((InternalEList<?>) getElements()).basicRemove(otherEnd, msgs);
            case ControlFlowPackage.COMPOSITE_TASK__PREDICATES:
                return ((InternalEList<?>) getPredicates()).basicRemove(otherEnd, msgs);
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
            case ControlFlowPackage.COMPOSITE_TASK__ELEMENTS:
                return getElements();
            case ControlFlowPackage.COMPOSITE_TASK__FINAL_STATE:
                if (resolve) return getFinalState();
                return basicGetFinalState();
            case ControlFlowPackage.COMPOSITE_TASK__INITIAL_STATE:
                if (resolve) return getInitialState();
                return basicGetInitialState();
            case ControlFlowPackage.COMPOSITE_TASK__PREDICATES:
                return getPredicates();
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
            case ControlFlowPackage.COMPOSITE_TASK__ELEMENTS:
                getElements().clear();
                getElements().addAll((Collection<? extends FlowElement>) newValue);
                return;
            case ControlFlowPackage.COMPOSITE_TASK__FINAL_STATE:
                setFinalState((FinalState) newValue);
                return;
            case ControlFlowPackage.COMPOSITE_TASK__INITIAL_STATE:
                setInitialState((InitialState) newValue);
                return;
            case ControlFlowPackage.COMPOSITE_TASK__PREDICATES:
                getPredicates().clear();
                getPredicates().addAll((Collection<? extends Predicate>) newValue);
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
            case ControlFlowPackage.COMPOSITE_TASK__ELEMENTS:
                getElements().clear();
                return;
            case ControlFlowPackage.COMPOSITE_TASK__FINAL_STATE:
                setFinalState((FinalState) null);
                return;
            case ControlFlowPackage.COMPOSITE_TASK__INITIAL_STATE:
                setInitialState((InitialState) null);
                return;
            case ControlFlowPackage.COMPOSITE_TASK__PREDICATES:
                getPredicates().clear();
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
            case ControlFlowPackage.COMPOSITE_TASK__ELEMENTS:
                return elements != null && !elements.isEmpty();
            case ControlFlowPackage.COMPOSITE_TASK__FINAL_STATE:
                return finalState != null;
            case ControlFlowPackage.COMPOSITE_TASK__INITIAL_STATE:
                return initialState != null;
            case ControlFlowPackage.COMPOSITE_TASK__PREDICATES:
                return predicates != null && !predicates.isEmpty();
        }
        return super.eIsSet(featureID);
    }
}
