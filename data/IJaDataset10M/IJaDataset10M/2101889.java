package org.pubcurator.model.overall;

import org.eclipse.emf.cdo.CDOObject;
import org.eclipse.emf.common.util.EList;
import org.pubcurator.model.projectstore.PubTopicRelation;
import org.pubcurator.model.result.PubIdentifier;
import org.pubcurator.model.result.PubTermRelation;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Pub Entity Relation</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.pubcurator.model.overall.PubEntityRelation#getName <em>Name</em>}</li>
 *   <li>{@link org.pubcurator.model.overall.PubEntityRelation#getCategoryName <em>Category Name</em>}</li>
 *   <li>{@link org.pubcurator.model.overall.PubEntityRelation#getTopicRelationLinks <em>Topic Relation Links</em>}</li>
 *   <li>{@link org.pubcurator.model.overall.PubEntityRelation#getSource <em>Source</em>}</li>
 *   <li>{@link org.pubcurator.model.overall.PubEntityRelation#getDestination <em>Destination</em>}</li>
 *   <li>{@link org.pubcurator.model.overall.PubEntityRelation#getIdentifiers <em>Identifiers</em>}</li>
 *   <li>{@link org.pubcurator.model.overall.PubEntityRelation#getTermRelations <em>Term Relations</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.pubcurator.model.overall.PubOverallPackage#getPubEntityRelation()
 * @model
 * @extends CDOObject
 * @generated
 */
public interface PubEntityRelation extends CDOObject {

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
	 * @see org.pubcurator.model.overall.PubOverallPackage#getPubEntityRelation_Name()
	 * @model
	 * @generated
	 */
    String getName();

    /**
	 * Sets the value of the '{@link org.pubcurator.model.overall.PubEntityRelation#getName <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Name</em>' attribute.
	 * @see #getName()
	 * @generated
	 */
    void setName(String value);

    /**
	 * Returns the value of the '<em><b>Category Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Category Name</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Category Name</em>' attribute.
	 * @see #setCategoryName(String)
	 * @see org.pubcurator.model.overall.PubOverallPackage#getPubEntityRelation_CategoryName()
	 * @model
	 * @generated
	 */
    String getCategoryName();

    /**
	 * Sets the value of the '{@link org.pubcurator.model.overall.PubEntityRelation#getCategoryName <em>Category Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Category Name</em>' attribute.
	 * @see #getCategoryName()
	 * @generated
	 */
    void setCategoryName(String value);

    /**
	 * Returns the value of the '<em><b>Topic Relation Links</b></em>' reference list.
	 * The list contents are of type {@link org.pubcurator.model.projectstore.PubTopicRelation}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Topic Relation Links</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Topic Relation Links</em>' reference list.
	 * @see org.pubcurator.model.overall.PubOverallPackage#getPubEntityRelation_TopicRelationLinks()
	 * @model
	 * @generated
	 */
    EList<PubTopicRelation> getTopicRelationLinks();

    /**
	 * Returns the value of the '<em><b>Source</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Source</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Source</em>' reference.
	 * @see #setSource(PubEntity)
	 * @see org.pubcurator.model.overall.PubOverallPackage#getPubEntityRelation_Source()
	 * @model
	 * @generated
	 */
    PubEntity getSource();

    /**
	 * Sets the value of the '{@link org.pubcurator.model.overall.PubEntityRelation#getSource <em>Source</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Source</em>' reference.
	 * @see #getSource()
	 * @generated
	 */
    void setSource(PubEntity value);

    /**
	 * Returns the value of the '<em><b>Destination</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Destination</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Destination</em>' reference.
	 * @see #setDestination(PubEntity)
	 * @see org.pubcurator.model.overall.PubOverallPackage#getPubEntityRelation_Destination()
	 * @model
	 * @generated
	 */
    PubEntity getDestination();

    /**
	 * Sets the value of the '{@link org.pubcurator.model.overall.PubEntityRelation#getDestination <em>Destination</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Destination</em>' reference.
	 * @see #getDestination()
	 * @generated
	 */
    void setDestination(PubEntity value);

    /**
	 * Returns the value of the '<em><b>Identifiers</b></em>' containment reference list.
	 * The list contents are of type {@link org.pubcurator.model.result.PubIdentifier}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Identifiers</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Identifiers</em>' containment reference list.
	 * @see org.pubcurator.model.overall.PubOverallPackage#getPubEntityRelation_Identifiers()
	 * @model containment="true"
	 * @generated
	 */
    EList<PubIdentifier> getIdentifiers();

    /**
	 * Returns the value of the '<em><b>Term Relations</b></em>' reference list.
	 * The list contents are of type {@link org.pubcurator.model.result.PubTermRelation}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Term Relations</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Term Relations</em>' reference list.
	 * @see org.pubcurator.model.overall.PubOverallPackage#getPubEntityRelation_TermRelations()
	 * @model
	 * @generated
	 */
    EList<PubTermRelation> getTermRelations();
}
