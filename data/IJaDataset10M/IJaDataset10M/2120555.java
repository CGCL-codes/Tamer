package org.xtext.example.swrtj;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Provided Method</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.xtext.example.swrtj.ProvidedMethod#isIsSynchronized <em>Is Synchronized</em>}</li>
 *   <li>{@link org.xtext.example.swrtj.ProvidedMethod#getBlock <em>Block</em>}</li>
 *   <li>{@link org.xtext.example.swrtj.ProvidedMethod#getReturnStatement <em>Return Statement</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.xtext.example.swrtj.SwrtjPackage#getProvidedMethod()
 * @model
 * @generated
 */
public interface ProvidedMethod extends Method {

    /**
   * Returns the value of the '<em><b>Is Synchronized</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Is Synchronized</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Is Synchronized</em>' attribute.
   * @see #setIsSynchronized(boolean)
   * @see org.xtext.example.swrtj.SwrtjPackage#getProvidedMethod_IsSynchronized()
   * @model
   * @generated
   */
    boolean isIsSynchronized();

    /**
   * Sets the value of the '{@link org.xtext.example.swrtj.ProvidedMethod#isIsSynchronized <em>Is Synchronized</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Is Synchronized</em>' attribute.
   * @see #isIsSynchronized()
   * @generated
   */
    void setIsSynchronized(boolean value);

    /**
   * Returns the value of the '<em><b>Block</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Block</em>' containment reference isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Block</em>' containment reference.
   * @see #setBlock(Block)
   * @see org.xtext.example.swrtj.SwrtjPackage#getProvidedMethod_Block()
   * @model containment="true"
   * @generated
   */
    Block getBlock();

    /**
   * Sets the value of the '{@link org.xtext.example.swrtj.ProvidedMethod#getBlock <em>Block</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Block</em>' containment reference.
   * @see #getBlock()
   * @generated
   */
    void setBlock(Block value);

    /**
   * Returns the value of the '<em><b>Return Statement</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Return Statement</em>' containment reference isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Return Statement</em>' containment reference.
   * @see #setReturnStatement(ReturnStatement)
   * @see org.xtext.example.swrtj.SwrtjPackage#getProvidedMethod_ReturnStatement()
   * @model containment="true"
   * @generated
   */
    ReturnStatement getReturnStatement();

    /**
   * Sets the value of the '{@link org.xtext.example.swrtj.ProvidedMethod#getReturnStatement <em>Return Statement</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Return Statement</em>' containment reference.
   * @see #getReturnStatement()
   * @generated
   */
    void setReturnStatement(ReturnStatement value);
}
