package org.modelversioning.operations.detection.operationoccurrence;

import org.eclipse.emf.compare.diff.metamodel.DiffPackage;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

/**
 * <!-- begin-user-doc -->
 * The <b>Package</b> for the model.
 * It contains accessors for the meta objects to represent
 * <ul>
 *   <li>each class,</li>
 *   <li>each feature of each class,</li>
 *   <li>each enum,</li>
 *   <li>and each data type</li>
 * </ul>
 * <!-- end-user-doc -->
 * @see org.modelversioning.operations.detection.operationoccurrence.OperationoccurrenceFactory
 * @model kind="package"
 * @generated
 */
public interface OperationoccurrencePackage extends EPackage {

    /**
	 * The package name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    String eNAME = "operationoccurrence";

    /**
	 * The package namespace URI.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    String eNS_URI = "http://modelversioning.org/core/operations/occurrence/metamodel/1.0";

    /**
	 * The package namespace name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    String eNS_PREFIX = "operationoccurrence";

    /**
	 * The singleton instance of the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    OperationoccurrencePackage eINSTANCE = org.modelversioning.operations.detection.operationoccurrence.impl.OperationoccurrencePackageImpl.init();

    /**
	 * The meta object id for the '{@link org.modelversioning.operations.detection.operationoccurrence.impl.OperationOccurrenceImpl <em>Operation Occurrence</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.modelversioning.operations.detection.operationoccurrence.impl.OperationOccurrenceImpl
	 * @see org.modelversioning.operations.detection.operationoccurrence.impl.OperationoccurrencePackageImpl#getOperationOccurrence()
	 * @generated
	 */
    int OPERATION_OCCURRENCE = 0;

    /**
	 * The feature id for the '<em><b>Sub Diff Elements</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int OPERATION_OCCURRENCE__SUB_DIFF_ELEMENTS = DiffPackage.DIFF_ELEMENT__SUB_DIFF_ELEMENTS;

    /**
	 * The feature id for the '<em><b>Is Hidden By</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int OPERATION_OCCURRENCE__IS_HIDDEN_BY = DiffPackage.DIFF_ELEMENT__IS_HIDDEN_BY;

    /**
	 * The feature id for the '<em><b>Conflicting</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int OPERATION_OCCURRENCE__CONFLICTING = DiffPackage.DIFF_ELEMENT__CONFLICTING;

    /**
	 * The feature id for the '<em><b>Kind</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int OPERATION_OCCURRENCE__KIND = DiffPackage.DIFF_ELEMENT__KIND;

    /**
	 * The feature id for the '<em><b>Remote</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int OPERATION_OCCURRENCE__REMOTE = DiffPackage.DIFF_ELEMENT__REMOTE;

    /**
	 * The feature id for the '<em><b>Hide Elements</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int OPERATION_OCCURRENCE__HIDE_ELEMENTS = DiffPackage.DIFF_ELEMENT_FEATURE_COUNT + 0;

    /**
	 * The feature id for the '<em><b>Is Collapsed</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int OPERATION_OCCURRENCE__IS_COLLAPSED = DiffPackage.DIFF_ELEMENT_FEATURE_COUNT + 1;

    /**
	 * The feature id for the '<em><b>Pre Condition Binding</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int OPERATION_OCCURRENCE__PRE_CONDITION_BINDING = DiffPackage.DIFF_ELEMENT_FEATURE_COUNT + 2;

    /**
	 * The feature id for the '<em><b>Applied Operation Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int OPERATION_OCCURRENCE__APPLIED_OPERATION_ID = DiffPackage.DIFF_ELEMENT_FEATURE_COUNT + 3;

    /**
	 * The feature id for the '<em><b>Title</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int OPERATION_OCCURRENCE__TITLE = DiffPackage.DIFF_ELEMENT_FEATURE_COUNT + 4;

    /**
	 * The feature id for the '<em><b>Applied Operation Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int OPERATION_OCCURRENCE__APPLIED_OPERATION_NAME = DiffPackage.DIFF_ELEMENT_FEATURE_COUNT + 5;

    /**
	 * The feature id for the '<em><b>Applied Operation</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int OPERATION_OCCURRENCE__APPLIED_OPERATION = DiffPackage.DIFF_ELEMENT_FEATURE_COUNT + 6;

    /**
	 * The feature id for the '<em><b>Post Condition Binding</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int OPERATION_OCCURRENCE__POST_CONDITION_BINDING = DiffPackage.DIFF_ELEMENT_FEATURE_COUNT + 7;

    /**
	 * The feature id for the '<em><b>Hidden Changes</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int OPERATION_OCCURRENCE__HIDDEN_CHANGES = DiffPackage.DIFF_ELEMENT_FEATURE_COUNT + 8;

    /**
	 * The feature id for the '<em><b>Order Hint</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int OPERATION_OCCURRENCE__ORDER_HINT = DiffPackage.DIFF_ELEMENT_FEATURE_COUNT + 9;

    /**
	 * The number of structural features of the '<em>Operation Occurrence</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int OPERATION_OCCURRENCE_FEATURE_COUNT = DiffPackage.DIFF_ELEMENT_FEATURE_COUNT + 10;

    /**
	 * Returns the meta object for class '{@link org.modelversioning.operations.detection.operationoccurrence.OperationOccurrence <em>Operation Occurrence</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Operation Occurrence</em>'.
	 * @see org.modelversioning.operations.detection.operationoccurrence.OperationOccurrence
	 * @generated
	 */
    EClass getOperationOccurrence();

    /**
	 * Returns the meta object for the containment reference '{@link org.modelversioning.operations.detection.operationoccurrence.OperationOccurrence#getPreConditionBinding <em>Pre Condition Binding</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Pre Condition Binding</em>'.
	 * @see org.modelversioning.operations.detection.operationoccurrence.OperationOccurrence#getPreConditionBinding()
	 * @see #getOperationOccurrence()
	 * @generated
	 */
    EReference getOperationOccurrence_PreConditionBinding();

    /**
	 * Returns the meta object for the attribute '{@link org.modelversioning.operations.detection.operationoccurrence.OperationOccurrence#getAppliedOperationId <em>Applied Operation Id</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Applied Operation Id</em>'.
	 * @see org.modelversioning.operations.detection.operationoccurrence.OperationOccurrence#getAppliedOperationId()
	 * @see #getOperationOccurrence()
	 * @generated
	 */
    EAttribute getOperationOccurrence_AppliedOperationId();

    /**
	 * Returns the meta object for the attribute '{@link org.modelversioning.operations.detection.operationoccurrence.OperationOccurrence#getTitle <em>Title</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Title</em>'.
	 * @see org.modelversioning.operations.detection.operationoccurrence.OperationOccurrence#getTitle()
	 * @see #getOperationOccurrence()
	 * @generated
	 */
    EAttribute getOperationOccurrence_Title();

    /**
	 * Returns the meta object for the attribute '{@link org.modelversioning.operations.detection.operationoccurrence.OperationOccurrence#getAppliedOperationName <em>Applied Operation Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Applied Operation Name</em>'.
	 * @see org.modelversioning.operations.detection.operationoccurrence.OperationOccurrence#getAppliedOperationName()
	 * @see #getOperationOccurrence()
	 * @generated
	 */
    EAttribute getOperationOccurrence_AppliedOperationName();

    /**
	 * Returns the meta object for the reference '{@link org.modelversioning.operations.detection.operationoccurrence.OperationOccurrence#getAppliedOperation <em>Applied Operation</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Applied Operation</em>'.
	 * @see org.modelversioning.operations.detection.operationoccurrence.OperationOccurrence#getAppliedOperation()
	 * @see #getOperationOccurrence()
	 * @generated
	 */
    EReference getOperationOccurrence_AppliedOperation();

    /**
	 * Returns the meta object for the containment reference '{@link org.modelversioning.operations.detection.operationoccurrence.OperationOccurrence#getPostConditionBinding <em>Post Condition Binding</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Post Condition Binding</em>'.
	 * @see org.modelversioning.operations.detection.operationoccurrence.OperationOccurrence#getPostConditionBinding()
	 * @see #getOperationOccurrence()
	 * @generated
	 */
    EReference getOperationOccurrence_PostConditionBinding();

    /**
	 * Returns the meta object for the reference list '{@link org.modelversioning.operations.detection.operationoccurrence.OperationOccurrence#getHiddenChanges <em>Hidden Changes</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Hidden Changes</em>'.
	 * @see org.modelversioning.operations.detection.operationoccurrence.OperationOccurrence#getHiddenChanges()
	 * @see #getOperationOccurrence()
	 * @generated
	 */
    EReference getOperationOccurrence_HiddenChanges();

    /**
	 * Returns the meta object for the attribute '{@link org.modelversioning.operations.detection.operationoccurrence.OperationOccurrence#getOrderHint <em>Order Hint</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Order Hint</em>'.
	 * @see org.modelversioning.operations.detection.operationoccurrence.OperationOccurrence#getOrderHint()
	 * @see #getOperationOccurrence()
	 * @generated
	 */
    EAttribute getOperationOccurrence_OrderHint();

    /**
	 * Returns the factory that creates the instances of the model.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
    OperationoccurrenceFactory getOperationoccurrenceFactory();

    /**
	 * <!-- begin-user-doc -->
	 * Defines literals for the meta objects that represent
	 * <ul>
	 *   <li>each class,</li>
	 *   <li>each feature of each class,</li>
	 *   <li>each enum,</li>
	 *   <li>and each data type</li>
	 * </ul>
	 * <!-- end-user-doc -->
	 * @generated
	 */
    interface Literals {

        /**
		 * The meta object literal for the '{@link org.modelversioning.operations.detection.operationoccurrence.impl.OperationOccurrenceImpl <em>Operation Occurrence</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.modelversioning.operations.detection.operationoccurrence.impl.OperationOccurrenceImpl
		 * @see org.modelversioning.operations.detection.operationoccurrence.impl.OperationoccurrencePackageImpl#getOperationOccurrence()
		 * @generated
		 */
        EClass OPERATION_OCCURRENCE = eINSTANCE.getOperationOccurrence();

        /**
		 * The meta object literal for the '<em><b>Pre Condition Binding</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
        EReference OPERATION_OCCURRENCE__PRE_CONDITION_BINDING = eINSTANCE.getOperationOccurrence_PreConditionBinding();

        /**
		 * The meta object literal for the '<em><b>Applied Operation Id</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
        EAttribute OPERATION_OCCURRENCE__APPLIED_OPERATION_ID = eINSTANCE.getOperationOccurrence_AppliedOperationId();

        /**
		 * The meta object literal for the '<em><b>Title</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
        EAttribute OPERATION_OCCURRENCE__TITLE = eINSTANCE.getOperationOccurrence_Title();

        /**
		 * The meta object literal for the '<em><b>Applied Operation Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
        EAttribute OPERATION_OCCURRENCE__APPLIED_OPERATION_NAME = eINSTANCE.getOperationOccurrence_AppliedOperationName();

        /**
		 * The meta object literal for the '<em><b>Applied Operation</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
        EReference OPERATION_OCCURRENCE__APPLIED_OPERATION = eINSTANCE.getOperationOccurrence_AppliedOperation();

        /**
		 * The meta object literal for the '<em><b>Post Condition Binding</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
        EReference OPERATION_OCCURRENCE__POST_CONDITION_BINDING = eINSTANCE.getOperationOccurrence_PostConditionBinding();

        /**
		 * The meta object literal for the '<em><b>Hidden Changes</b></em>' reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
        EReference OPERATION_OCCURRENCE__HIDDEN_CHANGES = eINSTANCE.getOperationOccurrence_HiddenChanges();

        /**
		 * The meta object literal for the '<em><b>Order Hint</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
        EAttribute OPERATION_OCCURRENCE__ORDER_HINT = eINSTANCE.getOperationOccurrence_OrderHint();
    }
}
