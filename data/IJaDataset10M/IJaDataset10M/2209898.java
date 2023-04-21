package net.sf.etl.samples.ej.ast;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Init Statement</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.sf.etl.samples.ej.ast.InitStatement#getBody <em>Body</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.sf.etl.samples.ej.ast.AstPackage#getInitStatement()
 * @model abstract="true"
 * @generated
 */
public interface InitStatement extends ClassifierMemberStatement {

    /**
	 * Returns the value of the '<em><b>Body</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Body</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Body</em>' containment reference.
	 * @see #setBody(MethodBlock)
	 * @see net.sf.etl.samples.ej.ast.AstPackage#getInitStatement_Body()
	 * @model containment="true" resolveProxies="false"
	 * @generated
	 */
    MethodBlock getBody();

    /**
	 * Sets the value of the '{@link net.sf.etl.samples.ej.ast.InitStatement#getBody <em>Body</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Body</em>' containment reference.
	 * @see #getBody()
	 * @generated
	 */
    void setBody(MethodBlock value);
}
