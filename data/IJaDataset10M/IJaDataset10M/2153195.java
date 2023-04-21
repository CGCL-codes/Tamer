package com.atosorigin.nl.dsl.agreement;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Expression Rule Parenthesised</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link com.atosorigin.nl.dsl.agreement.ExpressionRuleParenthesised#getExpression <em>Expression</em>}</li>
 * </ul>
 * </p>
 *
 * @see com.atosorigin.nl.dsl.agreement.AgreementPackage#getExpressionRuleParenthesised()
 * @model
 * @generated
 */
public interface ExpressionRuleParenthesised extends ExpressionRuleHead {

    /**
	 * Returns the value of the '<em><b>Expression</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Expression</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Expression</em>' containment reference.
	 * @see #setExpression(ExpressionRule)
	 * @see com.atosorigin.nl.dsl.agreement.AgreementPackage#getExpressionRuleParenthesised_Expression()
	 * @model containment="true"
	 * @generated
	 */
    ExpressionRule getExpression();

    /**
	 * Sets the value of the '{@link com.atosorigin.nl.dsl.agreement.ExpressionRuleParenthesised#getExpression <em>Expression</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Expression</em>' containment reference.
	 * @see #getExpression()
	 * @generated
	 */
    void setExpression(ExpressionRule value);
}
