package org.eclipse.uml2.uml;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Directed Relationship</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * A directed relationship represents a relationship between a collection of source model elements and a collection of target model elements.
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.uml2.uml.DirectedRelationship#getSources <em>Source</em>}</li>
 *   <li>{@link org.eclipse.uml2.uml.DirectedRelationship#getTargets <em>Target</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.uml2.uml.UMLPackage#getDirectedRelationship()
 * @model abstract="true"
 * @generated
 */
public interface DirectedRelationship extends Relationship {

    /**
	 * Returns the value of the '<em><b>Source</b></em>' reference list.
	 * The list contents are of type {@link org.eclipse.uml2.uml.Element}.
	 * <p>
	 * This feature subsets the following features:
	 * <ul>
	 *   <li>'{@link org.eclipse.uml2.uml.Relationship#getRelatedElements() <em>Related Element</em>}'</li>
	 * </ul>
	 * </p>
	 * This feature is a derived union.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Specifies the sources of the DirectedRelationship.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Source</em>' reference list.
	 * @see org.eclipse.uml2.uml.UMLPackage#getDirectedRelationship_Source()
	 * @model required="true" transient="true" changeable="false" volatile="true" derived="true" ordered="false"
	 * @generated
	 */
    EList<Element> getSources();

    /**
	 * Returns the value of the '<em><b>Target</b></em>' reference list.
	 * The list contents are of type {@link org.eclipse.uml2.uml.Element}.
	 * <p>
	 * This feature subsets the following features:
	 * <ul>
	 *   <li>'{@link org.eclipse.uml2.uml.Relationship#getRelatedElements() <em>Related Element</em>}'</li>
	 * </ul>
	 * </p>
	 * This feature is a derived union.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Specifies the targets of the DirectedRelationship.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Target</em>' reference list.
	 * @see org.eclipse.uml2.uml.UMLPackage#getDirectedRelationship_Target()
	 * @model required="true" transient="true" changeable="false" volatile="true" derived="true" ordered="false"
	 * @generated
	 */
    EList<Element> getTargets();
}
