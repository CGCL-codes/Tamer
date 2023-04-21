package de.fraunhofer.fokus.cttcn;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Guarded Trans</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link de.fraunhofer.fokus.cttcn.GuardedTrans#getGuard <em>Guard</em>}</li>
 * </ul>
 * </p>
 *
 * @see de.fraunhofer.fokus.cttcn.CttcnPackage#getGuardedTrans()
 * @model
 * @generated
 */
public interface GuardedTrans extends Trans {

    /**
	 * Returns the value of the '<em><b>Guard</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Guard</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Guard</em>' containment reference.
	 * @see #setGuard(Guard)
	 * @see de.fraunhofer.fokus.cttcn.CttcnPackage#getGuardedTrans_Guard()
	 * @model containment="true" required="true"
	 * @generated
	 */
    Guard getGuard();

    /**
	 * Sets the value of the '{@link de.fraunhofer.fokus.cttcn.GuardedTrans#getGuard <em>Guard</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Guard</em>' containment reference.
	 * @see #getGuard()
	 * @generated
	 */
    void setGuard(Guard value);
}
