package SBVR;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Logical Operation</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link SBVR.LogicalOperation#getLogicalOperand <em>Logical Operand</em>}</li>
 * </ul>
 * </p>
 *
 * @see SBVR.SBVRPackage#getLogicalOperation()
 * @model
 * @generated
 */
public interface LogicalOperation extends LogicalFormulation {

    /**
	 * Returns the value of the '<em><b>Logical Operand</b></em>' reference list.
	 * The list contents are of type {@link SBVR.LogicalFormulation}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Logical Operand</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Logical Operand</em>' reference list.
	 * @see SBVR.SBVRPackage#getLogicalOperation_LogicalOperand()
	 * @model required="true"
	 * @generated
	 */
    EList<LogicalFormulation> getLogicalOperand();
}
