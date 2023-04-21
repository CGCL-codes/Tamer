package org.eclipse.epf.uma._1._0;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.util.FeatureMap;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Process Component Interface</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * Comprises of a list of interface specifications (similar to operation declarations) that express inputs and outputs for a process component.  These interface specifications are expressed using Task Descriptors which are not linked to Tasks that are related to Work Product Descriptors as well as optional a Role Descriptor.
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.epf.uma._1._0.ProcessComponentInterface#getGroup1 <em>Group1</em>}</li>
 *   <li>{@link org.eclipse.epf.uma._1._0.ProcessComponentInterface#getInterfaceSpecification <em>Interface Specification</em>}</li>
 *   <li>{@link org.eclipse.epf.uma._1._0.ProcessComponentInterface#getInterfaceIO <em>Interface IO</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.epf.uma._1._0._0Package#getProcessComponentInterface()
 * @model extendedMetaData="name='ProcessComponentInterface' kind='elementOnly'"
 * @generated
 */
public interface ProcessComponentInterface extends BreakdownElement {

    /**
	 * Returns the value of the '<em><b>Group1</b></em>' attribute list.
	 * The list contents are of type {@link org.eclipse.emf.ecore.util.FeatureMap.Entry}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Group1</em>' attribute list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Group1</em>' attribute list.
	 * @see org.eclipse.epf.uma._1._0._0Package#getProcessComponentInterface_Group1()
	 * @model unique="false" dataType="org.eclipse.emf.ecore.EFeatureMapEntry" many="true"
	 *        extendedMetaData="kind='group' name='group:20'"
	 * @generated
	 */
    FeatureMap getGroup1();

    /**
	 * Returns the value of the '<em><b>Interface Specification</b></em>' containment reference list.
	 * The list contents are of type {@link org.eclipse.epf.uma._1._0.TaskDescriptor}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Interface Specification</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Interface Specification</em>' containment reference list.
	 * @see org.eclipse.epf.uma._1._0._0Package#getProcessComponentInterface_InterfaceSpecification()
	 * @model containment="true" transient="true" volatile="true" derived="true"
	 *        extendedMetaData="kind='element' name='InterfaceSpecification' group='#group:20'"
	 * @generated
	 */
    EList<TaskDescriptor> getInterfaceSpecification();

    /**
	 * Returns the value of the '<em><b>Interface IO</b></em>' containment reference list.
	 * The list contents are of type {@link org.eclipse.epf.uma._1._0.WorkProductDescriptor}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Interface IO</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Interface IO</em>' containment reference list.
	 * @see org.eclipse.epf.uma._1._0._0Package#getProcessComponentInterface_InterfaceIO()
	 * @model containment="true" transient="true" volatile="true" derived="true"
	 *        extendedMetaData="kind='element' name='InterfaceIO' group='#group:20'"
	 * @generated
	 */
    EList<WorkProductDescriptor> getInterfaceIO();
}
