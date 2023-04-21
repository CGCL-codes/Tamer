package iec61970.core;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Equipment</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * The parts of a power system that are physical devices, electronic or mechanical
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link iec61970.core.Equipment#getMemberOf_EquipmentContainer <em>Member Of Equipment Container</em>}</li>
 * </ul>
 * </p>
 *
 * @see iec61970.core.CorePackage#getEquipment()
 * @model
 * @generated
 */
public interface Equipment extends PowerSystemResource {

    /**
	 * Returns the value of the '<em><b>Member Of Equipment Container</b></em>' container reference.
	 * It is bidirectional and its opposite is '{@link iec61970.core.EquipmentContainer#getContains_Equipments <em>Contains Equipments</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * The association is used in the naming hierarchy.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Member Of Equipment Container</em>' container reference.
	 * @see #setMemberOf_EquipmentContainer(EquipmentContainer)
	 * @see iec61970.core.CorePackage#getEquipment_MemberOf_EquipmentContainer()
	 * @see iec61970.core.EquipmentContainer#getContains_Equipments
	 * @model opposite="Contains_Equipments" transient="false"
	 * @generated
	 */
    EquipmentContainer getMemberOf_EquipmentContainer();

    /**
	 * Sets the value of the '{@link iec61970.core.Equipment#getMemberOf_EquipmentContainer <em>Member Of Equipment Container</em>}' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Member Of Equipment Container</em>' container reference.
	 * @see #getMemberOf_EquipmentContainer()
	 * @generated
	 */
    void setMemberOf_EquipmentContainer(EquipmentContainer value);
}
