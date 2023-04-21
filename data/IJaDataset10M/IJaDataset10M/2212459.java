package hu.cubussapiens.modembed.model.lowlevelprogram;

import hu.cubussapiens.modembed.model.datatypes.Operation;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Operation Call</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link hu.cubussapiens.modembed.model.lowlevelprogram.OperationCall#getOperation <em>Operation</em>}</li>
 *   <li>{@link hu.cubussapiens.modembed.model.lowlevelprogram.OperationCall#getParameters <em>Parameters</em>}</li>
 * </ul>
 * </p>
 *
 * @see hu.cubussapiens.modembed.model.lowlevelprogram.LowLevelProgramPackage#getOperationCall()
 * @model
 * @generated
 */
public interface OperationCall extends EObject {

    /**
	 * Returns the value of the '<em><b>Operation</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Operation</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Operation</em>' reference.
	 * @see #setOperation(Operation)
	 * @see hu.cubussapiens.modembed.model.lowlevelprogram.LowLevelProgramPackage#getOperationCall_Operation()
	 * @model
	 * @generated
	 */
    Operation getOperation();

    /**
	 * Sets the value of the '{@link hu.cubussapiens.modembed.model.lowlevelprogram.OperationCall#getOperation <em>Operation</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Operation</em>' reference.
	 * @see #getOperation()
	 * @generated
	 */
    void setOperation(Operation value);

    /**
	 * Returns the value of the '<em><b>Parameters</b></em>' containment reference list.
	 * The list contents are of type {@link hu.cubussapiens.modembed.model.lowlevelprogram.ParameterMapping}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Parameters</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Parameters</em>' containment reference list.
	 * @see hu.cubussapiens.modembed.model.lowlevelprogram.LowLevelProgramPackage#getOperationCall_Parameters()
	 * @model containment="true"
	 * @generated
	 */
    EList<ParameterMapping> getParameters();
}
