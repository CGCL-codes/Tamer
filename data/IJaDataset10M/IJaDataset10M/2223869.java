package org.eclipse.emf.mapping;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Function Name Pair</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.emf.mapping.FunctionNamePair#getIn2out <em>In2out</em>}</li>
 *   <li>{@link org.eclipse.emf.mapping.FunctionNamePair#getOut2in <em>Out2in</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.emf.mapping.MappingPackage#getFunctionNamePair()
 * @model
 * @generated
 */
public interface FunctionNamePair extends TypeConverter {

    /**
   * Returns the value of the '<em><b>In2out</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>In2out</em>' attribute.
   * @see #setIn2out(String)
   * @see org.eclipse.emf.mapping.MappingPackage#getFunctionNamePair_In2out()
   * @model
   * @generated
   */
    String getIn2out();

    /**
   * Sets the value of the '{@link org.eclipse.emf.mapping.FunctionNamePair#getIn2out <em>In2out</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>In2out</em>' attribute.
   * @see #getIn2out()
   * @generated
   */
    void setIn2out(String value);

    /**
   * Returns the value of the '<em><b>Out2in</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Out2in</em>' attribute.
   * @see #setOut2in(String)
   * @see org.eclipse.emf.mapping.MappingPackage#getFunctionNamePair_Out2in()
   * @model
   * @generated
   */
    String getOut2in();

    /**
   * Sets the value of the '{@link org.eclipse.emf.mapping.FunctionNamePair#getOut2in <em>Out2in</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Out2in</em>' attribute.
   * @see #getOut2in()
   * @generated
   */
    void setOut2in(String value);
}
