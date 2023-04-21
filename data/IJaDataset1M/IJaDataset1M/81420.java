package de.hu_berlin.sam.mmunit.suggestion.tsadaptation;

import de.hu_berlin.sam.mmunit.Instance;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Remove Instance</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link de.hu_berlin.sam.mmunit.suggestion.tsadaptation.RemoveInstance#getInstance <em>Instance</em>}</li>
 * </ul>
 * </p>
 *
 * @see de.hu_berlin.sam.mmunit.suggestion.tsadaptation.TSAdaptationPackage#getRemoveInstance()
 * @model
 * @generated
 */
public interface RemoveInstance extends Destruction {

    /**
	 * Returns the value of the '<em><b>Instance</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Instance</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Instance</em>' reference.
	 * @see #setInstance(Instance)
	 * @see de.hu_berlin.sam.mmunit.suggestion.tsadaptation.TSAdaptationPackage#getRemoveInstance_Instance()
	 * @model required="true"
	 * @generated
	 */
    Instance getInstance();

    /**
	 * Sets the value of the '{@link de.hu_berlin.sam.mmunit.suggestion.tsadaptation.RemoveInstance#getInstance <em>Instance</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Instance</em>' reference.
	 * @see #getInstance()
	 * @generated
	 */
    void setInstance(Instance value);
}
