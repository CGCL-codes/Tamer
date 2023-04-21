package odm;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Object Has Value</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link odm.ObjectHasValue#getIndividual <em>Individual</em>}</li>
 *   <li>{@link odm.ObjectHasValue#getObjectPropertyExpression <em>Object Property Expression</em>}</li>
 *   <li>{@link odm.ObjectHasValue#getTag <em>Tag</em>}</li>
 * </ul>
 * </p>
 *
 * @see odm.OdmPackage#getObjectHasValue()
 * @model
 * @generated
 */
public interface ObjectHasValue extends Description {

    /**
	 * Returns the value of the '<em><b>Individual</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Individual</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Individual</em>' reference.
	 * @see #setIndividual(Individual)
	 * @see odm.OdmPackage#getObjectHasValue_Individual()
	 * @model required="true"
	 * @generated
	 */
    Individual getIndividual();

    /**
	 * Sets the value of the '{@link odm.ObjectHasValue#getIndividual <em>Individual</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Individual</em>' reference.
	 * @see #getIndividual()
	 * @generated
	 */
    void setIndividual(Individual value);

    /**
	 * Returns the value of the '<em><b>Object Property Expression</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Object Property Expression</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Object Property Expression</em>' reference.
	 * @see #setObjectPropertyExpression(ObjectPropertyExpression)
	 * @see odm.OdmPackage#getObjectHasValue_ObjectPropertyExpression()
	 * @model required="true"
	 * @generated
	 */
    ObjectPropertyExpression getObjectPropertyExpression();

    /**
	 * Sets the value of the '{@link odm.ObjectHasValue#getObjectPropertyExpression <em>Object Property Expression</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Object Property Expression</em>' reference.
	 * @see #getObjectPropertyExpression()
	 * @generated
	 */
    void setObjectPropertyExpression(ObjectPropertyExpression value);

    /**
	 * Returns the value of the '<em><b>Tag</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Tag</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Tag</em>' attribute.
	 * @see #setTag(String)
	 * @see odm.OdmPackage#getObjectHasValue_Tag()
	 * @model required="true"
	 * @generated
	 */
    String getTag();

    /**
	 * Sets the value of the '{@link odm.ObjectHasValue#getTag <em>Tag</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Tag</em>' attribute.
	 * @see #getTag()
	 * @generated
	 */
    void setTag(String value);
}
