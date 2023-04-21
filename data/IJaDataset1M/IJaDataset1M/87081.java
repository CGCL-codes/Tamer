package M3Actions.Debug;

import M3Actions.MActivityNode;
import org.eclipse.emf.ecore.ENamedElement;
import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc --> A representation of the model object '<em><b>MBreakpoint</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link M3Actions.Debug.MBreakpoint#getExpression <em>Expression</em>}</li>
 *   <li>{@link M3Actions.Debug.MBreakpoint#getActivityNode <em>Activity Node</em>}</li>
 *   <li>{@link M3Actions.Debug.MBreakpoint#isActivated <em>Activated</em>}</li>
 * </ul>
 * </p>
 *
 * @see M3Actions.Debug.DebugPackage#getMBreakpoint()
 * @model
 * @generated
 */
public interface MBreakpoint extends EObject {

    /**
	 * Returns the value of the '<em><b>Expression</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Expression</em>' attribute isn't clear, there really should be
	 * more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Expression</em>' attribute.
	 * @see #setExpression(String)
	 * @see M3Actions.Debug.DebugPackage#getMBreakpoint_Expression()
	 * @model
	 * @generated
	 */
    String getExpression();

    /**
	 * Sets the value of the '{@link M3Actions.Debug.MBreakpoint#getExpression <em>Expression</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @param value the new value of the '<em>Expression</em>' attribute.
	 * @see #getExpression()
	 * @generated
	 */
    void setExpression(String value);

    /**
	 * Returns the value of the '<em><b>Activity Node</b></em>' container reference.
	 * It is bidirectional and its opposite is '{@link M3Actions.MActivityNode#getBreakpoints <em>Breakpoints</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Activity Node</em>' reference isn't clear, there really should
	 * be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Activity Node</em>' container reference.
	 * @see #setActivityNode(MActivityNode)
	 * @see M3Actions.Debug.DebugPackage#getMBreakpoint_ActivityNode()
	 * @see M3Actions.MActivityNode#getBreakpoints
	 * @model opposite="breakpoints" required="true" transient="false"
	 * @generated
	 */
    MActivityNode getActivityNode();

    /**
	 * Sets the value of the '{@link M3Actions.Debug.MBreakpoint#getActivityNode <em>Activity Node</em>}' container reference.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @param value the new value of the '<em>Activity Node</em>' container reference.
	 * @see #getActivityNode()
	 * @generated
	 */
    void setActivityNode(MActivityNode value);

    /**
	 * Returns the value of the '<em><b>Activated</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Activated</em>' attribute isn't clear, there really should be
	 * more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Activated</em>' attribute.
	 * @see #setActivated(boolean)
	 * @see M3Actions.Debug.DebugPackage#getMBreakpoint_Activated()
	 * @model required="true"
	 * @generated
	 */
    boolean isActivated();

    /**
	 * Sets the value of the '{@link M3Actions.Debug.MBreakpoint#isActivated <em>Activated</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @param value the new value of the '<em>Activated</em>' attribute.
	 * @see #isActivated()
	 * @generated
	 */
    void setActivated(boolean value);
}
