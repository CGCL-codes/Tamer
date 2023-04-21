package de.fraunhofer.fokus.cttcn;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Block</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link de.fraunhofer.fokus.cttcn.Block#getTransitions <em>Transitions</em>}</li>
 *   <li>{@link de.fraunhofer.fokus.cttcn.Block#getStartTime <em>Start Time</em>}</li>
 *   <li>{@link de.fraunhofer.fokus.cttcn.Block#getStatements <em>Statements</em>}</li>
 * </ul>
 * </p>
 *
 * @see de.fraunhofer.fokus.cttcn.CttcnPackage#getBlock()
 * @model abstract="true"
 * @generated
 */
public interface Block extends Statement {

    /**
	 * Returns the value of the '<em><b>Transitions</b></em>' containment reference list.
	 * The list contents are of type {@link de.fraunhofer.fokus.cttcn.Transition}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Transitions</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Transitions</em>' containment reference list.
	 * @see de.fraunhofer.fokus.cttcn.CttcnPackage#getBlock_Transitions()
	 * @model containment="true"
	 * @generated
	 */
    EList<Transition> getTransitions();

    /**
	 * Returns the value of the '<em><b>Start Time</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Start Time</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Start Time</em>' attribute.
	 * @see #setStartTime(int)
	 * @see de.fraunhofer.fokus.cttcn.CttcnPackage#getBlock_StartTime()
	 * @model
	 * @generated
	 */
    int getStartTime();

    /**
	 * Sets the value of the '{@link de.fraunhofer.fokus.cttcn.Block#getStartTime <em>Start Time</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Start Time</em>' attribute.
	 * @see #getStartTime()
	 * @generated
	 */
    void setStartTime(int value);

    /**
	 * Returns the value of the '<em><b>Statements</b></em>' containment reference list.
	 * The list contents are of type {@link de.fraunhofer.fokus.cttcn.Statement}.
	 * It is bidirectional and its opposite is '{@link de.fraunhofer.fokus.cttcn.Statement#getBlock <em>Block</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Statements</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Statements</em>' containment reference list.
	 * @see de.fraunhofer.fokus.cttcn.CttcnPackage#getBlock_Statements()
	 * @see de.fraunhofer.fokus.cttcn.Statement#getBlock
	 * @model opposite="block" containment="true" required="true"
	 * @generated
	 */
    EList<Statement> getStatements();
}
