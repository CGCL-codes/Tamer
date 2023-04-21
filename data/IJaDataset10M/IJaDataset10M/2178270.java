package SensorDataWeb.impl;

import SensorDataWeb.*;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.impl.EFactoryImpl;
import org.eclipse.emf.ecore.plugin.EcorePlugin;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Factory</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class SensorDataWebFactoryImpl extends EFactoryImpl implements SensorDataWebFactory {

    /**
	 * Creates the default factory implementation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public static SensorDataWebFactory init() {
        try {
            SensorDataWebFactory theSensorDataWebFactory = (SensorDataWebFactory) EPackage.Registry.INSTANCE.getEFactory("SensorDataWeb");
            if (theSensorDataWebFactory != null) {
                return theSensorDataWebFactory;
            }
        } catch (Exception exception) {
            EcorePlugin.INSTANCE.log(exception);
        }
        return new SensorDataWebFactoryImpl();
    }

    /**
	 * Creates an instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public SensorDataWebFactoryImpl() {
        super();
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    public EObject create(EClass eClass) {
        switch(eClass.getClassifierID()) {
            case SensorDataWebPackage.STANDARD_SENSOR_DATA_WEB:
                return createStandardSensorDataWeb();
            case SensorDataWebPackage.SOURCE:
                return createSource();
            case SensorDataWebPackage.PROCESSING_ELEMENT:
                return createProcessingElement();
            case SensorDataWebPackage.TUPLE_WINDOW:
                return createTupleWindow();
            case SensorDataWebPackage.TIME_WINDOW:
                return createTimeWindow();
            default:
                throw new IllegalArgumentException("The class '" + eClass.getName() + "' is not a valid classifier");
        }
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    public Object createFromString(EDataType eDataType, String initialValue) {
        switch(eDataType.getClassifierID()) {
            case SensorDataWebPackage.TRIGGER_TYPE:
                return createTriggerTypeFromString(eDataType, initialValue);
            default:
                throw new IllegalArgumentException("The datatype '" + eDataType.getName() + "' is not a valid classifier");
        }
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    public String convertToString(EDataType eDataType, Object instanceValue) {
        switch(eDataType.getClassifierID()) {
            case SensorDataWebPackage.TRIGGER_TYPE:
                return convertTriggerTypeToString(eDataType, instanceValue);
            default:
                throw new IllegalArgumentException("The datatype '" + eDataType.getName() + "' is not a valid classifier");
        }
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public StandardSensorDataWeb createStandardSensorDataWeb() {
        StandardSensorDataWebImpl standardSensorDataWeb = new StandardSensorDataWebImpl();
        return standardSensorDataWeb;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public Source createSource() {
        SourceImpl source = new SourceImpl();
        return source;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public ProcessingElement createProcessingElement() {
        ProcessingElementImpl processingElement = new ProcessingElementImpl();
        return processingElement;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public TupleWindow createTupleWindow() {
        TupleWindowImpl tupleWindow = new TupleWindowImpl();
        return tupleWindow;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public TimeWindow createTimeWindow() {
        TimeWindowImpl timeWindow = new TimeWindowImpl();
        return timeWindow;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public TriggerType createTriggerTypeFromString(EDataType eDataType, String initialValue) {
        TriggerType result = TriggerType.get(initialValue);
        if (result == null) throw new IllegalArgumentException("The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'");
        return result;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public String convertTriggerTypeToString(EDataType eDataType, Object instanceValue) {
        return instanceValue == null ? null : instanceValue.toString();
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public SensorDataWebPackage getSensorDataWebPackage() {
        return (SensorDataWebPackage) getEPackage();
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @deprecated
	 * @generated
	 */
    @Deprecated
    public static SensorDataWebPackage getPackage() {
        return SensorDataWebPackage.eINSTANCE;
    }
}
