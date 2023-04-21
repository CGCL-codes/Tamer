package de.fraunhofer.isst.eastadl.hardwaremodeling;

/**
 * <!-- BEGIN-EAST-ADL2-SPEC -->
 * <strong>Sensor</strong> (from HardwareModeling)
 * <p>
 * <strong>Generalizations</strong>
 * <br> HardwareComponentType (from HardwareModeling)
 * <p>
 * <strong>Description</strong>
 * <br> Sensor represents a hardware entity for digital or analog sensor elements. The Sensor is
 *      connected electrically to the electrical entities of the Hardware Design Architecture.
 * <p>
 * <strong>Attributes</strong>
 * <br> No additional attributes
 * <p>
 * <strong>Associations</strong>
 * <br> No additional associations
 * <p>
 * <strong>Constraints</strong>
 * <br> No additional constraints
 * <p>
 * <strong>Semantics</strong>
 * <br> Sensor denotes an electrical sensor. The Sensor represents the physical and electrical aspects of
 *      sensor hardware. The logical aspect is represented by an HWFunctionType associated with the Sensor.
 * <!-- END-EAST-ADL2-SPEC -->
 * 
 * @author dprenzel
 *
 * @model
 */
public interface Sensor extends HardwareComponentType {
}
