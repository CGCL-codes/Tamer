package bpmn;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>User Task</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link bpmn.UserTask#getInMessage <em>In Message</em>}</li>
 *   <li>{@link bpmn.UserTask#getOutMessage <em>Out Message</em>}</li>
 *   <li>{@link bpmn.UserTask#getImplementation <em>Implementation</em>}</li>
 *   <li>{@link bpmn.UserTask#getPerformers <em>Performers</em>}</li>
 * </ul>
 * </p>
 *
 * @see bpmn.BpmnPackage#getUserTask()
 * @model
 * @generated
 */
public interface UserTask extends Task, MessageReceiver, MessageSender, WebService {

    /**
	 * Returns the value of the '<em><b>In Message</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>In Message</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>In Message</em>' reference.
	 * @see #setInMessage(Message)
	 * @see bpmn.BpmnPackage#getUserTask_InMessage()
	 * @model
	 * @generated
	 */
    Message getInMessage();

    /**
	 * Sets the value of the '{@link bpmn.UserTask#getInMessage <em>In Message</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>In Message</em>' reference.
	 * @see #getInMessage()
	 * @generated
	 */
    void setInMessage(Message value);

    /**
	 * Returns the value of the '<em><b>Out Message</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Out Message</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Out Message</em>' reference.
	 * @see #setOutMessage(Message)
	 * @see bpmn.BpmnPackage#getUserTask_OutMessage()
	 * @model
	 * @generated
	 */
    Message getOutMessage();

    /**
	 * Sets the value of the '{@link bpmn.UserTask#getOutMessage <em>Out Message</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Out Message</em>' reference.
	 * @see #getOutMessage()
	 * @generated
	 */
    void setOutMessage(Message value);

    /**
	 * Returns the value of the '<em><b>Implementation</b></em>' attribute.
	 * The default value is <code>"ImplementationType.WEB_SERVICE"</code>.
	 * The literals are from the enumeration {@link bpmn.ImplementationType}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Implementation</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Implementation</em>' attribute.
	 * @see bpmn.ImplementationType
	 * @see #setImplementation(ImplementationType)
	 * @see bpmn.BpmnPackage#getUserTask_Implementation()
	 * @model default="ImplementationType.WEB_SERVICE"
	 * @generated
	 */
    ImplementationType getImplementation();

    /**
	 * Sets the value of the '{@link bpmn.UserTask#getImplementation <em>Implementation</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Implementation</em>' attribute.
	 * @see bpmn.ImplementationType
	 * @see #getImplementation()
	 * @generated
	 */
    void setImplementation(ImplementationType value);

    /**
	 * Returns the value of the '<em><b>Performers</b></em>' attribute list.
	 * The list contents are of type {@link java.lang.String}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Performers</em>' attribute list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Performers</em>' attribute list.
	 * @see bpmn.BpmnPackage#getUserTask_Performers()
	 * @model type="java.lang.String"
	 * @generated
	 */
    EList getPerformers();
}
