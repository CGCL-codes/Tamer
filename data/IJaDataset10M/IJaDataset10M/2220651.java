package org.spbu.plweb;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Target Ref Element</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.spbu.plweb.TargetRefElement#getParent <em>Parent</em>}</li>
 *   <li>{@link org.spbu.plweb.TargetRefElement#isOptional <em>Optional</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.spbu.plweb.PlwebPackage#getTargetRefElement()
 * @model
 * @generated
 */
public interface TargetRefElement extends Element {

    /**
	 * Returns the value of the '<em><b>Parent</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Parent</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Parent</em>' reference.
	 * @see #setParent(SourceRefElement)
	 * @see org.spbu.plweb.PlwebPackage#getTargetRefElement_Parent()
	 * @model required="true"
	 * @generated
	 */
    SourceRefElement getParent();

    /**
	 * Sets the value of the '{@link org.spbu.plweb.TargetRefElement#getParent <em>Parent</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Parent</em>' reference.
	 * @see #getParent()
	 * @generated
	 */
    void setParent(SourceRefElement value);

    /**
	 * Returns the value of the '<em><b>Optional</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Optional</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Optional</em>' attribute.
	 * @see #setOptional(boolean)
	 * @see org.spbu.plweb.PlwebPackage#getTargetRefElement_Optional()
	 * @model
	 * @generated
	 */
    boolean isOptional();

    /**
	 * Sets the value of the '{@link org.spbu.plweb.TargetRefElement#isOptional <em>Optional</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Optional</em>' attribute.
	 * @see #isOptional()
	 * @generated
	 */
    void setOptional(boolean value);
}
