package net.sf.etl.samples.ej.ast;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Attribute Set</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.sf.etl.samples.ej.ast.AttributeSet#getAttributes <em>Attributes</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.sf.etl.samples.ej.ast.AstPackage#getAttributeSet()
 * @model
 * @generated
 */
public interface AttributeSet extends EJElement {

    /**
	 * Returns the value of the '<em><b>Attributes</b></em>' containment reference list.
	 * The list contents are of type {@link net.sf.etl.samples.ej.ast.Expression}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Attributes</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Attributes</em>' containment reference list.
	 * @see net.sf.etl.samples.ej.ast.AstPackage#getAttributeSet_Attributes()
	 * @model type="net.sf.etl.samples.ej.ast.Expression" containment="true" resolveProxies="false"
	 * @generated
	 */
    EList getAttributes();
}
