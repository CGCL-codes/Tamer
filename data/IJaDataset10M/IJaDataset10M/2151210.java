package hub.metrik.lang.dtmfcontrol;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Application</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link hub.metrik.lang.dtmfcontrol.Application#getBlocks <em>Blocks</em>}</li>
 *   <li>{@link hub.metrik.lang.dtmfcontrol.Application#getCurrentKeyPress <em>Current Key Press</em>}</li>
 *   <li>{@link hub.metrik.lang.dtmfcontrol.Application#getNextCommand <em>Next Command</em>}</li>
 *   <li>{@link hub.metrik.lang.dtmfcontrol.Application#getKeyPresses <em>Key Presses</em>}</li>
 *   <li>{@link hub.metrik.lang.dtmfcontrol.Application#getOutputs <em>Outputs</em>}</li>
 *   <li>{@link hub.metrik.lang.dtmfcontrol.Application#isWaiting <em>Waiting</em>}</li>
 * </ul>
 * </p>
 *
 * @see hub.metrik.lang.dtmfcontrol.DtmfcontrolPackage#getApplication()
 * @model
 * @generated
 */
public interface Application extends EObject {

    /**
	 * Returns the value of the '<em><b>Blocks</b></em>' containment reference list.
	 * The list contents are of type {@link hub.metrik.lang.dtmfcontrol.Block}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Blocks</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Blocks</em>' containment reference list.
	 * @see hub.metrik.lang.dtmfcontrol.DtmfcontrolPackage#getApplication_Blocks()
	 * @model containment="true" required="true"
	 * @generated
	 */
    EList<Block> getBlocks();

    /**
	 * Returns the value of the '<em><b>Current Key Press</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Current Key Press</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Current Key Press</em>' attribute.
	 * @see #setCurrentKeyPress(int)
	 * @see hub.metrik.lang.dtmfcontrol.DtmfcontrolPackage#getApplication_CurrentKeyPress()
	 * @model
	 * @generated
	 */
    int getCurrentKeyPress();

    /**
	 * Sets the value of the '{@link hub.metrik.lang.dtmfcontrol.Application#getCurrentKeyPress <em>Current Key Press</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Current Key Press</em>' attribute.
	 * @see #getCurrentKeyPress()
	 * @generated
	 */
    void setCurrentKeyPress(int value);

    /**
	 * Returns the value of the '<em><b>Next Command</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Next Command</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Next Command</em>' reference.
	 * @see #setNextCommand(Command)
	 * @see hub.metrik.lang.dtmfcontrol.DtmfcontrolPackage#getApplication_NextCommand()
	 * @model transient="true" volatile="true" derived="true"
	 * @generated
	 */
    Command getNextCommand();

    /**
	 * Sets the value of the '{@link hub.metrik.lang.dtmfcontrol.Application#getNextCommand <em>Next Command</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Next Command</em>' reference.
	 * @see #getNextCommand()
	 * @generated
	 */
    void setNextCommand(Command value);

    /**
	 * Returns the value of the '<em><b>Key Presses</b></em>' containment reference list.
	 * The list contents are of type {@link hub.metrik.lang.dtmfcontrol.KeyPress}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Key Presses</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Key Presses</em>' containment reference list.
	 * @see hub.metrik.lang.dtmfcontrol.DtmfcontrolPackage#getApplication_KeyPresses()
	 * @model containment="true"
	 * @generated
	 */
    EList<KeyPress> getKeyPresses();

    /**
	 * Returns the value of the '<em><b>Outputs</b></em>' containment reference list.
	 * The list contents are of type {@link hub.metrik.lang.dtmfcontrol.Output}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Outputs</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Outputs</em>' containment reference list.
	 * @see hub.metrik.lang.dtmfcontrol.DtmfcontrolPackage#getApplication_Outputs()
	 * @model containment="true"
	 * @generated
	 */
    EList<Output> getOutputs();

    /**
	 * Returns the value of the '<em><b>Waiting</b></em>' attribute.
	 * The default value is <code>"false"</code>.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Waiting</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Waiting</em>' attribute.
	 * @see #setWaiting(boolean)
	 * @see hub.metrik.lang.dtmfcontrol.DtmfcontrolPackage#getApplication_Waiting()
	 * @model default="false"
	 * @generated
	 */
    boolean isWaiting();

    /**
	 * Sets the value of the '{@link hub.metrik.lang.dtmfcontrol.Application#isWaiting <em>Waiting</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Waiting</em>' attribute.
	 * @see #isWaiting()
	 * @generated
	 */
    void setWaiting(boolean value);
}
