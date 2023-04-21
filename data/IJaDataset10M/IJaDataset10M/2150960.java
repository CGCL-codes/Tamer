package org.emftext.language.owl;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Disjoint Classes</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.emftext.language.owl.DisjointClasses#getDescriptions <em>Descriptions</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.emftext.language.owl.OwlPackage#getDisjointClasses()
 * @model
 * @generated
 */
public interface DisjointClasses extends Misc {

    /**
	 * Returns the value of the '<em><b>Descriptions</b></em>' containment reference list.
	 * The list contents are of type {@link org.emftext.language.owl.Description}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Descriptions</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Descriptions</em>' containment reference list.
	 * @see org.emftext.language.owl.OwlPackage#getDisjointClasses_Descriptions()
	 * @model containment="true" required="true"
	 * @generated
	 */
    EList<Description> getDescriptions();
}
