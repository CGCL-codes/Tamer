package hub.sam.lang.mf2b;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Function Call</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link hub.sam.lang.mf2b.FunctionCall#getFunction <em>Function</em>}</li>
 *   <li>{@link hub.sam.lang.mf2b.FunctionCall#getArguments <em>Arguments</em>}</li>
 * </ul>
 * </p>
 *
 * @see hub.sam.lang.mf2b.Mf2bPackage#getFunctionCall()
 * @model
 * @generated
 */
public interface FunctionCall extends Expression {

    /**
     * Returns the value of the '<em><b>Function</b></em>' reference.
     * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Function</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
     * @return the value of the '<em>Function</em>' reference.
     * @see #setFunction(Function)
     * @see hub.sam.lang.mf2b.Mf2bPackage#getFunctionCall_Function()
     * @model required="true"
     * @generated
     */
    Function getFunction();

    /**
     * Sets the value of the '{@link hub.sam.lang.mf2b.FunctionCall#getFunction <em>Function</em>}' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Function</em>' reference.
     * @see #getFunction()
     * @generated
     */
    void setFunction(Function value);

    /**
     * Returns the value of the '<em><b>Arguments</b></em>' containment reference list.
     * The list contents are of type {@link hub.sam.lang.mf2b.Expression}.
     * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Arguments</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
     * @return the value of the '<em>Arguments</em>' containment reference list.
     * @see hub.sam.lang.mf2b.Mf2bPackage#getFunctionCall_Arguments()
     * @model containment="true"
     * @generated
     */
    EList<Expression> getArguments();
}
