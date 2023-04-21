package fr.msimeon.mads.mads;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Variable Reference</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link fr.msimeon.mads.mads.VariableReference#getVarRef <em>Var Ref</em>}</li>
 * </ul>
 * </p>
 *
 * @see fr.msimeon.mads.mads.MadsPackage#getVariableReference()
 * @model
 * @generated
 */
public interface VariableReference extends AtomID {

    /**
   * Returns the value of the '<em><b>Var Ref</b></em>' reference.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Var Ref</em>' reference isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Var Ref</em>' reference.
   * @see #setVarRef(Variable)
   * @see fr.msimeon.mads.mads.MadsPackage#getVariableReference_VarRef()
   * @model
   * @generated
   */
    Variable getVarRef();

    /**
   * Sets the value of the '{@link fr.msimeon.mads.mads.VariableReference#getVarRef <em>Var Ref</em>}' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Var Ref</em>' reference.
   * @see #getVarRef()
   * @generated
   */
    void setVarRef(Variable value);
}
