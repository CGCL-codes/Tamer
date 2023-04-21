package org.emftext.language.owl;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Ontology</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.emftext.language.owl.Ontology#getVersionIRI <em>Version IRI</em>}</li>
 *   <li>{@link org.emftext.language.owl.Ontology#getImports <em>Imports</em>}</li>
 *   <li>{@link org.emftext.language.owl.Ontology#getFrames <em>Frames</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.emftext.language.owl.OwlPackage#getOntology()
 * @model
 * @generated
 */
public interface Ontology extends URIIdentified, Annotateable {

    /**
	 * Returns the value of the '<em><b>Version IRI</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Version IRI</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Version IRI</em>' attribute.
	 * @see #setVersionIRI(String)
	 * @see org.emftext.language.owl.OwlPackage#getOntology_VersionIRI()
	 * @model
	 * @generated
	 */
    String getVersionIRI();

    /**
	 * Sets the value of the '{@link org.emftext.language.owl.Ontology#getVersionIRI <em>Version IRI</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Version IRI</em>' attribute.
	 * @see #getVersionIRI()
	 * @generated
	 */
    void setVersionIRI(String value);

    /**
	 * Returns the value of the '<em><b>Imports</b></em>' reference list.
	 * The list contents are of type {@link org.emftext.language.owl.Ontology}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Imports</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Imports</em>' reference list.
	 * @see org.emftext.language.owl.OwlPackage#getOntology_Imports()
	 * @model
	 * @generated
	 */
    EList<Ontology> getImports();

    /**
	 * Returns the value of the '<em><b>Frames</b></em>' containment reference list.
	 * The list contents are of type {@link org.emftext.language.owl.Frame}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Frames</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Frames</em>' containment reference list.
	 * @see org.emftext.language.owl.OwlPackage#getOntology_Frames()
	 * @model containment="true"
	 * @generated
	 */
    EList<Frame> getFrames();
}
