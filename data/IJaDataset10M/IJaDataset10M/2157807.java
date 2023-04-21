package de.hu_berlin.sam.mmunit.error;

import org.eclipse.emf.ecore.EReference;

/**
 * <!-- begin-user-doc --> A representation of the model object '
 * <em><b>Unmatched Bidirectional Reference</b></em>'. <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link de.hu_berlin.sam.mmunit.error.UnmatchedBidirectionalReference#getEnd1ModelReference <em>End1 Model Reference</em>}</li>
 *   <li>{@link de.hu_berlin.sam.mmunit.error.UnmatchedBidirectionalReference#getEnd2ModelReference <em>End2 Model Reference</em>}</li>
 * </ul>
 * </p>
 *
 * @see de.hu_berlin.sam.mmunit.error.ErrorPackage#getUnmatchedBidirectionalReference()
 * @model
 * @generated
 */
public interface UnmatchedBidirectionalReference extends ReferenceCausedError {

    /**
	 * Returns the value of the '<em><b>End1 Model Reference</b></em>' reference. <!--
	 * begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>End1 Model Reference</em>' reference isn't clear, there really
	 * should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>End1 Model Reference</em>' reference.
	 * @see #setEnd1ModelReference(EReference)
	 * @see de.hu_berlin.sam.mmunit.error.ErrorPackage#getUnmatchedBidirectionalReference_End1ModelReference()
	 * @model required="true"
	 * @generated
	 */
    EReference getEnd1ModelReference();

    /**
	 * Sets the value of the '{@link de.hu_berlin.sam.mmunit.error.UnmatchedBidirectionalReference#getEnd1ModelReference <em>End1 Model Reference</em>}' reference.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @param value the new value of the '<em>End1 Model Reference</em>' reference.
	 * @see #getEnd1ModelReference()
	 * @generated
	 */
    void setEnd1ModelReference(EReference value);

    /**
	 * Returns the value of the '<em><b>End2 Model Reference</b></em>' reference. <!--
	 * begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>End2 Model Reference</em>' reference isn't clear, there really
	 * should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>End2 Model Reference</em>' reference.
	 * @see #setEnd2ModelReference(EReference)
	 * @see de.hu_berlin.sam.mmunit.error.ErrorPackage#getUnmatchedBidirectionalReference_End2ModelReference()
	 * @model required="true"
	 * @generated
	 */
    EReference getEnd2ModelReference();

    /**
	 * Sets the value of the '{@link de.hu_berlin.sam.mmunit.error.UnmatchedBidirectionalReference#getEnd2ModelReference <em>End2 Model Reference</em>}' reference.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @param value the new value of the '<em>End2 Model Reference</em>' reference.
	 * @see #getEnd2ModelReference()
	 * @generated
	 */
    void setEnd2ModelReference(EReference value);
}
