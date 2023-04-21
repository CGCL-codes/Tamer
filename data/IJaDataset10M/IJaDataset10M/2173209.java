package de.abg.jreichert.serviceqos.serviceQos;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Qo SAspect</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link de.abg.jreichert.serviceqos.serviceQos.QoSAspect#getName <em>Name</em>}</li>
 *   <li>{@link de.abg.jreichert.serviceqos.serviceQos.QoSAspect#getQosAspect <em>Qos Aspect</em>}</li>
 *   <li>{@link de.abg.jreichert.serviceqos.serviceQos.QoSAspect#getQosParameter <em>Qos Parameter</em>}</li>
 * </ul>
 * </p>
 *
 * @see de.abg.jreichert.serviceqos.serviceQos.ServiceQosPackage#getQoSAspect()
 * @model
 * @generated
 */
public interface QoSAspect extends EObject {

    /**
   * Returns the value of the '<em><b>Name</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Name</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Name</em>' attribute.
   * @see #setName(String)
   * @see de.abg.jreichert.serviceqos.serviceQos.ServiceQosPackage#getQoSAspect_Name()
   * @model
   * @generated
   */
    String getName();

    /**
   * Sets the value of the '{@link de.abg.jreichert.serviceqos.serviceQos.QoSAspect#getName <em>Name</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Name</em>' attribute.
   * @see #getName()
   * @generated
   */
    void setName(String value);

    /**
   * Returns the value of the '<em><b>Qos Aspect</b></em>' containment reference list.
   * The list contents are of type {@link de.abg.jreichert.serviceqos.serviceQos.QoSAspect}.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Qos Aspect</em>' containment reference list isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Qos Aspect</em>' containment reference list.
   * @see de.abg.jreichert.serviceqos.serviceQos.ServiceQosPackage#getQoSAspect_QosAspect()
   * @model containment="true"
   * @generated
   */
    EList<QoSAspect> getQosAspect();

    /**
   * Returns the value of the '<em><b>Qos Parameter</b></em>' containment reference list.
   * The list contents are of type {@link de.abg.jreichert.serviceqos.serviceQos.QoSParameter}.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Qos Parameter</em>' containment reference list isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Qos Parameter</em>' containment reference list.
   * @see de.abg.jreichert.serviceqos.serviceQos.ServiceQosPackage#getQoSAspect_QosParameter()
   * @model containment="true"
   * @generated
   */
    EList<QoSParameter> getQosParameter();
}
