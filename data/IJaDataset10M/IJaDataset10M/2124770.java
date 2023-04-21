package org.dbe.accounting.metering.usagedata;

import org.eclipse.emf.ecore.EFactory;

/**
 * <!-- begin-user-doc -->
 * The <b>Factory</b> for the model.
 * It provides a create method for each non-abstract class of the model.
 * <!-- end-user-doc -->
 * @see org.dbe.accounting.metering.usagedata.UsagedataPackage
 * @generated
 */
public interface UsagedataFactory extends EFactory {

    /**
	 * The singleton instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    UsagedataFactory eINSTANCE = org.dbe.accounting.metering.usagedata.impl.UsagedataFactoryImpl.init();

    /**
	 * Returns a new object of class '<em>Document Root</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Document Root</em>'.
	 * @generated
	 */
    DocumentRoot createDocumentRoot();

    /**
	 * Returns a new object of class '<em>Message Type</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Message Type</em>'.
	 * @generated
	 */
    MessageType createMessageType();

    /**
	 * Returns a new object of class '<em>Operation Element Type</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Operation Element Type</em>'.
	 * @generated
	 */
    OperationElementType createOperationElementType();

    /**
	 * Returns a new object of class '<em>Request Type</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Request Type</em>'.
	 * @generated
	 */
    RequestType createRequestType();

    /**
	 * Returns a new object of class '<em>Response Type</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Response Type</em>'.
	 * @generated
	 */
    ResponseType createResponseType();

    /**
	 * Returns a new object of class '<em>Service Usage Type</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Service Usage Type</em>'.
	 * @generated
	 */
    ServiceUsageType createServiceUsageType();

    /**
	 * Returns the package supported by this factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the package supported by this factory.
	 * @generated
	 */
    UsagedataPackage getUsagedataPackage();
}
