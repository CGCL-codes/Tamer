package de.fraunhofer.isst.eastadl.hardwaremodeling;

import org.eclipse.emf.common.util.EList;
import de.fraunhofer.isst.eastadl.elements.EAElement;

/**
 * <!-- BEGIN-EAST-ADL2-SPEC -->
 * <strong>LogicalBus</strong> (from HardwareModeling) �atpStructuredElement�
 * <p>
 * <strong>Generalizations</strong>
 * <br> AllocationTarget (from HardwareModeling)
 * <br> EAElement (from Elements)
 * <p>
 * <strong>Description</strong>
 * <br> The LogicalBus represents logical communication channels. It serves as an allocation target for
 *      connectors, i.e. the data exchanged between functions in the FunctionalDesignArchitecture.
 * <p>
 * <strong>Attributes</strong>
 * <br> busSpeed : Float [1]
 * <br> - The net bus speed in bits per second. Used to assess communication delay and
 *        schedulability on the bus. Note that scheduling details are not represented in the model.
 * <br> busType : LogicalBusKind [1]
 * <br> - The type of bus scheduling assumed.
 * <p>
 * <strong>Associations</strong>
 * <br> No additional associations
 * <p>
 * <strong>Dependencies</strong>
 * <br> wire : HardwareConnector [*]
 * <br> �instanceRef�
 * <p>
 * <strong>Constraints</strong>
 * <br> No additional constraints
 * <p>
 * <strong>Semantics</strong>
 * <br> The LogicalBus represents a logical connection that carries data from any sender to all receivers.
 *      Senders and receivers are identified by the wires of the LogicalBus, i.e. the associated
 *      HardwareConnectors. The available busSpeed represents the maximum amount of useful data
 *      that can be carried. The busSpeed has already deducted speed reduction resulting from frame
 *      overhead, timing effects, etc.
 * <!-- END-EAST-ADL2-SPEC -->
 * 
 * @author dprenzel
 *
 * @model
 */
public interface LogicalBus extends AllocationTarget, EAElement {

    /**
	 * Returns the value of the '<em><b>Bus Speed</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * <strong>busSpeed : Float [1]</strong>
	 * <br> The net bus speed in bits per second. Used to assess communication delay and
	 *      schedulability on the bus. Note that scheduling details are not represented in the model.
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Bus Speed</em>' attribute.
	 * @see #setBusSpeed(float)
	 * @see de.fraunhofer.isst.eastadl.hardwaremodeling.HardwaremodelPackage#getLogicalBus_BusSpeed()
	 * @model required="true"
	 */
    float getBusSpeed();

    /**
	 * Sets the value of the '{@link de.fraunhofer.isst.eastadl.hardwaremodeling.LogicalBus#getBusSpeed <em>Bus Speed</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * <strong>busSpeed : Float [1]</strong>
	 * <br> The net bus speed in bits per second. Used to assess communication delay and
	 *      schedulability on the bus. Note that scheduling details are not represented in the model.
	 * </p>
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Bus Speed</em>' attribute.
	 * @see #getBusSpeed()
	 * @generated
	 */
    void setBusSpeed(float value);

    /**
	 * Returns the value of the '<em><b>Bus Type</b></em>' attribute.
	 * The literals are from the enumeration {@link de.fraunhofer.isst.eastadl.hardwaremodeling.LogicalBusKind}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * <strong>busType : LogicalBusKind [1]</strong>
	 * <br> The type of bus scheduling assumed.
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Bus Type</em>' attribute.
	 * @see de.fraunhofer.isst.eastadl.hardwaremodeling.LogicalBusKind
	 * @see #setBusType(LogicalBusKind)
	 * @see de.fraunhofer.isst.eastadl.hardwaremodeling.HardwaremodelPackage#getLogicalBus_BusType()
	 * @model required="true"
	 */
    LogicalBusKind getBusType();

    /**
	 * Sets the value of the '{@link de.fraunhofer.isst.eastadl.hardwaremodeling.LogicalBus#getBusType <em>Bus Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * <strong>busType : LogicalBusKind [1]</strong>
	 * <br> The type of bus scheduling assumed.
	 * </p>
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Bus Type</em>' attribute.
	 * @see de.fraunhofer.isst.eastadl.hardwaremodeling.LogicalBusKind
	 * @see #getBusType()
	 * @generated
	 */
    void setBusType(LogicalBusKind value);

    /**
	 * Returns the value of the '<em><b>Wire</b></em>' reference list.
	 * The list contents are of type {@link de.fraunhofer.isst.eastadl.hardwaremodeling.HardwareConnector}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * <strong>wire : HardwareConnector [*]</strong>
	 * <br> �instanceRef�
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Wire</em>' reference list.
	 * @see de.fraunhofer.isst.eastadl.hardwaremodeling.HardwaremodelPackage#getLogicalBus_Wire()
	 * @model
	 */
    EList<HardwareConnectorInstanceRef> getWire();
}
