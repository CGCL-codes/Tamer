package hu.cubussapiens.modembed.notation.operation.operationNotation.util;

import hu.cubussapiens.modembed.notation.operation.operationNotation.*;
import java.util.List;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * The <b>Switch</b> for the model's inheritance hierarchy.
 * It supports the call {@link #doSwitch(EObject) doSwitch(object)}
 * to invoke the <code>caseXXX</code> method for each class of the model,
 * starting with the actual class of the object
 * and proceeding up the inheritance hierarchy
 * until a non-null result is returned,
 * which is the result of the switch.
 * <!-- end-user-doc -->
 * @see hu.cubussapiens.modembed.notation.operation.operationNotation.OperationNotationPackage
 * @generated
 */
public class OperationNotationSwitch<T> {

    /**
   * The cached model package
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    protected static OperationNotationPackage modelPackage;

    /**
   * Creates an instance of the switch.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public OperationNotationSwitch() {
        if (modelPackage == null) {
            modelPackage = OperationNotationPackage.eINSTANCE;
        }
    }

    /**
   * Calls <code>caseXXX</code> for each class of the model until one returns a non null result; it yields that result.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the first non-null result returned by a <code>caseXXX</code> call.
   * @generated
   */
    public T doSwitch(EObject theEObject) {
        return doSwitch(theEObject.eClass(), theEObject);
    }

    /**
   * Calls <code>caseXXX</code> for each class of the model until one returns a non null result; it yields that result.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the first non-null result returned by a <code>caseXXX</code> call.
   * @generated
   */
    protected T doSwitch(EClass theEClass, EObject theEObject) {
        if (theEClass.eContainer() == modelPackage) {
            return doSwitch(theEClass.getClassifierID(), theEObject);
        } else {
            List<EClass> eSuperTypes = theEClass.getESuperTypes();
            return eSuperTypes.isEmpty() ? defaultCase(theEObject) : doSwitch(eSuperTypes.get(0), theEObject);
        }
    }

    /**
   * Calls <code>caseXXX</code> for each class of the model until one returns a non null result; it yields that result.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the first non-null result returned by a <code>caseXXX</code> call.
   * @generated
   */
    protected T doSwitch(int classifierID, EObject theEObject) {
        switch(classifierID) {
            case OperationNotationPackage.OPERATIONS:
                {
                    Operations operations = (Operations) theEObject;
                    T result = caseOperations(operations);
                    if (result == null) result = defaultCase(theEObject);
                    return result;
                }
            case OperationNotationPackage.OPERATION:
                {
                    Operation operation = (Operation) theEObject;
                    T result = caseOperation(operation);
                    if (result == null) result = defaultCase(theEObject);
                    return result;
                }
            case OperationNotationPackage.OPERATION_PARAMETER:
                {
                    OperationParameter operationParameter = (OperationParameter) theEObject;
                    T result = caseOperationParameter(operationParameter);
                    if (result == null) result = defaultCase(theEObject);
                    return result;
                }
            case OperationNotationPackage.LITERAL_PARAMETER:
                {
                    LiteralParameter literalParameter = (LiteralParameter) theEObject;
                    T result = caseLiteralParameter(literalParameter);
                    if (result == null) result = caseOperationParameter(literalParameter);
                    if (result == null) result = defaultCase(theEObject);
                    return result;
                }
            case OperationNotationPackage.VARIABLE_PARAMETER:
                {
                    VariableParameter variableParameter = (VariableParameter) theEObject;
                    T result = caseVariableParameter(variableParameter);
                    if (result == null) result = caseOperationParameter(variableParameter);
                    if (result == null) result = defaultCase(theEObject);
                    return result;
                }
            case OperationNotationPackage.OPERATION_STEP:
                {
                    OperationStep operationStep = (OperationStep) theEObject;
                    T result = caseOperationStep(operationStep);
                    if (result == null) result = defaultCase(theEObject);
                    return result;
                }
            case OperationNotationPackage.OPERATION_LABEL:
                {
                    OperationLabel operationLabel = (OperationLabel) theEObject;
                    T result = caseOperationLabel(operationLabel);
                    if (result == null) result = caseOperationParameter(operationLabel);
                    if (result == null) result = caseOperationStep(operationLabel);
                    if (result == null) result = defaultCase(theEObject);
                    return result;
                }
            case OperationNotationPackage.OPERATION_WORD:
                {
                    OperationWord operationWord = (OperationWord) theEObject;
                    T result = caseOperationWord(operationWord);
                    if (result == null) result = caseOperationStep(operationWord);
                    if (result == null) result = defaultCase(theEObject);
                    return result;
                }
            case OperationNotationPackage.VALUE:
                {
                    Value value = (Value) theEObject;
                    T result = caseValue(value);
                    if (result == null) result = defaultCase(theEObject);
                    return result;
                }
            case OperationNotationPackage.LITERAL_VALUE:
                {
                    LiteralValue literalValue = (LiteralValue) theEObject;
                    T result = caseLiteralValue(literalValue);
                    if (result == null) result = caseValue(literalValue);
                    if (result == null) result = defaultCase(theEObject);
                    return result;
                }
            case OperationNotationPackage.LITERAL_PARAM_VALUE:
                {
                    LiteralParamValue literalParamValue = (LiteralParamValue) theEObject;
                    T result = caseLiteralParamValue(literalParamValue);
                    if (result == null) result = caseValue(literalParamValue);
                    if (result == null) result = defaultCase(theEObject);
                    return result;
                }
            case OperationNotationPackage.LABEL_VALUE:
                {
                    LabelValue labelValue = (LabelValue) theEObject;
                    T result = caseLabelValue(labelValue);
                    if (result == null) result = caseValue(labelValue);
                    if (result == null) result = defaultCase(theEObject);
                    return result;
                }
            case OperationNotationPackage.VARIABLE_VALUE:
                {
                    VariableValue variableValue = (VariableValue) theEObject;
                    T result = caseVariableValue(variableValue);
                    if (result == null) result = caseValue(variableValue);
                    if (result == null) result = defaultCase(theEObject);
                    return result;
                }
            case OperationNotationPackage.VARIABLE_INDEX:
                {
                    VariableIndex variableIndex = (VariableIndex) theEObject;
                    T result = caseVariableIndex(variableIndex);
                    if (result == null) result = defaultCase(theEObject);
                    return result;
                }
            default:
                return defaultCase(theEObject);
        }
    }

    /**
   * Returns the result of interpreting the object as an instance of '<em>Operations</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Operations</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
    public T caseOperations(Operations object) {
        return null;
    }

    /**
   * Returns the result of interpreting the object as an instance of '<em>Operation</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Operation</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
    public T caseOperation(Operation object) {
        return null;
    }

    /**
   * Returns the result of interpreting the object as an instance of '<em>Operation Parameter</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Operation Parameter</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
    public T caseOperationParameter(OperationParameter object) {
        return null;
    }

    /**
   * Returns the result of interpreting the object as an instance of '<em>Literal Parameter</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Literal Parameter</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
    public T caseLiteralParameter(LiteralParameter object) {
        return null;
    }

    /**
   * Returns the result of interpreting the object as an instance of '<em>Variable Parameter</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Variable Parameter</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
    public T caseVariableParameter(VariableParameter object) {
        return null;
    }

    /**
   * Returns the result of interpreting the object as an instance of '<em>Operation Step</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Operation Step</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
    public T caseOperationStep(OperationStep object) {
        return null;
    }

    /**
   * Returns the result of interpreting the object as an instance of '<em>Operation Label</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Operation Label</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
    public T caseOperationLabel(OperationLabel object) {
        return null;
    }

    /**
   * Returns the result of interpreting the object as an instance of '<em>Operation Word</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Operation Word</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
    public T caseOperationWord(OperationWord object) {
        return null;
    }

    /**
   * Returns the result of interpreting the object as an instance of '<em>Value</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Value</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
    public T caseValue(Value object) {
        return null;
    }

    /**
   * Returns the result of interpreting the object as an instance of '<em>Literal Value</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Literal Value</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
    public T caseLiteralValue(LiteralValue object) {
        return null;
    }

    /**
   * Returns the result of interpreting the object as an instance of '<em>Literal Param Value</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Literal Param Value</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
    public T caseLiteralParamValue(LiteralParamValue object) {
        return null;
    }

    /**
   * Returns the result of interpreting the object as an instance of '<em>Label Value</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Label Value</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
    public T caseLabelValue(LabelValue object) {
        return null;
    }

    /**
   * Returns the result of interpreting the object as an instance of '<em>Variable Value</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Variable Value</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
    public T caseVariableValue(VariableValue object) {
        return null;
    }

    /**
   * Returns the result of interpreting the object as an instance of '<em>Variable Index</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Variable Index</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
    public T caseVariableIndex(VariableIndex object) {
        return null;
    }

    /**
   * Returns the result of interpreting the object as an instance of '<em>EObject</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch, but this is the last case anyway.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>EObject</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject)
   * @generated
   */
    public T defaultCase(EObject object) {
        return null;
    }
}
