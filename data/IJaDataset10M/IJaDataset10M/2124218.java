package net.sf.etl.parsers.grammar;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Let</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.sf.etl.parsers.grammar.Let#getName <em>Name</em>}</li>
 *   <li>{@link net.sf.etl.parsers.grammar.Let#getOperator <em>Operator</em>}</li>
 *   <li>{@link net.sf.etl.parsers.grammar.Let#getExpression <em>Expression</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.sf.etl.parsers.grammar.GrammarPackage#getLet()
 * @model
 * @generated
 */
public interface Let extends SyntaxStatement {

    /**
	 * Returns the value of the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Name</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Name</em>' attribute.
	 * @see #setName(String)
	 * @see net.sf.etl.parsers.grammar.GrammarPackage#getLet_Name()
	 * @model
	 * @generated
	 */
    String getName();

    /**
	 * Sets the value of the '{@link net.sf.etl.parsers.grammar.Let#getName <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Name</em>' attribute.
	 * @see #getName()
	 * @generated
	 */
    void setName(String value);

    /**
	 * Returns the value of the '<em><b>Operator</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Operator</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Operator</em>' attribute.
	 * @see #setOperator(String)
	 * @see net.sf.etl.parsers.grammar.GrammarPackage#getLet_Operator()
	 * @model
	 * @generated
	 */
    String getOperator();

    /**
	 * Sets the value of the '{@link net.sf.etl.parsers.grammar.Let#getOperator <em>Operator</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Operator</em>' attribute.
	 * @see #getOperator()
	 * @generated
	 */
    void setOperator(String value);

    /**
	 * Returns the value of the '<em><b>Expression</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Expression</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Expression</em>' containment reference.
	 * @see #setExpression(Syntax)
	 * @see net.sf.etl.parsers.grammar.GrammarPackage#getLet_Expression()
	 * @model containment="true"
	 * @generated
	 */
    Syntax getExpression();

    /**
	 * Sets the value of the '{@link net.sf.etl.parsers.grammar.Let#getExpression <em>Expression</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Expression</em>' containment reference.
	 * @see #getExpression()
	 * @generated
	 */
    void setExpression(Syntax value);
}
