package furniture;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Value Ref</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link furniture.ValueRef#getRef <em>Ref</em>}</li>
 * </ul>
 * </p>
 *
 * @see furniture.FurniturePackage#getValueRef()
 * @model
 * @generated
 */
public interface ValueRef extends Value {

    /**
	 * Returns the value of the '<em><b>Ref</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Ref</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Ref</em>' reference.
	 * @see #setRef(NamedValue)
	 * @see furniture.FurniturePackage#getValueRef_Ref()
	 * @model
	 * @generated
	 */
    NamedValue getRef();

    /**
	 * Sets the value of the '{@link furniture.ValueRef#getRef <em>Ref</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Ref</em>' reference.
	 * @see #getRef()
	 * @generated
	 */
    void setRef(NamedValue value);
}
