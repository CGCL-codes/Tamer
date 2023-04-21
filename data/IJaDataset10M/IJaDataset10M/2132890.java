package de.nordakademie.lejos.stateMachine.impl;

import de.nordakademie.lejos.stateMachine.StartState;
import de.nordakademie.lejos.stateMachine.StateMachinePackage;
import org.eclipse.emf.ecore.EClass;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Start State</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * </p>
 *
 * @generated
 */
public class StartStateImpl extends AbstractStateImpl implements StartState {

    private static final String START_STATE_EDEFAULT = "StartState";

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
    protected StartStateImpl() {
        super();
        setName(START_STATE_EDEFAULT);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    protected EClass eStaticClass() {
        return StateMachinePackage.Literals.START_STATE;
    }
}
