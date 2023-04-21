package org.modelversioning.operations.detection.operationoccurrence;

import org.eclipse.emf.ecore.EFactory;

/**
 * <!-- begin-user-doc -->
 * The <b>Factory</b> for the model.
 * It provides a create method for each non-abstract class of the model.
 * <!-- end-user-doc -->
 * @see org.modelversioning.operations.detection.operationoccurrence.OperationoccurrencePackage
 * @generated
 */
public interface OperationoccurrenceFactory extends EFactory {

    /**
	 * The singleton instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    OperationoccurrenceFactory eINSTANCE = org.modelversioning.operations.detection.operationoccurrence.impl.OperationoccurrenceFactoryImpl.init();

    /**
	 * Returns a new object of class '<em>Operation Occurrence</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Operation Occurrence</em>'.
	 * @generated
	 */
    OperationOccurrence createOperationOccurrence();

    /**
	 * Returns the package supported by this factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the package supported by this factory.
	 * @generated
	 */
    OperationoccurrencePackage getOperationoccurrencePackage();
}
