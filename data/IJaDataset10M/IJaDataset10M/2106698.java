package ms.jasim.model.event.impl;

import java.util.Collection;
import ms.jasim.model.event.Event;
import ms.jasim.model.event.EventPackage;
import ms.jasim.model.event.EventReaction;
import ms.jasim.model.event.ReactionList;
import ms.jasim.model.impl.NamedModelObjectImpl;
import ms.utils.EModelObjectContainmentWithInverseEList;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.util.EObjectContainmentWithInverseEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Reaction List</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link ms.jasim.model.event.impl.ReactionListImpl#getReaction <em>Reaction</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class ReactionListImpl extends NamedModelObjectImpl implements ReactionList {

    /**
	 * The cached value of the '{@link #getReaction() <em>Reaction</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getReaction()
	 * @generated
	 * @ordered
	 */
    protected EList<EventReaction> reaction;

    protected Event event;

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
    public ReactionListImpl() {
        super();
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    protected EClass eStaticClass() {
        return EventPackage.Literals.REACTION_LIST;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 */
    public EList<EventReaction> getReaction() {
        if (reaction == null) {
            reaction = new EModelObjectContainmentWithInverseEList<EventReaction>(EventReaction.class, this, EventPackage.REACTION_LIST__REACTION, EventPackage.EVENT_REACTION__OWNER);
        }
        return reaction;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 */
    @Override
    public Event getEvent() {
        return event;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 */
    public void setEvent(Event newEvent) {
        this.event = newEvent;
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
            case EventPackage.REACTION_LIST__REACTION:
                return ((InternalEList<InternalEObject>) (InternalEList<?>) getReaction()).basicAdd(otherEnd, msgs);
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
            case EventPackage.REACTION_LIST__REACTION:
                return ((InternalEList<?>) getReaction()).basicRemove(otherEnd, msgs);
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
            case EventPackage.REACTION_LIST__REACTION:
                return getReaction();
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
            case EventPackage.REACTION_LIST__REACTION:
                getReaction().clear();
                getReaction().addAll((Collection<? extends EventReaction>) newValue);
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
            case EventPackage.REACTION_LIST__REACTION:
                getReaction().clear();
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
            case EventPackage.REACTION_LIST__REACTION:
                return reaction != null && !reaction.isEmpty();
        }
        return super.eIsSet(featureID);
    }
}
