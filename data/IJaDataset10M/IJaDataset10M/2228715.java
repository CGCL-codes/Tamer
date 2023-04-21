package eu.medeia.ecore.apmm.library.util;

import java.util.List;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import eu.medeia.ecore.apmm.bm.INamedElement;
import eu.medeia.ecore.apmm.bm.IVersionedElement;
import eu.medeia.ecore.apmm.library.AlarmPortStencil;
import eu.medeia.ecore.apmm.library.AutomationComponentType;
import eu.medeia.ecore.apmm.library.BackplaneType;
import eu.medeia.ecore.apmm.library.CustomPortStencil;
import eu.medeia.ecore.apmm.library.DeviceType;
import eu.medeia.ecore.apmm.library.DiagnosticPortStencil;
import eu.medeia.ecore.apmm.library.DiagnosticsComponentType;
import eu.medeia.ecore.apmm.library.FieldbusType;
import eu.medeia.ecore.apmm.library.LibraryElement;
import eu.medeia.ecore.apmm.library.LibraryPackage;
import eu.medeia.ecore.apmm.library.MemoryType;
import eu.medeia.ecore.apmm.library.PlantComponentType;
import eu.medeia.ecore.apmm.library.PlantPortStencil;
import eu.medeia.ecore.apmm.library.ProcessorType;

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
 * @see eu.medeia.ecore.apmm.library.LibraryPackage
 * @generated
 */
public class LibrarySwitch<T> {

    /**
	 * The cached model package
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    protected static LibraryPackage modelPackage;

    /**
	 * Creates an instance of the switch.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public LibrarySwitch() {
        if (modelPackage == null) {
            modelPackage = LibraryPackage.eINSTANCE;
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
            case LibraryPackage.LIBRARY_ELEMENT:
                {
                    LibraryElement libraryElement = (LibraryElement) theEObject;
                    T result = caseLibraryElement(libraryElement);
                    if (result == null) result = caseIVersionedElement(libraryElement);
                    if (result == null) result = caseINamedElement(libraryElement);
                    if (result == null) result = defaultCase(theEObject);
                    return result;
                }
            case LibraryPackage.AUTOMATION_COMPONENT_TYPE:
                {
                    AutomationComponentType automationComponentType = (AutomationComponentType) theEObject;
                    T result = caseAutomationComponentType(automationComponentType);
                    if (result == null) result = caseLibraryElement(automationComponentType);
                    if (result == null) result = caseIVersionedElement(automationComponentType);
                    if (result == null) result = caseINamedElement(automationComponentType);
                    if (result == null) result = defaultCase(theEObject);
                    return result;
                }
            case LibraryPackage.DEVICE_TYPE:
                {
                    DeviceType deviceType = (DeviceType) theEObject;
                    T result = caseDeviceType(deviceType);
                    if (result == null) result = caseLibraryElement(deviceType);
                    if (result == null) result = caseIVersionedElement(deviceType);
                    if (result == null) result = caseINamedElement(deviceType);
                    if (result == null) result = defaultCase(theEObject);
                    return result;
                }
            case LibraryPackage.FIELDBUS_TYPE:
                {
                    FieldbusType fieldbusType = (FieldbusType) theEObject;
                    T result = caseFieldbusType(fieldbusType);
                    if (result == null) result = caseLibraryElement(fieldbusType);
                    if (result == null) result = caseIVersionedElement(fieldbusType);
                    if (result == null) result = caseINamedElement(fieldbusType);
                    if (result == null) result = defaultCase(theEObject);
                    return result;
                }
            case LibraryPackage.BACKPLANE_TYPE:
                {
                    BackplaneType backplaneType = (BackplaneType) theEObject;
                    T result = caseBackplaneType(backplaneType);
                    if (result == null) result = caseLibraryElement(backplaneType);
                    if (result == null) result = caseIVersionedElement(backplaneType);
                    if (result == null) result = caseINamedElement(backplaneType);
                    if (result == null) result = defaultCase(theEObject);
                    return result;
                }
            case LibraryPackage.PROCESSOR_TYPE:
                {
                    ProcessorType processorType = (ProcessorType) theEObject;
                    T result = caseProcessorType(processorType);
                    if (result == null) result = caseLibraryElement(processorType);
                    if (result == null) result = caseIVersionedElement(processorType);
                    if (result == null) result = caseINamedElement(processorType);
                    if (result == null) result = defaultCase(theEObject);
                    return result;
                }
            case LibraryPackage.MEMORY_TYPE:
                {
                    MemoryType memoryType = (MemoryType) theEObject;
                    T result = caseMemoryType(memoryType);
                    if (result == null) result = caseLibraryElement(memoryType);
                    if (result == null) result = caseIVersionedElement(memoryType);
                    if (result == null) result = caseINamedElement(memoryType);
                    if (result == null) result = defaultCase(theEObject);
                    return result;
                }
            case LibraryPackage.CUSTOM_PORT_STENCIL:
                {
                    CustomPortStencil customPortStencil = (CustomPortStencil) theEObject;
                    T result = caseCustomPortStencil(customPortStencil);
                    if (result == null) result = caseLibraryElement(customPortStencil);
                    if (result == null) result = caseIVersionedElement(customPortStencil);
                    if (result == null) result = caseINamedElement(customPortStencil);
                    if (result == null) result = defaultCase(theEObject);
                    return result;
                }
            case LibraryPackage.DIAGNOSTIC_PORT_STENCIL:
                {
                    DiagnosticPortStencil diagnosticPortStencil = (DiagnosticPortStencil) theEObject;
                    T result = caseDiagnosticPortStencil(diagnosticPortStencil);
                    if (result == null) result = caseLibraryElement(diagnosticPortStencil);
                    if (result == null) result = caseIVersionedElement(diagnosticPortStencil);
                    if (result == null) result = caseINamedElement(diagnosticPortStencil);
                    if (result == null) result = defaultCase(theEObject);
                    return result;
                }
            case LibraryPackage.ALARM_PORT_STENCIL:
                {
                    AlarmPortStencil alarmPortStencil = (AlarmPortStencil) theEObject;
                    T result = caseAlarmPortStencil(alarmPortStencil);
                    if (result == null) result = caseLibraryElement(alarmPortStencil);
                    if (result == null) result = caseIVersionedElement(alarmPortStencil);
                    if (result == null) result = caseINamedElement(alarmPortStencil);
                    if (result == null) result = defaultCase(theEObject);
                    return result;
                }
            case LibraryPackage.PLANT_PORT_STENCIL:
                {
                    PlantPortStencil plantPortStencil = (PlantPortStencil) theEObject;
                    T result = casePlantPortStencil(plantPortStencil);
                    if (result == null) result = caseLibraryElement(plantPortStencil);
                    if (result == null) result = caseIVersionedElement(plantPortStencil);
                    if (result == null) result = caseINamedElement(plantPortStencil);
                    if (result == null) result = defaultCase(theEObject);
                    return result;
                }
            case LibraryPackage.PLANT_COMPONENT_TYPE:
                {
                    PlantComponentType plantComponentType = (PlantComponentType) theEObject;
                    T result = casePlantComponentType(plantComponentType);
                    if (result == null) result = caseLibraryElement(plantComponentType);
                    if (result == null) result = caseIVersionedElement(plantComponentType);
                    if (result == null) result = caseINamedElement(plantComponentType);
                    if (result == null) result = defaultCase(theEObject);
                    return result;
                }
            case LibraryPackage.DIAGNOSTICS_COMPONENT_TYPE:
                {
                    DiagnosticsComponentType diagnosticsComponentType = (DiagnosticsComponentType) theEObject;
                    T result = caseDiagnosticsComponentType(diagnosticsComponentType);
                    if (result == null) result = caseLibraryElement(diagnosticsComponentType);
                    if (result == null) result = caseIVersionedElement(diagnosticsComponentType);
                    if (result == null) result = caseINamedElement(diagnosticsComponentType);
                    if (result == null) result = defaultCase(theEObject);
                    return result;
                }
            default:
                return defaultCase(theEObject);
        }
    }

    /**
	 * Returns the result of interpreting the object as an instance of '<em>Element</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Element</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
    public T caseLibraryElement(LibraryElement object) {
        return null;
    }

    /**
	 * Returns the result of interpreting the object as an instance of '<em>Automation Component Type</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Automation Component Type</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
    public T caseAutomationComponentType(AutomationComponentType object) {
        return null;
    }

    /**
	 * Returns the result of interpreting the object as an instance of '<em>Device Type</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Device Type</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
    public T caseDeviceType(DeviceType object) {
        return null;
    }

    /**
	 * Returns the result of interpreting the object as an instance of '<em>Fieldbus Type</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Fieldbus Type</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
    public T caseFieldbusType(FieldbusType object) {
        return null;
    }

    /**
	 * Returns the result of interpreting the object as an instance of '<em>Backplane Type</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Backplane Type</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
    public T caseBackplaneType(BackplaneType object) {
        return null;
    }

    /**
	 * Returns the result of interpreting the object as an instance of '<em>Processor Type</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Processor Type</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
    public T caseProcessorType(ProcessorType object) {
        return null;
    }

    /**
	 * Returns the result of interpreting the object as an instance of '<em>Memory Type</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Memory Type</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
    public T caseMemoryType(MemoryType object) {
        return null;
    }

    /**
	 * Returns the result of interpreting the object as an instance of '<em>Custom Port Stencil</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Custom Port Stencil</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
    public T caseCustomPortStencil(CustomPortStencil object) {
        return null;
    }

    /**
	 * Returns the result of interpreting the object as an instance of '<em>Diagnostic Port Stencil</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Diagnostic Port Stencil</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
    public T caseDiagnosticPortStencil(DiagnosticPortStencil object) {
        return null;
    }

    /**
	 * Returns the result of interpreting the object as an instance of '<em>Alarm Port Stencil</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Alarm Port Stencil</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
    public T caseAlarmPortStencil(AlarmPortStencil object) {
        return null;
    }

    /**
	 * Returns the result of interpreting the object as an instance of '<em>Plant Port Stencil</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Plant Port Stencil</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
    public T casePlantPortStencil(PlantPortStencil object) {
        return null;
    }

    /**
	 * Returns the result of interpreting the object as an instance of '<em>Plant Component Type</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Plant Component Type</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
    public T casePlantComponentType(PlantComponentType object) {
        return null;
    }

    /**
	 * Returns the result of interpreting the object as an instance of '<em>Diagnostics Component Type</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Diagnostics Component Type</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
    public T caseDiagnosticsComponentType(DiagnosticsComponentType object) {
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
