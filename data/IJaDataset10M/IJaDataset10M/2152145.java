package eu.medeia.ecore.apmm.TiDiaM;

import eu.medeia.ecore.apmm.bm.INamedElement;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Transition</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link eu.medeia.ecore.apmm.TiDiaM.Transition#getTargetState <em>Target State</em>}</li>
 *   <li>{@link eu.medeia.ecore.apmm.TiDiaM.Transition#getTransitionEvent <em>Transition Event</em>}</li>
 * </ul>
 * </p>
 *
 * @see eu.medeia.ecore.apmm.TiDiaM.TiDiaMPackage#getTransition()
 * @model
 * @generated
 */
public interface Transition extends INamedElement {

    /**
	 * Returns the value of the '<em><b>Target State</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Target State</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Target State</em>' reference.
	 * @see #setTargetState(State)
	 * @see eu.medeia.ecore.apmm.TiDiaM.TiDiaMPackage#getTransition_TargetState()
	 * @model required="true"
	 * @generated
	 */
    State getTargetState();

    /**
	 * Sets the value of the '{@link eu.medeia.ecore.apmm.TiDiaM.Transition#getTargetState <em>Target State</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Target State</em>' reference.
	 * @see #getTargetState()
	 * @generated
	 */
    void setTargetState(State value);

    /**
	 * Returns the value of the '<em><b>Transition Event</b></em>' containment reference.
	 * It is bidirectional and its opposite is '{@link eu.medeia.ecore.apmm.TiDiaM.TransitionEvent#getTransition <em>Transition</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Transition Event</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Transition Event</em>' containment reference.
	 * @see #setTransitionEvent(TransitionEvent)
	 * @see eu.medeia.ecore.apmm.TiDiaM.TiDiaMPackage#getTransition_TransitionEvent()
	 * @see eu.medeia.ecore.apmm.TiDiaM.TransitionEvent#getTransition
	 * @model opposite="transition" containment="true" required="true"
	 * @generated
	 */
    TransitionEvent getTransitionEvent();

    /**
	 * Sets the value of the '{@link eu.medeia.ecore.apmm.TiDiaM.Transition#getTransitionEvent <em>Transition Event</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Transition Event</em>' containment reference.
	 * @see #getTransitionEvent()
	 * @generated
	 */
    void setTransitionEvent(TransitionEvent value);
}
