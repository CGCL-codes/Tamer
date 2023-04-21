package net.sf.etl.samples.ej.ast;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Class Statement</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.sf.etl.samples.ej.ast.ClassStatement#getStaticModifier <em>Static Modifier</em>}</li>
 *   <li>{@link net.sf.etl.samples.ej.ast.ClassStatement#getExtendedType <em>Extended Type</em>}</li>
 *   <li>{@link net.sf.etl.samples.ej.ast.ClassStatement#getImplementedTypes <em>Implemented Types</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.sf.etl.samples.ej.ast.AstPackage#getClassStatement()
 * @model
 * @generated
 */
public interface ClassStatement extends ImplemenationClassifierStatement {

    /**
	 * Returns the value of the '<em><b>Static Modifier</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Static Modifier</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Static Modifier</em>' containment reference.
	 * @see #setStaticModifier(Modifier)
	 * @see net.sf.etl.samples.ej.ast.AstPackage#getClassStatement_StaticModifier()
	 * @model containment="true" resolveProxies="false"
	 * @generated
	 */
    Modifier getStaticModifier();

    /**
	 * Sets the value of the '{@link net.sf.etl.samples.ej.ast.ClassStatement#getStaticModifier <em>Static Modifier</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Static Modifier</em>' containment reference.
	 * @see #getStaticModifier()
	 * @generated
	 */
    void setStaticModifier(Modifier value);

    /**
	 * Returns the value of the '<em><b>Extended Type</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Extended Type</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Extended Type</em>' containment reference.
	 * @see #setExtendedType(Expression)
	 * @see net.sf.etl.samples.ej.ast.AstPackage#getClassStatement_ExtendedType()
	 * @model containment="true" resolveProxies="false"
	 * @generated
	 */
    Expression getExtendedType();

    /**
	 * Sets the value of the '{@link net.sf.etl.samples.ej.ast.ClassStatement#getExtendedType <em>Extended Type</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Extended Type</em>' containment reference.
	 * @see #getExtendedType()
	 * @generated
	 */
    void setExtendedType(Expression value);

    /**
	 * Returns the value of the '<em><b>Implemented Types</b></em>' containment reference list.
	 * The list contents are of type {@link net.sf.etl.samples.ej.ast.Expression}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Implemented Types</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Implemented Types</em>' containment reference list.
	 * @see net.sf.etl.samples.ej.ast.AstPackage#getClassStatement_ImplementedTypes()
	 * @model type="net.sf.etl.samples.ej.ast.Expression" containment="true" resolveProxies="false"
	 * @generated
	 */
    EList getImplementedTypes();
}
