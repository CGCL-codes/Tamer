package de.fraunhofer.fokus.cttcn.util;

import de.fraunhofer.fokus.cttcn.*;
import hub.metrik.lang.eprovide.debuggingstate.LabeledElement;
import hub.metrik.lang.eprovide.debuggingstate.MActivationFrame;
import hub.metrik.lang.eprovide.debuggingstate.MConcurrencyContext;
import hub.metrik.lang.eprovide.debuggingstate.MProgramContext;
import hub.metrik.lang.eprovide.debuggingstate.MVariable;
import java.util.List;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import de.fraunhofer.fokus.cttcn.Assignment;
import de.fraunhofer.fokus.cttcn.Blck;
import de.fraunhofer.fokus.cttcn.BlckContainer;
import de.fraunhofer.fokus.cttcn.BooleanStreamValue;
import de.fraunhofer.fokus.cttcn.Cont;
import de.fraunhofer.fokus.cttcn.CttcnPackage;
import de.fraunhofer.fokus.cttcn.DefaultTrans;
import de.fraunhofer.fokus.cttcn.DottedTrans;
import de.fraunhofer.fokus.cttcn.Guard;
import de.fraunhofer.fokus.cttcn.GuardedTrans;
import de.fraunhofer.fokus.cttcn.InputStreamVar;
import de.fraunhofer.fokus.cttcn.IntStreamValue;
import de.fraunhofer.fokus.cttcn.Inv;
import de.fraunhofer.fokus.cttcn.OutputStreamVar;
import de.fraunhofer.fokus.cttcn.Par;
import de.fraunhofer.fokus.cttcn.Predicate;
import de.fraunhofer.fokus.cttcn.Seq;
import de.fraunhofer.fokus.cttcn.SimpleDataType;
import de.fraunhofer.fokus.cttcn.StreamValue;
import de.fraunhofer.fokus.cttcn.StreamVar;
import de.fraunhofer.fokus.cttcn.Test;
import de.fraunhofer.fokus.cttcn.Trans;

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
 * @see de.fraunhofer.fokus.cttcn.CttcnPackage
 * @generated
 */
public class CttcnSwitch<T> {

    /**
	 * The cached model package
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    protected static CttcnPackage modelPackage;

    /**
	 * Creates an instance of the switch.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public CttcnSwitch() {
        if (modelPackage == null) {
            modelPackage = CttcnPackage.eINSTANCE;
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
            case CttcnPackage.BLCK:
                {
                    Blck blck = (Blck) theEObject;
                    T result = caseBlck(blck);
                    if (result == null) result = defaultCase(theEObject);
                    return result;
                }
            case CttcnPackage.TRANS:
                {
                    Trans trans = (Trans) theEObject;
                    T result = caseTrans(trans);
                    if (result == null) result = defaultCase(theEObject);
                    return result;
                }
            case CttcnPackage.GUARD:
                {
                    Guard guard = (Guard) theEObject;
                    T result = caseGuard(guard);
                    if (result == null) result = casePredicate(guard);
                    if (result == null) result = defaultCase(theEObject);
                    return result;
                }
            case CttcnPackage.INV:
                {
                    Inv inv = (Inv) theEObject;
                    T result = caseInv(inv);
                    if (result == null) result = casePredicate(inv);
                    if (result == null) result = defaultCase(theEObject);
                    return result;
                }
            case CttcnPackage.ASSIGNMENT:
                {
                    Assignment assignment = (Assignment) theEObject;
                    T result = caseAssignment(assignment);
                    if (result == null) result = defaultCase(theEObject);
                    return result;
                }
            case CttcnPackage.STREAM_VAR:
                {
                    StreamVar streamVar = (StreamVar) theEObject;
                    T result = caseStreamVar(streamVar);
                    if (result == null) result = caseMVariable(streamVar);
                    if (result == null) result = caseLabeledElement(streamVar);
                    if (result == null) result = defaultCase(theEObject);
                    return result;
                }
            case CttcnPackage.CONT:
                {
                    Cont cont = (Cont) theEObject;
                    T result = caseCont(cont);
                    if (result == null) result = caseBlck(cont);
                    if (result == null) result = defaultCase(theEObject);
                    return result;
                }
            case CttcnPackage.PAR:
                {
                    Par par = (Par) theEObject;
                    T result = casePar(par);
                    if (result == null) result = caseBlckContainer(par);
                    if (result == null) result = caseBlck(par);
                    if (result == null) result = defaultCase(theEObject);
                    return result;
                }
            case CttcnPackage.SEQ:
                {
                    Seq seq = (Seq) theEObject;
                    T result = caseSeq(seq);
                    if (result == null) result = caseBlckContainer(seq);
                    if (result == null) result = caseBlck(seq);
                    if (result == null) result = defaultCase(theEObject);
                    return result;
                }
            case CttcnPackage.GUARDED_TRANS:
                {
                    GuardedTrans guardedTrans = (GuardedTrans) theEObject;
                    T result = caseGuardedTrans(guardedTrans);
                    if (result == null) result = caseTrans(guardedTrans);
                    if (result == null) result = defaultCase(theEObject);
                    return result;
                }
            case CttcnPackage.DEFAULT_TRANS:
                {
                    DefaultTrans defaultTrans = (DefaultTrans) theEObject;
                    T result = caseDefaultTrans(defaultTrans);
                    if (result == null) result = caseTrans(defaultTrans);
                    if (result == null) result = defaultCase(theEObject);
                    return result;
                }
            case CttcnPackage.DOTTED_TRANS:
                {
                    DottedTrans dottedTrans = (DottedTrans) theEObject;
                    T result = caseDottedTrans(dottedTrans);
                    if (result == null) result = caseTrans(dottedTrans);
                    if (result == null) result = defaultCase(theEObject);
                    return result;
                }
            case CttcnPackage.BLCK_CONTAINER:
                {
                    BlckContainer blckContainer = (BlckContainer) theEObject;
                    T result = caseBlckContainer(blckContainer);
                    if (result == null) result = caseBlck(blckContainer);
                    if (result == null) result = defaultCase(theEObject);
                    return result;
                }
            case CttcnPackage.TEST:
                {
                    Test test = (Test) theEObject;
                    T result = caseTest(test);
                    if (result == null) result = caseMProgramContext(test);
                    if (result == null) result = caseMConcurrencyContext(test);
                    if (result == null) result = caseMActivationFrame(test);
                    if (result == null) result = caseLabeledElement(test);
                    if (result == null) result = defaultCase(theEObject);
                    return result;
                }
            case CttcnPackage.PREDICATE:
                {
                    Predicate predicate = (Predicate) theEObject;
                    T result = casePredicate(predicate);
                    if (result == null) result = defaultCase(theEObject);
                    return result;
                }
            case CttcnPackage.SIMPLE_DATA_TYPE:
                {
                    SimpleDataType simpleDataType = (SimpleDataType) theEObject;
                    T result = caseSimpleDataType(simpleDataType);
                    if (result == null) result = defaultCase(theEObject);
                    return result;
                }
            case CttcnPackage.INT_STREAM_VALUE:
                {
                    IntStreamValue intStreamValue = (IntStreamValue) theEObject;
                    T result = caseIntStreamValue(intStreamValue);
                    if (result == null) result = caseStreamValue(intStreamValue);
                    if (result == null) result = defaultCase(theEObject);
                    return result;
                }
            case CttcnPackage.BOOLEAN_STREAM_VALUE:
                {
                    BooleanStreamValue booleanStreamValue = (BooleanStreamValue) theEObject;
                    T result = caseBooleanStreamValue(booleanStreamValue);
                    if (result == null) result = caseStreamValue(booleanStreamValue);
                    if (result == null) result = defaultCase(theEObject);
                    return result;
                }
            case CttcnPackage.STREAM_VALUE:
                {
                    StreamValue streamValue = (StreamValue) theEObject;
                    T result = caseStreamValue(streamValue);
                    if (result == null) result = defaultCase(theEObject);
                    return result;
                }
            case CttcnPackage.INPUT_STREAM_VAR:
                {
                    InputStreamVar inputStreamVar = (InputStreamVar) theEObject;
                    T result = caseInputStreamVar(inputStreamVar);
                    if (result == null) result = caseStreamVar(inputStreamVar);
                    if (result == null) result = caseMVariable(inputStreamVar);
                    if (result == null) result = caseLabeledElement(inputStreamVar);
                    if (result == null) result = defaultCase(theEObject);
                    return result;
                }
            case CttcnPackage.OUTPUT_STREAM_VAR:
                {
                    OutputStreamVar outputStreamVar = (OutputStreamVar) theEObject;
                    T result = caseOutputStreamVar(outputStreamVar);
                    if (result == null) result = caseStreamVar(outputStreamVar);
                    if (result == null) result = caseMVariable(outputStreamVar);
                    if (result == null) result = caseLabeledElement(outputStreamVar);
                    if (result == null) result = defaultCase(theEObject);
                    return result;
                }
            default:
                return defaultCase(theEObject);
        }
    }

    /**
	 * Returns the result of interpreting the object as an instance of '<em>Blck</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Blck</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
    public T caseBlck(Blck object) {
        return null;
    }

    /**
	 * Returns the result of interpreting the object as an instance of '<em>Trans</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Trans</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
    public T caseTrans(Trans object) {
        return null;
    }

    /**
	 * Returns the result of interpreting the object as an instance of '<em>Guard</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Guard</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
    public T caseGuard(Guard object) {
        return null;
    }

    /**
	 * Returns the result of interpreting the object as an instance of '<em>Inv</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Inv</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
    public T caseInv(Inv object) {
        return null;
    }

    /**
	 * Returns the result of interpreting the object as an instance of '<em>Assignment</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Assignment</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
    public T caseAssignment(Assignment object) {
        return null;
    }

    /**
	 * Returns the result of interpreting the object as an instance of '<em>Stream Var</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Stream Var</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
    public T caseStreamVar(StreamVar object) {
        return null;
    }

    /**
	 * Returns the result of interpreting the object as an instance of '<em>Cont</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Cont</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
    public T caseCont(Cont object) {
        return null;
    }

    /**
	 * Returns the result of interpreting the object as an instance of '<em>Par</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Par</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
    public T casePar(Par object) {
        return null;
    }

    /**
	 * Returns the result of interpreting the object as an instance of '<em>Seq</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Seq</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
    public T caseSeq(Seq object) {
        return null;
    }

    /**
	 * Returns the result of interpreting the object as an instance of '<em>Guarded Trans</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Guarded Trans</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
    public T caseGuardedTrans(GuardedTrans object) {
        return null;
    }

    /**
	 * Returns the result of interpreting the object as an instance of '<em>Default Trans</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Default Trans</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
    public T caseDefaultTrans(DefaultTrans object) {
        return null;
    }

    /**
	 * Returns the result of interpreting the object as an instance of '<em>Dotted Trans</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Dotted Trans</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
    public T caseDottedTrans(DottedTrans object) {
        return null;
    }

    /**
	 * Returns the result of interpreting the object as an instance of '<em>Blck Container</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Blck Container</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
    public T caseBlckContainer(BlckContainer object) {
        return null;
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
	 * Returns the result of interpreting the object as an instance of '<em>Predicate</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Predicate</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
    public T casePredicate(Predicate object) {
        return null;
    }

    /**
	 * Returns the result of interpreting the object as an instance of '<em>Simple Data Type</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Simple Data Type</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
    public T caseSimpleDataType(SimpleDataType object) {
        return null;
    }

    /**
	 * Returns the result of interpreting the object as an instance of '<em>Int Stream Value</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Int Stream Value</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
    public T caseIntStreamValue(IntStreamValue object) {
        return null;
    }

    /**
	 * Returns the result of interpreting the object as an instance of '<em>Boolean Stream Value</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Boolean Stream Value</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
    public T caseBooleanStreamValue(BooleanStreamValue object) {
        return null;
    }

    /**
	 * Returns the result of interpreting the object as an instance of '<em>Stream Value</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Stream Value</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
    public T caseStreamValue(StreamValue object) {
        return null;
    }

    /**
	 * Returns the result of interpreting the object as an instance of '<em>Input Stream Var</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Input Stream Var</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
    public T caseInputStreamVar(InputStreamVar object) {
        return null;
    }

    /**
	 * Returns the result of interpreting the object as an instance of '<em>Output Stream Var</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Output Stream Var</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
    public T caseOutputStreamVar(OutputStreamVar object) {
        return null;
    }

    /**
	 * Returns the result of interpreting the object as an instance of '<em>Labeled Element</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Labeled Element</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
    public T caseLabeledElement(LabeledElement object) {
        return null;
    }

    /**
	 * Returns the result of interpreting the object as an instance of '<em>MVariable</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>MVariable</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
    public T caseMVariable(MVariable object) {
        return null;
    }

    /**
	 * Returns the result of interpreting the object as an instance of '<em>MProgram Context</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>MProgram Context</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
    public T caseMProgramContext(MProgramContext object) {
        return null;
    }

    /**
	 * Returns the result of interpreting the object as an instance of '<em>MConcurrency Context</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>MConcurrency Context</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
    public T caseMConcurrencyContext(MConcurrencyContext object) {
        return null;
    }

    /**
	 * Returns the result of interpreting the object as an instance of '<em>MActivation Frame</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>MActivation Frame</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
    public T caseMActivationFrame(MActivationFrame object) {
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
