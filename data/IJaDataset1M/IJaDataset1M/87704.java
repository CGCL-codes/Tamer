package org.deft.repository.datamodel.ecoredatamodel;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Artifact</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.deft.repository.datamodel.ecoredatamodel.Artifact#getSourceId <em>Source Id</em>}</li>
 *   <li>{@link org.deft.repository.datamodel.ecoredatamodel.Artifact#getArtifactType <em>Artifact Type</em>}</li>
 *   <li>{@link org.deft.repository.datamodel.ecoredatamodel.Artifact#getArtifactReferences <em>Artifact References</em>}</li>
 *   <li>{@link org.deft.repository.datamodel.ecoredatamodel.Artifact#getRevisions <em>Revisions</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.deft.repository.datamodel.ecoredatamodel.DatamodelPackage#getArtifact()
 * @model
 * @generated
 */
public interface Artifact extends Fragment {

    /**
	 * Returns the value of the '<em><b>Source Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Source Id</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Source Id</em>' attribute.
	 * @see #setSourceId(String)
	 * @see org.deft.repository.datamodel.ecoredatamodel.DatamodelPackage#getArtifact_SourceId()
	 * @model required="true"
	 * @generated
	 */
    String getSourceId();

    /**
	 * Sets the value of the '{@link org.deft.repository.datamodel.ecoredatamodel.Artifact#getSourceId <em>Source Id</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Source Id</em>' attribute.
	 * @see #getSourceId()
	 * @generated
	 */
    void setSourceId(String value);

    /**
	 * Returns the value of the '<em><b>Artifact Type</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Artifact Type</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Artifact Type</em>' attribute.
	 * @see #setArtifactType(String)
	 * @see org.deft.repository.datamodel.ecoredatamodel.DatamodelPackage#getArtifact_ArtifactType()
	 * @model required="true"
	 * @generated
	 */
    String getArtifactType();

    /**
	 * Sets the value of the '{@link org.deft.repository.datamodel.ecoredatamodel.Artifact#getArtifactType <em>Artifact Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Artifact Type</em>' attribute.
	 * @see #getArtifactType()
	 * @generated
	 */
    void setArtifactType(String value);

    /**
	 * Returns the value of the '<em><b>Artifact References</b></em>' reference list.
	 * The list contents are of type {@link org.deft.repository.datamodel.ecoredatamodel.ArtifactReference}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Artifact References</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Artifact References</em>' reference list.
	 * @see org.deft.repository.datamodel.ecoredatamodel.DatamodelPackage#getArtifact_ArtifactReferences()
	 * @model
	 * @generated
	 */
    EList<ArtifactReference> getArtifactReferences();

    /**
	 * Returns the value of the '<em><b>Revisions</b></em>' containment reference list.
	 * The list contents are of type {@link org.deft.repository.datamodel.ecoredatamodel.Revision}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Revisions</em>' attribute list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Revisions</em>' containment reference list.
	 * @see org.deft.repository.datamodel.ecoredatamodel.DatamodelPackage#getArtifact_Revisions()
	 * @model containment="true" required="true"
	 * @generated
	 */
    EList<Revision> getRevisions();
}
