package org.fluid.uimodeling.uivocabulary;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>UI Containment</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.fluid.uimodeling.uivocabulary.UIContainment#getAllowedClass <em>Allowed Class</em>}</li>
 *   <li>{@link org.fluid.uimodeling.uivocabulary.UIContainment#getMaxCardinality <em>Max Cardinality</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.fluid.uimodeling.uivocabulary.UIVocabularyPackage#getUIContainment()
 * @model
 * @generated
 */
public interface UIContainment extends EObject {

    /**
	 * Returns the value of the '<em><b>Allowed Class</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Allowed Class</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Allowed Class</em>' reference.
	 * @see #setAllowedClass(UIClass)
	 * @see org.fluid.uimodeling.uivocabulary.UIVocabularyPackage#getUIContainment_AllowedClass()
	 * @model required="true"
	 * @generated
	 */
    UIClass getAllowedClass();

    /**
	 * Sets the value of the '{@link org.fluid.uimodeling.uivocabulary.UIContainment#getAllowedClass <em>Allowed Class</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Allowed Class</em>' reference.
	 * @see #getAllowedClass()
	 * @generated
	 */
    void setAllowedClass(UIClass value);

    /**
	 * Returns the value of the '<em><b>Max Cardinality</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Max Cardinality</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Max Cardinality</em>' attribute.
	 * @see #setMaxCardinality(int)
	 * @see org.fluid.uimodeling.uivocabulary.UIVocabularyPackage#getUIContainment_MaxCardinality()
	 * @model
	 * @generated
	 */
    int getMaxCardinality();

    /**
	 * Sets the value of the '{@link org.fluid.uimodeling.uivocabulary.UIContainment#getMaxCardinality <em>Max Cardinality</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Max Cardinality</em>' attribute.
	 * @see #getMaxCardinality()
	 * @generated
	 */
    void setMaxCardinality(int value);
}
