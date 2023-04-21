package org.contract.ist.schema.ist.contract;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Agent Ref</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.contract.ist.schema.ist.contract.AgentRef#getAgentName <em>Agent Name</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.contract.ist.schema.ist.contract.ContractPackage#getAgentRef()
 * @model extendedMetaData="name='AgentRef' kind='empty'"
 * @generated
 */
public interface AgentRef extends EObject {

    /**
	 * Returns the value of the '<em><b>Agent Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Agent Name</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Agent Name</em>' attribute.
	 * @see #setAgentName(String)
	 * @see org.contract.ist.schema.ist.contract.ContractPackage#getAgentRef_AgentName()
	 * @model dataType="org.eclipse.emf.ecore.xml.type.String" required="true"
	 *        extendedMetaData="kind='attribute' name='AgentName'"
	 * @generated
	 */
    String getAgentName();

    /**
	 * Sets the value of the '{@link org.contract.ist.schema.ist.contract.AgentRef#getAgentName <em>Agent Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Agent Name</em>' attribute.
	 * @see #getAgentName()
	 * @generated
	 */
    void setAgentName(String value);
}
