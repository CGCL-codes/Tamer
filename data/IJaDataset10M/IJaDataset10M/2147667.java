package de.hu_berlin.sam.mmunit.error;

import org.eclipse.emf.ecore.EStructuralFeature;

/**
 * <!-- begin-user-doc --> A representation of the model object '<em><b>Multiplicity Error</b></em>
 * '. <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link de.hu_berlin.sam.mmunit.error.MultiplicityError#getModelFeature <em>Model Feature</em>}</li>
 *   <li>{@link de.hu_berlin.sam.mmunit.error.MultiplicityError#getRequiredBound <em>Required Bound</em>}</li>
 * </ul>
 * </p>
 *
 * @see de.hu_berlin.sam.mmunit.error.ErrorPackage#getMultiplicityError()
 * @model abstract="true"
 * @generated
 */
public interface MultiplicityError extends MMUnitError {

    /**
	 * Returns the value of the '<em><b>Model Feature</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Model Feature</em>' reference isn't clear, there really should be
	 * more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Model Feature</em>' reference.
	 * @see #setModelFeature(EStructuralFeature)
	 * @see de.hu_berlin.sam.mmunit.error.ErrorPackage#getMultiplicityError_ModelFeature()
	 * @model required="true"
	 * @generated
	 */
    EStructuralFeature getModelFeature();

    /**
	 * Sets the value of the '{@link de.hu_berlin.sam.mmunit.error.MultiplicityError#getModelFeature <em>Model Feature</em>}' reference.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @param value the new value of the '<em>Model Feature</em>' reference.
	 * @see #getModelFeature()
	 * @generated
	 */
    void setModelFeature(EStructuralFeature value);

    /**
	 * Returns the value of the '<em><b>Required Bound</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Required Bound</em>' attribute isn't clear, there really should be
	 * more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Required Bound</em>' attribute.
	 * @see #setRequiredBound(int)
	 * @see de.hu_berlin.sam.mmunit.error.ErrorPackage#getMultiplicityError_RequiredBound()
	 * @model required="true"
	 * @generated
	 */
    int getRequiredBound();

    /**
	 * Sets the value of the '{@link de.hu_berlin.sam.mmunit.error.MultiplicityError#getRequiredBound <em>Required Bound</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @param value the new value of the '<em>Required Bound</em>' attribute.
	 * @see #getRequiredBound()
	 * @generated
	 */
    void setRequiredBound(int value);
}
