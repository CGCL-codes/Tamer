package net.sf.etl.parsers.grammar;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Modifier Op</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.sf.etl.parsers.grammar.ModifierOp#getValue <em>Value</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.sf.etl.parsers.grammar.GrammarPackage#getModifierOp()
 * @model
 * @generated
 */
public interface ModifierOp extends TokenRefOp {

    /**
	 * Returns the value of the '<em><b>Value</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Value</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Value</em>' attribute.
	 * @see #setValue(String)
	 * @see net.sf.etl.parsers.grammar.GrammarPackage#getModifierOp_Value()
	 * @model
	 * @generated
	 */
    String getValue();

    /**
	 * Sets the value of the '{@link net.sf.etl.parsers.grammar.ModifierOp#getValue <em>Value</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Value</em>' attribute.
	 * @see #getValue()
	 * @generated
	 */
    void setValue(String value);
}
