package org.xtext.example.swrtj;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Parameter Reference</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.xtext.example.swrtj.ParameterReference#getParameter <em>Parameter</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.xtext.example.swrtj.SwrtjPackage#getParameterReference()
 * @model
 * @generated
 */
public interface ParameterReference extends Start {

    /**
   * Returns the value of the '<em><b>Parameter</b></em>' reference.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Parameter</em>' reference isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Parameter</em>' reference.
   * @see #setParameter(Parameter)
   * @see org.xtext.example.swrtj.SwrtjPackage#getParameterReference_Parameter()
   * @model
   * @generated
   */
    Parameter getParameter();

    /**
   * Sets the value of the '{@link org.xtext.example.swrtj.ParameterReference#getParameter <em>Parameter</em>}' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Parameter</em>' reference.
   * @see #getParameter()
   * @generated
   */
    void setParameter(Parameter value);
}
