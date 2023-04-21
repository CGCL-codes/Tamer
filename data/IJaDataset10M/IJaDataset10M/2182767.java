package org.deltaj.constraints.constraints;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Class Exists</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.deltaj.constraints.constraints.ClassExists#getClassName <em>Class Name</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.deltaj.constraints.constraints.ConstraintsPackage#getClassExists()
 * @model
 * @generated
 */
public interface ClassExists extends MethodConstraint {

    /**
   * Returns the value of the '<em><b>Class Name</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Class Name</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Class Name</em>' attribute.
   * @see #setClassName(String)
   * @see org.deltaj.constraints.constraints.ConstraintsPackage#getClassExists_ClassName()
   * @model
   * @generated
   */
    String getClassName();

    /**
   * Sets the value of the '{@link org.deltaj.constraints.constraints.ClassExists#getClassName <em>Class Name</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Class Name</em>' attribute.
   * @see #getClassName()
   * @generated
   */
    void setClassName(String value);
}
