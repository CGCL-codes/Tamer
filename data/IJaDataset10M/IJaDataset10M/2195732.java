package hu.cubussapiens.modembed.notation.implementation.atomicImplementationNotation;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>NVariable</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link hu.cubussapiens.modembed.notation.implementation.atomicImplementationNotation.NVariable#getType <em>Type</em>}</li>
 *   <li>{@link hu.cubussapiens.modembed.notation.implementation.atomicImplementationNotation.NVariable#getName <em>Name</em>}</li>
 * </ul>
 * </p>
 *
 * @see hu.cubussapiens.modembed.notation.implementation.atomicImplementationNotation.AtomicImplementationNotationPackage#getNVariable()
 * @model
 * @generated
 */
public interface NVariable extends NImplStep {

    /**
   * Returns the value of the '<em><b>Type</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Type</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Type</em>' attribute.
   * @see #setType(String)
   * @see hu.cubussapiens.modembed.notation.implementation.atomicImplementationNotation.AtomicImplementationNotationPackage#getNVariable_Type()
   * @model
   * @generated
   */
    String getType();

    /**
   * Sets the value of the '{@link hu.cubussapiens.modembed.notation.implementation.atomicImplementationNotation.NVariable#getType <em>Type</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Type</em>' attribute.
   * @see #getType()
   * @generated
   */
    void setType(String value);

    /**
   * Returns the value of the '<em><b>Name</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Name</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Name</em>' attribute.
   * @see #setName(String)
   * @see hu.cubussapiens.modembed.notation.implementation.atomicImplementationNotation.AtomicImplementationNotationPackage#getNVariable_Name()
   * @model
   * @generated
   */
    String getName();

    /**
   * Sets the value of the '{@link hu.cubussapiens.modembed.notation.implementation.atomicImplementationNotation.NVariable#getName <em>Name</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Name</em>' attribute.
   * @see #getName()
   * @generated
   */
    void setName(String value);
}
