package SensorDataWebGui;

import org.eclipse.emf.ecore.EFactory;

/**
 * <!-- begin-user-doc -->
 * The <b>Factory</b> for the model.
 * It provides a create method for each non-abstract class of the model.
 * <!-- end-user-doc -->
 * @see SensorDataWebGui.SensorDataWebGuiPackage
 * @generated
 */
public interface SensorDataWebGuiFactory extends EFactory {

    /**
	 * The singleton instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    SensorDataWebGuiFactory eINSTANCE = SensorDataWebGui.impl.SensorDataWebGuiFactoryImpl.init();

    /**
	 * Returns a new object of class '<em>Standard Sensor Data Web</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Standard Sensor Data Web</em>'.
	 * @generated
	 */
    StandardSensorDataWeb createStandardSensorDataWeb();

    /**
	 * Returns a new object of class '<em>Tuple Window</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Tuple Window</em>'.
	 * @generated
	 */
    TupleWindow createTupleWindow();

    /**
	 * Returns a new object of class '<em>Time Window</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Time Window</em>'.
	 * @generated
	 */
    TimeWindow createTimeWindow();

    /**
	 * Returns a new object of class '<em>Fixed Window</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Fixed Window</em>'.
	 * @generated
	 */
    FixedWindow createFixedWindow();

    /**
	 * Returns a new object of class '<em>Processing Element</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Processing Element</em>'.
	 * @generated
	 */
    ProcessingElement createProcessingElement();

    /**
	 * Returns a new object of class '<em>Source</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Source</em>'.
	 * @generated
	 */
    Source createSource();

    /**
	 * Returns a new object of class '<em>Graph</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Graph</em>'.
	 * @generated
	 */
    Graph createGraph();

    /**
	 * Returns a new object of class '<em>Graph Reference</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Graph Reference</em>'.
	 * @generated
	 */
    GraphReference createGraphReference();

    /**
	 * Returns the package supported by this factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the package supported by this factory.
	 * @generated
	 */
    SensorDataWebGuiPackage getSensorDataWebGuiPackage();
}
