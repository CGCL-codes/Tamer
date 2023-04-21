package org.eclipse.epsilon.dom.eol;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Expression Statement</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.epsilon.dom.eol.ExpressionStatement#getExpression <em>Expression</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.epsilon.dom.eol.EolPackage#getExpressionStatement()
 * @model annotation="exeed label='return self.expression.label() + \';\';'"
 * @generated
 */
public interface ExpressionStatement extends Statement {

    /**
	 * Returns the value of the '<em><b>Expression</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Expression</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Expression</em>' containment reference.
	 * @see #setExpression(Expression)
	 * @see org.eclipse.epsilon.dom.eol.EolPackage#getExpressionStatement_Expression()
	 * @model containment="true"
	 * @generated
	 */
    Expression getExpression();

    /**
	 * Sets the value of the '{@link org.eclipse.epsilon.dom.eol.ExpressionStatement#getExpression <em>Expression</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Expression</em>' containment reference.
	 * @see #getExpression()
	 * @generated
	 */
    void setExpression(Expression value);
}
