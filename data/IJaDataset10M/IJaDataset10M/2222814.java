package org.eclipse.epsilon.dom.eol;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Binary Operator Expression</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.epsilon.dom.eol.BinaryOperatorExpression#getLeftExpression <em>Left Expression</em>}</li>
 *   <li>{@link org.eclipse.epsilon.dom.eol.BinaryOperatorExpression#getRightExpression <em>Right Expression</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.epsilon.dom.eol.EolPackage#getBinaryOperatorExpression()
 * @model annotation="exeed label='return self.leftExpression.label() + \' \' + self.operator.label() + \' \' + self.rightExpression.label();'"
 * @generated
 */
public interface BinaryOperatorExpression extends OperatorExpression {

    /**
	 * Returns the value of the '<em><b>Left Expression</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Left Expression</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Left Expression</em>' containment reference.
	 * @see #setLeftExpression(Expression)
	 * @see org.eclipse.epsilon.dom.eol.EolPackage#getBinaryOperatorExpression_LeftExpression()
	 * @model containment="true"
	 * @generated
	 */
    Expression getLeftExpression();

    /**
	 * Sets the value of the '{@link org.eclipse.epsilon.dom.eol.BinaryOperatorExpression#getLeftExpression <em>Left Expression</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Left Expression</em>' containment reference.
	 * @see #getLeftExpression()
	 * @generated
	 */
    void setLeftExpression(Expression value);

    /**
	 * Returns the value of the '<em><b>Right Expression</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Right Expression</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Right Expression</em>' containment reference.
	 * @see #setRightExpression(Expression)
	 * @see org.eclipse.epsilon.dom.eol.EolPackage#getBinaryOperatorExpression_RightExpression()
	 * @model containment="true"
	 * @generated
	 */
    Expression getRightExpression();

    /**
	 * Sets the value of the '{@link org.eclipse.epsilon.dom.eol.BinaryOperatorExpression#getRightExpression <em>Right Expression</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Right Expression</em>' containment reference.
	 * @see #getRightExpression()
	 * @generated
	 */
    void setRightExpression(Expression value);
}
