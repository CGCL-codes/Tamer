package net.sf.etl.parsers.grammar;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Wrapper</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.sf.etl.parsers.grammar.Wrapper#getProperty <em>Property</em>}</li>
 *   <li>{@link net.sf.etl.parsers.grammar.Wrapper#getObject <em>Object</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.sf.etl.parsers.grammar.GrammarPackage#getWrapper()
 * @model
 * @generated
 */
public interface Wrapper extends Element {

    /**
	 * Returns the value of the '<em><b>Property</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Property</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Property</em>' attribute.
	 * @see #setProperty(String)
	 * @see net.sf.etl.parsers.grammar.GrammarPackage#getWrapper_Property()
	 * @model
	 * @generated
	 */
    String getProperty();

    /**
	 * Sets the value of the '{@link net.sf.etl.parsers.grammar.Wrapper#getProperty <em>Property</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Property</em>' attribute.
	 * @see #getProperty()
	 * @generated
	 */
    void setProperty(String value);

    /**
	 * Returns the value of the '<em><b>Object</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Object</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Object</em>' containment reference.
	 * @see #setObject(ObjectName)
	 * @see net.sf.etl.parsers.grammar.GrammarPackage#getWrapper_Object()
	 * @model containment="true" required="true"
	 * @generated
	 */
    ObjectName getObject();

    /**
	 * Sets the value of the '{@link net.sf.etl.parsers.grammar.Wrapper#getObject <em>Object</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Object</em>' containment reference.
	 * @see #getObject()
	 * @generated
	 */
    void setObject(ObjectName value);
}
