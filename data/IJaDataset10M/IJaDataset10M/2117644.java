package assign;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Copy Object</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link assign.CopyObject#getObject <em>Object</em>}</li>
 * </ul>
 * </p>
 *
 * @see assign.AssignPackage#getCopyObject()
 * @model
 * @generated
 */
public interface CopyObject extends Expression {

    /**
	 * Returns the value of the '<em><b>Object</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Object</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Object</em>' containment reference.
	 * @see #setObject(Expression)
	 * @see assign.AssignPackage#getCopyObject_Object()
	 * @model containment="true" required="true"
	 * @generated
	 */
    Expression getObject();

    /**
	 * Sets the value of the '{@link assign.CopyObject#getObject <em>Object</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Object</em>' containment reference.
	 * @see #getObject()
	 * @generated
	 */
    void setObject(Expression value);
}
