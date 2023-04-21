package de.abg.jreichert.serviceqos.model.wsdl;

import javax.xml.namespace.QName;
import org.eclipse.emf.ecore.util.FeatureMap;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Binding Operation Fault Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link de.abg.jreichert.serviceqos.model.wsdl.BindingOperationFaultType#getGroup <em>Group</em>}</li>
 *   <li>{@link de.abg.jreichert.serviceqos.model.wsdl.BindingOperationFaultType#getAny <em>Any</em>}</li>
 *   <li>{@link de.abg.jreichert.serviceqos.model.wsdl.BindingOperationFaultType#getMessageLabel <em>Message Label</em>}</li>
 *   <li>{@link de.abg.jreichert.serviceqos.model.wsdl.BindingOperationFaultType#getRef <em>Ref</em>}</li>
 * </ul>
 * </p>
 *
 * @see de.abg.jreichert.serviceqos.model.wsdl.WsdlPackage#getBindingOperationFaultType()
 * @model extendedMetaData="name='BindingOperationFaultType' kind='elementOnly'"
 * @generated
 */
public interface BindingOperationFaultType extends ExtensibleDocumentedType {

    /**
	 * Returns the value of the '<em><b>Group</b></em>' attribute list.
	 * The list contents are of type {@link org.eclipse.emf.ecore.util.FeatureMap.Entry}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Group</em>' attribute list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Group</em>' attribute list.
	 * @see de.abg.jreichert.serviceqos.model.wsdl.WsdlPackage#getBindingOperationFaultType_Group()
	 * @model unique="false" dataType="org.eclipse.emf.ecore.EFeatureMapEntry" many="true"
	 *        extendedMetaData="kind='group' name='group:2'"
	 * @generated
	 */
    FeatureMap getGroup();

    /**
	 * Returns the value of the '<em><b>Any</b></em>' attribute list.
	 * The list contents are of type {@link org.eclipse.emf.ecore.util.FeatureMap.Entry}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Any</em>' attribute list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Any</em>' attribute list.
	 * @see de.abg.jreichert.serviceqos.model.wsdl.WsdlPackage#getBindingOperationFaultType_Any()
	 * @model unique="false" dataType="org.eclipse.emf.ecore.EFeatureMapEntry" many="true" transient="true" volatile="true" derived="true"
	 *        extendedMetaData="kind='elementWildcard' wildcards='##other' name=':3' processing='lax' group='#group:2'"
	 * @generated
	 */
    FeatureMap getAny();

    /**
	 * Returns the value of the '<em><b>Message Label</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Message Label</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Message Label</em>' attribute.
	 * @see #setMessageLabel(String)
	 * @see de.abg.jreichert.serviceqos.model.wsdl.WsdlPackage#getBindingOperationFaultType_MessageLabel()
	 * @model dataType="org.eclipse.emf.ecore.xml.type.NCName"
	 *        extendedMetaData="kind='attribute' name='messageLabel'"
	 * @generated
	 */
    String getMessageLabel();

    /**
	 * Sets the value of the '{@link de.abg.jreichert.serviceqos.model.wsdl.BindingOperationFaultType#getMessageLabel <em>Message Label</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Message Label</em>' attribute.
	 * @see #getMessageLabel()
	 * @generated
	 */
    void setMessageLabel(String value);

    /**
	 * Returns the value of the '<em><b>Ref</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Ref</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Ref</em>' attribute.
	 * @see #setRef(QName)
	 * @see de.abg.jreichert.serviceqos.model.wsdl.WsdlPackage#getBindingOperationFaultType_Ref()
	 * @model dataType="org.eclipse.emf.ecore.xml.type.QName" required="true"
	 *        extendedMetaData="kind='attribute' name='ref'"
	 * @generated
	 */
    QName getRef();

    /**
	 * Sets the value of the '{@link de.abg.jreichert.serviceqos.model.wsdl.BindingOperationFaultType#getRef <em>Ref</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Ref</em>' attribute.
	 * @see #getRef()
	 * @generated
	 */
    void setRef(QName value);
}
