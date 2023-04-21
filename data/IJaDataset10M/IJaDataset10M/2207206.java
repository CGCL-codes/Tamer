package net.sf.etl.samples.ej.ast;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Documentation Line</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.sf.etl.samples.ej.ast.DocumentationLine#getText <em>Text</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.sf.etl.samples.ej.ast.AstPackage#getDocumentationLine()
 * @model
 * @generated
 */
public interface DocumentationLine extends EJElement {

    /**
	 * Returns the value of the '<em><b>Text</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Text</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Text</em>' attribute.
	 * @see #setText(String)
	 * @see net.sf.etl.samples.ej.ast.AstPackage#getDocumentationLine_Text()
	 * @model dataType="net.sf.etl.samples.ej.ast.String"
	 * @generated
	 */
    String getText();

    /**
	 * Sets the value of the '{@link net.sf.etl.samples.ej.ast.DocumentationLine#getText <em>Text</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Text</em>' attribute.
	 * @see #getText()
	 * @generated
	 */
    void setText(String value);
}
