package de.nordakademie.lejos.stateMachine.impl;

import de.nordakademie.lejos.stateMachine.*;
import org.eclipse.emf.ecore.EClass;
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
public class StateMachineFactoryImpl extends EFactoryImpl implements StateMachineFactory {

    /**
	 * Creates the default factory implementation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public static StateMachineFactory init() {
        try {
            StateMachineFactory theStateMachineFactory = (StateMachineFactory) EPackage.Registry.INSTANCE.getEFactory("http://stateMachine");
            if (theStateMachineFactory != null) {
                return theStateMachineFactory;
            }
        } catch (Exception exception) {
            EcorePlugin.INSTANCE.log(exception);
        }
        return new StateMachineFactoryImpl();
    }

    /**
	 * Creates an instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public StateMachineFactoryImpl() {
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
            case StateMachinePackage.DIAGRAM:
                return createDiagram();
            case StateMachinePackage.STATE_MACHINE:
                return createStateMachine();
            case StateMachinePackage.STATE_VARIABLE:
                return createStateVariable();
            case StateMachinePackage.STATE:
                return createState();
            case StateMachinePackage.START_STATE:
                return createStartState();
            case StateMachinePackage.NAMED_EVENT:
                return createNamedEvent();
            case StateMachinePackage.IMPLICIT_EVENT:
                return createImplicitEvent();
            case StateMachinePackage.END_STATE:
                return createEndState();
            default:
                throw new IllegalArgumentException("The class '" + eClass.getName() + "' is not a valid classifier");
        }
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public Diagram createDiagram() {
        DiagramImpl diagram = new DiagramImpl();
        return diagram;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public StateMachine createStateMachine() {
        StateMachineImpl stateMachine = new StateMachineImpl();
        return stateMachine;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public StateVariable createStateVariable() {
        StateVariableImpl stateVariable = new StateVariableImpl();
        return stateVariable;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public State createState() {
        StateImpl state = new StateImpl();
        return state;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public StartState createStartState() {
        StartStateImpl startState = new StartStateImpl();
        return startState;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public NamedEvent createNamedEvent() {
        NamedEventImpl namedEvent = new NamedEventImpl();
        return namedEvent;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public ImplicitEvent createImplicitEvent() {
        ImplicitEventImpl implicitEvent = new ImplicitEventImpl();
        return implicitEvent;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EndState createEndState() {
        EndStateImpl endState = new EndStateImpl();
        return endState;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public StateMachinePackage getStateMachinePackage() {
        return (StateMachinePackage) getEPackage();
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @deprecated
	 * @generated
	 */
    @Deprecated
    public static StateMachinePackage getPackage() {
        return StateMachinePackage.eINSTANCE;
    }
}
