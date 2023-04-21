package net.sf.ubq.interfaces.gui.ubqtgui.util;

import java.util.List;
import net.sf.ubq.interfaces.gui.ubqtgui.*;
import net.sf.xqz.model.cmd.Cmd;
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
 * @see net.sf.ubq.interfaces.gui.ubqtgui.UbqtguiPackage
 * @generated
 */
public class UbqtguiSwitch<T> {

    /**
	 * The cached model package
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    protected static UbqtguiPackage modelPackage;

    /**
	 * Creates an instance of the switch.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public UbqtguiSwitch() {
        if (modelPackage == null) {
            modelPackage = UbqtguiPackage.eINSTANCE;
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
            case UbqtguiPackage.UBQT_GUI_CMD:
                {
                    UbqtGuiCmd ubqtGuiCmd = (UbqtGuiCmd) theEObject;
                    T result = caseUbqtGuiCmd(ubqtGuiCmd);
                    if (result == null) result = caseCmd(ubqtGuiCmd);
                    if (result == null) result = defaultCase(theEObject);
                    return result;
                }
            case UbqtguiPackage.UBQT_GUI_PROXIMA_CMD:
                {
                    UbqtGuiProximaCmd ubqtGuiProximaCmd = (UbqtGuiProximaCmd) theEObject;
                    T result = caseUbqtGuiProximaCmd(ubqtGuiProximaCmd);
                    if (result == null) result = caseUbqtGuiCmd(ubqtGuiProximaCmd);
                    if (result == null) result = caseCmd(ubqtGuiProximaCmd);
                    if (result == null) result = defaultCase(theEObject);
                    return result;
                }
            case UbqtguiPackage.UBQT_GUI_WIDGET_CMD:
                {
                    UbqtGuiWidgetCmd ubqtGuiWidgetCmd = (UbqtGuiWidgetCmd) theEObject;
                    T result = caseUbqtGuiWidgetCmd(ubqtGuiWidgetCmd);
                    if (result == null) result = caseUbqtGuiCmd(ubqtGuiWidgetCmd);
                    if (result == null) result = caseCmd(ubqtGuiWidgetCmd);
                    if (result == null) result = defaultCase(theEObject);
                    return result;
                }
            case UbqtguiPackage.UBQT_GUI_CURSOR_CMD:
                {
                    UbqtGuiCursorCmd ubqtGuiCursorCmd = (UbqtGuiCursorCmd) theEObject;
                    T result = caseUbqtGuiCursorCmd(ubqtGuiCursorCmd);
                    if (result == null) result = caseUbqtGuiCmd(ubqtGuiCursorCmd);
                    if (result == null) result = caseCmd(ubqtGuiCursorCmd);
                    if (result == null) result = defaultCase(theEObject);
                    return result;
                }
            default:
                return defaultCase(theEObject);
        }
    }

    /**
	 * Returns the result of interpreting the object as an instance of '<em>Ubqt Gui Cmd</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Ubqt Gui Cmd</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
    public T caseUbqtGuiCmd(UbqtGuiCmd object) {
        return null;
    }

    /**
	 * Returns the result of interpreting the object as an instance of '<em>Ubqt Gui Proxima Cmd</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Ubqt Gui Proxima Cmd</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
    public T caseUbqtGuiProximaCmd(UbqtGuiProximaCmd object) {
        return null;
    }

    /**
	 * Returns the result of interpreting the object as an instance of '<em>Ubqt Gui Widget Cmd</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Ubqt Gui Widget Cmd</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
    public T caseUbqtGuiWidgetCmd(UbqtGuiWidgetCmd object) {
        return null;
    }

    /**
	 * Returns the result of interpreting the object as an instance of '<em>Ubqt Gui Cursor Cmd</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Ubqt Gui Cursor Cmd</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
    public T caseUbqtGuiCursorCmd(UbqtGuiCursorCmd object) {
        return null;
    }

    /**
	 * Returns the result of interpreting the object as an instance of '<em>Cmd</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Cmd</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
    public T caseCmd(Cmd object) {
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
