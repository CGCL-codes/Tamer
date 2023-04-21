package com.safi.core.initiator.impl;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.impl.EFactoryImpl;
import org.eclipse.emf.ecore.plugin.EcorePlugin;
import com.safi.core.actionstep.ActionStepException;
import com.safi.core.initiator.*;
import com.safi.core.initiator.InitiatorFactory;
import com.safi.core.initiator.InitiatorPackage;
import com.safi.core.initiator.InputType;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Factory</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class InitiatorFactoryImpl extends EFactoryImpl implements InitiatorFactory {

    /**
	 * Creates the default factory implementation.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 */
    public static InitiatorFactory init() {
        try {
            InitiatorFactory theInitiatorFactory = (InitiatorFactory) EPackage.Registry.INSTANCE.getEFactory("http:///com/safi/core.ecore#initiator");
            if (theInitiatorFactory != null) {
                return theInitiatorFactory;
            }
        } catch (Exception exception) {
            EcorePlugin.INSTANCE.log(exception);
        }
        return new InitiatorFactoryImpl();
    }

    /**
	 * Creates an instance of the factory.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 */
    public InitiatorFactoryImpl() {
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
            case InitiatorPackage.INPUT_TYPE:
                return createInputTypeFromString(eDataType, initialValue);
            case InitiatorPackage.ACTION_STEP_EXCEPTION:
                return createActionStepExceptionFromString(eDataType, initialValue);
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
            case InitiatorPackage.INPUT_TYPE:
                return convertInputTypeToString(eDataType, instanceValue);
            case InitiatorPackage.ACTION_STEP_EXCEPTION:
                return convertActionStepExceptionToString(eDataType, instanceValue);
            default:
                throw new IllegalArgumentException("The datatype '" + eDataType.getName() + "' is not a valid classifier");
        }
    }

    /**
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 */
    public InputType createInputTypeFromString(EDataType eDataType, String initialValue) {
        InputType result = InputType.get(initialValue);
        if (result == null) throw new IllegalArgumentException("The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'");
        return result;
    }

    /**
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 */
    public String convertInputTypeToString(EDataType eDataType, Object instanceValue) {
        return instanceValue == null ? null : instanceValue.toString();
    }

    /**
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 */
    public ActionStepException createActionStepExceptionFromString(EDataType eDataType, String initialValue) {
        return (ActionStepException) super.createFromString(eDataType, initialValue);
    }

    /**
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 */
    public String convertActionStepExceptionToString(EDataType eDataType, Object instanceValue) {
        return super.convertToString(eDataType, instanceValue);
    }

    /**
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 */
    public InitiatorPackage getInitiatorPackage() {
        return (InitiatorPackage) getEPackage();
    }

    /**
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @deprecated
	 * @generated
	 */
    @Deprecated
    public static InitiatorPackage getPackage() {
        return InitiatorPackage.eINSTANCE;
    }
}
