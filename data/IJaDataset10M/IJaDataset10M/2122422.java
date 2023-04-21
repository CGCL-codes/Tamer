package org.pubcurator.model.overall;

import org.eclipse.emf.cdo.CDOObject;
import org.eclipse.emf.common.util.EList;
import org.pubcurator.model.projectstore.PubTopic;
import org.pubcurator.model.result.PubIdentifier;
import org.pubcurator.model.result.PubTerm;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Pub Entity</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.pubcurator.model.overall.PubEntity#getName <em>Name</em>}</li>
 *   <li>{@link org.pubcurator.model.overall.PubEntity#getCategoryName <em>Category Name</em>}</li>
 *   <li>{@link org.pubcurator.model.overall.PubEntity#getTopicLinks <em>Topic Links</em>}</li>
 *   <li>{@link org.pubcurator.model.overall.PubEntity#getIdentifiers <em>Identifiers</em>}</li>
 *   <li>{@link org.pubcurator.model.overall.PubEntity#getTerms <em>Terms</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.pubcurator.model.overall.PubOverallPackage#getPubEntity()
 * @model
 * @extends CDOObject
 * @generated
 */
public interface PubEntity extends CDOObject {

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
	 * @see org.pubcurator.model.overall.PubOverallPackage#getPubEntity_Name()
	 * @model
	 * @generated
	 */
    String getName();

    /**
	 * Sets the value of the '{@link org.pubcurator.model.overall.PubEntity#getName <em>Name</em>}' attribute.
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
	 * @see org.pubcurator.model.overall.PubOverallPackage#getPubEntity_CategoryName()
	 * @model
	 * @generated
	 */
    String getCategoryName();

    /**
	 * Sets the value of the '{@link org.pubcurator.model.overall.PubEntity#getCategoryName <em>Category Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Category Name</em>' attribute.
	 * @see #getCategoryName()
	 * @generated
	 */
    void setCategoryName(String value);

    /**
	 * Returns the value of the '<em><b>Topic Links</b></em>' reference list.
	 * The list contents are of type {@link org.pubcurator.model.projectstore.PubTopic}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Topic Links</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Topic Links</em>' reference list.
	 * @see org.pubcurator.model.overall.PubOverallPackage#getPubEntity_TopicLinks()
	 * @model
	 * @generated
	 */
    EList<PubTopic> getTopicLinks();

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
	 * @see org.pubcurator.model.overall.PubOverallPackage#getPubEntity_Identifiers()
	 * @model containment="true"
	 * @generated
	 */
    EList<PubIdentifier> getIdentifiers();

    /**
	 * Returns the value of the '<em><b>Terms</b></em>' reference list.
	 * The list contents are of type {@link org.pubcurator.model.result.PubTerm}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Terms</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Terms</em>' reference list.
	 * @see org.pubcurator.model.overall.PubOverallPackage#getPubEntity_Terms()
	 * @model
	 * @generated
	 */
    EList<PubTerm> getTerms();
}
