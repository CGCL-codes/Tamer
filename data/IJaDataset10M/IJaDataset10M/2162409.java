package org.deft.repository.datamodel.ecoredatamodel;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Artifact Representation</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.deft.repository.datamodel.ecoredatamodel.ArtifactRepresentation#getTransformations <em>Transformations</em>}</li>
 *   <li>{@link org.deft.repository.datamodel.ecoredatamodel.ArtifactRepresentation#getBaseArtifact <em>Base Artifact</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.deft.repository.datamodel.ecoredatamodel.DatamodelPackage#getArtifactRepresentation()
 * @model
 * @generated
 */
public interface ArtifactRepresentation extends Referable {

    /**
	 * Returns the value of the '<em><b>Transformations</b></em>' reference list.
	 * The list contents are of type {@link org.deft.repository.datamodel.ecoredatamodel.Transformation}.
	 * It is bidirectional and its opposite is '{@link org.deft.repository.datamodel.ecoredatamodel.Transformation#getArtifactRepresentations <em>Artifact Representations</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Transformations</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Transformations</em>' reference list.
	 * @see org.deft.repository.datamodel.ecoredatamodel.DatamodelPackage#getArtifactRepresentation_Transformations()
	 * @see org.deft.repository.datamodel.ecoredatamodel.Transformation#getArtifactRepresentations
	 * @model opposite="artifactRepresentations" required="true"
	 * @generated
	 */
    EList<Transformation> getTransformations();

    /**
	 * Returns the value of the '<em><b>Base Artifact</b></em>' container reference.
	 * It is bidirectional and its opposite is '{@link org.deft.repository.datamodel.ecoredatamodel.Artifact#getRepresentations <em>Representations</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Base Artifact</em>' container reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Base Artifact</em>' container reference.
	 * @see #setBaseArtifact(Artifact)
	 * @see org.deft.repository.datamodel.ecoredatamodel.DatamodelPackage#getArtifactRepresentation_BaseArtifact()
	 * @see org.deft.repository.datamodel.ecoredatamodel.Artifact#getRepresentations
	 * @model opposite="representations" required="true" transient="false"
	 * @generated
	 */
    Artifact getBaseArtifact();

    /**
	 * Sets the value of the '{@link org.deft.repository.datamodel.ecoredatamodel.ArtifactRepresentation#getBaseArtifact <em>Base Artifact</em>}' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Base Artifact</em>' container reference.
	 * @see #getBaseArtifact()
	 * @generated
	 */
    void setBaseArtifact(Artifact value);
}
