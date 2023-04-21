package org.xtext.example.swrtj;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Dotted Expression</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.xtext.example.swrtj.DottedExpression#getStart <em>Start</em>}</li>
 *   <li>{@link org.xtext.example.swrtj.DottedExpression#getReceiver <em>Receiver</em>}</li>
 *   <li>{@link org.xtext.example.swrtj.DottedExpression#getMessage <em>Message</em>}</li>
 *   <li>{@link org.xtext.example.swrtj.DottedExpression#getValue <em>Value</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.xtext.example.swrtj.SwrtjPackage#getDottedExpression()
 * @model
 * @generated
 */
public interface DottedExpression extends EObject {

    /**
   * Returns the value of the '<em><b>Start</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Start</em>' containment reference isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Start</em>' containment reference.
   * @see #setStart(Start)
   * @see org.xtext.example.swrtj.SwrtjPackage#getDottedExpression_Start()
   * @model containment="true"
   * @generated
   */
    Start getStart();

    /**
   * Sets the value of the '{@link org.xtext.example.swrtj.DottedExpression#getStart <em>Start</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Start</em>' containment reference.
   * @see #getStart()
   * @generated
   */
    void setStart(Start value);

    /**
   * Returns the value of the '<em><b>Receiver</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Receiver</em>' containment reference isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Receiver</em>' containment reference.
   * @see #setReceiver(DottedExpression)
   * @see org.xtext.example.swrtj.SwrtjPackage#getDottedExpression_Receiver()
   * @model containment="true"
   * @generated
   */
    DottedExpression getReceiver();

    /**
   * Sets the value of the '{@link org.xtext.example.swrtj.DottedExpression#getReceiver <em>Receiver</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Receiver</em>' containment reference.
   * @see #getReceiver()
   * @generated
   */
    void setReceiver(DottedExpression value);

    /**
   * Returns the value of the '<em><b>Message</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Message</em>' containment reference isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Message</em>' containment reference.
   * @see #setMessage(Message)
   * @see org.xtext.example.swrtj.SwrtjPackage#getDottedExpression_Message()
   * @model containment="true"
   * @generated
   */
    Message getMessage();

    /**
   * Sets the value of the '{@link org.xtext.example.swrtj.DottedExpression#getMessage <em>Message</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Message</em>' containment reference.
   * @see #getMessage()
   * @generated
   */
    void setMessage(Message value);

    /**
   * Returns the value of the '<em><b>Value</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Value</em>' containment reference isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Value</em>' containment reference.
   * @see #setValue(GenericExpression)
   * @see org.xtext.example.swrtj.SwrtjPackage#getDottedExpression_Value()
   * @model containment="true"
   * @generated
   */
    GenericExpression getValue();

    /**
   * Sets the value of the '{@link org.xtext.example.swrtj.DottedExpression#getValue <em>Value</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Value</em>' containment reference.
   * @see #getValue()
   * @generated
   */
    void setValue(GenericExpression value);
}
