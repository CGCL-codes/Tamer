package de.fraunhofer.isst.eastadl.hardwaremodeling;

import de.fraunhofer.isst.eastadl.elements.EAElement;

/**
 * <!-- BEGIN-EAST-ADL2-SPEC -->
 * <strong>HardwareComponentPrototype</strong> (from HardwareModeling) �atpPrototype�
 * <p>
 * <strong>Generalizations</strong>
 * <br> EAElement (from Elements)
 * <br> AllocationTarget (from HardwareModeling)
 * <p>
 * <strong>Description</strong>
 * <br> Appears as part of a HardwareComponentType and is itself typed by a HardwareComponentType.
 *      This allows for a reference to the occurrence of a HardwareComponentType when it acts as a
 *      part. The purpose is to support the definition of hierarchical structures, and to reuse the same type
 *      of Hardware at several places. For example, a wheel speed sensor may occur at all four wheels,
 *      but it has a single definition.
 * <p>
 * <strong>Attributes</strong>
 * <br> No additional attributes
 * <p>
 * <strong>Associations</strong>
 * <br> type : HardwareComponentType [1]
 *      �isOfType�
 * <p>
 * <strong>Constraints</strong>
 * <br> No additional constraints
 * <p>
 * <strong>Semantics</strong>
 * <br> The HardwareComponentPrototype represents an occurrence of a hardware element, according to
 *      the type of the HardwareComponentPrototype.
 * <!-- END-EAST-ADL2-SPEC -->
 * 
 * @author dprenzel
 *
 * @model
 */
public interface HardwareComponentPrototype extends EAElement, AllocationTarget {

    /**
	 * Returns the value of the '<em><b>Type</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * <strong>type : HardwareComponentType [1]</strong>
	 * <br> �isOfType�
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Type</em>' reference.
	 * @see #setType(HardwareComponentType)
	 * @see de.fraunhofer.isst.eastadl.hardwaremodeling.HardwaremodelPackage#getHardwareComponentPrototype_Type()
	 * @model required="true"
	 */
    HardwareComponentType getType();

    /**
	 * Sets the value of the '{@link de.fraunhofer.isst.eastadl.hardwaremodeling.HardwareComponentPrototype#getType <em>Type</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * <strong>type : HardwareComponentType [1]</strong>
	 * <br> �isOfType�
	 * </p>
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Type</em>' reference.
	 * @see #getType()
	 * @generated
	 */
    void setType(HardwareComponentType value);
}
