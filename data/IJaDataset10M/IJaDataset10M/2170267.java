package com.safi.core.actionstep;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Assignment</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link com.safi.core.actionstep.Assignment#getValue <em>Value</em>}</li>
 *   <li>{@link com.safi.core.actionstep.Assignment#getVariableName <em>Variable Name</em>}</li>
 * </ul>
 * </p>
 *
 * @see com.safi.core.actionstep.ActionStepPackage#getAssignment()
 * @model
 * @generated
 */
public interface Assignment extends ActionStep {

    /**
	 * Returns the value of the '<em><b>Value</b></em>' containment reference.
	 * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Value</em>' containment reference isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
	 * @return the value of the '<em>Value</em>' containment reference.
	 * @see #setValue(DynamicValue)
	 * @see com.safi.core.actionstep.ActionStepPackage#getAssignment_Value()
	 * @model containment="true" ordered="false"
	 *        annotation="DynamicValueAnnotation type='VariableName' isTypeLocked='false' description='The value to assign' expectedReturnType='Variable' helperClass=''"
	 *        annotation="Required criteria='non-null'"
	 * @generated
	 */
    DynamicValue getValue();

    /**
	 * Sets the value of the '{@link com.safi.core.actionstep.Assignment#getValue <em>Value</em>}' containment reference.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Value</em>' containment reference.
	 * @see #getValue()
	 * @generated
	 */
    void setValue(DynamicValue value);

    /**
	 * Returns the value of the '<em><b>Variable Name</b></em>' containment reference.
	 * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Variable Name</em>' containment reference isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
	 * @return the value of the '<em>Variable Name</em>' containment reference.
	 * @see #setVariableName(DynamicValue)
	 * @see com.safi.core.actionstep.ActionStepPackage#getAssignment_VariableName()
	 * @model containment="true" ordered="false"
	 *        annotation="DynamicValueAnnotation type='VariableName' isTypeLocked='true' description='The assignee variable' expectedReturnType='Variable' helperClass=''"
	 *        annotation="Directionality output='true'"
	 *        annotation="Required criteria='non-null'"
	 *        annotation="MetaProperty displayText='Variable'"
	 * @generated
	 */
    DynamicValue getVariableName();

    /**
	 * Sets the value of the '{@link com.safi.core.actionstep.Assignment#getVariableName <em>Variable Name</em>}' containment reference.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Variable Name</em>' containment reference.
	 * @see #getVariableName()
	 * @generated
	 */
    void setVariableName(DynamicValue value);
}
