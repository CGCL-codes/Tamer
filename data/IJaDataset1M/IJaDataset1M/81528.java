package de.fraunhofer.isst.vts.axlang.util;

import de.fraunhofer.isst.vts.axlang.*;
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
 * @see de.fraunhofer.isst.vts.axlang.AxlangPackage
 * @generated
 */
public class AxlangSwitch<T> {

    /**
   * The cached model package
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    protected static AxlangPackage modelPackage;

    /**
   * Creates an instance of the switch.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public AxlangSwitch() {
        if (modelPackage == null) {
            modelPackage = AxlangPackage.eINSTANCE;
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
            case AxlangPackage.MODEL:
                {
                    Model model = (Model) theEObject;
                    T result = caseModel(model);
                    if (result == null) result = defaultCase(theEObject);
                    return result;
                }
            case AxlangPackage.APPLICATION_MODEL:
                {
                    ApplicationModel applicationModel = (ApplicationModel) theEObject;
                    T result = caseApplicationModel(applicationModel);
                    if (result == null) result = defaultCase(theEObject);
                    return result;
                }
            case AxlangPackage.COMPONENT:
                {
                    Component component = (Component) theEObject;
                    T result = caseComponent(component);
                    if (result == null) result = defaultCase(theEObject);
                    return result;
                }
            case AxlangPackage.PORT:
                {
                    Port port = (Port) theEObject;
                    T result = casePort(port);
                    if (result == null) result = defaultCase(theEObject);
                    return result;
                }
            case AxlangPackage.IN_PORT:
                {
                    InPort inPort = (InPort) theEObject;
                    T result = caseInPort(inPort);
                    if (result == null) result = casePort(inPort);
                    if (result == null) result = defaultCase(theEObject);
                    return result;
                }
            case AxlangPackage.OUT_PORT:
                {
                    OutPort outPort = (OutPort) theEObject;
                    T result = caseOutPort(outPort);
                    if (result == null) result = casePort(outPort);
                    if (result == null) result = defaultCase(theEObject);
                    return result;
                }
            case AxlangPackage.SUB_COMPONENT:
                {
                    SubComponent subComponent = (SubComponent) theEObject;
                    T result = caseSubComponent(subComponent);
                    if (result == null) result = defaultCase(theEObject);
                    return result;
                }
            case AxlangPackage.ATOMIC_SUB_COMPONENT:
                {
                    AtomicSubComponent atomicSubComponent = (AtomicSubComponent) theEObject;
                    T result = caseAtomicSubComponent(atomicSubComponent);
                    if (result == null) result = caseSubComponent(atomicSubComponent);
                    if (result == null) result = defaultCase(theEObject);
                    return result;
                }
            case AxlangPackage.XOR_SUB_COMPONENT:
                {
                    XorSubComponent xorSubComponent = (XorSubComponent) theEObject;
                    T result = caseXorSubComponent(xorSubComponent);
                    if (result == null) result = caseSubComponent(xorSubComponent);
                    if (result == null) result = defaultCase(theEObject);
                    return result;
                }
            case AxlangPackage.CONNECTION:
                {
                    Connection connection = (Connection) theEObject;
                    T result = caseConnection(connection);
                    if (result == null) result = defaultCase(theEObject);
                    return result;
                }
            case AxlangPackage.ASSEMBLY_CONNECTION:
                {
                    AssemblyConnection assemblyConnection = (AssemblyConnection) theEObject;
                    T result = caseAssemblyConnection(assemblyConnection);
                    if (result == null) result = caseConnection(assemblyConnection);
                    if (result == null) result = defaultCase(theEObject);
                    return result;
                }
            case AxlangPackage.DELEGATION_CONNECTION:
                {
                    DelegationConnection delegationConnection = (DelegationConnection) theEObject;
                    T result = caseDelegationConnection(delegationConnection);
                    if (result == null) result = caseConnection(delegationConnection);
                    if (result == null) result = defaultCase(theEObject);
                    return result;
                }
            case AxlangPackage.IN_DELEGATION_CONNECTION:
                {
                    InDelegationConnection inDelegationConnection = (InDelegationConnection) theEObject;
                    T result = caseInDelegationConnection(inDelegationConnection);
                    if (result == null) result = caseDelegationConnection(inDelegationConnection);
                    if (result == null) result = caseConnection(inDelegationConnection);
                    if (result == null) result = defaultCase(theEObject);
                    return result;
                }
            case AxlangPackage.OUT_DELEGATION_CONNECTION:
                {
                    OutDelegationConnection outDelegationConnection = (OutDelegationConnection) theEObject;
                    T result = caseOutDelegationConnection(outDelegationConnection);
                    if (result == null) result = caseDelegationConnection(outDelegationConnection);
                    if (result == null) result = caseConnection(outDelegationConnection);
                    if (result == null) result = defaultCase(theEObject);
                    return result;
                }
            case AxlangPackage.INTERFACE:
                {
                    Interface interface_ = (Interface) theEObject;
                    T result = caseInterface(interface_);
                    if (result == null) result = defaultCase(theEObject);
                    return result;
                }
            case AxlangPackage.DATAELEMENT:
                {
                    Dataelement dataelement = (Dataelement) theEObject;
                    T result = caseDataelement(dataelement);
                    if (result == null) result = defaultCase(theEObject);
                    return result;
                }
            case AxlangPackage.SIGNAL:
                {
                    Signal signal = (Signal) theEObject;
                    T result = caseSignal(signal);
                    if (result == null) result = caseDataelement(signal);
                    if (result == null) result = defaultCase(theEObject);
                    return result;
                }
            case AxlangPackage.OPERATION:
                {
                    Operation operation = (Operation) theEObject;
                    T result = caseOperation(operation);
                    if (result == null) result = caseDataelement(operation);
                    if (result == null) result = defaultCase(theEObject);
                    return result;
                }
            case AxlangPackage.PARAMETER:
                {
                    Parameter parameter = (Parameter) theEObject;
                    T result = caseParameter(parameter);
                    if (result == null) result = defaultCase(theEObject);
                    return result;
                }
            default:
                return defaultCase(theEObject);
        }
    }

    /**
   * Returns the result of interpreting the object as an instance of '<em>Model</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Model</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
    public T caseModel(Model object) {
        return null;
    }

    /**
   * Returns the result of interpreting the object as an instance of '<em>Application Model</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Application Model</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
    public T caseApplicationModel(ApplicationModel object) {
        return null;
    }

    /**
   * Returns the result of interpreting the object as an instance of '<em>Component</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Component</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
    public T caseComponent(Component object) {
        return null;
    }

    /**
   * Returns the result of interpreting the object as an instance of '<em>Port</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Port</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
    public T casePort(Port object) {
        return null;
    }

    /**
   * Returns the result of interpreting the object as an instance of '<em>In Port</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>In Port</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
    public T caseInPort(InPort object) {
        return null;
    }

    /**
   * Returns the result of interpreting the object as an instance of '<em>Out Port</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Out Port</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
    public T caseOutPort(OutPort object) {
        return null;
    }

    /**
   * Returns the result of interpreting the object as an instance of '<em>Sub Component</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Sub Component</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
    public T caseSubComponent(SubComponent object) {
        return null;
    }

    /**
   * Returns the result of interpreting the object as an instance of '<em>Atomic Sub Component</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Atomic Sub Component</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
    public T caseAtomicSubComponent(AtomicSubComponent object) {
        return null;
    }

    /**
   * Returns the result of interpreting the object as an instance of '<em>Xor Sub Component</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Xor Sub Component</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
    public T caseXorSubComponent(XorSubComponent object) {
        return null;
    }

    /**
   * Returns the result of interpreting the object as an instance of '<em>Connection</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Connection</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
    public T caseConnection(Connection object) {
        return null;
    }

    /**
   * Returns the result of interpreting the object as an instance of '<em>Assembly Connection</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Assembly Connection</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
    public T caseAssemblyConnection(AssemblyConnection object) {
        return null;
    }

    /**
   * Returns the result of interpreting the object as an instance of '<em>Delegation Connection</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Delegation Connection</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
    public T caseDelegationConnection(DelegationConnection object) {
        return null;
    }

    /**
   * Returns the result of interpreting the object as an instance of '<em>In Delegation Connection</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>In Delegation Connection</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
    public T caseInDelegationConnection(InDelegationConnection object) {
        return null;
    }

    /**
   * Returns the result of interpreting the object as an instance of '<em>Out Delegation Connection</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Out Delegation Connection</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
    public T caseOutDelegationConnection(OutDelegationConnection object) {
        return null;
    }

    /**
   * Returns the result of interpreting the object as an instance of '<em>Interface</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Interface</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
    public T caseInterface(Interface object) {
        return null;
    }

    /**
   * Returns the result of interpreting the object as an instance of '<em>Dataelement</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Dataelement</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
    public T caseDataelement(Dataelement object) {
        return null;
    }

    /**
   * Returns the result of interpreting the object as an instance of '<em>Signal</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Signal</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
    public T caseSignal(Signal object) {
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
   * Returns the result of interpreting the object as an instance of '<em>Parameter</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Parameter</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
    public T caseParameter(Parameter object) {
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
