package default_.testpackage.util;

import java.util.List;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import default_.testpackage.*;

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
 * @see default_.testpackage.TestpackagePackage
 * @generated
 */
public class TestpackageSwitch<T> {

    /**
	 * The cached model package
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    protected static TestpackagePackage modelPackage;

    /**
	 * Creates an instance of the switch.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public TestpackageSwitch() {
        if (modelPackage == null) {
            modelPackage = TestpackagePackage.eINSTANCE;
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
            case TestpackagePackage.TEST:
                {
                    Test test = (Test) theEObject;
                    T result = caseTest(test);
                    if (result == null) result = defaultCase(theEObject);
                    return result;
                }
            case TestpackagePackage.TEST_PARAMETER:
                {
                    TestParameter testParameter = (TestParameter) theEObject;
                    T result = caseTestParameter(testParameter);
                    if (result == null) result = defaultCase(theEObject);
                    return result;
                }
            case TestpackagePackage.IEMULATOR:
                {
                    IEmulator iEmulator = (IEmulator) theEObject;
                    T result = caseIEmulator(iEmulator);
                    if (result == null) result = defaultCase(theEObject);
                    return result;
                }
            case TestpackagePackage.PARAMETER_VALUE:
                {
                    ParameterValue parameterValue = (ParameterValue) theEObject;
                    T result = caseParameterValue(parameterValue);
                    if (result == null) result = defaultCase(theEObject);
                    return result;
                }
            case TestpackagePackage.IEMULATOR_LISTENER:
                {
                    IEmulatorListener iEmulatorListener = (IEmulatorListener) theEObject;
                    T result = caseIEmulatorListener(iEmulatorListener);
                    if (result == null) result = defaultCase(theEObject);
                    return result;
                }
            case TestpackagePackage.VALUE:
                {
                    Value value = (Value) theEObject;
                    T result = caseValue(value);
                    if (result == null) result = defaultCase(theEObject);
                    return result;
                }
            case TestpackagePackage.STRING_VALUE:
                {
                    StringValue stringValue = (StringValue) theEObject;
                    T result = caseStringValue(stringValue);
                    if (result == null) result = caseValue(stringValue);
                    if (result == null) result = defaultCase(theEObject);
                    return result;
                }
            case TestpackagePackage.INT_VALUE:
                {
                    IntValue intValue = (IntValue) theEObject;
                    T result = caseIntValue(intValue);
                    if (result == null) result = caseValue(intValue);
                    if (result == null) result = defaultCase(theEObject);
                    return result;
                }
            case TestpackagePackage.BOOLEAN_VALUE:
                {
                    BooleanValue booleanValue = (BooleanValue) theEObject;
                    T result = caseBooleanValue(booleanValue);
                    if (result == null) result = caseValue(booleanValue);
                    if (result == null) result = defaultCase(theEObject);
                    return result;
                }
            case TestpackagePackage.REAL_VALUE:
                {
                    RealValue realValue = (RealValue) theEObject;
                    T result = caseRealValue(realValue);
                    if (result == null) result = caseIntValue(realValue);
                    if (result == null) result = caseValue(realValue);
                    if (result == null) result = defaultCase(theEObject);
                    return result;
                }
            default:
                return defaultCase(theEObject);
        }
    }

    /**
	 * Returns the result of interpreting the object as an instance of '<em>Test</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Test</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
    public T caseTest(Test object) {
        return null;
    }

    /**
	 * Returns the result of interpreting the object as an instance of '<em>Test Parameter</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Test Parameter</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
    public T caseTestParameter(TestParameter object) {
        return null;
    }

    /**
	 * Returns the result of interpreting the object as an instance of '<em>IEmulator</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>IEmulator</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
    public T caseIEmulator(IEmulator object) {
        return null;
    }

    /**
	 * Returns the result of interpreting the object as an instance of '<em>Parameter Value</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Parameter Value</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
    public T caseParameterValue(ParameterValue object) {
        return null;
    }

    /**
	 * Returns the result of interpreting the object as an instance of '<em>IEmulator Listener</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>IEmulator Listener</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
    public T caseIEmulatorListener(IEmulatorListener object) {
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
	 * Returns the result of interpreting the object as an instance of '<em>String Value</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>String Value</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
    public T caseStringValue(StringValue object) {
        return null;
    }

    /**
	 * Returns the result of interpreting the object as an instance of '<em>Int Value</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Int Value</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
    public T caseIntValue(IntValue object) {
        return null;
    }

    /**
	 * Returns the result of interpreting the object as an instance of '<em>Boolean Value</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Boolean Value</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
    public T caseBooleanValue(BooleanValue object) {
        return null;
    }

    /**
	 * Returns the result of interpreting the object as an instance of '<em>Real Value</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Real Value</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
    public T caseRealValue(RealValue object) {
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
