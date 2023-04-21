package eu.medeia.ecore.apmm.esm;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Physical Environment</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link eu.medeia.ecore.apmm.esm.PhysicalEnvironment#getInputPorts <em>Input Ports</em>}</li>
 *   <li>{@link eu.medeia.ecore.apmm.esm.PhysicalEnvironment#getCommunicationPorts <em>Communication Ports</em>}</li>
 *   <li>{@link eu.medeia.ecore.apmm.esm.PhysicalEnvironment#getSlots <em>Slots</em>}</li>
 *   <li>{@link eu.medeia.ecore.apmm.esm.PhysicalEnvironment#getMemory <em>Memory</em>}</li>
 *   <li>{@link eu.medeia.ecore.apmm.esm.PhysicalEnvironment#getProcessors <em>Processors</em>}</li>
 *   <li>{@link eu.medeia.ecore.apmm.esm.PhysicalEnvironment#getOutputPorts <em>Output Ports</em>}</li>
 * </ul>
 * </p>
 *
 * @see eu.medeia.ecore.apmm.esm.EsmPackage#getPhysicalEnvironment()
 * @model
 * @generated
 */
public interface PhysicalEnvironment extends EObject {

    /**
	 * Returns the value of the '<em><b>Input Ports</b></em>' containment reference list.
	 * The list contents are of type {@link eu.medeia.ecore.apmm.esm.InputPort}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Input Ports</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Input Ports</em>' containment reference list.
	 * @see eu.medeia.ecore.apmm.esm.EsmPackage#getPhysicalEnvironment_InputPorts()
	 * @model containment="true"
	 * @generated
	 */
    EList<InputPort> getInputPorts();

    /**
	 * Returns the value of the '<em><b>Communication Ports</b></em>' containment reference list.
	 * The list contents are of type {@link eu.medeia.ecore.apmm.esm.CommunicationPort}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Communication Ports</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Communication Ports</em>' containment reference list.
	 * @see eu.medeia.ecore.apmm.esm.EsmPackage#getPhysicalEnvironment_CommunicationPorts()
	 * @model containment="true"
	 * @generated
	 */
    EList<CommunicationPort> getCommunicationPorts();

    /**
	 * Returns the value of the '<em><b>Slots</b></em>' containment reference list.
	 * The list contents are of type {@link eu.medeia.ecore.apmm.esm.Slot}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Slots</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Slots</em>' containment reference list.
	 * @see eu.medeia.ecore.apmm.esm.EsmPackage#getPhysicalEnvironment_Slots()
	 * @model containment="true"
	 * @generated
	 */
    EList<Slot> getSlots();

    /**
	 * Returns the value of the '<em><b>Memory</b></em>' containment reference list.
	 * The list contents are of type {@link eu.medeia.ecore.apmm.esm.Memory}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Memory</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Memory</em>' containment reference list.
	 * @see eu.medeia.ecore.apmm.esm.EsmPackage#getPhysicalEnvironment_Memory()
	 * @model containment="true"
	 * @generated
	 */
    EList<Memory> getMemory();

    /**
	 * Returns the value of the '<em><b>Processors</b></em>' containment reference list.
	 * The list contents are of type {@link eu.medeia.ecore.apmm.esm.Processor}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Processors</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Processors</em>' containment reference list.
	 * @see eu.medeia.ecore.apmm.esm.EsmPackage#getPhysicalEnvironment_Processors()
	 * @model containment="true"
	 * @generated
	 */
    EList<Processor> getProcessors();

    /**
	 * Returns the value of the '<em><b>Output Ports</b></em>' containment reference list.
	 * The list contents are of type {@link eu.medeia.ecore.apmm.esm.OutputPort}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Output Ports</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Output Ports</em>' containment reference list.
	 * @see eu.medeia.ecore.apmm.esm.EsmPackage#getPhysicalEnvironment_OutputPorts()
	 * @model containment="true"
	 * @generated
	 */
    EList<OutputPort> getOutputPorts();
}
