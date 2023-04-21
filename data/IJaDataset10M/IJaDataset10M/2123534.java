package org.eclipse.epsilon.dom.eol;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Enumeration Literal Expression</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.epsilon.dom.eol.EnumerationLiteralExpression#getModel <em>Model</em>}</li>
 *   <li>{@link org.eclipse.epsilon.dom.eol.EnumerationLiteralExpression#getEnumeration <em>Enumeration</em>}</li>
 *   <li>{@link org.eclipse.epsilon.dom.eol.EnumerationLiteralExpression#getLiteral <em>Literal</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.epsilon.dom.eol.EolPackage#getEnumerationLiteralExpression()
 * @model annotation="exeed label='return self.model + \'!\' + self.enumeration + \'#\' + self.literal;'"
 * @generated
 */
public interface EnumerationLiteralExpression extends Expression {

    /**
	 * Returns the value of the '<em><b>Model</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Model</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Model</em>' attribute.
	 * @see #setModel(String)
	 * @see org.eclipse.epsilon.dom.eol.EolPackage#getEnumerationLiteralExpression_Model()
	 * @model dataType="org.eclipse.epsilon.dom.eol.String"
	 * @generated
	 */
    String getModel();

    /**
	 * Sets the value of the '{@link org.eclipse.epsilon.dom.eol.EnumerationLiteralExpression#getModel <em>Model</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Model</em>' attribute.
	 * @see #getModel()
	 * @generated
	 */
    void setModel(String value);

    /**
	 * Returns the value of the '<em><b>Enumeration</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Enumeration</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Enumeration</em>' attribute.
	 * @see #setEnumeration(String)
	 * @see org.eclipse.epsilon.dom.eol.EolPackage#getEnumerationLiteralExpression_Enumeration()
	 * @model dataType="org.eclipse.epsilon.dom.eol.String"
	 * @generated
	 */
    String getEnumeration();

    /**
	 * Sets the value of the '{@link org.eclipse.epsilon.dom.eol.EnumerationLiteralExpression#getEnumeration <em>Enumeration</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Enumeration</em>' attribute.
	 * @see #getEnumeration()
	 * @generated
	 */
    void setEnumeration(String value);

    /**
	 * Returns the value of the '<em><b>Literal</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Literal</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Literal</em>' attribute.
	 * @see #setLiteral(String)
	 * @see org.eclipse.epsilon.dom.eol.EolPackage#getEnumerationLiteralExpression_Literal()
	 * @model dataType="org.eclipse.epsilon.dom.eol.String"
	 * @generated
	 */
    String getLiteral();

    /**
	 * Sets the value of the '{@link org.eclipse.epsilon.dom.eol.EnumerationLiteralExpression#getLiteral <em>Literal</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Literal</em>' attribute.
	 * @see #getLiteral()
	 * @generated
	 */
    void setLiteral(String value);
}
