package eu.medeia.ecore.dm.util;

import eu.medeia.ecore.bm.INamedElement;
import eu.medeia.ecore.bm.IVersionedElement;
import eu.medeia.ecore.dm.*;
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
 * @see eu.medeia.ecore.dm.DmPackage
 * @generated
 */
public class DmSwitch<T> {

    /**
	 * The cached model package
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    protected static DmPackage modelPackage;

    /**
	 * Creates an instance of the switch.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public DmSwitch() {
        if (modelPackage == null) {
            modelPackage = DmPackage.eINSTANCE;
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
            case DmPackage.DIAGNOSTICS_COMPONENT:
                {
                    DiagnosticsComponent diagnosticsComponent = (DiagnosticsComponent) theEObject;
                    T result = caseDiagnosticsComponent(diagnosticsComponent);
                    if (result == null) result = caseIVersionedElement(diagnosticsComponent);
                    if (result == null) result = caseINamedElement(diagnosticsComponent);
                    if (result == null) result = defaultCase(theEObject);
                    return result;
                }
            case DmPackage.DIAGNOSTICS_BEHAVIOUR_IMPLEMENTATION:
                {
                    DiagnosticsBehaviourImplementation diagnosticsBehaviourImplementation = (DiagnosticsBehaviourImplementation) theEObject;
                    T result = caseDiagnosticsBehaviourImplementation(diagnosticsBehaviourImplementation);
                    if (result == null) result = caseINamedElement(diagnosticsBehaviourImplementation);
                    if (result == null) result = defaultCase(theEObject);
                    return result;
                }
            case DmPackage.DIAGNOSTICS_COMPONENT_RESULT:
                {
                    DiagnosticsComponentResult diagnosticsComponentResult = (DiagnosticsComponentResult) theEObject;
                    T result = caseDiagnosticsComponentResult(diagnosticsComponentResult);
                    if (result == null) result = caseINamedElement(diagnosticsComponentResult);
                    if (result == null) result = defaultCase(theEObject);
                    return result;
                }
            case DmPackage.DIAGNOSTICS_BEHAVIOUR_DESCRIPTION_METHOD:
                {
                    DiagnosticsBehaviourDescriptionMethod diagnosticsBehaviourDescriptionMethod = (DiagnosticsBehaviourDescriptionMethod) theEObject;
                    T result = caseDiagnosticsBehaviourDescriptionMethod(diagnosticsBehaviourDescriptionMethod);
                    if (result == null) result = caseINamedElement(diagnosticsBehaviourDescriptionMethod);
                    if (result == null) result = defaultCase(theEObject);
                    return result;
                }
            case DmPackage.PETRI_NET:
                {
                    PetriNet petriNet = (PetriNet) theEObject;
                    T result = casePetriNet(petriNet);
                    if (result == null) result = caseINamedElement(petriNet);
                    if (result == null) result = defaultCase(theEObject);
                    return result;
                }
            case DmPackage.RULE:
                {
                    Rule rule = (Rule) theEObject;
                    T result = caseRule(rule);
                    if (result == null) result = caseINamedElement(rule);
                    if (result == null) result = defaultCase(theEObject);
                    return result;
                }
            case DmPackage.CONDITION:
                {
                    Condition condition = (Condition) theEObject;
                    T result = caseCondition(condition);
                    if (result == null) result = caseINamedElement(condition);
                    if (result == null) result = defaultCase(theEObject);
                    return result;
                }
            case DmPackage.STATEMENT:
                {
                    Statement statement = (Statement) theEObject;
                    T result = caseStatement(statement);
                    if (result == null) result = caseINamedElement(statement);
                    if (result == null) result = defaultCase(theEObject);
                    return result;
                }
            default:
                return defaultCase(theEObject);
        }
    }

    /**
	 * Returns the result of interpreting the object as an instance of '<em>Diagnostics Component</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Diagnostics Component</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
    public T caseDiagnosticsComponent(DiagnosticsComponent object) {
        return null;
    }

    /**
	 * Returns the result of interpreting the object as an instance of '<em>Diagnostics Behaviour Implementation</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Diagnostics Behaviour Implementation</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
    public T caseDiagnosticsBehaviourImplementation(DiagnosticsBehaviourImplementation object) {
        return null;
    }

    /**
	 * Returns the result of interpreting the object as an instance of '<em>Diagnostics Component Result</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Diagnostics Component Result</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
    public T caseDiagnosticsComponentResult(DiagnosticsComponentResult object) {
        return null;
    }

    /**
	 * Returns the result of interpreting the object as an instance of '<em>Diagnostics Behaviour Description Method</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Diagnostics Behaviour Description Method</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
    public T caseDiagnosticsBehaviourDescriptionMethod(DiagnosticsBehaviourDescriptionMethod object) {
        return null;
    }

    /**
	 * Returns the result of interpreting the object as an instance of '<em>Petri Net</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Petri Net</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
    public T casePetriNet(PetriNet object) {
        return null;
    }

    /**
	 * Returns the result of interpreting the object as an instance of '<em>Rule</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Rule</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
    public T caseRule(Rule object) {
        return null;
    }

    /**
	 * Returns the result of interpreting the object as an instance of '<em>Condition</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Condition</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
    public T caseCondition(Condition object) {
        return null;
    }

    /**
	 * Returns the result of interpreting the object as an instance of '<em>Statement</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Statement</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
    public T caseStatement(Statement object) {
        return null;
    }

    /**
	 * Returns the result of interpreting the object as an instance of '<em>INamed Element</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>INamed Element</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
    public T caseINamedElement(INamedElement object) {
        return null;
    }

    /**
	 * Returns the result of interpreting the object as an instance of '<em>IVersioned Element</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>IVersioned Element</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
    public T caseIVersionedElement(IVersionedElement object) {
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
