package eu.medeia.ecore.apmm.esm;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Communication Port Network Mapping</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link eu.medeia.ecore.apmm.esm.CommunicationPortNetworkMapping#getSource <em>Source</em>}</li>
 *   <li>{@link eu.medeia.ecore.apmm.esm.CommunicationPortNetworkMapping#getTarget <em>Target</em>}</li>
 * </ul>
 * </p>
 *
 * @see eu.medeia.ecore.apmm.esm.EsmPackage#getCommunicationPortNetworkMapping()
 * @model
 * @generated
 */
public interface CommunicationPortNetworkMapping extends EObject {

    /**
	 * Returns the value of the '<em><b>Source</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Source</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Source</em>' reference.
	 * @see #setSource(CommunicationPort)
	 * @see eu.medeia.ecore.apmm.esm.EsmPackage#getCommunicationPortNetworkMapping_Source()
	 * @model required="true"
	 * @generated
	 */
    CommunicationPort getSource();

    /**
	 * Sets the value of the '{@link eu.medeia.ecore.apmm.esm.CommunicationPortNetworkMapping#getSource <em>Source</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Source</em>' reference.
	 * @see #getSource()
	 * @generated
	 */
    void setSource(CommunicationPort value);

    /**
	 * Returns the value of the '<em><b>Target</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Target</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Target</em>' reference.
	 * @see #setTarget(Network)
	 * @see eu.medeia.ecore.apmm.esm.EsmPackage#getCommunicationPortNetworkMapping_Target()
	 * @model required="true"
	 * @generated
	 */
    Network getTarget();

    /**
	 * Sets the value of the '{@link eu.medeia.ecore.apmm.esm.CommunicationPortNetworkMapping#getTarget <em>Target</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Target</em>' reference.
	 * @see #getTarget()
	 * @generated
	 */
    void setTarget(Network value);
}
