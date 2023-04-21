package org.emftext.language.owl;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>URI Identified</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.emftext.language.owl.URIIdentified#getUri <em>Uri</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.emftext.language.owl.OwlPackage#getURIIdentified()
 * @model abstract="true"
 * @generated
 */
public interface URIIdentified extends EObject {

    /**
	 * Returns the value of the '<em><b>Uri</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Uri</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Uri</em>' attribute.
	 * @see #setUri(String)
	 * @see org.emftext.language.owl.OwlPackage#getURIIdentified_Uri()
	 * @model required="true"
	 * @generated
	 */
    String getUri();

    /**
	 * Sets the value of the '{@link org.emftext.language.owl.URIIdentified#getUri <em>Uri</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Uri</em>' attribute.
	 * @see #getUri()
	 * @generated
	 */
    void setUri(String value);
}
