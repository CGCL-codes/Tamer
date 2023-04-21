package org.emftext.language.owl;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Different Individuals</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.emftext.language.owl.DifferentIndividuals#getIndividuals <em>Individuals</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.emftext.language.owl.OwlPackage#getDifferentIndividuals()
 * @model
 * @generated
 */
public interface DifferentIndividuals extends Misc {

    /**
	 * Returns the value of the '<em><b>Individuals</b></em>' reference list.
	 * The list contents are of type {@link org.emftext.language.owl.Individual}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Individuals</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Individuals</em>' reference list.
	 * @see org.emftext.language.owl.OwlPackage#getDifferentIndividuals_Individuals()
	 * @model required="true"
	 * @generated
	 */
    EList<Individual> getIndividuals();
}
