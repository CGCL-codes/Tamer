package iec61970.core;

import org.eclipse.emf.ecore.EFactory;

/**
 * <!-- begin-user-doc -->
 * The <b>Factory</b> for the model.
 * It provides a create method for each non-abstract class of the model.
 * <!-- end-user-doc -->
 * @see iec61970.core.CorePackage
 * @generated
 */
public interface CoreFactory extends EFactory {

    /**
	 * The singleton instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    CoreFactory eINSTANCE = iec61970.core.impl.CoreFactoryImpl.init();

    /**
	 * Returns a new object of class '<em>Bay</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Bay</em>'.
	 * @generated
	 */
    Bay createBay();

    /**
	 * Returns a new object of class '<em>Company</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Company</em>'.
	 * @generated
	 */
    Company createCompany();

    /**
	 * Returns a new object of class '<em>Conducting Equipment</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Conducting Equipment</em>'.
	 * @generated
	 */
    ConductingEquipment createConductingEquipment();

    /**
	 * Returns a new object of class '<em>Power System Resource</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Power System Resource</em>'.
	 * @generated
	 */
    PowerSystemResource createPowerSystemResource();

    /**
	 * Returns a new object of class '<em>Substation</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Substation</em>'.
	 * @generated
	 */
    Substation createSubstation();

    /**
	 * Returns a new object of class '<em>Terminal</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Terminal</em>'.
	 * @generated
	 */
    Terminal createTerminal();

    /**
	 * Returns a new object of class '<em>Sub Control Area</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Sub Control Area</em>'.
	 * @generated
	 */
    SubControlArea createSubControlArea();

    /**
	 * Returns a new object of class '<em>Curve</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Curve</em>'.
	 * @generated
	 */
    Curve createCurve();

    /**
	 * Returns a new object of class '<em>Curve Data</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Curve Data</em>'.
	 * @generated
	 */
    CurveData createCurveData();

    /**
	 * Returns a new object of class '<em>Base Voltage</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Base Voltage</em>'.
	 * @generated
	 */
    BaseVoltage createBaseVoltage();

    /**
	 * Returns a new object of class '<em>Base Power</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Base Power</em>'.
	 * @generated
	 */
    BasePower createBasePower();

    /**
	 * Returns a new object of class '<em>Voltage Level</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Voltage Level</em>'.
	 * @generated
	 */
    VoltageLevel createVoltageLevel();

    /**
	 * Returns a new object of class '<em>Identified Object</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Identified Object</em>'.
	 * @generated
	 */
    IdentifiedObject createIdentifiedObject();

    /**
	 * Returns a new object of class '<em>Equipment</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Equipment</em>'.
	 * @generated
	 */
    Equipment createEquipment();

    /**
	 * Returns a new object of class '<em>Unit</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Unit</em>'.
	 * @generated
	 */
    Unit createUnit();

    /**
	 * Returns a new object of class '<em>Equipment Container</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Equipment Container</em>'.
	 * @generated
	 */
    EquipmentContainer createEquipmentContainer();

    /**
	 * Returns a new object of class '<em>PSR Type</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>PSR Type</em>'.
	 * @generated
	 */
    PSRType createPSRType();

    /**
	 * Returns a new object of class '<em>Sub Geographical Region</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Sub Geographical Region</em>'.
	 * @generated
	 */
    SubGeographicalRegion createSubGeographicalRegion();

    /**
	 * Returns a new object of class '<em>Geographical Region</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Geographical Region</em>'.
	 * @generated
	 */
    GeographicalRegion createGeographicalRegion();

    /**
	 * Returns a new object of class '<em>Basic Interval Schedule</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Basic Interval Schedule</em>'.
	 * @generated
	 */
    BasicIntervalSchedule createBasicIntervalSchedule();

    /**
	 * Returns a new object of class '<em>Regular Interval Schedule</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Regular Interval Schedule</em>'.
	 * @generated
	 */
    RegularIntervalSchedule createRegularIntervalSchedule();

    /**
	 * Returns a new object of class '<em>Irregular Interval Schedule</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Irregular Interval Schedule</em>'.
	 * @generated
	 */
    IrregularIntervalSchedule createIrregularIntervalSchedule();

    /**
	 * Returns a new object of class '<em>Regular Time Point</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Regular Time Point</em>'.
	 * @generated
	 */
    RegularTimePoint createRegularTimePoint();

    /**
	 * Returns a new object of class '<em>Irregular Time Point</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Irregular Time Point</em>'.
	 * @generated
	 */
    IrregularTimePoint createIrregularTimePoint();

    /**
	 * Returns a new object of class '<em>Modeling Authority Set</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Modeling Authority Set</em>'.
	 * @generated
	 */
    ModelingAuthoritySet createModelingAuthoritySet();

    /**
	 * Returns a new object of class '<em>Modeling Authority</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Modeling Authority</em>'.
	 * @generated
	 */
    ModelingAuthority createModelingAuthority();

    /**
	 * Returns a new object of class '<em>Version</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Version</em>'.
	 * @generated
	 */
    CoreVersion createCoreVersion();

    /**
	 * Returns a new object of class '<em>Connectivity Node Container</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Connectivity Node Container</em>'.
	 * @generated
	 */
    ConnectivityNodeContainer createConnectivityNodeContainer();

    /**
	 * Returns the package supported by this factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the package supported by this factory.
	 * @generated
	 */
    CorePackage getCorePackage();
}
