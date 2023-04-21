package org.eclipse.emf.ecore;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>ENamed Element</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.emf.ecore.ENamedElement#getName <em>Name</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.emf.ecore.EcorePackage#getENamedElement()
 * @model abstract="true"
 *        annotation="http://www.eclipse.org/emf/2002/Ecore constraints='WellFormedName'"
 * @generated
 */
public interface ENamedElement extends EModelElement {

    /**
   * Returns the value of the '<em><b>Name</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * It represents the name of the element.
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Name</em>' attribute.
   * @see #setName(String)
   * @see org.eclipse.emf.ecore.EcorePackage#getENamedElement_Name()
   * @model
   * @generated
   */
    String getName();

    /**
   * Sets the value of the '{@link org.eclipse.emf.ecore.ENamedElement#getName <em>Name</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Name</em>' attribute.
   * @see #getName()
   * @generated
   */
    void setName(String value);
}
