package fr.inria.uml4tst.papyrus.ocl4tst.ocl;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Invariant Exp CS</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link fr.inria.uml4tst.papyrus.ocl4tst.ocl.InvariantExpCS#getName <em>Name</em>}</li>
 *   <li>{@link fr.inria.uml4tst.papyrus.ocl4tst.ocl.InvariantExpCS#getOclExpression <em>Ocl Expression</em>}</li>
 * </ul>
 * </p>
 *
 * @see fr.inria.uml4tst.papyrus.ocl4tst.ocl.OclPackage#getInvariantExpCS()
 * @model
 * @generated
 */
public interface InvariantExpCS extends InvariantOrDefinitionCS {

    /**
	 * Returns the value of the '<em><b>Name</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Name</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Name</em>' containment reference.
	 * @see #setName(SimpleNameCS)
	 * @see fr.inria.uml4tst.papyrus.ocl4tst.ocl.OclPackage#getInvariantExpCS_Name()
	 * @model containment="true"
	 * @generated
	 */
    SimpleNameCS getName();

    /**
	 * Sets the value of the '{@link fr.inria.uml4tst.papyrus.ocl4tst.ocl.InvariantExpCS#getName <em>Name</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Name</em>' containment reference.
	 * @see #getName()
	 * @generated
	 */
    void setName(SimpleNameCS value);

    /**
	 * Returns the value of the '<em><b>Ocl Expression</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Ocl Expression</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Ocl Expression</em>' containment reference.
	 * @see #setOclExpression(OclExpressionCS)
	 * @see fr.inria.uml4tst.papyrus.ocl4tst.ocl.OclPackage#getInvariantExpCS_OclExpression()
	 * @model containment="true" required="true"
	 * @generated
	 */
    OclExpressionCS getOclExpression();

    /**
	 * Sets the value of the '{@link fr.inria.uml4tst.papyrus.ocl4tst.ocl.InvariantExpCS#getOclExpression <em>Ocl Expression</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Ocl Expression</em>' containment reference.
	 * @see #getOclExpression()
	 * @generated
	 */
    void setOclExpression(OclExpressionCS value);
}
