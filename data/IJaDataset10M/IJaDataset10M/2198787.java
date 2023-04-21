package eu.medeia.ecore.bm.impl;

import eu.medeia.ecore.bm.*;
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
public class BmFactoryImpl extends EFactoryImpl implements BmFactory {

    /**
	 * Creates the default factory implementation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public static BmFactory init() {
        try {
            BmFactory theBmFactory = (BmFactory) EPackage.Registry.INSTANCE.getEFactory("eu.medeia.ecore.bm");
            if (theBmFactory != null) {
                return theBmFactory;
            }
        } catch (Exception exception) {
            EcorePlugin.INSTANCE.log(exception);
        }
        return new BmFactoryImpl();
    }

    /**
	 * Creates an instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public BmFactoryImpl() {
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
            case BmPackage.AUTOMATION_COMPONENT:
                return createAutomationComponent();
            case BmPackage.DATA:
                return createData();
            case BmPackage.FUNCTION_CALL:
                return createFunctionCall();
            case BmPackage.EVENT:
                return createEvent();
            case BmPackage.COMPLEX_PORT_ELEMENT:
                return createComplexPortElement();
            case BmPackage.CONSTRAINT:
                return createConstraint();
            case BmPackage.BEHAVIOUR_IMPLEMENTATION:
                return createBehaviourImplementation();
            case BmPackage.BEHAVIOUR_SPECIFICATION:
                return createBehaviourSpecification();
            case BmPackage.BEHAVIOUR_SPECIFICATION_METHOD:
                return createBehaviourSpecificationMethod();
            case BmPackage.TIMED_STATE_CHART:
                return createTimedStateChart();
            case BmPackage.SERVICE_SEQUENCE:
                return createServiceSequence();
            case BmPackage.ADDITIONAL_MODEL_INFORMATION:
                return createAdditionalModelInformation();
            case BmPackage.COMPONENT_PORT:
                return createComponentPort();
            case BmPackage.DIAGNOSTICS_PORT:
                return createDiagnosticsPort();
            case BmPackage.ALARM_PORT:
                return createAlarmPort();
            case BmPackage.MOTOR_CONTROL_PORT:
                return createMotorControlPort();
            case BmPackage.ROBOT_COMMAND_PORT:
                return createRobotCommandPort();
            case BmPackage.PLANT_PORT:
                return createPlantPort();
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
            case BmPackage.DIRECTION:
                return createDirectionFromString(eDataType, initialValue);
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
            case BmPackage.DIRECTION:
                return convertDirectionToString(eDataType, instanceValue);
            default:
                throw new IllegalArgumentException("The datatype '" + eDataType.getName() + "' is not a valid classifier");
        }
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public AutomationComponent createAutomationComponent() {
        AutomationComponentImpl automationComponent = new AutomationComponentImpl();
        return automationComponent;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public Data createData() {
        DataImpl data = new DataImpl();
        return data;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public FunctionCall createFunctionCall() {
        FunctionCallImpl functionCall = new FunctionCallImpl();
        return functionCall;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public Event createEvent() {
        EventImpl event = new EventImpl();
        return event;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public ComplexPortElement createComplexPortElement() {
        ComplexPortElementImpl complexPortElement = new ComplexPortElementImpl();
        return complexPortElement;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public Constraint createConstraint() {
        ConstraintImpl constraint = new ConstraintImpl();
        return constraint;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public BehaviourImplementation createBehaviourImplementation() {
        BehaviourImplementationImpl behaviourImplementation = new BehaviourImplementationImpl();
        return behaviourImplementation;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public BehaviourSpecification createBehaviourSpecification() {
        BehaviourSpecificationImpl behaviourSpecification = new BehaviourSpecificationImpl();
        return behaviourSpecification;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public BehaviourSpecificationMethod createBehaviourSpecificationMethod() {
        BehaviourSpecificationMethodImpl behaviourSpecificationMethod = new BehaviourSpecificationMethodImpl();
        return behaviourSpecificationMethod;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public TimedStateChart createTimedStateChart() {
        TimedStateChartImpl timedStateChart = new TimedStateChartImpl();
        return timedStateChart;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public ServiceSequence createServiceSequence() {
        ServiceSequenceImpl serviceSequence = new ServiceSequenceImpl();
        return serviceSequence;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public AdditionalModelInformation createAdditionalModelInformation() {
        AdditionalModelInformationImpl additionalModelInformation = new AdditionalModelInformationImpl();
        return additionalModelInformation;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public ComponentPort createComponentPort() {
        ComponentPortImpl componentPort = new ComponentPortImpl();
        return componentPort;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public DiagnosticsPort createDiagnosticsPort() {
        DiagnosticsPortImpl diagnosticsPort = new DiagnosticsPortImpl();
        return diagnosticsPort;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public AlarmPort createAlarmPort() {
        AlarmPortImpl alarmPort = new AlarmPortImpl();
        return alarmPort;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public MotorControlPort createMotorControlPort() {
        MotorControlPortImpl motorControlPort = new MotorControlPortImpl();
        return motorControlPort;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public RobotCommandPort createRobotCommandPort() {
        RobotCommandPortImpl robotCommandPort = new RobotCommandPortImpl();
        return robotCommandPort;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public PlantPort createPlantPort() {
        PlantPortImpl plantPort = new PlantPortImpl();
        return plantPort;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public Direction createDirectionFromString(EDataType eDataType, String initialValue) {
        Direction result = Direction.get(initialValue);
        if (result == null) throw new IllegalArgumentException("The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'");
        return result;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public String convertDirectionToString(EDataType eDataType, Object instanceValue) {
        return instanceValue == null ? null : instanceValue.toString();
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public BmPackage getBmPackage() {
        return (BmPackage) getEPackage();
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @deprecated
	 * @generated
	 */
    @Deprecated
    public static BmPackage getPackage() {
        return BmPackage.eINSTANCE;
    }
}
