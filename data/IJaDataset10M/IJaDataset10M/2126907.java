package hu.cubussapiens.modembed.model.datatypes;

import hu.cubussapiens.modembed.model.assembly.InstructionSet;
import hu.cubussapiens.modembed.model.infrastructure.PackageElement;
import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Operation</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link hu.cubussapiens.modembed.model.datatypes.Operation#getParams <em>Params</em>}</li>
 *   <li>{@link hu.cubussapiens.modembed.model.datatypes.Operation#getWords <em>Words</em>}</li>
 *   <li>{@link hu.cubussapiens.modembed.model.datatypes.Operation#getInstructionset <em>Instructionset</em>}</li>
 * </ul>
 * </p>
 *
 * @see hu.cubussapiens.modembed.model.datatypes.DatatypesPackage#getOperation()
 * @model
 * @generated
 */
public interface Operation extends PackageElement {

    /**
	 * Returns the value of the '<em><b>Params</b></em>' containment reference list.
	 * The list contents are of type {@link hu.cubussapiens.modembed.model.datatypes.OpParam}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Params</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Params</em>' containment reference list.
	 * @see hu.cubussapiens.modembed.model.datatypes.DatatypesPackage#getOperation_Params()
	 * @model containment="true"
	 * @generated
	 */
    EList<OpParam> getParams();

    /**
	 * Returns the value of the '<em><b>Words</b></em>' containment reference list.
	 * The list contents are of type {@link hu.cubussapiens.modembed.model.datatypes.Step}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Words</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Words</em>' containment reference list.
	 * @see hu.cubussapiens.modembed.model.datatypes.DatatypesPackage#getOperation_Words()
	 * @model containment="true"
	 * @generated
	 */
    EList<Step> getWords();

    /**
	 * Returns the value of the '<em><b>Instructionset</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Instructionset</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Instructionset</em>' reference.
	 * @see #setInstructionset(InstructionSet)
	 * @see hu.cubussapiens.modembed.model.datatypes.DatatypesPackage#getOperation_Instructionset()
	 * @model
	 * @generated
	 */
    InstructionSet getInstructionset();

    /**
	 * Sets the value of the '{@link hu.cubussapiens.modembed.model.datatypes.Operation#getInstructionset <em>Instructionset</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Instructionset</em>' reference.
	 * @see #getInstructionset()
	 * @generated
	 */
    void setInstructionset(InstructionSet value);
}
