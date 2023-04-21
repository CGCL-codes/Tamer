package model.impl;

import java.util.Collection;
import model.Event;
import model.FinalState;
import model.ModelPackage;
import model.StateMachine;
import model.Transition;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;
import org.eclipse.emf.ecore.util.EObjectResolvingEList;
import org.eclipse.emf.ecore.util.EObjectWithInverseResolvingEList;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Final State</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link model.impl.FinalStateImpl#getName <em>Name</em>}</li>
 *   <li>{@link model.impl.FinalStateImpl#getDescription <em>Description</em>}</li>
 *   <li>{@link model.impl.FinalStateImpl#getTransitionFrom <em>Transition From</em>}</li>
 *   <li>{@link model.impl.FinalStateImpl#getEvent <em>Event</em>}</li>
 *   <li>{@link model.impl.FinalStateImpl#getStateMachine <em>State Machine</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class FinalStateImpl extends EObjectImpl implements FinalState {

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
	 * The cached value of the '{@link #getTransitionFrom() <em>Transition From</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getTransitionFrom()
	 * @generated
	 * @ordered
	 */
    protected EList<Transition> transitionFrom;

    /**
	 * The cached value of the '{@link #getEvent() <em>Event</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getEvent()
	 * @generated
	 * @ordered
	 */
    protected EList<Event> event;

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    protected FinalStateImpl() {
        super();
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    protected EClass eStaticClass() {
        return ModelPackage.Literals.FINAL_STATE;
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
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.FINAL_STATE__NAME, oldName, name));
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
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.FINAL_STATE__DESCRIPTION, oldDescription, description));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EList<Transition> getTransitionFrom() {
        if (transitionFrom == null) {
            transitionFrom = new EObjectWithInverseResolvingEList<Transition>(Transition.class, this, ModelPackage.FINAL_STATE__TRANSITION_FROM, ModelPackage.TRANSITION__TO_STATE);
        }
        return transitionFrom;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EList<Event> getEvent() {
        if (event == null) {
            event = new EObjectResolvingEList<Event>(Event.class, this, ModelPackage.FINAL_STATE__EVENT);
        }
        return event;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public StateMachine getStateMachine() {
        if (eContainerFeatureID != ModelPackage.FINAL_STATE__STATE_MACHINE) return null;
        return (StateMachine) eContainer();
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public NotificationChain basicSetStateMachine(StateMachine newStateMachine, NotificationChain msgs) {
        msgs = eBasicSetContainer((InternalEObject) newStateMachine, ModelPackage.FINAL_STATE__STATE_MACHINE, msgs);
        return msgs;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setStateMachine(StateMachine newStateMachine) {
        if (newStateMachine != eInternalContainer() || (eContainerFeatureID != ModelPackage.FINAL_STATE__STATE_MACHINE && newStateMachine != null)) {
            if (EcoreUtil.isAncestor(this, newStateMachine)) throw new IllegalArgumentException("Recursive containment not allowed for " + toString());
            NotificationChain msgs = null;
            if (eInternalContainer() != null) msgs = eBasicRemoveFromContainer(msgs);
            if (newStateMachine != null) msgs = ((InternalEObject) newStateMachine).eInverseAdd(this, ModelPackage.STATE_MACHINE__FINAL_STATE, StateMachine.class, msgs);
            msgs = basicSetStateMachine(newStateMachine, msgs);
            if (msgs != null) msgs.dispatch();
        } else if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.FINAL_STATE__STATE_MACHINE, newStateMachine, newStateMachine));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @SuppressWarnings("unchecked")
    @Override
    public NotificationChain eInverseAdd(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch(featureID) {
            case ModelPackage.FINAL_STATE__TRANSITION_FROM:
                return ((InternalEList<InternalEObject>) (InternalEList<?>) getTransitionFrom()).basicAdd(otherEnd, msgs);
            case ModelPackage.FINAL_STATE__STATE_MACHINE:
                if (eInternalContainer() != null) msgs = eBasicRemoveFromContainer(msgs);
                return basicSetStateMachine((StateMachine) otherEnd, msgs);
        }
        return super.eInverseAdd(otherEnd, featureID, msgs);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch(featureID) {
            case ModelPackage.FINAL_STATE__TRANSITION_FROM:
                return ((InternalEList<?>) getTransitionFrom()).basicRemove(otherEnd, msgs);
            case ModelPackage.FINAL_STATE__STATE_MACHINE:
                return basicSetStateMachine(null, msgs);
        }
        return super.eInverseRemove(otherEnd, featureID, msgs);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    public NotificationChain eBasicRemoveFromContainerFeature(NotificationChain msgs) {
        switch(eContainerFeatureID) {
            case ModelPackage.FINAL_STATE__STATE_MACHINE:
                return eInternalContainer().eInverseRemove(this, ModelPackage.STATE_MACHINE__FINAL_STATE, StateMachine.class, msgs);
        }
        return super.eBasicRemoveFromContainerFeature(msgs);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch(featureID) {
            case ModelPackage.FINAL_STATE__NAME:
                return getName();
            case ModelPackage.FINAL_STATE__DESCRIPTION:
                return getDescription();
            case ModelPackage.FINAL_STATE__TRANSITION_FROM:
                return getTransitionFrom();
            case ModelPackage.FINAL_STATE__EVENT:
                return getEvent();
            case ModelPackage.FINAL_STATE__STATE_MACHINE:
                return getStateMachine();
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
            case ModelPackage.FINAL_STATE__NAME:
                setName((String) newValue);
                return;
            case ModelPackage.FINAL_STATE__DESCRIPTION:
                setDescription((String) newValue);
                return;
            case ModelPackage.FINAL_STATE__TRANSITION_FROM:
                getTransitionFrom().clear();
                getTransitionFrom().addAll((Collection<? extends Transition>) newValue);
                return;
            case ModelPackage.FINAL_STATE__EVENT:
                getEvent().clear();
                getEvent().addAll((Collection<? extends Event>) newValue);
                return;
            case ModelPackage.FINAL_STATE__STATE_MACHINE:
                setStateMachine((StateMachine) newValue);
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
            case ModelPackage.FINAL_STATE__NAME:
                setName(NAME_EDEFAULT);
                return;
            case ModelPackage.FINAL_STATE__DESCRIPTION:
                setDescription(DESCRIPTION_EDEFAULT);
                return;
            case ModelPackage.FINAL_STATE__TRANSITION_FROM:
                getTransitionFrom().clear();
                return;
            case ModelPackage.FINAL_STATE__EVENT:
                getEvent().clear();
                return;
            case ModelPackage.FINAL_STATE__STATE_MACHINE:
                setStateMachine((StateMachine) null);
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
            case ModelPackage.FINAL_STATE__NAME:
                return NAME_EDEFAULT == null ? name != null : !NAME_EDEFAULT.equals(name);
            case ModelPackage.FINAL_STATE__DESCRIPTION:
                return DESCRIPTION_EDEFAULT == null ? description != null : !DESCRIPTION_EDEFAULT.equals(description);
            case ModelPackage.FINAL_STATE__TRANSITION_FROM:
                return transitionFrom != null && !transitionFrom.isEmpty();
            case ModelPackage.FINAL_STATE__EVENT:
                return event != null && !event.isEmpty();
            case ModelPackage.FINAL_STATE__STATE_MACHINE:
                return getStateMachine() != null;
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
