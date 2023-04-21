package iec61970.wires;

import iec61970.core.RegularIntervalSchedule;
import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Regulation Schedule</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * A pre-established pattern over time for a controlled variable, e.g., busbar voltage.
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link iec61970.wires.RegulationSchedule#getLineDropCompensation <em>Line Drop Compensation</em>}</li>
 *   <li>{@link iec61970.wires.RegulationSchedule#getLineDropR <em>Line Drop R</em>}</li>
 *   <li>{@link iec61970.wires.RegulationSchedule#getLineDropX <em>Line Drop X</em>}</li>
 *   <li>{@link iec61970.wires.RegulationSchedule#getVoltageControlZones <em>Voltage Control Zones</em>}</li>
 *   <li>{@link iec61970.wires.RegulationSchedule#getRegulatingCondEqs <em>Regulating Cond Eqs</em>}</li>
 *   <li>{@link iec61970.wires.RegulationSchedule#getTapChangers <em>Tap Changers</em>}</li>
 * </ul>
 * </p>
 *
 * @see iec61970.wires.WiresPackage#getRegulationSchedule()
 * @model
 * @generated
 */
public interface RegulationSchedule extends RegularIntervalSchedule {

    /**
	 * Returns the value of the '<em><b>Line Drop Compensation</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Flag to indicate that line drop compensation is to be applied
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Line Drop Compensation</em>' attribute.
	 * @see #setLineDropCompensation(String)
	 * @see iec61970.wires.WiresPackage#getRegulationSchedule_LineDropCompensation()
	 * @model dataType="iec61970.domain.Boolean"
	 * @generated
	 */
    String getLineDropCompensation();

    /**
	 * Sets the value of the '{@link iec61970.wires.RegulationSchedule#getLineDropCompensation <em>Line Drop Compensation</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Line Drop Compensation</em>' attribute.
	 * @see #getLineDropCompensation()
	 * @generated
	 */
    void setLineDropCompensation(String value);

    /**
	 * Returns the value of the '<em><b>Line Drop R</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Line drop resistance.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Line Drop R</em>' attribute.
	 * @see #setLineDropR(String)
	 * @see iec61970.wires.WiresPackage#getRegulationSchedule_LineDropR()
	 * @model dataType="iec61970.domain.Resistance"
	 * @generated
	 */
    String getLineDropR();

    /**
	 * Sets the value of the '{@link iec61970.wires.RegulationSchedule#getLineDropR <em>Line Drop R</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Line Drop R</em>' attribute.
	 * @see #getLineDropR()
	 * @generated
	 */
    void setLineDropR(String value);

    /**
	 * Returns the value of the '<em><b>Line Drop X</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Line drop reactance.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Line Drop X</em>' attribute.
	 * @see #setLineDropX(String)
	 * @see iec61970.wires.WiresPackage#getRegulationSchedule_LineDropX()
	 * @model dataType="iec61970.domain.Reactance"
	 * @generated
	 */
    String getLineDropX();

    /**
	 * Sets the value of the '{@link iec61970.wires.RegulationSchedule#getLineDropX <em>Line Drop X</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Line Drop X</em>' attribute.
	 * @see #getLineDropX()
	 * @generated
	 */
    void setLineDropX(String value);

    /**
	 * Returns the value of the '<em><b>Voltage Control Zones</b></em>' reference list.
	 * The list contents are of type {@link iec61970.wires.VoltageControlZone}.
	 * It is bidirectional and its opposite is '{@link iec61970.wires.VoltageControlZone#getRegulationSchedule <em>Regulation Schedule</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * A VoltageControlZone may have a  voltage regulation schedule.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Voltage Control Zones</em>' reference list.
	 * @see iec61970.wires.WiresPackage#getRegulationSchedule_VoltageControlZones()
	 * @see iec61970.wires.VoltageControlZone#getRegulationSchedule
	 * @model opposite="RegulationSchedule"
	 * @generated
	 */
    EList<VoltageControlZone> getVoltageControlZones();

    /**
	 * Returns the value of the '<em><b>Regulating Cond Eqs</b></em>' reference list.
	 * The list contents are of type {@link iec61970.wires.RegulatingCondEq}.
	 * It is bidirectional and its opposite is '{@link iec61970.wires.RegulatingCondEq#getRegulationSchedule <em>Regulation Schedule</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * A regulating class may have a voltage regulation schedule.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Regulating Cond Eqs</em>' reference list.
	 * @see iec61970.wires.WiresPackage#getRegulationSchedule_RegulatingCondEqs()
	 * @see iec61970.wires.RegulatingCondEq#getRegulationSchedule
	 * @model opposite="RegulationSchedule"
	 * @generated
	 */
    EList<RegulatingCondEq> getRegulatingCondEqs();

    /**
	 * Returns the value of the '<em><b>Tap Changers</b></em>' reference list.
	 * The list contents are of type {@link iec61970.wires.TapChanger}.
	 * It is bidirectional and its opposite is '{@link iec61970.wires.TapChanger#getRegulationSchedule <em>Regulation Schedule</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * An LTC may have a regulation schedule.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Tap Changers</em>' reference list.
	 * @see iec61970.wires.WiresPackage#getRegulationSchedule_TapChangers()
	 * @see iec61970.wires.TapChanger#getRegulationSchedule
	 * @model opposite="RegulationSchedule"
	 * @generated
	 */
    EList<TapChanger> getTapChangers();
}
