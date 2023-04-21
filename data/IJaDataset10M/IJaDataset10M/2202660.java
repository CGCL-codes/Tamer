package ru.ifmo.rain.astrans.astransast;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Attribute AS</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link ru.ifmo.rain.astrans.astransast.AttributeAS#getType <em>Type</em>}</li>
 * </ul>
 * </p>
 *
 * @see ru.ifmo.rain.astrans.astransast.AstransastPackage#getAttributeAS()
 * @model
 * @generated
 */
public interface AttributeAS extends StructuralFeatureAS {

    /**
	 * Returns the value of the '<em><b>Type</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Type</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Type</em>' containment reference.
	 * @see #setType(QualifiedName)
	 * @see ru.ifmo.rain.astrans.astransast.AstransastPackage#getAttributeAS_Type()
	 * @model containment="true" required="true"
	 * @generated
	 */
    QualifiedName getType();

    /**
	 * Sets the value of the '{@link ru.ifmo.rain.astrans.astransast.AttributeAS#getType <em>Type</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Type</em>' containment reference.
	 * @see #getType()
	 * @generated
	 */
    void setType(QualifiedName value);
}
